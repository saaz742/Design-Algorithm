import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //OutputStream out = new BufferedOutputStream(System.out);
        MyScanner scanner = new MyScanner();
        int n = scanner.nextInt();
        int m = -1*scanner.nextInt();
        int e = -1*scanner.nextInt();

        Simplex s = new Simplex(n, 4);
        double[] equation = new double[n];
        double[] equation2 = new double[n];
        for (int i = 0; i < n; i++) {
            equation[i] = -1*scanner.nextInt();
            equation2[i] = -1* scanner.nextInt();
        }
        s.addUpperBound(equation, m);
        s.addUpperBound(equation2, e);
        /*
        //x + y <= 2
        equation[0] = equation[1] = 1;
        s.addUpperBound(equation, 2);
        //x + y >= 2
        equation[0] = equation[1] = -1;
        s.addUpperBound(equation, -2);
        //x - y <= 3
        equation[0] = 1;
        equation[1] = -1;
        s.addUpperBound(equation, 3);
        //x - y >= 1
        equation[0] = -1;
        equation[1] = 1;
        s.addUpperBound(equation, -1);

         */

        double[] objective = new double[n];

        s.setObjective(objective);
        //maximize x + 2y
        for (int i = 0; i < n; i++) {
            objective[i] = -1;
        }
      //  objective[0] = 1;
     //  objective[1] = 2;

        System.out.println(-1*s.getMaximum());
       /* for (double x: s.getSolution()) {
            System.out.print(x);
            System.out.print(" ");
        }
        System.out.print("\n");

        */
    }
}
 class Simplex {
    static final double eps = 1e-9, oo = 1e1;

    private int m, n;
    private int[] B, N;
    private int[] _B, _N;
    private double[][] matrix;
    private double[] values;
    private double[][] equations;
    private double[] objective;

    private double[] solution;
    private double maximum;

    public Simplex(int n, int m) {
        this.n = n;
        this.objective = new double[n];
        Arrays.fill(objective, 1.0);
        this.equations = new double[m][n];
        this.values = new double[m];
    }

    public void setObjective(double[] objective) {
        this.objective = objective;
    }

    public void addUpperBound(double[] equation, double value) {
        for (int i = 0; i < equation.length; i++)
            equations[m][i] = equation[i];
        values[m] = value;
        m++;
    }

    private void buildMatrix() {
        if (N == null) {
            N = new int[n + 1];
            _N = new int[n + 1];
        } else {
            Arrays.fill(N, 0);
            Arrays.fill(_N, 0);
        }
        if (B == null) {
            B = new int[m];
            _B = new int[m];
        } else {
            Arrays.fill(B, 0);
            Arrays.fill(_B, 0);
        }
        if (matrix == null) {
            matrix = new double[m + 2][n + 2];
            lastPhase1 = new double[m + 2][n + 2];
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = equations[i][j];
            }
        }
        for (int i = 0; i < m; i++) {
            B[i] = n + i;
            matrix[i][n] = -1;
            matrix[i][n + 1] = values[i];
        }
        for (int j = 0; j < n; j++)
            matrix[m][j] = -objective[N[j] = j];
        matrix[m][n] = 0;
        matrix[m][n + 1] = 0;
        Arrays.fill(matrix[m + 1], 0);
        matrix[m + 1][n] = -(N[n] = -1);
    }

    private void pivot(int r, int s) {
        for (int i = 0; i < m + 2; i++)
            if (i != r)
                for (int j = 0; j < n + 2; j++)
                    if (j != s)
                        matrix[i][j] -= matrix[r][j] * matrix[i][s] / matrix[r][s];
        for (int j = 0; j < n + 2; j++)
            if (j != s)
                matrix[r][j] /= matrix[r][s];
        for (int i = 0; i < m + 2; i++)
            if (i != r)
                matrix[i][s] /= -matrix[r][s];
        matrix[r][s] = 1.0 / matrix[r][s];
        B[r] = (N[s] = (B[r] = B[r] ^ N[s]) ^ N[s]) ^ B[r];
    }

    private boolean runSimplex(int phase) {
        int x = phase == 1 ? m + 1 : m;
        while (true) {
            int s = -1;
            for (int j = 0; j <= n; j++)
                if ((phase != 2 || N[j] != -1)
                        && (s == -1 || matrix[x][j] < matrix[x][s] || matrix[x][j] == matrix[x][s] && N[j] < N[s]))
                    s = j;
            if (matrix[x][s] >= -eps)
                return true;
            int r = -1;
            for (int i = 0; i < m; i++) {
                if (matrix[i][s] < eps)
                    continue;
                if (r == -1 || matrix[i][n + 1] / matrix[i][s] < matrix[r][n + 1] / matrix[r][s]
                        || matrix[i][n + 1] / matrix[i][s] == matrix[r][n + 1] / matrix[r][s] && B[i] < B[r])
                    r = i;
            }
            if (r == -1)
                return false;
            pivot(r, s);
        }
    }

    public double[] getSolution() {
        if (solution == null)
            solve();
        return solution;
    }

    public double getMaximum() {
        if (solution == null)
            solve();
        return maximum;
    }

    double[][] lastPhase1;
    int iter = 0;

    private double solve() {
        iter ^= 1;
        if (iter == 0) {
            for (int i = 0; i < lastPhase1.length; i++) {
                for (int j = 0; j < lastPhase1[i].length; j++) {
                    matrix[i][j] = lastPhase1[i][j];
                    if (i == lastPhase1.length - 2) {
                        matrix[i][j] *= -1;
                    }
                }
            }
            for (int i = 0; i < B.length; i++) {
                B[i] = _B[i];
            }
            for (int i = 0; i < N.length; i++) {
                N[i] = _N[i];
            }
            for (int i = 0; i < m; i++)
                if (B[i] == -1) {
                    int s = -1;
                    for (int j = 0; j <= n; j++)
                        if (s == -1 || matrix[i][j] < matrix[i][s]
                                || matrix[i][j] == matrix[i][s] && N[j] < N[s])
                            s = j;
                    pivot(i, s);
                }
        } else {
            buildMatrix();
            int r = 0;
            for (int i = 1; i < m; i++)
                if (matrix[i][n + 1] < matrix[r][n + 1])
                    r = i;
            if (matrix[r][n + 1] <= -eps) {
                pivot(r, n);
                if (!runSimplex(1) || matrix[m + 1][n + 1] < -eps) {
                    // throw new IllegalStateException();
                    return maximum = -oo;
                }
                for (int i = 0; i < lastPhase1.length; i++) {
                    for (int j = 0; j < lastPhase1[i].length; j++) {
                        lastPhase1[i][j] = matrix[i][j];
                    }
                }
                for (int i = 0; i < B.length; i++) {
                    _B[i] = B[i];
                }
                for (int i = 0; i < N.length; i++) {
                    _N[i] = N[i];
                }
//					 System.out.println(Arrays.deepToString(matrix));
//					 System.out.println(Arrays.toString(N));
//					 System.out.println(Arrays.toString(B));
                for (int i = 0; i < m; i++)
                    if (B[i] == -1) {
                        int s = -1;
                        for (int j = 0; j <= n; j++)
                            if (s == -1 || matrix[i][j] < matrix[i][s]
                                    || matrix[i][j] == matrix[i][s] && N[j] < N[s])
                                s = j;
                        pivot(i, s);
                    }
            }
        }
        solution = new double[n];
        if (!runSimplex(2)) {
            return maximum = oo;
        }
        for (int i = 0; i < m; i++)
            if (B[i] < n)
                solution[B[i]] = matrix[i][n + 1];
        return maximum = matrix[m][n + 1];
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









