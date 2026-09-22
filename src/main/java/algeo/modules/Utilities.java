package algeo.modules;

public class Utilities {
    public Matrix subMatrix(Matrix A, int r, int c) {
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
    public double Determinant( Matrix A){
        if(A.row  * A.col == 1)return A.value[0][0];
        double det = 0.00;
        int pow = 1;
        for(int i=0; i<A.row; i++){
            det += pow*A.value[i][0]*Determinant(subMatrix(A, i, 0));
            pow *= -1;
        }
        return det;
    }
}