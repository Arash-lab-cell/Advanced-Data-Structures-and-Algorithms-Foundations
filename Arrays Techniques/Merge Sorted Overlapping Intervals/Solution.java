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
                // overlaps and extends the last merged interval
                al.get(j - 1)[1] = end;
            } else if (start <= al.get(j - 1)[1] && end < al.get(j - 1)[1]) {
                // fully contained within the last merged interval — no-op
                j = j;
            } else {
                // no overlap — starts a new interval group
                al.add(new int[]{start, end});
                j++;
            }
        }
        return al.toArray(new int[al.size()][]);
    }
}
