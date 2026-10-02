package algeo.modules;

public class GaussElim {
    public enum SolutionType { UNIQUE_SOLUTION, INFINITE_SOLUTION, NO_SOLUTION }

    public static class Result {
        public SolutionType type;
        public Matrix solution;
        public String[] expression;
    }

    public static String formatNumber(double n) {
        return Utilities.formatDouble(n);
    }

    public static int[] findPivotColumnAfterREF(Matrix ref) {
        validateAugmented(ref);
        int variables = ref.col - 1;
        int[] pivots = new int[ref.row];
        java.util.Arrays.fill(pivots, -1);
        for (int i = 0; i < ref.row; i++) for (int j = 0; j < variables; j++) {
            if (!Utilities.isZero(ref.value[i][j])) { pivots[i] = j; break; }
        }
        return pivots;
    }

    public static boolean isPivotColumn(int[] pivots, int col) {
        for (int pivot : pivots) if (pivot == col) return true;
        return false;
    }

    public static Matrix toRowEchelonFormMatrix(Matrix augmented) {
        validateAugmented(augmented);
        Matrix work = Utilities.copyMatrix(augmented);
        int pivotRow = 0;
        for (int col = 0; col < work.col - 1 && pivotRow < work.row; col++) {
            int best = OBE.findPivotRow(work, pivotRow, col);
            if (Utilities.isZero(work.value[best][col])) continue;
            OBE.swapRow(work, pivotRow, best);
            for (int r = pivotRow + 1; r < work.row; r++) {
                double factor = work.value[r][col] / work.value[pivotRow][col];
                work.value[r][col] = 0.0;
                for (int j = col + 1; j < work.col; j++) work.value[r][j] -= factor * work.value[pivotRow][j];
            }
            pivotRow++;
        }
        cleanSmallValues(work);
        return work;
    }

    public static Matrix backSubstitution(Matrix ref, int[] pivotColOfRow) {
        validateAugmented(ref);
        int variables = ref.col - 1;
        Matrix x = new Matrix(variables, 1);
        for (int i = ref.row - 1; i >= 0; i--) {
            int pivot = pivotColOfRow[i];
            if (pivot < 0) continue;
            double rhs = ref.value[i][variables];
            for (int j = pivot + 1; j < variables; j++) rhs -= ref.value[i][j] * x.value[j][0];
            x.value[pivot][0] = rhs / ref.value[i][pivot];
        }
        return x;
    }

    public static String[] createParametricSolution(Matrix ref, int[] pivots) {
        int variables = ref.col - 1;
        String[] expression = new String[variables];
        for (int col = 0; col < variables; col++) if (!isPivotColumn(pivots, col)) expression[col] = "x" + (col + 1);
        for (int i = ref.row - 1; i >= 0; i--) {
            int pivot = pivots[i];
            if (pivot < 0) continue;
            String expr = formatNumber(ref.value[i][variables]);
            for (int j = pivot + 1; j < variables; j++) {
                double coefficient = ref.value[i][j];
                if (!Utilities.isZero(coefficient)) expr += " - (" + formatNumber(coefficient) + " * " + expression[j] + ")";
            }
            double divisor = ref.value[i][pivot];
            if (!Utilities.isZero(divisor - 1.0)) expr = "(" + expr + ") / " + formatNumber(divisor);
            expression[pivot] = expr;
        }
        return expression;
    }

    public static Result solveByGaussElim(Matrix augmented) {
        return classify(toRowEchelonFormMatrix(augmented));
    }

    static Result classify(Matrix echelon) {
        int variables = echelon.col - 1;
        int[] pivots = findPivotColumnAfterREF(echelon);
        int pivotCount = 0;
        for (int i = 0; i < echelon.row; i++) {
            if (pivots[i] < 0) {
                if (!Utilities.isZero(echelon.value[i][variables])) {
                    Result result = new Result(); result.type = SolutionType.NO_SOLUTION; return result;
                }
            } else pivotCount++;
        }
        Result result = new Result();
        if (pivotCount < variables) {
            result.type = SolutionType.INFINITE_SOLUTION;
            result.expression = createParametricSolution(echelon, pivots);
        } else {
            result.type = SolutionType.UNIQUE_SOLUTION;
            result.solution = backSubstitution(echelon, pivots);
        }
        return result;
    }

    static void validateAugmented(Matrix a) {
        if (a == null || a.row == 0 || a.col < 2) throw new IllegalArgumentException("Matriks augmented harus memiliki baris dan minimal satu variabel serta satu kolom konstanta.");
        Utilities.requireFiniteMatrix(a);
    }

    static void cleanSmallValues(Matrix a) {
        for (int i = 0; i < a.row; i++) for (int j = 0; j < a.col; j++) if (Utilities.isZero(a.value[i][j])) a.value[i][j] = 0.0;
    }
}
