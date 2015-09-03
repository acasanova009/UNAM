package mx.unam.ciencias.icc;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.lang.System;

/**
 * Clase para bases de datos de estudiantes.
 */
public class BaseDeDatosEstudiantes extends BaseDeDatos<Estudiante> {

    /** Columna de nombre. */
    public static final int NOMBRE     = 0;
    /** Columna de número de cuenta. */
    public static final int CUENTA     = 1;
    /** Columna de promedio. */
    public static final int PROMEDIO   = 2;
    /** Columna de edad. */
    public static final int EDAD       = 3;
    /** Número de columnas en el modelo. */
    public static final int N_COLUMNAS = 4;

    private static int rot;
    /**
     * Constructor único.
     */
    public BaseDeDatosEstudiantes() {
        // Aquí va su código.
        super();
    }

    /**
     * Crea un estudiante sin información relevante.
     * @return un estudiante sin información relevante.
     */
    @Override public Estudiante creaRegistro() {
        // Aquí va su código.
                return new Estudiante(null,0,0,0);
    }
    
    

    public void agregaRegistro(Estudiante e){
    
        boolean shouldAddIt = true;
        for (Estudiante ests : registros)
        {
            if (ests.getCuenta()==e.getCuenta())
                shouldAddIt  =false;
        }
        if(shouldAddIt)
        {
            super.agregaRegistro(e);
        }
    }

    /**
     * Busca estudiantes por una columna específica.
     * @param columna la columna del registro por la cuál buscar;
     *        debe ser una de {@link #NOMBRE}, {@link #CUENTA},
     *        {@link #PROMEDIO} o {@link #EDAD}.
     * @param texto el texto a buscar.
     * @return una lista con los estudiantes tales que en la columna
     *         especificada contienen el texto recibido.
     * @throws IllegalArgumentException si la columna no es ninguna de
     *         las especificadas arriba.
     */
    @Override public Lista<Estudiante> buscaRegistros(int campo, String texto) {
        Lista<Estudiante> r = new Lista<Estudiante>();
        for (Estudiante e: registros)
        {
            String v = "";
            if(campo == NOMBRE)
                v = e.getNombre();
            else if(campo == CUENTA)
                v = String.valueOf(e.getCuenta());
            else if(campo == PROMEDIO)
                v = String.valueOf(e.getPromedio());
            else if(campo == EDAD)
                v = String.valueOf(e.getEdad());
            else
                throw new IllegalArgumentException("Problemas al buscar registros");
            if (v.indexOf(texto)!=-1)
                r.agregaFinal(e);
            
        }
        return r;
    }

    /**
     * Actualiza un estudiante que ha cambiado en una columna.
     * @param registro el registro actualizado.
     * @param columna la columna que cambió en el registro: debe ser
     *        una de {@link #NOMBRE}, {@link #CUENTA}, {@link
     *        #PROMEDIO} o {@link #EDAD}.
     * @throws IllegalArgumentException si la columna no es ninguna de
     *         las especificadas arriba.
     */
    
    
    private boolean estudentEqualsEstudnetwithCampo(Estudiante oldE, Estudiante newE ,int campo)
    {
    
        

        boolean isEqual = false;
        
        if( (campo == NOMBRE) || oldE.getNombre().equals(newE.getNombre()))


            if(campo == PROMEDIO || oldE.getPromedio() == newE.getPromedio())

                if( campo == CUENTA || oldE.getCuenta() == newE.getCuenta())

                    if( campo == EDAD || oldE.getEdad() == newE.getEdad())
                    
                            isEqual = true;
                    
        
        return isEqual;
    }
    @Override public void actualizaRegistro(Estudiante eUpdated, int campo)
    {

        Estudiante locatedEstudent = null;
        for (Estudiante e : registros)
        {
            if (estudentEqualsEstudnetwithCampo(e,eUpdated,campo))
            {
                
                locatedEstudent = e;
                break;
            }
        }
        
       if(locatedEstudent != null)
        {
            int ind = registros.indiceDe(locatedEstudent);
            if(campo == NOMBRE)
                locatedEstudent.setNombre(eUpdated.getNombre());
            else if(campo == CUENTA)
                locatedEstudent.setCuenta(eUpdated.getCuenta());
            else if(campo == PROMEDIO)
                locatedEstudent.setPromedio(eUpdated.getPromedio());
            else if(campo == EDAD)
                locatedEstudent.setEdad(eUpdated.getEdad());
            
            super.ultimoModificado = locatedEstudent;
            for (TableModelListener l : escuchas)
                l.tableChanged(new TableModelEvent(this,ind,ind,campo,TableModelEvent.UPDATE));
            
        }

        
        
//        rotateFrom(campo);
//        Lista<Estudiante> eee = new Lista<Estudiante>();
//        
//    
//        for (int i = 0; i < (N_COLUMNAS-1); i++)
//        {
//            
//            int nextCampo = rotateNext();
//            String em = getEC(e,nextCampo);
//            if (i == 0)
//            {
//                if (em!=null)
//                    eee = buscaRegistrosEnLista(nextCampo,em,registros);
//            }else
//            {
//                eee = buscaRegistrosEnLista(nextCampo,em,eee);
//            }
//            
//        }
//    
//        for (Estudiante est : eee)
//        {
//            int ind = registros.indiceDe(est);
//            
//
//            if(campo == NOMBRE)
//                est.setNombre(e.getNombre());
//            else if(campo == CUENTA)
//                est.setCuenta(e.getCuenta());
//            else if(campo == PROMEDIO)
//                est.setPromedio(e.getPromedio());
//            else if(campo == EDAD)
//                est.setEdad(e.getEdad());
//            
//            super.ultimoModificado = est;
//            for (TableModelListener l : escuchas)
//            {
//                l.tableChanged(new TableModelEvent(this,ind,ind,campo,TableModelEvent.UPDATE));
//            }
//        }
    }
    private void log(String a)
    {
        System.out.println(a);
    }
    
