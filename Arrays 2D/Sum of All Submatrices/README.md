# Sum of All Submatrices

## Problem Statement

Given a square 2D matrix `A` of dimensions `N x N`, return the sum of the sums of all possible submatrices (every contiguous rectangular sub-region).

**Constraints:** `1 <= N <= 30`, `0 <= A[i][j] <= 10`

## Example

```
A = [[1, 2],
     [3, 4]]
```

All 9 possible subrectangles and their sums: `{1}=1`, `{2}=2`, `{3}=3`, `{4}=4`, `{1,2}=3`, `{3,4}=7`, `{1,3}=4`, `{2,4}=6`, `{1,2,3,4}=10`. Total = `1+2+3+4+3+7+4+6+10 = 40`.

## Approach

This is the 2D extension of the **contribution technique**. Instead of enumerating every subrectangle directly (which is combinatorially expensive), ask a different question for each cell: *in how many of the possible subrectangles does this specific cell appear?* Multiply the cell's value by that count, and sum over all cells — every subrectangle's contribution gets counted exactly once, split across its cells.

In the 1D version, a cell's contribution is `(number of valid left boundaries) * (number of valid right boundaries)`. In 2D, a cell contributes to a subrectangle only if **both** its row is within the row-range **and** its column is within the col-range, so the row-count and column-count multiply together.

**Trigger to recognize this pattern:** "sum over all subarrays/submatrices" is the signal — direct enumeration is `O(N^4)` for submatrices, but contribution counting brings it down to a single pass over the matrix.

## Algorithm

1. For each cell `(i, j)` (0-indexed), count:
   - Row-ranges `[r1, r2]` containing row `i`: `r1` can be any of `0..i` → `i+1` choices; `r2` can be any of `i..rows-1` → `rows-i` choices.
   - Col-ranges `[c1, c2]` containing col `j`: similarly `(j+1) * (cols-j)` choices.
2. The number of subrectangles containing `(i,j)` is the product of the two.
3. Add `A[i][j] * (i+1) * (j+1) * (rows-i) * (cols-j)` to a running `sum`.
4. Return `sum` after visiting every cell.

## Dry Run

`A = [[1,2],[3,4]]`, `rows = cols = 2`.

Per-cell term = `A[i][j] * (i+1) * (j+1) * (rows-i) * (cols-j)`:

| i | j | A[i][j] | term |
|---|---|---|---|
| 0 | 0 | 1 | `1*1*1*2*2 = 4` |
| 0 | 1 | 2 | `2*1*2*2*1 = 8` |
| 1 | 0 | 3 | `3*2*1*1*2 = 12` |
| 1 | 1 | 4 | `4*2*2*1*1 = 16` |

Running sum: `4 → 12 → 24 → 40`.

**Result: 40** — matches the brute-force enumeration in the Example above.

## Why Does This Work?

`(i+1)*(rows-i)` is exactly the count of row-ranges `[r1, r2]` with `r1 <= i <= r2`, and likewise for columns. A subrectangle contains cell `(i,j)` iff its row-range contains row `i` AND its col-range contains column `j`, so the total number of subrectangles containing `(i,j)` is the product of the two independent counts. Summing `value * count` over every cell is algebraically identical to summing the total of every subrectangle, because each subrectangle's sum decomposes into its individual cells, and each cell's value gets counted once per subrectangle it belongs to — which is precisely `count` times.

## Complexity Analysis

- **Time:** O(N²) — one pass over every cell of the `N x N` matrix, O(1) work per cell.
- **Space:** O(1) — no extra data structures beyond the running sum.

## Solution

```java
public class Solution {
    public int solve(int[][] A) {
        int rows = A.length;
        int cols = A[0].length;
        int sum = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum += A[i][j] * (i + 1) * (j + 1) * (rows - i) * (cols - j);
            }
        }

        return sum;
    }
}
```

## Key Learning

- **Core insight:** don't enumerate the thing you're summing over — count, per element, how many times it's counted, and multiply.
- **How to spot this pattern again:** any "sum over all subarrays/submatrices/subsequences" phrasing. In 1D it's `(i+1)*(n-i)`; in 2D it's the product of two independent 1D counts, one per axis.
- **Note on the note:** the original class-note version of this solution said "Time Complexity O(N)" — that undercounts it. The loop visits every one of the `N*N` cells once, so it's `O(N²)`, not `O(N)`. Easy trap when a matrix problem's *inner logic* is O(1) per cell — the complexity is still driven by the total number of cells, not the per-cell work.
