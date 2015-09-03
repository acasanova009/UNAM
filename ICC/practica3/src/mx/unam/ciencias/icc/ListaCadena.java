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

    private class Nodo {
        public String elemento;
        public Nodo anterior;
        public Nodo siguiente;

        public Nodo(String elemento) {
            this.elemento = elemento;
        }
    }

    private Nodo cabeza;
    private Nodo rabo;
    private Nodo iterador;
    private int longitud;

    public int getLongitud() {
        return longitud;
    }
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
    public void elimina(String elemento) {
        iterarElimina(cabeza , elemento);
        
    }
    void iterarElimina(Nodo t, String elemento){
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
    public boolean contiene(String elemento) {
        return iterarContiene(cabeza,elemento);
        
    }
    boolean iterarContiene(Nodo t, String elemento){
        if (t==null)
            return false;
        if(t.elemento.equals(elemento))
            return true;
        return iterarContiene(t.siguiente,elemento);
        
    }
    public ListaCadena reversa() {
        return iterarReversa(cabeza,new ListaCadena());
    }
    ListaCadena iterarReversa(Nodo t, ListaCadena lista){
        if (t==null)
            return lista;
        lista.agregaInicio(t.elemento);
        
        
        return iterarReversa(t.siguiente,lista);
        
    }
    public ListaCadena copia() {
        return iterarCopia(cabeza,new ListaCadena());
    }
    ListaCadena iterarCopia(Nodo t, ListaCadena lista){
        if (t==null)
            return lista;
        lista.agregaFinal(t.elemento);
        
        
        return iterarCopia(t.siguiente,lista);
        
    }
    public void limpia() {
        cabeza = rabo = iterador = null;
        longitud = 0;
        
    
    }
    public String get(int i) {
        
        if (i<0 || i>=longitud)
            return null;
        
        return iterarGet(i,0,cabeza);
        
    }
    String iterarGet(int i,int c, Nodo t){
        if (c < i)
            return iterarGet(i,++c,t.siguiente);
        return t.elemento;
    }
    
    public int indiceDe(String elemento) {

        return iterarInidiceDe(cabeza,elemento,0);
    }
    
    int iterarInidiceDe(Nodo t,String elemento,int c)
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
    String iterarToString(Nodo t,String s)
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
    public String get() {
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

    
}
