package algeo.modules;
package algeo.clis;
import java.io.*;


public class InputHandler {
    static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    
    public bool inputError_Int;
    public bool inputError_Double;
    
    public static int inputInt(){
        String s = BR.readLine();
        int x = 0;
        for(int i = 0; i < s.length(); i++){
            if(s[i] < '0' || s[i] > '9') {
                this.inputError_Int = true;
                return 0;
            }
            x *= 10;
            x += (s[i]-'0');
        }
        return x;
    }
    public static double inputDouble(){
        String s = BR.readLine();
        int coma = -1;
        double x = 0.0;
        for(int i = 0; i < s.length(); i++){
            if(s[i] >= 0 && s[i] <= 9){
                x *= 10;
                x += (s[i]-'0');  
            }else if(s[i] == ',' || s[i] == '.'){
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
}