public class Solution {
    public int trap(final int[] A) {
        int n = A.length;
        int[] leftMax = new int[n];
        leftMax[0] = A[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], A[i]);
        }
        int[] rightMax = new int[n];
        rightMax[n - 1] = A[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], A[i]);
        }
        int[] level = new int[n];
        for (int i = 0; i < n; i++) {
            level[i] = Math.min(leftMax[i], rightMax[i]);
        }
        int totalWater = 0;
        for (int i = 0; i < n; i++) {
            totalWater += (level[i] - A[i]);
        }
        return totalWater;
    }
}
