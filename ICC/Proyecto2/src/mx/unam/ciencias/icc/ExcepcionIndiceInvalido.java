package mx.unam.ciencias.icc;

/**
 * Clase para excepciones de índices de lista inválidos.
 */
public class ExcepcionIndiceInvalido extends IndexOutOfBoundsException {

    /**
     * Constructor sin mensaje.
     */
    public ExcepcionIndiceInvalido() {
        super();
    }

    /**
     * Constructor con un mensaje.
     * @param mensaje el mensaje a mostrarle al usuario si la
     *                excepción es lanzada.
     */
    public ExcepcionIndiceInvalido(String mensaje) {
        super(mensaje);
    }
}
