package mx.unam.ciencias.icc;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.lang.System;


/**
 * Clase para bases de datos de estudiantes.
 */
public class BaseDeDatosCanciones extends BaseDeDatos<Cancion> {

    /**
     * Constructor único.
     */
    public BaseDeDatosCanciones() {
        super();
    }

    /** Columna de nombre. */
    public static final int NAME     = 0;
    /** Columna de número de cuenta. */
    public static final int ARTIST     = 1;
    /** Columna de promedio. */
    public static final int SECONDS   = 2;
    /** Columna de edad. */
    public static final int RATING       = 3;
    
    public static final int PLAYS       = 4;
    /** Número de columnas en el modelo. */
    public static final int N_COLUMNAS = 5;
    
    
    /**
     * Crea un estudiante sin información relevante.
     * @return un estudiante sin información relevante.
     */
    @Override public Cancion creaRegistro() {
        return new Cancion(null,null,0.0f,new Rating(),0);
    }
    
//    
    public void agregaRegistro(Cancion e){
            super.agregaRegistro(e);
        
    }
    
    /**
     * Busca estudiantes por un campo específico.
     * @param campo el campo del registro por el cuál buscar; puede
     *              ser
     *              <ul>
     *               <li><tt>"nombre"</tt></li>
     *               <li><tt>"cuenta"</tt></li>
     *               <li><tt>"promedio"</tt></li>
     *               <li><tt>"edad"</tt></li>
     *              </ul>
     * @param texto el texto a buscar.
     * @return una lista con los estudiantes tales que en el campo
     *         especificado contienen el texto recibido.
     * @throws IllegalArgumentException si el campo no es ninguno de
     *         los especificados arriba.
     */
    @Override public Lista<Cancion> buscaRegistros(int campo, String texto) {
        Lista<Cancion> r = new Lista<Cancion>();
        
        
        for (Cancion c: registros)
        {
           
            String v = "";
            switch(campo)
            {
                case NAME: v = c.getName();
                    break;
            
                case ARTIST: v = c.getArtist();
                    break;
                    
                case SECONDS: v = String.valueOf(c.getSeconds());
                    break;
                case RATING:
                    v = String.valueOf(c.getRate().getRate());
                    break;
                case PLAYS: v = String.valueOf(c.getPlays());
                    break;
                default
                    :
                    throw new IllegalArgumentException("Se busca una columna inexsitneten");
            }
            
            if(campo == RATING)
            {
                if (v.equals(texto))
                    r.agregaFinal(c);
                
                
            }
            else

            {
                if (v.indexOf(texto)!=-1)
                    r.agregaFinal(c);
            }
        }
        return r;
    }
    
    
    private boolean cancionEQUALScancionWithCampo(Cancion c, Cancion e ,int campo)
    {
        
        
        
        boolean isEqual = false;
        
        if( (campo == NAME) || c.getName().equals(e.getName()))
            
            
            if(campo == ARTIST || c.getArtist().equals( e.getArtist()))
                
                if( campo == SECONDS || c.getSeconds() == e.getSeconds())
                    
                    if( campo == RATING || c.getRate().getRate() == e.getRate().getRate())
                        
                        if( campo == PLAYS || c.getPlays() == e.getPlays())
                        
                            isEqual = true;
        
        
        return isEqual;
    }
    @Override public void actualizaRegistro(Cancion cUpdated, int campo)
    {
        
        Cancion locatedSongs = null;
        for (Cancion  c: registros)
        {
            if (cancionEQUALScancionWithCampo(c,cUpdated,campo))
            {
                locatedSongs =c;
                break;
            }
        }
        
        if(locatedSongs != null)
        {
            int ind = registros.indiceDe(locatedSongs);
            
            switch(campo)
            {
                case 0:
                    locatedSongs.setName(cUpdated.getName());
                    break;
                case 1:
                                locatedSongs.setArtist(cUpdated.getArtist());
                    break;
                case 2:
                    locatedSongs.setSeconds(cUpdated.getSeconds());
                    break;
                case 3:
                                locatedSongs.setRate(cUpdated.getRate());
                    break;
                case 4:
                            locatedSongs.setPlays(cUpdated.getPlays());
                    
                    break;
                default:
                    
            }
            
            
            super.ultimoModificado = locatedSongs;
            for (TableModelListener l : escuchas)
                l.tableChanged(new TableModelEvent(this,ind,ind,campo,TableModelEvent.UPDATE));
            
        }
    }
    
    
    @Override public Class getColumnClass(int columna) {
        // Aquí va su código.
        Class<?> claseS;
        
        switch(columna)
        {
            case 0: claseS = String.class;
                break;
            case 1: claseS = String.class;
                break;
            case 2: claseS = Double.class;
                break;
            case 3: claseS = Rating.class;
                break;
            case 4: claseS = Integer.class;
                break;
            default: claseS = null;
                
        }
        return claseS;
    }
    @Override public int getColumnCount() {
        return N_COLUMNAS;
    }
    
    @Override public String getColumnName(int columna) {
        String claseS = "";
        
        switch(columna)
        {
            case 0: claseS = "<html><b>Name</b></html>";
                break;
            case 1: claseS = "<html><b>Artist</b></html>";
                break;
            case 2: claseS = "<html><b>Duration</b></html>";
                break;
            case 3: claseS = "<html><b>Rating</b></html>";
                break;
            case 4: claseS = "<html><b>Plays</b></html>";
                break;
            default: claseS = "";
        }
        return claseS;
    }
    
    
    
    @Override public Object getValueAt(int renglon, int columna) {
        
        Cancion c = registros.get(renglon);
        Object val;
        
        switch(columna)
        {
            case 0: val = c.getName();
                break;
            case 1: val = c.getArtist();
                break;
            case 2: val = c.getSeconds();
                break;
            case 3: val = c.getRate();
                break;
            case 4: val = c.getPlays();
                break;
            default: val = null;
        }
        return val;
    }
    @Override public void setValueAt(Object valor, int renglon, int columna) {
        
        Cancion cancion = registros.get(renglon);
        super.ultimoModificado = cancion;
        switch(columna)
        {
            case 0: String mr = (String)valor;
                if (mr == null || mr.equals(""))
                    break;
                cancion.setName((String)valor);
                break;
            case 1: String r = (String)valor;
                if (r == null || r.equals(""))
                    break;
                cancion.setArtist(r);
                break;
            case 2:
                double myD = ((Double)valor).doubleValue();
                if(myD>=0.0)
                    cancion.setSeconds(((Double)valor).doubleValue());
                break;
            case 3:
                
                System.out.printf(" %s",valor);
                int anotherInt = ((Integer)valor).intValue();
                if(anotherInt>0&& anotherInt<6)
                    cancion.setRate(new Rating(anotherInt));
                break;
            case 4:
                int bbI = ((Integer)valor).intValue();
                if(bbI>0)
                    cancion.setPlays(bbI);
                break;
            default:
        }
        for (TableModelListener e : escuchas)
        {
            e.tableChanged(new TableModelEvent(this,renglon,renglon,columna,TableModelEvent.UPDATE));
        }
        
        
    }
    @Override public boolean isCellEditable(int renglon, int columna) {
        
        boolean canEdit = true;
        if(columna == RATING)
        {
            Cancion c = registros.get(renglon);
            Cancion newC = c.copy();
            newC.getRate().aumentarRate();
            actualizaRegistro(newC,RATING);
            canEdit = false;
        }
        
        return canEdit;
    }
}
