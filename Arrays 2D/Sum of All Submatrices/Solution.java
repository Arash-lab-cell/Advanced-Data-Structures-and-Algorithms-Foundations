public class Solution {
    public int solve(int[][] A) {
        int rows = A.length;
        int cols = A[0].length;
        int sum = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // (i+1) * (rows-i) = number of row-ranges [r1, r2] that include row i
                // (j+1) * (cols-j) = number of col-ranges [c1, c2] that include col j
                sum += A[i][j] * (i + 1) * (j + 1) * (rows - i) * (cols - j);
            }
        }

        return sum;
    }
}
