package algeo.modules;

public class SplineRegression {
    public void calculate() {

        System.out.println("Masukkan Jumlah Data:");
        int N = Utilities.input.nextInt(); // Baca Input
                
        System.out.println("Masukkan K:");
        int K = Utilities.input.nextInt(); // Baca Input

        double[] array_Xi = new double[K];
        for(int i = 0; i < K; i++){
            System.out.println("Masukkan Knot Xi-" + (i + 1) + ":");
            array_Xi[i] = Utilities.input.nextDouble();
        }

        double[] x = new double [N];
        Matrix Y = Utilities.createMatrix(N ,1); // Menyediakan matriks

        System.out.println("\nMasukkan data titik (format: x y)");
        for(int i = 0; i < N; i++) {
            System.out.println("Titik " + (i + 1) + ":");
            x[i] = Utilities.input.nextDouble();
            Y.value[i][0] = Utilities.input.nextDouble();
        }

        int M = 4 + K;
        Matrix matriksjadi = Utilities.createMatrix(N, M);
        for(int i = 0; i < M; i++){
            if (i <= 3) {
                for(int j = 0; j < N; j++){
                    matriksjadi.value[j][i] = Math.pow(x[j], i);
                }
            }
            else {
                for(int j = 0; j < N; j++){
                    if (x[j] > array_Xi[i - 4]){
                        matriksjadi.value[j][i] = Math.pow(x[j] - array_Xi[i - 4], 3);
                    } else {
                        matriksjadi.value[j][i] = 0;
                    }
                }
            }
        }

        // Mentranspose matriks
        Matrix matriksTranspose = Utilities.transposeMatrix(matriksjadi);

        // Perkalian matriks transpose dengan matriks awal membentuk matriks 4 + K x m
        Matrix matriksKali = Utilities.matrixMultiplication(matriksTranspose, matriksjadi);

        // Perkalian matriks transpose dengan nilai Y
        Matrix matriksVektor = Utilities.matrixMultiplication(matriksTranspose, Y);

        // Membentuk matriks augmented dari matriksKali dan matriksVektor
        Matrix augmented = new Matrix(M, M + 1);

        for (int i = 0; i < M; i++){
            for (int j = 0; j < M; j++){
                augmented.value[i][j] = matriksKali.value[i][j];
            }
            augmented.value[i][M] = matriksVektor.value[i][0];
        }

        // Eliminasi gauss-jordan
        Matrix hasil = GaussJordanElim.toReducedRowEchelonFormMatrix(augmented);

        System.out.print("[");
        for (int i = 0; i < 4 + K; i++){
            if (i != 4 + K - 1 ){
                System.out.printf("%.3f ", hasil.value[i][4 + K]);
            }
            else {
                System.out.printf("%.3f", hasil.value[i][4 + K]);
            }
        }
        System.out.print("]");
    
    }

    public static void main(String[] args){
        SplineRegression sr = new SplineRegression();
        sr.calculate();
    }
}