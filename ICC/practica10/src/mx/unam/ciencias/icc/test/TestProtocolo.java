package mx.unam.ciencias.icc.test;

import mx.unam.ciencias.icc.Protocolo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la enumeración {@link Protocolo}.
 */
public class TestProtocolo {

    /**
     * Prueba unitaria para {@link Protocolo#obtenComando}.
     */
    @Test public void testObtenComando() {
        String linea = "|=COMANDO:ENVIAR_BASE_DE_DATOS";
        Assert.assertTrue(Protocolo.obtenComando(linea) ==
                          Protocolo.ENVIAR_BASE_DE_DATOS);
        linea = "|=COMANDO:REGISTRO_AGREGADO";
        Assert.assertTrue(Protocolo.obtenComando(linea) ==
                          Protocolo.REGISTRO_AGREGADO);
        linea = "|=COMANDO:REGISTRO_ELIMINADO";
        Assert.assertTrue(Protocolo.obtenComando(linea) ==
                          Protocolo.REGISTRO_ELIMINADO);
        linea = "|=COMANDO:REGISTRO_MODIFICADO";
        Assert.assertTrue(Protocolo.obtenComando(linea) ==
                          Protocolo.REGISTRO_MODIFICADO);
        linea = "|=COMANDO:COMANDO_INVALIDO";
        Assert.assertTrue(Protocolo.obtenComando(linea) ==
                          Protocolo.COMANDO_INVALIDO);
        linea = "xXXx";
        Assert.assertTrue(Protocolo.obtenComando(linea) ==
                          Protocolo.COMANDO_INVALIDO);
    }

    /**
     * Prueba unitaria para {@link Protocolo#generaComando}.
     */
    @Test public void testGeneraComando() {
        String comando;
        comando = Protocolo.generaComando(Protocolo.ENVIAR_BASE_DE_DATOS);
        Assert.assertTrue(comando.equals("|=COMANDO:ENVIAR_BASE_DE_DATOS"));
        comando = Protocolo.generaComando(Protocolo.REGISTRO_AGREGADO);
        Assert.assertTrue(comando.equals("|=COMANDO:REGISTRO_AGREGADO"));
        comando = Protocolo.generaComando(Protocolo.REGISTRO_ELIMINADO);
        Assert.assertTrue(comando.equals("|=COMANDO:REGISTRO_ELIMINADO"));
        comando = Protocolo.generaComando(Protocolo.REGISTRO_MODIFICADO);
        Assert.assertTrue(comando.equals("|=COMANDO:REGISTRO_MODIFICADO"));
        comando = Protocolo.generaComando(Protocolo.COMANDO_INVALIDO);
        Assert.assertTrue(comando.equals("|=COMANDO:COMANDO_INVALIDO"));
    }
}
