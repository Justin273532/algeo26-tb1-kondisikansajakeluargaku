package algeo.modules;

import java.util.Arrays;

/** Natural cubic spline interpolation. Each coefficient row stores a,b,c,d for a+b*t+c*t^2+d*t^3, t=x-x_i. */
public class CubicSplineInterpolation {
    public static final class Spline {
        public final double[] x;
        public final Matrix coefficients;
        private Spline(double[] x, Matrix coefficients) { this.x = x; this.coefficients = coefficients; }

        public double evaluate(double value) {
            int interval = 0;
            if (value >= x[x.length - 1]) interval = x.length - 2;
            else if (value > x[0]) {
                int found = Arrays.binarySearch(x, value);
                interval = found >= 0 ? Math.min(found, x.length - 2) : -found - 2;
            }
            double t = value - x[interval];
            return coefficients.value[interval][0] + t * (coefficients.value[interval][1] + t * (coefficients.value[interval][2] + t * coefficients.value[interval][3]));
        }
    }

    public static Spline cubicSplineInterpolate(Matrix points) {
        if (points == null || points.col != 2 || points.row < 2) throw new IllegalArgumentException("Spline membutuhkan sedikitnya dua titik dengan dua kolom (x dan y).");
        Utilities.requireFiniteMatrix(points);
        double[][] sorted = new double[points.row][2];
        for (int i = 0; i < points.row; i++) { sorted[i][0] = points.value[i][0]; sorted[i][1] = points.value[i][1]; }
        Arrays.sort(sorted, java.util.Comparator.comparingDouble(p -> p[0]));
        int n = sorted.length;
        double[] x = new double[n], y = new double[n], h = new double[n - 1];
        for (int i = 0; i < n; i++) { x[i] = sorted[i][0]; y[i] = sorted[i][1]; }
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];
            if (h[i] <= Utilities.EPS) throw new IllegalArgumentException("Nilai x harus unik dan meningkat.");
        }
        double[] second = new double[n];
        if (n > 2) {
            int m = n - 2;
            Matrix system = new Matrix(m, m + 1);
            for (int i = 0; i < m; i++) {
                int k = i + 1;
                system.value[i][i] = 2 * (h[k - 1] + h[k]);
                if (i > 0) system.value[i][i - 1] = h[k - 1];
                if (i + 1 < m) system.value[i][i + 1] = h[k];
                system.value[i][m] = 6 * ((y[k + 1] - y[k]) / h[k] - (y[k] - y[k - 1]) / h[k - 1]);
            }
            GaussElim.Result result = GaussElim.solveByGaussElim(system);
            if (result.type != GaussElim.SolutionType.UNIQUE_SOLUTION) throw new IllegalArgumentException("Sistem spline tidak memiliki solusi unik.");
            for (int i = 0; i < m; i++) second[i + 1] = result.solution.value[i][0];
        }
        Matrix coefficients = new Matrix(n - 1, 4);
        for (int i = 0; i < n - 1; i++) {
            coefficients.value[i][0] = y[i];
            coefficients.value[i][1] = (y[i + 1] - y[i]) / h[i] - h[i] * (2 * second[i] + second[i + 1]) / 6;
            coefficients.value[i][2] = second[i] / 2;
            coefficients.value[i][3] = (second[i + 1] - second[i]) / (6 * h[i]);
        }
        return new Spline(x, coefficients);
    }
}
