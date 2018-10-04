import javax.swing.*;

public class GramaticaDePrueba {
    public static void main(String[] args) {
        String gramatica = "a+b+(cc)+ U cba+  U bb";
        String expresion1 = "cbaaaaaaaaaaa"; //Sí
        JOptionPane.showMessageDialog(null, "Este applet verifica que la expresión ingresada pertenezca a " +
                "la gramática: a+b+(cc)+ U cba+  U bb.");
        try {
            do {
                expresion1 = JOptionPane.showInputDialog(null, "Ingrese expresión a verificar. \n" +
                        "Para salir, sólo presione cancelar.");
                if (expresion1.equals("null")) {
                    System.exit(0);
                }
                //Probando gramática
                try {
                    if (perteneceALaGramatica(expresion1)) {
                        JOptionPane.showMessageDialog(null, "La expresión " + expresion1 + " pertenece a la gramática.");
                    } else {
                        JOptionPane.showMessageDialog(null, "La expresión " + expresion1 + " no pertenece a la gramática.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "La expresión " + expresion1 + " no pertenece a la gramática.");
                }
            } while (true);
        } catch (Exception ex) {
            System.err.println("quéstapasando");
        }
    }


    private static boolean perteneceALaGramatica(String expresion) throws Exception {
        int i = 0;
        //Scenario A a+b+(cc)+
        if (expresion.charAt(i) == 'a') {
            //Si es una a, se sigue recorriendo la cadena hasta encontrar algo diferente
            while (expresion.charAt(++i) == 'a') ;
            //Si lo que sigue es diferente a una b, se rechaza
            if (expresion.charAt(i) == 'b') {
                //Después de la a, pueden venir una o más b
                while (expresion.charAt(++i) == 'b') ;
                //Luego de esa serie de b's, deben venir letras c en pares
                if (expresion.charAt(i) == 'c') {
                    while (i < expresion.length()) {
                        if (expresion.charAt(i) == 'c' && expresion.charAt(i + 1) == 'c') {
                            i += 2;
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
                //si ha llegado hasta aquí, la cadena respeta la gramática original
                return true;
            } else {
                return false;
            }
        }
        //Si la expresión comienza por una 'b', se considera el escenario B
        //Scenario B
        if (expresion.charAt(i) == 'b' && expresion.charAt(++i) == 'b') {
            if (i == expresion.length() - 1) {
                return true;
            }
        }
        //Si comienza por algo distinto de a, se considera el escenario C
        //Scenario C
        //La siguiente letra debe ser una b
        if (expresion.charAt(0) == 'c' && expresion.charAt(1) == 'b') {
            //La siguiente letra debe ser una o más a
            if (expresion.charAt(i = 2) == 'a') {
                //Mientras no haya terminado de responder la expresión
                while (i < expresion.length()) {
                    if (expresion.charAt(i) != 'a') {
                        return false;
                    }
                    i++;
                }
                //Se ha llegado hasta aquí, la expresión es correcta.
                return true;
            }
        }
        //Si no respeta escenario A, B o C; se descarta inmediatamente
        return false;
    }
}