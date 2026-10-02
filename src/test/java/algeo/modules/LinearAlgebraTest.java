package algeo.modules;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinearAlgebraTest {
    private static Matrix matrix(double[][] values) {
        Matrix out = new Matrix(values.length, values[0].length);
        for (int i = 0; i < values.length; i++) System.arraycopy(values[i], 0, out.value[i], 0, values[i].length);
        return out;
    }
    private static void assertClose(double expected, double actual) { assertEquals(expected, actual, 1e-8); }

    @Test void determinantHandlesOneByOneRowSwapsAndSingularMatrices() {
        assertClose(7, Utilities.Determinant(matrix(new double[][]{{7}})));
        assertClose(-1, Utilities.DeterminantOBE(matrix(new double[][]{{0, 1}, {1, 0}})));
        assertClose(0, Utilities.DeterminantOBE(matrix(new double[][]{{1, 2}, {2, 4}})));
    }

    @Test void determinantMethodsRecordTheirCalculationSteps() {
        Matrix a = matrix(new double[][]{{0, 1}, {1, 0}});
        CalculationTrace cofactorSteps = new CalculationTrace();
        CalculationTrace obeSteps = new CalculationTrace();
        assertClose(-1, Utilities.Determinant(a, cofactorSteps));
        assertClose(-1, Utilities.DeterminantOBE(a, obeSteps));
        assertTrue(cofactorSteps.toString().contains("Ekspansi kofaktor"));
        assertTrue(cofactorSteps.toString().contains("determinan minor"));
        assertTrue(obeSteps.toString().contains("Tukar baris"));
        assertTrue(obeSteps.toString().contains("hasil kali elemen diagonal"));
    }

    @Test void inverseMethodsRecordTheirCalculationSteps() {
        Matrix a = matrix(new double[][]{{2, 1}, {1, -1}});
        CalculationTrace cofactorSteps = new CalculationTrace();
        CalculationTrace gaussJordanSteps = new CalculationTrace();
        Matrix cofactorInverse = Utilities.InverseMatrix(a, cofactorSteps);
        Matrix gaussJordanInverse = Utilities.InverseMatrixAugmented(a, gaussJordanSteps);
        assertClose(cofactorInverse.value[0][0], gaussJordanInverse.value[0][0]);
        assertTrue(cofactorSteps.toString().contains("Matriks kofaktor C"));
        assertTrue(cofactorSteps.toString().contains("Transpose matriks kofaktor"));
        assertTrue(gaussJordanSteps.toString().contains("Matriks augmented awal [A|I]"));
        assertTrue(gaussJordanSteps.toString().contains("Skalakan R"));
        assertTrue(gaussJordanSteps.toString().contains("Karena [I|A^-1]"));
    }

    @Test void inverseMethodsAndCramerReturnKnownSolution() {
        Matrix a = matrix(new double[][]{{2, 1}, {1, -1}});
        Matrix b = matrix(new double[][]{{5}, {1}});
        Matrix cramer = CramerRule.cramerRule(a, b);
        Matrix inverse = InverseMethod.inverseMethod(a, b);
        assertClose(2, cramer.value[0][0]); assertClose(1, cramer.value[1][0]);
        assertClose(2, inverse.value[0][0]); assertClose(1, inverse.value[1][0]);
        assertClose(1, Utilities.matrixMultiplication(a, Utilities.InverseMatrixAugmented(a)).value[0][0]);
    }

    @Test void algorithmsRejectNonFiniteMatrixEntries() {
        Matrix invalid = matrix(new double[][]{{Double.NaN}});
        assertThrows(IllegalArgumentException.class, () -> Utilities.DeterminantOBE(invalid));
    }

    @Test void inverseAndCramerRejectSingularMatrices() {
        Matrix singular = matrix(new double[][]{{1, 2}, {2, 4}});
        assertThrows(IllegalArgumentException.class, () -> Utilities.InverseMatrix(singular));
        assertThrows(IllegalArgumentException.class, () -> CramerRule.cramerRule(singular, matrix(new double[][]{{1}, {2}})));
    }

    @Test void matrixOperationsRejectInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> Utilities.DeterminantOBE(matrix(new double[][]{{1, 2}})));
        assertThrows(IllegalArgumentException.class, () -> Utilities.matrixMultiplication(matrix(new double[][]{{1, 2}}), matrix(new double[][]{{1}, {2}, {3}})));
        assertThrows(IllegalArgumentException.class, () -> CramerRule.cramerRule(matrix(new double[][]{{1, 0}, {0, 1}}), matrix(new double[][]{{1}})));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(-1, 2));
    }

    @Test void gaussianAndGaussJordanClassifyUniqueInfiniteAndNoSolutions() {
        Matrix unique = matrix(new double[][]{{1, 1, 3}, {2, -1, 0}});
        assertEquals(GaussElim.SolutionType.UNIQUE_SOLUTION, GaussElim.solveByGaussElim(unique).type);
        assertClose(1, GaussJordanElim.solveByGaussJordan(unique).solution.value[0][0]);
        assertEquals(GaussElim.SolutionType.INFINITE_SOLUTION, GaussElim.solveByGaussElim(matrix(new double[][]{{1, 2, 3}, {2, 4, 6}})).type);
        assertEquals(GaussElim.SolutionType.NO_SOLUTION, GaussJordanElim.solveByGaussJordan(matrix(new double[][]{{1, 2, 3}, {2, 4, 7}})).type);
    }

    @Test void gaussianUsesPartialPivotingAndTolerance() {
        Matrix augmented = matrix(new double[][]{{1e-12, 1, 2}, {1, 1, 3}});
        GaussElim.Result result = GaussElim.solveByGaussElim(augmented);
        assertEquals(GaussElim.SolutionType.UNIQUE_SOLUTION, result.type);
        assertClose(1, result.solution.value[0][0]); assertClose(2, result.solution.value[1][0]);
    }

    @Test void polynomialInterpolationRejectsRepeatedAbscissasAndEvaluates() {
        Matrix points = matrix(new double[][]{{0, 1}, {1, 3}, {2, 5}});
        Matrix coefficients = PolinomialInterpolation.polinomialInterpolate(points);
        assertClose(7, PolinomialInterpolation.evaluate(coefficients, 3));
        assertThrows(IllegalArgumentException.class, () -> PolinomialInterpolation.polinomialInterpolate(matrix(new double[][]{{1, 2}, {1, 3}})));
    }

    @Test void naturalSplineInterpolatesPointsAndRejectsDuplicateX() {
        CubicSplineInterpolation.Spline spline = CubicSplineInterpolation.cubicSplineInterpolate(matrix(new double[][]{{0, 0}, {1, 1}, {2, 0}}));
        assertClose(1, spline.evaluate(1)); assertClose(0, spline.evaluate(2));
        assertThrows(IllegalArgumentException.class, () -> CubicSplineInterpolation.cubicSplineInterpolate(matrix(new double[][]{{0, 1}, {0, 2}})));
    }

    @Test void splineRegressionFitsAFullRankCubicBasis() {
        Matrix points = matrix(new double[][]{{0, 2}, {1, 5}, {2, 8}, {3, 11}});
        Matrix coefficients = SplineRegression.fit(points, new double[0]);
        assertClose(2, SplineRegression.evaluate(0, coefficients, new double[0]));
        assertClose(11, SplineRegression.evaluate(3, coefficients, new double[0]));
        assertThrows(IllegalArgumentException.class, () -> SplineRegression.fit(matrix(new double[][]{{0, 1}, {1, 2}}), new double[0]));
    }
}
