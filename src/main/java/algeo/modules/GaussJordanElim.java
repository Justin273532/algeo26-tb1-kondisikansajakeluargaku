package algeo.modules;

public class GaussJordanElim {
    public static Matrix toReducedRowEchelonFormMatrix(Matrix A) {
        Matrix B = GaussElim.toRowEchelonFormMatrix(A);   
        int colidx = 0;
        for (int i=0; i<B.row && colidx<B.col; i++) {
            while (colidx < B.col && B.value[i][colidx] == 0) {
                colidx++;
            }
            if (colidx >= B.col) {
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
}
