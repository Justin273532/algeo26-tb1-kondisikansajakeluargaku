package algeo.modules;

public class Options {
    public static void optionPrompt(){
        System.out.Print("Pilih opsi (1-8): ");
    }
    public static void mainMenu(){
        System.out.Println("===== Kalkulator Matriks =====");
        System.out.Println("");
        System.out.Println("1. Sistem Persamaan Linear");
        System.out.Println("2. Determinan Matriks");
        System.out.Println("3. Matriks Balikan (Invers)");
        System.out.Println("4. Interpolasi Polinomial");
        System.out.Println("5. Natural Cubic Spline Interpolation");
        System.out.Println("6. Regresi Spline Kubik");
        System.out.Println("7. Image Restoration");
        System.out.Println("8. Keluar");
        System.out.Println("");
        optionPrompt();
    }
    public static void menu_SPL(){
        System.out.Println("====== Sistem Persamaan Linear ======");
        System.out.Println("");
        System.out.Println("1. Metode Eliminasi Gauss");
        System.out.Println("2. Metode Eliminasi Gauss-Jordan");
        System.out.Println("3. Metode Matriks Balikan");
        System.out.Println("4. Metode Kaidah Cramer");
        System.out.Println("5. Kembali");
        System.out.Println("");
        optionPrompt();
    }
    public static void menu_Determinan(){
        System.out.Println("====== Determinan Matriks ======");
        System.out.Println("");
        System.out.Println("1. Determinan Kofaktor");
        System.out.Println("2. Determinan OBE");
        System.out.Println("3. Kembali");
        System.out.Println("");
        optionPrompt();
    }
    public static void menu_MatriksInvers(){
        System.out.Println("====== Matriks Balikan ======");
        System.out.Println("");
        System.out.Println("1. Matriks Balikan Kofaktor");
        System.out.Println("2. Matriks Balikan Augmented");
        System.out.Println("3. Kembali");
        System.out.Println("");
        optionPrompt();
    }
}