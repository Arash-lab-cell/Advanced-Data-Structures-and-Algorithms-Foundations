public class Solution {
    public int solve(int A) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            int n = A & (1 << i);
            if (n != 0) {
                count++;
            }
        }
        return count;
    }
}
