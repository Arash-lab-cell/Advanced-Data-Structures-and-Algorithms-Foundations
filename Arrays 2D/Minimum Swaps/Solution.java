public class Solution {
    public int solve(int[] A, int B) {
        int n = A.length;

        // Window size = count of elements that ultimately belong together (<= B).
        int goodElements = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] <= B) {
                goodElements++;
            }
        }

        // Bad elements (> B) inside the first window [0, goodElements-1].
        int badElements = 0;
        for (int i = 0; i < goodElements; i++) {
            if (A[i] > B) {
                badElements++;
            }
        }

        int minSwaps = badElements;

        // Slide the fixed-size window across the rest of the array.
        int i = 1;
        int j = goodElements;
        while (j < n) {
            if (A[i - 1] <= B && A[j] > B) {
                badElements++; // a good element left, a bad element entered
            } else if (A[i - 1] > B && A[j] <= B) {
                badElements--; // a bad element left, a good element entered
            }
            minSwaps = Math.min(minSwaps, badElements);
            i++;
            j++;
        }

        return minSwaps;
    }
}
