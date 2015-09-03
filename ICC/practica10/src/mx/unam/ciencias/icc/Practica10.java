package mx.unam.ciencias.icc;

import java.io.IOException;
import java.util.Locale;

/**
 * Práctica 10: Hilos de ejecución y enchufes.
 */
public class Practica10 {

    /* Imprime un mensaje de cómo usar el programa. */
    private static void uso() {
        System.out.println("Uso: java -jar practica10.jar " +
                           "[ --servidor puerto [archivo] | --cliente ]");
        System.exit(0);
    }

    /* Ejecuta el servidor. */
    private static void servidor(String[] args) {
        if (args.length < 2 || args.length > 3)
            uso();

        int puerto = 1234;
        try {
            puerto = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            uso();
        }

        String archivo = null;
        if (args.length == 3)
            archivo = args[2];

        try {
            ServidorBaseDeDatosEstudiantes servidor;
            servidor = new ServidorBaseDeDatosEstudiantes(archivo, puerto);
            servidor.sirve();
        } catch (IOException ioe) {
            System.out.println("Error al crear el servidor.");
        }
    }

    /* Ejecuta el cliente. */
    private static void cliente() {
        Locale.setDefault(new Locale("es", "MX"));
        AdministradorEstudiantes cliente = new AdministradorEstudiantes();
    }

    public static void main(String[] args) {
        
        System.out.println("ANTES DE MR");
        if (args.length < 1)
            uso();
        
        String firstArg;
        if(args.length >0)
        {
            firstArg = args[0];
            
            if( firstArg.equals("--cliente"))
            { cliente();
                return;
            }
            else if ( firstArg.equals("--servidor"))
            {
                servidor(args);
                return;
            }
        }
        else
        {
            uso();
        }
      
    }
}
