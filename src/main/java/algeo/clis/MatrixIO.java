package algeo.clis;

import algeo.modules.Matrix;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Text matrix format: one row per line, values separated by whitespace; decimal comma or dot is accepted. */
public final class MatrixIO {
    private MatrixIO() { }

    public static Matrix readMatrix(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<double[]> rows = new ArrayList<>();
        int width = -1;
        for (int lineNo = 0; lineNo < lines.size(); lineNo++) {
            String line = lines.get(lineNo).trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] tokens = line.split("\\s+");
            if (width < 0) width = tokens.length;
            if (tokens.length != width) throw new IllegalArgumentException("Jumlah elemen baris tidak konsisten pada baris " + (lineNo + 1) + ".");
            double[] row = new double[width];
            for (int j = 0; j < width; j++) {
                try {
                    row[j] = Double.parseDouble(tokens[j].replace(',', '.'));
                    if (!Double.isFinite(row[j])) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Nilai matriks tidak valid pada baris " + (lineNo + 1) + ", kolom " + (j + 1) + ".");
                }
            }
            rows.add(row);
        }
        if (rows.isEmpty() || width <= 0) throw new IllegalArgumentException("File tidak berisi matriks.");
        Matrix matrix = new Matrix(rows.size(), width);
        for (int i = 0; i < rows.size(); i++) System.arraycopy(rows.get(i), 0, matrix.value[i], 0, width);
        return matrix;
    }

    public static String formatMatrix(Matrix matrix) {
        if (matrix == null) throw new IllegalArgumentException("Matriks tidak boleh null.");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < matrix.row; i++) {
            for (int j = 0; j < matrix.col; j++) {
                if (j > 0) out.append(' ');
                out.append(algeo.modules.Utilities.formatDouble(matrix.value[i][j]));
            }
            if (i + 1 < matrix.row) out.append(System.lineSeparator());
        }
        return out.toString();
    }

    public static void writeText(Path path, String text) throws IOException {
        Path absolute = path.toAbsolutePath();
        Path parent = absolute.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(absolute, text, StandardCharsets.UTF_8);
    }
}
