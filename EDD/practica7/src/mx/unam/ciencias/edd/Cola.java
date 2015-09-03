package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     */
    @Override public void mete(T elemento) {
        super.elementos++;
        
        MeteSaca<T>.Nodo<T> m = new MeteSaca<T>.Nodo<T>(elemento);
        if (this.rabo == null)
        {
            this.rabo = m;
            this.cabeza = this.rabo;
        
        }else
        {
            this.rabo.siguiente = m;
            this.rabo = m;
        }
        // Aquí va su código.
    }
}
