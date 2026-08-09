public class Solution {
    public int[] plusOne(int[] A) {
        int n = A.length;
        int arrLength = 0;
        int zeros = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] != 0) {
                break;
            } else {
                zeros++;
            }
        }
        int nines = 0;
        for (int i = zeros; i < n; i++) {
            if (A[i] != 9) {
                break;
            } else {
                nines++;
            }
        }
        if (nines == (n - zeros)) {
            arrLength = n - zeros + 1;
        } else {
            arrLength = n - zeros;
        }
        int[] ans = new int[arrLength];
        int carry = 0;
        if (A[n - 1] == 9) {
            ans[arrLength - 1] = 0;
            carry = 1;
        } else {
            ans[arrLength - 1] = A[n - 1] + 1;
        }
        for (int i = n - 2, j = arrLength - 2; i >= 0 && j >= 0; i--, j--) {
            if (carry == 1) {
                if (A[i] == 9) {
                    ans[j] = 0;
                } else {
                    ans[j] = A[i] + 1;
                    carry = 0;
                }
            } else {
                ans[j] = A[i];
            }
        }
        if (ans[0] == 0) {
            ans[0] = 1;
        }
        return ans;
    }
}
