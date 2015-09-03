package mx.unam.ciencias.icc.test;

import java.util.Iterator;
import java.util.Random;
import org.junit.Test;
import org.junit.Assert;
import mx.unam.ciencias.icc.Lista;

/**
 * Clase para pruebas unitarias de la clase {@link Lista}.
 */
public class TestLista {

    private Random random;
    private int total;
    private Lista<Integer> lista;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra lista, y una lista.
     */
    public TestLista() {
        random = new Random();
        total = 10 + random.nextInt(90);
        lista = new Lista<Integer>();
    }

    /**
     * Prueba unitaria para {@link Lista#getLongitud}.
     */
    @Test public void testGetLongitud() {
        for (int i = 0; i < total/2; i++) {
            lista.agregaFinal(random.nextInt(total));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        for (int i = total/2; i < total; i++) {
            lista.agregaInicio(random.nextInt(total));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
    }

    /**
     * Prueba unitaria para {@link Lista#agregaFinal}.
     */
    @Test public void testAgregaFinal() {
        lista.agregaFinal(1);
        lista.ultimo();
        Assert.assertTrue(1 == lista.dame());
        lista.agregaInicio(2);
        lista.ultimo();
        Assert.assertFalse(2 == lista.dame());
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaFinal(e);
            lista.ultimo();
            Assert.assertTrue(e == lista.dame());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#agregaInicio}.
     */
    @Test public void testAgregaInicio() {
        lista.agregaInicio(1);
        Assert.assertTrue(1  == lista.dame());
        lista.agregaFinal(2);
        Assert.assertFalse(2 == lista.dame());
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaInicio(e);
            Assert.assertTrue(e == lista.dame());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#elimina}.
     */
    @Test public void testElimina() {
        int d = random.nextInt(total);
        int m = d - 10;
        for (int i = 0; i < total; i++) {
            lista.agregaInicio(d++);
            if (i == total / 2)
                m = d - 1;
        }
        lista.primero();
        int p = (Integer)lista.dame();
        lista.ultimo();
        int u = (Integer)lista.dame();
        Assert.assertTrue(lista.contiene(p));
        Assert.assertTrue(lista.contiene(m));
        Assert.assertTrue(lista.contiene(u));
        lista.elimina(p);
        Assert.assertFalse(lista.contiene(p));
        lista.elimina(m);
        Assert.assertFalse(lista.contiene(m));
        lista.elimina(u);
        Assert.assertFalse(lista.contiene(u));
        total = lista.getLongitud();
        while (lista.getLongitud() > 0) {
            lista.primero();
            int n = (Integer)lista.dame();
            lista.elimina(n);
            Assert.assertTrue(lista.getLongitud() == --total);
            if (lista.getLongitud() == 0)
                continue;
            n = (Integer)lista.dame();
            lista.elimina(n);
            Assert.assertTrue(lista.getLongitud() == --total);
        }
    }

    /**
     * Prueba unitaria para {@link Lista#contiene}.
     */
    @Test public void testContiene() {
        int d = random.nextInt(total);
        int m = d - 10;
        int n = d - 1;
        for (int i = 0; i < total; i++) {
            lista.agregaFinal(d++);
            if (i == total/2)
                m = d - 1;
        }
        Assert.assertTrue(lista.contiene(m));
        Assert.assertFalse(lista.contiene(n));
    }

    /**
     * Prueba unitaria para {@link Lista#reversa}.
     */
    @Test public void testReversa() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        Lista<Integer> reversa = lista.reversa();
        Assert.assertTrue(reversa.getLongitud() == lista.getLongitud());
        lista.primero();
        reversa.ultimo();
        while (lista.iteradorValido() && reversa.iteradorValido()) {
            Assert.assertTrue(lista.dame() == reversa.dame());
            lista.siguiente();
            reversa.anterior();
        }
        Assert.assertFalse(lista.iteradorValido());
        Assert.assertFalse(reversa.iteradorValido());
    }

    /**
     * Prueba unitaria para {@link Lista#copia}.
     */
    @Test public void testCopia() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        Lista<Integer> copia = lista.copia();
        Assert.assertFalse(lista == copia);
        lista.primero();
        copia.primero();
        while (lista.iteradorValido() && copia.iteradorValido()) {
            Assert.assertTrue(lista.dame() == copia.dame());
            lista.siguiente();
            copia.siguiente();
        }
        Assert.assertFalse(lista.iteradorValido());
        Assert.assertFalse(copia.iteradorValido());
    }

    /**
     * Prueba unitaria para {@link Lista#limpia}.
     */
    @Test public void testLimpia() {
        int primero = random.nextInt(total);
        lista.agregaFinal(primero);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        int ultimo = random.nextInt(total);
        lista.agregaFinal(ultimo);
        Assert.assertTrue(lista.getLongitud() != 0);
        lista.primero();
        Assert.assertTrue(lista.dame() == primero);
        lista.ultimo();
        Assert.assertTrue(lista.dame() == ultimo);
        lista.limpia();
        Assert.assertTrue(lista.getLongitud() == 0);
        Assert.assertTrue(lista.dame() == null);
    }

    /**
     * Prueba unitaria para {@link Lista#get}.
     */
    @Test public void testGet() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i++)
            Assert.assertTrue(lista.get(i) == a[i]);
        Assert.assertTrue(lista.get(-1) == null);
        Assert.assertTrue(lista.get(total) == null);
    }

