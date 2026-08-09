# Max Sum Contiguous Subarray

## Problem Statement
Given an array `A` of length `N`, find the maximum possible sum of any non-empty contiguous subarray.

**Constraints:** `1 <= N <= 10^6`, `-1000 <= A[i] <= 1000`

## Example(s)
`A = [-2, 1, -3, 4, -1, 2, 1]` → max sum = **6**, from subarray `[4, -1, 2, 1]`

`A = [-5, -2]` (all negative) → max sum = **-2** (the single largest element, since the subarray must be non-empty)

## Approach
This is **Kadane's Algorithm**. The idea: track a running sum as you scan left to right. Whenever the running sum drops below zero, it can only drag down any subarray extended from it — so it's better to abandon it and start a fresh subarray from the next element.

At every step, record the best running sum seen so far, *before* deciding whether to reset — that "seen so far" value is what guarantees correctness even when every element is negative.

**Trigger to recognize this pattern:** "maximum/best contiguous run" problems where a running total that goes negative (or otherwise "bad") should be discarded rather than carried forward — Kadane's is the canonical O(N) answer to what looks like an O(N²) brute force over all subarrays.

## Algorithm
1. Initialize `sum = 0`, `maxSum = Integer.MIN_VALUE`.
2. For each element `A[i]`:
   - `sum += A[i]`.
   - `maxSum = max(maxSum, sum)` — record **before** any reset.
   - If `sum < 0`, reset `sum = 0` (discard the losing run).
3. Return `maxSum`.

## Dry Run
`A = [-2, 1, -3, 4, -1, 2, 1]`

Start: `sum = 0`, `maxSum = MIN_VALUE`

| i | A[i] | sum (after add) | maxSum (after update) | reset? |
|---|---|---|---|---|
| 0 | -2 | -2 | -2 | yes → sum = 0 |
| 1 | 1 | 1 | 1 | no |
| 2 | -3 | -2 | 1 | yes → sum = 0 |
| 3 | 4 | 4 | 4 | no |
| 4 | -1 | 3 | 4 | no |
| 5 | 2 | 5 | 5 | no |
| 6 | 1 | 6 | 6 | no |

**Output: 6** (subarray `[4, -1, 2, 1]`)

## Why Does This Work?
For every index `i`, `sum` right before a potential reset represents the maximum sum of a contiguous subarray *ending exactly at `i`*, given that all subarrays starting at points where `sum` had gone negative are provably worse than starting fresh. Recording `maxSum` before the reset check ensures every ending position gets a fair chance to be the global answer — including the case where every element is negative and the "best" subarray is a single element, not the empty subarray with sum 0.

Because `maxSum` is updated at every single index (not just at resets), the algorithm implicitly considers "best subarray ending at `i`" for all `i`, and the maximum across all `i` is exactly the maximum over all contiguous subarrays — this is a form of dynamic programming where `sum` is the DP state and the reset is the transition that discards a negative-value prefix.

## Complexity Analysis
**Time:** O(N) — a single linear pass.

**Space:** O(1) — only two scalar variables tracked.

## Solution
```java
public class Solution {
    public int maxSubArray(final int[] A) {
        int n = A.length;
        int sum = 0;
        int maxSum = Integer.MIN_VALUE;
        if (n == 1) {
            return A[0];
        }
        for (int i = 0; i < n; i++) {
            sum = sum + A[i];
            maxSum = Math.max(maxSum, sum);
            if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }
}
```

## Key Learning
- **Core insight:** a running sum that has gone negative can never help a future subarray — discard it and restart, but always record the best value *before* discarding.
- **How to spot this pattern:** "maximum sum/product/length of a contiguous run" — especially when brute force is the obvious O(N²) (try every start/end pair) and the array can contain negative values, which is what makes the greedy reset non-trivial.
- **Common variants/traps:** resetting `sum` *before* updating `maxSum` (loses the correct answer on all-negative arrays); needing to also track the actual subarray indices (requires storing a candidate `start` pointer alongside the reset logic); the "maximum product subarray" variant, where a single very negative number can flip a large negative product back to a large positive one — that variant needs to track both a running max *and* a running min.
