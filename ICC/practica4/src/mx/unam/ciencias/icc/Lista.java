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
public class Lista<id> {
    
    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo <id> {
        public id elemento;
        public Nodo<id> anterior;
        public Nodo<id> siguiente;
        
        public Nodo(id elemento) {
            this.elemento = elemento;
        }
    }
    
    /* Primer elemento de la lista. */
    private Nodo<id> cabeza;
    /* Último elemento de la lista. */
    private Nodo<id> rabo;
    /* Nodo<T> iterador. */
    private Nodo<id> iterador;
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
    public void agregaFinal(id elemento) {
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
    public void agregaInicio(id elemento) {
        
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
    public void elimina(id elemento) {
        iterarElimina(cabeza , elemento);
        
    }
    private void iterarElimina(Nodo t, id elemento){
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
    public boolean contiene(id elemento) {
        return iterarContiene(cabeza,elemento);
        
    }
    private boolean iterarContiene(Nodo t, id elemento){
        if (t==null)
            return false;
        if(t.elemento.equals(elemento))
            return true;
        return iterarContiene(t.siguiente,elemento);
        
    }
    public Lista reversa() {
        return iterarReversa(cabeza,new Lista());
    }
    private Lista iterarReversa(Nodo t, Lista lista){
        if (t==null)
            return lista;
        lista.agregaInicio(t.elemento);
        
        
        return iterarReversa(t.siguiente,lista);
        
    }
    public Lista copia() {
        return iterarCopia(cabeza,new Lista());
    }
    private Lista iterarCopia(Nodo t, Lista lista){
        if (t==null)
            return lista;
        lista.agregaFinal(t.elemento);
        
        
        return iterarCopia(t.siguiente,lista);
        
    }
    public void limpia() {
        cabeza = rabo = iterador = null;
        longitud = 0;
        
    
    }
    public id get(int i) {
        
        if (i<0 || i>=longitud)
            return null;
        
        return iterarGet(i,0,cabeza);
        
    }
    private id iterarGet(int i,int c, Nodo t){
        if (c < i)
            return iterarGet(i,++c,t.siguiente);
        return t.elemento;
    }
    
    public int indiceDe(id elemento) {

        return iterarInidiceDe(cabeza,elemento,0);
    }
    
    private int iterarInidiceDe(Nodo t,id elemento,int c)
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
    private String iterarToString(Nodo t,String s)
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
    public id dame() {
        if (iterador == null)
            return null;
        return iterador.elemento;
    }
    public boolean iteradorValido() {
        if(iterador == null)
            return false;
        else
        return true;
    }

    public boolean equals( id obj)
    {
        boolean isEqual= false;
        if (obj instanceof Lista)
        {
            Lista l2 = (Lista)obj;
            
            Nodo t = cabeza;
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
    
}
