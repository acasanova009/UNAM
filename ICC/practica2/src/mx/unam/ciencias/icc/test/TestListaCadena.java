package mx.unam.ciencias.icc.test;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.icc.ListaCadena;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ListaCadena}.
 */
public class TestListaCadena {

    private Random random;
    private int total;
    private ListaCadena lista;

    /**
     * Crea un generador de números aleatorios para cada prueba, un
     * número total de elementos para nuestra lista, y una lista.
     */
    public TestListaCadena() {
        random = new Random();
        total = 10 + random.nextInt(90);
        lista = new ListaCadena();
    }

    /**
     * Prueba unitaria para {@link ListaCadena#getLongitud}.
     */
    @Test public void testGetLongitud() {
        for (int i = 0; i < total/2; i++) {
            lista.agregaFinal(String.valueOf(random.nextInt(total)));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        for (int i = total/2; i < total; i++) {
            lista.agregaInicio(String.valueOf(random.nextInt(total)));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
    }

    /**
     * Prueba unitaria para {@link ListaCadena#agregaFinal}.
     */
    @Test public void testAgregaFinal() {
        lista.agregaFinal("1");
        lista.ultimo();
        Assert.assertTrue("1".equals(lista.get()));
        lista.agregaInicio("2");
        lista.ultimo();
        Assert.assertFalse("2".equals(lista.get()));
        for (int i = 0; i < total; i++) {
            String e = String.valueOf(random.nextInt(total));
            lista.agregaFinal(e);
            lista.ultimo();
            Assert.assertTrue(e.equals(lista.get()));
        }
    }

    /**
     * Prueba unitaria para {@link ListaCadena#agregaInicio}.
     */
    @Test public void testAgregaInicio() {
        lista.agregaInicio("1");
        Assert.assertTrue("1".equals(lista.get()));
        lista.agregaFinal("2");
        Assert.assertFalse("2".equals(lista.get()));
        for (int i = 0; i < total; i++) {
            String e = String.valueOf(random.nextInt(total));
            lista.agregaInicio(e);
            Assert.assertTrue(e == lista.get());
        }
    }

    /**
     * Prueba unitaria para {@link ListaCadena#elimina}.
     */
    @Test public void testElimina() {
        int d = random.nextInt(total);
        String m = "";
        for (int i = 0; i < total; i++) {
            lista.agregaInicio(String.valueOf(d++));
            if (i == total / 2)
                m = String.valueOf(d - 1);
        }
        lista.primero();
        String p = (String)lista.get();
        lista.ultimo();
        String u = (String)lista.get();
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
            String n = (String)lista.get();
            lista.elimina(n);
            Assert.assertTrue(lista.getLongitud() == --total);
            if (lista.getLongitud() == 0)
                continue;
            n = (String)lista.get();
            lista.elimina(n);
            Assert.assertTrue(lista.getLongitud() == --total);
        }
    }

    /**
     * Prueba unitaria para {@link ListaCadena#contiene}.
     */
    @Test public void testContiene() {
        int d = random.nextInt(total);
        String m = "";
        String n = String.valueOf(d - 1);
        for (int i = 0; i < total; i++) {
            lista.agregaFinal(String.valueOf(d++));
            if (i == total/2)
                m = String.valueOf(d - 1);
        }
        Assert.assertTrue(lista.contiene(m));
        Assert.assertFalse(lista.contiene(n));
    }

    /**
     * Prueba unitaria para {@link ListaCadena#reversa}.
     */
    @Test public void testReversa() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(String.valueOf(random.nextInt(total)));
        ListaCadena reversa = lista.reversa();
        Assert.assertTrue(reversa.getLongitud() == lista.getLongitud());
        lista.primero();
        reversa.ultimo();
        while (lista.iteradorValido() && reversa.iteradorValido()) {
            Assert.assertTrue(lista.get() == reversa.get());
            lista.siguiente();
            reversa.anterior();
        }
        Assert.assertFalse(lista.iteradorValido());
        Assert.assertFalse(reversa.iteradorValido());
    }

