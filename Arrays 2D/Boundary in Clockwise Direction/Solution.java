public class Solution {
    public int[] solve(int[][] A) {
        int n = A.length;
        int m = A[0].length;

        // if only a single row
        if (n == 1) {
            int[] rowOnly = new int[m];
            for (int i = 0; i < m; i++) {
                rowOnly[i] = A[0][i];
            }
            return rowOnly;
        }
        // if only a single column
        if (m == 1) {
            int[] colOnly = new int[n];
            for (int i = 0; i < n; i++) {
                colOnly[i] = A[i][0];
            }
            return colOnly;
        }

        int totalElements = (2 * (n - 1)) + (2 * (m - 1));
        int[] ans = new int[totalElements];
        int ansId = 0;

        // top row: left to second-last element
        for (int i = 0; i < m - 1; i++) {
            ans[ansId] = A[0][i];
            ansId++;
        }
        // right column: top to second-last element
        for (int i = 0; i < n - 1; i++) {
            ans[ansId] = A[i][m - 1];
            ansId++;
        }
        // bottom row: right to second element from the left
        for (int i = m - 1; i > 0; i--) {
            ans[ansId] = A[n - 1][i];
            ansId++;
        }
        // left column: bottom to second element from the top
        for (int i = n - 1; i > 0; i--) {
            ans[ansId] = A[i][0];
            ansId++;
        }
        return ans;
    }
}
