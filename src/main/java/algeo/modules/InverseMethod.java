package algeo.modules;

public class InverseMethod {
    public static Matrix[] splitAugmentMatrix(Matrix augmented) {
        if (augmented == null || augmented.col < 2) throw new IllegalArgumentException("Matriks augmented harus memiliki kolom koefisien dan konstanta.");
        Matrix a = new Matrix(augmented.row, augmented.col - 1), b = new Matrix(augmented.row, 1);
        for (int i = 0; i < augmented.row; i++) {
            for (int j = 0; j < a.col; j++) a.value[i][j] = augmented.value[i][j];
            b.value[i][0] = augmented.value[i][augmented.col - 1];
        }
        return new Matrix[] { a, b };
    }

    /** Compatibility overload accepting the project's traditional augmented matrix. */
    public static Matrix inverseMethod(Matrix augmented) {
        Matrix[] parts = splitAugmentMatrix(augmented);
        return inverseMethod(parts[0], parts[1]);
    }

    public static Matrix inverseMethod(Matrix a, Matrix b) {
        if (a == null || b == null) throw new IllegalArgumentException("Matrix A dan b tidak boleh kosong.");
        if (!Utilities.isSquareMatrix(a) || a.row == 0) throw new IllegalArgumentException("Matrix A harus persegi dan tidak kosong untuk metode invers.");
        if (b.row != a.row || b.col != 1) throw new IllegalArgumentException("Vektor b harus berukuran n x 1.");
        return Utilities.matrixMultiplication(Utilities.InverseMatrixAugmented(a), b);
    }
}
