public class Solution {
    public int subarraysWithOR1(int[] A) {
        int n = A.length;
        int totalSubarray = (n * (n + 1)) / 2;
        int elementZero = 0;
        int subarrayZero = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] == 0) {
                elementZero++;
            } else {
                subarrayZero = subarrayZero + (elementZero * (elementZero + 1)) / 2;
                elementZero = 0;
            }
        }
        subarrayZero = subarrayZero + (elementZero * (elementZero + 1)) / 2;
        int subarrayOne = totalSubarray - subarrayZero;
        return subarrayOne;
    }
}
