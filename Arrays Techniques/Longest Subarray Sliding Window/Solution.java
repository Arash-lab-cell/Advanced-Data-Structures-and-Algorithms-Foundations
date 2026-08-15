public class Solution {
    /** Return the length of the longest contiguous subarray whose sum is
     *  at most target. Assumes all elements are non-negative. Uses a
     *  variable-size sliding window: grow the right edge, and shrink from
     *  the left whenever the window sum exceeds target. */
    public static int longestSubarrayWithSumAtMostTarget(int[] arr, int target) {
        int left = 0;
        int sum = 0;
        int maxLength = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            while (sum > target) {
                sum -= arr[left];
                left++;
            }
            maxLength = Math.max(maxLength, (i - left + 1));
        }
        return maxLength;
    }
}
