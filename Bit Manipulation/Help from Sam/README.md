# Help from Sam

## Problem Statement
Alex has a target score of `A`. Starting from a score of `0`, Alex can either **double** his current score by solving a question, or ask Sam for help to add exactly **+1** to his score. Alex wants his final score to be exactly `A`, using Sam's help as few times as possible. Return the minimum number of times Alex needs Sam's help.

**Constraints**
- `0 <= A <= 10^9`

## Example(s)
**Input:** `A = 13` → **Output:** `3`
`13` in binary is `1101`, which has three `1` bits — matching the minimum-help count of 3.

## Approach
Think about how any binary number is built by hand from its digits, left to right: `result = 0`, then repeatedly `result = result * 2 + digit`. This "double, then maybe add one" process is *exactly* Alex's two moves: doubling maps to `result * 2` (free), and Sam's help maps to `+ digit` — but Sam can only ever contribute `+1`, so his help is only needed on the steps where `digit = 1`. Steps where `digit = 0` need no addition at all — just a free double.

So the minimum number of times Sam's help is required is exactly the number of `1` bits in the binary representation of `A` — its **popcount**.

**Trigger for next time:** whenever a problem's two operations are "double a value" and "add exactly one," and the target is a fixed number, suspect that the answer reduces to counting set bits in that number's binary form.

## Algorithm
1. If `A == 0`, return `0` immediately (no moves needed).
2. Initialize `samHelp = 0`.
3. For each bit position `i` from `0` to `31`, check whether bit `i` of `A` is set (`A & (1 << i) != 0`); if so, increment `samHelp`.
4. Return `samHelp`.

## Dry Run
`A = 13` → binary `1101`

Hand-building 13 via double-and-add, most significant digit first (`1`, `1`, `0`, `1`):

| Step | digit | result = result×2 + digit | Sam's help used? | Running Sam count |
|------|-------|-----------------------------|-------------------|--------------------|
| start | — | 0 | — | 0 |
| 1 | 1 | 0×2+1 = 1 | Yes (can't double 0 to get 1) | 1 |
| 2 | 1 | 1×2+1 = 3 | Yes | 2 |
| 3 | 0 | 3×2+0 = 6 | No (double only) | 2 |
| 4 | 1 | 6×2+1 = 13 | Yes | 3 |

**Result:** `3` — matches popcount(13) = three `1` bits in `1101`. The bit-scanning solution arrives at the same count by scanning `A`'s bits directly (order doesn't matter for a pure count), rather than replaying the digit-by-digit build.

## Why Does This Work?
Any nonzero binary number's value equals `(((d_k)×2 + d_{k-1})×2 + ... )×2 + d_0` when built most-significant-digit-first — this is the standard "double and add the next digit" construction, equivalent to Horner's method. Alex's two allowed moves map onto exactly this construction's two operations: doubling is free and always available, while "+1" is only ever needed exactly where a digit is `1` (a digit of `0` needs no addition step at all). Since every `1` digit requires exactly one use of Sam's help and every `0` digit requires none, the total number of times Sam's help is used across the whole construction equals the count of `1` digits in `A`'s binary form — and this is optimal because there's no cheaper way to inject a `+1` into the running total than one use of Sam's help per required addition.

## Complexity Analysis
- **Time:** O(32) ≈ O(log A) — one check per bit of `A`.
- **Space:** O(1) — a single counter variable.

## Solution
```java
public class Solution {
    public int solve(int A) {
        if (A == 0) {
            return 0;
        }
        int samHelp = 0;
        int i = 0;
        while (i < 32) {
            int n = A & (1 << i);
            if (n != 0) {
                samHelp++;
            }
            i++;
        }
        return samHelp;
    }
}
```

## Key Learning
- **Core insight:** "double a running value, optionally add exactly one" building toward a fixed target is a direct restatement of binary construction — the answer is the popcount of the target.
- **Pattern recognition cue:** operations limited to "×2" and "+1" (or symmetric variants like "÷2" and "-1" working backward from the target) are a strong signal to think in binary digits rather than simulate the process step by step.
- **Common trap:** don't double-count the leading bit. It's tempting to special-case "the first digit always needs Sam's help" (true, since you can't double `0` into a `1`) — but that first `1` bit is already counted naturally by a bit-scanning loop over all of `A`'s bits, so pre-adding `1` to the counter *and* scanning all bits both counts it twice. Only pre-seed a counter like this if you're deliberately excluding that bit from the main scan.
