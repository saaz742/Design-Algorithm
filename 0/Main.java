import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


public class Main {

    public static void main(String[] args) throws IOException {
        MyScanner sc = new MyScanner();
        OutputStream out = new BufferedOutputStream(System.out);
        int n = sc.nextInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            points[i] = new Point(sc.nextInt(), sc.nextInt());
        }

        Arrays.sort(points, (p1, p2) -> (int) (p1.x - p2.x));
        // Point[] convex_hull = part1((points));
        // Point[] convex_hull = part2((points));
        Point[] convex_hull = part3((points));
        for (int i = 0; i < convex_hull.length; i++) {
            out.write((convex_hull[i].x + " " + convex_hull[i].y + "\n").getBytes());
            out.flush();
        }
    }

    private static Point[] part1(Point[] p) {
        int n = p.length;
        Point p1 = p[0];
        Point p2;
        int z = 0;
        for (int i = 1; i < n; i++) {
            if (p[i].x < p1.x) {
                p1 = p[i];
                z = i;
            }
        }
        ArrayList<Point> convex_hull = new ArrayList<>();
        //  Point[] convex_hull = new Point[n];
        do {
            convex_hull.add(p1);
            z = (z + 1) % n;
            p2 = p[z];
            for (int i = 0; i < n; i++) {
                Point p3 = p[i];
                if (((p2.x - p3.x) * (p3.y - p1.y)) - ((p3.x - p1.x) * (p2.y - p3.y)) > 0) {
                    p2 = p3;
                    z = i;
                }
            }
            p1 = p2;
        } while (p1 != convex_hull.get(0));
        Point[] convex_hull1 = new Point[convex_hull.size()];
        for (int i = 0; i < convex_hull.size(); i++) {
            convex_hull1[i] = convex_hull.get(i);
        }
        return convex_hull1;
    }


    private static Point[] part2(Point p[]) {
        int n = p.length;
        if (n < 10) {
            return part1(p);
        }
        Point[] c1 = new Point[n / 2];
        Point[] c2 = new Point[n / 2];
        if (n % 2 == 1) {
            c2 = new Point[n / 2 + 1];
        }

        for (int i = 0; i < n / 2; i++) {
            c1[i] = p[i];
        }
        for (int i = n / 2; i < n; i++) {
            c2[i - n / 2] = p[i];
        }

        Point[] convex_hull_1 = part2(c1);
        Point[] convex_hull_2 = part2(c2);

        return merge(convex_hull_1, convex_hull_2);

        //  for (int i = 0; i < n; i++) {
        //      if (p.x > m.x)
        //          c1 += p[i];
        //     else
        //        c2 += p[i];
        // }
    }

    private static Point[] part3(Point[] p) {
        Point[] convex = part2(p);
        int n = convex.length;
        Point[] closest = new Point[2];

        if (n < 3)
            return convex;

        Point a = convex[0];
        Point b = convex[1];
        Point c = convex[2];
        int index = 1;
        int cIndex = 2;
        Point max = c;
        Point max2 = c;
        double dis = 0;
        double rdis = 0;

        do {
            for (int i = cIndex; i < n; i++) {
                double newDis = distFromLine(a, b, convex[i]);
                double newDisc = distTwoPoints(a,  convex[i]);
                if (newDis >= dis) {
                    dis = newDis;
                    c = convex[i];
                    cIndex = i;
                }
                if (newDisc >= rdis) {
                    rdis = newDisc;
                    max = a;
                    max2 = convex[i];
                }
            }

            // if (cIndex > 2) {
            for (int i = 0; i < cIndex; i++) {
                double newDis = distFromLine(a, b, convex[i]);
                double newDisc = distTwoPoints(a,  convex[i]);
                if (newDis >= dis) {
                    dis = newDis;
                    c = convex[i];
                    cIndex = i;
                }
                if (newDisc >= rdis) {
                    rdis = newDisc;
                    max = a;
                    max2 = convex[i];
                }
            }
            // }

            double newDisc = distTwoPoints(a, c);
            if (newDisc >= rdis) {
                rdis = newDisc;
                max = a;
                max2 = c;
            }

            newDisc = distTwoPoints(b, c);
            if (newDisc >= rdis) {
                rdis = newDisc;
                max = b;
                max2 = c;
            }
//            newDis = distTwoPoints(a, b);
//            if (newDis >= rdis) {
//                rdis = newDis;
//                max = a;
//                max2 = b;
//            }

            a = b;
            index = (index + 1) % n;
            b = convex[index];

        } while (index != 0);

        if (max2.x < max.x) {
            closest[1] = max;
            closest[0] = max2;
        } else if (max2.x > max.x) {
            closest[0] = max;
            closest[1] = max2;
        } else {
            if (max2.y <= max.y) {
                closest[1] = max;
                closest[0] = max2;
            } else {
                closest[0] = max;
                closest[1] = max2;
            }

        }
        return closest;
    }

    public static double distFromLine(Point a, Point b, Point target) {
        long p = b.y - a.y;
        long q = a.x - b.x;
        long r = b.x * a.y - a.x * b.y;

        return Math.abs(p * target.x + q * target.y + r) / Math.sqrt(Math.pow(p, 2) + Math.pow(q, 2));
    }

    public static double distTwoPoints(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    static int check(Point p1, Point p2, Point p3) {
        long x = (p2.y - p1.y) * (p3.x - p2.x) - (p3.y - p2.y) * (p2.x - p1.x);
        if (x == 0)
            return 0;
        else if (x > 0)
            return 1;
        return -1;
    }

    static Point[] merge(Point[] convex_hull1, Point[] convex_hull2) {
        int n1 = convex_hull1.length;
        int n2 = convex_hull2.length;

        int leftMost2 = 0;
        int rightMost1 = 0;

        //most left point
        for (int i = 1; i < n2; i++) {
            if (convex_hull2[i].x < convex_hull2[leftMost2].x)
                leftMost2 = i;
            else if (convex_hull2[i].x == convex_hull2[leftMost2].x) {
                if (convex_hull2[i].y > convex_hull2[leftMost2].y)
                    leftMost2 = i;
            }


        }

        //most right point
        for (int i = 1; i < n1; i++) {
            if (convex_hull1[i].x > convex_hull1[rightMost1].x)
                rightMost1 = i;
            else if (convex_hull1[i].x == convex_hull1[leftMost2].x) {
                if (convex_hull1[i].y > convex_hull1[leftMost2].y)
                    rightMost1 = i;
            }
        }

        int p2_2 = leftMost2;
        int p2_1 = rightMost1;

        boolean find = false;

        //upper points
        while (!find) {
            find = true;
            while (check(convex_hull2[p2_2], convex_hull1[p2_1], convex_hull1[(p2_1 + 1) % n1]) <= 0)
                p2_1 = (p2_1 + 1) % n1;

            while (check(convex_hull1[p2_1], convex_hull2[p2_2], convex_hull2[(n2 + p2_2 - 1) % n2]) >= 0) {
                p2_2 = (n2 + p2_2 - 1) % n2;
                find = false;
            }
        }

        int up1 = p2_1;
        int up2 = p2_2;

        p2_1 = rightMost1;
        p2_2 = leftMost2;
        find = false;

        //lower points
        while (!find) {
            find = true;
            while (check(convex_hull1[p2_1], convex_hull2[p2_2], convex_hull2[(p2_2 + 1) % n2]) <= 0)
                p2_2 = (p2_2 + 1) % n2;

            while (check(convex_hull2[p2_2], convex_hull1[p2_1], convex_hull1[(n1 + p2_1 - 1) % n1]) >= 0) {
                p2_1 = (n1 + p2_1 - 1) % n1;
                find = false;
            }
        }

        int down1 = p2_1;
        int down2 = p2_2;

        ArrayList<Point> convex = new ArrayList<>();

        //right
        int point = up1;
        convex.add(convex_hull1[up1]);
        while (point != down1) {
            point = (point + 1) % n1;
            convex.add(convex_hull1[point]);
        }

        //left
        point = down2;
        convex.add(convex_hull2[down2]);
        while (point != up2) {
            point = (point + 1) % n2;
            convex.add(convex_hull2[point]);
        }

        //convex from mostleft
        int convSize = convex.size();
        Point[] conv = new Point[convSize];
        int x = 0;
        Point lm = convex.get(0);
        for (int i = 1; i < convSize; i++) {
            if (lm.x > convex.get(i).x) {
                x = i;
                lm = convex.get(i);
            }

        }
        for (int i = x; i < convSize; i++) {
            conv[i - x] = convex.get(i);
        }
        for (int i = 0; i < x; i++) {
            conv[convSize - x + i] = convex.get(i);
        }
        return conv;
    }
}

class Point {
    long x;
    long y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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





