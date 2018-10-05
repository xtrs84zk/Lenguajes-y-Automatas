import javax.swing.*;
import java.util.ArrayList;
import java.util.Stack;

public class VCI {
    //variable global donde estará el vector.
    private static String vector;

    public static void main(String[] args) {
        String expresion;  //Variable donde se almacenará la expresión.
        expresion = JOptionPane.showInputDialog(null, "Introduzca la expresión");
        vector = "| "; //Se inicializa el vector
        obtenerVector(expresion); //Se obtiene el vector
        JOptionPane.showMessageDialog(null, "El vector de código intermedio es: \n" + vector);
    }

    private static void obtenerVector(String expresion) {
        Stack operadores = new Stack();
        ArrayList<String> operador_prioridad;
        ArrayList objetoEnElTopeDeLaPila;
        String[] constantes = expresion.split(" ");
        for (int i = 0; i < constantes.length; i++) {
            int prioridad = obtenerPrioridad(constantes[i]);
            switch (prioridad) {
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
                        if (temp.get(1) != "(") {
                            temp = (ArrayList) operadores.pop();
                            vector = vector + temp.get(1) + " | ";
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
                    int prioridadDelObjetoEnLaPila = 0;
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
                                while (prioridadDelObjetoEnLaPila >= prioridad && !operadores.isEmpty()) {
                                    prioridadDelObjetoEnLaPila = Integer.parseInt((String) objetoEnElTopeDeLaPila.get(0));
                                    objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                                    vector = vector + objetoEnElTopeDeLaPila.get(1) + " | ";
                                    if (!operadores.isEmpty()) {
                                        objetoEnElTopeDeLaPila = (ArrayList) operadores.pop();
                                        if (objetoEnElTopeDeLaPila.size() == 0) {
                                            //no hay más objetos en la pila sortOf
                                            break;
                                        }
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
                return -4;
            }
            //Regresa -2 si es una constante (no reconocido)
            return -2;
        }
        //Regresa -1 advirtiendo una cadena no inicializada.
        return -1;
    }
}
