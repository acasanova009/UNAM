package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de
 * la lista, eliminar elementos de la lista, comprobar si un
 * elemento está o no en la lista, y otras operaciones básicas.</p>
 *
 * <p>Las instancias de la clase Lista implementan la interfaz
 * {@link Iterator}, por lo que el recorrerlas es muy sencillo:</p>
 *
<pre>
    for (String s : l)
        System.out.println(s);
</pre>
 *
 * <p>Además, se le puede pedir a una lista una instancia de {@link
 * IteradorLista} para recorrerla en ambas direcciones.</p>
 */
public class Lista<T> implements Iterable<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo<T> {
        public T elemento;
        public Nodo<T> anterior;
        public Nodo<T> siguiente;

        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador<T> implements IteradorLista<T> {

        /* La lista a iterar. */
        Lista<T> lista;
        /* Elemento anterior. */
        private Lista<T>.Nodo<T> anterior;
        /* Elemento siguiente. */
        private Lista<T>.Nodo<T> siguiente;

        /* El constructor recibe una lista para inicializar su
         * siguiente con la cabeza. */
        public Iterador(Lista<T> l) {
            // Aquí va su código.
            this.lista = l;
            start();
        }

        /* Existe un siguiente elemento, si el siguiente no es
         * nulo. */
        @Override public boolean hasNext() {
            boolean existe = false;
           if (this.siguiente != null)
               existe = true;
               return existe;
        }

        /* Regresa el elemento del siguiente, a menos que sea nulo,
         * en cuyo caso lanza la excepción
         * NoSuchElementException. */
        @Override public T next() {
            if (this.siguiente.elemento == null)
                throw new NoSuchElementException("El metodo next de la lista, dento de su sub clase iterdor no tiene elemento.");
            T t = this.siguiente.elemento;
            
            this.anterior = this.siguiente;
            this.siguiente = this.anterior.siguiente;
            
            return t;
        }

        /* Existe un elemento anterior, si el anterior no es
         * nulo. */
        @Override public boolean hasPrevious() {
            boolean existe = false;
            if (this.anterior != null)
                existe = true;
            
            return existe;
            // Aquí va su código.
        }

        /* Regresa el elemento del anterior, a menos que sea nulo,
         * en cuyo caso lanza la excepción
         * NoSuchElementException. */
        @Override public T previous() {
            if (this.anterior.elemento == null)
                throw new NoSuchElementException("El metodo next de la lista, dento de su sub clase iterdor no tiene elemento.");
            
            T t = this.anterior.elemento;
            
            this.siguiente = this.anterior;
            this.anterior = this.siguiente.anterior;
            return t;
            // Aquí va su código.
        }

        /* No implementamos el método remove(); sencillamente
         * lanzamos la excepción UnsupportedOperationException. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }

        /* Mueve el iterador al inicio de la lista; después de
         * llamar este método, y si la lista no es vacía, hasNext()
         * regresa verdadero y next() regresa el primer elemento. */
        @Override public void start() {
            
            
            this.siguiente = this.lista.cabeza;
            this.anterior = null;
            // Aquí va su código.
        }

        /* Mueve el iterador al final de la lista; después de llamar
         * este método, y si la lista no es vacía, hasPrevious()
         * regresa verdadero y previous() regresa el último
         * elemento. */
        @Override public void end() {
            
            this.anterior = this.lista.rabo;
            this.siguiente = null;
            // Aquí va su código.
        }
    }

