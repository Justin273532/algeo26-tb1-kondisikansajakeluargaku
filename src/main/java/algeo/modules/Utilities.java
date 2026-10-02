package algeo.modules;

public final class Utilities {
    public static final double EPS = 1e-10;

    private Utilities() { }

    public static Matrix createMatrix(int row, int col) { return new Matrix(row, col); }

    public static void showMatrix(Matrix a) {
        requireMatrix(a);
        System.out.print("[");
        for (int i = 0; i < a.row; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print("[");
            for (int j = 0; j < a.col; j++) {
                if (j > 0) System.out.print(" ");
                System.out.print(formatDouble(a.value[i][j]));
            }
            System.out.print("]");
            if (i + 1 < a.row) System.out.println();
        }
        System.out.print("]");
    }

    public static void setMatrixElement(Matrix a, int row, int col, double val) { a.value[row][col] = val; }
    public static double getMatrixElement(Matrix a, int row, int col) { return a.value[row][col]; }
    public static boolean isSquareMatrix(Matrix a) { return a != null && a.row == a.col; }
    public static boolean isZero(double value) { return Math.abs(value) <= EPS; }
    public static String formatDouble(double value) { return String.format(java.util.Locale.US, "%.3f", value); }

    public static boolean isMatrixIdentity(Matrix a) {
        if (!isSquareMatrix(a)) return false;
        for (int i = 0; i < a.row; i++) for (int j = 0; j < a.col; j++) {
            if (!isZero(a.value[i][j] - (i == j ? 1.0 : 0.0))) return false;
        }
        return true;
    }

    public static boolean isValidMatrixAddSub(Matrix a, Matrix b) {
        return a != null && b != null && a.row == b.row && a.col == b.col;
    }
    public static boolean isValidMatrixMul(Matrix a, Matrix b) {
        return a != null && b != null && a.col == b.row;
    }

    public static Matrix transposeMatrix(Matrix a) {
        requireMatrix(a);
        Matrix out = new Matrix(a.col, a.row);
        for (int i = 0; i < a.row; i++) for (int j = 0; j < a.col; j++) out.value[j][i] = a.value[i][j];
        return out;
    }

    public static Matrix matrixMultiplication(Matrix a, Matrix b) {
        requireMatrix(a); requireMatrix(b);
        if (!isValidMatrixMul(a, b)) throw new IllegalArgumentException("Dimensi matriks tidak sesuai untuk perkalian.");
        Matrix out = new Matrix(a.row, b.col);
        for (int i = 0; i < a.row; i++) for (int j = 0; j < b.col; j++) {
            double sum = 0;
            for (int k = 0; k < a.col; k++) sum += a.value[i][k] * b.value[k][j];
            out.value[i][j] = sum;
        }
        return out;
    }

    public static Matrix matrixAddition(Matrix a, Matrix b) {
        requireSameSize(a, b, "dijumlahkan");
        Matrix out = new Matrix(a.row, a.col);
        for (int i = 0; i < a.row; i++) for (int j = 0; j < a.col; j++) out.value[i][j] = a.value[i][j] + b.value[i][j];
        return out;
    }

    public static Matrix matrixSubtraction(Matrix a, Matrix b) {
        requireSameSize(a, b, "dikurangkan");
        Matrix out = new Matrix(a.row, a.col);
        for (int i = 0; i < a.row; i++) for (int j = 0; j < a.col; j++) out.value[i][j] = a.value[i][j] - b.value[i][j];
        return out;
    }

    public static Matrix copyMatrix(Matrix a) {
        requireMatrix(a);
        Matrix out = new Matrix(a.row, a.col);
        for (int i = 0; i < a.row; i++) System.arraycopy(a.value[i], 0, out.value[i], 0, a.col);
        return out;
    }

    public static Matrix createMatrixIdentity(int order) {
        if (order < 0) throw new IllegalArgumentException("Ukuran matriks identitas tidak boleh negatif.");
        Matrix out = new Matrix(order, order);
        for (int i = 0; i < order; i++) out.value[i][i] = 1.0;
        return out;
    }

