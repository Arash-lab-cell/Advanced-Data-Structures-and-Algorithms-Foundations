# Next Permutation

## Problem Statement
Implement **next permutation**, which rearranges numbers into the numerically next greater permutation of numbers for a given array `A` of size `N`.

If such an arrangement is not possible, rearrange the array into the lowest possible order (sorted ascending).

**Notes:**
- The replacement must be **in-place** — no extra array allocation.
- Library functions for next permutation are not allowed.

**Constraints:** `1 <= N <= 5 * 10^5`, `1 <= A[i] <= 10^9`

## Example(s)
`A = [1, 3, 5, 4, 2]` → `[1, 4, 2, 3, 5]`

| Step | What happens | Array |
|---|---|---|
| Find pivot | rightmost index where `A[i] < A[i+1]` → index 1 (`A[1]=3`) | `[1, 3, 5, 4, 2]` |
| Find swap partner | rightmost value in suffix `> A[pivot]` → `A[3]=4` | swap → `[1, 4, 5, 3, 2]` |
| Reverse suffix after pivot | reverse indices 2..4 | `[1, 4, 2, 3, 5]` |

## Approach
A permutation is "next" if it is the smallest permutation strictly greater than the current one. Reading the array from the right, the longest suffix that is already non-increasing is, by definition, already at its maximum arrangement — it cannot be rearranged to something bigger without changing something to its left. So the first place a bigger permutation can come from is the last position where the sequence still increases (`A[i] < A[i+1]`) — that's the **pivot**.

**Trigger to recognize this pattern:** "next/previous permutation," "next greater/smaller arrangement of digits/elements using in-place swaps" — reach for the pivot-identify → swap → reverse-suffix technique rather than generating and sorting all permutations.

## Algorithm
1. If `N == 1`, no next permutation is possible beyond itself — return `A` as is.
2. **Find the pivot:** scan from `i = n-2` down to `0`; the first `i` where `A[i] < A[i+1]` is the pivot index.
3. **No pivot found** (the whole array is non-increasing, i.e. the last/largest permutation): reverse the entire array to produce the smallest permutation, and return.
4. **Find the swap partner:** scan the suffix from the right (`i = n-1` down to `pivotId+1`), and swap `A[pivotId]` with the *first* (i.e., smallest) value greater than `A[pivotId]` encountered.
5. **Reverse the suffix** starting at `pivotId + 1` through the end of the array — this converts the (still-descending) suffix into ascending order, giving the smallest possible tail.
6. Return `A`.

## Dry Run
**Input:** `[1, 3, 5, 4, 2]`

**Phase 1 — Find Pivot** (scan right→left, compare `A[i]` vs `A[i+1]`)

| i | A[i] | A[i+1] | A[i] < A[i+1]? |
|---|---|---|---|
| 3 | 4 | 2 | No |
| 2 | 5 | 4 | No |
| 1 | 3 | 5 | **Yes → pivot** |

`pivotId = 1`, `A[pivotId] = 3`

**Phase 2 — Find Swap Partner** (scan right→left in suffix, first value > pivot)

| i | A[i] | A[i] > 3? |
|---|---|---|
| 4 | 2 | No |
| 3 | 4 | **Yes → swap** |

Swap `A[1]` ↔ `A[3]`: `[1, 3, 5, 4, 2]` → `[1, 4, 5, 3, 2]`

**Phase 3 — Reverse Suffix** (`start = pivotId+1 = 2`, `end = n-1 = 4`)

| start | end | Action | Array |
|---|---|---|---|
| 2 | 4 | swap A[2], A[4] | `[1, 4, 2, 3, 5]` |
| 3 | 3 | stop (start == end) | — |

**Result:** `[1, 4, 2, 3, 5]`

## Why Does This Work?
The pivot is defined as the *rightmost* index `i` where `A[i] < A[i+1]`. To find it, the scan moves right-to-left and only stops the moment that condition becomes true — meaning every adjacent pair strictly to the right of the pivot was already checked and found **not** increasing (`A[j] >= A[j+1]`). A sequence where every adjacent pair is non-increasing is, by chaining, non-increasing as a whole — so the suffix after the pivot is guaranteed to already be at its *maximum* arrangement and cannot be improved without touching something to its left.

Since the suffix is maxed out, the only way to get a strictly bigger permutation is to increase the value at the pivot itself, using the *smallest* value in the suffix that's still bigger than it (this keeps the increase as small as possible, which is required for "next," not "some bigger" permutation). After that swap, the suffix is still non-increasing (swapping doesn't change that some larger discarded value now sits at the pivot's old suffix slot, but the surrounding pairwise ordering property is preserved among the remaining elements), so reversing it converts it to non-decreasing — i.e., the smallest possible arrangement of that suffix — giving the overall smallest permutation greater than the input.

**Example — `[6, 8, 7, 4, 3, 1]`:** scanning right→left, every adjacent pair (`3<1`? No, `4<3`? No, `7<4`? No, `8<7`? No) fails until `6<8` succeeds at index 0. Four non-increasing pairs got chained together before stopping, confirming the suffix `[8,7,4,3,1]` is fully non-increasing (`8 ≥ 7 ≥ 4 ≥ 3 ≥ 1`).

## Complexity Analysis
**Time:** O(N) — each of the three phases (find pivot, find swap partner, reverse suffix) is a single linear scan; no nested nested nested loops (each scan touches disjoint parts of a bounded range).

**Space:** O(1) — in-place swaps only, no auxiliary array.

## Solution
```java
public class Solution {
    public int[] nextPermutation(int[] A) {
        int n = A.length;
        if (n == 1) {
            return A;
        }
        int pivotId = Integer.MIN_VALUE;
        for (int i = n - 2; i >= 0; i--) {
            if (A[i] < A[i + 1]) {
                pivotId = i;
                break;
            }
        }
        if (pivotId == Integer.MIN_VALUE) {
            int start = 0;
            int end = n - 1;
            while (start < end) {
                int temp = A[end];
                A[end] = A[start];
                A[start] = temp;
                start++;
                end--;
            }
            return A;
        }
        for (int i = n - 1; i >= pivotId + 1; i--) {
            if (A[i] > A[pivotId]) {
                int temp = A[i];
                A[i] = A[pivotId];
                A[pivotId] = temp;
                break;
            }
        }
        int end = n - 1;
        int start = pivotId + 1;
        while (start < end) {
            int temp = A[end];
            A[end] = A[start];
            A[start] = temp;
            start++;
            end--;
        }
        return A;
    }
}
```

## Key Learning
- **Core insight:** the pivot search direction (right-to-left, comparing adjacent pairs) doubles as a free proof that everything past the pivot is already maximally arranged — that fact is what justifies "swap with smallest-bigger-value, then reverse" as producing the *next* permutation rather than just *a* bigger one.
- **How to spot this pattern:** "rearrange in place to get the next/previous lexicographic permutation," or "smallest number greater than X using the same digits" — both are the exact same pivot-swap-reverse technique.
- **Common variants/traps:** comparing `A[i]` only against the *last* element instead of its immediate neighbor `A[i+1]` when hunting for the pivot; when picking the swap partner, grabbing the first value greater than the pivot from the *left* instead of scanning from the right (must pick the smallest excess, not just any excess); forgetting the "array is fully descending" edge case, which needs a full reverse instead of a pivot-based swap.
