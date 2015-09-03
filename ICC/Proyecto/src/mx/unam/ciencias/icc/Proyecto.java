package mx.unam.ciencias.icc;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Práctica 8: Iteradores.
 */
public class Proyecto {

    /* Hace búsquedas por nombre y número de cuenta en la base de
       datos. */
    private static void busquedas(BaseDeDatosCanciones bdd) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");

        System.out.printf("Entra un nombre de cancion para buscar: ");
        String nombre = sc.next();

        Lista<Cancion> r = bdd.buscaRegistros("name", nombre);
        if (r.getLongitud() == 0) {
            System.out.printf("\nNo se hallaron canciones con el nombre \"%s\".\n",
                              nombre);
        } else {
            System.out.printf("\nSe hallaron las siguientes canciones con el nombre \"%s\":\n\n",
                              nombre);
            for (Cancion e : r)
                System.out.println(e + "\n");
        }

        System.out.printf("Entra una nivel de puntaje [0-5] para buscar: ");
        int cuenta = 0;
        try {
            cuenta = sc.nextInt();
        } catch (InputMismatchException ime) {
            System.out.printf("Se entró una cuenta inválida. " +
                              "Se interpretará como cero.\n");
        }

        r = bdd.buscaRegistros("rating", String.valueOf(cuenta));
        if (r.getLongitud() == 0) {
            System.out.printf("\nNo se hallaron canciones con la cuenta \"%d\".\n",
                              cuenta);
        } else {
            System.out.printf("\nSe hallaron los siguientes estudiantes con la cuenta \"%d\":\n\n",
                              cuenta);
            for (Cancion e : r)
                System.out.println(e + "\n");
        }
    }

    /* Crea una base de datos y la llena a partir de los datos que
       el usuario escriba a través del teclado. Después la guarda en
       disco duro y la regresa. */
    private static BaseDeDatosCanciones escritura(String nombreArchivo) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");

        File archivo = new File(nombreArchivo);

        if (archivo.exists()) {
            System.out.printf("El archivo \"%s\" ya existe.\n" +
                              "Presiona Ctrl-C si no quieres reescribirlo, " +
                              "o Enter para continuar...\n", nombreArchivo);
            sc.nextLine();
        }

        System.out.println("Entra canciones a la base de datos.\n" +
                           "Cuando desees terminar, deja el nombre en blanco.\n");

        BaseDeDatosCanciones bdd = new BaseDeDatosCanciones();

        do {
            String name;
            String artist;
            double seconds = 0.0;
            Rating rate =  new Rating();
            int plays = 0;

            System.out.printf("Name   : ");
            name = sc.next();
            if (name.equals(""))
                break;
            try {
                System.out.printf("Artista   : ");
                artist = sc.next();
                
                System.out.printf("Duracion en segundos : ");
                seconds = sc.nextDouble();
                
                System.out.printf("Rate: (Entero entre [0-5])   : ");
                rate.setRate(sc.nextInt());
                
                System.out.printf("Plays (Cantidad de veces escuchada) : ");
                plays = sc.nextInt();
                
            } catch (InputMismatchException ime) {
                System.out.println("\nNúmero inválido: se descartará este estudiante.\n");
                continue;
            }
            Cancion c = new Cancion(name,artist,seconds,rate,plays);
            
            System.out.printf("En base de datos: Cancion: %s\n",c);
            
            bdd.agregaRegistro(c);
            System.out.println();
        } while (true);

        int n = bdd.getNumRegistros();
        if (n == 1)
            System.out.printf("\nSe agregó una cancion.\n");
        else
            System.out.printf("\nSe agregaron %d canciones.\n", n);

        try {
            FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
            OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
            BufferedWriter out = new BufferedWriter(osOut);
            bdd.guarda(out);
            out.close();
        } catch (IOException ioe) {
            System.out.printf("No pude guardar en el archivo \"%s\".\n",
                              nombreArchivo);
            System.exit(1);
        }

        System.out.printf("\nBase de datos guardada exitosamente en \"%s\".\n",
                          nombreArchivo);

        return bdd;
    }

    /* Crea una base de datos y la llena cargándola del disco
       duro. Después la regresa. */
    private static BaseDeDatosCanciones lectura(String nombreArchivo) {
        BaseDeDatosCanciones bdd = new BaseDeDatosCanciones();

        try {
            FileInputStream fileIn = new FileInputStream(nombreArchivo);
            InputStreamReader isIn = new InputStreamReader(fileIn);
            BufferedReader in = new BufferedReader(isIn);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            System.out.printf("No pude cargar del archivo \"%s\".\n",
                              nombreArchivo);
            System.exit(1);
        }

        System.out.printf("Base de datos cargada exitosamente de \"%s\".\n\n",
                          nombreArchivo);

        Lista<Cancion> r = bdd.getRegistros();
        for (Cancion e : r)
            System.out.println(e + "\n");

        return bdd;
    }

    /* Imprime en pantalla cómo debe usarse el programa y lo termina. */
    private static void uso() {
        System.out.println("Uso: java -jar proyecto.jar [-e|-l] <archivo>");
        System.exit(1);
    }


    public static void main(String[] args) {
//        Cancion c = new Cancion("HOla","Adios",1,new Rating(2),3);
//        
//        System.out.printf("Cancion; %s\n",c);
        
        if (args.length != 2)
            uso();

        String bandera = args[0];
        String nombreArchivo = args[1];

        if (!bandera.equals("-e") && !bandera.equals("-l"))
            uso();

        BaseDeDatosCanciones bdd;

        if (bandera.equals("-e"))
            bdd = escritura(nombreArchivo);
        else
            bdd = lectura(nombreArchivo);

        busquedas(bdd);
    }
}
