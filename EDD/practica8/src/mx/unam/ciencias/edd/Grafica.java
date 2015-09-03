package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y
 * aristas, tales que las aristas son un subconjunto del producto
 * cruz de los vértices.
 */
public class Grafica<T> implements Iterable<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador<T> implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice<T>> it;

        /* Construye un nuevo iterador, auxiliándose de la lista de
         * vértices. */
        
        
        public Iterador(Grafica<T> grafica) {
            this.it = grafica.vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            return this.it.hasNext();
        }

        /* Regresa el siguiente elemento. */
        public T next() {
            return it.next().elemento;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Vertices para gráficas; implementan la interfaz
     * VerticeGrafica */
    private class Vertice<T> implements VerticeGrafica<T> {

        /* Iterador para los vecinos del vértice. */
        private class IteradorVecinos implements Iterator<VerticeGrafica<T>> {

            /* Iterador auxiliar. */
            private Iterator<Grafica<T>.Vertice<T>> it;

            /* Construye un nuevo iterador, auxiliándose de la lista
             * de vecinos. */
            public IteradorVecinos(Iterator<Grafica<T>.Vertice<T>> m) {
                
                it = m;

            }

            /* Nos dice si hay un siguiente vecino. */
            public boolean hasNext() {
                // Aquí va su código.
                return it.hasNext();
            }

            /* Regresa el siguiente vecino. La audición es
             * inevitable. */
            public VerticeGrafica<T> next() {
                // Aquí va su código.
                return it.next();
            }

            /* No lo implementamos: siempre lanza una excepción. */
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        public T elemento;
        public Color color;
        public Lista<Grafica<T>.Vertice<T>> vecinos;

        
        
        public void marcarNinguno()
        {
            this.color = Color.NINGUNO;
        }
        private boolean esVisible()
        {
            boolean esVisible =false;
            if(this.color == Color.NEGRO  )
                esVisible = true;
            return esVisible;
        }

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T e) {
            
            elemento = e;
            color = Color.NINGUNO;
            vecinos = new Lista<Grafica<T>.Vertice<T>>();
            // Aquí va su código.
        }

        /* Regresa el elemento del vértice. */
        public T getElemento() {
            // Aquí va su código.
            return elemento;
        }

        /* Regresa el grado del vértice. */
        public int getGrado() {
            // Aquí va su código.
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        public Color getColor() {
            return color;
            // Aquí va su código.
        }

        /* Define el color del vértice. */
        public void setColor(Color c) {
            // Aquí va su código.
            color = c;
        }

        /* Regresa un iterador para los vecinos. */
        public Iterator<VerticeGrafica<T>> iterator() {
            // Aquí va su código.
            return new IteradorVecinos(vecinos.iterator());
        }
        public String toString()
        {
//            String s= new String;
//            s
            if(this.esVisible())
                
                return ("@"+ elemento.toString() +" ");

                
            return (" "+ elemento.toString() +" ");
        }
    }

    /* Vértices. */
    private Lista<Vertice<T>> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
        vertices = new Lista<Vertice<T>>();
        aristas = 0;
    }

    /**
     * Regresa el número de vértices.
     * @return el número de vértices.
     */
    public int getVertices() {
        return vertices.getLongitud();
        // Aquí va su código.
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
        // Aquí va su código.
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido
     *         agregado a la gráfica.
     */
    public void agrega(T elemento) throws IllegalArgumentException {
        
        if(contiene(elemento))
                throw new IllegalArgumentException();
        
        vertices.agregaFinal(new Vertice<T>(elemento));
        
        
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben
     * estar en la gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     * @throws IllegalArgumentException si a o b ya están
     *         conectados, o si a es igual a b.
     */
    public void conecta(T a, T b) throws NoSuchElementException, IllegalArgumentException{

        
        
        if (a == b)
            throw new IllegalArgumentException();
        
        
        
        Vertice<T> vA = miVertice(a);
        Vertice<T> vB = miVertice(b);

        
        for(VerticeGrafica<T> c: vA)
        {
            if(c.getElemento()==b)
            {
                throw new IllegalArgumentException();
            }
        }
        
        if(!sonVecinos(a,b))
        {
            
            aristas++;
            vA.vecinos.agregaFinal(vB);
            vB.vecinos.agregaFinal(vA);
        }
        
       
        
    }
    
    private Vertice<T> miVertice(T a)
    {
        Vertice<T> ver = null;
        for(Vertice<T> vE :vertices)
            if(vE.elemento ==a )
                return vE;
        
        
        throw new NoSuchElementException();
        
    }
    private Vertice<T> cVertice(VerticeGrafica<T> vG) {
        /* Tenemos que suprimir advertencias. */
        @SuppressWarnings("unchecked") Vertice<T> n = (Vertice<T>)vG;
        return n;
    }

    
    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @throws NoSuchElementException si elemento no es elemento de
     *         la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    
    public VerticeGrafica<T> vertice(T a) {
        
        Vertice<T> ver = null;
        for(Vertice<T> vE :vertices)
        if(vE.elemento ==a )
                return vE;
        
        
        throw new NoSuchElementException();
                // Aquí va su código.
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben
     * estar en la gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     * @throws IllegalArgumentException si a o b no están
     *         conectados.
     */
    public void desconecta(T a, T b) {
        
        if (a == b)
            throw new IllegalArgumentException();
        
        
        
        Vertice<T> vA = miVertice(a);
        Vertice<T> vB = miVertice(b);
        
        if(sonVecinos(a,b))
        {
            
            aristas--;
            vA.vecinos.elimina(vB);
            vB.vecinos.elimina(vA);
            
        }else
            throw new IllegalArgumentException();
        
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la
     *         gráfica, <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        boolean siContiene = false;
        for(T e:this)
            if(e==elemento)
                siContiene = true;
        return siContiene;
        // Aquí va su código.
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que
     * estar contenido en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está
     *         contenido en la gráfica.
     */
    public void elimina(T a) {
        
        Vertice<T> vE = miVertice(a);
        for(VerticeGrafica<T> vG : vE  )
            desconecta(a,vG.getElemento());
        vertices.elimina(vE);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los
     * elementos deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en
     *         otro caso.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        boolean siSonVecinos = false;
        
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("No esta alguno de los 2 elementos dentro de la grafica.");
        
        
        Vertice<T> vA = miVertice(a);
        Vertice<T> vB = miVertice(b);
        
        for(VerticeGrafica<T> c: vA)
        {
            if(c.getElemento()==b)
            {
                siSonVecinos = true;
                break;
            }
        }
        for(VerticeGrafica<T> c: vB)
        {
            if(c.getElemento()==a)
            {
                siSonVecinos = true;
                break;
            }
        }
        
        return siSonVecinos;
    }

   

    /**
     * Realiza la acción recibida en cada uno de los vértices de la
     * gráfica, en el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice<T> vA :vertices)
            accion.actua(vA);
    }
    

    /**
     * Realiza la acción recibida en todos los vértices de la
     * gráfica, en el orden determinado por BFS, comenzando por el
     * vértice correspondiente al elemento recibido. Al terminar el
     * método, todos los vértices tendrán color {@link
     * Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos
     *        comenzar el recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la
     *         gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        
        
        //Obtenemos nuestro elemento origen
        VerticeGrafica<T> vG = vertice(elemento);
        //Creamos cola q
        Cola<VerticeGrafica<T>> q = new Cola<VerticeGrafica<T>>();
        //Agregamos elemento origen a cola q.
        q.mete(vG);
        //Marcamos elemento origen como visitado
        vG.setColor(Color.NEGRO);
        //Mientras la cola no sea vacia
        while(!q.esVacia())
        {
            //Sacamos un elemento de la cola, (FIFO)
            VerticeGrafica<T> v = q.saca();
            //Al vertice que acabamos de sacar le corremos la accion
            accion.actua(v);
            //Para cada vecino de v
            for(VerticeGrafica<T> a:v)
            {
                //Si no ha sido visitado
                if(a.getColor()!=Color.NEGRO)
                {
                    a.setColor(Color.NEGRO);
                    //Metemos este elemento a la cola
                    q.mete(a);
                }
            }
            
        }
        
        
        for(Vertice<T> vA :vertices)
            vA.setColor(Color.NINGUNO);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la
     * gráfica, en el orden determinado por DFS, comenzando por el
     * vértice correspondiente al elemento recibido. Al terminar el
     * método, todos los vértices tendrán color {@link
     * Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos
     *        comenzar el recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la
     *         gráfica.
     */

     public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
         
         //Obtenemos nuestro elemento origen
         VerticeGrafica<T> vG = vertice(elemento);
        //Creamos una pila s de ejeccion
         Pila<VerticeGrafica<T>> s = new Pila<VerticeGrafica<T>>();
         //Agregamos el origen vG a la pila.
         s.mete(vG);
         //Marcamos el origen como visitado
         vG.setColor(Color.NEGRO);
         //Mientras s (la pila) no este vacia
         while(!s.esVacia())
         {
             VerticeGrafica<T> v = s.saca();
             
             //Corremos la accion sobre el vertice
             accion.actua(v);
             
             for(VerticeGrafica<T>vA:v)
             {
                 if(vA.getColor()!=Color.NEGRO)
                 {
                     vA.setColor(Color.NEGRO);
                     s.mete(vA);
                 }
             }
         }
         
         for(Vertice<T> vA :vertices)
             vA.setColor(Color.NINGUNO);
         
     }
    public void dfs(VerticeGrafica<T> vG, AccionVerticeGrafica<T> accion) {

        //Marcamos origen como visitado
        vG.setColor(Color.NEGRO);
        
        //Para cada vertice adyacente.
        for (VerticeGrafica<T> vA: vG )
        {
        //
            
            //SI no ha sido visitado el vertice adyancete
            if(vA.getColor()!=Color.NEGRO)
            {
                
                //Lo pintamos de visitado
                vA.setColor(Color.NEGRO);
                
                //Recursamos sobre este vertice
                
                dfs(vA, accion);
                
            }
            
        }
        
        //Corremos la accion sobre el vertice
        accion.actua(vG);
     }
    
    

    public String toString()
    {
        String s = new String();
        
        for(Vertice<T> v: vertices)
        {
        
            s += ( v  +" --> " + v.vecinos + "\n");
        }
        
        return s;
        
    }
    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se
     * itera en el orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(this);
    }
}
