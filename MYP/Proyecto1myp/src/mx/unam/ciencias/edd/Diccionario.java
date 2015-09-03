package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario
 * generaliza el concepto de arreglo, permitiendo (en general,
 * dependiendo de qué tan buena sea su método para generar huellas
 * digitales) agregar, eliminar, y buscar valores en <i>O</i>(1) en
 * cada uno de estos casos.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador<V> implements Iterator<V> {

        /* En qué lista estamos. */
        private int indice;
        /* Diccionario. */
        private Diccionario<K,V> diccionario;
        /* Iterador auxiliar. */
        private Iterator<Diccionario<K,V>.Entrada<K,V>> iterador;

        private Lista<Diccionario<K,V>.Entrada<K,V>> lista;
        /* Construye un nuevo iterador, auxiliándose de las listas
         * del diccionario. */
        public Iterador(Diccionario<K,V> d) {
            diccionario=d;
            indice=0;
            lista = new Lista<Diccionario<K,V>.Entrada<K,V>>();
            iterador = lista.iterator();
            
            
            // Aquí va su código.
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            
            
            if(!iterador.hasNext())
            {
                
                while(indice < diccionario.entradas.length && diccionario.entradas[indice] == null )
                    indice++;
            
                if(indice < diccionario.entradas.length && diccionario.entradas[indice] != null )
                {
                    lista =diccionario.entradas[indice++];
                    iterador = lista.iterator();
                }
            }
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        public V next() {
            
            Diccionario<K,V>.Entrada<K,V> e = iterador.next();
            return e.valor;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Tamaño mínimo; decidido arbitrariamente a 2^7. */
    private static final int MIN_N = 264;

    /* Máscara para no usar módulo. */
    public int mascara;
    /* Huella digital. */
    private HuellaDigital<K> huella;
    /* Nuestro diccionario. */
    public Lista<Entrada<K, V>>[] entradas;
    /* Número de valores*/
    private int total;

    /* Clase para las entradas del diccionario. */
    private class Entrada<K, V> {
        
        public K key;
        public V valor;
        
        public Entrada(K k, V v)
        {
            key = k;
            valor = v;
            
        }
        public String toString()
        {
//            "K: "+key+
            return (            "K: "+key+ " V:"+valor );
        }
        // Aquí va su código.
    }

    /* Truco para crear un arreglo genérico. Es necesario hacerlo
       así por cómo Java implementa sus genéricos; de otra forma
       obtenemos advertencias del compilador. */
    @SuppressWarnings("unchecked") private Lista<Entrada<K, V>>[] nuevoArreglo(int n) {
        Lista[] arreglo = new Lista[n];
        return (Lista<Entrada<K, V>>[])arreglo;
    }

    /**
     * Construye un diccionario con un tamaño inicial y huella
     * digital predeterminados.
     */
    public Diccionario() {
        
        this(MIN_N);
        // Aquí va su código.
    }

    /**
     * Construye un diccionario con un tamaño inicial definido por
     * el usuario, y una huella digital predeterminada.
     * @param tam el tamaño a utilizar.
     */
    public Diccionario(int tam) {
        this(tam, null);

       
        
        // Aquí va su código.
    }

    /**
     * Construye un diccionario con un tamaño inicial
     * predeterminado, y una huella digital definida por el usuario.
     * @param huella la huella digital a utilizar.
     */
    public Diccionario(HuellaDigital<K> huella) {
        
        this(MIN_N, huella);
        
        // Aquí va su código.
    }

    /**
     * Construye un diccionario con un tamaño inicial, y un método
     * de huella digital definidos por el usuario.
     * @param tam el tamaño del diccionario.
     * @param huella la huella digital a utilizar.
     */
    public Diccionario(int tam, HuellaDigital<K> h) {

        huella = h;
        if(tam < MIN_N)
            tam = MIN_N;
        entradas= this.nuevoArreglo(tam);
        total = 0;
        mascara  =(int) Math.floor((Math.log(tam)/Math.log(2))) +1;
//         System.out.println("Mascara: "+mascara);
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave
     * proporcionada. Si la llave ya había sido utilizada antes para
     * agregar un valor, el diccionario reemplaza ese valor con el
     * recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     */

    public void agrega(K llave, V v) {
        
        
        
        int i  = indice(llave);
        
        
//        System.out.println("LLave: "+llave+" I: "+i);
        
        Lista<Entrada<K,V>> l  = getLista(i, true);
        Entrada<K,V> e = buscaEntrada(llave, l);
        if(e!=null)
            e.valor =v;
        else
        {
            
            e = new Entrada<K,V>(llave, v);
            l.agregaFinal(e);
            total++;
        }
        
        

        if(this.carga()>=MAXIMA_CARGA)
            creceArreglo();
    }

    private Entrada<K,V> buscaEntrada(K k, Lista<Entrada<K,V>> l)
    {
        
        Entrada<K,V> f = null;
        if(l!=null)
        for(Entrada<K,V> e: l)
            
            if(k.equals(e.key))
            {
                f = e;
                break;
            }
        
        return f;
    }
    //true boolean si quiero que se cree
    private Lista<Entrada<K,V>> getLista(int i, boolean b)
    {
        
        Lista<Entrada<K,V>> m = entradas[i];
        if(m==null && b){
            entradas[i] = new Lista<Entrada<K,V>>();
            m = entradas[i];
            
        }
        
        return m;
    }
    
    private void creceArreglo()
    {

        Lista<Entrada<K,V>>[] a = entradas;
        entradas =  nuevoArreglo(entradas.length*2);
        mascara++;
        
        for(Lista<Entrada<K, V>> e: a)
        {
            if(e==null)
                continue;
            
            for (Entrada<K, V> ent : e){
                
                int i  = indice(ent.key);
                Lista<Entrada<K,V>> l  = getLista(i, true);
                
                Entrada<K,V> ex = buscaEntrada(ent.key, l);
                if(ex !=null)
                    ex.valor =ent.valor;
                else
                    l.agregaFinal(ent);
            }
            
        }
    }
    private int indice(K key)
    {
        
        
        

        int n= 0;
        if(huella !=null)
            n = huella.huellaDigital(key);
        else
            n = key.hashCode();

//                System.out.println("HuellaINdex: "+n);
        if(n<0)
            n*=-1;
        return n>>(32-mascara);
    }
    /**
     * Regresa el valor del diccionario asociado a la llave
     * proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws <tt>NoSuchElementException</tt> si la llave no está
     *         en el diccionario.
     */
    public V get(K llave) throws NoSuchElementException {
        
        V  v= null;
        int i = indice(llave);
        
        
        Lista<Entrada<K,V>> l  = getLista(i, false);
        Entrada<K,V> e = buscaEntrada(llave, l);
        if(e!=null && llave.equals(e.key))
            v = e.valor;
        
        if(v==null)
            throw new NoSuchElementException("No esta esta llave");
        return v;
        // Aquí va su código.
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
        
        boolean found= false;
        
        int i = indice(llave);

        Lista<Entrada<K,V>> l  = getLista(i, false);
        Entrada<K,V> e = buscaEntrada(llave, l);
        if(e!=null && llave.equals(e.key))
            found = true;
        
        
        return found;
        // Aquí va su código.
    }

    /**
     * Elimina el valor del diccionario asociado a la llave
     * proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        
        
        int i = indice(llave);
        
        Lista<Entrada<K,V>> l  = getLista(i, false);
        Entrada<K,V> e = buscaEntrada(llave, l);
        
        if(e!=null)
        {
            total--;
            l.elimina(e);
        }
        else
            throw new NoSuchElementException("No esta esta llave");

        
    }

    /**
     * Regresa una lista con todas las llaves con valores asociados
     * en el diccionario. La lista no tiene ningún tipo de orden.
     * @return una lista con todas las llaves.
     */
    public Lista<K> llaves() {
        
        Lista<K> keys = new Lista<K>();
        
        for(Lista<Entrada<K, V>> e: entradas)
        {
            if(e==null)
                continue;
            for (Entrada<K, V> ent : e)
                keys.agregaInicio(ent.key);
        }
                
        
        return keys;
    }

    /**
     * Regresa una lista con todos los valores en el diccionario. La
     * lista no tiene ningún tipo de orden.
     * @return una lista con todos los valores.
     */
    public Lista<V> valores() {
        
        Lista<V> valores = new Lista<V>();
        
        for(Lista<Entrada<K, V>> e: entradas)
        {
            if(e==null)
                continue;
            for (Entrada<K, V> ent : e)
                valores.agregaInicio(ent.valor);
        }
        
        
        return valores;

        // Aquí va su código.
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave
     * que tenemos en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        
        
        int col = 0;
        
        for(Lista<Entrada<K, V>> e: entradas)
        {
            if(e==null)
                continue;
            
            if(e.getLongitud()>col)
            col=e.getLongitud();
        }

        return col-1;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int col = 0;
        
        for(Lista<Entrada<K, V>> e: entradas)
        {
            if(e==null)
                continue;
            
            col+=e.getLongitud()-1;
        }
        return col;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return (double)total/entradas.length;
        // Aquí va su código.
    }

    /**
     * Regresa el número de valores en el diccionario.
     * @return el número de valores en el diccionario.
     */
    public int getTotal() {
        return total;
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar los valores del
     * diccionario. El diccionario se itera sin ningún orden
     * específico.
     * @return un iterador para iterar el diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new Iterador<V>(this);
    }
    public String toString()
    
    {
        String s= "";

//        String s= ("**Diccionario; "
//                   +"\nObjetos:    "+total
//                   +"\nCarga:      "+carga()
//                   +"\nColisiones: "+colisiones()
//                   +"\nMaxima:     "+colisionMaxima()
//                   +"\n\n");
        for(Lista<Entrada<K, V>> e: entradas)
        {
            if(e==null)
            {
//                s+="[]\n";
                continue;}
            
            s += (e +"\n");
        }
        return s;
    }
}
