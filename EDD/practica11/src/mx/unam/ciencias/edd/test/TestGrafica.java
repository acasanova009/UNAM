package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.AccionVerticeGrafica;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeGrafica;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link Grafica}.
 */
public class TestGrafica {

    private int total;
    private Random random;
    private Grafica<Integer> grafica;

    /**
     * Crea una gráfica para cada prueba.
     */
    public TestGrafica() {
        random = new Random();
        total = 2 + random.nextInt(100);
        grafica = new Grafica<Integer>();
    }

    /**
     * Prueba unitaria para {@link Grafica#Grafica}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(grafica.getVertices() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#getVertices}.
     */
    @Test public void testGetVertices() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.getVertices() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#getAristas}.
     */
    @Test public void testGetAristas() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int cont = 0;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                grafica.conecta(i, j);
                cont++;
                Assert.assertTrue(grafica.getAristas() == cont);
            }
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#agrega}.
     */
    @Test public void testAgrega() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.contiene(i));
        }
        try {
            grafica.agrega(total/2);
        } catch (IllegalArgumentException iae) {
            return;
        }
        Assert.fail();
    }

    /**
     * Prueba unitaria para {@link Grafica#conecta}.
     */
    @Test public void testConecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        boolean excepcion = false;
        try {
            grafica.conecta(0, 0);
        } catch (IllegalArgumentException iae) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
        excepcion = false;
        try {
            grafica.conecta(-1, -2);
        } catch (NoSuchElementException nsee) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
        excepcion = false;
        try {
            grafica.conecta(0, 1);
        } catch (IllegalArgumentException iae) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
    }

    /**
     * Prueba unitaria para {@link Grafica#conecta(Object,Object,double)}.
     */
    @Test public void testConectaDouble() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 1;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j, c);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                Assert.assertTrue(grafica.getPeso(i, j) == c++);
            }
        }
        boolean excepcion = false;
        try {
            grafica.conecta(0, 0);
        } catch (IllegalArgumentException iae) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
        excepcion = false;
        try {
            grafica.conecta(-1, -2);
        } catch (NoSuchElementException nsee) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
        excepcion = false;
        try {
            grafica.conecta(0, 1);
        } catch (IllegalArgumentException iae) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
    }

    /**
     * Prueba unitaria para {@link Grafica#desconecta}.
     */
    @Test public void testDesconecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int c = (total * (total-1)) / 2;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertTrue(grafica.getAristas() == c--);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                grafica.desconecta(i, j);
                Assert.assertFalse(grafica.sonVecinos(i, j));
            }
        }
        boolean excepcion = false;
        try {
            grafica.desconecta(0, 0);
        } catch (IllegalArgumentException iae) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
        excepcion = false;
        try {
            grafica.desconecta(-1, -2);
        } catch (NoSuchElementException nsee) {
            excepcion = true;
        }
        Assert.assertTrue(excepcion);
    }

    /**
     * Prueba unitaria para {@link Grafica#contiene}.
     */
    @Test public void testContiene() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.contiene(i));
        }
        Assert.assertFalse(grafica.contiene(-1));
    }

    /**
     * Prueba unitaria para {@link Grafica#elimina}.
     */
    @Test public void testElimina() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int vertices = total;
        int aristas = (total * (total - 1)) / 2;
        final int[] grado = { vertices - 1 };
        for (int i = 0; i < total; i++) {
            grafica.paraCadaVertice(new AccionVerticeGrafica<Integer>() {
                    public void actua(VerticeGrafica<Integer> vertice) {
                        Assert.assertTrue(vertice.getGrado() == grado[0]);
                    }
                });
            Assert.assertTrue(grafica.getVertices() == vertices);
            Assert.assertTrue(grafica.getAristas() == aristas);
            grafica.elimina(i);
            vertices--;
            aristas -= vertices;
            grado[0]--;
        }
        Assert.assertTrue(grafica.getVertices() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#sonVecinos}.
     */
    @Test public void testSonVecinos() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.sonVecinos(-1, -2);
        } catch (NoSuchElementException nsee) {
            return;
        }
        Assert.fail();
    }

   /**
     * Prueba unitaria para {@link Grafica#getPeso}.
     */
    @Test public void testGetPeso() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 1;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j, c++);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        c = 1;
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                Assert.assertTrue(grafica.getPeso(i, j) == c++);
    }

   /**
     * Prueba unitaria para {@link Grafica#vertice}.
     */
    @Test public void testVertice() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        VerticeGrafica<Integer> vertice = grafica.vertice(0);
        Assert.assertTrue(vertice.getElemento() == 0);
        Assert.assertTrue(vertice.getGrado() == total - 1);
        Assert.assertTrue(vertice.getColor() == Color.NINGUNO);
        vertice.setColor(Color.ROJO);
        Assert.assertTrue(vertice.getColor() == Color.ROJO);
        int v = 1;
        Lista<Integer> vecinos = new Lista<Integer>();
        for (int i = 1; i < total; i++)
            vecinos.agregaFinal(i);
        for (VerticeGrafica<Integer> vecino : vertice) {
            Assert.assertTrue(vecinos.contiene(vecino.getElemento()));
            vecinos.elimina(vecino.getElemento());
        }
        Assert.assertTrue(vecinos.getLongitud() == 0);
    }

   /**
     * Prueba unitaria para {@link Grafica#paraCadaVertice}.
     */
    @Test public void testParaCadaVertice() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        final int[] cont = { 0 };
        grafica.paraCadaVertice(new AccionVerticeGrafica<Integer>() {
                public void actua(VerticeGrafica<Integer> vertice) {
                    vertice.setColor(Color.ROJO);
                    cont[0]++;
                }
            });
        Assert.assertTrue(cont[0] == total);
        grafica.paraCadaVertice(new AccionVerticeGrafica<Integer>() {
                public void actua(VerticeGrafica<Integer> vertice) {
                    Assert.assertTrue(vertice.getColor() == Color.ROJO);
                }
            });
    }

   /**
     * Prueba unitaria para {@link Grafica#bfs}.
     */
    @Test public void testBfs() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        final Lista<Integer> lista = new Lista<Integer>();
        grafica.bfs(0, new AccionVerticeGrafica<Integer>() {
                public void actua(VerticeGrafica<Integer> vertice) {
                    lista.agregaFinal(vertice.getElemento());
                }
            });
        Lista<Integer> vertices = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            vertices.agregaFinal(i);
        Assert.assertTrue(lista.getLongitud() == vertices.getLongitud());
        for (Integer n : lista)
            Assert.assertTrue(vertices.contiene(lista.eliminaPrimero()));
    }

    /**
     * Prueba unitaria para {@link Grafica#dfs}.
     */
    @Test public void testDfs() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        final Lista<Integer> lista = new Lista<Integer>();
        grafica.dfs(0, new AccionVerticeGrafica<Integer>() {
                public void actua(VerticeGrafica<Integer> vertice) {
                    lista.agregaFinal(vertice.getElemento());
                }
            });
        Lista<Integer> vertices = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            vertices.agregaFinal(i);
        Assert.assertTrue(lista.getLongitud() == vertices.getLongitud());
        for (Integer n : lista)
            Assert.assertTrue(vertices.contiene(lista.eliminaPrimero()));
    }

    /**
     * Prueba unitaria para {@link Grafica#iterator}.
     */
    @Test public void testIterator() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 0;
        for (Integer i : grafica)
            Assert.assertTrue(i == c++);
    }

    /* Crea una gráfica específica para correr TrayectoriaMinima y
     * Dijsktra. */
    private void llenaGrafica() {
        grafica.agrega(1);
        grafica.agrega(2);
        grafica.agrega(3);
        grafica.agrega(4);
        grafica.agrega(5);
        grafica.agrega(6);
        grafica.conecta(1, 2, 1);
        grafica.conecta(1, 3, 2);
        grafica.conecta(2, 3, 2);
        grafica.conecta(2, 4, 3);
        grafica.conecta(2, 5, 1);
        grafica.conecta(3, 5, 1);
        grafica.conecta(4, 5, 2);
        grafica.conecta(4, 6, 1);
        grafica.conecta(5, 6, 1);
    }

    /**
     * Prueba unitaria para {@link Grafica#trayectoriaMinima}.
     */
    @Test public void testTreyactoriaMinima() {
        llenaGrafica();
        Lista<VerticeGrafica<Integer>> trayectoria = grafica.trayectoriaMinima(1, 6);
        Assert.assertTrue(trayectoria.getLongitud() == 4);
        int[] t = { 1, 2, 4, 6 };
        int c = 0;
        for (VerticeGrafica<Integer> v : trayectoria)
            Assert.assertTrue(v.getElemento() == t[c++]);
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 1; i < total; i++)
            grafica.conecta(i - 1, i);
        trayectoria = grafica.trayectoriaMinima(0, total - 1);
        Assert.assertTrue(trayectoria.getLongitud() == total);
        c = 0;
        for (VerticeGrafica<Integer> v : trayectoria)
            Assert.assertTrue(v.getElemento() == c++);
    }

   /**
     * Prueba unitaria para {@link Grafica#dijkstra}.
     */
    @Test public void testDijkstra() {
        llenaGrafica();
        Lista<VerticeGrafica<Integer>> dijkstra = grafica.dijkstra(1, 6);
        Assert.assertTrue(dijkstra.getLongitud() == 4);
        int[] t = { 1, 2, 5, 6 };
        int c = 0;
        for (VerticeGrafica<Integer> v : dijkstra)
            Assert.assertTrue(v.getElemento() == t[c++]);
        grafica = new Grafica<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 1; i < total; i++)
            grafica.conecta(i - 1, i, 1.0);
        grafica.conecta(total - 1, 0, total*10);
        dijkstra = grafica.dijkstra(0, total - 1);
        Assert.assertTrue(dijkstra.getLongitud() == total);
        c = 0;
        for (VerticeGrafica<Integer> v : dijkstra)
            Assert.assertTrue(v.getElemento() == c++);
    }
}
