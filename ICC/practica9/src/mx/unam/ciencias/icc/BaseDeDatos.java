package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Clase genérica abstracta para bases de datos. Provee métodos para
 * agregar y eliminar registros, y para guardarse y cargarse de una
 * entrada y salida dados.
 *
 * Las clases que extiendan a BaseDeDatos deben implementar el
 * método {@link #creaRegistro}, que crea un registro sin
 * información relevante. También deben implementar el método {@link
 * #buscaRegistros} para hacer consultas en la base de datos.
 *
 * Por último, las clases herederas a ésta deben implementar los
 * métodos definidos en la interfaz {@link TableModel}, para que la
 * base de datos pueda ser usada como modelo de una interfaz
 * gráfica.
 */
public abstract class BaseDeDatos<T extends Registro> implements TableModel {

    /** Lista de registros en la base de datos. */
    protected Lista<T> registros;
    /** Lista de escuchas para el modelo de tabla. */
    protected Lista<TableModelListener> escuchas;
    /**
     * Constructor único.
     */
    public BaseDeDatos() {
        registros = new Lista<T>();
        escuchas = new Lista<TableModelListener>();

    }

    /**
     * Regresa el número de registros en la base de datos.
     * @return el número de registros en la base de datos.
     */
    public int getNumRegistros() {
        return registros.getLongitud();
    }

    /**
     * Regresa una lista con los registros en la base de
     * datos. Modificar esta lista no cambia a la información en la
     * base de datos.
     * @return una lista con los registros en la base de datos.
     */
    public Lista<T> getRegistros() {
        return registros.copia();
    }

    /**
     * Agrega el registro recibido a la base de datos.
     * @param registro el registro que hay que agregar a la base de
     *        datos.
     */
    //WWW
    public void agregaRegistro(T registro) {
        
        //        Assert.assertTrue(e.getColumn() == TableModelEvent.ALL_COLUMNS);
        //        Assert.assertTrue(e.getType() == TableModelEvent.INSERT);
        //        Assert.assertTrue(e.getFirstRow() == 0);
        //        Assert.assertTrue(e.getLastRow() == 0);
//
        // Aquí va su código.
        registros.agregaFinal(registro);
        
        int mr = registros.getLongitud()-1;
        
        TableModelListener l =  escuchas.getAny();
        if(l!=null)
        l.tableChanged(new TableModelEvent(this,mr,mr,TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT));
    }
    /**
     * Elimina el registro recibido de la base de datos.
     * @param registro el registro que hay que eliminar de la base
     *        de datos.
     */
    //WWW
    public void eliminaRegistro(T registro) {
        
        

        int mr = registros.indiceDe(registro);
        

        registros.elimina(registro);
        
        TableModelListener l =  escuchas.getAny();
        if(l!=null)
        l.tableChanged(new TableModelEvent(this,mr,mr,TableModelEvent.ALL_COLUMNS,TableModelEvent.DELETE));
    }


    /**
     * Guarda todos los registros en la base de datos en la salida
     * recibida.
     * @param out la salida donde hay que guardar los registos.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public void guarda(BufferedWriter out) throws IOException {
        try {
            
        for(T r : registros)
            r.guarda(out);
         }catch(IOException e){
            throw new IOException("Fallo al guardar");
        }
    }


    /**
     * Guarda los registros de la entrada recibida en la base de
     * datos. Si antes de llamar el método había registros en la
     * base de datos, estos son eliminados.
     * @param in la entrada de donde hay que cargar los registos.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    
    public void carga(BufferedReader in) throws IOException {
        
        try
        {
        registros.limpia();
        boolean rv = false;
        do{
            T r = creaRegistro();
            rv = r.carga(in);
            
            if (rv)
                agregaRegistro(r);
            
        }while(rv);
             }catch(IOException e){
        throw new IOException("Fallo al cargar");
    }
    
    
    }

    
    /*IMEMENTADO POR CLASES HEREDITARIAS */
    /**
     * Crea un registro sin información relevante.
     */
    protected abstract T creaRegistro();

    /**
     * Busca registros por un campo específico.
     * @param campo el campo del registro por el cuál buscar.
     * @param texto el texto a buscar.
     * @return una lista con los registros tales que en el campo
     *         especificado contienen el texto recibido.
     * @throws IllegalArgumentException si el campo no es válido.
     */
    public abstract Lista<T> buscaRegistros(String campo, String texto);

    
    /**
     * Elimina los registros en los índices especificados.
     * @param indices los índices de los registros a elimnar.
     */
    public void eliminaRegistros(Integer[] indices) {
        Lista<T> elementosAEliminar = new Lista<T>();
        for(Integer i : indices)
        elementosAEliminar.agregaFinal(registros.get(i.intValue()));
        elementosAEliminar = elementosAEliminar.copia();
        for(T r: elementosAEliminar)
        eliminaRegistro(r);
    }

    /**
     * Regresa los índices de los registros recibidos en la base de
     * datos. El método supone que cada registro en la lista se
     * encuentra en la base de datos.
     * @param lista una lista con registros, de los cuales se busca sus
     *        índices.
     * @return un arreglo con los índices de los registros en la
     *         base de datos.
     */
    public Integer[] indicesDe(Lista<T> lista) {
        Integer[] dots = new Integer[lista.getLongitud()];
        int i = 0;
        for(T r : lista)
        dots[i++] = new Integer(registros.indiceDe(r));
        return dots;
    }

    /* Métodos de la interfaz TableModel. */

    /**
     * Agrega un escucha a la lista de escuchas que es notificada
     * cada vez que ocurra un cambio en el modelo de datos.
     * @param escucha el escucha a agregar a la lista de escuchas.
     */
    @Override public void
        addTableModelListener(TableModelListener escucha) {
            
            escuchas.agregaFinal(escucha);
    }

    /**
     * Elimina un escucha a la lista de escuchas que es notificada
     * cada vez que ocurra un cambio en el modelo de datos.
     * @param escucha el escucha a eliminar de la lista de escuchas.
     */
    @Override public void
        removeTableModelListener(TableModelListener escucha) {
            escuchas.elimina(escucha);
    }

    /**
     * Regresa el número de renglones en el modelo.
     * @return el número de renglones en el modelo.
     */
    @Override public int getRowCount() {
        return registros.getLongitud();
    }

    /**
     * Regresa <tt>true</tt>, siempre. Todas las celdas son
     * editables.
     * @return <tt>true</tt>.
     */
    @Override public boolean isCellEditable(int renglon, int columna) {
        return true;
    }
}
