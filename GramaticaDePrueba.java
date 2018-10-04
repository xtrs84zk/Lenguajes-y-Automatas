public class GramaticaDePrueba {
    public static void main(String[] args) {
        String gramatica = "a+b+(cc)+ U cba+  U bb";
        String expresion1 = "abcccc"; //Sí

        //Probando gramática 1
        try {
            if (perteneceALaGramatica(expresion1)) {
                System.out.println("La expresión 1 pertenece a la gramática.");
            } else {
                System.out.println("La expresión 1 no pertenece a la gramática.");
            }
        } catch (Exception e) {
            System.out.println("La expresión 1 no pertenece a la gramática.");
        }
    }


    private static boolean perteneceALaGramatica(String expresion) throws Exception {
        int i = 0;
        //Scenario A a+b+(cc)+
        if (expresion.charAt(i) == 'a') {
            //Si es una a, se sigue recorriendo la cadena hasta encontrar algo diferente
            while (expresion.charAt(++i) == 'a') ;
            System.out.println(expresion.charAt(i) + "wtf");
            //Si lo que sigue es diferente a una b, se rechaza
            if (expresion.charAt(i) == 'b') {
                //Después de la a, pueden venir una o más b
                while (expresion.charAt(++i) == 'b') ;
                //Luego de esa serie de b's, deben venir letras c en pares
                if (expresion.charAt(i) == 'c') {
                    while (i < expresion.length() - 1) {
                        if (expresion.charAt(++i) == 'c' && expresion.charAt(++i) == 'c') {
                            if (i == expresion.length() - 1) {
                                return true;
                            }
                        } else {
                            //Si lo que está en esta posición no son dos c, la cadena no pertenece a la gramática
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
                //Mientras siga encontrando a's
                if (i < expresion.length() - 1) {
                    while (expresion.charAt(i) == 'a') {
                        if (i < expresion.length()) {
                            i++;
                        } else {
                            break;
                        }
                    }
                    if (i == expresion.length() - 1) {
                        return false;
                    }
                }
                return true;
            }
        }
        //Si no respeta escenario A o C, se descarta inmediatamente
        return false;
    }
}