# Longest Subarray Sliding Window

## Problem Statement
Given an array `arr` of non-negative integers and an integer `target`, return the length of the longest contiguous subarray whose sum is at most `target`.

**Constraints:** all elements of `arr` are non-negative.

## Example(s)
`arr = [1, 2, 1, 0, 3]`, `target = 4`

| Candidate window | Sum | ≤ target? | Length |
|---|---|---|---|
| `[1, 2, 1, 0]` (indices 0–3) | 4 | Yes | 4 |
| `[1, 2, 1, 0, 3]` (indices 0–4) | 7 | No | — |
| `[1, 0, 3]` (indices 2–4) | 4 | Yes | 3 |

**Output: 4** — the window `[1, 2, 1, 0]` is the longest one whose sum stays within target.

## Approach
Every number in the array is non-negative. That means as more elements are added to a window, the sum can only go up, never down. So if a window's sum becomes too big, the *only* fix is removing elements from the left — there's never a need to consider removing from the middle or right. That single fact is what makes a fast, single-pass two-pointer approach possible.

**Trigger to recognize this pattern:** "find the longest/shortest contiguous run satisfying a sum/count condition, over non-negative values" — whenever a window's badness is monotonic in its size (only grows as it grows), reach for a variable-size sliding window instead of recomputing sums for every `(i, j)` pair.

## Algorithm
1. Keep two pointers, `left` and `right` (driven by loop index `i`), marking the current window — `right` explores forward, `left` only moves when forced to.
2. Move `right` one step at a time through the array, adding each new element to a running `sum`.
3. If `sum` exceeds `target`, shrink the window from the left inside a `while` loop — subtract `arr[left]` from `sum` and increment `left` — repeating until `sum` is valid again (`≤ target`).
4. Once the window is guaranteed valid, its current size (`i - left + 1`) is a candidate answer. Update `maxLength` with the larger of itself and this candidate.
5. Repeat steps 2–4 until `right` has passed through the whole array.
6. Return `maxLength`.

## Dry Run
Input: `arr = [1, 2, 1, 0, 3]`, `target = 4`
Init: `left = 0, sum = 0, maxLength = 0`

| i (right) | arr[i] | sum after add | shrink? | left after | window [left, i] | length = i-left+1 | maxLength |
|---|---|---|---|---|---|---|---|
| 0 | 1 | 1 | no | 0 | [0,0] | 1 | 1 |
| 1 | 2 | 3 | no | 0 | [0,1] | 2 | 2 |
| 2 | 1 | 4 | no | 0 | [0,2] | 3 | 3 |
| 3 | 0 | 4 | no | 0 | [0,3] | 4 | 4 |
| 4 | 3 | 7 | yes → sum-=arr[0]=1 (sum=6, left=1); still >4 → sum-=arr[1]=2 (sum=4, left=2) | 2 | [2,4] | 3 | 4 |

**Return: 4**

Key thing worth remembering: at `i=4`, shrinking dropped the window length to 3, but `maxLength` stayed 4 — `Math.max` never lets a later, smaller window overwrite a better earlier one.

## Why Does This Work?
`left` only ever moves forward, never resets backward, and `right` also only moves forward. That means each index enters and leaves the window at most once across the entire run — the total number of shrink-steps across the *whole* execution is capped at `n`, even though the shrinking loop is nested inside the growing loop. This is the standard two-pointer amortized-cost argument, and it's what turns an apparent O(n²) (window growth × shrink) into true O(n).

Correctness relies on non-negativity: because `sum` is monotonically non-decreasing as the window grows, the moment `sum > target`, the *only* way to fix it is to shrink from the left — there is no valid window containing the current invalid one, so nothing is missed by always immediately shrinking back to validity before recording a length.

## Complexity Analysis
**Time:** O(n) — `right` advances n times total; `left` also advances at most n times total across the whole run (never resets), so total work is O(n) despite the nested loop.

**Space:** O(1) — only a few scalar variables (`left`, `sum`, `maxLength`) are used.

## Solution
```java
public class Solution {
    public static int longestSubarrayWithSumAtMostTarget(int[] arr, int target) {
        int left = 0;
        int sum = 0;
        int maxLength = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            while (sum > target) {
                sum -= arr[left];
                left++;
            }
            maxLength = Math.max(maxLength, (i - left + 1));
        }
        return maxLength;
    }
}
```

## Key Learning
- **Core insight:** when a window's sum only ever grows as it grows (non-negative elements), a variable-size sliding window can maintain validity with a `while` shrink instead of recomputing every subarray — the `while (sum > target)` guard already covers both "less than" and "equal to" target, so no separate equality check is needed.
- **How to spot this pattern:** "longest/shortest contiguous subarray with sum/count at most/at least X, all values non-negative" — the monotonic-badness property is the tell. If the array can contain negative numbers, plain two-pointer sliding window breaks down and a prefix-sum + hashmap approach is usually needed instead.
- **Common variants/traps:** stopping the scan the moment `sum == target` (misses a longer valid window later); recomputing `sum` from scratch each iteration instead of incrementally adding/subtracting one element at a time; using `arr.length` instead of the moving pointer `i` in the final length formula.
