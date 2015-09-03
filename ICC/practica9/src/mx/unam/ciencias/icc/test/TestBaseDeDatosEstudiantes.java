package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link
 * BaseDeDatosEstudiantes}.
 */
public class TestBaseDeDatosEstudiantes {

    /* Escucha para modelos de tabla. */
    public class Escucha implements TableModelListener {

        public TableModelEvent evento;

        public void tableChanged(TableModelEvent evento) {
            this.evento = evento;
        }
    }

    private Random random;
    private BaseDeDatosEstudiantes bdd;
    private int total;

    /**
     * Crea un generador de números aleatorios para cada prueba y
     * una base de datos de estudiantes.
     */
    public TestBaseDeDatosEstudiantes() {
        random = new Random();
        bdd = new BaseDeDatosEstudiantes();
        total = random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#BaseDeDatosEstudiantes}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        int ini = 1000000;
        for (int i = 0; i < total; i++) {
            String nombre = String.valueOf(ini + i);
            int cuenta = ini + i;
            double promedio = random.nextDouble() * 10.0;
            int edad = random.nextInt(100);
            Estudiante estudiante = new Estudiante(nombre, cuenta,
                                                   promedio, edad);
            bdd.agregaRegistro(estudiante);
        }

        String busqueda = String.valueOf(10000);
        Lista<Estudiante> l = bdd.buscaRegistros("nombre", busqueda);
        Assert.assertTrue(l.getLongitud() == total);
        for (Estudiante e : l)
            Assert.assertTrue(e.getNombre().indexOf(busqueda) != -1);
        busqueda = String.valueOf(9999999);
        l = bdd.buscaRegistros("nombre", busqueda);
        Assert.assertFalse(l.getLongitud() != 0);

        busqueda = String.valueOf(10000);
        l = bdd.buscaRegistros("cuenta", busqueda);
        Assert.assertTrue(l.getLongitud() == total);
        for (Estudiante e : l)
            Assert.assertTrue(String.valueOf(e.getCuenta()).indexOf(busqueda) != -1);
        busqueda = String.valueOf(9999999);
        l = bdd.buscaRegistros("cuenta", busqueda);
        Assert.assertFalse(l.getLongitud() != 0);

        try {
            l = bdd.buscaRegistros("no-existe", "");
        } catch (IllegalArgumentException iae) {
            return;
        }
        Assert.fail();
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#getColumnClass}.
     */
    @Test public void testGetColumnClass() {
        Assert.assertTrue(bdd.getColumnClass(0) == String.class);
        Assert.assertTrue(bdd.getColumnClass(1) == Integer.class);
        Assert.assertTrue(bdd.getColumnClass(2) == Double.class);
        Assert.assertTrue(bdd.getColumnClass(3) == Integer.class);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#getColumnCount}.
     */
    @Test public void testGetColumnCount() {
        Assert.assertTrue(bdd.getColumnCount() ==
                          BaseDeDatosEstudiantes.N_COLUMNAS);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#getColumnName}.
     */
    @Test public void testGetColumnName() {
        String s = "<html><b>Nombre</b></html>";
        Assert.assertTrue(bdd.getColumnName(0).equals(s));
        s = "<html><b>Cuenta</b></html>";
        Assert.assertTrue(bdd.getColumnName(1).equals(s));
        s = "<html><b>Promedio</b></html>";
        Assert.assertTrue(bdd.getColumnName(2).equals(s));
        s = "<html><b>Edad</b></html>";
        Assert.assertTrue(bdd.getColumnName(3).equals(s));
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#getValueAt}.
     */
    @Test public void testGetValueAt() {
        for (int i = 0; i < total; i++)
            bdd.agregaRegistro(new Estudiante(String.valueOf(i), i, i, i));
        for (int i = 0; i < total; i++) {
            Object o = bdd.getValueAt(i, 0);
            Assert.assertTrue(o.equals(String.valueOf(i)));
            o = bdd.getValueAt(i, 1);
            Assert.assertTrue(o.equals(i));
            o = bdd.getValueAt(i, 2);
            Assert.assertTrue(o.equals(Double.valueOf(i)));
            o = bdd.getValueAt(i, 3);
            Assert.assertTrue(o.equals(i));
        }
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#setValueAt}.
     */
    @Test public void testSetValueAt() {
        Escucha escucha = new Escucha();
        bdd.addTableModelListener(escucha);
        for (int i = 0; i < total; i++) {
            String nombre = String.valueOf(i);
            int cuenta = 1000000 + i;
            double promedio = (double)((i+1)*10) / total;
            int edad = 16 + (i % 85);
            bdd.agregaRegistro(new Estudiante(nombre, cuenta,
                                              promedio, edad));
        }
        int d = total * 10;
        Lista<Estudiante> registros = bdd.getRegistros();
        for (int i = 0; i < total; i++) {
            String nombre = String.valueOf(i);
            int cuenta = 1000000 + i;
            double promedio = (double)((i+1)*10) / total;
            int edad = 16 + (i % 85);
            Estudiante e = registros.get(i);
            Assert.assertTrue(e.getNombre().equals(nombre));
            Assert.assertTrue(e.getCuenta() == cuenta);
            Assert.assertTrue(e.getPromedio() == promedio);
            Assert.assertTrue(e.getEdad() == edad);
            bdd.setValueAt("X X", i, 0);
            TableModelEvent ev = escucha.evento;
            Assert.assertTrue(ev != null);
            Assert.assertTrue(ev.getColumn() == 0);
            Assert.assertTrue(ev.getType() == TableModelEvent.UPDATE);
            Assert.assertTrue(ev.getFirstRow() == i);
            Assert.assertTrue(ev.getLastRow() == i);
            bdd.setValueAt(500000, i, 1);
            ev = escucha.evento;
            Assert.assertTrue(ev != null);
            Assert.assertTrue(ev.getColumn() == 1);
            Assert.assertTrue(ev.getType() == TableModelEvent.UPDATE);
            Assert.assertTrue(ev.getFirstRow() == i);
            Assert.assertTrue(ev.getLastRow() == i);
            bdd.setValueAt(0.0, i, 2);
            ev = escucha.evento;
            Assert.assertTrue(ev != null);
            Assert.assertTrue(ev.getColumn() == 2);
            Assert.assertTrue(ev.getType() == TableModelEvent.UPDATE);
            Assert.assertTrue(ev.getFirstRow() == i);
            Assert.assertTrue(ev.getLastRow() == i);
            bdd.setValueAt(15, i, 3);
            ev = escucha.evento;
            Assert.assertTrue(ev != null);
            Assert.assertTrue(ev.getColumn() == 3);
            Assert.assertTrue(ev.getType() == TableModelEvent.UPDATE);
            Assert.assertTrue(ev.getFirstRow() == i);
            Assert.assertTrue(ev.getLastRow() == i);
            e = registros.get(i);
            Assert.assertTrue(e.getNombre().equals("X X"));
            Assert.assertTrue(e.getCuenta() == 500000);
            Assert.assertTrue(e.getPromedio() == 0.0);
            Assert.assertTrue(e.getEdad() == 15);
        }
        Estudiante e = registros.get(0);
        Assert.assertTrue(e.getNombre().equals("X X"));
        bdd.setValueAt("", 0, 0);
        Assert.assertTrue(e.getNombre().equals("X X"));
        Assert.assertTrue(e.getCuenta() == 500000);
        bdd.setValueAt(499999, 0, 1);
        Assert.assertTrue(e.getCuenta() == 500000);
        bdd.setValueAt(1000000000, 0, 1);
        Assert.assertTrue(e.getCuenta() == 500000);
        Assert.assertTrue(e.getPromedio() == 0.0);
        bdd.setValueAt(-0.5, 0, 2);
        Assert.assertTrue(e.getPromedio() == 0.0);
        bdd.setValueAt(10.5, 0, 2);
        Assert.assertTrue(e.getPromedio() == 0.0);
        Assert.assertTrue(e.getEdad() == 15);
        bdd.setValueAt(14, 0, 3);
        Assert.assertTrue(e.getEdad() == 15);
        bdd.setValueAt(100, 0, 3);
        Assert.assertTrue(e.getEdad() == 15);
    }
}
