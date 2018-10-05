import java.util.ArrayList;
import java.util.Stack;

public class VCI {
    //variable global donde estará el vector.
    private static String vector = "";

    public static void main(String[] args) {
        String expresion = " a = 6 + 29 + 6 * ( 4 / 3 ) ; "; //Pensaré la manera de introducir los valores después
        obtenerVector(expresion);
        System.out.println(vector);
    }

    private static void obtenerVector(String expresion) {
        int errorCount = 0;
        Stack operadores = new Stack();
        ArrayList<String> operador_prioridad;
        String[] constantes = expresion.split(" ");
        for (int i = 0; i < constantes.length; i++) {
            System.out.println(vector);
            int prioridad = obtenerPrioridad(constantes[i]);
            switch (prioridad) {
                case -1:
                    System.err.println("Error.");
                case -2:
                    //Se agrega directamente al vector
                    vector = vector + constantes[i] + " | ";
                    break;
                case -3:
                    //Se ha encontrado un paréntesis que cierra, vaciar la pila hasta encontrar uno que abra
                    while (!operadores.isEmpty()) {
                        ArrayList temp = (ArrayList) operadores.peek();
                        if (temp.get(1) != "(") {
                            temp = (ArrayList) operadores.pop();
                            vector = vector + temp.get(1) + " | ";
                        } else {
                            operadores.pop();
                            break;
                        }
                    }
                    break;
                default:
                    int prioridadDelObjetoEnLaPila = 0;
                    operador_prioridad = new ArrayList<String>();
                    operador_prioridad.add(0, String.valueOf(prioridad));
                    operador_prioridad.add(1, constantes[i]);
                    System.out.println(operador_prioridad);
                    //Si la pila de operadores no está vacía, se procede a comparar
                    if (!operadores.isEmpty()) {
                        System.err.println("La pila no está vacía.");
                        ArrayList objetoEnElTopeDeLaPila = (ArrayList) operadores.peek();
                        //Por alguna razón que ahora parece lógica, esto está mal /* Creo que ya no */
                        if (objetoEnElTopeDeLaPila.size() != 0) {
                            System.err.println(objetoEnElTopeDeLaPila.get(0) + " | " + objetoEnElTopeDeLaPila.get(1));
                            prioridadDelObjetoEnLaPila = Integer.parseInt((String) objetoEnElTopeDeLaPila.get(0));
                            if (prioridadDelObjetoEnLaPila < prioridad) {
                                System.out.println("Pila con menor prioridad");
                                operadores.push(operador_prioridad);
                            } else {
                                System.out.println("Pila con mayor prioridad");
                                //La pila no está vacía y el objeto contenido posee mayor o igual prioridad
                                while (prioridadDelObjetoEnLaPila < prioridad) {
                                    prioridadDelObjetoEnLaPila = Integer.parseInt((String) objetoEnElTopeDeLaPila.get(0));
                                    objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                                    vector = vector + objetoEnElTopeDeLaPila.get(1) + " | ";
                                    objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                                    if (objetoEnElTopeDeLaPila.size() == 0) {
                                        //no hay más objetos en la pila sortOf
                                        break;
                                    }
                                }
                            }
                        } else {
                            System.err.println("Se inserta " + operador_prioridad + " a la pila.");
                            //Si la pila de operadores está vacía, se procede a insertar directamente
                            operadores.push(operador_prioridad);
                        }
                    } else {
                        System.err.println("Se inserta " + operador_prioridad + " a la pila.");
                        //Si la pila de operadores está vacía, se procede a insertar directamente
                        operadores.push(operador_prioridad);
                    }
                    break;
            }
        }// end for
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
