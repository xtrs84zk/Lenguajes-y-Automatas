import java.io.*;

public class Calc3 {
    int indice;
    String tokens[];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Calc3 c3 = new Calc3();
        c3.ejecuta();
    }

    public void ejecuta() {
        double e = 0;
        BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));

        try {
            String linea = dataIn.readLine();
            while (linea != null) {
                indice = 0;
                tokens = linea.split("\\s");
                e = expr();
                if (indice < tokens.length) {
                    System.out.printf("error at %s\n", tokens[indice]);
                    return;
                } else {
                    System.out.printf("\t%.8g\n", e);
                }
                linea = dataIn.readLine();
            }

        } catch (IOException exc) {
            System.out.println("Error de lectura");
            return;
        }

    }

    double expr() {        //term | term[+-] term e = term()
        double e = 0;
        e = term();
        while (indice < tokens.length && (tokens[indice].compareTo("+") == 0 || tokens[indice].compareTo("-") == 0)) {
            e = tokens[indice++].compareTo("+") == 0 ? e + term() : e - term();
        }
        return e;
    }

    double term() {        //factor | factor[  */] factor e = factor()
        double e = 0;
        e = factor();
        while (indice < tokens.length && (tokens[indice].compareTo("*") == 0 || tokens[indice].compareTo("/") == 0)) {
            e = tokens[indice++].compareTo("*") == 0 ? e * factor() : e / factor();
        }
        return e;
    }

    double factor() {      //#number | (expr)
        double e = 0;
        if (indice >= tokens.length)
            return 0;
        if (tokens[indice].compareTo("(") == 0) {
            indice++;
            e = expr();
            if (tokens[indice++].compareTo(")") != 0) {
                System.out.printf("error: missing ) at %s\n", tokens[indice]);
                return 0;
            }
            return e;
        } else if(tokens[indice].compareTo("sin(") == 0) {
            indice++;
            e = expr();
            e = Math.sin(e);
            if(tokens[indice++].compareTo(")") != 0) {
                System.out.printf("error: missing ) at %s\n", tokens[indice]);
                return 0;
            }
            return e;
        }else if(tokens[indice].compareTo("cos(") == 0) {
            indice++;
            e = expr();
            e = Math.cos(e);
            if (tokens[indice++].compareTo(")") != 0) {
                System.out.printf("error: missing ) at %s\n", tokens[indice]);
                return 0;
            }
            return e;
        } else if(tokens[indice].compareTo("tan(") == 0) {
            indice++;
            e = expr();
            e = Math.tan(e);
            if (tokens[indice++].compareTo(")") != 0) {
                System.out.printf("error: missing ) at %s\n", tokens[indice]);
                return 0;
            }
            return e;
        }else {
            try {
                e = Double.parseDouble(tokens[indice++]);
                return e;
            } catch (NumberFormatException nfe) {
                System.out.printf("error: expected number or ( at %s\n", tokens[indice]);
                return 0;
            }
        }

    }
}
