# Unset x Bits from Right

## Problem Statement
Given an integer `A`, unset (clear) the rightmost `B` bits of `A` in its binary representation, leaving every bit above that untouched.

For example, if `A = 93` (`1011101` in binary) and `B = 4`, clearing the rightmost 4 bits gives `1010000` = `80`.

**Constraints**
- `1 <= A <= 10^18`
- `1 <= B <= 60`

## Example(s)
**Input:** `A = 93`, `B = 4` → **Output:** `80`
**Input:** `A = 13`, `B = 2` → **Output:** `12` (`1101` → `1100`)

## Approach
This is a masking problem, not a looping-bit-by-bit problem. Build a mask that is `1` everywhere *except* the bottom `B` bits, then AND it with `A`:

1. Start from an all-`1`s value: `~0L` (bitwise NOT of `0`) flips every bit to `1`.
2. Left-shift that all-`1`s value by `B` positions: `~0L << B`. A left shift always fills in `0`s from the right, so this produces a mask with `0`s in the bottom `B` bits and `1`s everywhere above.
3. AND the mask with `A`: bits where the mask is `1` pass through unchanged, bits where the mask is `0` get forced to `0` — exactly "clear the bottom B bits, keep the rest."

Because `B` can be as large as 60, this must be done in `long` (64 bits) — an `int`-based mask would silently break, since Java shift amounts on `int` wrap modulo 32.

**Trigger for next time:** "clear/keep everything above or below some bit boundary" → build a mask with `~0 << k` (or `~0 >>> k`) rather than looping bit-by-bit; loop-based clearing still works but costs O(B) instead of O(1).

## Algorithm
1. Start with `mask = 0L`.
2. Flip every bit: `mask = ~mask` (now all 1s).
3. Shift left by `B`: `mask = mask << B` (bottom `B` bits become 0, rest stay 1).
4. AND with `A`: `A = A & mask`.
5. Return `A`.

## Dry Run
**Example: `A = 93` (`1011101`), `B = 4`**

| Line | Result (binary, 8 bits shown) | Notes |
|------|--------------------------------|-------|
| `mask = 0L` | `00000000` | starts as all zeros |
| `mask = ~mask` | `11111111` | flipped to all ones (64 ones in the real `long`) |
| `mask = mask << 4` | `11110000` | bottom 4 bits become 0 |
| `A = A & mask` | `01011101 & 11110000 = 01010000` | top bits of A survive, bottom 4 forced to 0 |
| `return A` | — | `80` ✅ |

**Smaller check: `A = 13` (`1101`), `B = 2`**
```
mask = 0000 → ~mask = 1111 → mask << 2 = 1100
A & mask = 1101 & 1100 = 1100 = 12   ✅
```

## Why Does This Work?
AND-ing with `1` leaves a bit unchanged, and AND-ing with `0` forces it to `0`. A mask built as `~0L << B` is `0` in exactly its bottom `B` positions (the left-shift pushed zeros in from the right) and `1` everywhere above. So ANDing `A` with this mask is a bit-for-bit rule: everywhere the mask has `1`, `A`'s original bit survives untouched; everywhere it has `0` (the bottom `B` positions), the result is forced to `0` regardless of what `A` had there. That's precisely "unset the rightmost B bits, keep everything else" — done in one O(1) masking operation instead of B separate single-bit clears.

## Complexity Analysis
- **Time:** O(1) — three fixed bitwise operations, independent of `B`. (A brute-force loop clearing one bit at a time, `A & ~(1 << i)` for `i = 0..B-1`, would be O(B) instead.)
- **Space:** O(1)

## Solution
```java
public class Solution {
    public long solve(long A, int B) {
        long mask = 0L;
        mask = ~mask;      // all bits become 1
        mask = mask << B;  // bottom B bits become 0
        A = A & mask;      // top bits survive, bottom B bits cleared
        return A;
    }
}
```

## Key Learning
- **Core insight:** clearing a *range* of low bits at once is the same idea as clearing a single bit (`A & ~(1 << i)`), generalized by shifting an all-1s mask instead of a single-bit mask — `~0 << B` in one line replaces a loop of `B` individual clears.
- **Pattern recognition cue:** "clear/zero out the bottom N bits" or "keep only bits above position N" → build the mask with shift + NOT, don't loop.
- **Common traps:** (1) `~` is **unary** in Java — `A ~ B` is invalid syntax; the clear-a-bit idiom is `A & ~(1 << i)`, an AND *combined with* a NOT, not NOT sitting between two operands. (2) `int` vs `long` — since `B` can reach 60, an `int` mask silently breaks because Java shifts on `int` use `shift % 32`; must use `long` (and the `L` suffix on literals like `0L`) throughout once shift amounts can exceed 31. (3) `|` (OR) can only turn bits on, never off — it's the wrong tool whenever the goal is to *unset* bits; reach for AND with a `0`-containing mask instead.
