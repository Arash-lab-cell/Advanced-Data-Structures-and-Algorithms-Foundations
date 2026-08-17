public class Solution {
    public int solve(int A) {
        if (A == 0) {
            return 0;
        }
        int samHelp = 0;
        int i = 0;
        while (i < 32) {
            int n = A & (1 << i);
            if (n != 0) {
                samHelp++;
            }
            i++;
        }
        return samHelp;
    }
}
