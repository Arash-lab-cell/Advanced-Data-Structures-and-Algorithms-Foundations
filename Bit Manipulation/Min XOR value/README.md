# Min XOR value

## Problem Statement
Given an integer array `A` of `N` integers, find the pair of integers in the array with the minimum XOR value, and report that minimum value.

**Constraints**
- `2 <= length of the array <= 100000`
- `0 <= A[i] <= 10^9`

## Example(s)
**Input:** `A = [16, 7, 15, 8]` → **Output:** `7`
Sorted, the array is `[7, 8, 15, 16]`; the adjacent pair `(8, 15)` gives `8^15 = 7`, the minimum over all pairs.

## Approach
XOR between two numbers is small when they agree on most of their bits, especially the high-order ones. Sorting the array puts numbers with similar magnitudes — and therefore similar leading bits — next to each other. The key claim is that the minimum-XOR pair is always found among *adjacent* elements after sorting, so a single linear scan over sorted adjacent pairs is sufficient; no need to check all `O(n^2)` pairs.

**Trigger for next time:** "minimum (or maximum) XOR over all pairs" with `N` large enough to forbid brute force → sort first and scan adjacent pairs (or reach for a bit-trie for the maximum-XOR variant).

## Algorithm
1. Sort `A`.
2. Initialize `minXOR = Integer.MAX_VALUE`.
3. For each adjacent pair `(A[i-1], A[i])` in the sorted array, compute `A[i-1] ^ A[i]` and keep the running minimum.
4. Return `minXOR`.

## Dry Run
`A = [16, 7, 15, 8]` → sorted: `[7, 8, 15, 16]`.

| i | A[i-1] | A[i] | xor | running minXOR |
|---|---|---|---|---|
| 1 | 7 (`0111`) | 8 (`1000`) | 15 | 15 |
| 2 | 8 (`1000`) | 15 (`1111`) | 7 | 7 |
| 3 | 15 (`1111`) | 16 (`10000`) | 31 | 7 |

**Result:** `minXOR = 7`. Only adjacent pairs were checked — `(7,8)`, `(8,15)`, `(15,16)` — never all 6 possible pairs, and the true minimum still turned up.

## Why Does This Work?
Take any three sorted values `x < y < z`. At the bit position where `x` and `z` first differ, the middle value `y` must agree with at least one of `x` or `z` at that position (it can't disagree with both, since a bit is either `0` or `1`). Pairing `y` with whichever one it agrees with there gives an XOR that is no larger than `x ^ z`, because that pair's disagreement can only start at the same bit or later. So a "far apart" pair in sorted order can never beat every adjacent pair — by induction, the global minimum-XOR pair always shows up somewhere among the adjacent pairs of the sorted array, making a single linear scan sufficient.

This also rules out two tempting shortcuts: picking the two numerically smallest values fails (e.g. `[1, 8, 9]` — the true minimum pair is `(8,9)`, not `(1,8)`), and picking the pair with the smallest numeric difference fails too (e.g. `[7, 8, 15, 16]` — the difference-1 pairs `(7,8)` and `(15,16)` both lose to the "farther apart" pair `(8,15)`, since crossing a power-of-two boundary like `7→8` flips every bit).

## Complexity Analysis
- **Time:** O(n log n) — dominated by the sort; the scan afterward is O(n).
- **Space:** O(1) extra (ignoring the sort's own working space).

## Solution
```java
import java.util.Arrays;

public class Solution {
    public int findMinXor(int[] A) {
        int n = A.length;
        Arrays.sort(A);
        int minXOR = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            int xor = A[i - 1] ^ A[i];
            minXOR = Math.min(minXOR, xor);
        }
        return minXOR;
    }
}
```

## Key Learning
- **Core insight:** minimum-XOR-pair problems reduce to "sort, then scan adjacent pairs only," because XOR-closeness correlates with numeric closeness once the array is sorted.
- **Pattern recognition cue:** "minimum/maximum XOR over all pairs" with large `N` → sort-and-scan-adjacent for the minimum; a bit-trie (greedy MSB matching, same family as [[Maximum AND Pair]]) for the maximum.
- **Common trap:** don't assume the two smallest numbers or the smallest numeric difference gives the answer — both can fail, especially across a power-of-two boundary. Also start the running minimum at `Integer.MAX_VALUE`, not `MIN_VALUE`, so the first real comparison can actually update it.
