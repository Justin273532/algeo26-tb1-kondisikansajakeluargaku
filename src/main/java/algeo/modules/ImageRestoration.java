package algeo.modules;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Harmonic inpainting for pixels selected by a grayscale mask (white means damaged). */
public class ImageRestoration {
    public static void restoreImage(String imagePath, String maskPath, String outputPath) throws IOException {
        BufferedImage source = ImageIO.read(new File(imagePath));
        BufferedImage mask = ImageIO.read(new File(maskPath));
        if (source == null || mask == null) throw new IOException("Format gambar asli atau mask tidak didukung.");
        int width = source.getWidth(), height = source.getHeight();
        if (width > 512 || height > 512 || width <= 0 || height <= 0) throw new IllegalArgumentException("Gambar harus berukuran 1 sampai 512 pixel pada setiap sisi.");
        if (mask.getWidth() != width || mask.getHeight() != height) throw new IllegalArgumentException("Ukuran gambar mask harus sama dengan gambar asli.");

        boolean[][] damaged = new boolean[height][width];
        double[][][] pixels = new double[height][width][3];
        int damagedCount = 0;
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            damaged[y][x] = (mask.getRGB(x, y) & 0xFF) > 128;
            if (damaged[y][x]) damagedCount++;
            int rgb = source.getRGB(x, y);
            pixels[y][x][0] = (rgb >> 16) & 0xFF;
            pixels[y][x][1] = (rgb >> 8) & 0xFF;
            pixels[y][x][2] = rgb & 0xFF;
        }
        if (damagedCount == 0) {
            ImageIO.write(source, "png", outputFile(outputPath));
            return;
        }
        if (damagedCount == width * height) throw new IllegalArgumentException("Mask tidak boleh menandai seluruh gambar sebagai rusak.");

        double[][][] next = new double[height][width][3];
        for (int iteration = 0; iteration < 10000; iteration++) {
            double maxChange = 0;
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
                for (int c = 0; c < 3; c++) next[y][x][c] = pixels[y][x][c];
                if (!damaged[y][x]) continue;
                for (int c = 0; c < 3; c++) {
                    double sum = 0; int count = 0;
                    if (y > 0) { sum += pixels[y - 1][x][c]; count++; }
                    if (y + 1 < height) { sum += pixels[y + 1][x][c]; count++; }
                    if (x > 0) { sum += pixels[y][x - 1][c]; count++; }
                    if (x + 1 < width) { sum += pixels[y][x + 1][c]; count++; }
                    next[y][x][c] = sum / count;
                    maxChange = Math.max(maxChange, Math.abs(next[y][x][c] - pixels[y][x][c]));
                }
            }
            double[][][] swap = pixels; pixels = next; next = swap;
            if (maxChange < 0.1) break;
        }
        BufferedImage restored = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
            int r = clamp(pixels[y][x][0]), g = clamp(pixels[y][x][1]), b = clamp(pixels[y][x][2]);
            restored.setRGB(x, y, (r << 16) | (g << 8) | b);
        }
        if (!ImageIO.write(restored, "png", outputFile(outputPath))) throw new IOException("Tidak tersedia encoder PNG.");
    }

    private static File outputFile(String path) throws IOException {
        File out = new File(path);
        File parent = out.getAbsoluteFile().getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) throw new IOException("Folder output tidak dapat dibuat.");
        return out;
    }
    private static int clamp(double value) { return Math.max(0, Math.min(255, (int) Math.round(value))); }
}
