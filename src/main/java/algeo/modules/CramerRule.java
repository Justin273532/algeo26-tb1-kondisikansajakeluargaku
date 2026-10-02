package algeo.modules;

public class CramerRule {
    public static Matrix cramerRule(Matrix a, Matrix b) {
        if (a == null || b == null) throw new IllegalArgumentException("Matrix A dan b tidak boleh kosong.");
        if (!Utilities.isSquareMatrix(a) || a.row == 0) throw new IllegalArgumentException("Matrix A harus persegi dan tidak kosong untuk kaidah Cramer.");
        if (b.row != a.row || b.col != 1) throw new IllegalArgumentException("Vektor b harus berukuran n x 1.");
        double detA = Utilities.DeterminantOBE(a);
        if (Utilities.isZero(detA)) throw new IllegalArgumentException("Kaidah Cramer tidak dapat dilakukan karena determinan A bernilai nol.");
        Matrix x = new Matrix(a.row, 1);
        for (int col = 0; col < a.col; col++) x.value[col][0] = Utilities.DeterminantOBE(Utilities.replaceColumn(a, b, col)) / detA;
        return x;
    }
}
