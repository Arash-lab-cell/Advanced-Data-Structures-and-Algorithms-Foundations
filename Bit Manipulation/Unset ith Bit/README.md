# Unset i-th Bit

## Problem Statement
Given two integers `A` and `B`:
- If the `B`-th bit in `A` is set, unset it.
- If the `B`-th bit in `A` is already unset, leave `A` unchanged.

Return the updated value of `A`. Bit positions are 0-indexed (LSB has index 0).

**Constraints**
- `1 <= A <= 10^9`
- `0 <= B <= 30`

## Example(s)
**Input:** `A = 7 (0111)`, `B = 1`
**Output:** `5 (0101)` — bit 1 was set, so it gets cleared.

**Input:** `A = 5 (0101)`, `B = 1`
**Output:** `5 (0101)` — bit 1 is already unset, so `A` is returned unchanged.

## Approach
First isolate bit `B` of `A` using AND with a shifted mask: `n = A & (1 << B)`. This tells us whether the bit is currently set (`n != 0`) or unset (`n == 0`).

If it's set, `n` is exactly a single bit equal to that same bit of `A` — XOR-ing `n` back into `A` flips only that one matching bit off (since `x ^ x = 0` at that position, and every other bit of `n` is `0` so it leaves the rest of `A` untouched). If it's already unset, there's nothing to do — return `A` as-is.

**Trigger for next time:** "check/read a specific bit" → AND with `1 << B`. "Turn a *known-set* bit off" → XOR the number with a mask that has just that bit on.

## Algorithm
1. Compute `n = A & (1 << B)` to isolate bit `B`.
2. If `n != 0` (bit was set): return `n ^ A` (clears exactly that bit).
3. Otherwise (bit was already unset): return `A` unchanged.

## Dry Run
**Case: bit set.** `A = 111 (7)`, `B = 1`
```
1 << B      = 010
n = A & mask = 111 & 010 = 010   (n != 0, bit was set)
n ^ A       = 010 ^ 111 = 101 (= 5)   ✅ bit 1 cleared
```
**Case: bit unset.** `A = 101 (5)`, `B = 1`
```
1 << B      = 010
n = A & mask = 101 & 010 = 000   (n == 0, bit already unset)
return A unchanged = 101 (5)     ✅
```

## Why Does This Work?
AND-ing `A` with `1 << B` zeroes out every bit except position `B`, so the result is either `0` (bit unset) or exactly the value `1 << B` (bit set) — never anything else, because every other bit of the mask is `0` and forces the corresponding bit of `A` to `0` in the result. When the bit is set, `n` equals `1 << B`, which is precisely a mask with a single `1` at position `B`; XOR-ing that mask into `A` toggles only that bit, and since we already know it was `1`, toggling it can only turn it to `0`. Every other bit of `A` gets XORed with `0` and stays unchanged.

## Complexity Analysis
- **Time:** O(1) — a fixed number of bitwise operations regardless of input size.
- **Space:** O(1) — no extra memory beyond a couple of integers.

## Solution
```java
public class Solution {
    public int solve(int A, int B) {
        int n = A & (1 << B);
        if (n != 0) {
            return n ^ A;
        }
        return A;
    }
}
```

## Key Learning
- **Core insight:** "read a bit" is AND with a shifted `1`; "clear a bit you know is set" is XOR with that same isolated bit value.
- **Pattern recognition cue:** any problem phrased as "if bit B is set, do X, else leave it" is a two-step AND-then-conditional-XOR pattern — check first, mutate only if needed.
- **Common trap / variant:** don't confuse this with the general "clear bit B regardless of its current state" operation, which is `A & ~(1 << B)` (works whether the bit was 0 or 1, and is simpler — no branch needed). The XOR-based approach here only works safely *because* we already confirmed the bit is set; XOR-ing an already-unset bit with itself-as-mask would incorrectly set it. See "Toggle i-th Bit" in this folder for the branch-free unconditional-flip version.
