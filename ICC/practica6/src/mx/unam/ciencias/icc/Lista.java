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
public class Lista <T extends Comparable<T>> {
    
    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo <T extends Comparable<T>> {
        public T elemento;
        public Nodo<T> anterior;
        public Nodo<T> siguiente;
        
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }
    
    /* Primer elemento de la lista. */
    private Nodo<T> cabeza;
    /* Último elemento de la lista. */
    private Nodo<T> rabo;
    /* Nodo<T> iterador. */
    private Nodo<T> iterador;
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
        
        iterador = cabeza;
        
    }
    public void elimina(T elemento) {
        iterarElimina(cabeza , elemento);
        
    }
    private void iterarElimina(Nodo<T> t, T elemento)
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
            iterador= cabeza;
            
        }
        iterarElimina(t.siguiente,elemento);
    }
    public boolean contiene(T elemento) {
        return iterarContiene(cabeza,elemento);
        
    }
    private boolean iterarContiene(Nodo<T> t, T elemento){
        if (t==null)
            return false;
        if(t.elemento.equals(elemento))
            return true;
        return iterarContiene(t.siguiente,elemento);
        
    }
    public Lista<T> reversa() {
        return iterarReversa(cabeza,new Lista<T>());
    }
    private Lista<T> iterarReversa(Nodo<T> t, Lista<T> lista){
        if (t==null)
            return lista;
        lista.agregaInicio(t.elemento);
        
        
        return iterarReversa(t.siguiente,lista);
        
    }
    public Lista<T> copia() {
        return iterarCopia(cabeza,new Lista<T>());
    }
    private Lista<T> iterarCopia(Nodo<T> t, Lista<T> lista){
        if (t==null)
            return lista;
        lista.agregaFinal(t.elemento);
        
        
        return iterarCopia(t.siguiente,lista);
        
    }
    public void limpia() {
        cabeza = rabo = iterador = null;
        longitud = 0;
        
    
    }
    public T get(int i) {
        
        if (i<0 || i>=longitud)
            return null;
        
        return iterarGet(i,0,cabeza);
        
    }
    private  T iterarGet(int i,int c, Nodo<T> t){
        if (c < i)
            return iterarGet(i, ++c, t.siguiente);
        return t.elemento;
    }
    
    public int indiceDe(T elemento) {

        return iterarInidiceDe(cabeza,elemento,0);
    }
    
    private int iterarInidiceDe(Nodo<T> t,T elemento,int c)
    {
        if (t== null)
            return -1;
        if(t.elemento.equals(elemento))
            return c;
        return iterarInidiceDe(t.siguiente,elemento,++c);
    }
    
    public String toString() {
        return iterarToString(cabeza,"");
    }
    private String iterarToString(Nodo<T> t,String s)
    {
        
        if (t ==null)
            return s;
        
        
        if (t.anterior == null)
            s+="[";
        if (t.siguiente == null)
            s +=String.format("%s]", t.elemento);
        else
            s += String.format("%s, ", t.elemento);
        
        return iterarToString(t.siguiente,s);
            
        
    }
    public void primero() {
        iterador = cabeza;
    }
    public void ultimo() {
        iterador = rabo;    }
    public void siguiente() {
        iterador = iterador.siguiente;
    }
    public void anterior() {
        iterador =iterador.anterior;
    }
    public T dame() {
        if (iterador == null)
            return null;
        return iterador.elemento;
    }
    public boolean iteradorValido() {
        
        boolean esNull = false;
        if(iterador != null)
            esNull =  true;
        return esNull;
    }

    public boolean equals(Object obj)
    {
                boolean isEqual= false;
        if ((obj instanceof Lista)&& obj !=null)
        {
            @SuppressWarnings("unchecked")  Lista<T> l2 = (Lista<T>)obj;
            
            Nodo<T> t = cabeza;
            
            l2.primero();
            
            while (l2.iteradorValido() && t!=null)
            {
                if(!l2.dame().equals(t.elemento))
                    break;
                if(t.siguiente == null)
                {
                    l2.siguiente();
                    if (l2.dame() == null)
                    {
                        isEqual = true;
                        break;
                    }
                }
                l2.siguiente();
                t=t.siguiente;
            }
        }
        return isEqual;
    }
    

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La
     * lista recibida tiene que contener nada más elementos que
     * implementan la interfaz {@link Comparable}.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> l)
    {
        if(l.longitud<2)
            return l.copia();
        
        Lista<T> li = new Lista<T>(),ld= new Lista<T>();
        int d = l.longitud/2;
        l.primero();
        for(int i =0; i<d ;i++)
        {
            li.agregaFinal(l.dame());
            l.siguiente();
        }
        
        for (int i = (int)d; i < l.longitud; i++)
        {
            ld.agregaFinal(l.dame());
            l.siguiente();
        }
        l.primero();
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
    
}
