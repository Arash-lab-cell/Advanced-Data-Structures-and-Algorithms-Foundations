public class Solution {
      public int singleNumber(final int[] A) {
                int result = 0;
        for (int i = 0; i < A.length; i++) {
          result = result ^ A[i];
        }
        return result;
      }
}
