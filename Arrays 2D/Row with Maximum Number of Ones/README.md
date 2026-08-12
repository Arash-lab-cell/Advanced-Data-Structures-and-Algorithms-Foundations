# Row with Maximum Number of Ones

## Problem Statement

Given a binary matrix `A` of size `N x N` where every row is sorted (all `0`s before all `1`s), find the row with the maximum number of `1`s. If multiple rows tie, return the one with the lower index. Expected time complexity: `O(rows + columns)`.

**Constraints:** `1 <= N <= 1000`, `A[i][j] in {0, 1}`, 0-based indexing.

## Example

```
Row0: [0,0,0,1]
Row1: [0,1,1,1]
Row2: [0,0,1,1]
Row3: [0,0,0,0]
```

Row 1 has three `1`s — the most of any row — so the answer is **1**.

## Approach

Same **staircase search** shape as [Search in a Row Wise and Column Wise Sorted Matrix](<../Search in a Row Wise and Column Wise Sorted Matrix>), adapted from "find a value" to "track a boundary." Start at the top-right corner:

1. Moving left (`m--`) happens while the current cell is `1` — and since each row is individually sorted, every `1` found this way pushes the "current best row" pointer further left, which can only mean *more* ones in that row.
2. Moving down (`n++`) happens when the current cell is `0` — that row can't have any more `1`s than what's already been counted in a previous row at this same column boundary, so it's safe to abandon.
3. A small early exit: if a row's very first cell (`A[n][0]`) is already `1`, the entire row is `1`s (rows are sorted), so it can't be beaten — return immediately.

**Trigger to recognize this pattern:** "each row sorted" + "O(rows + cols)" is again the staircase signal — but here the goal is a *running boundary* (how far left the `1`s extend) rather than a single located value.

## Algorithm

1. Set `n = 0` (row pointer), `m = A[0].length - 1` (column pointer, starts at the rightmost column), `resultID = 0`.
2. While `n < rows` and `m >= 0`:
   - If `A[n][0] == 1`: row `n` is entirely `1`s — return `n` immediately.
   - Else if `A[n][m] == 0`: `n++` (this row has no more `1`s to the right of `m`; move to the next row).
   - Else (`A[n][m] == 1`): set `resultID = n`, then `m--` (keep pushing the boundary left within this row).
3. Return `resultID`.

## Dry Run

Using the Example matrix above, `n=0, m=3, resultID=0`:

| n | m | A[n][0] | A[n][m] | Action |
|---|---|---|---|---|
| 0 | 3 | 0 | 1 | `resultID=0`, `m--` → 2 |
| 0 | 2 | 0 | 0 | `n++` → 1 |
| 1 | 2 | 0 | 1 | `resultID=1`, `m--` → 1 |
| 1 | 1 | 0 | 1 | `resultID=1`, `m--` → 0 |
| 1 | 0 | 0 | 0 | `n++` → 2 |
| 2 | 0 | 0 | 0 | `n++` → 3 |
| 3 | 0 | 0 | 0 | `n++` → 4, loop ends (`n < 4` fails) |

**Result: `resultID = 1`** — matches (row 1 has 3 ones, the max).

## Why Does This Work?

Because `m` only ever moves left and never resets between rows, whichever row most recently succeeded in moving `m` left is provably the row whose `1`s extend furthest left across the *entire* matrix scanned so far — and since rows are individually sorted, "extends furthest left" is exactly "has the most `1`s." No per-row counter is needed: the shared, monotonically-shrinking `m` pointer already encodes "best boundary found so far" implicitly, because a later row can only push `m` further left than an earlier row already did.

## Complexity Analysis

- **Time:** O(N + M) — `n` only increases (bounded by rows), `m` only decreases (bounded by columns).
- **Space:** O(1).

## Solution

```java
public class Solution {
    public int solve(int[][] A) {
        int n = 0;
        int m = A[0].length - 1;
        int resultID = 0;

        while (n < A.length && m >= 0) {
            if (A[n][0] == 1) {
                return n;
            } else if (A[n][m] == 0) {
                n++;
            } else {
                resultID = n;
                m--;
            }
        }

        return resultID;
    }
}
```

## Key Learning

- **Core insight:** a pointer that only ever moves in one direction across the *whole* matrix (not reset per row) can implicitly track "best row so far" without an explicit counter, as long as the update rule guarantees monotonic improvement.
- **How to spot this pattern again:** sorted rows/columns + O(rows+cols) requirement, same as staircase search — but watch for whether the problem wants a *located value* (reset-free pointer) or a *count/boundary* comparison across rows (this problem).
- **Mistake made this time:** first attempt reintroduced an explicit `count`/`countOfOnes` comparison — redundant once `m` stopped resetting per row, since at that point `count` only ever increases and the `if` guarding it can never be false. Lesson: after changing a loop invariant (here, removing the per-row `m` reset), re-derive from scratch what any auxiliary counters are actually still measuring — don't assume old bookkeeping is still meaningful.
