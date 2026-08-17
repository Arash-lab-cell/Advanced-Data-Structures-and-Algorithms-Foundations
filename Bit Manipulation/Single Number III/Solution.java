import java.util.ArrayList;

public class Solution {
    public int[] solve(int[] A) {
        int n = A.length;
        int xor = 0;
        for (int i = 0; i < n; i++) {
            xor = xor ^ A[i];
        }
        int p = 0;
        while (true) {
            int setBit = xor & (1 << p);
            if (setBit != 0) break;
            p++;
        }
        ArrayList<Integer> set = new ArrayList<>();
        ArrayList<Integer> unSet = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int setBit = A[i] & (1 << p);
            if (setBit != 0) set.add(A[i]);
            else unSet.add(A[i]);
        }
        int unique1 = 0, unique2 = 0;
        for (int v : set) unique1 ^= v;
        for (int v : unSet) unique2 ^= v;
        int[] ans = new int[2];
        if (unique1 > unique2) { ans[0] = unique2; ans[1] = unique1; }
        else { ans[0] = unique1; ans[1] = unique2; }
        return ans;
    }
}
