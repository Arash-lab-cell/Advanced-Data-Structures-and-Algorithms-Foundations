# Search in a Row Wise and Column Wise Sorted Matrix

## Problem Statement

Given a matrix of integers `A` of size `N x M` and an integer `B`, every row and every column of `A` is sorted in non-decreasing order. Find and return the position of `B` in the matrix, encoded as `i * 1009 + j` (1-based indexing).

- If `A[i][j] == B`, return `i * 1009 + j`.
- If `B` is not present, return `-1`.
- If `B` appears more than once, return the **smallest** `i * 1009 + j` among the matches — i.e. the topmost occurrence, and the leftmost among ties in that row.
- Expected time complexity: linear in `N + M`.

**Constraints:** `1 <= N, M <= 1000`, `-100000 <= A[i][j] <= 100000`, `-100000 <= B <= 100000`

## Example

```
A = [[1, 3, 5],
     [2, 5, 6],
     [5, 7, 8]]
B = 5
```

`B = 5` occurs at 1-indexed positions `(1,3)`, `(2,2)`, `(3,1)`. Their encodings are `1012`, `2020`, `3028`. The answer is the minimum: **1012**.

## Approach

This is the **staircase search** pattern. Because every row and column is sorted ascending, the top-right corner is a natural pivot: everything to its left is smaller, everything below it is larger.

1. Start at the top-right corner (`i = 1`, `j = last column`).
2. If the current value is greater than `B`, the whole column below it is also `> B` (columns are sorted) — safe to discard it by moving left (`j--`).
3. If the current value is less than `B`, the whole row to its left is also `< B` — safe to discard it by moving down (`i++`).
4. If it equals `B`, record the encoded position, then keep moving left (`j--`) — there could be an even smaller `j` (or a later row) also equal to `B`, so we can't return immediately.

**Trigger to recognize this pattern:** "matrix sorted along both rows and columns" + "find/count something in O(rows + cols)" is the signal to reach for staircase search instead of binary search per row (`O(N log M)`) or a full scan (`O(N*M)`).

## Algorithm

1. Set `i = 1`, `j = A[0].length` (top-right corner, 1-indexed), `result = infinity`.
2. While `i <= N` and `j >= 1`:
   - If `A[i-1][j-1] > B`: `j--`.
   - Else if `A[i-1][j-1] < B`: `i++`.
   - Else: compute `encoded = i*1009 + j`, set `result = min(result, encoded)`, then `j--`.
3. Return `result` if it was updated, otherwise `-1`.

## Dry Run

`A = [[1,3,5],[2,5,6],[5,7,8]]`, `B = 5`, `j` starts at `3`.

| i | j | A[i-1][j-1] | Compare | Action |
|---|---|---|---|---|
| 1 | 3 | 5 | == | `encoded = 1*1009+3 = 1012`, `result = 1012`, `j--` |
| 1 | 2 | 3 | < B | `i++` |
| 2 | 2 | 5 | == | `encoded = 2020`, `result = min(1012, 2020) = 1012`, `j--` |
| 2 | 1 | 2 | < B | `i++` |
| 3 | 1 | 5 | == | `encoded = 3028`, `result = min(1012, 3028) = 1012`, `j--` |
| 3 | 0 | — | `j < 1` | loop ends |

**Result: 1012** — matches the expected topmost, leftmost-in-that-row match.

## Why Does This Work?

The encoding `i * 1009 + j` is a total order that always ranks a smaller `i` below any larger `i` (since `1009` is larger than any possible `j`, `1 <= N, M <= 1000`). So a single `Math.min` comparison across all matches is equivalent to "prefer the topmost row, then the leftmost column" — no need to track `(i, j)` pairs separately.

The staircase itself is correct because each step permanently eliminates an entire row or column that provably cannot contain a better answer than what's already been considered: moving left discards a column that's all `> B` below the current cell; moving down discards a row that's all `< B` to the left of the current cell. Every cell is visited at most once, so the traversal is bounded by `N + M`.

## Complexity Analysis

- **Time:** O(N + M) — `i` only increases (bounded by `N`), `j` only decreases (bounded by `M`), so the loop runs at most `N + M` times.
- **Space:** O(1) — no extra data structures.

## Solution

```java
public class Solution {
    public int solve(int[][] A, int B) {
        int i = 1;
        int j = A[0].length;
        int result = Integer.MAX_VALUE;

        while (i <= A.length && j >= 1) {
            if (A[i - 1][j - 1] > B) {
                j--;
            } else if (A[i - 1][j - 1] < B) {
                i++;
            } else {
                int encoded = i * 1009 + j;
                result = Math.min(result, encoded);
                j--;
            }
        }

        return result < Integer.MAX_VALUE ? result : -1;
    }
}
```

## Key Learning

- **Core insight:** staircase search eliminates a full row or column per comparison by starting from a corner where "greater" and "smaller" point in different directions (right→down increasing, right→left decreasing).
- **How to spot this pattern again:** matrix sorted ascending along both rows and columns, and a required complexity of `O(rows + cols)` — that complexity bound is the tell, since binary search per row would be `O(N log M)` and brute force `O(N*M)`.
- **Mistake made this time:** initially returned on the first match without the trailing `j--`, silently assuming "first found" meant "minimum encoded". Fix: when there can be duplicates, keep searching past the first hit and let a running `min` — not early return — decide the final answer.
- **Common variant:** counting matches instead of locating one (see [Row with Maximum Number of Ones](<../Row with Maximum Number of Ones>) in this same folder) — same corner-start idea, different exit condition.
