package algeo.modules;

public class CramerRule {

    public static Matrix cramerRule(Matrix A, Matrix b) {
        Matrix x = Utilities.createMatrix(A.row, 1);
        double detA = Utilities.DeterminantOBE(A);
        for(int i=0; i<A.col; i++) {
            Matrix B = Utilities.copyMatrix(A);
            for(int j=0; j<A.row; j++) {
                Utilities.setMatrixElement(B, j, i, b.value[j][0]);
            }
            Utilities.setMatrixElement(x, i, 0, (Utilities.DeterminantOBE(B))/detA);
        }
        return x;
    }
}
