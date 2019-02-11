import java.io.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class AssemblyTest {

    public static void main(String[] args) {
        String tokens[] = new String[10];
        int[] memory = new int[12];
        fillTokens(tokens);
        fillMemory(memory);
        ArrayList<String> codigoPorLineas;
        String rutaDondeSeGuardaElArchivo = "C:/users/xtrs84zk/desktop/coodigo.txt";
        try {
            codigoPorLineas = cargarCodigoDesdeUnArchivoDeTexto();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("No hay archivo de código, no puede continuar.");
            return;
        }
    }

    /**
     * Inicializa el espacio de memoria con valores iniciales.
     *
     * @param memory la referencia a
     * @return void
     */
    private static void fillMemory(int[] memory) {
        memory[0] = 03010;
        memory[1] = 04011; //get
        memory[2] = 01000; //put
        memory[3] = 03004; //id M
        memory[4] = 05011; //st M
        memory[5] = 04011; //add M
        memory[6] = 9002; //sub M
        memory[7] = 03011; //jpos M
        memory[8] = 02000; //jz M
        memory[9] = 10000; //j M
        memory[10] = 00000; //haLt
        memory[11] = 00000;
    }




    private static void fillTokens(String[] tokens) {
        tokens[1] = "get"; //Lee el número de la entrada de usuario al acumulador
        tokens[2] = "put"; //Escribe en la salida el contenido del acumulador
        tokens[3] = "ld M"; //Pone en el acumulador el contenido de la memoria
        tokens[4] = "st M"; //Guarda el contenido del acumulador en la memoria
        tokens[5] = "add M"; //Suma el contenido de la memoria M al acumulador
        tokens[6] = "sub M"; //Substrae el contenido de la memoria M al acumulador
        tokens[7] = "jpos M"; //Brinca a la psición M si el acumulador es psitivo
        tokens[8] = "jz M"; //Brinca a la psición M si el acumulador es cero
        tokens[9] = "j M"; //Brinca a la posición M
        tokens[10] = "halt"; //detiene ejecución
    }

    /**
     * Método get para los segmentos de memoria.
     * @param memory
     * @param token
     * @return
     */
    private static int getMemory(int[] memory, int token){
        if(token > memory.length || token < 0){ //Regresa un mensaje de error si el token buscado no existe.
            return -1;
        }
        return memory[token];
    }

    /**
     * @param tokens
     * @param token
     * @return Regresa la posición del token a buscar, -1 en caso de no encontrarlo
     */
    private static int getToken(String[] tokens, String token) {
        if (token == null) {
            return -1;
        }
        return IntStream.range(1, tokens.length).filter(i -> tokens[i].equals(token)).findFirst().orElse(-1);
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
     * Recibe como parámetro un ArrayList y lo escribe a un archivo.
     *
     * @param codigoMaquina que contiene la información a escribir.
     * @throws IOException en caso de no poder escribir al archivo.
     */
    private static void escribirElResultadoAUnArchivo(ArrayList<String> codigoMaquina, String rutaDelArchivo) throws IOException {
        FileWriter writer = new FileWriter(rutaDelArchivo);
        for (String acodigoMaquina : codigoMaquina) {
            if (acodigoMaquina != null) {
                writer.write(acodigoMaquina);
            }
        }
        writer.close();
    }

}
