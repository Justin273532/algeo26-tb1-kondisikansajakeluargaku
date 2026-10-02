package algeo.modules;

public class PolinomialInterpolation {
    public static Matrix createAugmentedMatrix(Matrix a, Matrix b) {
        if (a == null || b == null) throw new IllegalArgumentException("Matrix A dan b tidak boleh kosong.");
        if (a.row != b.row || b.col != 1) throw new IllegalArgumentException("Vektor b harus memiliki jumlah baris yang sama dengan A dan satu kolom.");
        Matrix out = new Matrix(a.row, a.col + 1);
        for (int i = 0; i < a.row; i++) {
            System.arraycopy(a.value[i], 0, out.value[i], 0, a.col);
            out.value[i][a.col] = b.value[i][0];
        }
        return out;
    }

    /** Returns coefficients in ascending powers: c[0] + c[1]x + ... */
    public static Matrix polinomialInterpolate(Matrix points) {
        if (points == null || points.col != 2 || points.row < 2) throw new IllegalArgumentException("Data interpolasi harus berisi sedikitnya dua titik dengan dua kolom (x dan y).");
        Utilities.requireFiniteMatrix(points);
        Matrix a = new Matrix(points.row, points.row), b = new Matrix(points.row, 1);
        for (int i = 0; i < points.row; i++) {
            for (int k = 0; k < i; k++) if (Math.abs(points.value[i][0] - points.value[k][0]) <= Utilities.EPS)
                throw new IllegalArgumentException("Nilai x pada titik interpolasi harus berbeda.");
            double power = 1.0;
            for (int j = 0; j < points.row; j++) { a.value[i][j] = power; power *= points.value[i][0]; }
            b.value[i][0] = points.value[i][1];
        }
        GaussElim.Result result = GaussElim.solveByGaussElim(createAugmentedMatrix(a, b));
        if (result.type != GaussElim.SolutionType.UNIQUE_SOLUTION) throw new IllegalArgumentException("Koefisien interpolasi tidak dapat ditentukan secara unik.");
        return result.solution;
    }

    public static double evaluate(Matrix coefficients, double x) {
        if (coefficients == null || coefficients.col != 1) throw new IllegalArgumentException("Koefisien polinomial harus berupa vektor kolom.");
        double value = 0;
        for (int i = coefficients.row - 1; i >= 0; i--) value = value * x + coefficients.value[i][0];
        return value;
    }
}