    public static Matrix replaceColumn(Matrix a, Matrix b, int colidx) {
        requireMatrix(a); requireMatrix(b);
        if (a == null || b == null || b.row != a.row || b.col != 1 || colidx < 0 || colidx >= a.col)
            throw new IllegalArgumentException("Dimensi tidak sesuai untuk penggantian kolom.");
        Matrix out = copyMatrix(a);
        for (int i = 0; i < a.row; i++) out.value[i][colidx] = b.value[i][0];
        return out;
    }

    public static Matrix subMatrix(Matrix a, int excludedRow, int excludedCol) {
        requireMatrix(a);
        if (excludedRow < 0 || excludedRow >= a.row || excludedCol < 0 || excludedCol >= a.col)
            throw new IndexOutOfBoundsException("Indeks submatriks di luar batas.");
        Matrix out = new Matrix(a.row - 1, a.col - 1);
        for (int i = 0, r = 0; i < a.row; i++) {
            if (i == excludedRow) continue;
            for (int j = 0, c = 0; j < a.col; j++) {
                if (j == excludedCol) continue;
                out.value[r][c++] = a.value[i][j];
            }
            r++;
        }
        return out;
    }

    public static double Determinant(Matrix a) {
        return Determinant(a, null);
    }

    /** Computes a determinant by cofactor expansion and optionally records each minor calculation. */
    public static double Determinant(Matrix a, CalculationTrace trace) {
        requireSquareNonEmpty(a, "menghitung determinan");
        return determinantByCofactor(a, trace);
    }

    private static double determinantByCofactor(Matrix a, CalculationTrace trace) {
        if (a.row == 1) {
            if (trace != null) trace.add("Determinan matriks 1x1 = " + formatDouble(a.value[0][0]) + ".");
            return a.value[0][0];
        }
        if (trace != null) {
            trace.add("Ekspansi kofaktor pada baris pertama matriks " + a.row + "x" + a.col + ":");
            trace.addMatrix("Matriks saat ini:", a);
        }
        double det = 0;
        for (int j = 0; j < a.col; j++) {
            Matrix minor = subMatrix(a, 0, j);
            double minorDet = determinantByCofactor(minor, trace);
            double sign = (j % 2 == 0) ? 1.0 : -1.0;
            double term = sign * a.value[0][j] * minorDet;
            if (trace != null) {
                trace.add("Elemen a[1," + (j + 1) + "] = " + formatDouble(a.value[0][j])
                    + ", tanda " + (sign > 0 ? "+" : "-") + ", determinan minor = "
                    + formatDouble(minorDet) + ", suku = " + formatDouble(term) + ".");
            }
            det += term;
        }
        if (trace != null) trace.add("Jumlah suku ekspansi = " + formatDouble(det) + ".");
        return det;
    }

    public static int power(int x, int y) { return (int) Math.round(Math.pow(x, y)); }
    public static double power(double x, int y) { return Math.pow(x, y); }

    public static Matrix Cofactor(Matrix a) {
        return Cofactor(a, null);
    }

    public static Matrix Cofactor(Matrix a, CalculationTrace trace) {
        requireSquareNonEmpty(a, "menghitung matriks kofaktor");
        Matrix out = new Matrix(a.row, a.col);
        if (a.row == 1) {
            out.value[0][0] = 1.0;
            if (trace != null) trace.add("Untuk matriks 1x1, matriks kofaktor adalah [1].");
            return out;
        }
        for (int i = 0; i < a.row; i++) for (int j = 0; j < a.col; j++) {
            Matrix minor = subMatrix(a, i, j);
            double minorDet = determinantByCofactor(minor, trace);
            double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
            out.value[i][j] = sign * minorDet;
            if (trace != null) trace.add("C[" + (i + 1) + "," + (j + 1) + "] = "
                + (sign > 0 ? "+" : "-") + " det(minor) = " + formatDouble(out.value[i][j]) + ".");
        }
        if (trace != null) trace.addMatrix("Matriks kofaktor C:", out);
        return out;
    }

