public class Solution {
    public String solve(int[] A) {
        long sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum = sum + A[i];
        }
        if (sum % 2 == 0) {
            return "Yes";
        } else {
            return "No";
        }
    }
}
