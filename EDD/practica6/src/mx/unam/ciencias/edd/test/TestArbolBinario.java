package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolBinario}.
 */
public class TestArbolBinario {

    /* Clase interna privada para probar árboles binarios. */
    private class ArbolBinarioSimple<T> extends ArbolBinario<T> {

        @Override public void elimina(T elemento) {
            VerticeArbolBinario<T> v = busca(elemento);
            if (v == null)
                return;
            elementos--;
            Vertice<T> vertice = vertice(v);
            if (raiz == vertice) {
                raiz = vertice.derecho;
                raiz.padre = null;
                return;
            }
            vertice.padre.derecho = vertice.derecho;
            if (vertice.derecho != null)
                vertice.derecho.padre = vertice.padre;
        }

        /* Implementación trivial del método agrega, que siempre
         * agrega a la derecha. */
        @Override public VerticeArbolBinario<T> agrega(T elemento) {
            if (raiz != null)
                return agrega(raiz, elemento);
            raiz = new Vertice<T>(elemento);
            elementos++;
            return raiz;
        }

        /* Método auxiliar recursivo que agrega siempre a la derecha. */
        private VerticeArbolBinario<T> agrega(Vertice<T> vertice, T elemento) {
            if (vertice.derecho != null)
                return agrega(vertice.derecho, elemento);
            vertice.derecho = new Vertice<T>(elemento);
            vertice.derecho.padre = vertice;
            elementos++;
            return vertice;
        }

        /* Regresamos null; no utilizamos el iterador en estas
         * pruebas. */
        @Override public Iterator<T> iterator() {
            return null;
        }
    }

    /* Valida un vértice, y recursivamente valida sus hijos. */
    private static void arbolValido(VerticeArbolBinario<?> v) {
        try {
            if (v.hayIzquierdo()) {
                VerticeArbolBinario<?> i = v.getIzquierdo();
                Assert.assertTrue(i.hayPadre());
                Assert.assertTrue(i.getPadre() == v);
                arbolValido(i);
            }
            if (v.hayDerecho()) {
                VerticeArbolBinario<?> d = v.getDerecho();
                Assert.assertTrue(d.hayPadre());
                Assert.assertTrue(d.getPadre() == v);
                arbolValido(d);
            }
        } catch (NoSuchElementException sdee) {
            Assert.fail();
        }
    }

    /**
     * Valida un árbol. Comprueba que si un vértice A tiene como hijo
     * al vértice B, entonces el vértice B tiene al vértice A como padre.
     * @param arbol el árbol a validar.
     */
    public static void arbolValido(ArbolBinario<?> arbol) {
        if (arbol.getElementos() == 0)
            return;
        VerticeArbolBinario<?> v = arbol.raiz();
        Assert.assertFalse(v.hayPadre());
        arbolValido(v);
    }

    private Random random;
    private int total;
    private ArbolBinarioSimple<Integer> arbol;

    /**
     * Crea un árbol binario para cada prueba.
     */
    public TestArbolBinario() {
        random = new Random();
        total = 3 + random.nextInt(100);
        arbol = new ArbolBinarioSimple<Integer>();
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#profundidad}.
     */
    @Test public void testProfundidad() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolValido(arbol);
            Assert.assertTrue(arbol.profundidad() == i);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#getElementos}.
     */
    @Test public void testGetElementos() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolValido(arbol);
            Assert.assertTrue(arbol.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#busca}.
     */
    @Test public void testBusca() {
        int[] a = new int[total];
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            arbolValido(arbol);
            arbol.agrega(a[i]);
        }
        for (int i = 0; i < total; i++)
            Assert.assertTrue(arbol.busca(a[i]) != null);
        Assert.assertTrue(arbol.busca(ini - total) == null);
        Assert.assertTrue(arbol.busca(ini + total*2) == null);
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#raiz}.
     */
    @Test public void testRaiz() {
        for (int i = 0; i < total; i++)
            arbol.agrega(random.nextInt(total));
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.hayDerecho());
        v = v.getDerecho();
        while (v.hayDerecho()) {
            Assert.assertTrue(v.hayPadre());
            Assert.assertFalse(v.hayIzquierdo());
            Assert.assertTrue(v.hayDerecho());
            v = v.getDerecho();
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#toString}.
     */
    @Test public void testToString() {
        /* Estoy dispuesto a aceptar una mejor prueba. */
        Assert.assertTrue(arbol.toString() != null &&
                          arbol.toString().equals(""));
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolValido(arbol);
            Assert.assertTrue(arbol.toString() != null &&
                              !arbol.toString().equals(""));
        }
        String cadena =
            "1\n" +
            "└─»2\n" +
            "   └─»3\n" +
            "      └─»4\n" +
            "         └─»5";
        arbol = new ArbolBinarioSimple<Integer>();
        for (int i = 1; i <= 5; i++)
            arbol.agrega(i);
        Assert.assertTrue(arbol.toString().equals(cadena));
    }
}
