public class Solution {
    public int solve(int[] A) {
        int n = A.length;
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int chk1s = 0;
            for (int j = 0; j < n; j++) {
                int chk = A[j] & (1 << (31 - i));
                if (chk != 0) {
                    chk1s++;
                }
            }
            if (chk1s > 1) {
                for (int j = 0; j < n; j++) {
                    int chk = A[j] & (1 << (31 - i));
                    if (chk == 0) {
                        A[j] = 0;
                    }
                }
                ans = ans | (1 << (31 - i));
            }
        }
        return ans;
    }
}
