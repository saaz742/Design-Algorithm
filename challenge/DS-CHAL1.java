import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Mainn {
    public static void main(String[] args) {
        Scanners sc = new Scanners();
        int n = sc.nextInt();
        int k = sc.nextInt();
        Map<Integer, Integer> fr = new HashMap<Integer, Integer>();
        for (int i = 0; i < n; i++) {
            int num = sc.nextInt();
            if (fr.containsKey(num))
                fr.replace(num, fr.get(num), fr.get(num) + 1);
            else if (fr.size() < k - 1) {
                fr.put(num, 1);
            } else {
                int[] s = new int[k];
                int r = 0;
                for (Integer key : fr.keySet()) {
                    fr.replace(key, fr.get(key), fr.get(key) - 1);
                    if (fr.get(key) == 0) {
                        s[r] = key;
                        r++;
                    }
                }
                for (int j = 0; j < s.length; j++) {
                    fr.remove(s[j]);
                }

            }
        }
        for (Integer key : fr.keySet()) {
            System.out.print(key + " ");
        }
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




