# Add One To Number

## Problem Statement
Given a non-negative number represented as an array of digits, add 1 to the number (increment the number represented by the digits).

The digits are stored such that the most significant digit is at the head of the array.

**Clarifying questions worth asking an interviewer:**
- Can the input have leading zeros (e.g. is `[0, 1, 2, 3]` valid)? — Yes.
- Can the output have leading zeros? — No, even if the input did.

**Constraints:** `1 <= size of array <= 10^6`

## Example(s)
| Input | Output | Why |
|---|---|---|
| `[1, 2, 3]` | `[1, 2, 4]` | Simple increment, no carry. |
| `[9, 9]` | `[1, 0, 0]` | All 9s overflow — array grows by one digit. |
| `[0, 9, 9]` | `[1, 0, 0]` | Leading zero is stripped, then overflow still applies. |

## Approach
This is a **two-pointer** problem: one pointer walks the input array from the right, the other walks the output array from the right, and a carry flag propagates between them.

Before touching the pointers, two things need to be measured up front because the output array's *size* isn't necessarily the input's size:
- **Leading zeros** in the input contribute nothing to the value and must be dropped from the output.
- **A trailing run of 9s** (covering every remaining non-zero digit) means the increment overflows and the output needs one extra slot for the new leading `1`.

**Trigger to recognize this pattern:** any "increment/decrement a big number stored as a digit array" problem — the fixed-size primitive `int`/`long` can't hold arbitrarily large numbers, so digit-by-digit array manipulation with carry propagation is the only option.

## Algorithm
1. Scan left to right, counting leading zeros (`zeros`) until the first non-zero digit.
2. Scan from `zeros` onward, counting a contiguous run of 9s (`nines`).
3. If `nines == (n - zeros)` (every remaining digit is a 9), the output needs `n - zeros + 1` slots; otherwise `n - zeros` slots.
4. Allocate `ans` of that length. Handle the last digit: if it's 9, write `0` and set `carry = 1`; otherwise write `A[n-1] + 1` and `carry = 0`.
5. Walk `i` from `n-2` down to `0` and `j` from `arrLength-2` down to `0` in lockstep (loop while **both** `i >= 0 && j >= 0`):
   - If `carry == 1`: write `0` if `A[i] == 9` (carry stays), else write `A[i] + 1` and clear the carry.
   - If `carry == 0`: copy `A[i]` straight across.
6. After the loop, if `ans[0]` is still `0` (only possible when the carry pushed all the way past the input array), set it to `1` — this patches in the new leading digit created by an all-9s overflow.

## Dry Run
Input: `A = [0, 9, 9]`

**Leading zeros:** `A[0] == 0` → `zeros = 1`. `A[1] != 0` → stop.

**Trailing 9s (from index 1):** `A[1] == 9`, `A[2] == 9` → `nines = 2`. Since `nines == n - zeros == 2`, overflow applies → `arrLength = 3 - 1 + 1 = 3`. `ans = [0, 0, 0]`.

**Last digit:** `A[2] == 9` → `ans[2] = 0`, `carry = 1`.

**Backward walk:**

| i | j | A[i] | carry in | action | ans[j] | carry out |
|---|---|---|---|---|---|---|
| 1 | 1 | 9 | 1 | `A[i]==9` → write 0 | `ans[1]=0` | 1 |
| 0 | 0 | 0 | 1 | `A[i]!=9` → write 0+1 | `ans[0]=1` | 0 |

Loop stops (`i` and `j` both hit `-1`). `ans[0]` is already `1`, so the final patch step does nothing.

**Output:** `[1, 0, 0]`

## Why Does This Work?
The carry is exactly what elementary-school addition uses: a digit plus a possible incoming `1` either stays a single digit or rolls over to `0` with a `1` carried to the next (more significant) position. Because we process strictly right to left, every digit sees the correct up-to-date carry from its less-significant neighbor before it's written — so no digit is ever finalized before all information it depends on is known.

The `i >= 0 && j >= 0` condition is what makes the two arrays (different lengths in the overflow case) safe to walk in lockstep: the loop simply stops the instant either array runs out, and the leading-zero/leading-1 handling outside the loop covers the leftover slot on whichever side is longer.

## Complexity Analysis
**Time:** O(N) — each digit is visited a constant number of times across the zero-count, nine-count, and carry-propagation passes.

**Space:** O(N) — the output array; no other data structure scales with input size.

## Solution
```java
public class Solution {
    public int[] plusOne(int[] A) {
        int n = A.length;
        int arrLength = 0;
        int zeros = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] != 0) {
                break;
            } else {
                zeros++;
            }
        }
        int nines = 0;
        for (int i = zeros; i < n; i++) {
            if (A[i] != 9) {
                break;
            } else {
                nines++;
            }
        }
        if (nines == (n - zeros)) {
            arrLength = n - zeros + 1;
        } else {
            arrLength = n - zeros;
        }
        int[] ans = new int[arrLength];
        int carry = 0;
        if (A[n - 1] == 9) {
            ans[arrLength - 1] = 0;
            carry = 1;
        } else {
            ans[arrLength - 1] = A[n - 1] + 1;
        }
        for (int i = n - 2, j = arrLength - 2; i >= 0 && j >= 0; i--, j--) {
            if (carry == 1) {
                if (A[i] == 9) {
                    ans[j] = 0;
                } else {
                    ans[j] = A[i] + 1;
                    carry = 0;
                }
            } else {
                ans[j] = A[i];
            }
        }
        if (ans[0] == 0) {
            ans[0] = 1;
        }
        return ans;
    }
}
```

## Key Learning
- **Core insight:** when a number is too large for a primitive type, model it as a digit array and simulate addition with a carry, exactly like doing it by hand.
- **How to spot this pattern:** any problem describing a number as "an array/list of digits" combined with an arithmetic operation (add one, add two numbers, multiply by a small constant) — the fixed-width `int`/`long` types are a red flag that overflow is part of the problem, not an edge case to ignore.
- **Common variants/traps:** forgetting leading zeros must be stripped from the *output* even if present in the input; forgetting the output array can be *longer* than the input (all-9s case); mixing up which index represents the most vs. least significant digit.
