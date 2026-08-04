# Number of 1 Bits

## Problem Statement

Write a function that takes an integer `A` and returns the number of 1 bits (set bits) present in its binary representation.

**Input:** An integer `A`.
**Output:** An integer — the count of set bits (`1`s) in the binary representation of `A`.

## Example(s)

| Input (A) | Binary | Output | Why |
|---|---|---|---|
| 11 | 1011 | 3 | Three `1`s at positions 0, 1, 3 |
| 7 | 111 | 3 | All three bits are set |
| 8 | 1000 | 1 | Only the 4th bit is set |

## Approach

Since the question is about bit-level properties of a number, the natural first move is to get at the individual bits. The simplest way to do that in Java is to convert `A` into its binary string form with `Integer.toBinaryString(A)`, then walk the string and count how many characters are `'1'`.

**Trigger / tell:** any problem that talks about a number's binary representation, set/unset bits, or bit patterns is a signal to reach for bit-level inspection — either via a string conversion (as here) or, once comfortable with bitwise operators, via bit tricks directly on the integer (see Key Learning).

## Algorithm

1. Convert `A` to its binary string using `Integer.toBinaryString(A)`.
2. Initialize `count = 0`.
3. Iterate over every character in the binary string.
4. If the character is `'1'`, increment `count`.
5. Return `count`.

## Dry Run

`A = 11`

```
binary = "1011", n = 4

i = 0: '1' → count = 1
i = 1: '0' → count = 1 (unchanged)
i = 2: '1' → count = 2
i = 3: '1' → count = 3

Return 3
```

`11` in binary is `1011` — three set bits. Matches.

## Why Does This Work?

`Integer.toBinaryString(A)` produces an exact, lossless base-2 representation of `A` with no leading zeros. Each character in that string corresponds to exactly one bit position of `A` — a `'1'` character is a set bit, a `'0'` is not. Iterating over every character and counting the `'1'`s therefore counts every set bit exactly once, with nothing skipped or double-counted.

## Complexity Analysis

**Time:** O(log A) — both `toBinaryString` and the loop scale with the bit-length of `A` (≈ log₂A), not with `A` itself.
**Space:** O(log A) — for storing the binary string.

## Solution

```java
public class Solution {
    public int numSetBits(int A) {
        int noOf1bits = 0;
        // method to convert integer to binary string
        String binary = Integer.toBinaryString(A);
        int n = binary.length();
        for (int i = 0; i < n; i++) {
            if (binary.charAt(i) == '1') {
                noOf1bits++;
            }
        }
        return noOf1bits;
    }
}
```

## Key Learning

- **Core insight:** a number's binary representation exposes its bits directly as characters, so string iteration is a valid (if not the most optimal) way to inspect bits.
- **How to spot this pattern again:** any question about set bits, bit patterns, or binary properties of a number is a bit-manipulation signal — even before you're fluent in bitwise operators, converting to a string is a legitimate bridge technique.
- **Common variants/traps:**
  - Java's built-in `Integer.bitCount(A)` does exactly this in one call — no need to hand-roll it unless the interviewer explicitly asks you to implement it yourself.
  - The classic optimal trick is **Brian Kernighan's algorithm**: repeatedly do `n = n & (n - 1)` to clear the lowest set bit, counting iterations until `n == 0`. This runs in O(number of set bits) rather than O(log A), and is usually what an interviewer is fishing for when they ask "can you do better than checking every bit?"
  - `Integer.toBinaryString` on a **negative** int returns its 32-bit two's-complement form (e.g. `-1` → thirty-two `1`s) — worth double-checking against the problem's constraints if negative inputs are allowed.
