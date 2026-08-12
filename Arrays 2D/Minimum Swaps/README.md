# Minimum Swaps

## Problem Statement

Given an array of integers `A` and an integer `B`, find the minimum number of swaps required to bring all elements `<= B` together (contiguous), anywhere in the array. Any two elements may be swapped, not just adjacent ones.

**Constraints:** `1 <= length of A <= 100000`, `-10^9 <= A[i], B <= 10^9`

## Example

```
A = [2, 1, 5, 6, 3], B = 3
```

Elements `<= B`: `2, 1, 3` → 3 of them, so the target block has size 3. The window `[2, 1, 5]` already has only one element (`5`) that doesn't belong — **1 swap** is enough, and no other window of size 3 does better.

## Approach

This is a **fixed-size sliding window**. First, notice that the final "block of good elements" must have a fixed size — exactly the count of elements `<= B` in the whole array, since every good element has to end up inside the block and every bad element has to end up outside it. That converts the problem into: *find the window of that exact size which already contains the fewest bad (`> B`) elements* — because the number of bad elements caught inside a window is exactly the number of swaps needed to evict them and pull good elements in from outside.

Rather than recomputing the bad-element count for every window from scratch (`O(N)` per window, `O(N^2)` total), slide the window one step at a time and adjust incrementally:
- If the element leaving the window was good and the element entering is bad, the window just got one bad element worse.
- If the element leaving was bad and the one entering is good, the window just got one bad element better.
- If both are the same kind, nothing changes.

**Trigger to recognize this pattern:** "bring all elements satisfying some condition together, minimum swaps" — the window size is always the count of elements satisfying the condition, and the answer is `min` over all windows of that size of (elements failing the condition inside it).

## Algorithm

1. Count `goodElements` = number of `A[i] <= B` across the whole array — this is the fixed window size.
2. Count `badElements` in the first window `A[0 .. goodElements-1]` (elements `> B` inside it). This is the initial `minSwaps`.
3. Slide the window: for `i = 1, j = goodElements` while `j < n`:
   - If `A[i-1] <= B` and `A[j] > B`: a good element left, a bad one entered → `badElements++`.
   - Else if `A[i-1] > B` and `A[j] <= B`: a bad element left, a good one entered → `badElements--`.
   - Update `minSwaps = min(minSwaps, badElements)`.
   - Advance `i` and `j`.
4. Return `minSwaps`.

## Dry Run

`A = [2, 1, 5, 6, 3]`, `B = 3`.

**Window size:** `goodElements` = count of `{2, 1, 3}` = 3.

**First window `[0, 2]` = `[2, 1, 5]`:** bad count = 1 (only `5 > B`). `minSwaps = 1`.

**Sliding:**

| i | j | A[i-1] | A[j] | What happens | badElements | minSwaps |
|---|---|---|---|---|---|---|
| 1 | 3 | 2 (≤B) | 6 (>B) | good leaves, bad enters → `badElements++` | 2 | 1 |
| 2 | 4 | 1 (≤B) | 3 (≤B) | good leaves, good enters → no change | 2 | 1 |

`j` reaches `5 = n`, loop ends.

**Result: `minSwaps = 1`** — the best window was the very first one, `[2, 1, 5]`.

## Why Does This Work?

The final block of good elements must be contiguous and must contain exactly `goodElements` elements (that's how many exist total). For any candidate window of that fixed size, the number of bad elements trapped inside it is exactly the number of swaps needed: each trapped bad element can be exchanged with a good element currently sitting outside the window (and there are always enough good elements outside, since the total count matches). So the answer is the minimum, over all windows of size `goodElements`, of the count of bad elements inside. The incremental slide is correct because moving the window by one position changes membership by exactly one element on each side, so the bad-count only needs a `+1`/`-1`/no-change adjustment rather than a full recount.

## Complexity Analysis

- **Time:** O(N) — one pass to count `goodElements`, one pass to seed the first window, one pass to slide across the rest. All linear, no nesting.
- **Space:** O(1) — only a handful of counters.

## Solution

```java
public class Solution {
    public int solve(int[] A, int B) {
        int n = A.length;

        int goodElements = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] <= B) {
                goodElements++;
            }
        }

        int badElements = 0;
        for (int i = 0; i < goodElements; i++) {
            if (A[i] > B) {
                badElements++;
            }
        }

        int minSwaps = badElements;

        int i = 1;
        int j = goodElements;
        while (j < n) {
            if (A[i - 1] <= B && A[j] > B) {
                badElements++;
            } else if (A[i - 1] > B && A[j] <= B) {
                badElements--;
            }
            minSwaps = Math.min(minSwaps, badElements);
            i++;
            j++;
        }

        return minSwaps;
    }
}
```

## Key Learning

- **Core insight:** "minimum swaps to group elements together" almost always reduces to "fixed-size sliding window, minimize the count of the wrong kind of element inside it" — the window size is derived from the data (count of the target condition), not given directly.
- **How to spot this pattern again:** phrases like "bring all X together" or "group all elements satisfying condition C" with a swap-count objective.
- **Note on the code:** the version drafted in class referenced an undefined variable (`count`) in the second loop where it should have used `goodElements` (the window size computed in the first loop) — a copy/rename slip that would fail to compile. Fixed here to use `goodElements` consistently; the dry run and logic were otherwise correct.
- **Common variant:** if elements can only swap with adjacent neighbors (not arbitrary pairs), the answer changes completely — that's no longer a window-counting problem but a positional-distance problem (sum of displacement distances).
