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
public class Cancion implements Registro {

    /* Nombre del estudiante. */
    private String name;
    /* Número de cuenta. */
    private String artist ;
    /* Pormedio del estudiante. */
    private double seconds;
    /* Edad del estudiante.*/
    private Rating rate;
    /*Cantidad de veces escuchada*/
    private int plays;

    /**
     * Construye un estudiante con todas sus propiedades.
     * @param nombre el nombre del estudiante.
     * @param cuenta el número de cuenta del estudiante.
     * @param promedio el promedio del estudiante.
     * @param edad la edad del estudiante.
     */
    public Cancion(String nombre,
                    String    artist,
                      double tiempo,
                      Rating rateing,
                      int plays) {
        this.name = nombre;
        this.artist = artist;
        this.seconds = tiempo;
        this.rate = rateing;
        this.plays = plays;
    }

    public Cancion copy()
    {
        return new Cancion(this.name,this.artist,this.seconds,this.rate,this.plays);
    }
    /**
     * Regresa el nombre del estudiante.
     * @return el nombre del estudiante.
     */
    public String getName() {
        return name;
    }

    /**
     * Define el nombre del estudiante.
     * @param nombre el nuevo nombre del estudiante.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Regresa el número de cuenta del estudiante.
     * @return el número de cuenta del estudiante.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Define el número cuenta del estudiante.
     * @param cuenta el nuevo número de cuenta del estudiante.
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Regresa el promedio del estudiante.
     * @return el promedio del estudiante.
     */
    public double getSeconds() {
        return this.seconds;
    }

    /**
     * Define el promedio del estudiante.
     * @param promedio el nuevo promedio del estudiante.
     */
    public void setSeconds(double prom) {
        this.seconds = prom;
    }

    /**
     * Regresa la edad del estudiante.
     * @return la edad del estudiante.
     */
    public Rating getRate() {
        return this.rate;
    }

    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public void setRate(Rating ratin) {
        this.rate = ratin;
    }
    
    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public void setPlays(int i ) {
        this.plays = i;
    }

    
    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public int getPlays() {
        return this.plays;
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
        if (obj!=null  && (obj instanceof Cancion) )
        {
           @SuppressWarnings("unchecked")  Cancion est = (Cancion)obj;
            if(this.name.equals(est.name))
                if(this.artist.equals(est.artist))
                    if(this.seconds == est.seconds)
                        if(this.rate.equals(est.rate))
                            if(this.plays == est.plays)
                                isEqual = true;
        }
        return isEqual;
    }

    /**
     * Regresa una representación en cadena del estudiante.
     * @return una representación en cadena del estudiante.
     */
    @Override public String toString() {
        
        return String.format(
                      "Name: %s\t" +
                      "Artist: %s\t" +
                      "Seconds: %2.2f\t" +
                      "Rate: %s\t"+
                      "Plays: %d\n",
                            
                      name, artist, seconds, rate, plays);
    }

    /**
     * Guarda al estudiante en la salida recibida.
     * @param out la salida dónde hay que guardar al estudiante.
     * @throws IOException si un error de entrada/salida ocurre.
     */
    
    
    
    @Override public void guarda(BufferedWriter out) throws IOException{
        try{
            out.write(String.format("%s\t%s\t%f\t%d\t%d\n",
                                    name, artist, seconds, rate.getRate(),plays));
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
        
        if (p.length != 5)
            throw new IOException();
        
        
        try
        {
            name = p[0];
            artist = p[1];
            seconds = Double.parseDouble(p[2]);
            rate.setRate(Integer.parseInt(p[3]));
            plays = Integer.parseInt(p[4]);
            
        }
        catch (NumberFormatException nfe)
        {
            throw new IOException("The database data, such as numbers is incorrect is other type of inofrmation.");
        }
        
        return true;
        
    }
    
}
