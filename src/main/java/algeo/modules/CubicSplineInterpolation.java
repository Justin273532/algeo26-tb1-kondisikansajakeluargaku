package algeo.modules;


import algeo.modules.GaussJordanElim;
import algeo.modules.Matrix;
import algeo.modules.Utilities;

public class CubicSplineInterpolation {
    public void interpolate() {
        // Input N
        System.out.println("Masukkan N: ");
        int N = Utilities.input.nextInt();

        // Membuat matriks data
        double[][] input_data = new double[N][2];

        // Input kumpulan data
        for (int i = 0; i < N; i++){
            System.out.println("Masukkan nilai x dan y: ");
            input_data[i][0] = Utilities.input.nextDouble();
            input_data[i][1] = Utilities.input.nextDouble();
        }

        // Sorting input karena diperlukan data X berurutan
        for (int i = 0; i < N; i++){
            int idx_min = i;
            for (int j = i + 1; j < N; j++){
                if (input_data[j][0] < input_data[idx_min][0]){
                    idx_min = j;
                }
            }
            if (idx_min != i){
                double temp_x = input_data[i][0];
                input_data[i][0] = input_data[idx_min][0];
                input_data[idx_min][0] = temp_x;

                double temp_y = input_data[i][1];
                input_data[i][1] = input_data[idx_min][1];
                input_data[idx_min][1] = temp_y;
            }
        }

        double[] interval = new double[N - 1];
        for(int i = 0; i < N - 1; i++){
            interval[i] = input_data[i + 1][0] - input_data[i][0];
        }

        // Membentuk matriks tridiagonal
        Matrix tridiagonal = new Matrix(N - 2, N - 1);
        for(int i = 0; i < N - 2; i++){
            int j = i + 1;

            double h_kiri = interval[j - 1];
            double h_kanan = interval[j];
            double dy_kiri = (input_data[j][1] - input_data[j - 1][1]) / h_kiri;
            double dy_kanan = (input_data[j + 1][1] - input_data[j][1]) / h_kanan;
            double penyebut = h_kiri + h_kanan;
            double mu = h_kiri/penyebut;
            double lambda = 1.0 - mu;
            double d = (6.0 / penyebut) * (dy_kanan - dy_kiri);

            tridiagonal.value[i][i] = 2.0;
            if (i > 0){
                tridiagonal.value[i][i - 1] = mu;
            }

            if (i < N - 3){
                tridiagonal.value[i][i + 1] = lambda;
            }

            tridiagonal.value[i][N - 2] = d;
        }

        Matrix rref = GaussJordanElim.toReducedRowEchelonFormMatrix(tridiagonal);

        double[] M = new double[N];
        M[0] = 0.0;
        M[N - 1] = 0.0;
        for (int i = 0; i < N - 2; i++){
            M[i + 1] = rref.value[i][N - 2];
        }

        Matrix koef = Utilities.createMatrix(N - 1, 4);
        for (int i = 0; i < N - 1; i++){
            double h = interval[i];
            double yi = input_data[i][1];
            double yi_plus_1 = input_data[i + 1][1];
            
            double a = yi;
            double b = ((yi_plus_1 - yi)/h) - (h * (2.0 * M[i] + M[i + 1])/6.0);
            double c = M[i] / 2.0;
            double d = (M[i + 1] - M[i]) / (6.0 * h);

            koef.value[i][0] = a;
            koef.value[i][1] = b;
            koef.value[i][2] = c;
            koef.value[i][3] = d;
        }

    }
}