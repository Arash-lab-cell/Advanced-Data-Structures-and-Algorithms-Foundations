# Single Number III

## Problem Statement
Given an array of positive integers `A`, exactly two integers appear only once, and every other integer appears exactly twice. Find the two integers that appear only once, and return them in ascending order.

**Constraints**
- `2 <= |A| <= 100000`
- `1 <= A[i] <= 10^9`

## Example(s)
**Input:** `A = [2, 4, 7, 9, 2, 4]`
**Output:** `[7, 9]`

`2` and `4` each appear twice and cancel out; `7` and `9` are the two unique elements, returned smallest-first.

## Approach
The plain "Single Number" XOR trick (see the companion problem in this folder) works when there's exactly *one* unique element. Here there are two, call them `x` and `y`. If we XOR the whole array, every duplicate pair cancels and we're left with `xor = x ^ y`.

The key move: since `x != y`, `xor` must have at least one bit set — pick any set bit `p` of `xor`. That bit is guaranteed to differ between `x` and `y` (if it didn't, it would've cancelled out of `xor` too). So bit `p` can be used as a splitter: partition the whole array into two groups based on whether bit `p` is set. `x` and `y` land in different groups by construction, and every duplicate pair lands together in the same group (since duplicates are identical, they share every bit). XOR-ing each group in isolation cancels its duplicates and leaves exactly one unique value per group.

**Trigger for next time:** "two elements appear once, everything else appears twice" → XOR everything to isolate `x ^ y`, then find a set bit in that XOR to split the array into two single-XOR subproblems.

## Algorithm
1. XOR every element of `A` together into `xor`. Now `xor = x ^ y`.
2. Find any bit position `p` where `xor` has a `1` bit (scan `p = 0..31`, check `xor & (1 << p) != 0`).
3. Partition `A` into two lists: `set` (elements with bit `p` on) and `unset` (elements with bit `p` off).
4. XOR all elements of `set` together → `unique1`. XOR all elements of `unset` together → `unique2`.
5. Return `[min(unique1, unique2), max(unique1, unique2)]`.
## Dry Run
Input: `A = [2, 4, 7, 9, 2, 4]` (unique elements: 7 and 9)

```
Step 1 — XOR everything:
2 ^ 4 ^ 7 ^ 9 ^ 2 ^ 4
= (2^2) ^ (4^4) ^ 7 ^ 9      // duplicate pairs cancel to 0
= 7 ^ 9 = 0111 ^ 1001 = 1110 (= 14)
xor = 14

Step 2 — find a set bit in xor (p starts at 0):
p=0: 14 & 1  = 0   → not set, p++
p=1: 14 & 2  = 2   → set! break, p = 1

Step 3 — partition A by bit 1:
  2 (010) → bit1 set   → set   = [2]
  4 (100) → bit1 unset → unSet = [4]
  7 (0111) → bit1 set  → set   = [2, 7]
  9 (1001) → bit1 unset→ unSet = [4, 9]
  2 (010) → bit1 set   → set   = [2, 7, 2]
  4 (100) → bit1 unset → unSet = [4, 9, 4]

Step 4 — XOR each group:
unique1 = 2 ^ 7 ^ 2 = 7
unique2 = 4 ^ 9 ^ 4 = 9

Step 5 — order ascending: 7 < 9 → ans = [7, 9] ✅
```

## Why Does This Work?
Every duplicate pair shares the exact same bit pattern, so at bit `p` a duplicate pair always has matching bits — meaning both copies always fall into the *same* group, and cancel each other there via XOR just like in the single-unique-element case. `x` and `y`, on the other hand, are guaranteed to differ at bit `p` (that's precisely how `p` was chosen — it's a bit that survived the full-array XOR, meaning it's set in exactly one of `x`/`y`). So `x` and `y` are forced into *opposite* groups, and each group's XOR reduces to exactly one leftover value — the group's unique element.

## Complexity Analysis
- **Time:** O(N) — a constant number of linear passes over the array (one to compute `xor`, one to partition, two to reduce each group).
- **Space:** O(N) for the `set`/`unset` lists in this version (can be reduced to O(1) by XOR-reducing each group in a second pass over `A` instead of materializing lists).
## Solution
```java
public class Solution {
    public int[] solve(int[] A) {
        int n = A.length;
        int xor = 0;
        for (int i = 0; i < n; i++) {
            xor = xor ^ A[i];
        }
        int p = 0;
        while (true) {
            int setBit = xor & (1 << p);
            if (setBit != 0) break;
            p++;
        }
        ArrayList<Integer> set = new ArrayList<>();
        ArrayList<Integer> unSet = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int setBit = A[i] & (1 << p);
            if (setBit != 0) set.add(A[i]);
            else unSet.add(A[i]);
        }
        int unique1 = 0, unique2 = 0;
        for (int v : set) unique1 ^= v;
        for (int v : unSet) unique2 ^= v;
        int[] ans = new int[2];
        if (unique1 > unique2) { ans[0] = unique2; ans[1] = unique1; }
        else { ans[0] = unique1; ans[1] = unique2; }
        return ans;
    }
}
```

## Key Learning
- **Core insight:** when XOR leaves you with the combination of two unknowns (`x ^ y`) instead of one clean answer, look for a bit that differs between them — any set bit of the combined XOR is guaranteed to be such a bit — and use it to split the problem into two independent single-XOR subproblems.
- **Pattern recognition cue:** "exactly two elements appear once, rest appear twice" is the tell for this two-stage XOR + bit-splitter pattern. If it were "one element once, rest twice," the plain single-XOR trick from "Single Number" suffices; the moment it becomes "two elements," expect a partition step.
- **Common variant/trap:** picking *any* set bit of `xor` works (not just the lowest), but you must pick a bit that is actually set — a bit that's `0` in `xor` tells you nothing, since it means `x` and `y` agree there and would land in the same group, breaking the partition.
