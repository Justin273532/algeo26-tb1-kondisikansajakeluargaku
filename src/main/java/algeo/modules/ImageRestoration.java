package algeo.modules;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRestoration {
    public static void restoreImage() {
        try{
            // Terima input gambar
            System.out.print("Masukkan path gambar asli: ");
            String pathGambar = Utilities.input.next();
        
            // Terima input mask
            System.out.print("Masukkan path gambar mask: ");
            String pathMask = Utilities.input.next();

            System.out.print("Masukkan path output hasil restorasi: ");
            String pathHasil = Utilities.input.next();

            BufferedImage gambarAsli = ImageIO.read(new File(pathGambar));
            BufferedImage gambarMask = ImageIO.read(new File(pathMask));

            int lebar = gambarAsli.getWidth();
            int tinggi = gambarAsli.getHeight();

            if (lebar > 512 || tinggi > 512){
                System.out.println("Error: Gambar wajib berukuran maksimal 512x512 pixel. Tolong masukkan gambar yang valid.");
                return;
            }

            double[][][] matriksGambar = new double[tinggi][lebar][3];
            Matrix matriksMask = Utilities.createMatrix(lebar, tinggi);
            int N = 0;

            // Hitung jumlah pixel rusak dan tetapkan nilai pada matriks sebagai penanda
            for (int i = 0; i < tinggi; i++){
                for (int j = 0; j < lebar; j++){
                    int warna = gambarMask.getRGB(j, i) & 0xFF;
                    if (warna > 128){
                        matriksMask.value[i][j] = 1.0;
                        matriksGambar[i][j][0] = 0.0;
                        matriksGambar[i][j][1] = 0.0;
                        matriksGambar[i][j][2] = 0.0;
                        N++;

                    } else {
                        matriksMask.value[i][j] = 0.0;
                        int rgb = gambarAsli.getRGB(j, i);
                        double red = (rgb >> 16) & 0xFF;
                        double green = (rgb >> 8) & 0xFF;
                        double blue = rgb & 0xFF;
                        matriksGambar[i][j][0] = red;
                        matriksGambar[i][j][1] = green;
                        matriksGambar[i][j][2] = blue;
                    }
                }
            }

            // Membuat matriks baru berisi koordinat pixel rusak
            Matrix pixelRusak = Utilities.createMatrix(N, 2);
            int idx = 0;
            for (int i = 0; i < tinggi; i++){
                for (int j = 0; j < lebar; j++){
                    if (matriksMask.value[i][j] == 1.0){
                        pixelRusak.value[idx][0] = j;
                        pixelRusak.value[idx][1] = i;
                        idx++;
                    } 
                }
            }

            int batasIterasi = 1000;
            int jumlahIterasi = 0;
            double galatMaks = 0.0001;
            double galatTerbesar = 100.0;
            while (jumlahIterasi < batasIterasi && galatTerbesar > galatMaks){
                for (int i = 0; i < N; i++){
                    int x = (int) pixelRusak.value[i][0];
                    int y = (int) pixelRusak.value[i][1];
                    for (int z = 0; z < 3; z++){
                        galatTerbesar = 0.0;
                        double atas = matriksGambar[y - 1][x][z];
                        double bawah = matriksGambar[y + 1][x][z];
                        double kiri = matriksGambar[y][x - 1][z];
                        double kanan = matriksGambar[y][x + 1][z];

                        double nilaiLama = matriksGambar[y][x][z];
                        double nilaiBaru = (atas + bawah + kiri + kanan) / 4;

                        double selisih = Math.abs(nilaiBaru - nilaiLama);
                        if (selisih > galatTerbesar){
                            galatTerbesar = selisih;
                        }

                        matriksGambar[y][x][z] = nilaiBaru;
                    }
                }
                jumlahIterasi++;
            }

            BufferedImage gambarHasil = new BufferedImage(lebar, tinggi, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < tinggi; i++){
                for (int j = 0; j < lebar; j++){
                    int r = (int) Math.round(matriksGambar[i][j][0]);
                    int g = (int) Math.round(matriksGambar[i][j][1]);
                    int b = (int) Math.round(matriksGambar[i][j][2]);

                    int rgb_hasil = (r << 16) | (g << 8) | b;
                    gambarHasil.setRGB(j, i, rgb_hasil);

                }
            }
            File fileHasil = new File(pathHasil);
            ImageIO.write((gambarHasil), "png", fileHasil);
            System.out.println("Restorasi berhasil! Gambar hasil disimpan di: " + fileHasil.getAbsolutePath());
            System.out.println("Jumlah iterasi yang dilakukan pada restorasi sebanyak " + jumlahIterasi + " iterasi.");
            System.out.println("Nilai galat terbesar akhir adalah " + galatTerbesar);
            System.out.println("Dimensi SPL yang diselesaikan adalah " + N + "x" + N);

        } catch (IOException e){
            System.out.println("Terjadi kesalahan saat memperbaiki gambar: " +e.getMessage());
        }
    }
    public static void main(String[] args) {
    new ImageRestoration().restoreImage();
    }

}