# Merge Sorted Overlapping Intervals

## Problem Statement
You are given a collection of intervals `A` in a 2-D array format, where each interval is `[start, end]` and the intervals are already sorted by their start values. Merge all overlapping intervals and return the resulting set of non-overlapping intervals.

**Constraints:** `1 <= len(A) <= 100000`, `1 <= A[i][0] <= A[i][1] <= 100000`, `A` is sorted by `A[i][0]`.

## Example(s)
`A = [[1,10],[2,3],[9,15],[20,25]]`

| Interval | Overlaps last merged group? | Result so far |
|---|---|---|
| `[1,10]` (seed) | — | `[[1,10]]` |
| `[2,3]` | contained within `[1,10]` | `[[1,10]]` |
| `[9,15]` | overlaps and extends `[1,10]` | `[[1,15]]` |
| `[20,25]` | no overlap with `[1,15]` | `[[1,15],[20,25]]` |

**Output: `[[1,15],[20,25]]`**

## Approach
Because the intervals arrive pre-sorted by start value, a single linear scan suffices — no sorting step is needed. Keep a running "last merged interval." For each new interval, compare its `start` against the *end* of the last merged interval:
- if the new interval overlaps (its `start` falls at or before the last merged interval's `end`), either extend the last interval's `end` (if the new interval reaches further) or discard it (if it's already fully contained);
- if it doesn't overlap at all, it begins a brand-new group.

**Trigger to recognize this pattern:** "merge overlapping ranges/meetings/intervals," especially when the input is pre-sorted by start — this is the greedy linear-sweep pattern: process left to right, only ever comparing against the most recently finalized group.

## Algorithm
1. Seed the result list with the first interval `A[0]` as the first "in-progress" merged group.
2. For each subsequent interval `A[i] = [start, end]` (i from 1 to n-1), compare against the last group's current end (`al.get(j-1)[1]`):
   - **Overlaps and extends:** if `start <= lastEnd` and `end >= lastEnd`, update the last group's end to `end`.
   - **Fully contained:** if `start <= lastEnd` and `end < lastEnd`, the new interval adds nothing — skip it.
   - **No overlap:** otherwise (`start > lastEnd`), append `[start, end]` as a new group.
3. After processing all intervals, convert the list of merged groups back into a 2D array and return it.

## Dry Run
`A = [[1,10],[2,3],[9,15],[20,25]]`

| step | interval | check vs al.get(j-1)[1] | decision | al after |
|---|---|---|---|---|
| seed | — | — | `al.add([1,10])` | `[[1,10]]` |
| i=1 | [2,3] | 2≤10 ✓, 3≥10 ✗ → contained | no-op | `[[1,10]]` |
| i=2 | [9,15] | 9≤10 ✓, 15≥10 ✓ → extend | `al.get(0)[1]=15` | `[[1,15]]` |
| i=3 | [20,25] | 20≤15 ✗ → new group | `al.add([20,25])` | `[[1,15],[20,25]]` |

**Final:** `[[1,15],[20,25]]`

## Why Does This Work?
Because the input is sorted by `start`, once an interval's `start` is beyond the current merged group's `end`, no interval processed *later* can ever overlap that earlier group either (every later interval has an equal or greater `start`). This means it's safe to permanently finalize a merged group the moment a non-overlapping interval is seen — there's no need to look back and re-merge previously closed groups. Comparing only against the single most recent group (rather than all groups seen so far) is therefore sufficient and correct, which is exactly what keeps this a single O(n) linear sweep instead of something quadratic.

## Complexity Analysis
**Time:** O(N) — one linear pass through the already-sorted intervals; if the input weren't pre-sorted, an O(N log N) sort would be required first.

**Space:** O(N) for the output list of merged intervals (this is required to hold the result; no additional auxiliary structures scale with input beyond that).

## Solution
```java
import java.util.ArrayList;

public class Solution {
    public int[][] solve(int[][] A) {
        int n = A.length;
        ArrayList<int[]> al = new ArrayList<>();
        al.add(new int[]{A[0][0], A[0][1]});
        int j = 1;
        for (int i = 1; i < n; i++) {
            int start = A[i][0];
            int end = A[i][1];
            if (start <= al.get(j - 1)[1] && end >= al.get(j - 1)[1]) {
                al.get(j - 1)[1] = end;
            } else if (start <= al.get(j - 1)[1] && end < al.get(j - 1)[1]) {
                j = j;
            } else {
                al.add(new int[]{start, end});
                j++;
            }
        }
        return al.toArray(new int[al.size()][]);
    }
}
```

## Key Learning
- **Core insight:** when intervals are pre-sorted by start, merging only ever needs to look at the single most-recently-finalized group — sortedness guarantees earlier groups can never be reopened by a later interval.
- **How to spot this pattern:** "merge overlapping meetings/ranges/intervals" is almost always this greedy linear-sweep technique; if the input isn't sorted, sort by start first, then apply the same scan.
- **Common variants/traps:** if intervals *aren't* pre-sorted, skipping the sort step breaks the "never need to look back" guarantee; the "fully contained" branch is easy to miss — without it, a shorter interval nested inside a longer one can incorrectly shrink the merged group's end; useful `ArrayList<int[]>` idioms worth remembering: `list.get(list.size()-1)[1] = newEnd` mutates the last element's array in place, and `list.toArray(new int[list.size()][])` converts back to a 2D array for the return type.
