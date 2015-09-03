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
 * información relevante. También deben implementar los métodos
 * {@link #buscaRegistros} (para hacer consultas en la base de
 * datos) y {@link #actualizaRegistro} (para actualizar un
 * registro). Estos dos métodos reciben un entero que especifica la
 * columna a buscar o actualizar; las clases que extiendan
 * BaseDeDatos deben proveer variables estáticas que simbolicen
 * estas columnas.
 *
 *
 *
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
    /** El último registro eliminado o actualizado. */
    protected T ultimoModificado;

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
     * Regresa el último registro agregado, eliminado o actualizado.
     * @return el último registro agregado, eliminado o actualizado.
     */
    public T getUltimoModificado() {
        // Aquí va su código.
        return ultimoModificado;
    }

    /**
     * Regresa una lista con los registros en la base de
     * datos. Modificar esta lista no cambia a la información en la
     * base de datos.
     * @return una lista con los registros en la base de datos.
     */
    public Lista<T> getRegistros() {
        // Aquí va su código. public Lista<T> getRegistros() {
        return registros.copia();
    
    }

    /**
     * Agrega el registro recibido a la base de datos.
     * @param registro el registro que hay que agregar a la base de
     *        datos.
     */
    public void agregaRegistro(T registro){
        // Aquí va su código.

        
        
        ultimoModificado = registro;
        registros.agregaFinal(registro);
        
        int mr = registros.getLongitud()-1;
        
        TableModelListener l =  escuchas.getAny();
        for (TableModelListener e : escuchas)
        {
            e.tableChanged(new TableModelEvent(this,mr,mr,TableModelEvent.ALL_COLUMNS,TableModelEvent.INSERT));
        }
    }

    /**
     * Elimina el registro recibido de la base de datos.
     * @param registro el registro que hay que eliminar de la base
     *        de datos.
     */
    public void eliminaRegistro(T registro) {

        ultimoModificado = registro;

    int mr = registros.indiceDe(registro);


    registros.elimina(registro);

        for (TableModelListener e : escuchas)
        {
            e.tableChanged(new TableModelEvent(this,mr,mr,TableModelEvent.ALL_COLUMNS,TableModelEvent.DELETE));
        }
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
    {

        agregaRegistro(r);}

        }while(rv);

    }catch(IOException e)
    {
        throw new IOException("Fallo al cargar");
}


    }
    /**
     * Crea un registro sin información relevante.
     */
    public abstract T creaRegistro();

    /**
     * Busca registros por una columna específico.
     * @param columna la columna del registro por la cuál buscar.
     * @param texto el texto a buscar.
     * @return una lista con los registros tales que en la columna
     *         especificada contienen el texto recibido.
     * @throws IllegalArgumentException si la columna no es válida.
     */
    public abstract Lista<T> buscaRegistros(int columna, String texto);

    /**
     * Actualiza un registro que ha cambiado en una columna.
     * @param registro el registro actualizado.
     * @param columna la columna que cambió en el registro.
     * @throws IllegalArgumentException si la columna no es válida.
     */
    public abstract void actualizaRegistro(T registro, int columna);
    

    /**
     * Elimina los registros en los índices especificados.
     * @param indices los índices de los registros a elimnar.
     */
    public void eliminaRegistros(Integer[] indices) {
        // Aquí va su código.
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
// Aquí va su código.public Integer[] indicesDe(Lista<T> lista) {
        Integer[] dots = new Integer[lista.getLongitud()];
            int i = 0;
            for(T r : lista)
            dots[i++] = new Integer(registros.indiceDe(r));
            return dots;

    }

    /**
     * Limpia la base de datos.
     */
    public void limpia() {
        eliminaRegistros(indicesDe(registros));
    }

    /* Métodos de la interfaz TableModel. */

    /**
     * Agrega un escucha a la lista de escuchas que es notificada
     * cada vez que ocurra un cambio en el modelo de datos.
     * @param escucha el escucha a agregar a la lista de escuchas.
     */
    @Override public void
        addTableModelListener(TableModelListener escucha) {
        // Aquí va su código.
    escuchas.agregaFinal(escucha);
    }

    /**
     * Elimina un escucha a la lista de escuchas que es notificada
     * cada vez que ocurra un cambio en el modelo de datos.
     * @param escucha el escucha a eliminar de la lista de escuchas.
     */
    @Override public void
        removeTableModelListener(TableModelListener escucha) {
        // Aquí va su código.
        escuchas.elimina(escucha);
    }

    /**
     * Regresa el número de renglones en el modelo.
     * @return el número de renglones en el modelo.
     */
    @Override public int getRowCount() {
        // Aquí va su código.
                return registros.getLongitud();
    }



}
