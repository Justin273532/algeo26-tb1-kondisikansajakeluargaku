package algeo.modules;
import java.util.Scanner;

public class Utilities {

    public static Scanner input = new Scanner(System.in);
    
    public static Matrix createMatrix(int row, int col) {
        Matrix A = new Matrix(row, col);
        return A;
    }

    public static void showMatrix(Matrix A) {
        System.out.print("[");
        for(int i=0; i<A.row; i++) {
            System.out.print("[");
            for(int j=0; j<A.col; j++) {
                System.out.printf("%.3f", A.value[i][j]);
                if(j < A.col-1) {
                    System.out.print(" ");
                }

            }
            if(i < A.row-1) {
                System.out.println();
                System.out.print(" ");
            }
        }
        System.out.print("]");
    }

    public static void setMatrixElement(Matrix A, int row, int col, double val) {
        A.value[row][col] = val;
    }

    public static double getMatrixElement(Matrix A, int row, int col) {
        return A.value[row][col];
    }

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

    public static Matrix transposeMatrix(Matrix A) {
        Matrix B = new Matrix(A.col, A.row);
        for(int i=0; i<A.row; i++) {
            for(int j=0; j<A.col; j++) {
                B.value[j][i] = A.value[i][j];
            }
        }
        return B;
    }

    public static Matrix matrixMultiplication(Matrix A, Matrix B) {
        if(!isValidMatrixMul(A, B)) {
            throw new IllegalArgumentException("Matrix tidak dapat dikalikan karena jumlah kolom matrix A tidak sama dengan jumlah baris matrix B.");
        }
        Matrix C = new Matrix(A.row, B.col);
        for(int i=0; i<A.row; i++) {
            for(int j=0; j<B.col; j++) {
                double sumelement = 0;
                for(int k=0; k<A.col; k++) {
                    sumelement += A.value[i][k] * B.value[k][j];
                }
                C.value[i][j] = sumelement;
            }
        }
        return C;
    }

    public static Matrix matrixAddition(Matrix A, Matrix B) {
        if(!isValidMatrixAddSub(A, B)) {
            throw new IllegalArgumentException("Matrix tidak dapat dijumlahkan karena ukuran matrix tidak sama.");
        }
        Matrix C = new Matrix(A.row, A.col);
        for(int i=0; i<A.row; i++) {
            for(int j=0; j<A.col; j++) {
                C.value[i][j] = A.value[i][j] + B.value[i][j];
            }
        }
        return C;
    }

    public static Matrix matrixSubtraction(Matrix A, Matrix B) {
        if(!isValidMatrixAddSub(A, B)) {
            throw new IllegalArgumentException("Matrix tidak dapat dikurangkan karena ukuran matrix tidak sama.");
        }
        Matrix C = new Matrix(A.row, A.col);
        for(int i=0; i<A.row; i++) {
            for(int j=0; j<A.col; j++) {
                C.value[i][j] = A.value[i][j] - B.value[i][j];
            }
        }
        return C;
    }

    public static Matrix copyMatrix(Matrix A) {
        Matrix B = new Matrix(A.row, A.col);
        for(int i=0; i<A.row; i++) {
            for(int j=0; j<A.col; j++) {
                B.value[i][j] = A.value[i][j];
            }
        }
        return B;
    }

    public static Matrix createMatrixIdentity(int order) {
        Matrix A = new Matrix(order, order);
        for(int i=0; i<order; i++) {
            for(int j=0; j<order; j++) {
                if(i == j) {
                    A.value[i][j] = 1;
                }
                else {
                    A.value[i][j] = 0;
                }
            }
        }
        return A;
    }

    public static Matrix replaceColumn(Matrix A, Matrix B, int colidx) {
        Matrix C = copyMatrix(A);
        for(int i=0; i<A.row; i++) {
            C.value[i][colidx] = B.value[i][0];
        }
        return C;
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
        if(!isSquareMatrix(A)) {
            throw new IllegalArgumentException("Matrix harus berbentuk persegi untuk menghitung determinan.");
        }
        if(A.row  * A.col == 1)return A.value[0][0];
        double det = 0.00;
        for(int i=0; i<A.row; i++){
            det += power(-1, i % 2)*A.value[i][0]*Determinant(subMatrix(A, i, 0));
        }
        return det;
    }

  
    public static int power(int x, int y){
        if(y == 0)return 1;
        if(y == 1)return x;
        return power(x * x, y / 2) * power(x, y % 2);
    } 
  
    public static Matrix Cofactor(Matrix A){
        Matrix B = new Matrix(A.row, A.col);
        for(int i=0; i<A.row; i++){
           for(int j=0; j<A.col; j++){
               B.value[i][j] = power(-1, (i + j) % 2) * A.value[i][j] * Determinant(subMatrix(A, i, j)); 
           }
        }
        return B;
    }
  
    public static Matrix ScalarMatrixMul(Matrix A, double x){
        for(int i = 0; i < A.row; i++){
            for(int j = 0; j < A.col; j++){
               A.value[i][j] *= x;
            }
        }
        return A;
    }
  
    public static Matrix InverseMatrix(Matrix A){
        if(!isSquareMatrix(A)) {
            throw new IllegalArgumentException("Matrix harus berbentuk persegi untuk menghitung invers.");
        }
        if(Determinant(A) == 0){
            throw new IllegalArgumentException("Matrix tidak memiliki invers atau bersifat singularkarena determinan = 0.");
        }
        return ScalarMatrixMul(transposeMatrix(Cofactor(A)), 1/Determinant(A));
    }

    public static double DeterminantOBE(Matrix A){
        if(!isSquareMatrix(A)) {
            throw new IllegalArgumentException("Matrix harus berbentuk persegi untuk menghitung determinan.");
        }
        Matrix B = copyMatrix(A);
        int p = 0;
        double det = 1.00;
        for(int i = 0; i < B.row; i++){
            if(B.value[i][i] == 0.00){
                OBE.swapRow(B, i, OBE.findPivotRow(B, i, i));
                p++;
            }
            det *= B.value[i][i];
            for(int j=i+1; j<B.row; j++){
                OBE.addMultipleRow(B, -B.value[j][i]/B.value[i][i], j, i);
            }
        }
        return det * power(-1, p % 2);
    }

    public static Matrix InverseMatrixAugmented(Matrix A){
        if(!isSquareMatrix(A)) {
            throw new IllegalArgumentException("Matrix harus berbentuk persegi untuk menghitung invers.");
        }
        Matrix B = copyMatrix(A);
        Matrix C = createMatrixIdentity(A.row);
        
        for(int i = 0; i < B.row; i++){
            int pivotrow = OBE.findPivotRow(B, i, i);
            if(B.value[pivotrow][i] == 0.) { 
                throw new IllegalArgumentException("Matrix tidak memiliki invers atau bersifat singular karena determinan = 0.");
            }
            OBE.swapRow(B, i, pivotrow);
            OBE.swapRow(C, i, pivotrow);
            OBE.multiplyRow(C, 1/B.value[i][i], i);
            OBE.multiplyRow(B, 1/B.value[i][i], i);
            for(int j=0; j<B.row; j++){
                if(j == i) continue;
                OBE.addMultipleRow(C, -B.value[j][i]/B.value[i][i], j, i);
                OBE.addMultipleRow(B, -B.value[j][i]/B.value[i][i], j, i);
            }
        }
        return C;
    }
}