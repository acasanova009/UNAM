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
        private Iterator<Grafica<T>.Vertice<T>> iterador;

        /* Construye un nuevo iterador, auxiliándose del diccionario
         * de vértices. */
        public Iterador(Grafica<T> grafica) {
            // Aquí va su código.
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            return false;
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento. */
        public T next() {
            return null;
            // Aquí va su código.
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Aristas para gráficas; para poder guardar el peso de las
     * aristas. */
    private class Arista<T> {

        /* El vecino del vértice. */
        public Grafica<T>.Vertice<T> vecino;
        /* El peso de arista conectando al vértice con el vecino. */
        public double peso;

        public Arista(Grafica<T>.Vertice<T> v, double p) {
            
            peso = p ;
            vecino = v;
        }
        public String toString()
        {
            String a = new String();
            String b = String.valueOf(peso);
            a+= ("V:"+vecino+" P:"+ b);
            return a;
            
        }
    }

    /* Vertices para gráficas; implementan la interfaz
     * ComparableIndexable y VerticeGrafica */
    private class Vertice<T> implements ComparableIndexable<Vertice<T>>,
        VerticeGrafica<T> {

        /* Iterador para las vecinos del vértice. */
        private class IteradorVecinos implements Iterator<VerticeGrafica<T>> {

            /* Iterador auxiliar. */
            private Iterator<Grafica<T>.Arista<T>> iterador;

            /* Construye un nuevo iterador, auxiliándose del
             * diccionario de vecinos. */
            public IteradorVecinos(Iterator<Grafica<T>.Arista<T>> i) {
                
                iterador = i;
                // Aquí va su código.
            }

            /* Nos dice si hay un siguiente vecino. */
            public boolean hasNext() {
                
                return iterador.hasNext();
                // Aquí va su código.
            }

            /* Regresa el siguiente vecino. La audición es
             * inevitable. */
            public VerticeGrafica<T> next() {
                return (VerticeGrafica<T>)iterador.next().vecino;
                // Aquí va su código.
            }

            /* No lo implementamos: siempre lanza una excepción. */
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* El diccionario de aristas que conectan al vértice con sus
         * vecinos. */
        public Diccionario<T, Grafica<T>.Arista<T>> aristas;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T e) {
            // Aquí va su código.
            elemento = e;
            color = Color.NINGUNO;
            aristas = new Diccionario<T, Grafica<T>.Arista<T>>();
            indice = 10;
            distancia = 1;
            
        }
        /* Regresa el elemento del vértice. */
        public T getElemento() {

            return elemento;
        }

        /* Regresa el grado del vértice. */
        public int getGrado() {
            return aristas.getTotal();
            // Aquí va su código.
        }

        /* Regresa el color del vértice. */
        public Color getColor() {
            return color;
            // Aquí va su código.
        }

        /* Define el color del vértice. */
        public void setColor(Color c) {
            color= c;
            // Aquí va su código.
        }

        /* Regresa un iterador para los vecinos. */
        public Iterator<VerticeGrafica<T>> iterator() {
            return new IteradorVecinos(aristas.iterator());
        }

        /* Define el índice del vértice. */
        public void setIndice(int i) {
            indice=i;
            // Aquí va su código.
        }

        /* Regresa el índice del vértice. */
        public int getIndice() {
            return indice;
            // Aquí va su código.
        }

        /* Compara dos vértices por distancia. */
        public int compareTo(Vertice<T> vertice) {
            // Aquí va su código.
            
            int same = 0;
            if(vertice.distancia < this.distancia)
                same = 1;
            else if(vertice.distancia > this.distancia)
                same = -1;
            return same;
        }
        public String toString(){
            return (" "+ elemento.toString() +" D:"+ distancia+" ");
        }
    }

    /* Vértices. */
    private Diccionario<T, Vertice<T>> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        
        vertices =  new Diccionario<T, Vertice<T>> ();
        aristas = 0;
        // Aquí va su código.
    }

    /**
     * Regresa el número de vértices.
     * @return el número de vértices.
     */
    public int getVertices() {
            return vertices.getTotal();
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
    public void agrega(T elemento) {
        
        
        if(vertices.contiene(elemento))
            throw new IllegalArgumentException();
        
        vertices.agrega(elemento, new Vertice<T>(elemento));
        // Aquí va su código.
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben
     * estar en la gráfica. El peso de la arista que conecte a los
     * elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     * @throws IllegalArgumentException si a o b ya están
     *         conectados, o si a es igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a, b, 1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben
     * estar en la gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva arista.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     * @throws IllegalArgumentException si a o b ya están
     *         conectados, o si a es igual a b.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
        
        if (a.equals(b))
            throw new IllegalArgumentException();
    
        Vertice<T> vA = miVertice(a);
        Vertice<T> vB = miVertice(b);
        
        if (vA.aristas.contiene(b))
            throw new IllegalArgumentException();
        
        if(!sonVecinos(a,b))
        {
            aristas++;
            vA.aristas.agrega(b , new Grafica<T>.Arista<T>(vB,peso));
            vB.aristas.agrega(a , new Grafica<T>.Arista<T>(vA,peso));
        }
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
        if (a.equals(b))
            throw new IllegalArgumentException();
        
        
        Vertice<T> vA = miVertice(a);
        Vertice<T> vB = miVertice(b);
        
        if(sonVecinos(a,b))
        {
            aristas--;
            vA.aristas.elimina(b);
            vB.aristas.elimina(a);
            
        }else
            throw new IllegalArgumentException();
        // Aquí va su código.
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la
     *         gráfica, <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        return vertices.contiene(elemento);
        // Aquí va su código.
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que
     * estar contenido en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está
     *         contenido en la gráfica.
     */
    public void elimina(T elemento) {
        
        if(!contiene(elemento))
            throw new NoSuchElementException("No alguno entos dentro de la grafica.");
        for(VerticeGrafica<T> vecino : vertice(elemento))
            desconecta(elemento, vecino.getElemento());
        
        vertices.elimina(elemento);
        
        // Aquí va su código.
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
            throw new NoSuchElementException("No esta alguno los elemntos en la grafica.");
        
        if(miVertice(a).aristas.contiene(b) && miVertice(b).aristas.contiene(a))
            siSonVecinos = true;
        
        return siSonVecinos;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que
     * contienen a los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que
     *         contienen a los elementos recibidos, o -1 si los
     *         elementos no están conectados.
     * @throws NoSuchElementException si a o b no son elementos de
     *         la gráfica.
     */
    public double getPeso(T a, T b) {
        
        double peso = -1;
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException("No alguno entos dentro de la grafica.");
        if(sonVecinos(a,b))
             peso = ((Vertice<T>)vertice(a)).aristas.get(b).peso;
        return peso;
        // Aquí va su código.
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @throws NoSuchElementException si elemento no es elemento de
     *         la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) throws NoSuchElementException{
        return vertices.get(elemento);
        // Aquí va su código.
    }
    private Vertice<T> miVertice(T elemento)
    {
        return vertices.get(elemento);
        
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
    public void bfs(T elemento, AccionVerticeGrafica<T> accion)
    {
        
        
        // Aquí va su código.
        
        for(Vertice<T> vA :vertices)
            vA.setColor(Color.NINGUNO);
        
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

        // Aquí va su código.
        
        for(Vertice<T> vA :vertices)
            vA.setColor(Color.NINGUNO);
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

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se
     * itera en el orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(this);
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos
     * vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman
     *         una trayectoria de distancia mínima entre los
     *         vértices <tt>a</tt> y <tt>b</tt>. Si los elementos se
     *         encuentran en componentes conexos distintos, el
     *         algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos
     *         no está en la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {

        //Pintamos todo de color ROJO y distancia infinito
        for (VerticeGrafica<T> v :vertices)
        {
            Vertice<T> vA = cVertice(v);
            vA.setColor(Color.ROJO);
            vA.distancia = -1;
        }
        //Lista donde estara la futura trayectoria.
        Lista<VerticeGrafica<T>> trayectoria = null;
        
        //Cola donde se guardaran los vertices visitados.
        Cola<VerticeGrafica<T>> q = new Cola<VerticeGrafica<T>>();
        
        //Vertice Original
        Vertice<T> vG = miVertice(origen);
        //Basico del vertice original
        
        //Marcamos elemento origen como visitado
        vG.setColor(Color.NEGRO);
        vG.distancia = 0;
        
        //Vertice Destino
        VerticeGrafica<T> vF = vertice(destino);
        
        
        if(!contiene(origen)||!contiene(destino))
            throw new NoSuchElementException("No esta alguno de los vertces");
        
        q.mete(vG);
        //Mientras la cola no sea vacia
        while(!q.esVacia())
        {
            //Sacamos un elemento de la cola, (FIFO)
            VerticeGrafica<T> n = q.saca();
            
            Vertice<T> v = cVertice(n);
            //Al vertice que acabamos de sacar le corremos la accion
            //            accion.actua(v);
            //Para cada vecino de v
            for(VerticeGrafica<T> b:v)
            {
                Vertice<T> a = cVertice(b);
                //Si no ha sido visitado
                if(a.getColor()!=Color.NEGRO)
                {
                    a.setColor(Color.NEGRO);
                    
                    if(a.distancia == -1  && v.distancia == 0)
                        a.distancia = 1;
                    else
                        //                    else if(a.distancia == -1)
                    {
                        a.distancia =0;
                        a.distancia += v.distancia + 1;
                    }
                    
                    //SI YA LO ENCONTRAMOS POS NOS VAMOS
                    if(a.elemento == destino)
                    {
                        //Que tan lejos nos saca.
                        
                        trayectoria = new Lista<VerticeGrafica<T>>();
                        trayectoria.agregaInicio(a);
                        recursionSobreVertice(trayectoria,a);
                        
                        break;
                    }
                    
                    //Metemos este elemento a la cola
                    q.mete(a);
                }
            }
            //Si esto no es null, es que ya encontramos nuestro vertice final. Y nOS vamos
            if(trayectoria!= null)
                break;
            
        }
        //Regresamos la trayectoria
        return trayectoria;
    }
    private Vertice<T> cVertice(VerticeGrafica<T> vG) {
        /* Tenemos que suprimir advertencias. */
        @SuppressWarnings("unchecked") Vertice<T> n = (Vertice<T>)vG;
        return n;
    }
    private void recursionSobreVertice(Lista<VerticeGrafica<T>> l , Vertice<T> a)
    {
        if(a.distancia<0)
            return;
        
        for(VerticeGrafica<T> b:a)
        {
            Vertice<T> c = cVertice(b);
            if(c.color==Color.NEGRO &&  c.distancia== a.distancia-1)
            {
                l.agregaInicio(c);
                recursionSobreVertice(l, c);
                break;
            }
        }
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y
     * el elemento de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice
     *         <tt>origen</tt> y el vértice <tt>destino</tt>. Si los
     *         vértices están en componentes conexas distintas,
     *         regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos
     *         no está en la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        
        
        //Vertice Original
        Vertice<T> vG = miVertice(origen);
        
        //Vertice Destino
        Vertice<T> vF = miVertice(destino);
        
        if(vG==null || vF==null)
            throw new NoSuchElementException("No esta alguno de los vertces");
        
        //Pintamos todo de color ROJO y distancia infinito
        //Distancia del vertice todos en inf, excepto el primero.
        for (VerticeGrafica<T> v :vertices)
        {
            Vertice<T> vA = cVertice(v);
            vA.setColor(Color.ROJO);
            vA.distancia = Double.POSITIVE_INFINITY;
        }
        
        //Marcamos elemento origen como visitado
        vG.setColor(Color.NEGRO);
        vG.distancia = 0;
        
        //Meto todos en monticulo
        MonticuloMinimo<Vertice<T>> mon = new MonticuloMinimo<Vertice<T>>(vertices.valores());
        
        //Lista donde estara la futura trayectoria.
        Lista<VerticeGrafica<T>> trayectoria = null;
        
        boolean siEstanConectados = false;
        
        
        
        while(!mon.esVacio())
        {
            
            Vertice<T> currentV = mon.elimina();
            
            for(Arista<T> a: currentV.aristas)
            {
                Vertice<T> v = a.vecino;
                if(v.getColor()!=Color.NEGRO)
                {
                    double novaDistancia = a.peso + currentV.distancia;
                    
                    if(v.distancia==Double.POSITIVE_INFINITY || novaDistancia < v.distancia)
                    {
                        
                        v.distancia = novaDistancia;
                        mon.reordena(v);
                    }
                    
                    if(v.elemento == destino)
                        siEstanConectados = true;
                }
            }
            currentV.setColor(Color.NEGRO);
            
        }
        
        if(siEstanConectados)
        {
            
            trayectoria = new Lista<VerticeGrafica<T>>();
            trayectoria.agregaInicio(vF);
            rVerticeDijkstra(trayectoria,vF);
            
            
        }
        //Regreamos sobre ultimo.
        return trayectoria;
    }
    //metodo recursivo para trayectoria de dijkstra, clusula de escape if (a.distancia == 0) para construccion de trayectoria. A partir del ultimo con menos uno de distancia y ya visitado
    private void rVerticeDijkstra(Lista<VerticeGrafica<T>> l , Vertice<T> current)
    {
        if(current.distancia == 0)
            return;
        
        for(Arista<T> a:current.aristas)
        {
            Vertice<T> v = a.vecino;
            
            double distanciaBuscamos = current.distancia - a.peso;
            if(v.distancia == distanciaBuscamos)
            {
                l.agregaInicio(v);
                recursionSobreVertice(l,v);
                break;
            }
        }
    }
    public String toString()
    {
        String s = new String();
        
        for(Vertice<T> v: vertices)
        {
            
            s += ( v  +" --> " + v.aristas + "\n");
        }
        
        return s;
        
    }
}
