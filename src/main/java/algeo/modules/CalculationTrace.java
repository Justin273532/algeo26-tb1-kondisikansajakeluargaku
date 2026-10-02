package algeo.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Collects human-readable calculation steps without printing from algorithms. */
public final class CalculationTrace {
    private final List<String> steps = new ArrayList<>();

    public void add(String step) {
        steps.add(step);
    }

    public void addMatrix(String heading, Matrix matrix) {
        StringBuilder text = new StringBuilder(heading).append('\n');
        for (int i = 0; i < matrix.row; i++) {
            for (int j = 0; j < matrix.col; j++) {
                if (j > 0) text.append(' ');
                text.append(Utilities.formatDouble(matrix.value[i][j]));
            }
            if (i + 1 < matrix.row) text.append('\n');
        }
        steps.add(text.toString());
    }

    public List<String> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public boolean isEmpty() {
        return steps.isEmpty();
    }

    @Override
    public String toString() {
        return String.join(System.lineSeparator() + System.lineSeparator(), steps);
    }
}
