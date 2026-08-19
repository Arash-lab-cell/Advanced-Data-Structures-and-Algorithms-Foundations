# Strange Equality

## Problem Statement
Given an integer `A`, define two numbers `X` and `Y`:
- `X` is the **greatest number smaller** than `A` such that the XOR sum of `X` and `A` equals the arithmetic sum of `X` and `A`.
- `Y` is the **smallest number greater** than `A` such that the XOR sum of `Y` and `A` equals the arithmetic sum of `Y` and `A`.

Return `X ^ Y`.

**Constraints**
- `1 <= A <= 10^9`
- The code must run efficiently against up to `100000` test cases.

## Example(s)
**Input:** `A = 10` → **Output:** `21`
`X = 5` (the bitwise complement of `10` within its own 4-bit width), `Y = 16` (the next power of two above `10`'s highest bit), and `5 ^ 16 = 21`.

## Approach
The key identity: `X + A == X ^ A` if and only if `X & A == 0`. This is because addition and XOR only differ where a carry occurs, and a carry only happens where *both* operands have a `1` in the same bit position — so "no shared 1-bit" is exactly the no-carry condition that makes sum and XOR coincide.

**Finding X (greatest number below A, disjoint from A's bits):** count how many bit positions `A` occupies (its bit-width), then flip every one of those positions in `A`. This produces the bitwise complement of `A` *within its own width* — a number guaranteed to be disjoint from `A`'s bits (by construction) and as large as possible while staying below `A` (since it uses every one of `A`'s bit-slots, just inverted; anything even larger and still disjoint would need a bit outside that width, which would make it exceed `A` entirely).

**Finding Y (smallest number above A, disjoint from A's bits):** the smallest number that shares no bits with `A` and exceeds it is the single bit sitting just above `A`'s highest set bit — every bit at or below that width is `0` in this number by construction, so it's automatically disjoint from `A`.

**Trigger for next time:** whenever a problem states "XOR sum equals arithmetic sum," immediately translate that to "these two numbers share no set bits" (`X & A == 0`) rather than reasoning about XOR and addition separately.

## Algorithm
1. Count `A`'s bit-width: repeatedly right-shift a copy of `A` by 1 until it reaches `0`, counting the shifts as `lastBitCount`.
2. Build `X`: start with `X = A`, then flip bits `0` through `lastBitCount - 1` (`X ^= (1 << i)` for each), producing `A`'s complement within its own width.
3. Build `Y = 1 << lastBitCount` — the smallest power of two above `A`'s bit-width, guaranteed disjoint from `A`.
4. Return `X ^ Y`.

## Dry Run
`A = 10` (binary `1010`).

**Phase 1 — counting bit-width:**

| step | copy before | copy after | lastBitCount |
|---|---|---|---|
| 1 | 10 (`1010`) | 5 (`0101`) | 1 |
| 2 | 5 (`0101`) | 2 (`0010`) | 2 |
| 3 | 2 (`0010`) | 1 (`0001`) | 3 |
| 4 | 1 (`0001`) | 0 (`0000`) | 4 |

`lastBitCount = 4` — `A` occupies bit positions `0` through `3`.

**Phase 2 — building X (flip bits 0–3 of A):**

| i | X before | operation | X after |
|---|---|---|---|
| 0 | 10 | `X ^ 1` | `1011` = 11 |
| 1 | 11 | `X ^ 2` | `1001` = 9 |
| 2 | 9 | `X ^ 4` | `1101` = 13 |
| 3 | 13 | `X ^ 8` | `0101` = 5 |

`X = 5`.

**Phase 3 — Y:** `Y = 1 << 4 = 16`.

**Final:** `X ^ Y = 5 ^ 16 = 0101 ^ 10000 = 10101 = 21`.

## Why Does This Work?
`X & A == 0` and `X + A == X ^ A` are equivalent because a carry during addition only ever originates where both numbers have a `1` in the same position — eliminate every shared bit, and addition degenerates into XOR exactly. For `X`, flipping every bit within `A`'s occupied width turns each of `A`'s `1`s into `0`s and each of `A`'s `0`s into `1`s, so the result shares no set bit with `A` by construction, and it's the *largest* such number below `A` because it fully uses every bit-slot `A` has — going any larger while staying disjoint would require a bit beyond `A`'s width, which would make the number exceed `A` rather than sit just below it. For `Y`, the smallest number exceeding `A` while sharing no bits with it must use a bit position `A` doesn't occupy; the lowest such position is immediately above `A`'s highest set bit, giving `Y = 1 << lastBitCount` — disjoint from `A` since `A` has no bits at or above that position.

## Complexity Analysis
- **Time:** O(log A) ≈ O(1) for a fixed-width `int` — bounded by ~32 shifts and ~32 flips.
- **Space:** O(1) — a handful of integer variables.

## Solution
```java
public class Solution {
    public int solve(int A) {
        int copy = A;
        int X = A;
        int lastBitCount = 0;
        while (copy != 0) {
            copy = copy >> 1;
            lastBitCount++;
        }
        for (int i = 0; i < lastBitCount; i++) {
            X = X ^ (1 << i);
        }
        int Y = 1 << (lastBitCount);
        return (X ^ Y);
    }
}
```

## Key Learning
- **Core insight:** "XOR sum equals arithmetic sum" is a direct restatement of "the two numbers share no set bits" (`X & A == 0`) — recognize this identity immediately rather than reasoning about carries from scratch each time.
- **Pattern recognition cue:** "greatest/smallest number satisfying a bit-disjointness condition relative to A" → think in terms of `A`'s occupied bit-width (via repeated right-shift) rather than searching numerically.
- **Common trap:** confusing a bit **count** (1-indexed total, e.g. `lastBitCount = 4` for `A = 10`) with a bit **position** (0-indexed, e.g. highest set bit at position `3`). Since `count = highest position + 1` always, `lastBitCount` already has the "+1" baked in — writing `1 << (lastBitCount + 1)` for `Y` double-counts it and overshoots.
