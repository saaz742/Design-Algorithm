
import java.io.*;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        OutputStream out = new BufferedOutputStream(System.out);
        MyScanner sc = new MyScanner();
        String st1 = sc.next();
        String st2 = sc.next();
        int gop = -1 * sc.nextInt();
        int gep = -1 * sc.nextInt();
        st1 = st1.toUpperCase();
        st2 = st2.toUpperCase();
        int[][] blo = Blosum62s();
        String all = "*ARNDCQEGHILKMFPSTWYVBZX";
        st1 = "*" + st1;
        st2 = "*" + st2;
        int n1 = st1.length();
        int n2 = st2.length();
        int[] mat1 = new int[n1];
        int[] mat2 = new int[n2];
        int[] a = new int[n2 + 1];
        Node[][] matrix = new Node[n1 + 1][n2 + 1];

        int i, j;
        for (i = 1; i < n1; i++) {
            for (j = 1; j < all.length(); j++) {
                if (st1.charAt(i) == all.charAt(j)) {
                    mat1[i] = j;
                }
            }
        }

        for (i = 1; i < n2; i++) {
            for (j = 1; j < all.length(); j++) {
                if (st2.charAt(i) == all.charAt(j)) {
                    mat2[i] = j;
                }
            }
        }


        for (j = 1; j < n2; j++) {
            a[j] = -1;
        }

        for (i = 1; i < n1; i++) {
            for (j = 1; j < n2; j++) {
                matrix[i][j] = new Node(0, blo[mat1[i]][mat2[j]], 0, 0, 0, 0, 0);
            }
        }
        matrix[0][0] = new Node(0, 0, 0, 0, 0, 0, 0);
        matrix[1][0] = new Node(gop, 0, 0, 0, 0, 0, 0);
        matrix[0][1] = new Node(gop, 0, 0, 0, 0, 0, 0);
        for (j = 2; j < n2; j++) {
            matrix[0][j] = new Node(matrix[0][j - 1].value + gep, 0, 0, 0, 0, 0, 0);
        }
        for (i = 2; i < n1; i++) {
            matrix[i][0] = new Node(matrix[i - 1][0].value + gep, 0, 0, 0, 0, 0, 0);
        }

        for (i = 1; i < n1; i++) {
            matrix[i][0].preLeft = matrix[i][0].value;
            matrix[i][0].gLeft = 1;
            matrix[i][0].gUp = i;
        }

        for (j = 1; j < n2; j++) {
            matrix[0][j].prvUp = matrix[0][j].value;
            matrix[0][j].gLeft = j;
            matrix[0][j].gUp = 1;
        }

        int e, left, up;
        for (i = 1; i < n1; i++) {
            for (j = 1; j < n2; j++) {
                e = matrix[i - 1][j - 1].value + matrix[i][j].score;
                matrix[i][j].gUp = 1;
                int val1 = matrix[i - 1][j].value + gop;
                int val2 = matrix[i - 1][j].prvUp + gep;
                if (val1 > val2) {
                    up = val1;
                } else {
                    up = val2;
                    if (i > 1)
                        matrix[i][j].gUp = matrix[i - 1][j].gUp + 1;
                }
                matrix[i][j].gLeft = 1;
                val1 = matrix[i][j - 1].value + gop;
                val2 = matrix[i][j - 1].preLeft + gep;
                if (val1 > val2) {
                    left = val1;
                } else {
                    left = val2;
                    if (j > 1) {
                        matrix[i][j].gLeft = matrix[i][j - 1].gLeft + 1;
                    }
                }
                matrix[i][j].prvUp = up;
                matrix[i][j].preLeft = left;
                if ((e > up) && (e > left)) {
                    matrix[i][j].value = e;
                    matrix[i][j].same = 1;
                } else if (up > left) {
                    matrix[i][j].value = up;
                    matrix[i][j].same = 2;
                } else {
                    matrix[i][j].value = left;
                }
            }
        }

        i = n1 - 1;
        j = n2 - 1;
        while ((i > 0) && (j > 0)) {
            if (matrix[i][j].same == 1) {
                a[j] = i;
                i--;
                j--;
            } else if (matrix[i][j].same == 2) {
                int mins = matrix[i][j].gUp;
                for (int z = 1; z <= mins; z++) {
                    if (i > 0)
                        i--;
                }
            } else {
                int mins = matrix[i][j].gLeft;
                for (int z = 1; z <= mins; z++) {
                    if (j > 0)
                        j--;
                }
            }
        }

        String str1out = "";
        String str2out = "";
        i = 1;
        j = 1;
        while (true) {
            if ((i > (n1 - 1)) && (j > (n2 - 1)))
                break;
            if ((i > (n1 - 1)) && (j < (n2 - 1))) {
                str1out = str1out + '-';
                str2out = str2out + all.charAt(mat2[j]);
                j++;
            } else if ((i < (n1 - 1)) && (j > (n2 - 1))) {
                str1out = str1out + all.charAt(mat1[i]);
                str2out = str2out + '-';
                i++;
            } else if (i == a[j]) {
                str1out = str1out + all.charAt(mat1[i]);
                str2out = str2out + all.charAt(mat2[j]);
                i++;
                j++;
            } else if (a[j] < 0) {
                str1out = str1out + '-';
                str2out = str2out + all.charAt(mat2[j]);
                j++;
            } else if (a[j] >= 0) {
                str1out = str1out + all.charAt(mat1[i]);
                str2out = str2out + '-';
                i++;
            }
        }

        out.write((matrix[n1 - 1][n2 - 1].value + "\n" + str1out + "\n" + str2out).getBytes());
        out.flush();

