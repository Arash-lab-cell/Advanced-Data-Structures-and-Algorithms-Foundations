public class Solution {
    public int solve(int A) {
        int i = 0;
        int sum = 0;
        int power = 5;
        while (i < 32) {
            int n = A & (1 << i);
            if (n != 0) {
                sum = sum + power;
            }
            power = power * 5;
            i++;
        }
        return sum;
    }
}
