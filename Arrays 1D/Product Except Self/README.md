# Product Except Self

## Problem Statement
Given an integer array `nums`, return an array `result` such that `result[i]` equals the product of every element in `nums` **except** `nums[i]`. Division is not allowed.

**Target:** O(n) time using two passes — one carrying a running product of everything to the left of `i`, one carrying a running product of everything to the right of `i`.

## Example(s)
`nums = [1, 2, 3, 4]`

| i | nums[i] | prefix[i] (product left of i) | suffix[i] (product right of i) | result[i] |
|---|---|---|---|---|
| 0 | 1 | 1 | 24 | 24 |
| 1 | 2 | 1 | 12 | 12 |
| 2 | 3 | 2 | 4 | 8 |
| 3 | 4 | 6 | 1 | 6 |

**Output: `[24, 12, 8, 6]`**

## Approach
`result[i]` needs the product of every element except `nums[i]`. Splitting that product into "everything to the left of `i`" times "everything to the right of `i`" avoids division entirely: build a `prefix` array where `prefix[i]` is the product of all elements before index `i`, and a `suffix` array where `suffix[i]` is the product of all elements after index `i`. Multiplying `prefix[i] * suffix[i]` gives exactly the product of everything except `nums[i]`, since the two halves never overlap and together cover every other index.

**Trigger to recognize this pattern:** "compute something for every index that depends on all *other* elements, and division/recomputation is disallowed or unsafe (e.g. zeros in the array)" — split the computation into a left-to-right pass and a right-to-left pass that meet at each index.

## Algorithm
1. Let `n = nums.length`. Allocate `prefix[n]`, `suffix[n]`, `result[n]`.
2. Set `prefix[0] = 1` (nothing lies to the left of index 0).
3. For `i` from `1` to `n-1`: `prefix[i] = prefix[i-1] * nums[i-1]` — carry the running product one element at a time.
4. Set `suffix[n-1] = 1` (nothing lies to the right of the last index).
5. For `i` from `n-2` down to `0`: `suffix[i] = suffix[i+1] * nums[i+1]`.
6. For each `i` from `0` to `n-1`: `result[i] = prefix[i] * suffix[i]`.
7. Return `result`.

## Dry Run
`nums = [1, 2, 3, 4]`

**Prefix pass (left → right):** `prefix[i] = prefix[i-1] × nums[i-1]`, start `prefix[0] = 1`
`prefix[0]=1` → `prefix[1]=1×1=1` → `prefix[2]=1×2=2` → `prefix[3]=2×3=6`
`prefix = [1, 1, 2, 6]`

**Suffix pass (right → left):** `suffix[i] = suffix[i+1] × nums[i+1]`, start `suffix[3] = 1`
`suffix[3]=1` → `suffix[2]=1×4=4` → `suffix[1]=4×3=12` → `suffix[0]=12×2=24`
`suffix = [24, 12, 4, 1]`

**Combine (same index, no shifting):** `result[i] = prefix[i] × suffix[i]`
`result = [1×24, 1×12, 2×4, 6×1] = [24, 12, 8, 6]` ✅

## Why Does This Work?
For any index `i`, the full set of "all other elements" splits cleanly into two disjoint, non-overlapping groups: everything strictly before `i`, and everything strictly after `i`. `prefix[i]` is defined to be exactly the product of the first group, and `suffix[i]` exactly the product of the second. Since the groups partition "everything except index `i`" with no overlap and no gap, their product `prefix[i] * suffix[i]` is precisely the product of every element except `nums[i]` — no adjustment or correction term is ever needed. The base cases (`prefix[0] = 1`, `suffix[n-1] = 1`) are the identity element for multiplication, correctly representing "the product of an empty set of elements."

## Complexity Analysis
**Time:** O(n) — three separate O(n) passes over the array (prefix build, suffix build, combine), no nested loops.

**Space:** O(n) for the `prefix`, `suffix`, and `result` arrays (the two auxiliary arrays can be optimized away to O(1) extra by folding the suffix pass directly into the result array, but the two-array version is clearer for learning the pattern).

## Solution
```java
public class Solution {
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        int[] prefix = new int[n];
        int[] suffix = new int[n];
        prefix[0] = 1;
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] * nums[i - 1];
        }
        suffix[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i + 1];
        }
        for (int i = 0; i < n; i++) {
            result[i] = prefix[i] * suffix[i];
        }
        return result;
    }
}
```

## Key Learning
- **Core insight:** "product/sum of everything except index i" decomposes into an independent left-running pass and right-running pass that meet at `i` — this avoids both division (unsafe with zeros) and an O(n²) recompute-per-index approach.
- **How to spot this pattern:** any "exclude self, combine the rest" problem over an array (product except self, sum except self, max except self) is solved the same way — prefix pass + suffix pass + combine.
- **Common variants/traps:** forgetting the `prefix[0] = 1` / `suffix[n-1] = 1` identity base cases; off-by-one on whether `prefix[i]` includes `nums[i-1]` or `nums[i]`; the array contains zero(s) — this prefix/suffix approach still works correctly with zeros since it never divides, unlike a "compute total product then divide by nums[i]" approach which breaks entirely when any element is zero.
