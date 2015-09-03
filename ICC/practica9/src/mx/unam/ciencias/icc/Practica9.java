package mx.unam.ciencias.icc;

import java.util.Locale;

/**
 * Práctica 9: Interfaces gráficas.
 */
public class Practica9 {

    public static void main(String[] args) {
        /* Para que la interfaz hable español de México. */
        Locale.setDefault(new Locale("es", "MX"));
        AdministradorEstudiantes adm = new AdministradorEstudiantes();
    }
}
