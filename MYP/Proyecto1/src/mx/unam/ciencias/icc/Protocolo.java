package mx.unam.ciencias.icc;

public enum Protocolo {

    /**
     * El cliente solicita toda la base de datos. Si este mensaje es
     * recibido por el servidor, debe contestar enviando toda la
     * base de datos al cliente. Un cliente ignorará este mensaje
     * después de imprimir una advertencia en consola.
     */
    ENVIAR_BASE_DE_DATOS,

    /**
     * El interlocutor agregó un registro. Si este mensaje es
     * recibido por el servidor o el cliente, inmediatamente después
     * recibirá un registro que debe agregar a la base de datos.
     */
    REGISTRO_AGREGADO,

    /**
     * El interlocutor eliminó un registro. Si este mensaje es
     * recibido por el servidor o el cliente, inmediatamente después
     * recibirá un registro que debe eliminar de la base de datos.
     */
    REGISTRO_ELIMINADO,

    /**
     * El interlocutor modificó un registro. Si este mensaje es
     * recibido por el servidor o el cliente, inmediatamente después
     * recibirá dos registros: el primero será el registro original,
     * y el segundo los nuevos valores de dicho registro.
     */
    REGISTRO_MODIFICADO,

    /**
     * El comando recibido no es reconocido.
     */
    COMANDO_INVALIDO;

    /**
     * Descifra un comando recibido.
     * @param linea la línea de texto con el comando. La cadena
     *        recibida debe comenzar con "|=COMANDO:", seguido del
     *        comando, de otra forma se le considerará inválido.
     */
    public static Protocolo obtenComando(String linea) {

    Protocolo prt = COMANDO_INVALIDO;
        if (linea.startsWith("|=COMANDO:"))
        {
if(linea.equals("|=COMANDO:ENVIAR_BASE_DE_DATOS"))
prt = ENVIAR_BASE_DE_DATOS;
else if(linea.equals("|=COMANDO:REGISTRO_AGREGADO"))
prt = REGISTRO_AGREGADO;
else if(linea.equals("|=COMANDO:REGISTRO_ELIMINADO"))
prt = REGISTRO_ELIMINADO;
else if(linea.equals("|=COMANDO:REGISTRO_MODIFICADO"))
prt = REGISTRO_MODIFICADO;

        }

return prt;


        // Aquí va su código.
    }

    /**
     * Genera un comando válido.
     * @param comando el comando del protocolo que queremos generar.
     */
    public static String generaComando(Protocolo prt) {
        // Aquí va su código.

    String ww  = "|=COMANDO:COMANDO_INVALIDO";

    if(prt == ENVIAR_BASE_DE_DATOS){

    ww ="|=COMANDO:ENVIAR_BASE_DE_DATOS";

}

    else if (prt == REGISTRO_AGREGADO)

{        ww="|=COMANDO:REGISTRO_AGREGADO";}

    else if (prt == REGISTRO_ELIMINADO)
{        ww="|=COMANDO:REGISTRO_ELIMINADO";}

    else if (prt == REGISTRO_MODIFICADO)

{        ww="|=COMANDO:REGISTRO_MODIFICADO";}

        return ww;
    }
}
