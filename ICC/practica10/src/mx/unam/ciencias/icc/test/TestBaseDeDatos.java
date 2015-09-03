package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.IteradorLista;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatos}.
 */
public class TestBaseDeDatos {

    private Random random;
    private BaseDeDatosIds bdd;
    private int total;

    /**
     * Crea un generador de números aleatorios para cada prueba y
     * una base de datos.
     */
    public TestBaseDeDatos() {
        random = new Random();
        bdd = new BaseDeDatosIds();
        total = random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#BaseDeDatos}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getNumRegistros}.
     */
    @Test public void testNumRegistros() {
        for (int i = 0; i < total; i++) {
            Id id = new Id(String.valueOf(i), String.valueOf(i));
            bdd.agregaRegistro(id);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
        bdd.limpia();
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getUltimoModificado}.
     */
    @Test public void testGetUltimoModificado() {
        for (int i = 0; i < total; i++) {
            Id id = new Id(String.valueOf(i), String.valueOf(i));
            bdd.agregaRegistro(id);
            Assert.assertTrue(bdd.getUltimoModificado() == id);
        }
        Lista<Id> lista = bdd.getRegistros();
        IteradorLista<Id> it = lista.iteradorLista();
        it.end();
        while (it.hasPrevious()) {
            Id id = it.previous();
            bdd.eliminaRegistro(id);
            Assert.assertTrue(bdd.getUltimoModificado() == id);
        }
        for (int i = 0; i < total; i++)
            bdd.agregaRegistro(new Id(String.valueOf(i), String.valueOf(i)));
        lista = bdd.getRegistros();
        for (Id id : lista) {
            Id id2 = new Id(id.getId(),
                                    id.getValor() + id.getValor());
            bdd.actualizaRegistro(id2, BaseDeDatosIds.VALOR);
            Assert.assertTrue(bdd.getUltimoModificado() == id);
            Assert.assertTrue(id.getValor().equals(id2.getValor()));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        for (int i = 0; i < total; i++) {
            Id id = new Id(String.valueOf(i), String.valueOf(i));
            bdd.agregaRegistro(id);
        }
        Lista<Id> l1 = bdd.getRegistros();
        Lista<Id> l2 = bdd.getRegistros();
        Assert.assertTrue(l1 != l2);
        Assert.assertTrue(l1.equals(l2));
        Id id = l1.get(total/2);
        l1.elimina(id);
        Lista<Id> l3 = bdd.getRegistros();
        Assert.assertTrue(l1.getLongitud() == l3.getLongitud()-1);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        Escucha escucha = new Escucha();
        bdd.addTableModelListener(escucha);
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(random.nextInt());
            Id id = new Id(s, s);
            bdd.agregaRegistro(id);
            TableModelEvent e = escucha.getEvento();
            Assert.assertTrue(e != null);
            Assert.assertTrue(e.getColumn() == TableModelEvent.ALL_COLUMNS);
            Assert.assertTrue(e.getType() == TableModelEvent.INSERT);
            Assert.assertTrue(e.getFirstRow() == i);
            Assert.assertTrue(e.getLastRow() == i);
            Lista l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(id));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistro}.
     */
    @Test public void testEliminaRegistro() {
        int ini = random.nextInt();
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(ini + i);
            Id id = new Id(s, s);
            bdd.agregaRegistro(id);
        }
        Escucha escucha = new Escucha();
        bdd.addTableModelListener(escucha);
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            Id id = bdd.getRegistros().get(i);
            Assert.assertTrue(bdd.getRegistros().contiene(id));
            bdd.eliminaRegistro(id);
            TableModelEvent e = escucha.getEvento();
            Assert.assertTrue(e != null);
            Assert.assertTrue(e.getColumn() == TableModelEvent.ALL_COLUMNS);
            Assert.assertTrue(e.getType() == TableModelEvent.DELETE);
            Assert.assertTrue(e.getFirstRow() == i);
            Assert.assertTrue(e.getLastRow() == i);
            Assert.assertFalse(bdd.getRegistros().contiene(id));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(random.nextInt());
            Id id = new Id(s, s);
            bdd.agregaRegistro(id);
        }
        String guardado = "";
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            bdd.guarda(out);
            out.close();
            guardado = swOut.toString();
        } catch (IOException ioe) {
            Assert.fail();
        }
        String[] lineas = guardado.split("\n");
        Assert.assertTrue(lineas.length == total);
        Lista<Id> l = bdd.getRegistros();
        int i = 0;
        for (Id id : l)
            Assert.assertTrue(lineas[i++].equals(id.getId()));
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        String entrada = "";
        Id[] ids = new Id[total];
        for (int i = 0; i < total; i++) {
            String id = String.valueOf(random.nextInt());
            entrada += id + "\n";
            ids[i] = new Id(id, id);
        }
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Lista<Id> l = bdd.getRegistros();
        int i = 0;
        for (Id id : l)
            Assert.assertTrue(ids[i++].equals(id));
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistros}.
     */
    @Test public void testEliminaRegistros() {
        Lista<Integer> ind = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(i);
            Id id = new Id(s, s);
            bdd.agregaRegistro(id);
            if ((i % 2) == 0)
                ind.agregaFinal(i);
        }
        Integer[] indices = new Integer[ind.getLongitud()];
        int i = 0;
        for (Integer n : ind)
            indices[i++] = n;
        bdd.eliminaRegistros(indices);
        Assert.assertTrue(bdd.getNumRegistros() == total - indices.length);
        Lista<Id> registros = bdd.getRegistros();
        for (Id id : registros) {
            int n = Integer.parseInt(id.getId());
            Assert.assertFalse((n % 2) == 0);
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#indicesDe}.
     */
    @Test public void testIndicesDe() {
        Lista<Id> lista = new Lista<Id>();
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(i);
            Id id = new Id(s, s);
            bdd.agregaRegistro(id);
            if (random.nextInt(2) == 0)
                lista.agregaFinal(id);
        }
        Integer[] indices = bdd.indicesDe(lista);
        Lista<Id> registros = bdd.getRegistros();
        int c = 0;
        for (Id id : lista)
            Assert.assertTrue(registros.indiceDe(id) == indices[c++]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#limpia}.
     */
    @Test public void testLimpia() {
        Escucha escucha = new Escucha();
        bdd.addTableModelListener(escucha);
        for (int i = 0; i < total; i++) {
            Id id = new Id(String.valueOf(i), String.valueOf(i));
            bdd.agregaRegistro(id);
        }
        bdd.limpia();
        TableModelEvent evento = escucha.getEvento();
        Assert.assertTrue(evento.getColumn() == TableModelEvent.ALL_COLUMNS);
        Assert.assertTrue(evento.getType() == TableModelEvent.DELETE);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#addTableModelListener}.
     */
    @Test public void testAddTableModelListener() {
        Escucha escucha = new Escucha();
        bdd.addTableModelListener(escucha);
        String s = String.valueOf(random.nextInt(total));
        Id id = new Id(s, s);
        bdd.agregaRegistro(id);
        TableModelEvent e = escucha.getEvento();
        Assert.assertTrue(e != null);
        Assert.assertTrue(e.getColumn() == TableModelEvent.ALL_COLUMNS);
        Assert.assertTrue(e.getType() == TableModelEvent.INSERT);
        Assert.assertTrue(e.getFirstRow() == 0);
        Assert.assertTrue(e.getLastRow() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#removeTableModelListener}.
     */
    @Test public void testRemoveTableModelListener() {
        Escucha escucha = new Escucha();
        bdd.addTableModelListener(escucha);
        String s = String.valueOf(random.nextInt(total));
        Id id = new Id(s, s);
        bdd.agregaRegistro(id);
        TableModelEvent e = escucha.getEvento();
        Assert.assertTrue(e != null);
        Assert.assertTrue(e.getColumn() == TableModelEvent.ALL_COLUMNS);
        Assert.assertTrue(e.getType() == TableModelEvent.INSERT);
        Assert.assertTrue(e.getFirstRow() == 0);
        Assert.assertTrue(e.getLastRow() == 0);
        bdd.removeTableModelListener(escucha);
        bdd.eliminaRegistro(id);
        e = escucha.getEvento();
        Assert.assertTrue(e != null);
        Assert.assertTrue(e.getColumn() == TableModelEvent.ALL_COLUMNS);
        Assert.assertTrue(e.getType() == TableModelEvent.INSERT);
        Assert.assertTrue(e.getFirstRow() == 0);
        Assert.assertTrue(e.getLastRow() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRowCount}.
     */
    @Test public void testGetRowCount() {
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(i);
            Id id = new Id(s, s);
            bdd.agregaRegistro(id);
            Assert.assertTrue(bdd.getRowCount() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#isCellEditable}.
     */
    @Test public void testIsCellEditable() {
        for (int i = 0; i < total; i++)
            for (int j = 0; j < total; j++)
                Assert.assertTrue(bdd.isCellEditable(i, j));
    }
}