    /**
     * Prueba unitaria para {@link ListaCadena#copia}.
     */
    @Test public void testCopia() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(String.valueOf(random.nextInt(total)));
        ListaCadena copia = lista.copia();
        Assert.assertFalse(lista == copia);
        lista.primero();
        copia.primero();
        while (lista.iteradorValido() && copia.iteradorValido()) {
            Assert.assertTrue(lista.get() == copia.get());
            lista.siguiente();
            copia.siguiente();
        }
        Assert.assertFalse(lista.iteradorValido());
        Assert.assertFalse(copia.iteradorValido());
    }

    /**
     * Prueba unitaria para {@link ListaCadena#limpia}.
     */
    @Test public void testLimpia() {
        String primero = String.valueOf(random.nextInt(total));
        lista.agregaFinal(primero);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(String.valueOf(random.nextInt(total)));
        String ultimo = String.valueOf(random.nextInt(total));
        lista.agregaFinal(ultimo);
        Assert.assertTrue(lista.getLongitud() != 0);
        lista.primero();
        Assert.assertTrue(lista.get().equals(primero));
        lista.ultimo();
        Assert.assertTrue(lista.get().equals(ultimo));
        lista.limpia();
        Assert.assertTrue(lista.getLongitud() == 0);
        Assert.assertTrue(lista.get() == null);
    }

    /**
     * Prueba unitaria para {@link ListaCadena#get(int)}.
     */
    @Test public void testGet() {
        String[] a = new String[total];
        for (int i = 0; i < total; i++) {
            a[i] = String.valueOf(random.nextInt(total));
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i++)
            Assert.assertTrue(lista.get(i) == a[i]);
        Assert.assertTrue(lista.get(-1) == null);
        Assert.assertTrue(lista.get(total) == null);
    }

    /**
     * Prueba unitaria para {@link ListaCadena#indiceDe}.
     */
    @Test public void testIndiceDe() {
        int ini = random.nextInt(total);
        String[] a = new String[total];
        for (int i = 0; i < total; i++) {
            a[i] = String.valueOf(ini + i);
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i ++)
            Assert.assertTrue(i == lista.indiceDe(a[i]));
        Assert.assertTrue(lista.indiceDe(String.valueOf(ini - 10)) == -1);
    }

    /**
     * Prueba unitaria para {@link ListaCadena#toString}.
     */
    @Test public void testToString() {
        String[] a = new String[total];
        for (int i = 0; i < total; i++) {
            a[i] = String.valueOf(i);
            lista.agregaFinal(a[i]);
        }
        String s = "[";
        for (int i = 0; i < total-1; i++)
            s += String.format("%s, ", a[i]);
        s += String.format("%s]", a[total-1]);
        Assert.assertTrue(s.equals(lista.toString()));
    }

    /**
     * Prueba unitaria para {@link ListaCadena#primero}.
     */
    @Test public void testPrimero() {
        lista.agregaInicio("1");
        lista.ultimo();
        lista.primero();
        Assert.assertTrue(lista.get().equals("1"));
        lista.agregaFinal("2");
        lista.ultimo();
        lista.primero();
        Assert.assertFalse(lista.get().equals("2"));
        for (int i = 0; i < total; i++) {
            String e = String.valueOf(random.nextInt(total));
            lista.agregaInicio(e);
            lista.ultimo();
            lista.primero();
            Assert.assertTrue(lista.get().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link ListaCadena#ultimo}.
     */
    @Test public void testUltimo() {
        for (int i = 0; i < total; i++) {
            String u = String.valueOf(random.nextInt(total));
            lista.agregaFinal(u);
            lista.ultimo();
            Assert.assertTrue(lista.get().equals(u));
        }
    }

    /**
     * Prueba unitaria para {@link ListaCadena#siguiente}.
     */
    @Test public void testSiguiente() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(String.valueOf(ini + i));
        lista.primero();
        String n = (String)lista.get();
        lista.siguiente();
        Assert.assertFalse(lista.get().equals(n));
    }

    /**
     * Prueba unitaria para {@link ListaCadena#anterior}.
     */
    @Test public void testAnterior() {
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(String.valueOf(ini + i));
        lista.ultimo();
        String n = (String)lista.get();
        lista.anterior();
        Assert.assertFalse(lista.get().equals(n));
    }

    /**
     * Prueba unitaria para {@link ListaCadena#get}.
     */
    @Test public void testGetIterador() {
        String[] a = new String[total];
        for (int i = 0; i < total; i++) {
            String e = String.valueOf(random.nextInt(total));
            lista.agregaFinal(e);
            a[i] = e;
        }
        int i = 0;
        lista.primero();
        while (lista.iteradorValido()) {
            Assert.assertTrue(lista.get().equals(a[i++]));
            lista.siguiente();
        }
        lista.limpia();
    }

    /**
     * Prueba unitaria para {@link ListaCadena#iteradorValido}.
     */
    @Test public void testIteradorValido() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(String.valueOf(random.nextInt(total)));
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
}
