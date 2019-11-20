import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class expo {
    public static void main(String[]args){
        try {
            ArrayList<String> code = cargarCodigoDesdeUnArchivoDeTexto();
            ArrayList<String> list = searchForTheInteger(code);
            sumarNumeros(list);

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo.");
        }
    }

    private static void sumarNumeros(ArrayList<String> p){
        int suma = 0;
        int k = 0;
        while (k<p.size()){
            System.out.println("Se detectó la variable num" + (k+1) + " con el valor: " + p.get(k));
            suma += Integer.parseInt(p.get(k));
            k++;
        }
        System.out.print("La suma es: ");
        System.out.print(suma);
    }

    private static ArrayList<String>  searchForTheInteger(ArrayList<String> codigo){
        String temp;
        String [] temporal;
        String p1 = "";
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i<codigo.size(); i++){
            temp = codigo.get(i);
            temporal = temp.split(" ");
            if(temporal[0].equals("var")){
                temporal = temp.split("=");
                int k = 0;
                while(k<temporal[1].length()){
                    if(isNumeric("" +temporal[1].charAt(k))) {
                        p1 += temporal[1].charAt(k);
                    }
                    k++;
                }
                list.add(p1);
                p1 = "";
            }
        }
        return list;
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * Se encarga de leer un archivo previamente especifícado en codificación UTF-8
     *
     * @return ArrayList con el contenido del archivo
     * @throws IOException en caso de no poder acceder o leer el mismo.
     */
    private static ArrayList cargarCodigoDesdeUnArchivoDeTexto() throws IOException {
        ArrayList<String> codigoPorLineas;
        File archivoDelCodigo = new File("./test1.txt");
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
}
