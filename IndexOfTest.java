package aplicacion;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class IndexOfTest {

    public static void main (String [] args) throws IOException {
        String codigo = "";
        String t = " ";
        char letraInicial = t.charAt(0);
        String letras = "abcdefghijklmnñopqrstuvwxyz";
        if(0 <= letras.indexOf(letraInicial)){
            System.out.println("t comienza con una letra: " + letraInicial);
        } else {
            System.out.println("t comienza con algo que no es una letra: " + letraInicial);
        }
        System.out.println("El índice de k es " + t.indexOf('k'));
        System.out.println("");



        //prueba del manejo de archivos
        /**
        codigo = "El código comienza aquí \n";
        File archivoDelCodigo = new File("C:/users/xtrs84zk/desktop/codigo.txt");
        InputStreamReader input = null;
                try{
                input = new InputStreamReader(new FileInputStream(archivoDelCodigo), "UTF8");
        } catch(Exception e){

                    System.err.println("El archivo con el código no fue encontrado.");
        }
        if(input != null) {
            String r = "";
            BufferedReader in = new BufferedReader(input);
            while ((r = in.readLine()) != null) {
                codigo += r + "\n";
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(codigo);
        }
         **/

        //prueba del split
        String[] expresiones = codigo.split(Pattern.quote("\n"));
        String contenidoDelCodigo = "";
        for(int i = 0; i<expresiones.length; i++){
            contenidoDelCodigo += "N°" + i + " " + expresiones[i] + "\n";
        }
        System.out.println(contenidoDelCodigo);

        //manejo de archivos a una lista para evitar el primer split
        File archivoDelCodigo = new File("C:/users/xtrs84zk/desktop/codigo.txt");
        InputStreamReader input = null;
        try{
            input = new InputStreamReader(new FileInputStream(archivoDelCodigo), "UTF8");
        } catch(Exception e){

            System.err.println("El archivo con el código no fue encontrado.");
        }
        if(input != null) {
            ArrayList<String> codigoPorLineas =new ArrayList<String>();
            String r;
            BufferedReader in = new BufferedReader(input);
            while ((r = in.readLine()) != null) {
                codigoPorLineas.add(r + "\n");
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(codigoPorLineas.get(700));
        }
    }
}
