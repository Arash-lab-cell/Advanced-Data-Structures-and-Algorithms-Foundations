public class Solution {
    public int maxSubArray(final int[] A) {
        int n = A.length;
        int sum = 0;
        int maxSum = Integer.MIN_VALUE;
        if (n == 1) {
            return A[0];
        }
        for (int i = 0; i < n; i++) {
            sum = sum + A[i];
            maxSum = Math.max(maxSum, sum);
            if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }
}
