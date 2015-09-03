package mx.unam.ciencias.icc;

/**
 * <p>Clase para listas doblemente ligadas de cadenas.</p>
 *
 * <p>Las listas de cadenas nos permiten agregar elementos al inicio
 * o final de la lista, eliminar elementos de la lista, comprobar si
 * un elemento está o no en la lista, y otras operaciones
 * básicas.</p>
 *
 * <p>Las listas tienen un iterador para poder recorrerlas.</p>
 */
public class ListaCadena {

    /* Clase Nodo privada para uso interno de la clase ListaCadena. */
    private class Nodo {
        public String elemento;
        public Nodo anterior;
        public Nodo siguiente;

        public Nodo(String elemento) {
            this.elemento = elemento;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Nodo iterador. */
    private Nodo iterador;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que
     *         contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene
     * elementos, el elemento a agregar será el primero y
     * último. Después de llamar este método, el iterador apunta a
     * la cabeza de la lista.
     * @param elemento el elemento a agregar.
     */
    public void agregaFinal(String elemento) {
        Nodo n = new Nodo(elemento);
        
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
     * último. Después de llamar este método, el iterador apunta a
     * la cabeza de la lista.
     * @param elemento el elemento a agregar.
     */
    public void agregaInicio(String elemento) {
        
        Nodo n = new Nodo(elemento);
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
        
        iterador = cabeza;
        
    }
    
    

    /**
     * Elimina un elemento de la lista. Si el elemento no está
     * contenido en la lista, el método no la modifica. Si un
     * elemento de la lista es modificado, el iterador se mueve al
     * primer elemento.  -cr- -c-r-  -c-x-r -c-x-x-r
     * @param elemento el elemento a eliminar.
     */
    public void elimina(String elemento) {
        
        
            Nodo t =cabeza;

            while (t != null)
            {
            
                
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
                    iterador= cabeza;

                }
                
                t=t.siguiente;
            
            }
        

            return;
        

    }

    /**
     * Nos dice si un elemento está en la lista. El iterador no se
     * mueve.
     * @param elemento el elemento que queremos saber si está en la
     *        lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(String elemento) {
        
//        ListaCadena l = new ListaCadena();
        Nodo t = cabeza;
        while ( t != null)
        {
            if(t.elemento.equals(elemento))
                return true;
            
            t = t.siguiente;
        }
        return false;
        
        // Aquí va su código.
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar
     *         el método.
     */
    public ListaCadena reversa() {
        // Aquí va su código.
        ListaCadena l = new ListaCadena();
        Nodo t = cabeza;
        while ( t != null)
        {
            l.agregaInicio(t.elemento);
            t = t.siguiente;
        }
        return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos
     * elementos que la lista que manda llamar el método, en el
     * mismo orden.
     * @return una copiad de la lista.
     */
    public ListaCadena copia() {
        ListaCadena l = new ListaCadena();
        Nodo t = cabeza;
        while ( t != null)
        {
            l.agregaFinal(t.elemento);
            t = t.siguiente;
        }
        return l;
        // Aquí va su código.
    }

    /**
     * Limpia la lista de elementos. El llamar este método es
     * equivalente a eliminar todos los elementos de la lista.
     */
    public void limpia() {
        // Aquí va su código.
        cabeza = rabo = iterador = null;
        longitud = 0;
        
    
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista. Si el
     * índice es menor que cero o mayor o igual que el número de
     * elementos de la lista, el método regresa <tt>null</tt>.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, si
     *         <em>i</em> es mayor o igual que cero y menor que el
     *         número de elementos en la lista; <tt>null</tt> en
     *         otro caso.
     */
    public String get(int i) {
        // Aquí va su código.
        
        if (i<0 || i>=longitud)
            return null;
        int c =0;
        Nodo t = cabeza;
        while (c++ < i)
        {
            t=t.siguiente;
        }
        return t.elemento;
        
        
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(String elemento) {
        
        int c =0;
        Nodo t = cabeza;
        while ( t != null)
        {
            if (t.elemento.equals(elemento))
                return c;
            
            c++;
            t = t.siguiente;
            
        }
        return -1;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    public String toString() {
        
        String s = "[";
        for (int i = 0; i < longitud-1; i++)
            s += String.format("%s, ", this.get(i));
        s += String.format("%s]", this.get(longitud-1));
        
        return s;
        
    }

    /**
     * Mueve el iterador de la lista a su primer elemento.
     */
    public void primero() {
        iterador = cabeza;
        // Aquí va su código.
    }

    /**
     * Mueve el iterador de la lista a su último elemento.
     */
    public void ultimo() {
        iterador = rabo;
        // Aquí va su código.
    }

    /**
     * Mueve el iterador al siguiente elemento.
     */
    public void siguiente() {
        iterador = iterador.siguiente;
        // Aquí va su código.
    }

    /**
     * Mueve el iterador al elemento anterior.
     */
    public void anterior() {
        iterador =iterador.anterior;
        // Aquí va su código.
    }

    /**
     * Regresa el elemento al que el iterador apunta.
     * @return el elemento al que el iterador apunta, o
     *         <tt>null</tt> si el iterador es inválido.
     */
    public String get() {
        // Aquí va su código.
        if (iterador == null)
            return null;
        return iterador.elemento;
    }

    /**
     * Nos dice si el iterador es válido.
     * @return <tt>true</tt> si el iterador es válido;
     *         <tt>false</tt> en otro caso.
     */
    
    public boolean iteradorValido() {
        if(iterador == null)
            return false;
        else
        return true;
    }

    
}
