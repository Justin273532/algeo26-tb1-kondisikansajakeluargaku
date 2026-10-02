package algeo.clis;

import algeo.modules.Matrix;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class MatrixIOTest {
    @TempDir Path tempDir;
    @Test void formatsOutputToThreeDecimalPlaces() {
        Matrix matrix = new Matrix(1, 2);
        matrix.value[0][0] = 1.23456;
        matrix.value[0][1] = -2;
        assertEquals("1.235 -2.000", MatrixIO.formatMatrix(matrix));
        assertEquals("-0.125", algeo.modules.Utilities.formatDouble(-0.1254));
    }

    @Test void readsDecimalDotAndCommaAndWritesRoundTrip() throws Exception {
        Path input = tempDir.resolve("matrix.txt");
        Files.writeString(input, "# data\n1,5 2.25\n-3 4\n");
        Matrix parsed = MatrixIO.readMatrix(input);
        assertEquals(1.5, parsed.value[0][0], 1e-12);
        assertEquals(2.25, parsed.value[0][1], 1e-12);
        Path output = tempDir.resolve("out.txt");
        MatrixIO.writeText(output, MatrixIO.formatMatrix(parsed));
        Matrix roundTrip = MatrixIO.readMatrix(output);
        assertEquals(-3, roundTrip.value[1][0], 1e-12);
    }
    @Test void rejectsInconsistentRowsAndMissingFiles() {
        Path malformed = tempDir.resolve("bad.txt");
        assertDoesNotThrow(() -> Files.writeString(malformed, "1 2\n3\n"));
        assertThrows(IllegalArgumentException.class, () -> MatrixIO.readMatrix(malformed));
        assertThrows(java.io.IOException.class, () -> MatrixIO.readMatrix(tempDir.resolve("missing.txt")));
    }
}
