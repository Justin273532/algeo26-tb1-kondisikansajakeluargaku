package algeo.modules;

public final class OBE {
    private OBE() { }

    public static void swapRow(Matrix a, int row1, int row2) {
        if (a == null) throw new IllegalArgumentException("Matriks tidak boleh null.");
        if (row1 < 0 || row1 >= a.row || row2 < 0 || row2 >= a.row) throw new IndexOutOfBoundsException("Indeks baris di luar batas.");
        double[] temp = a.value[row1]; a.value[row1] = a.value[row2]; a.value[row2] = temp;
    }

    public static void multiplyRow(Matrix a, double scalar, int row) {
        if (a == null || row < 0 || row >= a.row) throw new IllegalArgumentException("Baris tidak valid.");
        for (int j = 0; j < a.col; j++) a.value[row][j] *= scalar;
    }

    public static void addMultipleRow(Matrix a, double scalar, int targetRow, int sourceRow) {
        if (a == null || targetRow < 0 || targetRow >= a.row || sourceRow < 0 || sourceRow >= a.row)
            throw new IllegalArgumentException("Baris tidak valid.");
        for (int j = 0; j < a.col; j++) a.value[targetRow][j] += scalar * a.value[sourceRow][j];
    }

    /** Returns the row with the largest absolute candidate pivot at or below startrowidx. */
    public static int findPivotRow(Matrix a, int startrowidx, int colidx) {
        if (a == null || startrowidx < 0 || startrowidx >= a.row || colidx < 0 || colidx >= a.col)
            throw new IllegalArgumentException("Posisi pivot tidak valid.");
        int pivot = startrowidx;
        for (int i = startrowidx + 1; i < a.row; i++)
            if (Math.abs(a.value[i][colidx]) > Math.abs(a.value[pivot][colidx])) pivot = i;
        return pivot;
    }
}
