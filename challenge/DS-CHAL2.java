import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        Scanners sc = new Scanners();
        int n = sc.nextInt();
        int k = sc.nextInt();
        Map<Integer, Integer> fr = new HashMap<Integer, Integer>();
        for (int i = 0; i < n; i++) {
            int num = sc.nextInt();
            int v = sc.nextInt();
            if (fr.containsKey(num))
                fr.replace(num, fr.get(num), fr.get(num) + v);
            else if (fr.size() <= k - 1) {
                fr.put(num, v);
            } else {
                int[] s = new int[k];
                int r = 0;
                // for (Integer key : fr.keySet()) {
                int mink = keys(Collections.min(fr.values()), fr);
                int minv = Collections.min(fr.values());
                fr.replace(mink, minv, minv - v);
                //fr.replace(key, fr.get(key), fr.get(key) -v);
                if (minv - v <= 0) {
                    s[r] = mink;
                    r++;
                }
                //}
                for (int j = 0; j < s.length; j++) {
                    fr.remove(s[j]);
                }
                if (s[0] != 0)
                    fr.put(num, v);

            }
        }
        for (Integer key : fr.keySet()) {
            System.out.print(key + " ");
        }
    }

    public static int keys(int v, Map<Integer, Integer> fr) {
        for (int k : fr.keySet()) {
            if (fr.get(k) == v)
                return k;
        }
        return 0;
    }
}


class Scanners {
    BufferedReader br;
    StringTokenizer st;

    public Scanners() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    String next() {
        while (st == null || !st.hasMoreElements()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return st.nextToken();
    }

    int nextInt() {
        return Integer.parseInt(next());
    }
}




