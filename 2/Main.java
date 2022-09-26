import java.io.*;
import java.util.*;

//kargah
public class Main {
    static int size = 0;
    public static ArrayList<int[]> del = new ArrayList<>();
    //static int[][] del;
   // static int t = 0;

    public static void main(String[] args) throws IOException {
        OutputStream out = new BufferedOutputStream(System.out);
        MyScanner scanner = new MyScanner();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int k = scanner.nextInt();
       // del = new int[k + 1][n + 1];
        size = k;
        Graph graph = new Graph(n, m);
        for (int i = 0; i < k; i++) {
            graph.addEdge(scanner.nextInt(), scanner.nextInt());
        }
        int[] box = new int[k];
        int t =0;
        while (size != 0) {
            box[t] = graph.find();
            t++;
        }
        // System.out.println(t);
        out.write((t +"\n").getBytes());
        out.flush();
        for (int i = 0; i < t; i++) {
            out.write((box[i] + "\n").getBytes());
            out.flush();
            for (int j = 1; j < n + 1; j++) {
                int x = del.get(i)[j];
                if (x != 0) {
                    out.write((j + " " + x + "\n").getBytes());
                    out.flush();
                }
            }
        }
    }
}

class Graph {
    static final int zero = 0;
    static final int maxNum = Integer.MAX_VALUE;
    public int n;
    public int m;
    public ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
    int[] left;
    int[] right;
    int[] match;

    public Graph(int m, int n) {
        this.n = n;
        this.m = m;
        for (int i = 0; i < n + m; i++) {
            adj.add(new ArrayList<>());
        }
    }

    void addEdge(int l, int r) {
        adj.get(l).add(r);
    }

    public void removeEdge(int l, int r) {
        adj.get(l).remove(adj.get(l).indexOf(r));
    }

    int find() {
        // 0 not matched //max matched
        match = new int[m + 1];
        //edge fro
        // m left node(index of array) to right nodes (values of indexes)
        left = new int[m + 1];
        //edge from right node(index of array) to left nodes (values of indexes)
        right = new int[n + 1];
        int time = 0;
        while (bfs()) {
            //only left side
            for (int l = 1; l <= m; l++) {
                if (left[l] == zero && dfs(l)) {
                    Main.size--;
                    time++;
                }
            }
        }
        Main.del.add( left);
        for (int l = 1; l < left.length; l++) {
            if (left[l] != 0) {
                removeEdge(l, left[l]);
            }
        }
        return time;
    }

    //augmenting path
    boolean bfs() {
        Queue<Integer> queue = new LinkedList<>();
        for (int l = 1; l <= m; l++) {
            if (left[l] == zero) {
                match[l] = 0;
                queue.add(l);
            } else
                match[l] = maxNum;
        }
        match[zero] = maxNum;
        while (!queue.isEmpty()) {
            int l = queue.poll();
            if (match[l] < match[zero]) {
                for (int i : adj.get(l)) {
                    int r = i;
                    if (match[right[r]] == maxNum) {
                        match[right[r]] = match[l] + 1;
                        queue.add(right[r]);
                    }
                }
            }
        }
        return (match[zero] != maxNum);
    }

    //l start of augmenting path
    boolean dfs(int l) {
        if (l != zero) {
            for (int i : adj.get(l)) {
                int r = i;
                if (match[right[r]] == match[l] + 1) {
                    if (dfs(right[r]) == true) {
                        left[l] = r;
                        right[r] = l;
                        return true;
                    }
                }
            }
            match[l] = maxNum;
            return false;
        }
        return true;
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









