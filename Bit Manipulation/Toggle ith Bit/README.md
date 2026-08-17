# Toggle i-th Bit

## Problem Statement
Given two integers `A` and `B`:
- If the `B`-th bit in `A` is set, make it unset.
- If the `B`-th bit in `A` is unset, make it set.

Return the updated value of `A`.

**Constraints**
- `1 <= A <= 10^9`
- `0 <= B <= 30`

## Example(s)
**Input:** `A = 7 (0111)`, `B = 1` → **Output:** `5 (0101)` — bit 1 was `1`, gets cleared.
**Input:** `A = 5 (0101)`, `B = 1` → **Output:** `7 (0111)` — bit 1 was `0`, gets set.

## Approach
This extends "Unset i-th Bit" (see companion problem) to the case where an *unset* bit should also be handled — flipped on instead of left alone. Isolate bit `B` with `n = A & (1 << B)`. If it's set, XOR it out (same trick as before). If it's unset, XOR `A` directly with `1 << B` to turn that bit on.

Both branches are actually doing the exact same underlying operation — XOR-ing `A` with a mask that has only bit `B` set always flips that bit, whichever direction it needs to go. The branch is only there for explicitness/readability; it isn't logically necessary.

**Trigger for next time:** "toggle/flip a bit regardless of its current state" → XOR with a single-bit mask, no conditional required: `A ^ (1 << B)`.

## Algorithm
1. Compute `n = A & (1 << B)` to check the current state of bit `B`.
2. If `n != 0` (bit set): compute `n = n ^ A` (clears it).
3. Else (bit unset): compute `n = A ^ (1 << B)` (sets it).
4. Return `n`.

*(Equivalent one-line version: `return A ^ (1 << B);` — see "Why Does This Work?" below.)*

## Dry Run
**Case 1 — bit is 1 → cleared.** `A = 7 (0111)`, `B = 1`
```
1 << B = 0010
n = A & mask = 0111 & 0010 = 0010        (n != 0 → if-branch)
n = n ^ A    = 0010 ^ 0111 = 0101 (5)    ✅
```
**Case 2 — bit is 0 → set.** `A = 5 (0101)`, `B = 1`
```
1 << B = 0010
n = A & mask = 0101 & 0010 = 0000        (n == 0 → else-branch)
n = A ^ mask = 0101 ^ 0010 = 0111 (7)    ✅
```

## Why Does This Work?
XOR-ing any bit with `1` always flips it (`0^1=1`, `1^1=0`), while XOR-ing with `0` leaves it unchanged. A mask of `1 << B` is `1` at position `B` and `0` everywhere else, so `A ^ (1 << B)` flips exactly bit `B` and leaves every other bit of `A` untouched — regardless of whether that bit started as `0` or `1`. The two branches in the code above are just two disguises of this same identity: the `if` branch computes `n ^ A` where `n` happens to equal `A`'s bit-`B` value, which reduces to clearing that bit; the `else` branch computes `A ^ (1<<B)` directly. Both are special cases of the single unconditional XOR.

## Complexity Analysis
- **Time:** O(1)
- **Space:** O(1)

## Solution
```java
public class Solution {
    public int solve(int A, int B) {
        int n = A & (1 << B);
        if (n != 0) {
            n = n ^ A;
        } else {
            n = A ^ (1 << B);
        }
        return n;
    }
}
// Equivalent, simpler form:
// return A ^ (1 << B);
```

## Key Learning
- **Core insight:** toggling a bit is always XOR with a single-bit mask — no need to check the current state first.
- **Pattern recognition cue:** the word "toggle" (or "flip") applied to a specific bit position is an immediate signal for `x ^ (1 << i)`, contrasted with "set" (`x | (1 << i)`) and "unset/clear" (`x & ~(1 << i)`) which are one-directional and don't need XOR.
- **Common trap:** writing branching code (as shown above) isn't wrong, but it's unnecessary — a good interview follow-up is to notice and collapse it to the one-liner, which also avoids any risk of getting a branch's logic backwards under time pressure.
