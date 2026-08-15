public class Solution {
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        // prefix product array from left to right
        int[] prefix = new int[n];
        // suffix product array from right to left
        int[] suffix = new int[n];
        // as per question, the product should not contain the ith element,
        // so the first prefix value is 1, not nums[0]
        prefix[0] = 1;
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] * nums[i - 1];
        }
        suffix[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] * nums[i + 1];
        }
        for (int i = 0; i < n; i++) {
            result[i] = prefix[i] * suffix[i];
        }
        return result;
    }
}
