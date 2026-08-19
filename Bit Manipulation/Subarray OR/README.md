# Subarray OR

## Problem Statement
You are given an array of integers `A` of size `N`. The value of a subarray is the bitwise OR of all its elements. Return the sum of the values of all subarrays of `A`, modulo `10^9 + 7`.

**Constraints**
- `1 <= N <= 10^5`
- `1 <= A[i] <= 10^8`

## Example(s)
**Input:** `A = [6, 1, 6, 3]` → **Output:** `58`
Only bit positions 0, 1, and 2 ever contribute (the largest value is `6 = 110`); their combined contribution across all subarrays sums to `58`.

## Approach
`N` up to `10^5` makes the number of subarrays alone `~5*10^9` — even an `O(n^2)` running-OR approach is far too slow. The way out is to decompose by bit position instead of by subarray: since OR at a given bit depends only on that same bit across the elements, the contribution of each bit position to the final sum can be computed completely independently of every other bit.

For a fixed bit `b`, a subarray's OR has bit `b` set if *at least one* element inside it has bit `b` set. Counting "at least one" directly is awkward, so count the complement: subarrays where *every* element has bit `b` off. This is exactly the maximal-run counting trick from [[Subarrays with OR 1]], just restricted to a single bit and repeated for each of the ~30 bit positions `A[i]` can occupy. If `offCount` is the all-off subarray count for bit `b`, then `onCount = totalSubarray - offCount`, and bit `b` contributes `onCount * 2^b` to the answer.

**Trigger for next time:** "sum of OR (or AND) across all subarrays," or "count subarrays with a property on their OR/AND," with large `N` → decompose by bit position and reuse per-bit maximal-run counting.

## Algorithm
1. Compute `totalSubarray = N(N+1)/2` (as a `long`, to avoid overflow).
2. For each bit position `b` from `0` to `31` (mask `bitValue = 1L << b`):
   - Scan the array left to right, tracking `count0`, the length of the current run of elements with bit `b` off.
   - Whenever an element has bit `b` on, add `count0*(count0+1)/2` to `subarray0` (the all-off subarray count) and reset `count0 = 0`.
   - After the scan, flush the trailing run: add `count0*(count0+1)/2` once more.
   - `subarray1 = totalSubarray - subarray0` — the count of subarrays with bit `b` on.
   - Add `subarray1 * bitValue` to the running answer.
3. Return `ans % (10^9 + 7)`.

## Dry Run
`A = [6, 1, 6, 3]` → `n = 4`, `totalSubarray = 4*5/2 = 10`. Since the largest value is `6` (`110`), only bits 0–2 ever contribute; every higher bit sees `chk = 0` everywhere and adds nothing.

**Bit 0** (`bitValue = 1`): elements `6(0), 1(1), 6(0), 3(1)` → runs of "off" are `[6]` then `[6]`, each length 1 → `subarray0 = 1 + 1 = 2` → `subarray1 = 10 - 2 = 8` → `ans += 8*1 = 8`.

**Bit 1** (`bitValue = 2`): elements `6(on), 1(off), 6(on), 3(on)` → the only "off" run is `[1]`, length 1 → `subarray0 = 1` → `subarray1 = 10 - 1 = 9` → `ans += 9*2 = 18` → running `ans = 26`.

**Bit 2** (`bitValue = 4`): elements `6(on), 1(off), 6(on), 3(off)` → two length-1 "off" runs (`[1]` and `[3]`) → `subarray0 = 1 + 1 = 2` → `subarray1 = 10 - 2 = 8` → `ans += 8*4 = 32` → running `ans = 58`.

**Bits 3–31:** every element has bit off nowhere set beyond bit 2, so `count0` climbs to `4` for the whole array, `subarray0 = 10`, `subarray1 = 0` — no contribution.

**Result:** `ans % (10^9+7) = 58`.

## Why Does This Work?
OR's result at a given bit position depends only on that same bit across the operands — this is what makes each bit position's contribution to the final sum completely independent of every other position, letting the total decompose as `Σ_b (subarrays with bit b on) × 2^b`. "Bit `b` off for the whole subarray" is the same maximal-run condition used in [[Subarrays with OR 1]]: every all-off subarray at bit `b` lies inside exactly one maximal run of elements with bit `b` off, so summing `L(L+1)/2` over those runs counts them exactly once, with no overlap or omission. Subtracting from the total subarray count then gives the exact "bit on" count, and multiplying by `2^b` converts that count into its numeric contribution to the sum of OR-values.

## Complexity Analysis
- **Time:** O(32N) = O(N) — one full array scan per bit position.
- **Space:** O(1) extra, aside from the accumulating `long` answer.

## Solution
```java
public class Solution {
    public int solve(int[] A) {
        int n = A.length;
        long totalSubarray = (long) n * (n + 1) / 2;
        long ans = 0;
        for (int i = 0; i < 32; i++) {
            long count0 = 0;
            long subarray0 = 0;
            long subarray1 = 0;
            long bitValue = 1L << (31 - i);
            for (int j = 0; j < n; j++) {
                long chk = A[j] & bitValue;
                if (chk == 0) {
                    count0++;
                } else {
                    subarray0 = subarray0 + (count0 * (count0 + 1) / 2);
                    count0 = 0;
                }
            }
            subarray0 = subarray0 + (count0 * (count0 + 1) / 2);
            subarray1 = totalSubarray - subarray0;
            ans = ans + subarray1 * bitValue;
        }
        long mod = 1000000007L;
        return (int) (ans % mod);
    }
}
```

## Key Learning
- **Core insight:** "sum of a bitwise OR/AND value across all subarrays" decomposes per bit position, turning an `O(n^2)`-subarray problem into ~30 independent `O(n)` run-counting passes.
- **Pattern recognition cue:** "sum of OR/AND of all subarrays" or "count subarrays with an OR/AND property," large `N` → decompose by bit, reuse the maximal-run `L(L+1)/2` trick per bit; this generalizes directly from [[Subarrays with OR 1]].
- **Common trap:** `int` overflow — `N(N+1)/2` and `count*(count+1)/2` can exceed `int` range when `N` is up to `10^5`, so every count/sum must be a `long`, and the arithmetic itself (not just the storage variable) must happen in `long`. Never use `Math.pow()` for powers of two here (it returns a precision-losing `double`) — use `1L << b`, and declare the modulus as a `long` constant (`1000000007L`) directly.
