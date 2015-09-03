package mx.unam.ciencias.icc.test;

import java.io.IOException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;

/**
 * Base de datos tonta de Ids para hacer pruebas.
 */
public class BaseDeDatosIds extends BaseDeDatos<Id> {

    /** Campo del id del Id. */
    public static final int ID         = 0;
    /** Campo del valor del Id. */
    public static final int VALOR      = 1;
    /** Número de columnas en el modelo. */
    public static final int N_COLUMNAS = 2;

    /**
     * Crea una nueva base de datos de Ids.
     */
    public BaseDeDatosIds() {
        super();
    }

    /**
     * Crea un nuevo Id sin información relevante.
     * @return un nuevo Id sin información relevante.
     */
    @Override public Id creaRegistro() {
        return new Id(null, null);
    }

    /**
     * Busca Ids por un id.
     * @param campo el campo del registro por el cuál buscar, que
     *        sólo puede ser <tt>"id"</tt>.
     * @param texto el texto a buscar.
     * @return una lista con los Ids tales que su id contienen el
     *         texto recibido.
     * @throws IllegalArgumentException si el campo no es
     *         <tt>"id"</tt>.
     */
    @Override public Lista<Id> buscaRegistros(int campo, String texto) {
        Lista<Id> r = new Lista<Id>();
        for (Id id : registros) {
            switch (campo) {
            case ID:
                if (id.getId().indexOf(texto) != -1)
                    r.agregaFinal(id);
                break;
            case VALOR:
                if (id.getId().indexOf(texto) != -1)
                    r.agregaFinal(id);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        return r;
    }

    /**
     * Actualiza un Id que ha cambiado.
     * @param registro el registro actualizado.
     * @param columna la columna que cambió en el registro: debe ser
     *        {@link #ID}.
     * @throws IllegalArgumentException si la columna no es {@link
     *         #ID}.
     */
    @Override public void actualizaRegistro(Id registro, int columna) {
        Id r = null;
        int renglon = 0;
        for (Id id : registros) {
            boolean i, v;
            i = registro.getId().equals(id.getId());
            v = registro.getValor().equals(id.getValor());
            switch (columna) {
            case ID:    i = true; break;
            case VALOR: v = true; break;
            default: throw new IllegalArgumentException();
            }
            if (i && v) {
                r = id;
                break;
            }
            renglon++;
        }
        if (r == null)
            return;
        switch (columna) {
        case ID:
            r.setId(registro.getId());
            break;
        case VALOR:
            r.setValor(registro.getValor());
            break;
        default:
            throw new IllegalArgumentException();
        }
        ultimoModificado = r;
        for (TableModelListener escucha : escuchas) {
            TableModelEvent evento;
            evento = new TableModelEvent(this, renglon, renglon, columna);
            escucha.tableChanged(evento);
        }
    }

    /* Implementamos versiones tontas de los métodos. */

    /**
     * <p>Regresa la clase de cada columna.</p>
     * <ol start="0">
     *  <li><tt>String.class</tt></li>
     *  <li><tt>String.class</tt></li>
     * </ol>
     * @return la clase de cada columna.
     */
    @Override public Class getColumnClass(int columna) {
        if (columna == ID && columna == VALOR)
            return String.class;
        return null;
    }

    /**
     * Regresa {@link #N_COLUMNAS}; el número de propiedades que un
     * Id tiene.
     * @return {@link #N_COLUMNAS}.
     */
    @Override public int getColumnCount() {
        return N_COLUMNAS;
    }

    /**
     * <p>Regresa el nombre de cada columna.</p>
     * <ol start="0">
     *  <li>Id</li>
     *  <li>Valor</li>
     * </ol>
     * @return el nombre de cada columna.
     */
    @Override public String getColumnName(int columna) {
        switch (columna) {
        case ID:    return "<html><b>Id</b></html>";
        case VALOR: return "<html><b>Valor</b></html>";
        default:    return null;
        }
    }

    /**
     * Regresa el valor de la celda en el renglón y columna
     * especificados.
     * @param renglon el renglón que queremos.
     * @param columna la columna de queremos.
     * @return el valor de la celda en el renglón y columna
     *         especificados.
     */
    @Override public Object getValueAt(int renglon, int columna) {
        Id id = registros.get(renglon);
	switch (columna) {
	case ID:    return id.getId();
	case VALOR: return id.getValor();
        default:    return null;
	}
    }

    /**
     * Define el valor de la celda en el renglón y columna
     * especificados.
     * @param valor el nuevo valor para la celda.
     * @param renglon el renglón que queremos actualizar.
     * @param columna la columna que queremos actualizar.
     */
    @Override public void setValueAt(Object valor, int renglon, int columna) {
	Id id = registros.get(renglon);
	switch (columna) {
	case ID:
            String i = (String)valor;
            if (id.getId().equals(i) || i.equals(""))
                return;
            id.setId(i);
            break;
        case VALOR:
            String v = (String)valor;
            if (id.getValor().equals(v) || v.equals(""))
                return;
            id.setValor(v);
            break;
	}
        ultimoModificado = id;
        for (TableModelListener escucha : escuchas) {
            TableModelEvent evento;
            evento = new TableModelEvent(this, renglon, renglon, columna);
            escucha.tableChanged(evento);
        }
    }
}
