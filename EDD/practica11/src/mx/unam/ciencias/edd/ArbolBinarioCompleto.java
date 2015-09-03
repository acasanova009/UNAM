package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal
 * forma que el árbol siempre es lo más cercano posible a estar
 * lleno.<p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios
     * completos. */
    private class Iterador<T> implements Iterator<T> {

        private Cola<ArbolBinario<T>.Vertice<T>> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador(ArbolBinario<T>.Vertice<T> raiz) {
            
            cola = new Cola<ArbolBinario<T>.Vertice<T>>();
            Cola<ArbolBinario<T>.Vertice<T>> q = new Cola<ArbolBinario<T>.Vertice<T>>();
            
            if (raiz != null)
                q.mete(raiz);
                while(!q.esVacia())
                {
                    ArbolBinario<T>.Vertice<T> cVer = q.saca();
                    cola.mete(cVer);
                    
                    if(cVer.hayIzquierdo())
                    {
                        q.mete(cVer.izquierdo);
                    }
                    if(cVer.hayDerecho())
                    {
                        q.mete(cVer.derecho);
                    }
                }
            }
        

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return !cola.esVacia();
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            ArbolBinario<T>.Vertice<T> ver = cola.saca();
            return ver.elemento;
            // Aquí va su código.
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Sencillamente ejecuta el
     * constructor sin parámetros de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() {
        super(); }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo
     * elemento se coloca a la derecha del último nivel, o a la
     * izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @return un iterador que apunta al vértice del árbol que
     *         contiene el elemento.
     */
    @Override public VerticeArbolBinario<T> agrega(T elemento) {
        

        Cola<ArbolBinario<T>.Vertice<T>> q = new Cola<ArbolBinario<T>.Vertice<T>>();
        ArbolBinario<T>.Vertice<T> verElem = new ArbolBinario<T>.Vertice<T>(elemento);
        
        if (raiz == null)
            raiz = verElem;
        else
        {
            q.mete(raiz);
            while(!q.esVacia())
            {
                ArbolBinario<T>.Vertice<T> cVer = q.saca();
            if(cVer.hayIzquierdo())
            {
                q.mete(cVer.izquierdo);
            }
            if(cVer.hayDerecho())
            {
                q.mete(cVer.derecho);
            }
            
                if (cVer.hayIzquierdo() && !cVer.hayDerecho())
                {
                    
                    cVer.derecho = verElem;
                    verElem.padre = cVer;
                    break;
                }
                else if (!cVer.hayIzquierdo() && !cVer.hayDerecho())
                {
                    
                    cVer.izquierdo = verElem;
                    verElem.padre = cVer;
                    break;
                }
            }
        }
        elementos++;
        return verElem;
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia
     * lugares con el último elemento del árbol al recorrerlo por
     * BFS, y entonces es eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        
        ArbolBinario<T>.Vertice<T> lastV = getLastElement();
        ArbolBinario<T>.Vertice<T> eraseV = busquedaBFS(elemento);
        
        if (eraseV != null)
        {
            elementos--;
            if (lastV == eraseV)
            {
                if (eraseV == raiz)
                {
                    raiz = null;
                }
                else
                {
                    if (lastV.padre.izquierdo == lastV)
                        lastV.padre.izquierdo = null;
                    else
                        lastV.padre.derecho = null;
                
                }
            }
            else
            {
                
                    eraseV.elemento = lastV.elemento;
                if (lastV.padre.izquierdo == lastV)
                    lastV.padre.izquierdo = null;
                else
                    lastV.padre.derecho = null;
            }
        }
    }
    private ArbolBinario<T>.Vertice<T> busquedaBFS(T e){
        ArbolBinario<T>.Vertice<T> eraseElem = null;
        Cola<ArbolBinario<T>.Vertice<T>> q = new Cola<ArbolBinario<T>.Vertice<T>>();
        if (raiz == null)
            return eraseElem;
        
        else
        {
            q.mete(raiz);
            while(!q.esVacia())
            {
                ArbolBinario<T>.Vertice<T> cVer = q.saca();
                
                if(cVer.elemento.equals(e))
                {
                    eraseElem = cVer;
                    break;
                }
                
                if(cVer.hayIzquierdo())
                {
                    q.mete(cVer.izquierdo);
                }
                if(cVer.hayDerecho())
                {
                    q.mete(cVer.derecho);
                }
               
            }
        }
        
        return eraseElem;
        
    }
    private ArbolBinario<T>.Vertice<T> getLastElement(){
        ArbolBinario<T>.Vertice<T> lastElem = null;
        Cola<ArbolBinario<T>.Vertice<T>> q = new Cola<ArbolBinario<T>.Vertice<T>>();
        if (raiz == null)
            return lastElem;
        
        else
        {
            q.mete(raiz);
            while(!q.esVacia())
            {
                ArbolBinario<T>.Vertice<T> cVer = q.saca();
                
                if(cVer.hayIzquierdo())
                {
                    q.mete(cVer.izquierdo);
                }
                if(cVer.hayDerecho())
                {
                    q.mete(cVer.derecho);
                }
                if (!cVer.hayIzquierdo() && !cVer.hayDerecho() && q.esVacia())
                {
                    lastElem = cVer;
                    break;
                }
            }
        }
        
        return lastElem;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera
     * en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
//        return new ArbolBinarioCompleto<T>.Iterador<T>(this.raiz);
        return new Iterador<T>(raiz);
    }
}
