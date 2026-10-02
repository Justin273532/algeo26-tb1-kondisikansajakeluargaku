package algeo.clis;
import algeo.modules.CubicSplineInterpolation;
import algeo.modules.ImageRestoration;
import algeo.modules.Matrix;
import algeo.modules.PolinomialInterpolation;
import algeo.modules.SplineRegression;

public class AppController {
    private final InputHandler inputHandler;
    

    public AppController() {
        this.inputHandler = new InputHandler();
    }

    public void run() {
        boolean running = true;
        while (running) {
            Options.mainMenu();
            int choice = inputHandler.inputInt();
            if (inputHandler.inputError_Int) {
                inputHandler.inputError_Int = false;
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                continue;
            }
            switch (choice) {
                case 1:
                    runSPL();
                    break;
                case 2:
                    runDeterminan();
                    break;
                case 3:
                    runMatriksInvers();
                    break;
                case 4:
                    Matrix A = inputHandler.inputMatrix();
                    PolinomialInterpolation.polinomialInterpolate(A);
                    break;
                case 5:
                    CubicSplineInterpolation.cubicSplineInterpolate();
                    break;
                case 6:
                    SplineRegression.calculate();
                    break;
                case 7:
                    ImageRestoration.restoreImage();
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public void runSPL() {
        boolean back = false;
        while (!back) {
            Options.menu_SPL();
            int choice = inputHandler.inputInt();
            if (inputHandler.inputError_Int) {
                inputHandler.inputError_Int = false;
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("Fitur Eliminasi Gauss belum tersedia.");
                    break;
                case 2:
                    System.out.println("Fitur Eliminasi Gauss-Jordan belum tersedia.");
                    break;
                case 3:
                    System.out.println("Fitur Matriks Balikan belum tersedia.");
                    break;
                case 4:
                    System.out.println("Fitur Kaidah Cramer belum tersedia.");
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public void runDeterminan() {
        boolean back = false;
        while (!back) {
            Options.menu_Determinan();
            int choice = inputHandler.inputInt();
            if (inputHandler.inputError_Int) {
                inputHandler.inputError_Int = false;
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("Fitur Determinan Kofaktor belum tersedia.");
                    break;
                case 2:
                    System.out.println("Fitur Determinan OBE belum tersedia.");
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public void runMatriksInvers() {
        boolean back = false;
        while (!back) {
            Options.menu_MatriksInvers();
            int choice = inputHandler.inputInt();
            if (inputHandler.inputError_Int) {
                inputHandler.inputError_Int = false;
                System.out.println("Input tidak valid. Silakan masukkan angka.");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("Fitur Matriks Balikan Kofaktor belum tersedia.");
                    break;
                case 2:
                    System.out.println("Fitur Matriks Balikan Augmented belum tersedia.");
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void handlePolinomialInterpolasi() {
        System.out.println("Fitur Interpolasi Polinomial belum tersedia.");
    }

    private void handleCubicSpline() {
        System.out.println("Fitur Natural Cubic Spline Interpolation belum tersedia.");
    }

    private void handleRegresiSpline() {
        System.out.println("Fitur Regresi Spline Kubik belum tersedia.");
    }

    private void handleImageRestoration() {
        System.out.println("Fitur Image Restoration belum tersedia.");
    }
}
