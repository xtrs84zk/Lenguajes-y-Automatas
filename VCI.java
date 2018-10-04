import java.util.ArrayList;
import java.util.Stack;

public class VCI {
    //variable global donde estará el vector.
    private static String vector = "";

    public static void main(String[] args) {
        String expresion = "a = 6 + 29 + 6 * ( 4 / 3 ) ; "; //Pensaré la manera de introducir los valores después
        String vci = obtenerVector(expresion);
        System.out.println(vector);
    }

    private static String obtenerVector(String expresion) {
        int errorCount = 0;
        Stack operadores = new Stack();
        ArrayList<String> operador_prioridad;
        String[] constantes = expresion.split(" ");
        while (errorCount < 3) {
            for (int i = 0; i < constantes.length; i++) {
                System.out.println(vector);
                operador_prioridad = new ArrayList<String>();
                int prioridad = obtenerPrioridad(constantes[i]);
                if (prioridad == -2) {
                    //Se agrega directamente al vector
                    vector = vector + constantes[i] + " | ";
                } else if (prioridad == -3) {
                    //Se ha encontrado un paréntesis que cierra, vaciar la pila hasta encontrar uno que abra
                    while (!operadores.isEmpty()) {
                        ArrayList temp = (ArrayList) operadores.peek();
                        if (temp.get(1) != "(") {
                            temp = (ArrayList) operadores.pop();
                            vector = vector + temp.get(1) + " | ";
                        } else {
                            break;
                        }
                    }
                } else if (prioridad != -1) {
                    operador_prioridad.add(0, prioridad + "");
                    operador_prioridad.add(1, constantes[i]);
                } else {
                    errorCount++;
                    continue;
                }

                //Si la pila de operadores no está vacía, se procede a comparar
                if (!operadores.isEmpty()) {
                    ArrayList objetoEnElTopeDeLaPila = (ArrayList) operadores.peek();
                    if ((int) objetoEnElTopeDeLaPila.get(0) < prioridad) {
                        operadores.push(operador_prioridad);
                    } else {
                        //La pila no está vacía y el objeto contenido posee mayor o igual prioridad
                        while (!((int) objetoEnElTopeDeLaPila.get(0) < prioridad)) {
                            objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                            vector = vector + objetoEnElTopeDeLaPila.get(1) + " | ";
                            objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                            if (objetoEnElTopeDeLaPila == null) {
                                //no hay más objetos en la pila sortOf
                                break;
                            }
                        }
                    }
                } else {
                    //Si la pila de operadores está vacía, se procede a insertar directamente
                    operadores.push(operador_prioridad);
                }
            } // end for
        } // end while
        return "wtf";
    }

    /**
     * Determina la prioridad correspondiente al operador proporcionado.
     *
     * @param operador a procesar
     * @return prioridad correspondiente a dicho operador
     */
    private static int obtenerPrioridad(String operador) {
        if (operador != null) {
            if (operador.equals("*") || operador.equals("/")) {
                return 60;
            }
            if (operador.equals("+") || operador.equals("-")) {
                return 50;
            }
            if (operador.equals("==")) {
                return 40;
            }
            if (operador.equals("not")) {
                return 30;
            }
            if (operador.equals("and")) {
                return 20;
            }
            if (operador.equals("or")) {
                return 10;
            }
            if (operador.equals("=") || operador.equals("(")) {
                return 0;
            }
            if (operador.equals(")")) {
                //regresará -3 cuando se trate de un paréntesis que cierra
                return -3;
            }
            //Regresa -2 si es una constante (no reconocido)
            return -2;
        }
        //Regresa -1 advirtiendo una cadena no inicializada.
        return -1;
    }
}