    private String getEC(Estudiante e, int campo)
    {
        String v = null;
        
        if(campo == NOMBRE)
            v = e.getNombre();
        else if(campo == CUENTA)
            v = String.valueOf(e.getCuenta());
        else if(campo == PROMEDIO)
            v = String.valueOf(e.getPromedio());
        else if(campo == EDAD)
            v = String.valueOf(e.getEdad());
        return v;
        
    }
    
    private void rotateFrom(int campo)
    {
        rot = 0;
        for(int i = -1; i < campo; i++)
        {
            rotateNext();
        }
    }
    private int rotateNext()
    {
        if ((N_COLUMNAS) == rot)
        {
            rot = 0;
        }
        return rot++;
    }
    
    private Lista<Estudiante> buscaRegistrosEnLista(int campo, String texto, Lista<Estudiante> l )
    {
        Lista<Estudiante> r = new Lista<Estudiante>();
        for (Estudiante e: l)
        {

            String v = "";
            if(campo == NOMBRE)
                v = e.getNombre();
            else if(campo == CUENTA)
                v = String.valueOf(e.getCuenta());
            else if(campo == PROMEDIO)
                v = String.valueOf(e.getPromedio());
            else if(campo == EDAD)
                v = String.valueOf(e.getEdad());
            else
                throw new IllegalArgumentException("Problemas al buscar registros");
            
            if (v.equals(texto))
                r.agregaFinal(e);
        }
        return r;
    }

    /* Métodos de la interfaz TableModel. */

    /**
     * <p>Regresa la clase de cada columna.</p>
     * <ol start="0">
     *  <li><tt>String.class</tt></li>
     *  <li><tt>Integer.class</tt></li>
     *  <li><tt>Double.class</tt></li>
     *  <li><tt>Integer.class</tt></li>
     * </ol>
     * @return la clase de cada columna.
     */
    @Override public Class getColumnClass(int columna) {
        // Aquí va su código.
        Class<?> claseS;
        
        switch(columna)
        {
            case 0: claseS = String.class;
                break;
            case 1: claseS = Integer.class;
                break;
            case 2: claseS = Double.class;
                break;
            case 3: claseS = Integer.class;
                break;
            default: claseS = null;
        }
        return claseS;
    }

    /**
     * Regresa {@link #N_COLUMNAS}; el número de propiedades que un
     * estudiante tiene.
     * @return {@link #N_COLUMNAS}.
     */
    @Override public int getColumnCount() {
        return N_COLUMNAS;
    }

    /**
     * <p>Regresa el nombre de cada columna.</p>
     * <ol start="0">
     *  <li>Nombre</li>
     *  <li>Cuenta</li>
     *  <li>Promedio</li>
     *  <li>Edad</li>
     * </ol>
     * @return el nombre de cada columna.
     */
    @Override public String getColumnName(int columna) {
        String claseS = "";
        
        switch(columna)
        {
            case 0: claseS = "<html><b>Nombre</b></html>";
                break;
            case 1: claseS = "<html><b>Cuenta</b></html>";
                break;
            case 2: claseS = "<html><b>Promedio</b></html>";
                break;
            case 3: claseS = "<html><b>Edad</b></html>";
                break;
            default: claseS = "";
        }
        return claseS;
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
        
        Estudiante estudiante = registros.get(renglon);
        Object claseS;
        
        switch(columna)
        {
            case 0: claseS = estudiante.getNombre();
                break;
            case 1: claseS = estudiante.getCuenta();
                break;
            case 2: claseS = estudiante.getPromedio();
                break;
            case 3: claseS = estudiante.getEdad();
                break;
            default: claseS = null;
        }
        return claseS;
    }

    /**
     * Define el valor de la celda en el renglón y columna
     * especificados.
     * @param valor el nuevo valor para la celda.
     * @param renglon el renglón que queremos actualizar.
     * @param columna la columna que queremos actualizar.
     */
    @Override public void setValueAt(Object valor, int renglon, int columna) {
        
        Estudiante estudiante = registros.get(renglon);
        super.ultimoModificado = estudiante;
        
        switch(columna)
        {
            case 0: String mr = (String)valor;
                if (mr == null || mr.equals(""))
                    break;
                estudiante.setNombre((String)valor);
                break;
            case 1:
                int myInt = ((Integer)valor).intValue();
                if(myInt>499999 && myInt<1000000000)
                    estudiante.setCuenta(myInt);
                break;
            case 2:
                double myD = ((Double)valor).doubleValue();
                if(myD>=0.0 && myD<=10.0)
                    estudiante.setPromedio(((Double)valor).doubleValue());
                break;
            case 3:
                int anotherInt = ((Integer)valor).intValue();
                if(anotherInt>14 && anotherInt<100)
                    estudiante.setEdad(anotherInt);
                break;
            default:
        }
        for (TableModelListener e : escuchas)
        {
            e.tableChanged(new TableModelEvent(this,renglon,renglon,columna,TableModelEvent.UPDATE));
        }
        
    
}
    /**
     * Regresa <tt>true</tt>, siempre. Todas las celdas son
     * editables.
     * @return <tt>true</tt>.
     */
    @Override public boolean isCellEditable(int renglon, int columna) {
        
        boolean canEdit = true;
        if(columna == CUENTA)
            canEdit = false;
        
        return canEdit;
    }
}
