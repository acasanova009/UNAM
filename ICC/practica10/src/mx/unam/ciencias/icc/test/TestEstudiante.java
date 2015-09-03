package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Random;
import org.junit.Test;
import org.junit.Assert;
import mx.unam.ciencias.icc.Estudiante;

/**
 * Clase para pruebas unitarias de la clase {@link Estudiante}.
 */
public class TestEstudiante {

    private Random random;
    private Estudiante estudiante;

    /**
     * Crea un generador de números aleatorios para cada prueba.
     */
    public TestEstudiante() {
        random = new Random();
    }

    /**
     * Prueba unitaria para {@link
     * Estudiante#Estudiante(String,int,double,int)}.
     */
    @Test public void testConstructor() {
        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(1000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(100);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getNombre().equals(nombre));
        Assert.assertTrue(estudiante.getCuenta() == cuenta);
        Assert.assertTrue(estudiante.getPromedio() == promedio);
        Assert.assertTrue(estudiante.getEdad() == edad);
    }

    /**
     * Prueba unitaria para {@link Estudiante#setNombre} y {@link
     * Estudiante#getNombre}.
     */
    @Test public void testSetGetNombre() {
        int n = random.nextInt();
        String nombre = String.valueOf(n);
        int cuenta = random.nextInt(1000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(100);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getNombre().equals(nombre));
        String nuevoNombre = String.valueOf(n + 1);
        estudiante.setNombre(nuevoNombre);
        Assert.assertTrue(estudiante.getNombre().equals(nuevoNombre));
        Assert.assertFalse(estudiante.getNombre().equals(nombre));
    }

    /**
     * Prueba unitaria para {@link Estudiante#setCuenta} y {@link
     * Estudiante#getCuenta}.
     */
    @Test public void testSetGetCuenta() {
        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(10000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(100);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getCuenta() == cuenta);
        int nuevaCuenta = cuenta + 100;
        estudiante.setCuenta(nuevaCuenta);
        Assert.assertTrue(estudiante.getCuenta() == nuevaCuenta);
        Assert.assertFalse(estudiante.getCuenta() == cuenta);
    }

    /**
     * Prueba unitaria para {@link Estudiante#setPromedio} y {@link
     * Estudiante#getPromedio}.
     */
    @Test public void testSetGetPromedio() {
        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(10000000);
        double promedio = random.nextDouble() * 9.0;
        int edad = random.nextInt(100);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getPromedio() == promedio);
        double nuevoPromedio = promedio + 1.0;
        estudiante.setPromedio(nuevoPromedio);
        Assert.assertTrue(estudiante.getPromedio() == nuevoPromedio);
        Assert.assertFalse(estudiante.getPromedio() == promedio);
    }

    /**
     * Prueba unitaria para {@link Estudiante#setEdad} y {@link
     * Estudiante#getEdad}.
     */
    @Test public void testSetGetEdad() {
        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(10000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(50);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getEdad() == edad);
        int nuevaEdad = edad + 50;
        estudiante.setEdad(nuevaEdad);
        Assert.assertTrue(estudiante.getEdad() == nuevaEdad);
        Assert.assertFalse(estudiante.getEdad() == edad);
    }

    /**
     * Prueba unitaria para {@link Estudiante#equals}.
     */
    @Test public void testEquals() {
        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(10000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(50);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Estudiante igual = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.equals(igual));
        nombre = String.valueOf(random.nextInt());
        cuenta = random.nextInt(10000000);
        promedio = random.nextDouble() * 10.0;
        edad = random.nextInt(50);
        Estudiante distinto = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertFalse(estudiante.equals(distinto));
        Assert.assertFalse(estudiante.equals("Una cadena"));
        Assert.assertFalse(estudiante.equals(null));
    }

    /**
     * Prueba unitaria para {@link Estudiante#toString}.
     */
    @Test public void testToString() {
        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(10000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(50);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        String cadena = String.format("Nombre   : %s\n" +
                                      "Cuenta   : %d\n" +
                                      "Promedio : %2.2f\n" +
                                      "Edad     : %d",
                                      nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link Estudiante#guarda}.
     */
    @Test public void testGuarda() {
        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(10000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(50);
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);

        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            estudiante.guarda(out);
            out.close();
            String guardado = swOut.toString();
            String s = String.format("%s\t%d\t%2.2f\t%d\n",
                                     nombre, cuenta, promedio, edad);
                Assert.assertTrue(guardado.equals(s));
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link Estudiante#carga}.
     */
    @Test public void testCarga() {
        estudiante = new Estudiante(null, 0, 0.0, 0);

        String nombre = String.valueOf(random.nextInt());
        int cuenta = random.nextInt(10000000);
        double promedio = random.nextDouble() * 10.0;
        int edad = random.nextInt(50);

        String entrada =
            nombre   + "\t" +
            cuenta   + "\t" +
            promedio + "\t" +
            edad     + "\n";

        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            Assert.assertTrue(estudiante.carga(in));
            in.close();
            Assert.assertTrue(estudiante.getNombre().equals(nombre));
            Assert.assertTrue(estudiante.getCuenta() == cuenta);
            Assert.assertTrue(estudiante.getPromedio() == promedio);
            Assert.assertTrue(estudiante.getEdad() == edad);
        } catch (IOException ioe) {
            Assert.fail();
        }

        entrada = "";
        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            Assert.assertFalse(estudiante.carga(in));
            in.close();
            Assert.assertTrue(estudiante.getNombre().equals(nombre));
            Assert.assertTrue(estudiante.getCuenta() == cuenta);
            Assert.assertTrue(estudiante.getPromedio() == promedio);
            Assert.assertTrue(estudiante.getEdad() == edad);
        } catch (IOException ioe) {
            Assert.fail();
        }

        entrada = "a\ta\ta\ta";
        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            estudiante.carga(in);
            Assert.fail();
        } catch (IOException ioe) {}

        entrada = "a\ta";
        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            estudiante.carga(in);
            Assert.fail();
        } catch (IOException ioe) {}
    }
}
