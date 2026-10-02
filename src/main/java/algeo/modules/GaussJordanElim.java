package algeo.modules;

public class GaussJordanElim {
    public static Matrix toReducedRowEchelonFormMatrix(Matrix augmented) {
        Matrix work = GaussElim.toRowEchelonFormMatrix(augmented);
        int variables = work.col - 1;
        int[] pivots = GaussElim.findPivotColumnAfterREF(work);
        for (int i = 0; i < work.row; i++) {
            int pivot = pivots[i];
            if (pivot < 0) continue;
            double value = work.value[i][pivot];
            for (int j = pivot; j < work.col; j++) work.value[i][j] /= value;
            work.value[i][pivot] = 1.0;
            for (int r = 0; r < i; r++) {
                double factor = work.value[r][pivot];
                if (!Utilities.isZero(factor)) {
                    for (int j = pivot; j < work.col; j++) work.value[r][j] -= factor * work.value[i][j];
                    work.value[r][pivot] = 0.0;
                }
            }
        }
        GaussElim.cleanSmallValues(work);
        return work;
    }

    public static GaussElim.Result solveByGaussJordan(Matrix augmented) {
        return GaussElim.classify(toReducedRowEchelonFormMatrix(augmented));
    }
}
