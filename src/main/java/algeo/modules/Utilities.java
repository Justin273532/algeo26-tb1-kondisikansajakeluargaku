package algeo.modules;

public class Utilities {

    public static boolean isSquareMatrix(Matrix A) {
        return A.row == A.col;
    }

    public static boolean isMatrixIdentity(Matrix A) {
        if(!isSquareMatrix(A)) {
            return false;
        }
        for (int i = 0; i < A.row; i++) {
            for (int j = 0; j < A.col; j++) {
                if (i == j) {
                    if (A.value[i][j] != 1) {
                        return false;
                    }
                }
                else {
                    if (A.value[i][j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isValidMatrixAddSub(Matrix A, Matrix B) {
        return (A.row == B.row) && (A.col == B.col);
    }

    public static boolean isValidMatrixMul(Matrix A, Matrix B) {
        return (A.col == B.row);
    }
    
    public static Matrix subMatrix(Matrix A, int r, int c) {
        Matrix B = new Matrix((A.row - 1), (A.col -1));
        int flagR = 0;
        int flagC = 0;
        for(int i=0; i<A.row; i++){
            for(int j=0; j<A.col; j++){
                if(i == r || j == c){
                    if(j == c) flagC = -1;
                    if(i == r) {
                        flagR = -1;
                        break;
                    }
                }else {
                    B.value[i + flagR][j + flagC] = A.value[i][j];
                }
            }
            flagC = 0;
        }
        return B;
    }
  
    public static double Determinant(Matrix A){
        if(A.row  * A.col == 1)return A.value[0][0];
        double det = 0.00;
        int pow = 1;
        for(int i=0; i<A.row; i++){
            det += pow*A.value[i][0]*Determinant(subMatrix(A, i, 0));
            pow *= -1;
        }
        return det;
    }
  
    public static Matrix TransposeMatrix(Matrix A){
        Matrix B = new Matrix(A.col, A.row);
        for(int i = 0; i < A.row; i++){
            for(int j = 0; j < A.col; j++){
                B.value[j][i] = A.value[i][j];
            }
        }
        return B;
    }
  
    public static int power(int x, int y){
        if(y == 0)return 1;
        if(y == 1)return x;
        return power(x*x, y / 2)*power(x, y % 2);
    }
  
    public static Matrix Cofactor(Matrix A){
        Matrix B = new Matrix(A.row, A.col);
        for(int i=0; i<A.row; i++){
           for(int j=0; j<A.col; j++){
               B.value[i][j] = power(-1, i + j) * A.value[i][j] * Determinan(subMatrix(A, i, j)); 
           }
        }
        return B;
    }
  
    public static Matrix ScalarMatrixMul(Matrix A, double x){
        for(int i=0; i < A.row; i++){
            for(int j=0; j < A.col; j++){
               A.value[i][j] *= x;
            }
        }
        return A;
    }
  
    public static Matrix InverseMatrix(Matrix A){
        return ScalarMatrixMul(TransposeMatrix(Cofactor(Matrix A)), 1/Determinant(A));
    }
}