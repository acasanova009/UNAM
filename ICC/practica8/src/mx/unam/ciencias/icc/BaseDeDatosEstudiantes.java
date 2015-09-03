package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de estudiantes.
 */
public class BaseDeDatosEstudiantes extends BaseDeDatos<Estudiante> {

    /**
     * Constructor único.
     */
    public BaseDeDatosEstudiantes() {
        super();
    }

    /**
     * Crea un estudiante sin información relevante.
     * @return un estudiante sin información relevante.
     */
    @Override protected Estudiante creaRegistro() {
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
//        registros.primero();
//        while(registros.iteradorValido())
//        {
//            Estudiante e = registros.dame();
//            String v = "";
//            if(campo.equals("nombre"))
//                v = e.getNombre();
//            else if(campo.equals("cuenta"))
//                v = String.valueOf(e.getCuenta());
//            else if(campo.equals("promedio"))
//                v = String.valueOf(e.getPromedio());
//            else if(campo.equals("edad"))
//                v = String.valueOf(e.getEdad());
//            else
//                 throw new IllegalArgumentException("Problemas al buscar registros");
//            
////                case "nombre": v = e.getNombre(); break;
////                case "cuenta": v = String.valueOf(e.getCuenta());break;
////                case "promedio": v = String.valueOf(e.getPromedio());break;
////                case "edad": v = String.valueOf(e.getEdad());break;
////                default:
//            
//            if (v.indexOf(texto)!=-1)
//                r.agregaFinal(e);
//            registros.siguiente();
//            }
    
        return r;
    }
}
