package ru.maritariny;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] parts = reader.readLine().split(" ");
        long k = Long.parseLong(parts[0]);
        long m = Long.parseLong(parts[1]);
        int d = Integer.parseInt(parts[2]);
//        LocalDateTime from = LocalDateTime.now();

        System.out.println(solveFast(k, m, d));
        System.out.println(solveSlow(k, m, d));
//        LocalDateTime to = LocalDateTime.now();
//        System.out.println(Duration.between(from, to).toMillis());

       reader.close();
    }

    public static long getRandomNumber(long min, long max) {
        return (long) ((Math.random() * (max - min)) + min);
    }
    public static int getRandomNumberInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private static void test() {
        try (FileWriter writer = new FileWriter("test6.txt", false))
        {
            long a = 0;
            long b = 1000000001;
            writer.write("70000000");
            writer.append('\n');
            for (int i = 1; i <= 10000000; i++) {
                for (int d = 1; d <= 7; d++) {
                    //long k =  getRandomNumber(a, b);
                    long m = getRandomNumber(0, 10);
                   // int d = getRandomNumberInt(1, 8);
                    long r = solveFast(i, m, d);
                    String t = i + " " + m + " " + d + " " + r;
                    writer.write(t);
                    writer.append('\n');
                }
            }
            writer.flush();
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static long solveFast(long k, long m, int d) {
        if (d > 5 && m < 3) {
            if (m == 0) return 0;
            if (d != 7) return solveSlow(k, m, d);
        }
        double a = -24.5;
        double b = 5 * k - 3.5;
        double c = m;
        double[] s = solveQuadraticEquation(a, b, c);
        double q = Math.max(s[0], s[1]);
        long numberOfWeek = (long) q;
        long result = numberOfWeek * 7 + 1;
        long perDay;
        double countBeginD = -24.5 * numberOfWeek * numberOfWeek + numberOfWeek * (5*k-3.5) + m;
        long countBegin = (long) countBeginD;
        for (int i = 1; i <= 7; i++) {
            perDay = (d > 5 ? 0 : k);
            countBegin = countBegin + perDay - result;
            if (countBegin < 0) {
                break;
            }
            result++;
            d = d % 7 + 1;
        }
        return result - 1;
    }

    private static double[] solveQuadraticEquation(double a, double b, double c) {
        double[] result = new double[2];
        if (b == 0 && c == 0) {
            result[0] = 0;
            return result;
        }
        if (b == 0 && a != 0 && c != 0) {
            double n = -c/a;
            if (n < 0 ) return null;
            result[0] = Math.sqrt(n);
            result[1] = -result[0];
            return result;
        }
        if (a == 0 && b != 0) {
            result[0] = -c / b;
            return result;
        }
        double d = b * b - 4 * a * c;
        if (d < 0) {
            return null;
        }
        if (d == 0) {
            result[0] = -b / (2 * a);
            return result;
        }
        result[0] = (-b - Math.sqrt(d)) / (2 * a);
        result[1] = (-b + Math.sqrt(d)) / (2 * a);
        return result;
    }

    private static double[] solveQuadraticEquationBigDecimal(BigDecimal a, BigDecimal b, BigDecimal c) {
        double[] result = new double[2];
        if (b.compareTo(BigDecimal.ZERO) == 0 && c.compareTo(BigDecimal.ZERO) == 0) {
            result[0] = 0;
            return result;
        }
        if (b.compareTo(BigDecimal.ZERO) == 0 && a.compareTo(BigDecimal.ZERO) != 0 && c.compareTo(BigDecimal.ZERO) != 0) {
            //double n = -c/a;
            BigDecimal n = c.multiply(new BigDecimal(-1)).divide(a);
            //if (n < 0 ) return null;
            if (n.compareTo(BigDecimal.ZERO) == -1) return null; // (n < 0 )
            //result[0] = Math.sqrt(n);
            result[0] = n.sqrt(new MathContext(10)).doubleValue();
            result[1] = -result[0];
            return result;
        }
        if (a.compareTo(BigDecimal.ZERO) == 0 && b.compareTo(BigDecimal.ZERO) != 0) {
            //result[0] = -c / b;
            result[0] = c.multiply(new BigDecimal(-1)).divide(b).doubleValue();
            return result;
        }
        BigDecimal d = b.multiply(b).subtract(a.multiply(c).multiply(new BigDecimal(4)));
        //if (d < 0) {
        if (d.compareTo(BigDecimal.ZERO) == -1) {
            return null;
        }
        if (d.compareTo(BigDecimal.ZERO) == 0) {
            //result[0] = -b / (2 * a);
            b.multiply(new BigDecimal(-1)).divide(a.multiply(new BigDecimal(2))).doubleValue();
            return result;
        }
        //result[0] = (-b - Math.sqrt(d)) / (2 * a);
        //result[1] = (-b + Math.sqrt(d)) / (2 * a);
        BigDecimal top = b.multiply(new BigDecimal(-1)).subtract(d.sqrt(new MathContext(2)));
        BigDecimal top2 = b.multiply(new BigDecimal(-1)).add(d.sqrt(new MathContext(2)));
        BigDecimal bottom = a.multiply(new BigDecimal(2));
        //BigDecimal x1 = top.divide(bottom, 2, RoundingMode.HALF_UP);
        //BigDecimal x2 = top2.divide(bottom, 2, RoundingMode.HALF_UP);
        result[0] = top.divide(bottom, 2, RoundingMode.HALF_UP).doubleValue();
        result[1] = top2.divide(bottom, 2, RoundingMode.HALF_UP).doubleValue();
        //result[0] = b.multiply(new BigDecimal(-1)).subtract(d.sqrt(new MathContext(10))).divide(a.multiply(new BigDecimal(2))).doubleValue();
        //result[1] = b.multiply(new BigDecimal(-1)).add(d.sqrt(new MathContext(10))).divide(a.multiply(new BigDecimal(2))).doubleValue();
        return result;
    }

    private static int solveSlow(long k, long m, int d) {
        int count = 0;
        long rest = m;
        long perDay;
        while (rest >= 0) {
            perDay = (d > 5 ? 0 : k);
            count++;
            rest = rest + perDay - count;
            d = d % 7 + 1;
        }
        return count - 1;
    }
}
