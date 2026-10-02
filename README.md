# IF2123 Aljabar Linier dan Geometri â€” Kalkulator Matriks

A Java 17 command-line calculator for linear systems, determinants, matrix inverses, polynomial interpolation, natural cubic splines, and cubic spline regression. It also includes a masked-image restoration utility.

## Requirements

- JDK 17 or newer
- Apache Maven 3.6.3 or newer

## Build and run

```bash
mvn test
mvn package
java -jar target/matrix-calculator-1.0-SNAPSHOT.jar
```

`mvn package` runs the test suite and creates an executable JAR with `algeo.App` as its entry point.

## CLI and input format

The CLI accepts matrices from the keyboard or from UTF-8 `.txt` files. A matrix file contains one row per line and whitespace-separated values, for example:

```text
1 2 3
4.5 -6 7
```

Blank lines and lines beginning with `#` are ignored. Decimal points and decimal commas are accepted (`4.5` or `4,5`). Every row must contain the same number of values. For a linear system entered from files, provide separate files for the square coefficient matrix `A` and the column vector `b`. Results can be saved as UTF-8 text files from the CLI. Displayed and saved calculated values are rounded to three decimal places; internal calculations retain full double precision.

For interpolation and spline operations, provide a matrix with two columns (`x y`), one point per row. Spline regression asks for the knot values after reading the data.

## Project layout

- `src/main/java/algeo/clis`: menus, controller, keyboard handling, and text file I/O
- `src/main/java/algeo/modules`: matrix operations and mathematical algorithms
- `src/test/java`: JUnit regression tests

## Fitur Program
### 1. SPL
- Metode Eliminasi Gauss
- Metode Eliminasi Gauss-Jordan
- Kaidah Cramer

### 2. Determinan
- Determinan dengan Metode OBE
- Determinan dengan Metode Kofaktor 

### 3. Invers
- Invers dengan Metode Matriks Augmented
- Invers dengan Metode Kofaktor

### 4. Interpolasi
- Interpolasi Polinomial
- Interpolasi Spline Kubik

### 5. Regresi
- Regresi Spline Kubik

### 6. Image Inpainting
- Image Restoration dengan Metode Gauss-Seidel

Lengkapi kembali README kelompok dengan deskripsi program, alur penggunaan, dan cara menjalankan program sebelum pengumpulan.
