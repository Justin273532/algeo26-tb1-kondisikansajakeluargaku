package algeo.modules;

public class GaussElim {

    public enum SolutionType { UNIQUE_SOLUTION, INFINITE_SOLUTION, NO_SOLUTION }

    public static class Result {
        public SolutionType type;
        public Matrix solution;
        public String[] expression;
    }

    public static String formatNumber(double n) {
        if (n == (long) n) {
            return Long.toString((long) n);
        }

        return Double.toString(n);
    }

    public static int[] findPivotColumnAfterREF(Matrix REF) {
        int variableCount = REF.col-1;
        int[] pivotColOfRow = new int[REF.row];
        for(int i=0; i<REF.row; i++) {
            int pivotCol = -1;
            for(int j=0; j<variableCount; j++) {
                if(REF.value[i][j] != 0) {
                    pivotCol = j;
                    break;
                }
            }
            pivotColOfRow[i] = pivotCol;
        }
        return pivotColOfRow;
    }

    public static boolean isPivotColumn(int[] pivotColOfRow, int col) {
        for(int pivotCol : pivotColOfRow) {
            if(pivotCol == col) {
                return true;
            }
        }
        return false;
    }

    public static Matrix toRowEchelonFormMatrix(Matrix A) {
        Matrix B = Utilities.copyMatrix(A);
        int colidx = 0;
        for (int i=0; i<B.row && colidx<B.col-1; i++) {
            int pivotrow = OBE.findPivotRow(B, i, colidx);
            if (B.value[pivotrow][colidx] == 0) {
                colidx++;
                i--;
                continue;
            }
            if (pivotrow != i) {
                OBE.swapRow(B, i, pivotrow);
            }
            for (int j=i+1; j<B.row;j++) {
                double multiplier = -(B.value[j][colidx] / B.value[i][colidx]);
                OBE.addMultipleRow(B, multiplier, j, i);
            }
            colidx++;
        }
        return B;
    }

    public static Matrix backSubstitution(Matrix REF, int[] pivotColOfRow) {
        int variableCount = REF.col-1;
        Matrix x = Utilities.createMatrix(variableCount, 1);
        for(int i=REF.row-1; i>=0; i--) {
            int pivotCol = pivotColOfRow[i];
            if(pivotCol == -1) {
                continue;
            }
            double total = REF.value[i][variableCount];
            for(int j=pivotCol+1; j<variableCount; j++) {
                total -= REF.value[i][j] * x.value[j][0];
            }
            x.value[pivotCol][0] = total / REF.value[i][pivotCol];
        }
        return x;
    }

    public static String[] createParametricSolution(Matrix REF, int[] pivotColOfRow) {
        int variableCount = REF.col-1;
        String[] expression = new String[variableCount];
        for (int col=0; col<variableCount; col++) {
            if (!isPivotColumn(pivotColOfRow, col)) {
                expression[col] = "x" + (col+1);
            }
        }
        for (int i=REF.row-1; i>=0; i--) {
                int pivotCol = pivotColOfRow[i];
                if (pivotCol == -1) {
                    continue;
                }
                String expr = formatNumber(REF.value[i][variableCount]);
                for (int j=pivotCol+1; j<variableCount; j++) {
                    double coefficient = REF.value[i][j];
                    if (coefficient != 0) {
                        expr += " - (" + formatNumber(coefficient) + " * " + expression[j] + ")";
                    }
                }
                double pivot = REF.value[i][pivotCol];
                if (pivot != 1) {
                    expr = "(" + expr + ") / " + formatNumber(pivot);
                }
                expression[pivotCol] = expr;
            }
            return expression;
    }

    public static Result solveByGaussElim(Matrix Augmented) {
        Matrix REF = toRowEchelonFormMatrix(Augmented);
        int variableCount = Augmented.col-1;
        int[] pivotColOfRow = findPivotColumnAfterREF(REF);
        int pivotCount = 0;
        for(int i=0; i<REF.row; i++) {
            if(pivotColOfRow[i] == -1) {
                if(REF.value[i][variableCount] != 0) {
                    Result resultType = new Result();
                    resultType.type = SolutionType.NO_SOLUTION;
                    return resultType;
                }
            }
            else {
                pivotCount++;
            }
        }
        if(pivotCount < variableCount) {
            Result resultType = new Result();
            resultType.type = SolutionType.INFINITE_SOLUTION;
            resultType.expression = createParametricSolution(REF, pivotColOfRow);
            return resultType;
        }

        Result result = new Result();
        result.type = SolutionType.UNIQUE_SOLUTION;
        result.solution = backSubstitution(REF, pivotColOfRow);
        return result;
    }
}

