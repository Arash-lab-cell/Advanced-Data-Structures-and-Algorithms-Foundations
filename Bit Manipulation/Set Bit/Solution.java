public class Solution {
    public int solve(int A, int B) {
        int n = 0 ^ (1 << A);
        if (A == B) {
            return n;
        }
        n = n ^ (1 << B);
        return n;
    }
}
