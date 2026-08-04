public class Solution {
    public int solve(int A) {
        int countFactors = 0;
        for (int i = 1; i * i <= A; i++) {
            if (A % i == 0) {
                if (i == A / i) {
                    countFactors++;
                } else {
                    countFactors = countFactors + 2;
                }
            }
        }
        return countFactors;
    }
}
