
import java.io.*;
import java.util.StringTokenizer;

public class Main {

    private static int[] planets;
    private static int[] ans;
    static int x = 0;

    public static void main(String[] args) throws IOException {
        MyScanner sc = new MyScanner();
        OutputStream out = new BufferedOutputStream(System.out);
        int n = sc.nextInt();
        int m = sc.nextInt();

        planets = new int[n];
        ans = new int[m];

        for (int i = 0; i < n; i++) {
            planets[i] = sc.nextInt();
        }

        for (int i = 0; i < m; i++) {
            min(sc.nextInt() - 1, sc.nextInt() - 1);
        }
        for (int i = 0; i < m; i++) {
            out.write((ans[i] + "\n").getBytes());
            out.flush();
        }
    }

    private static void min(final int first, final int last) {
        int min = planets[first];

        for (int i = first; i < last + 1; i++) {
            if (planets[i] < min) {
                min = planets[i];
            }
        }
        ans[x] = min;
        x++;

    }
}

class MyScanner {
    BufferedReader br;
    StringTokenizer st;

    public MyScanner() {
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





