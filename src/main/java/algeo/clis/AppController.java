package algeo.clis;

import algeo.modules.CramerRule;
import algeo.modules.CalculationTrace;
import algeo.modules.CubicSplineInterpolation;
import algeo.modules.GaussElim;
import algeo.modules.GaussJordanElim;
import algeo.modules.ImageRestoration;
import algeo.modules.InverseMethod;
import algeo.modules.Matrix;
import algeo.modules.PolinomialInterpolation;
import algeo.modules.SplineRegression;
import algeo.modules.Utilities;
import java.io.IOException;
import java.nio.file.Path;

/** Coordinates menus, input acquisition, algorithm calls, and result presentation. */
public class AppController {
    private final InputHandler inputHandler;

    public AppController() { this(new InputHandler()); }
    public AppController(InputHandler inputHandler) { this.inputHandler = inputHandler; }

    public void run() {
        boolean running = true;
        while (running) {
            Options.mainMenu();
            int choice = inputHandler.inputInt();
            if (inputHandler.inputEnded) break;
            if (inputHandler.inputError_Int) { inputHandler.inputError_Int = false; System.out.println("Pilihan harus berupa angka."); continue; }
            try {
                switch (choice) {
                    case 1 -> runSPL();
                    case 2 -> runDeterminan();
                    case 3 -> runMatriksInvers();
                    case 4 -> runPolynomialInterpolation();
                    case 5 -> runNaturalSpline();
                    case 6 -> runSplineRegression();
                    case 7 -> runImageRestoration();
                    case 8 -> running = false;
                    default -> System.out.println("Pilihan tidak valid.");
                }
            } catch (IOException | IllegalArgumentException e) {
                if (!inputHandler.inputEnded) System.out.println("Operasi gagal: " + e.getMessage());
            }
            if (inputHandler.inputEnded) running = false;
        }
        System.out.println("Program selesai.");
    }

