package algeo.modules;
public class Matrix {
    public int row;
    public int col;
    public double[][] value;

    public Matrix(int row, int col) {
        this.row = row;
        this.col = col;
        this.value = new double[row][col];
    }
}