    /**
     * Prueba unitaria para {@link Lista#indiceDe}.
     */
    @Test public void testIndiceDe() {
        int ini = random.nextInt(total);
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i ++)
            Assert.assertTrue(i == lista.indiceDe(a[i]));
        Assert.assertTrue(lista.indiceDe(ini - 10) == -1);
    }

    /**
     * Prueba unitaria para {@link Lista#equals}.
     */
    @Test public void testEquals() {
        Lista<Integer> otra = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            int r = random.nextInt(total);
            lista.agregaFinal(r);
            otra.agregaFinal(r);
        }
        Assert.assertTrue(lista.equals(otra));
        lista.ultimo();
        int u = (Integer)lista.dame();
        lista.elimina(u);
        lista.agregaFinal(u+1);
        Assert.assertFalse(lista.equals(otra));
        Assert.assertFalse(lista.equals(null));
        Assert.assertFalse(lista.equals("X"));
    }

    /**
     * Prueba unitaria para {@link Lista#toString}.
     */
    @Test public void testToString() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = i;
            lista.agregaFinal(a[i]);
        }
        String s = "[";
        for (int i = 0; i < total-1; i++)
            s += String.format("%s, ", a[i]);
        s += String.format("%s]", a[total-1]);
        Assert.assertTrue(s.equals(lista.toString()));
    }

    /**
     * Prueba unitaria para {@link Lista#primero}.
     */
    @Test public void testPrimero() {
        lista.agregaInicio(1);
        lista.ultimo();
        lista.primero();
        Assert.assertTrue(lista.dame().equals(1));
        lista.agregaFinal(2);
        lista.ultimo();
        lista.primero();
        Assert.assertFalse(lista.dame().equals(2));
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaInicio(e);
            lista.ultimo();
            lista.primero();
            Assert.assertTrue(lista.dame().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link Lista#ultimo}.
     */
    @Test public void testUltimo() {
        for (int i = 0; i < total; i++) {
            int u = random.nextInt(total);
            lista.agregaFinal(u);
            lista.ultimo();
            Assert.assertTrue(lista.dame().equals(u));
        }
    }

    /**
     * Prueba unitaria para {@link Lista#siguiente}.
     */
    @Test public void testSiguiente() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(ini + i);
        lista.primero();
        int n = (Integer)lista.dame();
        lista.siguiente();
        Assert.assertFalse(lista.dame().equals(n));
    }

    /**
     * Prueba unitaria para {@link Lista#anterior}.
     */
    @Test public void testAnterior() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(ini + i);
        lista.ultimo();
        int n = (Integer)lista.dame();
        lista.anterior();
        Assert.assertFalse(lista.dame().equals(n));
    }

    /**
     * Prueba unitaria para {@link Lista#dame}.
     */
    @Test public void testDame() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaFinal(e);
            a[i] = e;
        }
        int i = 0;
        lista.primero();
        while (lista.iteradorValido()) {
            Assert.assertTrue(lista.dame().equals(a[i++]));
            lista.siguiente();
        }
        lista.limpia();
    }

    /**
     * Prueba unitaria para {@link Lista#iteradorValido}.
     */
    @Test public void testIteradorValido() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        lista.primero();
        Assert.assertTrue(lista.iteradorValido());
        lista.ultimo();
        Assert.assertTrue(lista.iteradorValido());
        lista.primero();
        lista.anterior();
        Assert.assertFalse(lista.iteradorValido());
        lista.ultimo();
        lista.siguiente();
        Assert.assertFalse(lista.iteradorValido());
    }

    /**
     * Prueba unitaria para {@link Lista#mergeSort}.
     */
    @Test public void testMergeSort() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        Lista<Integer> ordenada = Lista.mergeSort(lista);
        Assert.assertTrue(lista.getLongitud() == total);
        Assert.assertTrue(ordenada.getLongitud() == total);
        lista.primero();
        while (lista.iteradorValido()) {
            Assert.assertTrue(ordenada.contiene(lista.dame()));
            lista.siguiente();
        }
        ordenada.primero();
        
        int a = ordenada.dame();
        while (ordenada.iteradorValido()) {
            int e = ordenada.dame();
            Assert.assertTrue(a <= e);
            ordenada.siguiente();
            a = e;
        }
    }

    /**
     * Prueba unitaria para {@link Lista#busquedaLineal}.
     */
    @Test public void testBusquedaLineal() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        lista = Lista.mergeSort(lista);
        int m = lista.get(total/2);
        Assert.assertTrue(Lista.busquedaLineal(lista, m));
        lista.primero();
        int o = lista.dame() - 10;
        Assert.assertFalse(Lista.busquedaLineal(lista, o));
    }
}