    public void runSPL() throws IOException {
        boolean back = false;
        while (!back) {
            Options.menu_SPL();
            int choice = readChoice();
            switch (choice) {
                case 1 -> runSubmenuOperation(() -> {
                    SPLInput system = readSPLInput();
                    present(formatSolution(GaussElim.solveByGaussElim(system.augmented())));
                });
                case 2 -> runSubmenuOperation(() -> {
                    SPLInput system = readSPLInput();
                    present(formatSolution(GaussJordanElim.solveByGaussJordan(system.augmented())));
                });
                case 3 -> runSubmenuOperation(() -> {
                    SPLInput system = readSPLInput();
                    present(MatrixIO.formatMatrix(InverseMethod.inverseMethod(system.a(), system.b())));
                });
                case 4 -> runSubmenuOperation(this::runSPL_crammer);
                case 5 -> back = true;
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public void runDeterminan() throws IOException {
        boolean back = false;
        while (!back) {
            Options.menu_Determinan();
            int choice = readChoice();
            switch (choice) {
                case 1 -> runSubmenuOperation(() -> runDeterminant(true));
                case 2 -> runSubmenuOperation(() -> runDeterminant(false));
                case 3 -> back = true;
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public void runMatriksInvers() throws IOException {
        boolean back = false;
        while (!back) {
            Options.menu_MatriksInvers();
            int choice = readChoice();
            switch (choice) {
                case 1 -> runSubmenuOperation(() -> runInverse(true));
                case 2 -> runSubmenuOperation(() -> runInverse(false));
                case 3 -> back = true;
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void runDeterminant(boolean useCofactor) throws IOException {
        Matrix matrix = readMatrix("matriks persegi");
        CalculationTrace trace = new CalculationTrace();
        try {
            double determinant = useCofactor
                ? Utilities.Determinant(matrix, trace)
                : Utilities.DeterminantOBE(matrix, trace);
            String method = useCofactor ? "Ekspansi kofaktor" : "Eliminasi OBE";
            present("Langkah perhitungan determinan (" + method + "):\n" + trace
                + "\n\nDeterminan = " + Utilities.formatDouble(determinant));
        } catch (IllegalArgumentException e) {
            present("Langkah perhitungan:\n" + trace + "\n\nPerhitungan gagal: " + e.getMessage());
        }
    }

    private void runInverse(boolean useCofactor) throws IOException {
        Matrix matrix = readMatrix("matriks persegi");
        CalculationTrace trace = new CalculationTrace();
        try {
            Matrix inverse = useCofactor
                ? Utilities.InverseMatrix(matrix, trace)
                : Utilities.InverseMatrixAugmented(matrix, trace);
            String method = useCofactor ? "Adjoin/kofaktor" : "Gauss-Jordan";
            present("Langkah perhitungan invers (" + method + "):\n" + trace
                + "\n\nMatriks invers:\n" + MatrixIO.formatMatrix(inverse));
        } catch (IllegalArgumentException e) {
            present("Langkah perhitungan invers:\n" + trace + "\n\nPerhitungan gagal: " + e.getMessage());
        }
    }

    public void runSPL_crammer() throws IOException {
        SPLInput system = readSPLInput();
        present(MatrixIO.formatMatrix(CramerRule.cramerRule(system.a(), system.b())));
    }

    private void runPolynomialInterpolation() throws IOException {
        Matrix points = readMatrix("titik interpolasi (kolom x dan y)");
        Matrix coefficients = PolinomialInterpolation.polinomialInterpolate(points);
        StringBuilder out = new StringBuilder("Koefisien (pangkat menaik):\n").append(MatrixIO.formatMatrix(coefficients));
        out.append("\nP(x) = ").append(formatPolynomial(coefficients));
        present(out.toString());
        System.out.print("Evaluasi polinomial pada x (kosongkan untuk selesai): ");
        String text = inputHandler.inputLine("");
        if (text != null && !text.trim().isEmpty()) {
            double x = parseDouble(text);
            present("P(" + Utilities.formatDouble(x) + ") = " + Utilities.formatDouble(PolinomialInterpolation.evaluate(coefficients, x)));
        }
    }

    private void runNaturalSpline() throws IOException {
        CubicSplineInterpolation.Spline spline = CubicSplineInterpolation.cubicSplineInterpolate(readMatrix("titik spline (kolom x dan y)"));
        StringBuilder out = new StringBuilder("Koefisien tiap interval untuk a + b*t + c*t^2 + d*t^3, t=x-x_i:\n");
        for (int i = 0; i < spline.coefficients.row; i++) {
            Matrix row = new Matrix(1, 4);
            System.arraycopy(spline.coefficients.value[i], 0, row.value[0], 0, 4);
            out.append("Interval [").append(Utilities.formatDouble(spline.x[i])).append(", ").append(Utilities.formatDouble(spline.x[i + 1])).append("]: ")
                .append(MatrixIO.formatMatrix(row)).append('\n');
        }
        present(out.toString().trim());
        System.out.print("Evaluasi spline pada x (kosongkan untuk selesai): ");
        String text = inputHandler.inputLine("");
        if (text != null && !text.trim().isEmpty()) {
            double x = parseDouble(text);
            present("S(" + Utilities.formatDouble(x) + ") = " + Utilities.formatDouble(spline.evaluate(x)));
        }
    }

    private void runSplineRegression() throws IOException {
        Matrix points = readMatrix("data regresi (kolom x dan y)");
        System.out.print("Jumlah knot: ");
        int count = inputHandler.inputInt();
        if (inputHandler.inputError_Int || count < 0) { inputHandler.inputError_Int = false; throw new IllegalArgumentException("Jumlah knot tidak valid."); }
        double[] knots = new double[count];
        for (int i = 0; i < count; i++) {
            System.out.print("Knot " + (i + 1) + ": ");
            knots[i] = readDouble();
        }
        Matrix coefficients = SplineRegression.fit(points, knots);
        present("Knot: " + formatDoubles(knots) + "\nKoefisien regresi (pangkat menaik, lalu basis knot):\n" + MatrixIO.formatMatrix(coefficients));
        System.out.print("Evaluasi model pada x (kosongkan untuk selesai): ");
        String text = inputHandler.inputLine("");
        if (text != null && !text.trim().isEmpty()) {
            double x = parseDouble(text);
            present("f(" + Utilities.formatDouble(x) + ") = " + Utilities.formatDouble(SplineRegression.evaluate(x, coefficients, knots)));
        }
    }

    private void runImageRestoration() throws IOException {
        String image = inputHandler.inputLine("Path gambar asli: ");
        String mask = inputHandler.inputLine("Path gambar mask: ");
        String output = inputHandler.inputLine("Path output PNG: ");
        if (image == null || mask == null || output == null) throw new IllegalArgumentException("Input path tidak lengkap.");
        ImageRestoration.restoreImage(image.trim(), mask.trim(), output.trim());
        System.out.println("Restorasi disimpan ke " + Path.of(output.trim()).toAbsolutePath());
    }

    private SPLInput readSPLInput() throws IOException {
        System.out.println("Format input SPL: 1. Matriks A dan vektor b terpisah  2. Matriks augmented [A|b]");
        int format = readChoice();
        if (format == 1) {
            Matrix a = readMatrix("matriks koefisien A");
            Matrix b = readMatrix("vektor konstanta b (satu kolom)");
            Matrix augmented = PolinomialInterpolation.createAugmentedMatrix(a, b);
            return new SPLInput(a, b, augmented);
        }
        if (format == 2) {
            Matrix augmented = readMatrix("matriks augmented [A|b] (kolom terakhir adalah b)");
            Matrix[] parts = InverseMethod.splitAugmentMatrix(augmented);
            return new SPLInput(parts[0], parts[1], augmented);
        }
        throw new IllegalArgumentException("Format input SPL tidak valid.");
    }

    private record SPLInput(Matrix a, Matrix b, Matrix augmented) {}

    private Matrix readMatrix(String purpose) throws IOException {
        System.out.println("Input " + purpose + ": 1. Keyboard  2. File .txt");
        int source = readChoice();
        if (source == 1) {
            Matrix matrix = inputHandler.inputMatrix();
            if (matrix == null) throw new IllegalArgumentException("Input matriks tidak valid.");
            return matrix;
        }
        if (source == 2) return readMatrixFile(purpose);
        throw new IllegalArgumentException("Sumber input tidak valid.");
    }

    private Matrix readMatrixFile(String purpose) throws IOException {
        String prompt = "Path file .txt untuk " + purpose
            + " (relatif dari folder proyek, contoh: test/input.txt; absolut: C:/Users/Nama/Desktop/input.txt). Path dibaca satu baris; spasi diperbolehkan.\n"
            + "Isi file: satu baris per baris matriks, elemen dipisahkan spasi. Contoh matriks:\n"
            + "1 2\n3 4\nContoh vektor:\n1\n2\nPath file: ";
        String file = inputHandler.inputLine(prompt);
        if (file == null || file.trim().isEmpty()) throw new IllegalArgumentException("Path file tidak boleh kosong.");
        return MatrixIO.readMatrix(Path.of(file.trim()));
    }

    private void runSubmenuOperation(MenuOperation operation) {
        try {
            operation.run();
        } catch (IOException | IllegalArgumentException e) {
            if (!inputHandler.inputEnded) System.out.println("Operasi gagal: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface MenuOperation {
        void run() throws IOException;
    }

    private int readChoice() {
        int choice = inputHandler.inputInt();
        if (inputHandler.inputEnded) throw new IllegalArgumentException("Input berakhir.");
        if (inputHandler.inputError_Int) { inputHandler.inputError_Int = false; return -1; }
        return choice;
    }

    private void present(String result) {
        System.out.println(result);
        System.out.print("Simpan hasil ke file teks? (1=ya, 2=tidak): ");
        int choice = readChoice();
        if (choice == 1) {
            String file = inputHandler.inputLine("Path output: ");
            if (file == null || file.trim().isEmpty()) { System.out.println("Path output kosong; hasil tidak disimpan."); return; }
            try { MatrixIO.writeText(Path.of(file.trim()), result + System.lineSeparator()); System.out.println("Hasil disimpan."); }
            catch (IOException e) { System.out.println("Gagal menyimpan hasil: " + e.getMessage()); }
        }
    }

    private static String formatSolution(GaussElim.Result result) {
        return switch (result.type) {
            case UNIQUE_SOLUTION -> "Solusi unik (x1..xn):\n" + MatrixIO.formatMatrix(result.solution);
            case NO_SOLUTION -> "Sistem tidak memiliki solusi.";
            case INFINITE_SOLUTION -> {
                StringBuilder out = new StringBuilder("Sistem memiliki tak hingga solusi:\n");
                for (int i = 0; i < result.expression.length; i++) out.append("x").append(i + 1).append(" = ").append(result.expression[i]).append('\n');
                yield out.toString().trim();
            }
        };
    }

    private static String formatDoubles(double[] values) {
        StringBuilder out = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) out.append(", ");
            out.append(Utilities.formatDouble(values[i]));
        }
        return out.append(']').toString();
    }
    private static String formatPolynomial(Matrix coefficients) {
        StringBuilder out = new StringBuilder();
        for (int i = coefficients.row - 1; i >= 0; i--) {
            double c = coefficients.value[i][0];
            if (Utilities.isZero(c)) continue;
            if (out.length() > 0) out.append(c < 0 ? " - " : " + ");
            else if (c < 0) out.append('-');
            out.append(GaussElim.formatNumber(Math.abs(c)));
            if (i > 0) out.append("*x");
            if (i > 1) out.append('^').append(i);
        }
        return out.length() == 0 ? "0" : out.toString();
    }

    private double readDouble() {
        double value = inputHandler.inputDouble();
        if (inputHandler.inputError_Double) { inputHandler.inputError_Double = false; throw new IllegalArgumentException("Nilai harus berupa bilangan real."); }
        return value;
    }

    private static double parseDouble(String text) {
        try { double value = Double.parseDouble(text.trim().replace(',', '.')); if (!Double.isFinite(value)) throw new NumberFormatException(); return value; }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Nilai x tidak valid."); }
    }
}
