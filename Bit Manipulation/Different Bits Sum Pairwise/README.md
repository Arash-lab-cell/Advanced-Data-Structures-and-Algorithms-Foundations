# Different Bits Sum Pairwise

## Problem Statement

Define `f(X, Y)` as the number of differing corresponding bits in the binary representations of `X` and `Y`. For example, `f(2, 7) = 2`, since `2 = 010` and `7 = 111` differ in the first and third bits.

Given an array of `N` positive integers `A[1..N]`, find the sum of `f(A[i], A[j])` over all ordered pairs `(i, j)` with `1 <= i, j <= N`. Return the answer modulo `10^9 + 7`.

**Constraints:**
- `1 <= N <= 10^5`
- `1 <= A[i] <= 2^31 - 1`

**Input format:** a single integer array `A`.
**Output format:** a single integer — the sum, modulo `10^9 + 7`.

## Example(s)

**Input 1:** `A = [1, 3, 5]` → **Output:** `8`

| Pair | Binary | f(i,j) |
|---|---|---|
| (1,1) | 001,001 | 0 |
| (1,3) | 001,011 | 1 |
| (1,5) | 001,101 | 1 |
| (3,1) | 011,001 | 1 |
| (3,3) | 011,011 | 0 |
| (3,5) | 011,101 | 2 |
| (5,1) | 101,001 | 1 |
| (5,3) | 101,011 | 2 |
| (5,5) | 101,101 | 0 |

Sum = 8.

**Input 2:** `A = [2, 3]` → **Output:** `2` — `f(2,2)+f(2,3)+f(3,2)+f(3,3) = 0+1+1+0 = 2`.

## Approach

Brute force compares every pair directly: for each `(i, j)`, XOR the numbers and count set bits. That's `O(N^2 * 32)`, which is way too slow for `N = 10^5` (up to `10^10` operations).

The trigger to look for: whenever a cost is defined per bit position and summed over all pairs, stop thinking pair-by-pair and start thinking bit-by-bit, position-by-position. Bits are independent of each other — the contribution of bit position `i` to the total answer doesn't depend on any other bit position. So instead of iterating over `N^2` pairs, iterate over the 31 bit positions, and for each position count how the array's bits split.

## Algorithm

1. Initialize `total = 0`.
2. For each bit position `i` from `0` to `30`:
   a. Count `c` = number of elements in `A` whose bit `i` is set.
   b. The number of elements whose bit `i` is `0` is `N - c`.
   c. Every pair where one element has bit `i` set and the other doesn't contributes exactly 1 to `f` at this position — `2 * c * (N - c)` such ordered pairs.
   d. Add `2 * c * (N - c)` to `total`, taking `mod 10^9 + 7`.
3. Return `total`.

## Dry Run

`A = [1, 3, 5]` → binary: `1 = 001`, `3 = 011`, `5 = 101`.

- Bit 0: bits are `1, 1, 1` → c=3, unset=0 → contribution `2*3*0 = 0`.
- Bit 1: bits are `0, 1, 0` → c=1, unset=2 → contribution `2*1*2 = 4`.
- Bit 2: bits are `0, 0, 1` → c=1, unset=2 → contribution `2*1*2 = 4`.

Total = `0 + 4 + 4 = 8`. Matches the expected output.

## Why Does This Work?

`f(X, Y)` is additive across bit positions: `f(X, Y) = sum over bit positions of [bit_i(X) != bit_i(Y)]`. Since summation is linear, summing over all pairs and summing over bit positions can be swapped. For a fixed bit position, a pair disagrees exactly when one number has the bit set and the other doesn't — a simple counting problem. This decomposition is exact, which is why per-bit counting gives the precise answer.

## Complexity Analysis

- **Time:** O(31 * N) ≈ O(N) — 31 passes over the array.
- **Space:** O(1) extra space.

## Solution

```java
public class Solution {
    public int cntBits(int[] A) {
        int n = A.length;
        long total = 0;
        long mod = 1_000_000_007L;
        for (int i = 0; i < 31; i++) {
            long c = 0;
            for (int j = 0; j < A.length; j++) {
                int bit = (A[j] >> i) & 1;
                if (bit == 1) {
                    c++;
                }
            }
            long contribution = 2 * c * (A.length - c);
            total = (total + contribution) % mod;
        }
        return (int) total;
    }
}
```

## Key Learning

- **Core insight:** when a metric is defined bit-by-bit and summed over all pairs, decompose by bit position instead of by pair.
- **How to spot this pattern again:** look for phrases like "differing bits," "XOR of all pairs," "sum of Hamming distances" — any pairwise cost that is secretly a sum over ~30 independent bit positions.
- **Common variants/traps:** use `long` for counts and totals to avoid overflow before the modulo. This problem wants ordered pairs including i == j.
