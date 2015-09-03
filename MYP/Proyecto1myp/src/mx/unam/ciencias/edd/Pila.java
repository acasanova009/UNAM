package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     */
    @Override public void mete(T elemento) {
        Nodo<T> nodo = new Nodo<T>(elemento);
        if (rabo == null) {
            cabeza = rabo = nodo;
        } else {
            nodo.siguiente = cabeza;
            cabeza = nodo;
        }
        elementos++;
    }
}
