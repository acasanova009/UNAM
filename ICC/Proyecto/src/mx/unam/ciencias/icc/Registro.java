package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Interfaz para registros. Los registros deben de poder guardarse
 * utilizando una instancia de {@link BufferedWriter}, y cargarse
 * utilizando una instancia de {@link BufferedReader}.
 */
public interface Registro {

    /**
     * Guarda el registro en la salida recibida.
     * @param out la salida donde hay que guardar el registro.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public void guarda(BufferedWriter out) throws IOException;

    /**
     * Carga el registro de la entrada recibida.
     * @param in la entrada de donde hay que cargar el registro.
     * @return <tt>true</tt> si un registro válido fue leído;
     *         <tt>false</tt> en otro caso.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public boolean carga(BufferedReader in) throws IOException;
}