    public static Matrix ScalarMatrixMul(Matrix a, double scalar) {
        requireMatrix(a);
        Matrix out = copyMatrix(a);
        for (int i = 0; i < out.row; i++) for (int j = 0; j < out.col; j++) out.value[i][j] *= scalar;
        return out;
    }

    public static Matrix InverseMatrix(Matrix a) {
        return InverseMatrix(a, null);
    }

    /** Computes the adjugate/cofactor inverse and optionally records its construction. */
    public static Matrix InverseMatrix(Matrix a, CalculationTrace trace) {
        requireSquareNonEmpty(a, "menghitung invers");
        if (trace != null) trace.add("Metode invers adjoin: A^-1 = transpose(C) / det(A).");
        double det = Determinant(a, trace);
        if (trace != null) trace.add("det(A) = " + formatDouble(det) + ".");
        if (isZero(det)) throw new IllegalArgumentException("Matriks tidak memiliki invers karena determinannya nol.");
        Matrix cofactor = Cofactor(a, trace);
        Matrix adjugate = transposeMatrix(cofactor);
        if (trace != null) trace.addMatrix("Transpose matriks kofaktor (adjugat):", adjugate);
        Matrix inverse = ScalarMatrixMul(adjugate, 1.0 / det);
        if (trace != null) {
            trace.add("Kalikan adjugat dengan 1/det(A) = " + formatDouble(1.0 / det) + ".");
            trace.addMatrix("Matriks invers A^-1:", inverse);
        }
        return inverse;
    }

    public static double DeterminantOBE(Matrix a) {
        return DeterminantOBE(a, null);
    }

    /** Computes a determinant by elimination and optionally records each row operation. */
    public static double DeterminantOBE(Matrix a, CalculationTrace trace) {
        requireSquareNonEmpty(a, "menghitung determinan");
        Matrix work = copyMatrix(a);
        double det = 1.0;
        int swaps = 0;
        if (trace != null) {
            trace.add("Eliminasi OBE: operasi penjumlahan kelipatan baris tidak mengubah determinan; setiap pertukaran baris membalik tanda.");
            trace.addMatrix("Matriks awal:", work);
        }
        for (int col = 0; col < work.col; col++) {
            int pivot = OBE.findPivotRow(work, col, col);
            if (isZero(work.value[pivot][col])) {
                if (trace != null) trace.add("Pivot pada kolom " + (col + 1) + " bernilai nol (dalam toleransi); det(A) = 0.000.");
                return 0.0;
            }
            if (pivot != col) {
                OBE.swapRow(work, pivot, col); det = -det; swaps++;
                if (trace != null) {
                    trace.add("Tukar baris " + (col + 1) + " dengan baris " + (pivot + 1) + "; tanda determinan berubah.");
                    trace.addMatrix("Matriks setelah pertukaran:", work);
                }
            }
            double pivotValue = work.value[col][col];
            det *= pivotValue;
            if (trace != null) trace.add("Pivot diagonal a[" + (col + 1) + "," + (col + 1) + "] = " + formatDouble(pivotValue) + ".");
            for (int r = col + 1; r < work.row; r++) {
                double factor = work.value[r][col] / pivotValue;
                if (!isZero(factor) && trace != null) trace.add("R" + (r + 1) + " <- R" + (r + 1) + " - (" + formatDouble(factor) + ")R" + (col + 1) + ".");
                for (int j = col + 1; j < work.col; j++) work.value[r][j] -= factor * work.value[col][j];
                work.value[r][col] = 0.0;
            }
            if (trace != null) trace.addMatrix("Matriks setelah eliminasi kolom " + (col + 1) + ":", work);
        }
        if (isZero(det)) det = 0.0;
        if (trace != null) trace.add("Hasil akhir: det(A) = " + (swaps % 2 == 0 ? "+" : "-") + " hasil kali elemen diagonal = " + formatDouble(det) + ".");
        return det;
    }

    public static Matrix InverseMatrixAugmented(Matrix a) {
        return InverseMatrixAugmented(a, null);
    }

