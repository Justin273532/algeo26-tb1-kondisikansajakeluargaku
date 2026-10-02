package algeo.clis;

public final class Options {
    private Options() { }
    public static void optionPrompt() { System.out.print("Pilih opsi: "); }
    public static void mainMenu() {
        System.out.println("\n===== Kalkulator Aljabar Linier =====");
        System.out.println("1. Sistem Persamaan Linear");
        System.out.println("2. Determinan Matriks");
        System.out.println("3. Matriks Balikan");
        System.out.println("4. Interpolasi Polinomial");
        System.out.println("5. Natural Cubic Spline");
        System.out.println("6. Regresi Spline Kubik");
        System.out.println("7. Image Restoration");
        System.out.println("8. Keluar");
        optionPrompt();
    }
    public static void menu_SPL() {
        System.out.println("\n====== Sistem Persamaan Linear ======");
        System.out.println("1. Eliminasi Gauss\n2. Eliminasi Gauss-Jordan\n3. Metode Matriks Balikan\n4. Kaidah Cramer\n5. Kembali");
        optionPrompt();
    }
    public static void menu_Determinan() {
        System.out.println("\n====== Determinan Matriks ======");
        System.out.println("1. Ekspansi Kofaktor\n2. Eliminasi OBE\n3. Kembali");
        optionPrompt();
    }
    public static void menu_MatriksInvers() {
        System.out.println("\n====== Matriks Balikan ======");
        System.out.println("1. Adjoin/Kofaktor\n2. Gauss-Jordan\n3. Kembali");
        optionPrompt();
    }
}
