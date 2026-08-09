# Rain Water Trapped

## Problem Statement
Given an array `A` representing a histogram (bars of uniform width 1, heights `A[i]`), compute the total volume of water that can be trapped between the bars after rain.

**Constraints:** `1 <= |A| <= 10^5`, `0 <= A[i] <= 10^5`

## Example(s)
`A = [5, 4, 1, 4, 3, 2, 7]` → total trapped water = **11**

`A = [4, 2, 0, 3, 2, 5]` → total trapped water = **9** (traced in detail below)

## Approach
The water sitting above any bar `i` is bounded by two walls: the tallest bar to its left and the tallest bar to its right. Water can never rise higher than the *shorter* of those two walls — anything above that height simply spills over the lower side.

So for every index, the water level is `min(tallest bar to the left, tallest bar to the right)`, and the trapped water at that index is that level minus the bar's own height (since the bar occupies its own footprint).

**Trigger to recognize this pattern:** "how much X can accumulate at each position, bounded by the extremes on both sides" — precompute a running max from the left and a running max from the right, then combine with `min` at each index.

## Algorithm
1. Build `leftMax[i]` = tallest bar in `A[0..i]`, scanning left to right (`leftMax[0] = A[0]`).
2. Build `rightMax[i]` = tallest bar in `A[i..n-1]`, scanning right to left (`rightMax[n-1] = A[n-1]`).
3. For each index `i`, the water level is `min(leftMax[i], rightMax[i])`.
4. Trapped water at `i` = `level - A[i]` (always `>= 0`, since `level >= A[i]` because `A[i]` itself contributes to both max arrays).
5. Sum trapped water across all indices and return the total.

## Dry Run
`A = [4, 2, 0, 3, 2, 5]`

**leftMax** (left to right, running max):
`[4, 4, 4, 4, 4, 5]`

**rightMax** (right to left, running max):
`[5, 5, 5, 5, 5, 5]`

**level = min(leftMax, rightMax)** at each index:
`[4, 4, 4, 4, 4, 5]`

**trapped = level − A[i]:**

| i | A[i] | level | trapped |
|---|---|---|---|
| 0 | 4 | 4 | 0 |
| 1 | 2 | 4 | 2 |
| 2 | 0 | 4 | 4 |
| 3 | 3 | 4 | 1 |
| 4 | 2 | 4 | 2 |
| 5 | 5 | 5 | 0 |

**Total = 0+2+4+1+2+0 = 9**

## Why Does This Work?
Water resting above index `i` is physically constrained by the tallest wall on each side — no more water can accumulate than what the *lower* of the two walls allows, because anything higher escapes over that shorter side. `min(leftMax[i], rightMax[i])` is therefore an exact ceiling on the water surface at `i`, not an approximation. Subtracting `A[i]` correctly removes the bar's own volume since the bar is solid, not water.

Precomputing `leftMax`/`rightMax` in single passes works because each is a pure running maximum — no index's value depends on anything other than the running max seen so far, so both arrays can be built in one linear sweep each, independent of one another.

## Complexity Analysis
**Time:** O(N) — three linear passes (leftMax, rightMax, final sum).

**Space:** O(N) — for the two auxiliary arrays. (Can be reduced to O(1) with a two-pointer variant that tracks running leftMax/rightMax without storing full arrays, but the three-array version is the clearest starting point.)

## Solution
```java
public class Solution {
    public int trap(final int[] A) {
        int n = A.length;
        int[] leftMax = new int[n];
        leftMax[0] = A[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], A[i]);
        }
        int[] rightMax = new int[n];
        rightMax[n - 1] = A[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], A[i]);
        }
        int[] level = new int[n];
        for (int i = 0; i < n; i++) {
            level[i] = Math.min(leftMax[i], rightMax[i]);
        }
        int totalWater = 0;
        for (int i = 0; i < n; i++) {
            totalWater += (level[i] - A[i]);
        }
        return totalWater;
    }
}
```

## Key Learning
- **Core insight:** the water level at any position is bounded by the *minimum* of the two directional maxima — this "two-sided constraint" is the essence of the problem.
- **How to spot this pattern:** any "trapped between two boundaries" question, or one asking for a value at each index that depends on the best/worst seen from both directions — build `leftMax`/`rightMax` (or `leftMin`/`rightMin`) arrays first.
- **Common variants/traps:** trying to solve it with a single left-to-right pass (doesn't work — you don't know the right boundary yet); the O(1)-space two-pointer optimization (move whichever of `left`/`right` has the smaller max inward, since the smaller side's water level is already fully determined); 2D "trapping rain water" is a much harder variant requiring a priority queue/BFS from the boundary inward.
