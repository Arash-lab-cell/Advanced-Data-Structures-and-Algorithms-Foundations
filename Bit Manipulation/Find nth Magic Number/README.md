# Find nth Magic Number

## Problem Statement
Given an integer `A`, find and return the `A`-th magic number.

A magic number is defined as a number that can be expressed as a power of 5, or as a sum of **unique** powers of 5. The first few magic numbers, in order, are: `5, 25, 30 (=5+25), 125, 130 (=125+5), ...`

**Constraints**
- `1 <= A <= 5000`

## Example(s)
**Input:** `A = 11` → **Output:** `655`
`11` in binary is `1011` — bits set at positions 0, 1, 3 → `5^1 + 5^2 + 5^4 = 5 + 25 + 625 = 655`.

## Approach
Every magic number is built by choosing, independently, whether to include each power of 5 (`5^1, 5^2, 5^3, ...`) — each power is either fully used once or not used at all, exactly like a subset-selection problem. That "use it / don't use it" structure per position is *exactly* what a binary digit represents.

So the `A`-th magic number can be read directly off the binary representation of `A` itself: the bit at position `i` (0-indexed from the right) tells you whether to include `5^(i+1)` in the sum. Instead of generating and sorting all possible sums to find the `A`-th one, we jump straight there by decoding `A`'s own bits.

**Trigger for next time:** whenever a sequence is described as "sums of unique powers of X, in increasing order," suspect a direct bit-to-power-of-X mapping rather than generation + sorting — this is the same subset-enumeration-via-binary idea used in bitmask DP and subset generation.

## Algorithm
1. Initialize `sum = 0` and a running `power` starting at `5^1`.
2. For each bit position `i` from `0` to `31`:
   - Check whether bit `i` of `A` is set: `A & (1 << i)`.
   - If set, add the current `power` (representing `5^(i+1)`) to `sum`.
   - Advance `power` to the next power of 5 (multiply by 5) for the next iteration.
3. Return `sum`.

## Dry Run
`A = 11` (binary `1011`), `sum = 0`, `power = 5` (ready for bit 0 → `5^1`)

| i | power (this iter.) | bit i of A | Action | sum after |
|---|---------------------|------------|--------|-----------|
| 0 | 5 | 1 (`...1011`, LSB=1) | bit set → `sum += 5` | 5 |
| 1 | 25 | 1 | bit set → `sum += 25` | 30 |
| 2 | 125 | 0 | skip | 30 |
| 3 | 625 | 1 | bit set → `sum += 625` | 655 |
| 4–31 | (keeps ×5) | 0 (no more set bits) | skip | 655 |

**Result:** `655` — matches `5^1 + 5^2 + 5^4 = 5 + 25 + 625`.

## Why Does This Work?
There's a bijection between binary numbers and subsets of powers of 5: for `n` powers of 5 available, there are exactly `2^n` possible on/off combinations, matching exactly the `2^n` distinct `n`-bit binary numbers. Because binary numbers `1, 2, 3, ...` enumerate subsets in the natural order of "which bits are on," and larger powers of 5 dominate smaller ones in any sum (a single higher power always exceeds the sum of every lower power combined, similar to place-value systems), the ordering of magic numbers by value matches the ordering of their corresponding binary numbers by value. So the `A`-th magic number is obtained by directly decoding `A`'s bit pattern into the corresponding powers of 5 — no separate sorting step is needed.

## Complexity Analysis
- **Time:** O(32) ≈ O(log A) — only as many bit positions as `A` actually has need checking.
- **Space:** O(1) — a running sum and power variable, no extra storage.

## Solution
```java
public class Solution {
    public int solve(int A) {
        int i = 0;
        int sum = 0;
        int power = 5;
        while (i < 32) {
            int n = A & (1 << i);
            if (n != 0) {
                sum = sum + power;
            }
            power = power * 5;
            i++;
        }
        return sum;
    }
}
```

## Key Learning
- **Core insight:** "sums of unique powers of X, in increasing order" sequences map bit-for-bit onto the binary representation of the index — decode the index's bits directly instead of generating and sorting all combinations.
- **Pattern recognition cue:** any "generate the Nth item of a set built from independent yes/no choices" problem (subsets, power sums, combinations) where the target ordering matches binary counting order is a candidate for this direct-decode trick.
- **Common traps:** (1) off-by-one on the exponent — bit `i` maps to `5^(i+1)`, not `5^i`, since the powers start at `5^1`; (2) avoid `Math.pow` for this — it returns a `double` and can introduce floating-point rounding errors (e.g. `124.999999998` truncating to `124` instead of `125`) for large integer powers, so track the power as a running `int`/`long` multiplied by 5 each iteration instead; (3) always cast/track types consistently — mixing `double` arithmetic into an otherwise integer computation is a common source of silent bugs here.
