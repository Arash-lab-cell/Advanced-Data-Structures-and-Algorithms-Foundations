public class Solution {
    public int solve(int A) {
        int copy = A;
        int X = A;
        int lastBitCount = 0;
        while (copy != 0) {
            copy = copy >> 1;
            lastBitCount++;
        }
        for (int i = 0; i < lastBitCount; i++) {
            X = X ^ (1 << i);
        }
        int Y = 1 << (lastBitCount);
        return (X ^ Y);
    }
}
