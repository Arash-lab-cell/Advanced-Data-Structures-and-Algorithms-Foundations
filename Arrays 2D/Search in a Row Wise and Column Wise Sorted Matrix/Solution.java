public class Solution {
    public int solve(int[][] A, int B) {
        int i = 1;
        int j = A[0].length;
        int result = Integer.MAX_VALUE;

        while (i <= A.length && j >= 1) {
            if (A[i - 1][j - 1] > B) {
                j--;
            } else if (A[i - 1][j - 1] < B) {
                i++;
            } else {
                int encoded = i * 1009 + j;
                result = Math.min(result, encoded);
                j--; // keep scanning left — a smaller j with the same value can still exist
            }
        }

        return result < Integer.MAX_VALUE ? result : -1;
    }
}
