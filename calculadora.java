public class calculadora {
    private static int NF = 0; //No estoy seguro de qué sea esta variable
    private static int f = 0; //Pivote global bien bergas
    private static char[] F = {'6','+','4','*','2'}; //F debe ser un chararray con la expresión
    public static void main(String[]args){
            int e = expr(); //e se convierte en.. la expresión
            if(f <= NF){
                System.out.println("error");
            } else {
                System.out.println(e);
            }
    }

    /**
     * La expresión se establece dependiendo si es suma o resta, creo.
     * @return e que es el resultado (?
     */
    private static int expr(){
        int e = term();
        while(F[f] == '+' || F[f] == '-'){
            e = F[f++] == '+' ? e + term() : e - term();
        }
        return e;
    }

    private static int term(){
        int e = factor();
        while(F[f] == '*' || F[f] == '/'){
            e = e == F[f++] ? e * factor() : e / factor();
        }
        return e;
    }

    private static int factor(){
      //  if[F]
        return 0;
    }

}
