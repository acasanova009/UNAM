package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.Cola;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link Cola}.
 */
public class TestCola {

    private Random random;
    private int total;
    private Cola<Integer> cola;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra cola, y una cola.
     */
    public TestCola() {
        random = new Random();
        total = 10 + random.nextInt(90);
        cola = new Cola<Integer>();
    }

    /**
     * Prueba unitaria para {@link Cola#mete}.
     */
    @Test public void testMete() {
        for (int i = 0; i < total; i++)
            cola.mete(i);
        int c = 0;
        while (!cola.esVacia())
            Assert.assertTrue(cola.saca() == c++);
    }
}
