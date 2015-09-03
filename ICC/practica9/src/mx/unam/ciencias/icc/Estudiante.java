package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Clase para representar estudiantes. Un estudiante tiene nombre,
 * número de cuenta, promedio y edad. La clase implementa {@link
 * Registro}, por lo que puede cargarse y guardarse utilizando
 * objetos de las clases {@link BufferedReader} y {@link
 * BufferedWriter} como entrada y salida respectivamente.
 */
public class Estudiante implements Registro{

    /* Nombre del estudiante. */
    private String nombre;
    /* Número de cuenta. */
    private int cuenta;
    /* Pormedio del estudiante. */
    private double promedio;
    /* Edad del estudiante.*/
    private int edad;

    /**
     * Construye un estudiante con todas sus propiedades.
     * @param nombre el nombre del estudiante.
     * @param cuenta el número de cuenta del estudiante.
     * @param promedio el promedio del estudiante.
     * @param edad la edad del estudiante.
     */
    public Estudiante(String nombre,
                      int    cuenta,
                      double promedio,
                      int    edad) {
        this.nombre = nombre;
        this.cuenta = cuenta;
        this.promedio = promedio;
        this.edad = edad;
    }

    /**
     * Regresa el nombre del estudiante.
     * @return el nombre del estudiante.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el nombre del estudiante.
     * @param nombre el nuevo nombre del estudiante.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Regresa el número de cuenta del estudiante.
     * @return el número de cuenta del estudiante.
     */
    public int getCuenta() {
        return cuenta;
    }

    /**
     * Define el número cuenta del estudiante.
     * @param cuenta el nuevo número de cuenta del estudiante.
     */
    public void setCuenta(int cu) {
        cuenta = cu;
    }

    /**
     * Regresa el promedio del estudiante.
     * @return el promedio del estudiante.
     */
    public double getPromedio() {
        return promedio;
    }

    /**
     * Define el promedio del estudiante.
     * @param promedio el nuevo promedio del estudiante.
     */
    public void setPromedio(double prom) {
        promedio = prom;
    }

    /**
     * Regresa la edad del estudiante.
     * @return la edad del estudiante.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Nos dice si el objeto recibido es un estudiante igual al que
     * manda llamar el método.
     * @param o el objeto con el que el estudiante se comparará.
     * @return <tt>true</tt> si el objeto o es un estudiante con las
     *         mismas propiedades que el objeto que manda llamar al
     *         método, <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object obj) {
        boolean isEqual = false;
        
        
            
        if ((obj instanceof Estudiante) && obj !=null)
        {
           @SuppressWarnings("unchecked")  Estudiante est = (Estudiante)obj;
            if(this.nombre.equals(est.nombre))
                if(this.promedio == est.promedio)
                    if(this.edad == est.edad)
                        if(this.cuenta == est.cuenta)
                            isEqual = true;
        }
        return isEqual;
    }

    /**
     * Regresa una representación en cadena del estudiante.
     * @return una representación en cadena del estudiante.
     */
    @Override public String toString() {
        
        return String.format("Nombre   : %s\n" +
                      "Cuenta   : %d\n" +
                      "Promedio : %2.2f\n" +
                      "Edad     : %d",
                      nombre, cuenta, promedio, edad);
    }

    /**
     * Guarda al estudiante en la salida recibida.
     * @param out la salida dónde hay que guardar al estudiante.
     * @throws IOException si un error de entrada/salida ocurre.
     */
    @Override public void guarda(BufferedWriter out) throws IOException{
        
        try{
            out.write(String.format("%s\t%d\t%2.2f\t%d\n",
                                    nombre, cuenta, promedio, edad));
        }catch (IOException ioe){
            throw new IOException("Fallo al guardar el estudiante.");
        }
    }

    /**
     * Carga al estudiante de la entrada recibida.
     * @param in la entrada de dónde hay que cargar al estudiante.
     * @return <tt>true</tt> si el método carga un estudiante
     *         válido, <tt>false</tt> en otro caso.
     * @throws IOException si un error de entrada/salida ocurre.
     */
    @Override public boolean carga(BufferedReader in) throws IOException
    {
        
    
        String l = in.readLine();
        
        if (l==null || in == null || l.equals(""))
            return false;
        l.trim();
        String [] p= l.split("\t");
        
        if (p.length != 4)
            throw new IOException();
        
        nombre = p[0];
        try
        {
            cuenta = Integer.parseInt(p[1]);
            promedio = Double.parseDouble(p[2]);
            edad = Integer.parseInt(p[3]);
            
        }
        catch (NumberFormatException nfe)
        {
            throw new IOException("The database data, such as numbers is incorrect is other type of inofrmation.");
        }
        
        return true;
        
    }
    
}
