package algeo.clis;

import algeo.modules.Matrix;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Handles keyboard input and converts it to project data objects. */
public class InputHandler {
    private final BufferedReader reader;
    public boolean inputEnded;
    public boolean inputError_Int;
    public boolean inputError_Double;

    public InputHandler() { this(System.in); }
    public InputHandler(InputStream stream) { this.reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)); }

    public String inputLine(String prompt) {
        if (prompt != null && !prompt.isEmpty()) System.out.print(prompt);
        System.out.flush();
        try { String line = reader.readLine(); inputEnded = line == null; return line; }
        catch (IOException e) { System.err.println("Gagal membaca input: " + e.getMessage()); return null; }
    }

    public int inputInt() {
        String text = inputLine("");
        try {
            if (text == null) throw new NumberFormatException();
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            inputError_Int = true;
            return 0;
        }
    }

    public double inputDouble() {
        String text = inputLine("");
        try {
            if (text == null) throw new NumberFormatException();
            double value = Double.parseDouble(text.trim().replace(',', '.'));
            if (!Double.isFinite(value)) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            inputError_Double = true;
            return 0.0;
        }
    }

    public Matrix inputMatrix() {
        System.out.print("Masukkan jumlah baris: ");
        int row = inputInt();
        if (inputError_Int || row <= 0) { inputError_Int = false; System.err.println("Jumlah baris harus berupa bilangan bulat positif."); return null; }
        System.out.print("Masukkan jumlah kolom: ");
        int col = inputInt();
        if (inputError_Int || col <= 0) { inputError_Int = false; System.err.println("Jumlah kolom harus berupa bilangan bulat positif."); return null; }
        return inputMatrix(row, col);
    }

    public Matrix inputMatrix(int row, int col) {
        if (row <= 0 || col <= 0) { System.err.println("Dimensi matriks harus positif."); return null; }
        Matrix matrix = new Matrix(row, col);
        for (int i = 0; i < row; i++) for (int j = 0; j < col; j++) {
            System.out.print("Masukkan elemen [" + (i + 1) + "][" + (j + 1) + "]: ");
            double value = inputDouble();
            if (inputError_Double) { inputError_Double = false; System.err.println("Elemen harus berupa bilangan real."); return null; }
            matrix.value[i][j] = value;
        }
        return matrix;
    }
}
