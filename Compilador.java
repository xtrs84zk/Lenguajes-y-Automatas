import java.io.*;
import java.util.StringTokenizer;

public class Compilador {
    private static String[][] tokens = new String[12][2];
    private static boolean a = false;
    public static void main(String[] args) {
        BufferedReader br;
        fillTokens();
        int cantidadDeLineas;
        try {
            br = new BufferedReader(new FileReader("C:/users/xtrs84zk/desktop/codigo.txt"));
            cantidadDeLineas = 0;
            while (br.ready()) {
               br.readLine();
                cantidadDeLineas++;
            }
            br = new BufferedReader(new FileReader("C:/users/xtrs84zk/desktop/codigo.txt"));
            procesar(cantidadDeLineas, br);
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void procesar(int cantidadDeLineas, BufferedReader cr) {
        String k;
        String[][] matriz = new String[cantidadDeLineas][5];
        boolean w, t;
        int j = 0;
        try {
            while (cr.ready()) {
                String s1 = cr.readLine();
                StringTokenizer st = new StringTokenizer(s1);
                int count = st.countTokens();
                matriz[j][0] = String.valueOf(j);

                if (count == 1) {
                    k = st.nextToken();
                    String bb = opCodeNumeric(k);
                    matriz[j][2] = k;
                    matriz[j][3] = bb;
                } else if (count == 2) {
                    k = st.nextToken();
                    String s3 = st.nextToken();

                    w = opCode(k);
                    if (w) {
                        String bb = opCodeNumeric(k);
                        matriz[j][2] = k;
                        matriz[j][3] = bb;
                    } else {
                        w = etiqueta(k);
                        if (w) {
                            matriz[j][1] = k;
                        }
                    }

                    t = opCode(s3);
                    if (t) {
                        String bb = opCodeNumeric(s3);
                        matriz[j][2] = s3;
                        matriz[j][3] = bb;
                    } else {
                        matriz[j][4] = s3;
                    }


                } else if (count == 3) {
                    k = st.nextToken();
                    String s3 = st.nextToken();
                    String s4 = st.nextToken();
                    String bb = opCodeNumeric(s3);

                    matriz[j][1] = k;
                    matriz[j][2] = s3;
                    matriz[j][3] = bb;
                    matriz[j][4] = s4;

                }
                j++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        String a1 = "";
        String a2 = "";
        String a3 = "";
        String[] posicion = new String[cantidadDeLineas];
        for (int i = 0; i < cantidadDeLineas; i++) {
            for (int z = 0; z < cantidadDeLineas; z++) {
                a1 = matriz[i][4];
                a2 = matriz[z][1];
                if (a2 != null) {
                    if (a1 != null) {
                        if (a1.equals(a2)) {
                            a3 = matriz[z][0];
                            posicion[i] = a3;
                        }
                    } else if (a1 == null) {
                        posicion[i] = "000";
                    }
                } else if (a1 == null)
                    posicion[i] = "000";
                if (posicion[i] == null) {
                    posicion[i] = "000";
                }

            }
        }
        try {
            escribirElResultadoAUnArchivo(posicion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa la tabla de tokens con los valores predeterminados.
     */
    private static void fillTokens(){
        tokens[0][0] = "01";
        tokens[0][1] = "get";
        tokens[1][0] = "02";
        tokens[1][1] = "out";
        tokens[2][0] = "03";
        tokens[2][1] = "id";
        tokens[3][0] = "04";
        tokens[3][1] = "st";
        tokens[4][0] = "05";
        tokens[4][1] = "add";
        tokens[5][0] = "06";
        tokens[5][1] = "sub";
        tokens[6][0] = "07";
        tokens[6][1] = "jpes";
        tokens[7][0] = "08";
        tokens[7][1] = "jz";
        tokens[8][0] = "09";
        tokens[8][1] = "j";
        tokens[9][0] = "10";
        tokens[9][1] = "halt";
        tokens[10][0] = "00";
        tokens[10][1] = "const";
        tokens[11][0] = "12";
        tokens[11][1] = "div";
    }

    private static boolean opCode(String s) {
        for (String[] token : tokens) {
            if (s.equals(token[1])) {
                return true;
            }
        }
        return false;
    }

    private static String opCodeNumeric(String s) {
        for (String[] token : tokens) {
            if (s.equals(token[1])) {
                return token[0];
            }
        }
        return "";
    }

    private static boolean etiqueta(String s) {
        if (s.equals("loop") || s.equals("done") || s.equals("zero") || s.equals("sum") || s.equals("sum2")) {
            a = true;
        }
        return a;
    }

    /**
     * Recibe como parámetro un ArrayList y lo escribe a un archivo.
     *
     * @param codigoMaquina que contiene la información a escribir.
     * @throws IOException en caso de no poder escribir al archivo.
     */
    private static void escribirElResultadoAUnArchivo(String[] codigoMaquina) throws IOException {
        FileWriter writer = new FileWriter("C:/users/xtrs84zk/desktop/codigo_maquina.txt");
        for (int i = 0; i < codigoMaquina.length; i++) {
            String acodigoMaquina = codigoMaquina[i];
            if (acodigoMaquina != null) {
                writer.write(i+1 + " " +acodigoMaquina + " \n");
            }
        }
        writer.close();

        //Llama al intérprete con el archivo generado.
        Interprete i2 = new Interprete();
        i2.procesarArchivo("C:/users/xtrs84zk/desktop/codigo_maquina.txt");
    }
}

