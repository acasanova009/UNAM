package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 11: Diccionarios.
 */
public class Practica11 {

    public static void main(String[] args) {
        Random random = new Random();
        int N = 1000000;
        long tiempoInicial, tiempoTotal;
        NumberFormat nf = NumberFormat.getIntegerInstance();

        String[] arreglo = new String[N];
        int ini = random.nextInt();
        for (int i = 0; i < N; i++)
            arreglo[i] = String.format("%x", ini + i * 1000);

        int idx = random.nextInt(N);

//        System.out.println("XOR ==================================:");
        Diccionario<String, String> dicXOR;
        int th = FabricaHuellasDigitales.XOR_STRING;
        HuellaDigital<String> xor = FabricaHuellasDigitales.getInstanciaString(th);
        dicXOR = new Diccionario<String, String>(N, xor);
        tiempoInicial = System.nanoTime();
//        for (int i = 0; i < N; i++)
//            dicXOR.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
//        System.out.printf("%2.9f segundos en llenar un diccionario con %s elementos.\n",
//                          (tiempoTotal/1000000000.0), nf.format(N));
//        System.out.printf("Máximo número de colisiones: %d.\n", dicXOR.colisionMaxima());
//        System.out.printf("Colisiones: %s.\n", nf.format(dicXOR.colisiones()));
//        System.out.printf("Carga: %g.\n", dicXOR.carga());
        tiempoInicial = System.nanoTime();
        String s ="";//= dicXOR.get(arreglo[idx]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
//        System.out.printf("%2.9f segundos en buscar un elemento en el diccionario.\n",
//                          (tiempoTotal/1000000000.0), nf.format(N));

        System.out.println("BJ ==================================:");
        Diccionario<String, String> dicBJ;
        th = FabricaHuellasDigitales.BJ_STRING;
        HuellaDigital<String> bj = FabricaHuellasDigitales.getInstanciaString(th);
        dicBJ = new Diccionario<String, String>(N, bj);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            dicBJ.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
        System.out.printf("Máximo número de colisiones: %d.\n", dicBJ.colisionMaxima());
        System.out.printf("Colisiones: %s.\n", nf.format(dicBJ.colisiones()));
        System.out.printf("Carga: %g.\n", dicBJ.carga());
        tiempoInicial = System.nanoTime();
        s = dicBJ.get(arreglo[idx]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar un elemento en el diccionario.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        System.out.println("GLib ==================================:");
        Diccionario<String, String> dicGLib;
        th = FabricaHuellasDigitales.GLIB_STRING;
        HuellaDigital<String> glib = FabricaHuellasDigitales.getInstanciaString(th);
        dicGLib = new Diccionario<String, String>(N, glib);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            dicGLib.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
        System.out.printf("Máximo número de colisiones: %d.\n", dicGLib.colisionMaxima());
        System.out.printf("Colisiones: %s.\n", nf.format(dicGLib.colisiones()));
        System.out.printf("Carga: %g.\n", dicGLib.carga());
        tiempoInicial = System.nanoTime();
        s = dicGLib.get(arreglo[idx]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar un elemento en el diccionario.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        System.out.println("Java ==================================:");
        Diccionario<String, String> dicJava;
        dicJava = new Diccionario<String, String>(N);
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            dicJava.agrega(arreglo[i], arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un diccionario con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
        System.out.printf("Máximo número de colisiones: %d.\n", dicJava.colisionMaxima());
        System.out.printf("Colisiones: %s.\n", nf.format(dicJava.colisiones()));
        System.out.printf("Carga: %g.\n", dicJava.carga());
        tiempoInicial = System.nanoTime();
        s = dicJava.get(arreglo[idx]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en buscar un elemento en el diccionario.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));
    }
}
