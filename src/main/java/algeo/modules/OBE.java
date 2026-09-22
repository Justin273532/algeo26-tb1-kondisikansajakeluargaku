package algeo.modules;

public class OBE {

    public static void swapRow(Matrix A, int row1, int row2) {
        for(int i=0; i<A.col; i++) {
            double temp = A.value[row1][i];
            A.value[row1][i] = A.value[row2][i];
            A.value[row2][i] = temp;
        }
    }

    public static void multiplyRow(Matrix A, double n, int rowidx) {
        for(int i=0; i<A.col; i++) {
            A.value[rowidx][i] *= n;
        }
    }
    
    public static void addMultipleRow(Matrix A, double n, int row1, int row2) {
        for(int i=0; i<A.col; i++) {
            A.value[row1][i] += (A.value[row2][i] * n);
        }
    }

    public static int findPivotRow(Matrix A, int startrowidx, int colidx) {
        int pivotrow = startrowidx;
        for(int i=startrowidx+1; i<A.row; i++) {
            if(Math.abs(A.value[i][colidx]) > Math.abs(A.value[pivotrow][colidx])) {
                pivotrow = i;
            }
        }
        return pivotrow;
    }
}
