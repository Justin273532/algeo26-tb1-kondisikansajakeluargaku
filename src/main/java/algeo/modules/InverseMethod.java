package algeo.modules;

public class InverseMethod {

    public static Matrix[] splitAugmentMatrix(Matrix Augmented) {
        if(Augmented.col < 2) {
            throw new IllegalArgumentException("Matrix augmented harus memiliki setidaknya 2 kolom untuk dipisahkan menjadi A dan b.");
        }
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
        if(!Utilities.isSquareMatrix(A)) {
            throw new IllegalArgumentException("Matrix A harus berbentuk persegi untuk metode invers.");
        }
        if(b.row != A.row || b.col != 1) {
            throw new IllegalArgumentException("Jumlah baris matrix b harus sama dengan jumlah baris matrix A dan matrix b harus punya tepat 1 kolom.");
        }
        if(Utilities.Determinant(A) == 0) {
            throw new IllegalArgumentException(
                "Matrix A tidak memiliki invers atau bersifat singular karena determinan = 0."
            );
        }
        Matrix inverseA = Utilities.InverseMatrix(A);
        Matrix x = Utilities.matrixMultiplication(inverseA, b);
        return x;
    }
}
