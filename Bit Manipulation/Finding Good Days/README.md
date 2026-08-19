# Finding Good Days

## Problem Statement
Alex has a cat named Boomer. He starts on day 1 with one unit of food in the stash, and the stash doubles every following day. If Boomer is well-behaved on a given day, she receives food worth exactly that day's stash; otherwise she receives nothing that day.

Boomer receives a total of `A` units of food over time. Return the number of days on which she received the stash.

**Constraints**
- `1 <= A <= 2^31 - 1`

## Example(s)
**Input:** `A = 13` → **Output:** `3`
`13` in binary is `1101` — three `1` bits, so three days contributed: day 1 (`2^0 = 1`), day 3 (`2^2 = 4`), and day 4 (`2^3 = 8`), summing to `1 + 4 + 8 = 13`.

## Approach
Day `i`'s stash is exactly `2^(i-1)` — a distinct power of two. Boomer either receives that day's stash in full or gets nothing, so the total `A` she receives is a sum of some subset of distinct powers of two. A sum of distinct powers of two has exactly one representation — its binary form — so the days she was well-behaved are precisely the bit positions that are set in `A`. Counting the days therefore reduces to counting `A`'s set bits (its popcount).

**Trigger for next time:** whenever a story describes a total built by summing a subset of a doubling sequence ("stash doubles every day, some days count, total is `A`"), read it directly as "count the set bits of `A`" rather than simulating day by day.

## Algorithm
1. Initialize `count = 0`.
2. For each bit position `i` from `0` to `31`, check whether bit `i` of `A` is set (`A & (1 << i) != 0`); if so, increment `count`.
3. Return `count`.

## Dry Run
`A = 13` → binary `1101`.

| i | `1 << i` | `A & (1<<i)` | Bit set? | count |
|---|---|---|---|---|
| 0 | `0001` | `1101 & 0001 = 0001` | Yes | 1 |
| 1 | `0010` | `1101 & 0010 = 0000` | No | 1 |
| 2 | `0100` | `1101 & 0100 = 0100` | Yes | 2 |
| 3 | `1000` | `1101 & 1000 = 1000` | Yes | 3 |
| 4–31 | ... | all `0` | No | 3 |

**Result:** `count = 3` — Boomer received the stash on 3 days (days 1, 3, and 4), matching `1 + 4 + 8 = 13`.

## Why Does This Work?
Because each day's stash is a *distinct* power of two and every power of two contributes to the sum at most once (Boomer either gets that whole day's stash or nothing), the total `A` is, by definition, the binary number whose 1-bits mark exactly the days that contributed. This is nothing more than the definition of binary representation applied to the story — there's no ambiguity to resolve, since a sum of distinct powers of two has one and only one decomposition. Reading off `A`'s bits therefore *is* reading off the good days, and counting them is exactly `A`'s popcount.

## Complexity Analysis
- **Time:** O(32) ≈ O(1) — one check per bit of a fixed-width `int`.
- **Space:** O(1) — a single counter.

## Solution
```java
public class Solution {
    public int solve(int A) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            int n = A & (1 << i);
            if (n != 0) {
                count++;
            }
        }
        return count;
    }
}
```

## Key Learning
- **Core insight:** a total built by summing a subset of a "doubles every step" sequence is just a binary number in disguise — decode it directly instead of simulating.
- **Pattern recognition cue:** "doubles every day/step," "only some steps count," "total received is `A`" → popcount / binary-decomposition problem.
- **Common trap:** don't try to reconstruct *which* days were good by guesswork or greedy subtraction — the binary representation already answers that unambiguously, since distinct powers of two never overlap in a subset sum.
