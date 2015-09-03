package mx.unam.ciencias.edd;

/**
 * Interfaz genérica para huellas digitales.
 */
public interface HuellaDigital<T> {

    /**
     * Calcula la huella digital del objeto recibido.
     * @param objeto el objeto del que queremos la huella digital.
     * @return la huella digital del objeto recibido.
     */
    public int huellaDigital(T objeto);
}
