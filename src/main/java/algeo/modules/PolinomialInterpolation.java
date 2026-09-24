package algeo.modules;

public class PolinomialInterpolation {

    public static Matrix createAugmentedMatrix(Matrix A, Matrix b) {
        if(A == null || b == null) {
            throw new IllegalArgumentException("Matrix A dan b tidak boleh kosong.");
        }
        if(A.row != b.row || b.col != 1) {
            throw new IllegalArgumentException("Jumlah baris matrix A harus sama dengan jumlah baris matrix b dan matrix b harus punya tepat 1 kolom.");
        }
        Matrix Ab = Utilities.createMatrix(A.row, A.col + b.col);
        for(int i=0; i<A.row; i++) {
            for(int j=0; j<A.col+b.col; j++) {
                if(j < A.col) {
                    Ab.value[i][j] = A.value[i][j];
                }
                else {
                    Ab.value[i][j] = b.value[i][j-A.col];
                }
            }
        }
        return Ab;
    }
    public static Matrix Interpolate(Matrix Point) {
        if(Point == null) {
            throw new IllegalArgumentException("Matrix tidak boleh kosong.");
        }
        if(Point.col != 2) {
            throw new IllegalArgumentException("Matrix harus memiliki tepat 2 kolom untuk interpolasi polinomial.");
        }
        if(Point.row < 2) {
            throw new IllegalArgumentException("Matrix harus memiliki setidaknya 2 baris untuk interpolasi polinomial.");
        }
        Matrix A = Utilities.createMatrix(Point.row,Point.row);
        Matrix b = Utilities.createMatrix(Point.row, 1);
        for(int i=0; i<Point.row; i++) {
            double x = Point.value[i][0];
            double power = 1;
            for(int j=0; j<Point.row; j++) {
                A.value[i][j] = power;
                power *= x;
            }
            b.value[i][0] = Point.value[i][1];
        }
        Matrix Ab = createAugmentedMatrix(A, b);

        GaussElim.Result result = GaussElim.solveByGaussElim(Ab);
        if(result.type != GaussElim.SolutionType.UNIQUE_SOLUTION) {
            throw new IllegalArgumentException("Interpolasi tidak dapat dilakukan.");
        }
        return result.solution;
    }
}
