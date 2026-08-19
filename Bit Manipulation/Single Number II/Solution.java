public class Solution {
    public int singleNumber(final int[] A) {
        int n = A.length;
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                int chk = A[j] & (1 << (31 - i));
                if (chk != 0) {
                    count++;
                }
            }
            if (count % 3 != 0) {
                ans = ans | (1 << (31 - i));
            }
        }
        return ans;
    }
}