    /* Primer elemento de la lista. */
    private Nodo<T> cabeza;
    /* Último elemento de la lista. */
    private Nodo<T> rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que
     * contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no
     * tiene elementos, el elemento a agregar será el primero y
     * último.
     * @param elemento el elemento a agregar.
     */
    public void agregaFinal(T elemento) {
        Nodo<T> n = new Nodo<T>(elemento);
        
        longitud++;
        
        if (rabo == null)
            cabeza = rabo = n;
        else
        {
            n.siguiente = null;
            n.anterior = rabo;
            rabo.siguiente = n;
            rabo = n;
        }
        
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no
     * tiene elementos, el elemento a agregar será el primero y
     * último.
     * @param elemento el elemento a agregar.
     */
    public void agregaInicio(T elemento) {
        
        Nodo<T> n = new Nodo<T>(elemento);
        longitud++;
        
        if (cabeza == null)
            cabeza = rabo = n;
        else
        {
            
            n.anterior = null;
            n.siguiente = cabeza;
            cabeza.anterior = n;
            cabeza =n;
        }
        
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está
     * contenido en la lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        elimina(cabeza , elemento);
        
    }
    private void elimina(Nodo<T> t, T elemento)
    {
        if (t==null)
            return;
        
        if(t.elemento.equals(elemento))
        {
            if(cabeza == rabo && t == cabeza)
            {
                cabeza = null;
                rabo = cabeza;
                
            }
            else if(t == cabeza)
            {
                cabeza = cabeza.siguiente;
                cabeza.anterior = null;
            }
            else if(t == rabo)
            {
                rabo = rabo.anterior;
                rabo.siguiente = null;
                
            }
            else
            {
                
                t.anterior.siguiente = t.siguiente;
                t.siguiente.anterior = t.anterior;
            }
            longitud-=1;

            
        }
        elimina(t.siguiente,elemento);
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero()  throws NoSuchElementException {
        T e;
        try
        {
            e = getPrimero();
            
        }catch(NoSuchElementException nsee)
        {
            throw nsee;
        }
        
        --longitud;
        
        if(cabeza == rabo){
            cabeza=rabo=null;
        }else{
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }
        
        return e;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() throws NoSuchElementException{
        
        
        T t;
        try
        {
        t = getUltimo();
        }catch(NoSuchElementException nsee)
        {
            throw new NoSuchElementException();
        }
        --longitud;
        if(cabeza == rabo)
        {
            limpia();
        }
        else
        {
            rabo = rabo.anterior;
            rabo.siguiente = null;
            
        }
        return t;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la
     * lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        return contiene(cabeza,elemento);
        
    }
    private boolean contiene(Nodo<T> t, T elemento){
        if (t==null)
            return false;
        if(t.elemento.equals(elemento))
            return true;
        return contiene(t.siguiente,elemento);
    }
    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar
     *         el método.
     */
    public Lista<T> reversa() {
        return reversa(cabeza,new Lista<T>());
    }
    private Lista<T> reversa(Nodo<T> t, Lista<T> lista){
        if (t==null)
            return lista;
        lista.agregaInicio(t.elemento);
        return reversa(t.siguiente,lista);
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos
     * elementos que la lista que manda llamar el método, en el
     * mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        return copia(cabeza,new Lista<T>());
    }
    private Lista<T> copia(Nodo<T> t, Lista<T> lista){
        if (t==null)
            return lista;
        lista.agregaFinal(t.elemento);
        
        
        return copia(t.siguiente,lista);
        
    }

    /**
     * Limpia la lista de elementos. El llamar este método es
     * equivalente a eliminar todos los elementos de la lista.
     */
    public void limpia() {
        cabeza = rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() throws NoSuchElementException {
        if (cabeza == null)
            throw new NoSuchElementException("getPrimero elem, this cabeza es null.");
        return this.cabeza.elemento;
        
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() throws NoSuchElementException{
        if (this.rabo == null)
            throw new NoSuchElementException("getUltimo elemento, this.rabo es null");
            return this.rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, si
     *         <em>i</em> es mayor o igual que cero y menor que el
     *         número de elementos en la lista.
     * @throws ExcepcionIndiceInvalido si el índice recibido es
     *         menor que cero, o mayor que el número de elementos en
     *         la lista menos uno.
     */
    public T get(int i) {
        
        if (i<0 || i>=longitud)
            throw new ExcepcionIndiceInvalido();
        return get(i,0,cabeza);
    }
    private  T get(int i,int c, Nodo<T> t){
        if (c < i)
            return get(i, ++c, t.siguiente);
        return t.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        
        return indiceDe(cabeza,elemento,0);
    }
    
    private int indiceDe(Nodo<T> t,T elemento,int c)
    {
        if (t== null)
            return -1;
        if(t.elemento.equals(elemento))
            return c;
        return indiceDe(t.siguiente,elemento,++c);
    }

   

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        return toString(cabeza,"");
    }
    private String toString(Nodo<T> t,String s)
    {
        if (t ==null)
            return s;
        
        
        if (t.anterior == null)
            s+="[";
        if (t.siguiente == null)
            s +=String.format("%s]", t.elemento);
        else
            s += String.format("%s, ", t.elemento);
        
        return toString(t.siguiente,s);
    }

    /**
     * Regresa un iterador para recorrer la lista.
     * @return un iterador para recorrer la lista.
     */
    @Override public Iterator<T> iterator() {
        return iteradorLista();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas
     * direcciones.
     * @return un iterador para recorrer la lista en ambas
     * direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador<T>(this);
    }
    
    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto
     *         recibido; <tt>false</tt> en otro caso.
     */
    
    /* Método auxiliar recursivo para equals. */
    private boolean nodosIguales(Nodo<T> n, Nodo<T> m) {
        if (n == null && m == null)
            return true;
        if (n == null || m == null)
            return false;
        return n.elemento.equals(m.elemento) &&
        nodosIguales(n.siguiente, m.siguiente);
    }
    
    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto
     *         recibido; <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
        return nodosIguales(cabeza, lista.cabeza);
    }
    
//    public boolean equals(Object obj)
//    {
//        boolean isEqual= false;
//        if ((obj instanceof Lista)&& obj !=null)
//        {
//            @SuppressWarnings("unchecked")  Lista<T> l2 = (Lista<T>)obj;
//            
//            Nodo<T> t = cabeza;
//            
//            l2.primero();
//            
//            while (l2.iteradorValido() && t!=null)
//            {
//                if(!l2.dame().equals(t.elemento))
//                    break;
//                if(t.siguiente == null)
//                {
//                    l2.siguiente();
//                    if (l2.dame() == null)
//                    {
//                        isEqual = true;
//                        break;
//                    }
//                }
//                l2.siguiente();
//                t=t.siguiente;
//            }
//        }
//        return isEqual;
//    }
    
    
    /**
     * Regresa una copia de la lista recibida, pero ordenada. La
     * lista recibida tiene que contener nada más elementos que
     * implementan la interfaz {@link Comparable}.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    
    //PARTE DE MERGESORT VIEJO
    
    //        l.primero();
    //        for(int i =0; i<d ;i++)
    //        {
    //            li.agregaFinal(l.dame());
    //            l.siguiente();
    //        }
    
    //        for (int i = (int)d; i < l.longitud; i++)
    //        {
    //            ld.agregaFinal(l.dame());
    //            l.siguiente();
    //        }
    //        l.primero();
    
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> l)
    {
        if(l.longitud<2)
            return l.copia();
        
        Lista<T> li = new Lista<T>(),ld= new Lista<T>();
        int d = l.longitud/2;
        
        int i=0;
        for(T e:l)
        {
            if(i<d)
                li.agregaFinal(e);
            else
                ld.agregaFinal(e);
            i++;
        }
        
        
        li = mergeSort(li);
        ld = mergeSort(ld);
        
        return mezcla(li,ld);
    }
    
    private static <T extends Comparable<T>>
    Lista<T> mezcla(Lista<T> a,Lista<T> b)
    {
        Lista<T> l = new Lista<T>();
        Lista<T>.Nodo<T> na = a.cabeza;
        Lista<T>.Nodo<T> nb = b.cabeza;
        
        while (na != null && nb != null)
        {
            
            if(na.elemento.compareTo(nb.elemento)<0)
            {
                l.agregaFinal(na.elemento);
                na = na.siguiente;
            }
            else
            {
                l.agregaFinal(nb.elemento);
                nb = nb.siguiente;
            }
        }
        while(na != null)
        {
            l.agregaFinal(na.elemento);
            na=na.siguiente;
        }
        while(nb != null)
        {
            l.agregaFinal(nb.elemento);
            nb=nb.siguiente;
        }
        
        
        return l;
    }
    /**
     * Busca un elemento en una lista ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la
     * interfaz {@link Comparable}, y se da por hecho que está
     * ordenada.
     * @param l la lista donde se buscará.
     * @param e el elemento a buscar.
     * @return <tt>true</tt> si e está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> l, T e)
    {
        
        return l.contiene(e);
    }
    
    public T getAny()
    {
        if(longitud==0)
            return null;
        else
            return getUltimo();
    }

}
