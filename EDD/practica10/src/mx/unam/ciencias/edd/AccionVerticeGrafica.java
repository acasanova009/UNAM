package mx.unam.ciencias.edd;

/**
 * Interfaz para realizar acciones sobre iteradores de gráficas.
 */
public interface AccionVerticeGrafica<T> {

    /**
     * Realiza una acción sobre un vértice de gráfica.
     * @param vertice el vértice sobre el que se ralizará la
     *                acción.
     */
    public void actua(VerticeGrafica<T> vertice);
}
