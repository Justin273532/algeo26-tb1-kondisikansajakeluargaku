package algeo.modules;

public class GaussElim {
    public static Matrix toRowEchelonFormMatrix(Matrix A) {
        Matrix B = Utilities.copyMatrix(A);
        int colidx = 0;
        for (int i=0; i<B.row && colidx<B.col; i++) {
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
}
