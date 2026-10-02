package algeo.clis;
import algeo.modules.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InputHandler {
    static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    
    public boolean inputError_Int;
    public boolean inputError_Double;
    
    public int inputInt(){
        String s;
        try {
            s = BR.readLine();
        } catch (IOException e) {
            inputError_Int = true;
            System.err.println(e);
            return 0;
        }
        int x = 0;
        for(int i = 0; i < s.length(); i++){
            if(s.indexOf(i) < '0' || s.indexOf(i) > '9') {
                this.inputError_Int = true;
                return 0;
            }
            x *= 10;
            x += (s.indexOf(i)-'0');
        }
        return x;
    }
    public double inputDouble(){
        String s;
        try {
            s = BR.readLine();
        } catch (IOException e) {
            inputError_Double = true;
            System.err.println(e);
            return 0;
        }
        int coma = -1;
        double x = 0.0;
        for(int i = 0; i < s.length(); i++){
            if(s.indexOf(i) >= 0 && s.indexOf(i) <= 9){
                x *= 10;
                x += (s.indexOf(i)-'0');  
            }else if(s.indexOf(i) == ',' || s.indexOf(i) == '.'){
                if(coma != -1){
                  this.inputError_Double = true;
                  return 0.00;
                }
                coma = i;
            }else{
                this.inputError_Double = true;
                return 0.00;
            }
        }
        if(coma != -1){
            x /= Math.pow(10.0, coma);
        }
        return x;
    }

    public Matrix inputMatrix(){
        System.out.print("Masukkan jumlah baris: ");
        int row = inputInt();
        if(inputError_Int){
            inputError_Int = false;
            System.err.println("Input jumlah baris tidak valid.");
            return null;
        }
        System.out.print("Masukkan jumlah kolom: ");
        int col = inputInt();
        if(inputError_Int){
            inputError_Int = false;
            System.err.println("Input jumlah kolom tidak valid.");
            return null;
        }
        return inputMatrix(row, col);
    }

    public Matrix inputMatrix(int row, int col){
        Matrix A = new Matrix(row, col);
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                System.out.print("Masukkan elemen [" + (i + 1) + "][" + (j + 1) + "]: ");
                double val = inputDouble();
                if(inputError_Double){
                    inputError_Double = false;
                    System.err.println("Input elemen tidak valid.");
                    return null;
                }
                A.value[i][j] = val;
            }
        }
        return A;
    }
}