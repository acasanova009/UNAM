package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Clase para hilos de ejecución que manejan conexiones del
 * servidor.
 */
public class HiloServidor<T extends Registro> extends Thread {

    /* El servidor de base de datos. */
    private ServidorBaseDeDatos<T> sbdd;
    /* La base de datos. */
    private BaseDeDatos<T> bdd;
    /* El enchufe. */
    private Socket enchufe;
    /* El nombre del cliente. */
    private String cliente;
    /* La entrada del cliente. */
    private BufferedReader in;
    /* La salida hacia el cliente. */
    private BufferedWriter out;
    /* El identificador del hilo. */
    private int id;

    /**
     * Construye un nuevo hilo de ejecución.
     * @param sbdd el servidor de bases de datos.
     * @param enchufe el enchufe de la conexión.
     */
    public HiloServidor(ServidorBaseDeDatos<T> sbdd,
                        BaseDeDatos<T> bdd,
                        Socket enchufe,
                        int id)
        throws IOException {
        this.sbdd = sbdd;
        this.bdd = bdd;
        this.id = id;
        this.enchufe = enchufe;
        in =
            new BufferedReader(
                new InputStreamReader(enchufe.getInputStream()));
        out =
            new BufferedWriter(
                new OutputStreamWriter(enchufe.getOutputStream()));
    }

    /**
     * Maneja la conexión con el cliente.
     */
    public void run() {
        cliente = enchufe.getInetAddress().getCanonicalHostName();
        log("Conexión recibida de " + clienteId());
        String linea = null;
        try {
            while ((linea = in.readLine()) != null) {
                Protocolo comando = Protocolo.obtenComando(linea);
                switch (comando) {
                case ENVIAR_BASE_DE_DATOS:
                    enviarBaseDeDatos();
                    break;
                case REGISTRO_AGREGADO:
                    registroAgregado();
                    break;
                case REGISTRO_ELIMINADO:
                    registroEliminado();
                    break;
                case REGISTRO_MODIFICADO:
                    registroModificado();
                    break;
                case COMANDO_INVALIDO:
                default:
                    log("Comando inválido de " + clienteId() +
                        ": " + comando);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log("Hubo un error al comunicarnos con el cliente " +
                clienteId() + ".");
            log("Nos desconectaremos del cliente " + clienteId() + ".");
            cierraTodo();
        }
        log("Conexión de " + clienteId() + " terminada.");
        sbdd.clienteDesconectado(this);
    }

    private String clienteId() {
        return id + " (" + cliente + ")";
    }

    /* Cierra la conexión con el cliente. */
    private void cierraTodo() {
        try {
            enchufe.close();
        } catch (Exception ioe) {
            /* No importa qué ocurra, terminamos el hilo de ejecución. */
        }
    }

    /* Manda un mensaje a la consola del servidor. */
    private void log(String mensaje) {
        System.err.println(mensaje);
    }

    /* Envía la base de datos al cliente. */
    private void enviarBaseDeDatos()
        throws IOException {
        bdd.guarda(out);
        out.newLine();
        out.flush();
        log("Base de datos enviada a " + clienteId());
    }

    /* Notifica a los demás hilos de la modificación. */
    private void notificaHilos(T registro, Protocolo accion, int columna)
        throws IOException {
        for (HiloServidor<T> hilo : sbdd.getHilos()) {
            if (hilo == this)
                continue;
            String c = Protocolo.generaComando(accion);
            hilo.out.write(c);
            hilo.out.newLine();
            if (columna != -1) {
                hilo.out.write(String.valueOf(columna));
                hilo.out.newLine();
            }
            registro.guarda(hilo.out);
            hilo.out.flush();
        }
    }

    /* Agrega un registro recibido. */
    private void registroAgregado()
        throws IOException {
        log("Recibiendo registro de " + clienteId() + " para agregar...");
        T registro = bdd.creaRegistro();
        if (!registro.carga(in)) {
            log("Registro inválido recibido de " + clienteId() +
                " para agregar.");
            return;
        }
        log("Registro recibido de " + clienteId() + ".");
        synchronized(bdd) {
            bdd.agregaRegistro(registro);
            sbdd.guarda();
        }
        notificaHilos(registro, Protocolo.REGISTRO_AGREGADO, -1);
        log("Registro agregado de " + clienteId() + ".");
    }

    /* Elimina un registro recibido. */
    private void registroEliminado()
        throws IOException {
        log("Recibiendo registro de " + clienteId() + " para eliminar...");
        T registro = bdd.creaRegistro();
        if (!registro.carga(in)) {
            log("Registro inválido recibido de " + clienteId() +
                " para eliminar.");
            return;
        }
        log("Registro recibido de " + clienteId() + ".");
        synchronized(bdd) {
            bdd.eliminaRegistro(registro);
            sbdd.guarda();
        }
        notificaHilos(registro, Protocolo.REGISTRO_ELIMINADO, -1);
        log("Registro agregado de " + clienteId() + ".");
    }

    /* Modifica un registro recibido. */
    private void registroModificado()
        throws IOException {
        log("Recibiendo columna de " + clienteId() +
            " para modificar registro...");
        String c = in.readLine();
        final int columna;
        try {
            columna = Integer.parseInt(c);
        } catch (NumberFormatException nfe) {
            log("La columna a modificar no es válida: " + c + ".");
            return;
        }
        log("Columna recibida de " + clienteId() + ".");
        log("Recibiendo registro de " + clienteId() + " para modificar...");
        T registro = bdd.creaRegistro();
        if (!registro.carga(in)) {
            log("Registro inválido recibido de " + clienteId() +
                " para modificar.");
            return;
        }
        log("Registro recibido de " + clienteId() + ".");
        synchronized(bdd) {
            bdd.actualizaRegistro(registro, columna);
            sbdd.guarda();
        }
        notificaHilos(registro, Protocolo.REGISTRO_MODIFICADO, columna);
        log("Registro modificado de " + clienteId() + ".");
    }
}
