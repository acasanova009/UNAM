package mx.unam.ciencias.edd;

/**
 * Clase para fabricar generadores de huellas digitales.
 */
public class FabricaHuellasDigitales<T> {

    /**
     * Identificador para fabricar la huella digital de Bob
     * Jenkins para cadenas.
     */
    public static final int BJ_STRING   = 0;
    /**
     * Identificador para fabricar la huella digital de GLib para
     * cadenas.
     */
    public static final int GLIB_STRING = 1;
    /**
     * Identificador para fabricar la huella digital de XOR para
     * cadenas.
     */
    public static final int XOR_STRING  = 2;

    /**
     * Regresa una instancia de {@link HuellaDigital} para cadenas.
     * @param identificador el identificador del tipo de huella
     *        digital que se desea.
     * @throws IllegalArgumentException si recibe un identificador
     *         no reconocido.
     */
    public static HuellaDigital<String> getInstanciaString(int identificador) {
        
        HuellaDigital<String> v = null;
        
        switch(identificador){
            case BJ_STRING: v = new HuellaDigitalBobJenkins<String>();
                break;
            case GLIB_STRING: v = new HuellaDigitalGLib<String>();
                break;
            case XOR_STRING: v = new HuellaDigitalXor<String>();
                break;
            default:
                throw new IllegalArgumentException();
        }
        
        return v;
    }
}
