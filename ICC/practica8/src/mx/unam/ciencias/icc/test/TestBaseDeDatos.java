package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatos}.
 */
public class TestBaseDeDatos {

    /* Registro tonto para poder hacer una BaseDeDatosIds. */
    private class Id implements Registro {

        private String id;

        public Id(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (!(o instanceof Id))
                return false;
            Id i = (Id)o;
            return id.equals(i.id);
        }

        @Override public boolean carga(BufferedReader in)
            throws IOException {
            String linea = in.readLine();
            if (linea == null)
                return false;
            if (linea.equals(""))
                return false;
            id = linea;
            return true;
        }

        @Override public void guarda(BufferedWriter out) throws IOException {
            out.write(id + "\n");
        }
    }

    /* Base de datos tonta para ids. */
    private class BaseDeDatosId extends BaseDeDatos<Id> {

        public BaseDeDatosId() {
            super();
        }

        @Override public Id creaRegistro() {
            return new Id(null);
        }

        @Override public Lista<Id> buscaRegistros(String campo, String texto) {
            Lista<Id> r = new Lista<Id>();
            for (Id id : registros) {
                if(campo.equals("id"))
                {
                    if (id.getId().indexOf(texto) != -1)
                        r.agregaFinal(id);
                    
                }
            }
            return r;
        }
    }

    private Random random;
    private BaseDeDatosId bdd;
    private int total;

    /**
     * Crea un generador de números aleatorios para cada prueba y
     * una base de datos.
     */
    public TestBaseDeDatos() {
        random = new Random();
        bdd = new BaseDeDatosId();
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
    @Test public void testNumeroDeRegistros() {
        for (int i = 0; i < total; i++) {
            Id id = new Id(String.valueOf(random.nextInt()));
            bdd.agregaRegistro(id);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        Id[] ids = new Id[total];
        for (int i = 0; i < total; i++) {
            ids[i] = new Id(String.valueOf(random.nextInt()));
            bdd.agregaRegistro(ids[i]);
        }
        Lista<Id> l = bdd.getRegistros();
        int i = 0;
        for (Id id : l)
            ids[i++].equals(id);
        l.elimina(ids[0]);
        Assert.assertFalse(l.equals(bdd.getRegistros()));
        Assert.assertFalse(l.getLongitud() == bdd.getNumRegistros());
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        for (int i = 0; i < total; i++) {
            Id id = new Id(String.valueOf(random.nextInt()));
            bdd.agregaRegistro(id);
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
            Id id = new Id(String.valueOf(ini + i));
            bdd.agregaRegistro(id);
        }
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            Id id = (Id)bdd.getRegistros().get(i);
            Assert.assertTrue(bdd.getRegistros().contiene(id));
            bdd.eliminaRegistro(id);
            Assert.assertFalse(bdd.getRegistros().contiene(id));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        for (int i = 0; i < total; i++) {
            Id id = new Id(String.valueOf(random.nextInt()));
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
            ids[i] = new Id(id);
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
}
