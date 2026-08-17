public class Solution {
    public int solve(int A, int B) {
        int n = A & (1 << B);
        if (n != 0) {
            n = n ^ A;
        } else {
            n = A ^ (1 << B);
        }
        return n;
    }
}
