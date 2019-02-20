import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Interprete {
    private static Scanner s;
    private static int acumulador;
    private static int[][] t;
    private static boolean continuarLaEjecucion = false;

    public static void main(String[] args) {
        procesarArchivo("C:/users/xtrs84zk/desktop/codigo_maquina.txt");
    }

    public static void procesarArchivo(String rutaDelArchivo) {
        s = new Scanner(System.in);
        int cantidadDeLineasEnElCodigo = 0;
        BufferedReader r;
        try {
            //Se lee el archivo una vez para definir la cantidad de líneas
            r = new BufferedReader(new FileReader(rutaDelArchivo));
            while (r.ready()) {
                r.readLine();
                cantidadDeLineasEnElCodigo++;
            }
            //Una vez se tiene la cantidad, se procede a crear un arreglo para insertar cada línea
            r = new BufferedReader(new FileReader(rutaDelArchivo));
            t = new int[cantidadDeLineasEnElCodigo][2];
            int i = 0;
            while (true) {
                if (!r.ready()) break;
                String ss2 = r.readLine();
                String o = ss2.substring(0, 2);
                o = o.replaceAll("\\s", "");
                int a = Integer.parseInt(o);
                String direccion = ss2.substring(2, 4);
                direccion = direccion.replaceAll("\\s", "");
                int b = Integer.parseInt(direccion);
                t[i][0] = a;
                t[i][1] = b;
                i++;
                continuarLaEjecucion = true;
            }
        } catch (FileNotFoundException e) {
            System.err.println("El archivo no se encontró.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println();
        }
        procesarLosDatos(cantidadDeLineasEnElCodigo);
    }

    private static void procesarLosDatos(int cantidadDeLineasEnElCodigo) {
        int temp;
        while (continuarLaEjecucion) {
            for (int numeroDeLineaActual = 0; numeroDeLineaActual < cantidadDeLineasEnElCodigo; numeroDeLineaActual++) {
                switch (t[numeroDeLineaActual][0]) {
                    case 1: //get
                        System.out.print("> ");
                        acumulador = Integer.parseInt(s.next());
                        System.out.println();
                        break;
                    case 2: //out
                        System.out.println("El resultado es: " + acumulador);
                        break;
                    case 3:
                        temp = t[numeroDeLineaActual][1];
                        acumulador = t[temp][1];
                        break;
                    case 4:
                        temp = t[numeroDeLineaActual][1];
                        t[temp][1] = acumulador;
                        break;
                    case 5:
                        temp = t[numeroDeLineaActual][1];
                        acumulador = t[temp][1] + acumulador;
                        break;
                    case 6:
                        temp = t[numeroDeLineaActual][1];
                        acumulador = acumulador - t[temp][1];
                        break;
                    case 7:
                        temp = t[numeroDeLineaActual][1];
                        if (acumulador > 0)
                            numeroDeLineaActual = temp - 1;
                        break;
                    case 8:
                        temp = t[numeroDeLineaActual][1];
                        if (acumulador == 0)
                            numeroDeLineaActual = temp - 1;
                        break;
                    case 9:
                        temp = t[numeroDeLineaActual][1];
                        numeroDeLineaActual = temp - 1;
                        break;
                    case 10:
                        continuarLaEjecucion = false;
                        break;
                    case 12:
                        temp = t[numeroDeLineaActual][1];
                        int dividendo = t[temp][1];
                        acumulador = acumulador / dividendo;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}