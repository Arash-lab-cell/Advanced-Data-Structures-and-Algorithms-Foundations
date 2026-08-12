# First Missing Integer

## Problem Statement

Given an unsorted integer array `A` of size `N`, find the first missing positive integer, in `O(N)` time and `O(1)` extra space.

**Constraints:** `1 <= N <= 1000000`, `-10^9 <= A[i] <= 10^9`

## Example

```
A = [3, 4, -1, 1]
```

Positives present: `{1, 3, 4}`. `1` is present, `2` is not → the answer is **2**.

## Approach

This is **cyclic sort / in-place index marking**. The key observation: the answer is always in the range `[1, N+1]` — if all of `1..N` are present, the answer is `N+1`; otherwise it's the smallest missing value in `1..N`. That means the array itself, indexed `0..N-1`, has exactly enough room to record "have I seen value `k`?" for every candidate `k` in `1..N`, using the array's own cells as a presence-bitmap.

Instead of a separate boolean array, the sign bit of each cell is repurposed: negative means "the value at this index+1 was seen somewhere in the array." Values that can't possibly be in `[1, N]` (i.e. `<= 0` or `> N`) are first neutralized so they don't corrupt this marking.

**Trigger to recognize this pattern:** "array of size N, find the missing/duplicate value in `[1, N]` range, O(N) time, O(1) space" — that space constraint rules out a hash set and pushes toward using array indices themselves as the presence structure.

## Algorithm

1. Replace every `A[i] <= 0` with a sentinel `n + 2` (guaranteed out of the `[1, n]` marking range, and distinct from any valid negative marker created in step 2).
2. For each `i`, let `v = abs(A[i])`. If `v <= n`, negate `A[v-1]` (using `abs` first, so re-marking an already-negative cell doesn't flip it back positive).
3. Scan left to right; the first index `i` where `A[i] > 0` means value `i+1` was never marked as seen — return `i + 1`.
4. If every cell ended up negative, all of `1..n` are present — return `n + 1`.

## Dry Run

`A = [3, 4, -1, 1]`, `n = 4`.

**Step 1 — neutralize non-positives:**
```
i=2: A[2] = -1 <= 0 -> A[2] = n+2 = 6
A = [3, 4, 6, 1]
```

**Step 2 — mark index `abs(A[i])-1` negative when `abs(A[i]) <= 4`:**

| i | A[i] | abs <= 4? | Mark | A after |
|---|---|---|---|---|
| 0 | 3 | yes | `A[2] = -6` | `[3, 4, -6, 1]` |
| 1 | 4 | yes | `A[3] = -1` | `[3, 4, -6, -1]` |
| 2 | -6 | no (6 > 4) | skip | `[3, 4, -6, -1]` |
| 3 | -1 | yes | `A[0] = -3` | `[-3, 4, -6, -1]` |

**Step 3 — first positive cell:**
```
i=0: A[0]=-3, not > 0
i=1: A[1]=4, > 0 -> return 1+1 = 2
```

**Result: 2** — matches the expected answer.

## Why Does This Work?

The answer can never exceed `n+1`, so only values in `[1, n]` are relevant — anything outside that range is noise that must be prevented from interfering with the marking scheme. Sending non-positives to `n+2` guarantees they land outside `[1, n]` and outside the range of any legitimate negative marker, so the `abs(A[i]) <= n` check cleanly separates "a real candidate value" from "already-processed marker or irrelevant garbage."

After marking, `A[i] > 0` at position `i` means "no element in the original array equaled `i+1`" — because if one had, step 2 would have negated it. The first such gap, scanned left to right, is by definition the smallest missing positive integer.

## Complexity Analysis

- **Time:** O(N) — three separate single passes over the array, each O(N), no nesting.
- **Space:** O(1) — marking is done in place on the input array; no auxiliary structure.

## Solution

```java
public class Solution {
    public int firstMissingPositive(int[] A) {
        int n = A.length;

        for (int i = 0; i < n; i++) {
            if (A[i] <= 0) {
                A[i] = n + 2;
            }
        }

        for (int i = 0; i < n; i++) {
            if (Math.abs(A[i]) <= n) {
                A[Math.abs(A[i]) - 1] = -1 * Math.abs(A[Math.abs(A[i]) - 1]);
            }
        }

        for (int i = 0; i < n; i++) {
            if (A[i] > 0) {
                return i + 1;
            }
        }

        return n + 1;
    }
}
```

## Key Learning

- **Core insight:** when the answer is provably bounded by `[1, N+1]`, the array's own `N` cells are enough storage to track presence — no hash set needed to hit O(1) space.
- **How to spot this pattern again:** "find the missing/first-missing/duplicate positive integer" + an O(1) space constraint is the strongest signal. Without the space constraint, a hash set would be the simpler (if less elegant) answer.
- **Mistake made this time:** comparing `A[i] <= n` directly instead of `abs(A[i]) <= n` — once a value gets marked negative in step 2, comparing the raw (possibly negative) value against `n` is always true and corrupts later indexing. Always take the absolute value before using a cell's contents as an index, once that cell might have been sign-flipped.
- **Common variant:** "find the duplicate" instead of "find the missing" uses the same negative-marking trick, but the exit condition flips to "first index that's *already* negative when you go to mark it."
