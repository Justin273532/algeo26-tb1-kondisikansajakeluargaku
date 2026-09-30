package algeo.modules;

public class CubicSplineInterpolation {
    public void cubicSplineInterpolate() {
        // Input N
        System.out.println("Masukkan N: ");
        int N = Utilities.input.nextInt();

        // Membuat matriks data
        double[][] input_data = new double[N][2];

        // Input kumpulan data
        for (int i = 0; i < N; i++){
            System.out.println("Masukkan nilai x dan y (format input x y): ");
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
            
            koef.value[i][0] = yi;
            koef.value[i][1] = ((yi_plus_1 - yi)/h) - (h * (2.0 * M[i] + M[i + 1])/6.0);
            koef.value[i][2] = M[i] / 2.0;
            koef.value[i][3] = (M[i + 1] - M[i]) / (6.0 * h);

        }

        System.out.println("Nilai turunan kedua pada setiap knot:");
        for (int i = 0; i < N; i++){
            System.out.println("M" + i + ": " + M[i]);
        }

        System.out.println("Persamaan polinom kubik untuk setiap segmen:");
        for (int i = 0; i < N - 1; i++){
            double a = koef.value[i][0];
            double b = koef.value[i][1];
            double c = koef.value[i][2];
            double d = koef.value[i][3];
            System.out.printf("S_%d(x) = %.3f + %.3f(x - %.3f) + %.3f(x - %.3f)^2 + %.3f(x - %.3f)^3\n", i, a, b, input_data[i][0], c, input_data[i][0], d, input_data[i][0]);
            System.out.printf("Domain: [%.3f <= x <= %.3f]\n\n", input_data[i][0], input_data[i + 1][0]);
        }

        System.out.println("Masukkan nilai x untuk interpolasi: ");
        double x = Utilities.input.nextDouble();
        if (x >= input_data[0][0] && x <= input_data[N - 1][0]){
            int segmen = 0;
            for (int i = 0; i < N - 1; i++){
                if (x >= input_data[i][0] && x <= input_data[i + 1][0]);
                segmen = i;
                break;
            }

            double xi = input_data[segmen][0];
            double selisih = x - xi;
            double a = koef.value[segmen][0];
            double b = koef.value[segmen][1];
            double c = koef.value[segmen][2];
            double d = koef.value[segmen][3];
            double hasil = a + b*selisih + c*Math.pow(selisih, 2) + d*Math.pow(selisih, 3);
            System.out.println("Hasil interpolasi: " + hasil);
        } else {
            System.out.printf("Input berada di luar domain: [%.3f <= x <= %.3f]", input_data[0][0], input_data[N - 1][0]);
        }

    }
}