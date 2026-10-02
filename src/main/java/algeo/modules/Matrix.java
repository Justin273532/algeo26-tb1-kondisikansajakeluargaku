package algeo.modules;

/** A mutable rectangular matrix backed by a two-dimensional double array. */
public class Matrix {
    public int row;
    public int col;
    public double[][] value;

    public Matrix(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Dimensi matriks tidak boleh negatif.");
        }
        this.row = row;
        this.col = col;
        this.value = new double[row][col];
    }
}
