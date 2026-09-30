package  algeo.clis;
import algeo.modules.*;

public class Options {
    public static void optionPrompt(){
        System.out.print("Pilih opsi (1-8): ");
    }
    public static void mainMenu(){
        System.out.println("===== Kalkulator Matriks =====");
        System.out.println("");
        System.out.println("1. Sistem Persamaan Linear");
        System.out.println("2. Determinan Matriks");
        System.out.println("3. Matriks Balikan (Invers)");
        System.out.println("4. Interpolasi Polinomial");
        System.out.println("5. Natural Cubic Spline Interpolation");
        System.out.println("6. Regresi Spline Kubik");
        System.out.println("7. Image Restoration");
        System.out.println("8. Keluar");
        System.out.println("");
        optionPrompt();
    }
    public static void menu_SPL(){
        System.out.println("====== Sistem Persamaan Linear ======");
        System.out.println("");
        System.out.println("1. Metode Eliminasi Gauss");
        System.out.println("2. Metode Eliminasi Gauss-Jordan");
        System.out.println("3. Metode Matriks Balikan");
        System.out.println("4. Metode Kaidah Cramer");
        System.out.println("5. Kembali");
        System.out.println("");
        optionPrompt();
    }
    public static void menu_Determinan(){
        System.out.println("====== Determinan Matriks ======");
        System.out.println("");
        System.out.println("1. Determinan Kofaktor");
        System.out.println("2. Determinan OBE");
        System.out.println("3. Kembali");
        System.out.println("");
        optionPrompt();
    }
    public static void menu_MatriksInvers(){
        System.out.println("====== Matriks Balikan ======");
        System.out.println("");
        System.out.println("1. Matriks Balikan Kofaktor");
        System.out.println("2. Matriks Balikan Augmented");
        System.out.println("3. Kembali");
        System.out.println("");
        optionPrompt();
    }
}