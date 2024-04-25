import java.io.*;
import java.util.*;

public class Main {
    static int node;

    public static void main(String[] args) throws IOException {
        OutputStream out = new BufferedOutputStream(System.out);
        MyScanner scanner = new MyScanner();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int ns = scanner.nextInt();
        int nt = scanner.nextInt();
        int max = Integer.MAX_VALUE;
        Graph graph = new Graph(n, m);
        for (int i = 0; i < ns; i++) {
            graph.srcs.add(scanner.nextInt());
        }
        for (int i = 0; i < nt; i++) {
            graph.dests.add(scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            graph.addEdge(scanner.nextInt(), scanner.nextInt());
        }
        node = 2 * n + 2 - nt - ns;
        int ngraph[][] = new int[node][node];
        for (int s : Graph.srcs) {
            ngraph[0][s] = max;
        }
        for (int d : Graph.dests) {
            ngraph[d][node - 1] = max;
        }

        int p =1;
        for (int i = 1; i <= n; i++) {
            if (isThereSrc(i)) {
             // ngraph[0][i] = max;
                for (int j : graph.adj.get(i)) {
                    ngraph[i][j] = 1;
                }
            }
            else if (!isThereDest(i)) {
                ngraph[i][n + p] = 1;
                for (int j : graph.adj.get(i)) {
                    ngraph[n + p][j] = 1;
                }
                p++;
            }


        }

        int c = graph.fulkerson(ngraph, 0, node - 1);
        //System.out.println( maxf.fordFulkerson(ngraph, 0, node - 1));
        out.write((c +"\n").getBytes());
        out.flush();
    }

    static boolean isThereSrc(int i) {
        for (int s : Graph.srcs) {
            if (s == i)
                return true;
        }
        return false;
    }

    static boolean isThereDest(int i) {
        for (int s : Graph.dests) {
            if (s == i)
                return true;
        }
        return false;
    }
}


class Graph {
    public int n;
    public int m;
    public ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
    public static ArrayList<Integer> srcs = new ArrayList<>();
    public static ArrayList<Integer> dests = new ArrayList<>();

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

    boolean bfs(int graph[][], int s, int t, int parent[]) {
        boolean visit[] = new boolean[Main.node];
        for (int i = 0; i < Main.node; ++i)
            visit[i] = false;

        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visit[s] = true;
        parent[s] = -1;
        while (queue.size() != 0) {
            int u = queue.poll();
            for (int v = 0; v < Main.node; v++) {
                if (visit[v] == false && graph[u][v] > 0) {
                    if (v == t) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visit[v] = true;
                }
            }
        }
        return false;
    }

    int fulkerson(int graph[][], int s, int t) {
        int u, v;
        int parent[] = new int[Main.node];
        int paths =0;
        while (bfs(graph, s, t, parent)) {
            paths++;
            int flow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                flow = Math.min(graph[u][v], flow);
            }
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                graph[v][u] += flow;
                graph[u][v] -= flow;
            }
        }
        return paths;
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









