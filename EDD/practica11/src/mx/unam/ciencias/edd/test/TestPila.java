package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.Pila;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link Pila}.
 */
public class TestPila {

    private Random random;
    private int total;
    private Pila<Integer> pila;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra pila, y una pila.
     */
    public TestPila() {
        random = new Random();
        total = 10 + random.nextInt(90);
        pila = new Pila<Integer>();
    }

    /**
     * Prueba unitaria para {@link Pila#mete}.
     */
    @Test public void testMete() {
        for (int i = 0; i < total; i++)
            pila.mete(i);
        int c = total - 1;
        while (!pila.esVacia())
            Assert.assertTrue(pila.saca() == c--);
    }
}
