package mx.unam.ciencias.icc;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Clase para bases de datos de estudiantes.
 */
public class BaseDeDatosEstudiantes extends BaseDeDatos<Estudiante> {

    /* Constantes para columnas. */
    private static final int NOMBRE     = 0;
    private static final int CUENTA     = 1;
    private static final int PROMEDIO   = 2;
    private static final int EDAD       = 3;
    /** Número de columnas en el modelo. */
    public static final int N_COLUMNAS  = 4;

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
    @Override protected Estudiante creaRegistro() {
        // Aquí va su código.
        
        return new Estudiante(null,0,0,0);
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
    @Override public Lista<Estudiante> buscaRegistros(String campo, String texto) {
        Lista<Estudiante> r = new Lista<Estudiante>();
        for (Estudiante e: registros)
        {
            String v = "";
            if(campo.equals("nombre"))
                v = e.getNombre();
            else if(campo.equals("cuenta"))
                v = String.valueOf(e.getCuenta());
            else if(campo.equals("promedio"))
                v = String.valueOf(e.getPromedio());
            else if(campo.equals("edad"))
                v = String.valueOf(e.getEdad());
            else
                throw new IllegalArgumentException("Problemas al buscar registros");
            
            if (v.indexOf(texto)!=-1)
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
     * Regresa 4, siempre; el número de propiedades que un
     * estudiante tiene.
     * @return 4.
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
        
        TableModelListener l =  escuchas.getAny();
        if(l!=null)
            l.tableChanged(new TableModelEvent(this,renglon,renglon,columna,TableModelEvent.UPDATE));
        
    }
}
