public class Solution {
    public int solve(int A, int B) {
        int n = A & (1 << B);
        if (n != 0) {
            return n ^ A;
        }
        return A;
    }
}
