package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Práctica 10: Huellas digitales.
 */
public class Practica10 {

    public static void main(String[] args) {
        Random random = new Random();
        String[] letras = {
            "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z"
        };
        int n = 10 + random.nextInt(40);
        String mensaje = "";
        for (int i = 0; i < n; i++)
            mensaje += letras[random.nextInt(letras.length)];
        System.out.println("Mensaje: " + mensaje);
        HuellaDigital<String> bj = FabricaHuellasDigitales.
            getInstanciaString(FabricaHuellasDigitales.BJ_STRING);
        System.out.printf("BJ  : 0x%08x\n", bj.huellaDigital(mensaje));
        HuellaDigital<String> glib = FabricaHuellasDigitales.
            getInstanciaString(FabricaHuellasDigitales.GLIB_STRING);
        System.out.printf("GLib: 0x%08x\n", glib.huellaDigital(mensaje));
        HuellaDigital<String> xor = FabricaHuellasDigitales.
            getInstanciaString(FabricaHuellasDigitales.XOR_STRING);
        System.out.printf("XOR : 0x%08x\n", xor.huellaDigital(mensaje));
    }
}
