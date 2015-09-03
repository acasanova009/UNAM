package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * Interfaz para iteradores de lista. Un iterador de lista se puede
 * visualizar como que está siempre entre dos elementos de la lista,
 * o antes del primero, o después del último.
 */
public interface IteradorLista<T> extends Iterator<T> {

    /**
     * Nos dice si hay un elemento anterior. El método debe regresar
     * <tt>true</tt>, excepto cuando la lista esté vacía, o el
     * iterador esté antes del primer elemento.
     * @return <tt>true</tt> si el iterador tiene un elemento a su
     *         izquierda, <tt>false</tt> en otro caso.
     */
    public boolean hasPrevious();

    /**
     * Regresa el elemento a la izquierda del iterador, y lo mueve a
     * la izquierda.
     * @throws NoSuchElementException si el iterador no tiene
     *         elemento a la izquierda.
     */
    public T previous();

    /**
     * Mueve el iterador a la izquierda del primer elemento. Después
     * de llamar este método, el método {@link Iterator#hasNext} siempre
     * regresa <tt>true</tt> si la lista no es vacía.
     */
    public void start();

    /**
     * Mueve el iterador a la derecha del último elemento. Después
     * de llamar este método, el método {@link IteradorLista#hasPrevious} siempre
     * regresa <tt>true</tt> si la lista no es vacía.
     */
    public void end();
}
