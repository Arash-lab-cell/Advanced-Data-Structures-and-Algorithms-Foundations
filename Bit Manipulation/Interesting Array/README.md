# Interesting Array

## Problem Statement

You have an array `A` with `N` elements. Two operations are available:

1. **Split:** pick an element `B` and replace it with two elements `C` and `D` such that `B = C + D`.
2. **Merge:** pick two elements `P` and `Q` and replace them with a single element `R = P XOR Q`.

Determine whether it is possible to reduce the array to a single element equal to `0` using any number of splits and/or merges, in any order.

**Constraints:**
- `1 <= N <= 10^5`
- `1 <= A[i] <= 10^6`

**Input format:** an integer array `A`.
**Output format:** `"Yes"` if reducible to `[0]`, else `"No"`.

## Example(s)

**Input 1:** `A = [9, 17]` → **Output:** `"Yes"`
Merge: `9 XOR 17 = 24`. Split `24` into `12, 12`. Merge: `12 XOR 12 = 0`. Array is now `[0]`.

**Input 2:** `A = [1]` → **Output:** `"No"`
A single odd element can never become `0` — there's nothing to merge it with, and splitting `1` only produces pairs summing to 1, never a lone `0`.

## Approach

The operations look complicated (arbitrary splits and XOR-merges), so brute-forcing the sequence of moves is not tractable. The trigger here: whenever a problem allows a sequence of transformations and asks "can you reach state X?", look for an **invariant** — some property of the array that never changes no matter which operations you apply. If the invariant differs between the start state and the target state, the answer is automatically "No"; if it matches, the condition is (here) also sufficient.

The natural candidate invariant is the **sum of the array**, because:
- Split preserves the sum exactly (`B = C + D`).
- Merge *looks* like it changes the sum, but XOR and addition agree in parity: `P XOR Q = P + Q - 2*(P AND Q)`. Subtracting an even number never changes parity. So **merge preserves the sum's parity**, even though it doesn't preserve the exact value.

The target state `[0]` has sum `0` (even). So the array can only reach `[0]` if its total sum starts even.

## Algorithm

1. Compute `sum = A[0] + A[1] + ... + A[N-1]`.
2. If `sum` is even, return `"Yes"`.
3. Otherwise, return `"No"`.

## Dry Run

`A = [9, 17]`:
- `sum = 26`, even → `"Yes"`.
- Sanity check: `9 XOR 17 = 24` (even). Split `24` into `12 + 12` (even). Merge `12 XOR 12 = 0`. Final array `[0]` — success, consistent with `"Yes"`.

`A = [1]`:
- `sum = 1`, odd → `"No"`. No sequence of operations can flip parity, so `[0]` is unreachable.

## Why Does This Work?

The correctness argument: **sum parity is a true invariant** under both operations.
- Split: `B → C, D` with `B = C + D`. Total sum unchanged, so parity unchanged.
- Merge: `P, Q → P XOR Q`. Since `P + Q` and `P XOR Q` always differ by an even number (`2 * (P AND Q)`), replacing `P + Q` with `P XOR Q` in the running sum changes it by an even amount — parity preserved even though the exact value isn't.

Because parity is preserved by every allowed move, the final parity must equal the starting parity for any sequence of operations. The only way to end at `[0]` (even sum) is to start with an even sum. This necessary condition is also sufficient — an even-sum array can always be maneuvered down to `[0]` — which is why checking parity alone suffices.

## Complexity Analysis

- **Time:** O(N) — one pass to sum the array.
- **Space:** O(1) extra space.

## Solution

```java
public class Solution {
    public String solve(int[] A) {
        long sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum = sum + A[i];
        }
        if (sum % 2 == 0) {
            return "Yes";
        } else {
            return "No";
        }
    }
}
```

## Key Learning

- **Core insight:** when operations transform an array in complicated ways and the question is reachability of a target state, hunt for an invariant (sum, parity, GCD, count of odd elements, etc.) preserved by every operation, then compare it between start and target.
- **How to spot this pattern again:** questions phrased as "can you reduce/transform/reach state X using operations Y and Z" are a strong signal — especially when operations mix "structure-preserving" (split, exact sum) with "structure-changing" (XOR-merge, exact value changes but a coarser property like parity survives).
- **Common variants/traps:** don't abandon the sum-based approach just because XOR-merge changes the exact sum — check a weaker invariant (parity) instead. Also, proving the condition is sufficient (not just necessary) usually needs a constructive argument; don't stop at "the invariant matches" without sanity-checking a constructive path exists.
