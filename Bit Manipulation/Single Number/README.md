# Single Number

## Problem Statement
Given an array of integers `A`, every element appears twice except for one. Find that integer that occurs once.

Your algorithm should run in linear time and should ideally use no extra memory.

**Constraints**
- `1 <= |A| <= 2000000`
- `0 <= A[i] <= INT_MAX`

## Example(s)
**Input:** `A = [4, 1, 2, 1, 2]`
**Output:** `4`

`1` and `2` each appear twice and cancel each other out; `4` is the only element left standing, so it's the answer.

## Approach
XOR has two properties that make it perfect here: `x ^ x = 0` (a number cancels itself out) and `x ^ 0 = x` (XOR with the identity element leaves a number unchanged). It's also commutative and associative, so the order elements are XORed in doesn't matter.

If we XOR every element of the array together, every pair of duplicates cancels to `0`, and we're left with only the unique element XORed with `0` — i.e., the unique element itself.

**Trigger for next time:** "every element appears twice except one" (or any variant — "appears an even number of times except one") is the signature of an XOR-cancellation problem. Reach for a running XOR accumulator, not a hash set, whenever the problem promises O(1) space.

## Algorithm
1. Initialize `result = 0` (the XOR identity element).
2. Iterate over every element `A[i]` in the array.
3. Update `result = result ^ A[i]`.
4. After the loop, `result` holds the unique element. Return it.

## Dry Run
Input: `A = [4, 1, 2, 1, 2]`

| i | A[i] | binary | result (before) | result = result ^ A[i] | binary (after) |
|---|------|--------|------------------|-------------------------|-----------------|
| 0 | 4 | 0100 | 0 (0000) | 0000 ^ 0100 | 0100 (4) |
| 1 | 1 | 0001 | 4 (0100) | 0100 ^ 0001 | 0101 (5) |
| 2 | 2 | 0010 | 5 (0101) | 0101 ^ 0010 | 0111 (7) |
| 3 | 1 | 0001 | 7 (0111) | 0111 ^ 0001 | 0110 (6) |
| 4 | 2 | 0010 | 6 (0110) | 0110 ^ 0010 | 0100 (4) |

**Return:** `4` ✅

## Why Does This Work?
XOR is commutative and associative, so XOR-ing the whole array is equivalent to grouping every value with its duplicate first: `(a ^ a) ^ (b ^ b) ^ ... ^ unique`. Each duplicate pair collapses to `0`, and `0 ^ 0 ^ ... ^ unique = unique`, since `0` is the identity element for XOR. The invariant holds regardless of array order, which is what guarantees a single linear pass is enough — no sorting or grouping required first.

## Complexity Analysis
- **Time:** O(N) — one pass over the array.
- **Space:** O(1) — a single accumulator variable, no extra data structures.

## Solution
```java
public class Solution {
    // DO NOT MODIFY THE ARGUMENTS WITH "final" PREFIX. IT IS READ ONLY
    public int singleNumber(final int[] A) {
        int result = 0;
        for (int i = 0; i < A.length; i++) {
            result = result ^ A[i];
        }
        return result;
    }
}
```

## Key Learning
- **Core insight:** XOR-ing an entire collection cancels every value that appears an even number of times, leaving only the odd-one-out.
- **Pattern recognition cue:** "find the element that appears once/an odd number of times while everything else repeats" → XOR accumulator, O(N) time, O(1) space.
- **Common trap:** never seed the accumulator with `A[0]` before looping from `i = 0` — that XORs `A[0]` into itself first (`A[0] ^ A[0] = 0`), silently destroying it if it happens to be the unique element. Always start the accumulator at the identity element (`0` for XOR) and let the loop touch every element exactly once.
- **Variants:** "Single Number II" (every other element appears three times — needs bit counting mod 3, not plain XOR) and "Single Number III" (two unique elements — needs a partition step on a differentiating bit, see the companion problem in this same topic folder).
