package mx.unam.ciencias.edd;

/**
 * Clase para fabricar generadores de huellas digitales.
 */
public class HuellaDigitalGLib<T> implements HuellaDigital<T> {

    public int huellaDigital(T objeto){
        byte[] k = objeto.toString().getBytes();
        int h = 5381;
        for(int i = 0; i<k.length ; i++ )
        {
            byte c = k[i];
            h = h * 33+c;
        }
        
        return h;
    }
}
