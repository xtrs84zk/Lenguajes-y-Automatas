import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Automata {
    public static void main(String[] args) {
        String tokens[] = new String[100];
        inicializarTokens(tokens);
        ArrayList<String> codigoPorLineas;
        ArrayList<String> analisisLexicoDeTodasLasLineas = new ArrayList<String>();
        try {
            codigoPorLineas = cargarCodigoDesdeUnArchivoDeTexto();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("No hay archivo de código, no puede continuar.");
            return;
        }
        System.out.println("El archivo de texto se ha cargado correctamente.");

        //Analizar línea por línea
        int i = 0;
        try {
            do {
                analisisLexicoDeTodasLasLineas.addAll(identificarLexicoEnUnaLinea(codigoPorLineas.get(i), tokens, i));
                i++;
            } while (codigoPorLineas.get(i) != null);
        } catch (Exception e) {
            System.err.print("Límite de la lista alcanzado, no debería ocurrir, pero después reviso \n");
        }
        //concatenar el análisis a un archivo
        try {
            escribirElResultadoAUnArchivo(analisisLexicoDeTodasLasLineas);
            System.out.println("El archivo se guardó satisfactoriamente.");
        } catch (Exception e) {
            System.err.print(e.toString());
            System.out.println("El archivo no se pudo guardar, imprimiendo el resultado en pantalla.");
            try {
                i = 0;
                do {
                    System.out.print(analisisLexicoDeTodasLasLineas.get(i));
                    i++;
                } while (analisisLexicoDeTodasLasLineas.get(i) != null);
            } catch (Exception p) {
                System.err.print("Límite de la lista alcanzado, es común. \n");
            }
        }
    }

    private static ArrayList cargarCodigoDesdeUnArchivoDeTexto() throws IOException {
        ArrayList<String> codigoPorLineas = new ArrayList<String>();
        File archivoDelCodigo = new File("C:/users/xtrs84zk/desktop/codigo.txt");
        InputStreamReader input = null;
        input = new InputStreamReader(new FileInputStream(archivoDelCodigo), "UTF8");
        if (input != null) {
            codigoPorLineas = new ArrayList<String>();
            String r;
            BufferedReader in = new BufferedReader(input);
            //el código será leído en minúsculas
            while ((r = in.readLine()) != null) {
                codigoPorLineas.add(r.toLowerCase() + "\n");
            }
            in.close();
        }
        return codigoPorLineas;
    }

    /**
     * Hipotéticamente, este método inicializará el arreglo de tokens que contiene cada elemento definido
     * del lenguaje, su token está definido por la posición que ocupa en el arreglo.
     *
     * @param tokens referencia que fungirá para almacenar los token
     */
    private static void inicializarTokens(String tokens[]) {
        //Palabras reservadas
        tokens[1] = "prog";
        tokens[6] = "limpiar";
        tokens[11] = "leer";
        tokens[16] = "si";
        tokens[2] = "var";
        tokens[7] = "vexy";
        tokens[12] = "escribir";
        tokens[17] = "sino";
        tokens[3] = "proc";
        tokens[8] = "string";
        tokens[13] = "repite";
        tokens[18] = "ejecuta";
        tokens[4] = "inicio";
        tokens[9] = "real";
        tokens[14] = "hasta";
        tokens[19] = "and";
        tokens[5] = "fin";
        tokens[10] = "10";
        tokens[15] = "mientras";
        tokens[20] = "or";

        //Operadores aritméticos
        tokens[31] = "+";
        tokens[32] = "-";
        tokens[33] = "*";
        tokens[34] = "/";

        //Operadores relacionales
        tokens[41] = "<";
        tokens[43] = "<=";
        tokens[46] = "<>";
        tokens[42] = ">";
        tokens[44] = ">=";
        tokens[35] = "=";

        //Operadores lógicos
        tokens[51] = "&&";
        tokens[52] = "||";
        tokens[53] = "!";

        //Carácteres especiales que generan tokens
        tokens[85] = ";";
        tokens[83] = "[";
        tokens[84] = "]";
        tokens[88] = ",";
        tokens[86] = ":";
        tokens[82] = "(";
        tokens[82] = ")";
        tokens[87] = ":=";

        //Carácteres especiales que no generan token
        //Pensando qué haré con ellos
        //String caracteresEspecialesQueNoGeneranToken[] = {"\"", ".", "BCO", "TAB", "EOLN", "EOF"};
    }


    private static ArrayList identificarLexicoEnUnaLinea(String lineaAIdentificar, String[] tokens, int numeroDeLinea) {
        ArrayList<String> analisisDeLaLinea = new ArrayList<String>();
        //Estableciendo las expresiones
        String analisisDeLaIteracionActual = null;
        String expresiones[] = lineaAIdentificar.split(Pattern.quote(" "));
        for (int i = 0; i < expresiones.length; i++) {
            //Si alguna de las expresiones comienza por comillas, se procede a concatenar expresiones
            //mientras la expresión completa sea incorrecta, una vez que es correcta, se procede a analizar
            if (expresiones[i].charAt(0) == '\"') {
                String posibleConstante = expresiones[i];
                do {
                    i++;
                    //si la línea termina, la expresión se considera incorrecta.
                    if (i > expresiones.length - 1) {
                        analisisDeLaLinea.add(analisisLexico(posibleConstante, 100, 1, numeroDeLinea));
                        return analisisDeLaLinea;
                    }
                    posibleConstante += " " + expresiones[i];
                } while (!constanteStringCorrectamenteFormulada(posibleConstante) && i < expresiones.length);
                //Si el do finaliza satisfactoriamente, la expresión es considerada una constante correcta.
                analisisDeLaLinea.add(analisisLexico(posibleConstante, 64, 1, numeroDeLinea));
            } else {
                analisisDeLaIteracionActual = analisisLexicoDeElementosDelLenguaje(expresiones[i], tokens, numeroDeLinea);
                if (analisisDeLaIteracionActual != null) {
                    analisisDeLaLinea.add(analisisDeLaIteracionActual);
                } else {
                    analisisDeLaIteracionActual = analisisLexicoDeIdentificadores(expresiones[i], numeroDeLinea);
                    if (analisisDeLaIteracionActual == null) {
                        analisisDeLaLinea.add(analisisLexico(expresiones[i], 100, 1, numeroDeLinea));
                    }
                    analisisDeLaLinea.add(analisisDeLaIteracionActual);
                }
            }
        }
        return analisisDeLaLinea;
    }

    private static String analisisLexicoDeElementosDelLenguaje(String posibleIdentificador, String[] tokens, int numeroDeLinea) {
        int token = obtenerToken(tokens, posibleIdentificador);
        if (token != 100) {
            return analisisLexico(posibleIdentificador, token, 1, numeroDeLinea);
        }
        return null;
    }

    private static String analisisLexico(String elemento, int token, int tipoDeElemento, int numeroDeLinea) {
        return "(" + elemento + ", -" + token + ", -" + tipoDeElemento + " ," + numeroDeLinea + " )" + "\r\n";
    }

    /**
     * Recibe un arreglo y la cadena que buscará en él. En caso de encontrarlo, regresa
     * su posición en el arreglo; de lo contrario, regresa -100 como mensaje de error.
     *
     * @param tokens
     * @param loQueDeberiaContener
     * @return
     */
    private static int obtenerToken(String[] tokens, String loQueDeberiaContener) {
        loQueDeberiaContener = loQueDeberiaContener.replaceAll("\n", "");
        for (int i = 0; i < 100; i++) {
            if (tokens[i] != null) {
                if (tokens[i].equals(loQueDeberiaContener)) {
                    return i;
                }
            }
        }
        return 100;
    }

    private static String analisisLexicoDeIdentificadores(String posibleIdentificador, int numeroDeLinea) {
        //Los identificadores inician con una letra y pueden seguir con letras o hasta seis caracteres.
        if (Character.isDigit(posibleIdentificador.charAt(0))) {
            //El elemento comienza con un número, posible constante.
            switch (tipoDeConstanteNumerica(posibleIdentificador)) {
                case 2:
                    return analisisLexico(posibleIdentificador, 100, 2, numeroDeLinea);
                case 1:
                    return analisisLexico(posibleIdentificador, 63, 2, numeroDeLinea);
                case 0:
                    return analisisLexico(posibleIdentificador, 62, 2, numeroDeLinea);
            }
        }
        if (0 <= "abcdefghijklmnñopqrstuvwxyz".indexOf(posibleIdentificador.charAt(0)) && posibleIdentificador.length() < 6) {
            return analisisLexico(posibleIdentificador, 61, 2, numeroDeLinea);
        }

        return null;
    }


    private static boolean constanteStringCorrectamenteFormulada(String constante) {
        if (constante.charAt(0) == '\"' && constante.charAt(constante.length() - 1) == '\"') {
            constante = constante.replaceAll(" ","");
            String caracterEnLaIteracionActual = "";
            int i = 0;
            {
                //Si el caracter actual fue una comilla, no es el último caracter en la constante y antes de él
                //no estaba un caracter de escape, la constante String está mal formada
                System.out.println(i + " " + constante.length() + " " + constante + " " + constante.charAt(i));
                if (constante.charAt(i) == '\\') {
                    if(constante.charAt(i+1) == '\"'){
                        if(i < constante.length()) {
                            i += 1;
                        } else {
                            return false;
                        }
                    }
                }
                i++;
            } while (i < constante.length());
            return true;
        }
        return false;
    }

    private static int tipoDeConstanteNumerica(String constante) {
        int cantidadDePuntosEnLaConstante = 0;
        for (int i = 0; i < constante.length(); i++) {
            //Si la constante tiene más de un punto, se puede considerar un error
            if (cantidadDePuntosEnLaConstante > 1) {
                return 2;
            }
            if (!Character.isDigit(constante.charAt(i))) {
                if (constante.charAt(i) == '.') {
                    cantidadDePuntosEnLaConstante++;
                } else {
                    //si comienza por un dígito pero contiene algo diferente a un dígito o punto, error
                    return 2;
                }
            }
        }
        if (cantidadDePuntosEnLaConstante == 1) {
            return 1;
        } else if (cantidadDePuntosEnLaConstante == 0) {
            return 0;
        }
        return 2;
    }

    /**
     * Recibe como parámetro un ArrayList y lo escribe a un archivo.
     * @param analisisLexico
     * @throws IOException
     */
    private static void escribirElResultadoAUnArchivo(ArrayList<String> analisisLexico) throws IOException {
        FileWriter writer = new FileWriter("C:/users/xtrs84zk/desktop/analisis.txt");
        //for (String str : analisisLexico) {
        for(int i = 0; i<analisisLexico.size(); i++) {
            if(analisisLexico.get(i) != null) {
                writer.write(analisisLexico.get(i));
            }
        }
        writer.close();
    }
}
