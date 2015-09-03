package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolBinarioCompleto}.
 */
public class TestArbolBinarioCompleto {

    private int total;
    private Random random;
    private ArbolBinarioCompleto<Integer> arbol;

    /**
     * Valida un árbol binario completo. Comprueba que todos los
     * niveles del árbol estén llenos excepto tal vez el último.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
                     arbolBinarioCompletoValido(ArbolBinarioCompleto<T> arbol) {
        if (arbol.getElementos() == 0)
            return;
        Assert.assertTrue(arbol.profundidad() ==
                          (int)(Math.floor(Math.log(arbol.getElementos()) /
                                           Math.log(2))));
    }

    /**
     * Crea un árbol binario completo para cada prueba.
     */
    public TestArbolBinarioCompleto() {
        random = new Random();
        arbol = new ArbolBinarioCompleto<Integer>();
        total = random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#agrega}.
     */
    @Test public void testAgrega() {
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            TestArbolBinario.arbolValido(arbol);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolBinarioCompletoValido(arbol);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#elimina}.
     */
    @Test public void testElimina() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            int r;
            boolean repetido = false;
            do {
                r = random.nextInt(1000);
                repetido = false;
                for (int j = 0; j < i; j++)
                    if (r == a[j])
                        repetido = true;
            } while (repetido);
            a[i] = r;
            arbol.agrega(a[i]);
        }
        for (int i : a)
            Assert.assertTrue(arbol.busca(i) != null);
        int n = total;
        while (arbol.getElementos() > 0) {
            Assert.assertTrue(arbol.getElementos() == n);
            int i = random.nextInt(total);
            if (a[i] == -1)
                continue;
            int e = a[i];
            VerticeArbolBinario<Integer> it = arbol.busca(e);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == e);
            arbol.elimina(e);
            it = arbol.busca(e);
            Assert.assertTrue(it == null);
            Assert.assertTrue(arbol.getElementos() == --n);
            TestArbolBinario.arbolValido(arbol);
            arbolBinarioCompletoValido(arbol);
            a[i] = -1;
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#iterator}.
     */
    @Test public void testIterator() {
        int[] arreglo = new int[total];
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arreglo[i] = n;
            arbol.agrega(n);
        }
        int c = 0;
        for (Integer i : arbol)
            Assert.assertTrue(i == arreglo[c++]);
        Assert.assertTrue(c == total);
    }
}
