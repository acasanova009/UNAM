package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.SwingUtilities;

/**
 * Clase para hilos de ejecución que manejan conexiones de
 * servidores.
 */
public class HiloCliente<T extends Registro> extends Thread {

    /* La base de datos. */
    private BaseDeDatos<T> bdd;
    /* El enchufe. */
    private Socket enchufe;
    /* La entrada del servidor. */
    private BufferedReader in;
    /* La salida hacia el servidor. */
    private BufferedWriter out;
    /* Bandera para puestro escucha. */
    private boolean ignorar;
    /* Bandera de sincronización. */
    private boolean espera;
    /* Bandera de terminación. */
    private boolean terminado;

    /**
     * Crea un nuevo hilo de ejecución para manejar la conexión de
     * un cliente.
     * @param b la base de datos.
     * @param enchufe el enchufe conectado al servidor.
     */
    public HiloCliente(BaseDeDatos<T> b, Socket enchufe)
        throws IOException {
        bdd = b;
        bdd.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    
                    
                    
                    System.out.println("\nEvent: ");
                    if (ignorar)
                    {
                        System.out.println("IGNORED");
                        return;
                    }
                    int columna = -1;
                    Protocolo accion = null;
                    
                    switch (e.getType()) {
                    case TableModelEvent.INSERT:
                            log("INSERTED");
                        accion = Protocolo.REGISTRO_AGREGADO;
                        break;
                    case TableModelEvent.UPDATE:
                            log("UPDATE");
                        accion = Protocolo.REGISTRO_MODIFICADO;
                        columna = e.getColumn();
                        break;
                    case TableModelEvent.DELETE:
                            log("DELETE");
                        accion = Protocolo.REGISTRO_ELIMINADO;
                        break;
                    default:
                        return;
                    }
                    
                    T registro = bdd.getUltimoModificado();
                    try {
                        out.write(Protocolo.generaComando(accion));
                        out.newLine();
                        if (columna != -1) {
                            out.write(String.valueOf(columna));
                            out.newLine();
                        }
                        registro.guarda(out);
                        out.flush();
                    } catch (IOException ioe) {
                        log("Hubo un error enviando " +
                            "información al servidor.");
                    }
                }
            });
        
        this.enchufe = enchufe;
        in =
            new BufferedReader(
                new InputStreamReader(enchufe.getInputStream()));
        out =
            new BufferedWriter(
                new OutputStreamWriter(enchufe.getOutputStream()));
    }

    /*
     * Método principal del hilo de ejecución.
     */
    public void run() {
        try {
            out.write(Protocolo.generaComando(Protocolo.ENVIAR_BASE_DE_DATOS));
            out.newLine();
            out.flush();
            espera = true;
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ignorar = true;
                        try {
                            bdd.carga(in);
//                            System.out.printf("BDD recibida %s\n ",bdd);
                        } catch (IOException ioe) {
                            log("Hubo un error al comunicarnos " +
                                "con el servidor.");
                            log("Nos desconectaremos.");
                            cierraTodo();
                        }
                        ignorar = false;
                        espera = false;
                    }
                });
            while (espera)
                Thread.sleep(100);
            String linea = null;
            while ((linea = in.readLine()) != null) {
                Protocolo comando = Protocolo.obtenComando(linea);
                switch (comando) {
                case ENVIAR_BASE_DE_DATOS:
                    log("Error: clientes no pueden enviar la base de datos.");
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
                    log("Comando inválido: " + linea);
                }
            }
        } catch (IOException ioe) {
            if (!terminado) {
                log("Hubo un error al comunicarnos con el servidor.");
                log("Nos desconectaremos.");
                cierraTodo();
            }
        } catch (InterruptedException ie) {
            // Nada que hacer.
        }
        ignorar = true;
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    bdd.limpia();
                }
            });
    }

    /**
     * Cierra la conexión con el servidor.
     */
    public void cierraTodo() {
        terminado = true;
        try {
            enchufe.close();
        } catch (Exception ioe) {
            /* No importa qué ocurra, terminamos el hilo de ejecución. */
        }
    }

    /* Manda un mensaje a la consola del cliente. */
    private void log(String mensaje) {
        System.err.println(mensaje);
    }

    /* Agrega un registro recibido. */
    private void registroAgregado()
        throws IOException {
        final T registro = bdd.creaRegistro();
        if (!registro.carga(in)) {
            log("Registro inválido recibido para agregar.");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ignorar = true;
                        bdd.agregaRegistro(registro);
                        ignorar = false;
                    }
                });
    }

    /* Elimina un registro recibido. */
    private void registroEliminado()
        throws IOException {
        final T registro = bdd.creaRegistro();
        if (!registro.carga(in)) {
            log("Registro inválido recibido para eliminar.");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ignorar = true;
                        bdd.eliminaRegistro(registro);
                        ignorar = false;
                    }
            });
    }

    /* Modifica un registro recibido. */
    private void registroModificado()
        throws IOException {
        String c = in.readLine();
        final int columna;
        try {
            columna = Integer.parseInt(c);
        } catch (NumberFormatException nfe) {
            log("La columna a modificar no es válida: " + c + ".");
            return;
        }
        final T registro = bdd.creaRegistro();
        if (!registro.carga(in)) {
            log("Registro inválido recibido al modificar.");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ignorar = true;
                        bdd.actualizaRegistro(registro, columna);
                        ignorar = false;
                    }
            });
    }
}
