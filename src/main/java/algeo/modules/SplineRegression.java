package algeo.modules;

/** Least-squares cubic spline regression using a truncated-power basis. */
public class SplineRegression {
    public static Matrix fit(Matrix points, double[] knots) {
        if (points == null || points.col != 2 || points.row == 0) throw new IllegalArgumentException("Data regresi harus memiliki baris berformat x dan y.");
        Utilities.requireFiniteMatrix(points);
        if (knots == null) throw new IllegalArgumentException("Daftar knot tidak boleh null.");
        for (int i = 0; i < knots.length; i++) {
            if (!Double.isFinite(knots[i])) throw new IllegalArgumentException("Knot harus berupa bilangan finite.");
            for (int j = 0; j < i; j++) if (Math.abs(knots[i] - knots[j]) <= Utilities.EPS) throw new IllegalArgumentException("Knot harus berbeda.");
        }
        int terms = 4 + knots.length;
        if (points.row < terms) throw new IllegalArgumentException("Jumlah data harus sekurangnya sama dengan jumlah koefisien (" + terms + ").");
        Matrix design = new Matrix(points.row, terms), y = new Matrix(points.row, 1);
        for (int i = 0; i < points.row; i++) {
            double x = points.value[i][0];
            for (int j = 0; j < 4; j++) design.value[i][j] = Math.pow(x, j);
            for (int j = 0; j < knots.length; j++) design.value[i][4 + j] = Math.pow(Math.max(0.0, x - knots[j]), 3);
            y.value[i][0] = points.value[i][1];
        }
        Matrix transpose = Utilities.transposeMatrix(design);
        Matrix normal = Utilities.matrixMultiplication(transpose, design);
        Matrix rhs = Utilities.matrixMultiplication(transpose, y);
        Matrix augmented = PolinomialInterpolation.createAugmentedMatrix(normal, rhs);
        GaussElim.Result result = GaussElim.solveByGaussElim(augmented);
        if (result.type != GaussElim.SolutionType.UNIQUE_SOLUTION) throw new IllegalArgumentException("Basis spline tidak memiliki rank penuh; koefisien regresi tidak dapat ditentukan secara unik.");
        return result.solution;
    }

    public static double evaluate(double x, Matrix coefficients, double[] knots) {
        if (coefficients == null || coefficients.col != 1 || knots == null || coefficients.row != 4 + knots.length)
            throw new IllegalArgumentException("Dimensi koefisien dan knot tidak sesuai.");
        double y = 0;
        for (int i = 0; i < 4; i++) y += coefficients.value[i][0] * Math.pow(x, i);
        for (int i = 0; i < knots.length; i++) y += coefficients.value[4 + i][0] * Math.pow(Math.max(0.0, x - knots[i]), 3);
        return y;
    }
}
