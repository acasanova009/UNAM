package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son
 * genéricos, pero acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos
 *       sus descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos
 *       sus descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios
     * ordenados. */
    private class Iterador<T> implements Iterator<T> {

        
        private Cola<ArbolBinario<T>.Vertice<T>> cola;
        
        
        public Iterador(ArbolBinario<T>.Vertice<T> raiz) {
            
            cola = new Cola<ArbolBinario<T>.Vertice<T>>();
            Cola<ArbolBinario<T>.Vertice<T>> q = new Cola<ArbolBinario<T>.Vertice<T>>();
            
            if (raiz != null)
                dfsQuick(raiz,cola);
           
        }
        
        private void dfsQuick(ArbolBinario<T>.Vertice<T> v, Cola<ArbolBinario<T>.Vertice<T>> cola)
        {
        
                    if(v.hayIzquierdo())
                        dfsQuick(v.izquierdo,cola);
                        cola.mete(v);
                    if(v.hayDerecho())
                        dfsQuick(v.derecho,cola);
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
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de un árbol
     * binario. El árbol binario ordenado tiene los mismos elementos
     * que el árbol recibido, pero ordenados.
     * @param arbol el árbol binario a partir del cuál creamos el
     *        árbol binario ordenado.
     */
    public ArbolBinarioOrdenado(ArbolBinario<T> arbol)
    {
        
//        for(T e : arbol)
//        this.agrega(e);
        
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden
     * in-order.
     * @param elemento el elemento a agregar.
     * @return el vértice que contiene al nuevo elemento.
     */
    @Override public VerticeArbolBinario<T> agrega(T elemento) {
        
        elementos++;
        
        ArbolBinario<T>.Vertice<T> vE = new ArbolBinario<T>.Vertice<T>(elemento);
        vE.color = Color.ROJO;
        if (raiz == null)
        {
            raiz = vE;
            return raiz;
        }
        else
            return vabAgrega(raiz, vE);
    }
    
    private VerticeArbolBinario<T> vabAgrega( ArbolBinario<T>.Vertice<T> v, ArbolBinario<T>.Vertice<T> vE)
    {
        if (v.elemento.compareTo(vE.elemento)>0)
        {
            if(v.hayIzquierdo())
                return vabAgrega(v.izquierdo,vE);
            v.izquierdo = vE;        }
        else
        {
            if (v.hayDerecho())
                return vabAgrega(v.derecho,vE);
            v.derecho = vE;
        }
        vE.padre = v;
        
        return vE;
    }


    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no
     * hace nada; si está varias veces, elimina el primero que
     * encuentre (in-order). El árbol conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
   
    @Override public void elimina(T e) {
        ArbolBinario<T>.Vertice<T> vAB = abBusca(e);
        if(vAB !=null)
        eliminaV(vAB);
    }

    private void eliminaV(ArbolBinario<T>.Vertice<T> vB)
    {

            if(vB.hayDerecho() && vB.hayIzquierdo())
            {
                

                Vertice<T> vMax = maximoEnSubarbol(vB.izquierdo);
                vB.elemento = vMax.elemento;
                eliminaV(vMax);
                return;
                
            }
            elementos--;
        
        
        
            if(vB.hayPadre())
            {
                conectaHijoConAbuelo(vB);
            
                
            }
            else
            {
                conectaHijoConLaRaiz(vB);
            }
                
    
        
    }
    protected void conectaHijoConAbuelo(Vertice<T> vB)
    {

            if (vB.hayIzquierdo() && !vB.hayDerecho()){
                vB.izquierdo.padre = vB.padre;
                if(vB == vB.padre.derecho)
                    vB.padre.derecho =vB.izquierdo;
                else
                    vB.padre.izquierdo =vB.izquierdo;
            }
            else if (!vB.hayIzquierdo() && vB.hayDerecho()){
                
                vB.derecho.padre = vB.padre;
                if(vB == vB.padre.derecho)
                    vB.padre.derecho =vB.derecho;
                else
                    vB.padre.izquierdo =vB.derecho;
            }
            else {
                if(vB == vB.padre.derecho)
                    vB.padre.derecho =null;
                else
                    vB.padre.izquierdo =null;
            }
        }
    protected void conectaHijoConLaRaiz(Vertice<T> vB)
    {
            if (vB.hayIzquierdo() && !vB.hayDerecho()){
                raiz = vB.izquierdo;
                raiz.padre = null;
            }else if (!vB.hayIzquierdo() && vB.hayDerecho()){
                raiz = vB.derecho;
                raiz.padre = null;
            }else
                raiz = null;
        }
        
    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo
     * encuentra, regresa el vértice que lo contiene; si no, regresa
     * <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
        
    
        private ArbolBinario<T>.Vertice<T> abBusca(T elemento) {
            
            
            if (raiz == null)
                return null;
            else
                return abBusca(raiz, elemento);
        }
        
        private ArbolBinario<T>.Vertice<T> abBusca(ArbolBinario<T>.Vertice<T> v,T e)
        {
            
            
            if (v.get().compareTo(e)==0)
                return v;
            
            if (v.get().compareTo(e)<0)
            {
                if (v.hayDerecho())
                    return abBusca(v.derecho,e);
            }
            else
            {
                if(v.hayIzquierdo())
                    return abBusca(v.izquierdo,e);
            }
            return null;
        }

        
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        

        if (raiz == null)
            return null;
        else
            return VABbusca(raiz, elemento);
    }

    private VerticeArbolBinario<T> VABbusca(ArbolBinario<T>.Vertice<T> v,T e)
    {
        
        
           if (v.get().compareTo(e)==0)
               return v;
            
            if (v.get().compareTo(e)<0)
            {
                if (v.hayDerecho())
                    return VABbusca(v.derecho,e);
            }
            else
            {
                    if(v.hayIzquierdo())
                        return VABbusca(v.izquierdo,e);
            }
            return null;
    }
    /**
     * Regresa el vértice máximo en el subárbol cuya raíz es el
     * vértice que recibe.
     * @param vertice el vértice raíz del subárbol del que queremos
     *                encontrar el máximo.
     * @return el vértice máximo el subárbol cuya raíz es el vértice
     *         que recibe.
     */
    protected Vertice<T> maximoEnSubarbol(Vertice<T> vertice) {
        Vertice<T> v = vertice;
        while(v!=null)
        {
        
            if (v.hayDerecho())
            v = v.derecho;
            else break;
            
        }
        return v;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera
     * en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(raiz);
    }
        
        /**
         * Gira el árbol a la derecha sobre el vértice recibido. Si el
         * vértice no tiene hijo izquierdo, el método no hace nada.
         * @param vertice el vértice sobre el que vamos a girar.
         */
        
    protected void miDerecha(Vertice<T> v)
    {
        if (v==null || !v.hayIzquierdo())
            return;
        Vertice<T> h,n,a;
        h=n=a=null;
        
        
        //Definimos h
        h = v.izquierdo;
        h.padre = null;
        
        //si hay padre de nuestro vertice
        if (v.hayPadre())
        {
            //Definimos a;
            a = v.padre;
            
            h.padre = a;
            
            if(v.padre.izquierdo==v)
                v.padre.izquierdo = h;
            else
                v.padre.derecho = h;
        }
        else
            
            raiz = h;
    
        //Casos seguros ya que tenemos las referencias anteriores.
        v.padre = null;
        v.izquierdo = null;
        
        //En caso de que haya nieto, o arbol derecho de nuestro hijo izquierdo.
        if(h.hayDerecho())
        {
            //Definimos n
            n=h.derecho;
            n.padre = v;
            v.izquierdo = n;
        }
        
        h.derecho = v;
        v.padre=h;
    }
        
    public void giraDerecha(VerticeArbolBinario<T> v) {
        
        miDerecha(vertice(v));
        
    }
        
        /**
         * Gira el árbol a la izquierda sobre el vértice recibido. Si el
         * vértice no tiene hijo derecho, el método no hace nada.
         * @param vertice el vértice sobre el que vamos a girar.
         */
        public void giraIzquierda(VerticeArbolBinario<T> v) {
            miIzquierda(vertice(v));
            // Aquí va su código.
            
            
    }
        protected void miIzquierda(Vertice<T> v)
        {
            if (v==null || !v.hayDerecho())
                return;
            
            Vertice<T> h,n,a;
            h=n=a=null;
            
            
            //Definimos h
            h = v.derecho;
            h.padre = null;
            
            //si hay padre de nuestro vertice
            if (v.hayPadre())
            {
                //Definimos a;
                a = v.padre;
                h.padre = a;
                if(v.padre.izquierdo==v)
                    v.padre.izquierdo = h;
                else
                    v.padre.derecho = h;
            }else
                raiz = h;
            
            
            
            //Casos seguros ya que tenemos las referencias anteriores.
            v.padre = null;
            v.derecho = null;
            
            //En caso de que haya nieto, o arbol derecho de nuestro hijo izquierdo.
            if(h.hayIzquierdo())
            {
                //Definimos n
                n=h.izquierdo;
                n.padre = v;
                v.derecho = n;
            }
            
            h.izquierdo = v;
            v.padre=h;
        }
}