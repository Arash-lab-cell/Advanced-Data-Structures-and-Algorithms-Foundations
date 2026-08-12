public class Solution {
    public int firstMissingPositive(int[] A) {
        int n = A.length;

        // Step 1: push every non-positive value out of the [1..n] range
        // so it can never collide with a legitimate index-marker later.
        for (int i = 0; i < n; i++) {
            if (A[i] <= 0) {
                A[i] = n + 2;
            }
        }

        // Step 2: for every value v with 1 <= v <= n, mark index v-1 as "seen"
        // by flipping its sign negative.
        for (int i = 0; i < n; i++) {
            if (Math.abs(A[i]) <= n) {
                A[Math.abs(A[i]) - 1] = -1 * Math.abs(A[Math.abs(A[i]) - 1]);
            }
        }

        // Step 3: the first index still positive was never marked -> value i+1 is missing.
        for (int i = 0; i < n; i++) {
            if (A[i] > 0) {
                return i + 1;
            }
        }

        return n + 1;
    }
}
