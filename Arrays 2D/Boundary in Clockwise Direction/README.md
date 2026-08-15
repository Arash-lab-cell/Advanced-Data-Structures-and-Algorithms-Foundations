# Boundary in Clockwise Direction

## Problem Statement
Given a rectangular matrix `A` of `N×M` dimension, return its boundary elements traversed in clockwise direction.

**Constraints:** `1 <= N, M <= 10^3`, `1 <= A[i][j] <= 10^9`

## Example(s)
Matrix (N=4, M=3):
```
1   2   3
4   5   6
7   8   9
10  11  12
```

**Output:** `[1, 2, 3, 6, 9, 12, 11, 10, 7, 4]`

Walking clockwise: across the top row (1, 2, 3), down the right column (6, 9, 12), back across the bottom row (11, 10), up the left column (7, 4) — stopping one short of the corner already visited by the previous side each time.

## Approach
This is a single-layer boundary walk: traverse the four edges of the matrix in order (top row → right column → bottom row → left column) and collect elements into a result array, without revisiting any corner.

The key discipline is where each of the four loops stops: each loop should stop *one element before* the corner that the *next* loop will pick up. For example, in a row with 5 elements, the top-row loop only visits the first 4 — the 5th (top-right corner) is picked up by the right-column loop that follows. Applying this consistently on all four sides means every boundary cell is visited exactly once, with no duplicates and no gaps.

Single row (`N == 1`) or single column (`M == 1`) are edge cases: the "boundary" is just that one row/column in its entirety, so the four-loop walk (which assumes at least 2 rows and 2 columns) doesn't apply and needs a separate, simpler pass.

**Trigger to recognize this pattern:** "return the border/boundary/perimeter of a 2D grid" or "spiral traversal" (the boundary walk is spiral traversal's outermost ring) — reach for a 4-loop directional walk with each loop excluding the corner claimed by the next.

## Algorithm
1. Let `n = A.length`, `m = A[0].length`.
2. **Edge case — single row:** if `n == 1`, copy the entire row into the result and return.
3. **Edge case — single column:** if `m == 1`, copy the entire column into the result and return.
4. Compute `totalElements = 2*(n-1) + 2*(m-1)` — every row contributes 2 boundary cells except the top and bottom rows (fully counted), and similarly for columns; this formula accounts for the 4 corners being shared between adjacent sides without double counting.
5. **Top row:** append `A[0][i]` for `i` from `0` to `m-2` (stop before the top-right corner).
6. **Right column:** append `A[i][m-1]` for `i` from `0` to `n-2` (stop before the bottom-right corner).
7. **Bottom row:** append `A[n-1][i]` for `i` from `m-1` down to `1` (stop before the bottom-left corner).
8. **Left column:** append `A[i][0]` for `i` from `n-1` down to `1` (stop before the top-left corner, already collected in step 5).
9. Return the accumulated result.

## Dry Run
Matrix (N=4, M=3):
```
1   2   3
4   5   6
7   8   9
10  11  12
```
`totalElements = 2*(4-1) + 2*(3-1) = 6 + 4 = 10`

| Step | Loop | i | Action | ans so far |
|---|---|---|---|---|
| 1 | top row | 0 | `A[0][0]=1` | [1] |
| 2 | top row | 1 | `A[0][1]=2` | [1,2] |
| 3 | right col | 0 | `A[0][2]=3` | [1,2,3] |
| 4 | right col | 1 | `A[1][2]=6` | [1,2,3,6] |
| 5 | right col | 2 | `A[2][2]=9` | [1,2,3,6,9] |
| 6 | bottom row | 2 | `A[3][2]=12` | [1,2,3,6,9,12] |
| 7 | bottom row | 1 | `A[3][1]=11` | [...,11] |
| 8 | left col | 3 | `A[3][0]=10` | [...,10] |
| 9 | left col | 2 | `A[2][0]=7` | [...,7] |
| 10 | left col | 1 | `A[1][0]=4` | [...,4] |

**Result:** `[1, 2, 3, 6, 9, 12, 11, 10, 7, 4]` — 10 elements, no revisited corners.

## Why Does This Work?
Every boundary cell belongs to exactly one of four sides, except the four corners, which sit at the junction of two sides. The algorithm resolves that ambiguity with a fixed rule: each corner is "owned" by whichever side comes *first* in the clockwise order (top-left owned by top row, top-right by right column, bottom-right by bottom row, bottom-left by left column). Each loop's bounds are written to stop exactly one cell short of the corner owned by the *next* loop — so no corner is ever collected twice, and because the four sides together cover the entire perimeter, no boundary cell is ever skipped either. The single-row/single-column special cases are handled separately because with only one row or column, "top" and "bottom" (or "left" and "right") collapse into the same cells, which would break the corner-ownership assumption the four-loop walk depends on.

## Complexity Analysis
**Time:** O(N + M) — each of the four loops does linear work bounded by `N` or `M`; no cell is visited more than once.

**Space:** O(N + M) for the output array holding the boundary elements (this is required output space, not extra auxiliary space).

## Solution
```java
public class Solution {
    public int[] solve(int[][] A) {
        int n = A.length;
        int m = A[0].length;
        if (n == 1) {
            int[] rowOnly = new int[m];
            for (int i = 0; i < m; i++) {
                rowOnly[i] = A[0][i];
            }
            return rowOnly;
        }
        if (m == 1) {
            int[] colOnly = new int[n];
            for (int i = 0; i < n; i++) {
                colOnly[i] = A[i][0];
            }
            return colOnly;
        }
        int totalElements = (2 * (n - 1)) + (2 * (m - 1));
        int[] ans = new int[totalElements];
        int ansId = 0;
        for (int i = 0; i < m - 1; i++) {
            ans[ansId] = A[0][i];
            ansId++;
        }
        for (int i = 0; i < n - 1; i++) {
            ans[ansId] = A[i][m - 1];
            ansId++;
        }
        for (int i = m - 1; i > 0; i--) {
            ans[ansId] = A[n - 1][i];
            ansId++;
        }
        for (int i = n - 1; i > 0; i--) {
            ans[ansId] = A[i][0];
            ansId++;
        }
        return ans;
    }
}
```

## Key Learning
- **Core insight:** a four-sided boundary walk is really one shared rule applied four times — walk a side, stop one cell before the corner the next side owns — which is exactly the outer ring of a spiral matrix traversal.
- **How to spot this pattern:** "return the border/perimeter of a matrix," "print the outer layer of a 2D grid," or the first ring of a full spiral-order traversal problem.
- **Common variants/traps:** forgetting the single-row / single-column degenerate cases, where the normal 4-loop logic double-counts or under-counts because "top" and "bottom" (or "left" and "right") are literally the same row/column; off-by-one errors in loop bounds that either revisit a corner or skip the cell just before it.