//        System.out.println(matrix[n1 - 1][n2 - 1].value);
//        System.out.println(str1out);
//        System.out.println(str2out);


//        for ( i = 0; i < n1 ; i++) {
//            for ( j = 0; j <n2  ; j++) {
//                System.out.printf("% 3d ", matrix[i][j].value);
//            }
//            System.out.println("");
//        }
    }

    public static int[][] Blosum62s() {
        int[][] blosum62 = {
                //A   R   N   D   C   Q   E   G   H   I   L   K   M   F   P   S   T   W   Y   V   B   Z   X  *
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
/* A */     {0, 4, -1, -2, -2, 0, -1, -1, 0, -2, -1, -1, -1, -1, -2, -1, 1, 0, -3, -2, 0, -2, -1, 0},
/* R */ {0, -1, 5, 0, -2, -3, 1, 0, -2, 0, -3, -2, 2, -1, -3, -2, -1, -1, -3, -2, -3, -1, 0, -1,},
/* N */ {0, -2, 0, 6, 1, -3, 0, 0, 0, 1, -3, -3, 0, -2, -3, -2, 1, 0, -4, -2, -3, 3, 0, -1},
/* D */ {0, -2, -2, 1, 6, -3, 0, 2, -1, -1, -3, -4, -1, -3, -3, -1, 0, -1, -4, -3, -3, 4, 1, -1},
/* C */ {0, 0, -3, -3, -3, 9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1, -3, -3, -2},
/* Q */ {0, -1, 1, 0, 0, -3, 5, 2, -2, 0, -3, -2, 1, 0, -3, -1, 0, -1, -2, -1, -2, 0, 3, -1},
/* E */ {0, -1, 0, 0, 2, -4, 2, 5, -2, 0, -3, -3, 1, -2, -3, -1, 0, -1, -3, -2, -2, 1, 4, -1},
/* G */ {0, 0, -2, 0, -1, -3, -2, -2, 6, -2, -4, -4, -2, -3, -3, -2, 0, -2, -2, -3, -3, -1, -2, -1},
/* H */ {0, -2, 0, 1, -1, -3, 0, 0, -2, 8, -3, -3, -1, -2, -1, -2, -1, -2, -2, 2, -3, 0, 0, -1},
/* I */ {0, -1, -3, -3, -3, -1, -3, -3, -4, -3, 4, 2, -3, 1, 0, -3, -2, -1, -3, -1, 3, -3, -3, -1},
/* L */ {0, -1, -2, -3, -4, -1, -2, -3, -4, -3, 2, 4, -2, 2, 0, -3, -2, -1, -2, -1, 1, -4, -3, -1},
/* K */ {0, -1, 2, 0, -1, -3, 1, 1, -2, -1, -3, -2, 5, -1, -3, -1, 0, -1, -3, -2, -2, 0, 1, -1},
/* M */ {0, -1, -1, -2, -3, -1, 0, -2, -3, -2, 1, 2, -1, 5, 0, -2, -1, -1, -1, -1, 1, -3, -1, -1},
/* F */ {0, -2, -3, -3, -3, -2, -3, -3, -3, -1, 0, 0, -3, 0, 6, -4, -2, -2, 1, 3, -1, -3, -3, -1},
/* P */ {0, -1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4, 7, -1, -1, -4, -3, -2, -2, -1, -2},
/* S */ {0, 1, -1, 1, 0, -1, 0, 0, 0, -1, -2, -2, 0, -1, -2, -1, 4, 1, -3, -2, -2, 0, 0, 0},
/* T */ {0, 0, -1, 0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1, 1, 5, -2, -2, 0, -1, -1, 0},
/* W */ {0, -3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1, 1, -4, -3, -2, 11, 2, -3, -4, -3, -2},
/* Y */ {0, -2, -2, -2, -3, -2, -1, -2, -3, 2, -1, -1, -2, -1, 3, -3, -2, -2, 2, 7, -1, -3, -2, -1},
/* V */ {0, 0, -3, -3, -3, -1, -2, -2, -3, -3, 3, 1, -2, 1, -1, -2, -2, 0, -3, -1, 4, -3, -2, -1},
/* B */ {0, -2, -1, 4, 4, -3, 0, 1, -1, 0, -3, -4, 0, -3, -3, -2, 0, -1, -4, -3, -3, 4, 1, -1},
/* Z */ {0, -1, 0, 0, 1, -3, 4, 4, -2, 0, -3, -3, 1, -1, -3, -1, 0, -1, -3, -2, -2, 1, 4, -1},
/* X */ {0, 0, -1, -1, -1, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, 0, 0, -2, -1, -1, -1, -1, -1},

        };
        return blosum62;

    }


}

class Node {
    int value;
    int score;
    int gUp;
    int gLeft;
    int preLeft;
    int prvUp;
    int same;

    public Node(int value, int score, int gUp, int gLeft, int preLeft, int prvUp, int same) {
        this.value = value;
        this.score = score;
        this.gUp = gUp;
        this.gLeft = gLeft;
        this.preLeft = preLeft;
        this.prvUp = prvUp;
        this.same = same;
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



