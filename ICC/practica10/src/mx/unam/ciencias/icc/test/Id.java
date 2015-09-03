package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import mx.unam.ciencias.icc.Registro;


/**
 * Clase tonta que implementa Registro para ser utilizada en las
 * pruebas.
 */
public class Id implements Registro {

    /* Id del Id. */
    private String id;
    /* Valor del id. */
    private String valor;

    /**
     * Crea un Id con el id y valores especificados.
     * @param id el id del Id.
     * @param valor el valor del Id.
     */
    public Id(String id, String valor) {
        this.id = id;
        this.valor = valor;
    }

    /**
     * Regresa el id del Id.
     * @return el id del Id.
     */
    public String getId() {
        return id;
    }

    /**
     * Define el id del Id.
     * @param id el nuevo id del Id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Regresa el valor del Id.
     * @return el valor del Id.
     */
    public String getValor() {
        return valor;
    }

    /**
     * Define el valor del Id.
     * @param valor el nuevo valor del Id.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Nos dice si el Id es igual al objeto recibido.
     * @param o el objeto con el cual comparar el Id.
     * @return <tt>true</tt> si o es un Id con id igual al id del
     *         Id, <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Id))
            return false;
        Id i = (Id)o;
        return id.equals(i.id);
    }

    /**
     * Carga el Id de la entrada recibida.
     * @param in la entrada de dónde cargar el Id.
     * @return <tt>true</tt> si el Id se cargó correctamente,
     *         <tt>false</tt> en otro caso.
     * @throws IOException si ocurre algún error al tratar de cargar
     *         el Id.
     */
    @Override public boolean carga(BufferedReader in)
        throws IOException {
        String linea = in.readLine();
        if (linea == null)
            return false;
        if (linea.equals(""))
            return false;
        id = linea;
        return true;
    }

    /**
     * Guarda el Id en la salida recibida.
     * @param out la salida dónde guardar el Id.
     * @throws IOException si ocurre algún error al tratar de cargar
     *         el Id.
     */
    @Override public void guarda(BufferedWriter out) throws IOException {
        out.write(id + "\n");
    }
}
