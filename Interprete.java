public class Interprete {

    public static void main (String [] args) {
        int[] memoria = new int[0];
        Interprete c3 = new Interprete();
                c3.inter(memoria);

    }

    private void inter(int[] memoria){
        int acum = 0;
        int indice = 0;
        int code =0;
        int addr = 0;
        memoria = new int[0];
        while(indice >= 0) {
            code = memoria[indice] / 1000;
            addr = memoria[indice++] %1000;
        }
        switch (code){
            case 1: //leer del teclado.
                    break;
            case 2: System.out.println(acum);
                break;
            case 3: acum = memoria[addr];
                break;
            case 4: memoria[addr] = acum;
                break;
            case 5: acum += memoria[addr];
                break;
            case 6: acum -= memoria[addr];
                break;
            case 7: if(acum > 0) indice = addr;
                break;
            case 8: if(acum == 0) indice = addr;
                break;
            case 9: indice = addr;
                break;
            case 10: indice = 1;
                break;
            default: indice = 1;
        }
    }
}
