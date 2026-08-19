import java.util.Arrays;

public class Solution {
    public int findMinXor(int[] A) {
        int n = A.length;
        Arrays.sort(A);
        int minXOR = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            int xor = A[i - 1] ^ A[i];
            minXOR = Math.min(minXOR, xor);
        }
        return minXOR;
    }
}
