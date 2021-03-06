import javax.swing.*;
import java.util.ArrayList;
import java.util.Stack;

public class VCI {
    //variable global donde estará el vector.
    private static String vector;

    public static void main(String[] args) {
        String expresion;  //Variable donde se almacenará la expresión.
        while (true) {
            //Se le pedirá al usuario introducir la expresión cuantas veces quiera
            expresion = JOptionPane.showInputDialog(null, "Introduzca la expresión");
            if (expresion == null) {
                //El usuario puede detenerlo presionando cancelar.
                return;
            }
            vector = "| "; //Se inicializa el vector
            obtenerVector(expresion); //Se obtiene el vector
            JOptionPane.showMessageDialog(null, "El vector de código intermedio es: \n" + vector);
        }
    }

    private static void obtenerVector(String expresion) {
        Stack operadores = new Stack();
        ArrayList<String> operador_prioridad;
        ArrayList objetoEnElTopeDeLaPila;
        String[] constantes = expresion.split(" ");
        for (int i = 0; i < constantes.length; i++) {
            int prioridad = obtenerPrioridad(constantes[i]);
            switch (prioridad) {
                case 0:
                    operador_prioridad = new ArrayList<String>();
                    operador_prioridad.add(0, String.valueOf(prioridad));
                    operador_prioridad.add(1, constantes[i]);
                    operadores.push(operador_prioridad);
                    break;
                case -1:
                    System.err.println("Error.");
                    break;
                case -2:
                    //Se agrega directamente al vector
                    vector = vector + constantes[i] + " | ";
                    break;
                case -3:
                    //Se ha encontrado un paréntesis que cierra, vaciar la pila hasta encontrar uno que abra
                    while (!operadores.isEmpty()) {
                        ArrayList temp = (ArrayList) operadores.peek();
                        String temporal = (String) temp.get(1);
                        System.err.println(temp.get(1));
                        if (!temporal.equals("(")) {
                            operadores.pop();
                            vector = vector + temporal + " | ";
                        } else {
                            operadores.pop();
                            break;
                        }
                    }
                    break;
                case -4:
                    //Se ha encontrado un ';', la pila debe vaciarse aquí por completo
                    while (!operadores.isEmpty()) {
                        objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                        vector = vector + objetoEnElTopeDeLaPila.get(1) + " | ";
                    }
                    vector = vector + constantes[i] + " | ";
                    break;

                default:
                    int prioridadDelObjetoEnLaPila;
                    operador_prioridad = new ArrayList<String>();
                    operador_prioridad.add(0, String.valueOf(prioridad));
                    operador_prioridad.add(1, constantes[i]);
                    //Si la pila de operadores no está vacía, se procede a comparar
                    if (!operadores.isEmpty()) {
                        objetoEnElTopeDeLaPila = (ArrayList) operadores.peek();
                        //Por alguna razón que ahora parece lógica, esto está mal /* Creo que ya no */
                        if (objetoEnElTopeDeLaPila.size() != 0) {
                            prioridadDelObjetoEnLaPila = Integer.parseInt((String) objetoEnElTopeDeLaPila.get(0));
                            if (prioridadDelObjetoEnLaPila < prioridad) {
                                operadores.push(operador_prioridad);
                            } else {
                                //La pila no está vacía y el objeto contenido posee mayor o igual prioridad
                                while (!operadores.isEmpty() && prioridad < prioridadDelObjetoEnLaPila) {
                                    prioridadDelObjetoEnLaPila = Integer.parseInt((String) objetoEnElTopeDeLaPila.get(0));
                                    objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                                    String objetoStringEETDP = (String) objetoEnElTopeDeLaPila.get(1);
                                    if (!objetoStringEETDP.equals("(")) {
                                        vector = vector + objetoEnElTopeDeLaPila.get(1) + " | ";
                                    }
                                }
                            }
                        } else {
                            //Si la pila de operadores está vacía, se procede a insertar directamente
                            operadores.push(operador_prioridad);
                        }
                    } else {
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
            if (operador.equals(";")) {
                //Regresará -4 en caso de un punto y coma
                return -4;
            }
            //Regresa -2 si es una constante (no reconocido)
            return -2;
        }
        //Regresa -1 advirtiendo una cadena no inicializada.
        return -1;
    }
}
