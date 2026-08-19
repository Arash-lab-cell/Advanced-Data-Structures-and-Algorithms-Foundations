# Subarrays with OR 1

## Problem Statement
You are given a binary array `A` of length `N`, where each element is either `0` or `1`. Count the number of subarrays where the bitwise OR of all elements in the subarray is `1`.

**Constraints**
- `1 <= N <= 10^4`
- `A[i]` is either `0` or `1`

## Example(s)
**Input:** `A = [0, 1, 0, 0, 1]` → **Output:** `11`
Out of all `15` subarrays (`N(N+1)/2` for `N=5`), `4` are all-zero and don't count, leaving `11` with OR `= 1`.

## Approach
The OR of a subarray of `0`s and `1`s is `1` exactly when the subarray contains at least one `1`, and `0` exactly when every element in it is `0`. Counting "contains at least one `1`" directly is awkward, so count the complement instead: subarrays that are entirely `0`s.

An all-zero subarray must sit entirely inside one maximal run of consecutive zeros. Scan the array left to right, tracking the length of the current run of zeros. Every time a `1` breaks the run, add `L(L+1)/2` (the count of subarrays inside a run of length `L`) to a running total of zero-only subarrays, then reset the run counter. Once the whole array is counted, subtract the zero-only total from the total subarray count `N(N+1)/2` to get the answer.

**Trigger for next time:** "count subarrays where a bitwise OR/AND-style condition holds" on a binary array → check whether the complement condition ("all zero," "all one," etc.) reduces to counting maximal runs — it very often does.

## Algorithm
1. Compute `totalSubarray = N(N+1)/2`.
2. Initialize `elementZero = 0` (current run length of zeros), `subarrayZero = 0` (running total of all-zero subarrays).
3. For each element in `A`:
   - If it's `0`, increment `elementZero`.
   - If it's `1`, add `elementZero * (elementZero + 1) / 2` to `subarrayZero`, then reset `elementZero = 0`.
4. After the loop, flush the trailing run: add `elementZero * (elementZero + 1) / 2` to `subarrayZero` once more (handles the case where the array ends in zeros).
5. Return `totalSubarray - subarrayZero`.

## Dry Run
`A = [0, 1, 0, 0, 1]`, `n = 5`, `totalSubarray = 5*6/2 = 15`.

| i | A[i] | Branch | elementZero after | subarrayZero after |
|---|---|---|---|---|
| 0 | 0 | zero-branch: elementZero++ | 1 | 0 |
| 1 | 1 | one-branch: += 1·2/2 = 1 | 0 | 1 |
| 2 | 0 | zero-branch: elementZero++ | 1 | 1 |
| 3 | 0 | zero-branch: elementZero++ | 2 | 1 |
| 4 | 1 | one-branch: += 2·3/2 = 3 | 0 | 4 |

After the loop, `elementZero = 0`, so the flush adds `0`. `subarrayZero = 4`.

**Result:** `subarrayOne = 15 - 4 = 11`.

## Why Does This Work?
Every all-zero subarray lies entirely within exactly one maximal run of consecutive zeros — it can't straddle a `1`, since that would put a `1` inside it. So summing `L(L+1)/2` over every maximal zero-run counts each all-zero subarray exactly once, with no overlap and no omission (this is the same "count all sub-ranges of a contiguous block of length `L`" formula used for total-subarray counts, just applied to each run individually). Since OR is `1` for every subarray that isn't all-zero, subtracting this exact count from the total subarray count gives the exact count of OR-`1` subarrays.

## Complexity Analysis
- **Time:** O(N) — a single left-to-right scan.
- **Space:** O(1) — two running counters.

## Solution
```java
public class Solution {
    public int subarraysWithOR1(int[] A) {
        int n = A.length;
        int totalSubarray = (n * (n + 1)) / 2;
        int elementZero = 0;
        int subarrayZero = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] == 0) {
                elementZero++;
            } else {
                subarrayZero = subarrayZero + (elementZero * (elementZero + 1)) / 2;
                elementZero = 0;
            }
        }
        subarrayZero = subarrayZero + (elementZero * (elementZero + 1)) / 2;
        int subarrayOne = totalSubarray - subarrayZero;
        return subarrayOne;
    }
}
```

## Key Learning
- **Core insight:** on a binary array, "OR = 0" is a far easier condition to count directly than "OR = 1" ("at least one"), so count the complement and subtract from the total subarray count.
- **Pattern recognition cue:** "count subarrays where [bitwise property] holds" on a `0/1` array → look for a maximal-run counting reduction; this exact building block reappears in [[Subarray OR]] applied per bit position.
- **Common trap:** forgetting to flush the trailing run after the loop — if the array ends in `0`s, no `1` ever triggers the addition inside the loop, so the final `elementZero*(elementZero+1)/2` must be added once more after the loop ends.
