import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class calculadora {
    //private static int NF = 0; //No estoy seguro de qué sea esta variable
    private static int indice = 0; //Pivote global
    private static String[] F; //F debe ser un array con la expresión
    public static void main(String[]args){
            double e;
        BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));

        try {
            String linea = dataIn.readLine();
            while (linea != null) {
                indice = 0;
                F = linea.split("\\s");
                e = expr();
                if (indice < F.length) {
                    System.out.printf("error at %s\n", F[indice]);
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

    /**
     * La expresión se establece dependiendo si es suma o resta, creo.
     * @return e que es el resultado (?
     */
    private static double expr(){
        double e = term();
        while(indice < F.length && (F[indice].compareTo("+") == 0 || F[indice].compareTo("-") == 0)){
            e = F[indice++].equals("+") ? e + term() : e - term();
        }
        return e;
    }

    /**
     * Se establece el estilo de término para la expresión.
     * Puede ser tanto una multiplicación o una división.
     * @return
     */
    private static double term(){
        double e = factor();
        while(indice < F.length && (F[indice].compareTo("*") == 0 || F[indice].compareTo("/") == 0)){
            //e = e.equals(F[indice++]) ? e * factor() : e / factor();
            e = F[indice++].compareTo("*") == 0 ? e * factor() : e / factor();
        }
        return e;
    }

    /**
     * Determina el estado final, idealmente, convertirá la expresión en un número.
     * Además, se encarga de resolver las expresiones aritméticas.
     * @return e
     */
    private static double factor() {
    //#number | (expr)
    double e = 0;
        if (indice >= F.length)
            return 0;
        if (F[indice].compareTo("(") == 0) {
        indice++;
        e = expr();
        if (F[indice++].compareTo(")") != 0) {
            System.out.printf("error: missing ) at %s\n", F[indice]);
            return 0;
        }
        return e;
    } else if(F[indice].compareTo("sin") == 0) {
        indice++;
        e = expr();
        e = Math.sin(e);
            if(indice<F.length -1 ) {
                if (F[indice++].compareTo(")") != 0) {
                    System.out.printf("error: missing ) at %s\n", F[indice]);
                    return 0;
                }
            }
        return e;
    }else if(F[indice].compareTo("cos") == 0) {
        indice++;
        e = expr();
        e = Math.cos(e);
        if(indice<F.length -1 ) {
            if (F[indice++].compareTo(")") != 0) {
                System.out.printf("error: missing ) at %s\n", F[indice]);
                return 0;
            }
        }
        return e;
    } else if(F[indice].compareTo("tan") == 0) {
        indice++;
        e = expr();
        e = Math.tan(e);
            if(indice<F.length -1 ) {
                if (F[indice++].compareTo(")") != 0) {
                    System.out.printf("error: missing ) at %s\n", F[indice]);
                    return 0;
                }
            }
        return e;
    }else {
        try {
            e = Double.parseDouble(F[indice++]);
            return e;
        } catch (NumberFormatException nfe) {
            System.out.printf("error: expected number or ( at %s\n", F[indice]);
            return 0;
        }
    }

}

}
