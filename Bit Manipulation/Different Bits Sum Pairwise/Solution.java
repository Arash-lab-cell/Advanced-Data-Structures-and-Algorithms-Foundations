public class Solution {
    public int cntBits(int[] A) {
        int n = A.length;
        long total = 0;
        long mod = 1_000_000_007L;
        for (int i = 0; i < 31; i++) {
            long c = 0;
            for (int j = 0; j < A.length; j++) {
                int bit = (A[j] >> i) & 1;
                if (bit == 1) {
                    c++;
                }
            }
            long contribution = 2 * c * (A.length - c);
            total = (total + contribution) % mod;
        }
        return (int) total;
    }
}
