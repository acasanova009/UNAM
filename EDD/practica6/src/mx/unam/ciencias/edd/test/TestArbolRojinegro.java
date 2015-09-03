package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolRojinegro}.
 */
public class TestArbolRojinegro {

    private int total;
    private Random random;
    private ArbolRojinegro<Integer> arbol;

    /* Valida el vértice de un árbol rojinegro, y recursivamente
     * revisa sus hijos. */
    private static <T extends Comparable<T>> void
                      arbolRojinegroValido(VerticeArbolBinario<T> v) {
        switch (v.getColor()) {
        case NEGRO:
            if (v.hayIzquierdo())
                arbolRojinegroValido(v.getIzquierdo());
            if (v.hayDerecho())
                arbolRojinegroValido(v.getDerecho());
            break;
        case ROJO:
            if (v.hayIzquierdo()) {
                VerticeArbolBinario<T> i = v.getIzquierdo();
                Assert.assertTrue(i.getColor() == Color.NEGRO);
                arbolRojinegroValido(i);
            }
            if (v.hayDerecho()) {
                VerticeArbolBinario<T> d = v.getDerecho();
                Assert.assertTrue(d.getColor() == Color.NEGRO);
                arbolRojinegroValido(d);
            }
            break;
        default:
            Assert.fail();
        }
    }

    /* Valida que los caminos del vértice a sus hojas tengan todos
       el mismo número de vértices negros. */
    private static <T extends Comparable<T>> int
                      validaCaminos(VerticeArbolBinario<T> v) {
        int ni = -1, nd = -1;
        if (v.hayIzquierdo()) {
            VerticeArbolBinario<T> i = v.getIzquierdo();
            ni = validaCaminos(i);
        } else {
            ni = 1;
        }
        if (v.hayDerecho()) {
            VerticeArbolBinario<T> d = v.getDerecho();
            nd = validaCaminos(d);
        } else {
            nd = 1;
        }
        Assert.assertTrue(ni == nd);
        switch (v.getColor()) {
        case NEGRO:
            return 1 + ni;
        case ROJO:
            return ni;
        default:
            Assert.fail();
        }
        // Inalcanzable.
        return -1;
    }

    /**
     * Valida un árbol rojinegro. Comprueba que la raíz sea negra,
     * que las hojas sean negras, que un vértice rojo tenga dos hijos
     * negros, y que todo camino de la raíz a sus hojas tiene el
     * mismo número de vértices negros.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
                     arbolRojinegroValido(ArbolRojinegro<T> arbol) {
        if (arbol.getElementos() == 0)
            return;
        VerticeArbolBinario<T> v = arbol.raiz();
        Assert.assertTrue(v.getColor() == Color.NEGRO);
        arbolRojinegroValido(v);
        validaCaminos(v);
    }

    /**
     * Crea un árbol rojo-ngro para cada prueba.
     */
    public TestArbolRojinegro() {
        random = new Random();
        arbol = new ArbolRojinegro<Integer>();
        total = random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#agrega}.
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
            TestArbolBinarioOrdenado.arbolOrdenadoValido(arbol);
            arbolRojinegroValido(arbol);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#elimina}.
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
            arbolRojinegroValido(arbol);
            a[i] = -1;
        }
    }
}