    /** Computes an inverse by Gauss-Jordan elimination and optionally records each row operation. */
    public static Matrix InverseMatrixAugmented(Matrix a, CalculationTrace trace) {
        requireSquareNonEmpty(a, "menghitung invers");
        int n = a.row;
        Matrix left = copyMatrix(a), right = createMatrixIdentity(n);
        if (trace != null) {
            trace.add("Bentuk matriks augmented [A|I], lalu lakukan Gauss-Jordan sampai ruas kiri menjadi I.");
            trace.addMatrix("Matriks augmented awal [A|I]:", augment(left, right));
        }
        for (int col = 0; col < n; col++) {
            int pivot = OBE.findPivotRow(left, col, col);
            if (isZero(left.value[pivot][col])) {
                if (trace != null) trace.add("Tidak ditemukan pivot pada kolom " + (col + 1) + "; matriks singular dan tidak memiliki invers.");
                throw new IllegalArgumentException("Matriks tidak memiliki invers karena singular.");
            }
            if (pivot != col) {
                OBE.swapRow(left, col, pivot); OBE.swapRow(right, col, pivot);
                if (trace != null) {
                    trace.add("Tukar R" + (col + 1) + " dengan R" + (pivot + 1) + ".");
                    trace.addMatrix("Matriks augmented:", augment(left, right));
                }
            }
            double p = left.value[col][col];
            for (int j = 0; j < n; j++) { left.value[col][j] /= p; right.value[col][j] /= p; }
            if (trace != null) {
                trace.add("Skalakan R" + (col + 1) + " dengan 1/" + formatDouble(p) + " agar pivot menjadi 1.");
                trace.addMatrix("Matriks augmented:", augment(left, right));
            }
            for (int r = 0; r < n; r++) if (r != col) {
                double factor = left.value[r][col];
                if (!isZero(factor) && trace != null) trace.add("R" + (r + 1) + " <- R" + (r + 1) + " - (" + formatDouble(factor) + ")R" + (col + 1) + ".");
                for (int j = 0; j < n; j++) { left.value[r][j] -= factor * left.value[col][j]; right.value[r][j] -= factor * right.value[col][j]; }
            }
            if (trace != null) trace.addMatrix("Matriks augmented setelah eliminasi kolom " + (col + 1) + ":", augment(left, right));
        }
        if (trace != null) trace.addMatrix("Karena [I|A^-1] sudah terbentuk, inversnya adalah ruas kanan:", right);
        return right;
    }

    private static Matrix augment(Matrix left, Matrix right) {
        Matrix combined = new Matrix(left.row, left.col + right.col);
        for (int i = 0; i < left.row; i++) {
            System.arraycopy(left.value[i], 0, combined.value[i], 0, left.col);
            System.arraycopy(right.value[i], 0, combined.value[i], left.col, right.col);
        }
        return combined;
    }

    public static void requireFiniteMatrix(Matrix a) { requireMatrix(a); }
    private static void requireMatrix(Matrix a) {
        if (a == null) throw new IllegalArgumentException("Matriks tidak boleh null.");
        if (a.row < 0 || a.col < 0 || a.value == null || a.value.length != a.row) throw new IllegalArgumentException("Struktur matriks tidak valid.");
        for (int i = 0; i < a.row; i++) {
            if (a.value[i] == null || a.value[i].length != a.col) throw new IllegalArgumentException("Dimensi matriks tidak konsisten dengan datanya.");
            for (int j = 0; j < a.col; j++) if (!Double.isFinite(a.value[i][j])) throw new IllegalArgumentException("Elemen matriks harus bernilai finite.");
        }
    }
    private static void requireSquareNonEmpty(Matrix a, String operation) {
        requireMatrix(a);
        if (a.row == 0 || a.row != a.col) throw new IllegalArgumentException("Matriks harus persegi dan tidak kosong untuk " + operation + ".");
    }
    private static void requireSameSize(Matrix a, Matrix b, String verb) {
        requireMatrix(a); requireMatrix(b);
        if (!isValidMatrixAddSub(a, b)) throw new IllegalArgumentException("Dimensi matriks tidak sesuai untuk " + verb + ".");
    }
}
