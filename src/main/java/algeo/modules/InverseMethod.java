package algeo.modules;

public class InverseMethod {

    public static Matrix[] splitAugmentMatrix(Matrix Augmented) {
        Matrix A = Utilities.createMatrix(Augmented.row, Augmented.col-1);
        Matrix b  = Utilities.createMatrix(Augmented.row,1);

        for(int i=0; i<Augmented.row; i++) {
            for(int j=0; j<(Augmented.col-1); j++) {
                Utilities.setMatrixElement(A, i, j, Augmented.value[i][j]);
            }
            Utilities.setMatrixElement(b, i,0, Augmented.value[i][Augmented.col-1]);
        }
        return new Matrix[] {A,b};
    }

    public static Matrix inverseMethod(Matrix Augmented) {
        Matrix[] parts = splitAugmentMatrix(Augmented);
        Matrix A = parts[0];
        Matrix b = parts[1];
        Matrix inverseA = Utilities.InverseMatrix(A);
        Matrix x = Utilities.matrixMultiplication(inverseA, b);
        return x;
    }
}
