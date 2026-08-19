# Maximum AND Pair

## Problem Statement
Given an array `A`, find the maximum value of `A[i] & A[j]` over every pair of indices `i != j`.

**Constraints**
- `1 <= len(A) <= 10^5`
- `1 <= A[i] <= 10^9`

## Example(s)
**Input:** `A = [4, 8, 12]` → **Output:** `8`
Pairwise ANDs are `4&8=0`, `4&12=4`, `8&12=8` — the maximum is `8`.

## Approach
`N` up to `10^5` rules out checking all `O(n^2)` pairs directly. The maximum possible AND value will always have its highest bit as high as possible — so build the answer greedily from the most significant bit down.

At each bit position, count how many currently-"alive" elements have that bit set. If **more than one** element has it set, that bit is achievable in some pair's AND, so commit to it: OR it into the answer, and eliminate every alive element that *doesn't* have this bit (they can no longer be part of the best pair, since AND needs the bit on both sides). Move to the next bit and repeat only among survivors. If fewer than two elements have the bit, skip it — it can't appear in any valid pair's AND — and re-check the same *set* of survivors at the next bit.

**Trigger for next time:** "maximum/minimum AND, OR, or XOR over all pairs" with a large `N` (ruling out brute force) → think greedy bit-fixing from the MSB down, eliminating candidates that fall out of the running prefix.

## Algorithm
1. Initialize `ans = 0`.
2. For each bit position (MSB → LSB, mask `1 << (31 - i)` for `i` from `0` to `31`):
   - Count how many elements currently have this bit set (`chk1s`).
   - If `chk1s > 1`:
     - Set this bit in `ans`.
     - Zero out every element that does **not** have this bit set (removing it from consideration in later, lower-bit rounds).
3. Return `ans`.

## Dry Run
`A = [4, 8, 12]` — binary `4 = 0100`, `8 = 1000`, `12 = 1100`.

| bit (value) | A[0] | A[1] | A[2] | chk1s | >1? | action | array after | ans after |
|---|---|---|---|---|---|---|---|---|
| bits 31–4 | 0 | 0 | 0 | 0 | no | nothing | `[4, 8, 12]` | 0 |
| bit 3 (8) | 0 | 8 | 8 | 2 | **yes** | set bit; zero A[0] | `[0, 8, 12]` | 8 |
| bit 2 (4) | 0 | 0 | 4 | 1 | no | nothing | `[0, 8, 12]` | 8 |
| bit 1 (2) | 0 | 0 | 0 | 0 | no | nothing | `[0, 8, 12]` | 8 |
| bit 0 (1) | 0 | 0 | 0 | 0 | no | nothing | `[0, 8, 12]` | 8 |

**Result: `ans = 8`**, matching the brute-force check above.

## Why Does This Work?
The AND of any pair is dominated by its highest set bit — a pair sharing a `1` at a higher bit position always beats a pair that only shares lower bits, regardless of what happens below. So the greedy strategy of "try to fix the highest bit first, keep only the elements that can still achieve it, then move down" never discards a candidate that could have led to a better answer: any element eliminated at a given bit lacks that bit, so pairing it with anything from that point on can only produce an AND missing that bit too — strictly worse than the prefix already committed to `ans`. Requiring `chk1s > 1` (not `>= 1`) ensures the bit is only claimed when it's actually achievable by some real pair, since AND needs two distinct elements.

## Complexity Analysis
- **Time:** O(32N) = O(N) — one pass per bit, over the (shrinking) array.
- **Space:** O(1) extra — the array is mutated in place to track survivors.

## Solution
```java
public class Solution {
    public int solve(int[] A) {
        int n = A.length;
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int chk1s = 0;
            for (int j = 0; j < n; j++) {
                int chk = A[j] & (1 << (31 - i));
                if (chk != 0) {
                    chk1s++;
                }
            }
            if (chk1s > 1) {
                for (int j = 0; j < n; j++) {
                    int chk = A[j] & (1 << (31 - i));
                    if (chk == 0) {
                        A[j] = 0;
                    }
                }
                ans = ans | (1 << (31 - i));
            }
        }
        return ans;
    }
}
```

## Key Learning
- **Core insight:** maximize-pairwise-AND (and similar pairwise bitwise-max problems) via greedy MSB-first bit fixing, discarding candidates that can no longer beat the committed prefix.
- **Pattern recognition cue:** "maximum AND/OR/XOR over all pairs" with `N` large enough to forbid `O(n^2)` → bit-by-bit greedy from the top bit down.
- **Common trap:** using `chk1s >= 1` instead of `> 1` — a single element with the bit set has no partner to pair with, so that bit can never appear in *any* valid pair's AND. Also remember that zeroing a losing element (`A[j] = 0`) permanently disqualifies it from every later, lower-bit round, since `0 & anything == 0`.
