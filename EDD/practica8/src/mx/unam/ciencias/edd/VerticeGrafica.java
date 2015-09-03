package mx.unam.ciencias.edd;

/**
 * Interfaz para vértices de gráfica. Un vértice de gráfica puede
 * darnos el elemento en el vértice, recorrer sus vecinos, decirnos
 * el grado del vértice, y obtener o cambiar su color.
 */
public interface VerticeGrafica<T> extends Iterable<VerticeGrafica<T>> {

    /**
     * Regresa el elemento del vértice.
     * @return el elemento del vértice.
     */
    public T getElemento();

    /**
     * Regresa el grado del vértice.
     * @return el grado del vértice.
     */
    public int getGrado();

    /**
     * Regresa el color del vértice.
     * @return el color del vértice.
     */
    public Color getColor();

    /**
     * Define el color del vértice.
     * @param color el nuevo color del vértice.
     */
    public void setColor(Color color);
}
