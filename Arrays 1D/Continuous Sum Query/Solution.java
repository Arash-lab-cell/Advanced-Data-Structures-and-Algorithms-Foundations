public class Solution {
    public int[] solve(int A, int[][] B) {
        int n = B.length;
        int[] arr = new int[A];
        for (int i = 0; i < n; i++) {
            // beggar no. 3 will be at index 2
            int start = B[i][0] - 1;
            int end = B[i][1] - 1;
            int amount = B[i][2];
            arr[start] = arr[start] + amount;
            if (end < arr.length - 1) {
                arr[end + 1] = arr[end + 1] - amount;
            }
        }
        for (int i = 1; i < arr.length; i++) {
            arr[i] = arr[i] + arr[i - 1];
        }
        return arr;
    }
}
