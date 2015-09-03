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
        super.elementos++;
        
        MeteSaca<T>.Nodo<T> m = new MeteSaca<T>.Nodo<T>(elemento);
        if (this.cabeza == null)
        {
            this.cabeza = m;
            this.rabo = this.cabeza;
            
        }else
        {
            m.siguiente = this.cabeza;
            this.cabeza = m;
        }
    }
}
