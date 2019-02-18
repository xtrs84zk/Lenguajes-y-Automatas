import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class AssemblyTest {
    private static int acumulador = 0;
    private static Scanner s = new Scanner(System.in);
    private static ArrayList constantes = new ArrayList();
    private static String tokens[];
    public static void main(String[] args) {
        tokens = new String[11];
        int[] memory = new int[12];
        fillTokens();
        fillMemory(memory);
        ArrayList<String> codigoPorLineas;
        try {
            codigoPorLineas = cargarCodigoDesdeUnArchivoDeTexto();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("No hay archivo de código, no puede continuar.");
            return;
        }
        detectarConstantes(codigoPorLineas);
        try {
            for (int i = 0; i < codigoPorLineas.size(); i++) {
                analizarLineaDeCodigo(codigoPorLineas.get(i), tokens, memory);
            }
        } catch (Exception e){
            System.err.println("Error ");
            e.printStackTrace();
        } finally {
            System.out.println(memory.toString());
        }
        try{
            String [] temporal = new String[1];
            temporal[0] = memory.toString();
            escribirElResultadoAUnArchivo(temporal);
        }catch (Exception e){
            System.err.println("Error en el manejo de archivo." + e.getMessage());
        }
    }

    /**
     * Analiza una línea de código (?
     * @param linea Línea que se analizará.
     * @param tokens dirección de memoria que contiene la tabla de tokens
     * @param memory arreglo que contiene las posiciones de memoria.
     */
    private static void analizarLineaDeCodigo(String linea, String[] tokens,int[] memory){
        String[] expr = linea.split("\t");
        int tokenActual;
        int apuntador = 0;
        if(expr.length > 0){
            for(int i = 0; i<expr.length; i++) {
                tokenActual = getToken(expr[i]);
                switch (tokenActual) {
                    case -1:
                        System.err.println("token desconocido " + expr[apuntador]);
                        break;
                    case 1:
                        System.out.println("Entrada de usuario:");
                        acumulador = s.nextInt();
                        break;
                    case 2:
                        System.out.println(acumulador);
                        break;
                    case 3:
                        acumulador = getLd(memory, expr[apuntador + 1]);
                        break;
                    case 4:
                        memory[apuntador] = acumulador;
                        break;
                    case 5:
                        acumulador = acumulador + memory[apuntador];
                        break;
                    case 6:
                        acumulador = acumulador - memory[apuntador];
                        break;
                    case 7:
                        if (acumulador >= 0) {
                            //brinca a la posición m (?
                        }
                        break;
                    case 8:
                        if (acumulador == 0) {
                            //brinca a la posición M (?
                        }
                        break;
                    case 9: //brinca a la posición M (?
                        break;
                    case 10: //detener (? se detuvo
                        break;
                    default:
                        System.err.println("Error desconocido.");
                }
            }
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
        memory[3] = 8007; //id M
        memory[4] = 05011; //st M
        memory[5] = 04011; //add M
        memory[6] = 9002; //sub M
        memory[7] = 03011; //jpos M
        memory[8] = 02000; //jz M
        memory[9] = 10000; //j M
        memory[10] = 00000; //haLt
        memory[11] = 00000;
    }

    /**
     * Inicializa la tabla de tokens con los valores predeterminados.
     */
    private static void fillTokens() {
        tokens[1] = "get"; //Lee el número de la entrada de usuario al acumulador
        tokens[2] = "put"; //Escribe en la salida el contenido del acumulador
        tokens[3] = "ld"; //Pone en el acumulador el contenido de la memoria
        tokens[4] = "st"; //Guarda el contenido del acumulador en la memoria
        tokens[5] = "add"; //Suma el contenido de la memoria M al acumulador
        tokens[6] = "sub"; //Substrae el contenido de la memoria M al acumulador
        tokens[7] = "jpos"; //Brinca a la psición M si el acumulador es psitivo
        tokens[8] = "jz"; //Brinca a la psición M si el acumulador es cero
        tokens[9] = "j"; //Brinca a la posición M
        tokens[10] = "halt"; //detiene ejecución
    }

    /**
     * Busca la posición en que se encuentra el objeto a cargar y regresa su contenido.
     * @param memory
     * @param token
     * @return
     */
    private static int getLd(int[] memory, String token){
        int contenido;
        contenido = getMemory(memory,getToken(token));
        if(contenido == -1){
            contenido = Integer.parseInt((String)constantes.get(contiene(constantes,token)));
        }
        return contenido;
    }

    /**
     * Analiza el código proporcionado en busca de constantes declaradas en el mismo.
     * @param codigoPorLineas que es el código a analizar.
     */
    private static void detectarConstantes(ArrayList<String> codigoPorLineas){
        if(codigoPorLineas != null){
            String lineaActual;
            String lineaActualSeparada[];
            int posiciónTemporal;
            ArrayList<String> listaTemporal = new ArrayList<>();
            for(int i = codigoPorLineas.size()-1; i >= 0 ; i--){
                lineaActual = codigoPorLineas.get(i);
                lineaActualSeparada = lineaActual.split("\t");
                posiciónTemporal = contiene(lineaActualSeparada,"const");
                if(posiciónTemporal != -1){
                    listaTemporal.add(lineaActualSeparada[posiciónTemporal -1]);
                    listaTemporal.add(lineaActualSeparada[posiciónTemporal +1]);
                    if(listaTemporal != null) {
                        constantes.add(listaTemporal);
                    }
                }
            }
        }
    }

    /**
     * Recorre un arreglo en busca de la expresión proporcionada.
     * @param arregloDondeBuscar
     * @param expresionABuscar
     * @return la posición donde lo encontró o -1 en caso de no hallarlo.
     */
    private static int contiene(String[] arregloDondeBuscar, String expresionABuscar){
        for(int i = 0; i<arregloDondeBuscar.length; i++){
            if(arregloDondeBuscar[i].compareTo(expresionABuscar) == 0){
                return i;
            }
        }
        return -1;
    }

    /**
     * Recorre una lista en busca de la expresión proporcionada.
     * @param listaDondeBuscar
     * @param expresionABuscar
     * @return
     */
    private static int contiene(ArrayList listaDondeBuscar, String expresionABuscar){
        for(int i = 0; i<listaDondeBuscar.size(); i++){
            String temp = listaDondeBuscar.get(i).toString();
            if(temp.compareTo(expresionABuscar) == 0){
                return i;
            }
        }
        return -1;
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
     * Analiza la lista de tokens en busca del token asignado.
     * @param token
     * @return Regresa la posición del token a buscar, -1 en caso de no encontrarlo
     */
    private static int getToken(String token) {
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
    private static void escribirElResultadoAUnArchivo(String[] codigoMaquina) throws IOException {
        FileWriter writer = new FileWriter("C:/users/xtrs84zk/desktop/codigo_maquina.txt");
        for (String acodigoMaquina : codigoMaquina) {
            if (acodigoMaquina != null) {
                writer.write(acodigoMaquina);
            }
        }
        writer.close();
    }

}
