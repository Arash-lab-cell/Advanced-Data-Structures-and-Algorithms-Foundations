# Continuous Sum Query

## Problem Statement
There are `A` beggars sitting in a row outside a temple, each starting with an empty pot. Devotees donate a fixed amount `P` to a contiguous range of beggars from index `L` to `R` (1-indexed, `1 <= L <= R <= A`).

Given the donations as a 2D array `B`, where `B[i] = [L, R, P]` for the i-th devotee, return the final amount in each beggar's pot.

**Constraints:** `1 <= A <= 2*10^5`, `1 <= L <= R <= A`, `1 <= P <= 10^3`, `0 <= len(B) <= 10^5`

## Example(s)
`A = 5`, `B = [[1, 3, 10], [2, 4, 5]]`

| Beggar (1-indexed) | 1 | 2 | 3 | 4 | 5 |
|---|---|---|---|---|---|
| From devotee 1 (range 1–3) | +10 | +10 | +10 | | |
| From devotee 2 (range 2–4) | | +5 | +5 | +5 | |
| **Total** | **10** | **15** | **15** | **5** | **0** |

## Approach
Naively applying every donation to every index in its range is O(A · len(B)) in the worst case. The key realization: we don't need to know the running total at every index *while processing donations* — we only need the total once, at the end. That reframes the problem as **range-update, single final read**, which is exactly what a **difference array** is built for.

**Trigger to recognize this pattern:** "apply the same delta to every element in a range, many times, then report final values" — whenever range *updates* vastly outnumber range *reads*, reach for a difference array instead of repeatedly touching every element in the range.

## Algorithm
1. Create `arr` of size `A`, all zeros — this is the *difference* array, not the answer yet.
2. For each donation `[L, R, P]` (converting to 0-indexed `start = L-1`, `end = R-1`):
   - `arr[start] += P` — marks "the donation turns on starting here."
   - If `end + 1 < A`, `arr[end + 1] -= P` — marks "the donation turns off right after here."
3. Run a prefix sum over `arr` left to right: `arr[i] += arr[i-1]`. After this pass, `arr[i]` holds the true total for beggar `i`.
4. Return `arr`.

## Dry Run
`A = 5`, queries `[1,3,10]` and `[2,4,5]` (already shown in Example above, traced in raw index terms below).

Start: `arr = [0, 0, 0, 0, 0]`

**Query 1** (`start=0, end=2, amount=10`): `arr[0] += 10` → `[10,0,0,0,0]`; `arr[3] -= 10` → `[10,0,0,-10,0]`

**Query 2** (`start=1, end=3, amount=5`): `arr[1] += 5` → `[10,5,0,-10,0]`; `arr[4] -= 5` → `[10,5,0,-10,-5]`

**Prefix sum pass:**
- `arr[0] = 10`
- `arr[1] = 5 + 10 = 15`
- `arr[2] = 0 + 15 = 15`
- `arr[3] = -10 + 15 = 5`
- `arr[4] = -5 + 5 = 0`

**Output:** `[10, 15, 15, 5, 0]`

## Why Does This Work?
Think of `arr[start] += amount` as switching a "water tap" on at `start`: once the prefix-sum pass runs, that `+amount` flows forward into every index from `start` onward, forever. `arr[end+1] -= amount` is the tap being switched back off exactly one step past where the donation should stop — when the prefix sum reaches `end+1`, the carried `+amount` and the planted `-amount` cancel, so indices beyond `end` are unaffected.

Because addition is linear, this superposes correctly across any number of overlapping donations: each one independently contributes its own "on/off" pair, and the single final prefix-sum pass sums all of their effects at once.

## Complexity Analysis
**Time:** O(A + len(B)) — O(len(B)) to apply all the +/- marks, O(A) for the one prefix-sum pass. Far better than the O(A · len(B)) naive approach.

**Space:** O(1) extra — the difference array is built in place / reuses the output array; no space scales beyond the output itself.

## Solution
```java
public class Solution {
    public int[] solve(int A, int[][] B) {
        int n = B.length;
        int[] arr = new int[A];
        for (int i = 0; i < n; i++) {
            // beggar no. 3 will be at index 2
            int start = B[i][0] - 1;
            int end = B[i][1] - 1;
            int amount = B[i][2];
            arr[start] = arr[start] + amount;
            if (end < arr.length - 1) {
                arr[end + 1] = arr[end + 1] - amount;
            }
        }
        for (int i = 1; i < arr.length; i++) {
            arr[i] = arr[i] + arr[i - 1];
        }
        return arr;
    }
}
```

## Key Learning
- **Core insight:** a range update can be encoded as two point updates (a start marker and an end+1 canceling marker); a single prefix-sum pass "expands" all of them back into per-index values.
- **How to spot this pattern:** many range-add operations followed by one read of final values — if you catch yourself about to write a nested loop that touches every index in every range, stop and ask whether a difference array collapses it to O(1) per update.
- **Common variants/traps:** off-by-one on `end + 1` (forgetting to convert to 0-indexed, or writing past the array bounds when `end == A-1`); confusing this with plain prefix sum (which answers *range-sum queries* on a fixed array) — difference array is the mirror-image technique for *range-update* problems.
