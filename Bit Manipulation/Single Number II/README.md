# Single Number II

## Problem Statement
Given an array of integers, every element appears three times except for one element, which appears exactly once. Find that element.

Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?

**Constraints**
- `2 <= |A| <= 5*10^6`
- `0 <= A[i] <= INT_MAX`

## Example(s)
**Input:** `A = [2, 2, 2, 7]` → **Output:** `7`
Three `2`s and one `7` — `7` is the element that doesn't repeat three times.

## Approach
A hashmap of frequencies solves this in O(n) time but O(n) space, which fails the "without extra memory" requirement — the O(1)-space hint is the signal to think bitwise.

Look at any single bit position across the whole array. Every element that appears exactly three times contributes a multiple of 3 to the count of set bits at that position (0, 3, 6, …), since it's counted three separate times. Whatever the count of set bits at that position leaves behind after `% 3` can only be coming from the one element that doesn't repeat — either it has that bit set (remainder `1`) or it doesn't (remainder `0`). Doing this independently for all 32 bit positions and OR-ing the results back together reconstructs the unique element bit by bit, with no hashmap and no extra array.

**Trigger for next time:** "every element appears exactly `k` times except one" + O(1) space requirement → count each bit position's contribution mod `k`, rather than reaching for a hashmap.

## Algorithm
1. Initialize `ans = 0`.
2. For each bit position (scanned MSB → LSB, mask `1 << (31 - i)` for `i` from `0` to `31`):
   - Count how many elements in `A` have that bit set (`count`).
   - If `count % 3 != 0`, the unique element has this bit set — OR it into `ans`.
3. Return `ans`.

## Dry Run
`A = [2, 2, 2, 7]` — three `2`s (`010`) and one `7` (`111`). Expected answer: `7`.

Only three of the 32 bit positions ever have a nonzero count for this array; every other position contributes nothing since `0 % 3 == 0`.

| bit (value) | count of 1s across A | count % 3 | contributes to ans? |
|---|---|---|---|
| bit 2 (value 4) | 1 (only from 7) | 1 → ≠0 | yes, `ans |= 4` |
| bit 1 (value 2) | 4 (three 2's + 7) | 1 → ≠0 | yes, `ans |= 2` |
| bit 0 (value 1) | 1 (only from 7) | 1 → ≠0 | yes, `ans |= 1` |

Building `ans`: `0 → 4 → 6 → 7`. **Result: `7`** — matches the unique element.

## Why Does This Work?
For a fixed bit position, summing that bit across every element in the array counts each repeated-three-times element's bit contribution exactly three times, so those elements always add a multiple of 3 to the running count — they fully "cancel out" under `% 3`. The one element that doesn't repeat contributes exactly once (0 or 1, whichever its bit is), so it's the only source of a nonzero remainder. Since `ans` starts at all zeros and OR can only ever turn a bit on, deciding "should this bit become 1?" independently at every position and OR-ing the results reconstructs the unique element exactly, one bit at a time, with no information lost across positions.

## Complexity Analysis
- **Time:** O(32n) = O(n) — one full array scan per bit position.
- **Space:** O(1) — a running answer and a per-position counter only.

## Solution
```java
public class Solution {
    public int singleNumber(final int[] A) {
        int n = A.length;
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                int chk = A[j] & (1 << (31 - i));
                if (chk != 0) {
                    count++;
                }
            }
            if (count % 3 != 0) {
                ans = ans | (1 << (31 - i));
            }
        }
        return ans;
    }
}
```

## Key Learning
- **Core insight:** for "every element appears exactly `k` times except one," summing each bit position's set-count and reducing mod `k` isolates exactly the unique element's bits.
- **Pattern recognition cue:** "appears `k` times except one/two" combined with an O(1)-space ask is a strong signal for bit-by-bit counting rather than a hashmap.
- **Common variants/traps:** for `k = 2` ("appears twice except one"), plain XOR works because a value XORed with itself cancels — no mod needed. For "appears twice except *two*" unique elements, XOR everything first, then use a differentiating bit to split the array into two groups and XOR each group separately. For `k = 3`, XOR alone doesn't isolate the answer — you need this mod-count approach (or the equivalent ones/twos-bitmask trick that does it in a single pass).
