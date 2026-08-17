public class Solution {
    public long solve(long A, int B) {
        long mask = 0L;
        mask = ~mask;      // all bits become 1
        mask = mask << B;  // bottom B bits become 0
        A = A & mask;      // top bits survive, bottom B bits cleared
        return A;
    }
}
