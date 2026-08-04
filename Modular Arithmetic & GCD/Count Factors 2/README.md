# Count Factors 2

## Problem Statement

Given an integer `A`, find the count of its factors. A factor of `A` is a number that divides `A` completely, leaving no remainder.

**Input:** An integer `A`.
**Output:** An integer — the count of factors of `A`.

**Example of factors:** `1, 2, 3, 6` are factors of `6`.

## Example(s)

| Input (A) | Factors | Output | Why |
|---|---|---|---|
| 6 | 1, 2, 3, 6 | 4 | Four numbers divide 6 evenly |
| 36 | 1, 2, 3, 4, 6, 9, 12, 18, 36 | 9 | 6 is a perfect-square factor, counted once |
| 7 | 1, 7 | 2 | 7 is prime — exactly two factors |

## Approach

Brute force checks every `i` from `1` to `A` and tests `A % i == 0` — O(A) time, and it recomputes work it doesn't need to.

The optimization: factors of `A` always come in **pairs** — if `i` divides `A`, then `A / i` also divides `A`, and `i * (A / i) = A`. So it's only necessary to search `i` up to `√A`; every factor pair is discovered from that range. The one wrinkle: when `A` is a perfect square, the pair `(√A, √A)` collapses into a single distinct factor, so it must be counted once instead of twice.

**Trigger / tell:** any "count/find divisors of N" or "is N prime" question (primality is just "does N have exactly 2 factors") — especially with large constraints where O(N) brute force would time out — is a signal to reach for the `√N` factor-pairing pattern.

## Algorithm

1. Initialize `countFactors = 0`.
2. Iterate `i` from `1` while `i * i <= A`.
3. If `A % i == 0`:
   - If `i == A / i` (perfect square case), increment `countFactors` by `1`.
   - Otherwise, increment `countFactors` by `2` (both `i` and `A / i` are distinct factors).
4. Return `countFactors`.

## Dry Run

`A = 36`

```
i = 1: 36 % 1 == 0, 1 ≠ 36 → count += 2 → count = 2   (factors 1, 36)
i = 2: 36 % 2 == 0, 2 ≠ 18 → count += 2 → count = 4   (factors 2, 18)
i = 3: 36 % 3 == 0, 3 ≠ 12 → count += 2 → count = 6   (factors 3, 12)
i = 4: 36 % 4 == 0, 4 ≠ 9  → count += 2 → count = 8   (factors 4, 9)
i = 5: 36 % 5 ≠ 0 → skip
i = 6: 6*6 = 36 ≤ 36, 36 % 6 == 0, 6 == 6 → count += 1 → count = 9   (factor 6, counted once)
i = 7: 7*7 = 49 > 36 → loop ends

Return 9
```

Actual factors of 36: `1, 2, 3, 4, 6, 9, 12, 18, 36` → 9 factors. Matches.

## Why Does This Work?

Every factor of `A` pairs with a complementary factor: if `i` divides `A`, then `A / i` divides `A` too, and one of the pair is always `≤ √A` while the other is `≥ √A`. Scanning `i` only up to `√A` is therefore guaranteed to surface both members of every factor pair — nothing above the square root is ever missed. The only case where the "pair" isn't really two distinct numbers is when `A` is a perfect square and `i == A / i`; that's why it's counted once instead of twice, otherwise it would be double-counted.

## Complexity Analysis

**Time:** O(√A) — the loop runs only while `i * i <= A`, i.e. `i` up to `√A`.
**Space:** O(1) — only a counter variable is used.

## Solution

```java
public class Solution {
    public int solve(int A) {
        int countFactors = 0;
        for (int i = 1; i * i <= A; i++) {
            if (A % i == 0) {
                if (i == A / i) {
                    countFactors++;
                } else {
                    countFactors = countFactors + 2;
                }
            }
        }
        return countFactors;
    }
}
```

## Key Learning

- **Core insight:** factors of `N` pair up around `√N` — you never need to check divisors beyond `√N` to find them all.
- **How to spot this pattern again:** any "count/list all divisors of N" or "is N prime" question, especially where `N` is large enough that O(N) brute force would time out, is signaling the `√N` factor-pairing pattern.
- **Common variants/traps:**
  - Forgetting the perfect-square edge case is the single most common bug here — always test with a perfect-square input (like `36` or `4`) to catch double-counting.
  - The exact same `√N` loop structure underlies primality testing (no factors found between `2` and `√N`) and factor *listing* (collect instead of count) — recognize it as one pattern with several uses.
  - If factors of many different numbers are needed repeatedly (e.g. across a range of queries), precomputing smallest prime factors with a sieve is the next-level optimization beyond a per-query `√N` scan.
