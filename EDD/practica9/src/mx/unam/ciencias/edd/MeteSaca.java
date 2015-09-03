package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase abtracta para estructuras lineales restringidas a
 * operaciones mete/saca/mira, todas ocupando una lista subyaciente.
 */
public abstract class MeteSaca<T> {

    /* Clase Nodo protegida para uso interno de sus clases
     * herederas. */
    protected class Nodo<T> {
        public T elemento;
        public Nodo<T> siguiente;

        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Primer elemento de la estructura. */
    protected Nodo<T> cabeza;
    /* Último elemento de la lista. */
    protected Nodo<T> rabo;
    /* Número de elementos en la estructura. */
    protected int elementos;

    /**
     * Agrega un elemento al extremo de la estructura.
     * @param elemento el elemento a agregar.
     */
    public abstract void mete(T elemento);

    /**
     * Elimina el elemento en un extremo de la estructura y lo
     * regresa.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T saca() {
        // Aquí va su código.
        T cabezon = null;
        if (!esVacia())
        {
            elementos--;
            cabezon =cabeza.elemento;
            if (cabeza == rabo)
                cabeza = rabo = null;
            else
                cabeza = cabeza.siguiente;
            
        }else
        {
            throw new NoSuchElementException("la estructura esta vacia");
        }
        return cabezon;
        
    }

    /**
     * Nos permite ver el elemento en un extremo de la estructura,
     * sin sacarlo de la misma.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T mira() {

        T cabezon = null;
        if (!esVacia())
        {
            cabezon =cabeza.elemento;
        }else
        {
            throw new NoSuchElementException("la estructura esta vacia");
        }
        return cabezon;
        
    }

    /**
     * Nos dice si la estructura está vacía.
     * @return <tt>true</tt> si la estructura no tiene elementos,
     *         <tt>false</tt> en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        boolean isEmpty = false;
        if (cabeza == null && rabo == null)
            isEmpty = true;
        return isEmpty;
        
        
    }
}
