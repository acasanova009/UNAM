package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.MeteSaca;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link MeteSaca}.
 */
public class TestMeteSaca {

    /* Clase privada interna para poder hacer pruebas con instancias
     * de MeteSaca. Implementa mete() de tal forma que los elementos
     * pares se agregan al final, y los impares al inicio. */
    private class MSPrueba<T> extends MeteSaca<T> {
        @Override public void mete(T n) {
            Nodo<T> nodo = new Nodo<T>(n);
            if (cabeza == null) {
                cabeza = rabo = nodo;
            } else if ((elementos % 2) == 0) {
                rabo.siguiente = nodo;
                rabo = nodo;
            } else {
                nodo.siguiente = cabeza;
                cabeza = nodo;
            }
            elementos++;
        }
    }

    private Random random;
    private int total;
    private MeteSaca<Integer> meteSaca;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra estructura, y una
     * instancia.
     */
    public TestMeteSaca() {
        random = new Random();
        total = 10 + random.nextInt(90);
        meteSaca = new MSPrueba<Integer>();
    }

    /**
     * Prueba unitaria para {@link MeteSaca#saca}.
     */
    @Test public void testSaca() {
        try {
            meteSaca.saca();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            meteSaca.mete(i);
        int i = 0;
        int[] a = new int[total];
        while (!meteSaca.esVacia())
            a[i++] = meteSaca.saca();
        int m = total / 2;
        int u = a[0];
        Assert.assertTrue((u % 2) == 1);
        for (i = 1; i < m; i++) {
            Assert.assertTrue((a[i] % 2) == 1);
            Assert.assertTrue(a[i] == u - 2);
            u = a[i];
        }
        u = a[i];
        Assert.assertTrue((u % 2) == 0);
        for (i = m + 1; i < total; i++) {
            Assert.assertTrue((a[i] % 2) == 0);
            Assert.assertTrue(a[i] == u + 2);
            u = a[i];
        }
    }

    /**
     * Prueba unitaria para {@link MeteSaca#mira}.
     */
    @Test public void testMira() {
    }

    /**
     * Prueba unitaria para {@link MeteSaca#esVacia}.
     */
    @Test public void testEsVacia() {
    }
}
