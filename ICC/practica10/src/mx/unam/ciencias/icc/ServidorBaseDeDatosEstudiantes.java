package mx.unam.ciencias.icc;

import java.io.IOException;

/**
 * Clase para servidores de bases de datos de estudiantes.
 */
public class ServidorBaseDeDatosEstudiantes
    extends ServidorBaseDeDatos<Estudiante> {

    /**
     * @param archivo el archivo con el cual poblar la base de
     *        datos. Puede ser <tt>null</tt>, en cuyo caso la base
     *        de datos será vacía.
     * @param puerto el puerto dónde escuchar por conexiones.
     * Construye un servidor de base de datos de estudiantes.
     */
    public ServidorBaseDeDatosEstudiantes(String archivo, int puerto)
        throws IOException {
        super(archivo, puerto);
    }

    /**
     * Crea una base de datos de estudiantes.
     * @return una base de datos de estudiantes.
     */
    @Override public BaseDeDatos<Estudiante> creaBaseDeDatos() {
        return new BaseDeDatosEstudiantes();
    }
}
