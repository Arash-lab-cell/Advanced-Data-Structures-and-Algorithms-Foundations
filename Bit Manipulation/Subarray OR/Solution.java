public class Solution {
    public int solve(int[] A) {
        int n = A.length;
        long totalSubarray = (long) n * (n + 1) / 2;
        long ans = 0;
        for (int i = 0; i < 32; i++) {
            long count0 = 0;
            long subarray0 = 0;
            long subarray1 = 0;
            long bitValue = 1L << (31 - i);
            for (int j = 0; j < n; j++) {
                long chk = A[j] & bitValue;
                if (chk == 0) {
                    count0++;
                } else {
                    subarray0 = subarray0 + (count0 * (count0 + 1) / 2);
                    count0 = 0;
                }
            }
            subarray0 = subarray0 + (count0 * (count0 + 1) / 2);
            subarray1 = totalSubarray - subarray0;
            ans = ans + subarray1 * bitValue;
        }
        long mod = 1000000007L;
        return (int) (ans % mod);
    }
}
