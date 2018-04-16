import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Automata {
    public static void main(String[] args) {
        String tokens[] = new String[100];
        inicializarTokens(tokens);
        ArrayList<String> codigoPorLineas;
        ArrayList<String> analisisLexicoDeTodasLasLineas;
        String rutaDondeSeGuardaElArchivo = "C:/users/xtrs84zk/desktop/analisis.txt";
        try {
            codigoPorLineas = cargarCodigoDesdeUnArchivoDeTexto();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("No hay archivo de código, no puede continuar.");
            return;
        }
        System.out.println("El archivo de texto se ha cargado correctamente.");

        //Analizar línea por línea
        analisisLexicoDeTodasLasLineas = new ArrayList();
        int i = 0;
        try {
            do {
                analisisLexicoDeTodasLasLineas.addAll(identificarLexicoEnUnaLinea(codigoPorLineas.get(i), tokens, i));
                i++;
            } while (codigoPorLineas.get(i) != null);
        } catch (Exception e) {
            //Se espera que esta excepción siempre ocurra.
            System.err.print("");
        }
        System.out.println("Se ha completado el análisis.");
        //concatenar el análisis a un archivo
        try {
            escribirElResultadoAUnArchivo(analisisLexicoDeTodasLasLineas, rutaDondeSeGuardaElArchivo);
            System.out.println("El archivo se guardó satisfactoriamente en la ruta \"" + rutaDondeSeGuardaElArchivo + "\"");
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

    /**
     * Se encarga de leer un archivo previamente especifícado en codificación UTF-8
     *
     * @return ArrayList con el contenido del archivo
     * @throws IOException en caso de no poder acceder o leer el mismo.
     */
    private static ArrayList cargarCodigoDesdeUnArchivoDeTexto() throws IOException {
        ArrayList<String> codigoPorLineas;
        File archivoDelCodigo = new File("C:/users/xtrs84zk/desktop/codigo.txt");
        InputStreamReader input;
        input = new InputStreamReader(new FileInputStream(archivoDelCodigo), "UTF8");
        codigoPorLineas = new ArrayList<>();
        String r;
        BufferedReader in = new BufferedReader(input);
        //el código será leído en minúsculas
        while ((r = in.readLine()) != null) {
            codigoPorLineas.add(r.toLowerCase());
        }
        in.close();
        return codigoPorLineas;
    }

    /**
     * Hipotéticamente, este método inicializará el arreglo de tokens que contiene cada elemento definido
     * del lenguaje, su token está definido por la posición que ocupa en el arreglo.
     *
     * @param tokens referencia que funge para almacenar los token
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
        tokens[81] = "(";
        tokens[82] = ")";
        tokens[87] = ":=";

        //Carácteres especiales que no generan token
        //Pensando qué haré con ellos
        //String caracteresEspecialesQueNoGeneranToken[] = {"\"", ".", "BCO", "TAB", "EOLN", "EOF"};
    }


    /**
     * Analiza una línea que a la vez, partiendo de ella estudia fragmentos más pequeños, separados
     * por espacios en blanco. Si encuentra una comilla, comienza a verificar la cadena String.
     * De otra forma, busca identificadores o constantes. En caso de no encontrar similitud, se
     * define como un error y continua con la siguiente palabra.
     *
     * @param lineaAIdentificar que contiene una sola línea String
     * @param tokens            referencia donde se almacenan los tokens del lenguaje
     * @param numeroDeLinea     que es el número de la línea actual
     * @return un ArrayList que contiene los análisis uno a uno de cada elemento de la línea en cuestión
     */
    private static ArrayList identificarLexicoEnUnaLinea(String lineaAIdentificar, String[] tokens, int numeroDeLinea) {
        ArrayList<String> analisisDeLaLinea = new ArrayList<>();
        //La primer línea siempre debe venir vacía, cuando sea detectada, se ignorará.
        if (!lineaAIdentificar.contains("\uFEFF")) {
            //Estableciendo las expresiones
            String analisisDeLaIteracionActual;
            String expresiones[] = lineaAIdentificar.split(Pattern.quote(" "));
            for (int i = 0; i < expresiones.length; i++) {
                //Si alguna de las expresiones comienza por comillas, se procede a concatenar expresiones
                //mientras la expresión completa sea incorrecta, una vez que es correcta, se procede a analizar
                if (expresiones[i].charAt(0) == '\"') {
                    String posibleConstante = expresiones[i];
                    do {
                        i++;
                        //si la línea termina, la expresión se considera incorrecta.
                        if (i == expresiones.length) {
                            analisisDeLaLinea.add(analisisLexico(posibleConstante, 100, 1, numeroDeLinea));
                            //Agrega una línea vacía al final del análisis
                            analisisDeLaLinea.add("\r\n");
                            return analisisDeLaLinea;
                        }
                        posibleConstante += " " + expresiones[i];
                    } while (!constanteStringCorrectamenteFormulada(posibleConstante));
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
            //Agrega una línea vacía al final del análisis
            analisisDeLaLinea.add("\r\n");
        }
        return analisisDeLaLinea;
    }

    /**
     * Busca el identificador en un conjunto de tokens predefinido, en caso de encontrarlo,
     * se considera un elemento del lenguaje; regresa el análisis léxico que corresponde.
     *
     * @param posibleIdentificador a buscar su token
     * @param tokens               donde se buscará el identificador
     * @param numeroDeLinea        en que se encuentra
     * @return análisis de la línea o null en caso de no encontrarse entre los elementos
     */
    private static String analisisLexicoDeElementosDelLenguaje(String posibleIdentificador, String[] tokens, int numeroDeLinea) {
        int token = obtenerToken(tokens, posibleIdentificador);
        if (token != 100) {
            return analisisLexico(posibleIdentificador, token, 1, numeroDeLinea);
        }
        return null;
    }

    /**
     * Compone la línea de análisis con base en los parámetros que recibe.
     *
     * @param elemento       el elemento identificado
     * @param token          que le corresponde al elemento según el lenguaje
     * @param tipoDeElemento ya sea -1 o -2
     * @param numeroDeLinea  en que fue encontrado el elemento
     * @return una cadena preformada legible al usuario
     */
    private static String analisisLexico(String elemento, int token, int tipoDeElemento, int numeroDeLinea) {
        return "(" + elemento + ", -" + token + ", -" + tipoDeElemento + " ," + numeroDeLinea + " )" + "\r\n";
    }

    /**
     * Recibe un arreglo y la cadena que buscará en él. En caso de encontrarlo, regresa
     * su posición en el arreglo; de lo contrario, regresa -100 como mensaje de error.
     *
     * @param tokens               bloque que contiene los tokens del lenguaje
     * @param loQueDeberiaContener cadena que debería encontrase en dicho bloque
     * @return el número del token que corresponde al elemento
     */
    private static int obtenerToken(String[] tokens, String loQueDeberiaContener) {
        loQueDeberiaContener = loQueDeberiaContener.replaceAll("\\s", "");
        for (int i = 0; i < 100; i++) {
            if (tokens[i] != null) {
                if (tokens[i].equals(loQueDeberiaContener)) {
                    return i;
                }
            }
        }
        return 100;
    }

    /**
     * Define el tipo de identificador que recibió. En caso de encontrar dígito al inicio,
     * se procede a analizar una posible constante entera o real. Encontrando letras, un
     * identificador que puede tener, como máximo, seis caracteres.
     * Si lo anterior falla, regresa null.
     *
     * @param posibleIdentificador a analizar
     * @param numeroDeLinea        en que se encuentra
     * @return análisis léxico de el elemento
     */
    private static String analisisLexicoDeIdentificadores(String posibleIdentificador, int numeroDeLinea) {
        //Los identificadores inician con una letra y pueden seguir con letras o hasta seis caracteres.
        if (Character.isDigit(posibleIdentificador.charAt(0))) {
            //El elemento comienza con un número, posible constante.
            switch (tipoDeConstanteNumerica(posibleIdentificador)) {
                case 0:
                    return analisisLexico(posibleIdentificador, 62, 2, numeroDeLinea);
                case 1:
                    return analisisLexico(posibleIdentificador, 63, 2, numeroDeLinea);
                default:
                    return analisisLexico(posibleIdentificador, 100, 2, numeroDeLinea);
            }
        }
        if (0 <= "abcdefghijklmnñopqrstuvwxyz".indexOf(posibleIdentificador.charAt(0)) && posibleIdentificador.length() < 6) {
            return analisisLexico(posibleIdentificador, 61, 2, numeroDeLinea);
        }
        return null;
    }


    /**
     * Analiza una posible cadena String, si comienza con comillas y termina con las mismas, se puede asumir
     * que quizá está correctamente formulada y procede un análisis más minucioso; en caso contrario, se
     * descarta inmediatamente.
     *
     * @param constante a analizar
     * @return true o false dependiendo de el estado de la cadena
     */
    private static boolean constanteStringCorrectamenteFormulada(String constante) {
        constante = constante.replaceAll(" ", "");
        if (constante.charAt(0) == '\"' && constante.charAt(constante.length() - 1) == '\"') {
            int i = 1;
            do {
                i++;
                //Si el caracter actual fue una comilla, no es el último caracter en la constante y antes de él
                //no estaba un caracter de escape, la constante String está mal formada
                if (constante.charAt(i) == '\"') {
                    if (constante.charAt(i - 1) == '\\') {
                        if (i < constante.length()) {
                            i += 1;
                        } else {
                            return false;
                        }
                    } else {
                        return i >= constante.length() - 1;
                    }
                }
            } while (i < constante.length());
            return true;
        }
        return false;
    }

    /**
     * Analiza la constante recibida para detectar su composición, en caso de tener
     * sólo un punto, es correcta-real; en caso de contener más de uno, es un error.
     * Si no contiene puntos, es una constante entera.
     * Además, analiza que no haya caracteres desconocidos (aparte de números)
     *
     * @param constante almacena la cadena a verificar.
     * @return retorna la cantidad de puntos que contiene la constante o 2, en caso de error.
     */
    private static int tipoDeConstanteNumerica(String constante) {
        int cantidadDePuntosEnLaConstante = 0;
        for (int i = 0; i < constante.length(); i++) {

            //Si la constante tiene más de un punto, se considera un error.
            if (cantidadDePuntosEnLaConstante > 1) {
                return 2;
            }
            //Se encontró algo que no es un dígito
            if (!Character.isDigit(constante.charAt(i))) {
                //Se encontró un punto
                if (constante.charAt(i) == '.') {
                    //El punto está al final de la cadena, se considera un error.
                    if (i == constante.length() - 1) {
                        return 2;
                    }
                    cantidadDePuntosEnLaConstante++;
                } else {
                    //si contiene algo diferente a un dígito o punto, error
                    return 2;
                }
            }
        }

        //Se terminó de analizar la cadena sin errores
        return cantidadDePuntosEnLaConstante;

    }

    /**
     * Recibe como parámetro un ArrayList y lo escribe a un archivo.
     *
     * @param analisisLexico que contiene la información a escribir.
     * @throws IOException en caso de no poder escribir al archivo.
     */
    private static void escribirElResultadoAUnArchivo(ArrayList<String> analisisLexico, String rutaDelArchivo) throws IOException {
        FileWriter writer = new FileWriter(rutaDelArchivo);
        for (String anAnalisisLexico : analisisLexico) {
            if (anAnalisisLexico != null) {
                writer.write(anAnalisisLexico);
            }
        }
        writer.close();
    }
}
