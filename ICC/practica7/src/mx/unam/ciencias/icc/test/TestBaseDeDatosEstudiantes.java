package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
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
        Lista l = bdd.buscaRegistros("nombre", busqueda);
        Assert.assertTrue(l.getLongitud() == total);
        l.primero();
        while (l.iteradorValido()) {
            Estudiante e = (Estudiante)l.dame();
            Assert.assertTrue(e.getNombre().indexOf(busqueda) != -1);
            l.siguiente();
        }
        busqueda = String.valueOf(9999999);
        l = bdd.buscaRegistros("nombre", busqueda);
        Assert.assertFalse(l.getLongitud() != 0);

        busqueda = String.valueOf(10000);
        l = bdd.buscaRegistros("cuenta", busqueda);
        Assert.assertTrue(l.getLongitud() == total);
        l.primero();
        while (l.iteradorValido()) {
            Estudiante e = (Estudiante)l.dame();
            Assert.assertTrue(String.valueOf(e.getCuenta()).indexOf(busqueda) != -1);
            l.siguiente();
        }
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
}
