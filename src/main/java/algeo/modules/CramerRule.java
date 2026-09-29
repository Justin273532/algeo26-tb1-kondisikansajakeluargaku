package algeo.modules;

public class CramerRule {

    public static Matrix cramerRule(Matrix A, Matrix b) {
        if(A == null || b == null) {
            throw new IllegalArgumentException("Matrix A dan b tidak boleh kosong.");
        }
        if(!Utilities.isSquareMatrix(A)) {
            throw new IllegalArgumentException("Matrix A harus berbentuk persegi untuk kaidah Cramer.");
        }
        if(b.row != A.row || b.col != 1) {
            throw new IllegalArgumentException("Jumlah baris matrix b harus sama dengan jumlah baris matrix A dan matrix b harus punya tepat 1 kolom.");
        }
        Matrix x = Utilities.createMatrix(A.row, 1);
        double detA = Utilities.DeterminantOBE(A);
        if(detA == 0) {
            throw new IllegalArgumentException("Kaidah Cramer tidak dapat dilakukan karena determinan = 0.");
        }
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
