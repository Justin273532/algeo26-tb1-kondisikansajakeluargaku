package algeo.modules;

public class GaussJordanElim {
    public static Matrix toReducedRowEchelonFormMatrix(Matrix A) {
        Matrix B = GaussElim.toRowEchelonFormMatrix(A);   
        int colidx = 0;
        for (int i=0; i<B.row && colidx<B.col-1; i++) {
            while (colidx < B.col-1 && B.value[i][colidx] == 0) {
                colidx++;
            }
            if (colidx >= B.col-1) {
                break;
            }
            double pivotvalue = B.value[i][colidx];
            OBE.multiplyRow(B, 1/pivotvalue, i);
            for (int j=0; j<i; j++) {
                double multiplier = -(B.value[j][colidx]);
                OBE.addMultipleRow(B, multiplier, j, i);
            }
            colidx++;
        }
        return B;
    }

    public static GaussElim.Result solveByGaussJordan(Matrix Augmented) {
        Matrix RREF = toReducedRowEchelonFormMatrix(Augmented);
        int variableCount = RREF.col - 1;
        int[] pivotColOfRow = GaussElim.findPivotColumnAfterREF(RREF);
        int pivotCount = 0;
        for (int i = 0; i < RREF.row; i++) {
            if (pivotColOfRow[i] == -1) {
                if (RREF.value[i][variableCount] != 0) {
                    GaussElim.Result result = new GaussElim.Result();
                    result.type = GaussElim.SolutionType.NO_SOLUTION;
                    return result;
                }
            } else {
                pivotCount++;
            }
        }
        if (pivotCount < variableCount) {
            GaussElim.Result result = new GaussElim.Result();
            result.type = GaussElim.SolutionType.INFINITE_SOLUTION;
            result.expression = GaussElim.createParametricSolution(RREF, pivotColOfRow);
            return result;
        }
        GaussElim.Result result = new GaussElim.Result();
        result.type = GaussElim.SolutionType.UNIQUE_SOLUTION;
        result.solution = GaussElim.backSubstitution(RREF, pivotColOfRow);
        return result;
    }
}
