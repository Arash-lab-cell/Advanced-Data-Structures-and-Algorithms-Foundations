public class Solution {
    public int numSetBits(int A) {
        int noOf1bits = 0;
        // method to convert integer to binary string
        String binary = Integer.toBinaryString(A);
        int n = binary.length();
        for (int i = 0; i < n; i++) {
            if (binary.charAt(i) == '1') {
                noOf1bits++;
            }
        }
        return noOf1bits;
    }
}
