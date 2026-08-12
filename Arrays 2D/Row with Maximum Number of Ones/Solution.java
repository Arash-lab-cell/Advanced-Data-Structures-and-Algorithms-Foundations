public class Solution {
    public int solve(int[][] A) {
        int n = 0;
        int m = A[0].length - 1;
        int resultID = 0;

        while (n < A.length && m >= 0) {
            if (A[n][0] == 1) {
                // row n is all 1s (sorted row) -> can't be beaten, exit early
                return n;
            } else if (A[n][m] == 0) {
                n++;
            } else { // A[n][m] == 1
                resultID = n;
                m--;
            }
        }

        return resultID;
    }
}
