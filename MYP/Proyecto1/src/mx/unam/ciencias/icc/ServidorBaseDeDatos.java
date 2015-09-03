package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Clase abstracta para servidores de bases de datos.
 */
public abstract class ServidorBaseDeDatos<T extends Registro> {

    /* La base de datos. */
    private BaseDeDatos<T> bdd;
    /* El nombre de archivo a utilizar. */
    private String archivo;
    /* El servidor de enchufes. */
    private ServerSocket servidor;
    /* El enchufe. */
    private Socket enchufe;
    /* El puerto. */
    private int puerto;
    /* Lista con los hilos del servidor. */
    private Lista<HiloServidor<T>> clientes;
    /* Contador para identificadores de hilos. */
    private int siguienteId;

    /**
     * Crea un nuevo servidor usando el archivo recibido para poblar
     * la base de datos.
     * @param archivo el archivo con el cual poblar la base de
     *        datos. Puede ser <tt>null</tt>, en cuyo caso la base
     *        de datos será vacía.
     * @param puerto el puerto dónde escuchar por conexiones.
     */
    public ServidorBaseDeDatos(String archivo, int puerto)
        throws IOException {
        this.archivo = archivo;
        this.puerto = puerto;
        servidor = new ServerSocket(puerto);
        clientes = new Lista<HiloServidor<T>>();
        bdd = creaBaseDeDatos();
        if (archivo != null) {
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(archivo)));
            bdd.carga(in);
            in.close();
            System.out.println("Base de datos cargada.");
        } else {
            this.archivo = "musica.dat";
        }
            
            System.out.println("Cargando base de datos de " +
                               archivo);
    }

    /**
     * Guarda la base de datos en disco.
     */
    public void guarda() {
        try {
            System.out.println("Guardando base de datos en " +
                               archivo + ".");
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(archivo)));
            bdd.guarda(out);
            out.close();
            System.out.println("Base de datos guardada ");
        } catch (IOException ioe) {
            System.err.println("Ocurrió un error al guardar " +
                               "la base de datos.");
        }
    }

    /**
     * Comienza a escuchar por conexiones de clientes.
     */
    public void sirve() {
        System.out.println("Escuchando en el puerto " + puerto);
        while (true) {
            
            try {
                
                
                System.err.println("Corriendo Infinitamente...");
                
                enchufe = servidor.accept();
                HiloServidor<T> hilo;
                hilo = new HiloServidor<T>(this, bdd, enchufe, ++siguienteId);
                hilo.start();
                synchronized (clientes) {
                    clientes.agregaFinal(hilo);
                }
            } catch (IOException ioe) {
                System.err.println("Error al recibir una conexión...");
            }
        }
    }

    /**
     * Deja de tomar en cuenta a uno de los clientes.
     * @param hilo el hilo de ejecución que maneja al cliente.
     */
    public void clienteDesconectado(HiloServidor<T> hilo) {
        siguienteId--;
        synchronized (clientes) {
            clientes.elimina(hilo);
        }
    }

    /**
     * Regresa una lista con todos los hilos de servidor.
     * @return una lista con todos los hilos de servidor.
     */
    public Lista<HiloServidor<T>> getHilos() {
        return clientes;
    }

    /**
     * Crea la base de datos concreta.
     */
    public abstract BaseDeDatos<T> creaBaseDeDatos();
}
