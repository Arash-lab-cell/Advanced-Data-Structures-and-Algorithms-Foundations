public class Solution {
    public int[] nextPermutation(int[] A) {
        int n = A.length;
        if (n == 1) {
            return A;
        }

        // Finding the element that is less than its adjacent (next) element
        int pivotId = Integer.MIN_VALUE;
        for (int i = n - 2; i >= 0; i--) {
            if (A[i] < A[i + 1]) {
                pivotId = i;
                break;
            }
        }

        // Edge case: no pivot found — array is fully descending, so wrap to
        // the smallest arrangement (ascending order).
        if (pivotId == Integer.MIN_VALUE) {
            int start = 0;
            int end = n - 1;
            while (start < end) {
                int temp = A[end];
                A[end] = A[start];
                A[start] = temp;
                start++;
                end--;
            }
            return A;
        }

        // Find the element in the suffix that is just greater than the pivot
        for (int i = n - 1; i >= pivotId + 1; i--) {
            if (A[i] > A[pivotId]) {
                int temp = A[i];
                A[i] = A[pivotId];
                A[pivotId] = temp;
                break;
            }
        }

        // Reverse the suffix after the pivot to get the smallest arrangement
        int end = n - 1;
        int start = pivotId + 1;
        while (start < end) {
            int temp = A[end];
            A[end] = A[start];
            A[start] = temp;
            start++;
            end--;
        }
        return A;
    }
}
