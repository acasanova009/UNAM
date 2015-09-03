package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>). Podemos crear
 * un montículo mínimo con <em>n</em> elementos en tiempo
 * <em>O</em>(<em>n</em>), y podemos agregar y actualizar elementos
 * en tiempo <em>O</em>(log <em>n</em>). Eliminar el elemento mínimo
 * también nos toma tiempo <em>O</em>(log <em>n</em>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Iterable<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador<T extends ComparableIndexable<T>> implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;
        private MonticuloMinimo<T> monticulo;

        /* Construye un nuevo iterador, auxiliándose del montículo
         * mínimo. */
        public Iterador(MonticuloMinimo<T> monticulo) {
            this.monticulo = monticulo;
            indice = 0;
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            if(indice < monticulo.siguiente) 
               return true;
	    else
               return false;
        }

        /* Regresa el siguiente elemento. */
        public T next() {
            return monticulo.arbol[indice ++];
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int siguiente;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así
       por cómo Java implementa sus genéricos; de otra forma
       obtenemos advertencias del compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Lista)}, pero se ofrece este constructor por
     * completez.
     */
    public MonticuloMinimo() {
        arbol = creaArregloGenerico(0);
        siguiente = 0;
    }

    /**
     * Constructor para montículo mínimo que recibe una lista. Es
     * más barato construir un montículo con todos sus elementos de
     * antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     */
    public MonticuloMinimo(Lista<T> lista) {
        siguiente = lista.getLongitud();
        arbol = creaArregloGenerico(siguiente);
        int j = 0;
        
        for(T e : lista){
                arbol[j] = e;
                arbol[j].setIndice(j);
                j++;
        }
        
        for(int a = siguiente/2; a>= 0; a--){
            
            int b = a;
            int c = minimiza(a);
            while(c != b){
                T aux = arbol[b];
                arbol[b] = arbol[c];
                arbol[b].setIndice(b);
                arbol[c] = aux;
                arbol[c].setIndice(c);
                b = c;
                c = minimiza(b);
            }  
       }
    }

    private int minimiza(int i){
        int izquierdo = (2*i)+1;
        int derecho = izquierdo + 1;
        int m;
        
        if(izquierdo >= siguiente)
            return i;
        
        if(arbol[i].compareTo(arbol[izquierdo]) < 0)
            m = i;
        else
            m = izquierdo;
        
        if(derecho < siguiente)
                if(arbol[m].compareTo(arbol[derecho]) > 0)
                    m = derecho;
                 	
    
    return m;
  }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    public void agrega(T elemento) {
        if(siguiente + 1 > arbol.length){
	   T [] arbolin = creaArregloGenerico((arbol.length+1)*2);
	   for(int j= 0; j< siguiente; j++){
	       arbolin[j] = arbol[j];	    
	   }
	   arbol = arbolin;
       }
       arbol[siguiente] = elemento;
       arbol[siguiente].setIndice(siguiente);
       siguiente++;
       reordena(elemento);	 
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina() {
        if(esVacio())
	 throw new IllegalStateException();
        
         T w = arbol[0];
         siguiente = siguiente-1;
         arbol[0] = arbol[siguiente];
         arbol[0].setIndice(0);
         int p = 0;
         int m = minimiza(p);
         while(m != p){	
	       T padre = arbol[p];
	       arbol[p] = arbol[m];
	       arbol[p].setIndice(p);
	       arbol[m] = padre;
	       arbol[m].setIndice(m);
	       p = m;
	       m = minimiza(p);
         }
         return w;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    public boolean esVacio() {
        return siguiente == 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    public void reordena(T elemento) {
        int i = elemento.getIndice();
        int p = (i-1)/2;
        while(p >= 0 && arbol[p].compareTo(arbol[i]) > 0){
	      T padre = arbol[p];
	      arbol[p] = arbol[i];
	      arbol[p].setIndice(p);
	      arbol[i] = padre;
	      arbol[i].setIndice(i);
	      i = p;
	      p = (i-1)/2;
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    public int getElementos() {
        return siguiente;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o
     *         mayor o igual que el número de elementos.
     */
    public T get(int i) {
        if(i< 0 || i>= siguiente)
           throw new NoSuchElementException();
           return arbol[i]; 
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El
     * montículo se itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    public Iterator<T> iterator() {
        return new Iterador<T>(this);
    }
    public String toString()
    {
        String a = "";
        for(T e: this)
        {
            a+="["+e+"]";
        }
        return a;
    }
}
