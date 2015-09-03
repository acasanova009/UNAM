package mx.unam.ciencias.icc.test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Clase para escuchas de modelos de tabla.
 */
public class Escucha implements TableModelListener {

    /* Evento. */
    private TableModelEvent evento;

    /**
     * Esta notificación le dice a los escuchas el rango exacto de
     * celdas, renglones, or columnas que cambiaron.
     * @param evento el evento que ocurrió en la tabla.
     */
    public void tableChanged(TableModelEvent evento) {
        this.evento = evento;
    }

    /**
     * Regresa el evento del escucha.
     * @return el evento del escucha.
     */
    public TableModelEvent getEvento() {
        return evento;
    }
}
