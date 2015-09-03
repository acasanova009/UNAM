package mx.unam.ciencias.edd;

import java.util.Random;

/**
 * Práctica 7: Gráficas.
 */
public class Practica7 {

    private static String cadena;

    public static void main(String[] args) {
        /*
         *    b─────d
         *   ╱│╲    │╲
         *  ╱ │ ╲   │ ╲
         * a  │  ╲  │  f
         *  ╲ │   ╲ │ ╱
         *   ╲│    ╲│╱
         *    c─────e
         */
        Grafica<String> grafica = new Grafica<String>();
        grafica.agrega("a");
        grafica.agrega("b");
        grafica.agrega("c");
        grafica.agrega("d");
        grafica.agrega("e");
        grafica.agrega("f");

        grafica.conecta("a", "b");
        grafica.conecta("a", "c");
        grafica.conecta("b", "c");
        grafica.conecta("b", "d");
        grafica.conecta("b", "e");
        grafica.conecta("c", "e");
        grafica.conecta("d", "e");
        grafica.conecta("d", "f");
        grafica.conecta("e", "f");

        /* BFS */
        grafica.paraCadaVertice(new AccionVerticeGrafica<String>() {
                public void actua(VerticeGrafica<String> vertice) {
                    vertice.setColor(Color.ROJO);
                }
            });
        Cola<VerticeGrafica<String>> c = new Cola<VerticeGrafica<String>>();
        VerticeGrafica<String> vertice = grafica.vertice("a");
        vertice.setColor(Color.NEGRO);
        c.mete(vertice);
        cadena = "BFS 1: ";
        while (!c.esVacia()) {
            vertice = c.saca();
            cadena += vertice.getElemento() + ", ";
            for (VerticeGrafica<String> vecino : vertice) {
                if (vecino.getColor() == Color.ROJO) {
                    vecino.setColor(Color.NEGRO);
                    c.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* BFS de la clase */
        cadena = "BFS 2: ";
        grafica.bfs("a", new AccionVerticeGrafica<String>() {
                public void actua(VerticeGrafica<String> vertice) {
                    cadena += vertice.getElemento() + ", ";
                }
            });
        System.out.println(cadena);

        /* DFS */
        grafica.paraCadaVertice(new AccionVerticeGrafica<String>() {
                public void actua(VerticeGrafica<String> vertice) {
                    vertice.setColor(Color.ROJO);
                }
            });
        Pila<VerticeGrafica<String>> p = new Pila<VerticeGrafica<String>>();
        vertice = grafica.vertice("a");
        vertice.setColor(Color.NEGRO);
        p.mete(vertice);
        cadena = "DFS 1: ";
        while (!p.esVacia()) {
            vertice = p.saca();
            cadena += vertice.getElemento() + ", ";
            for (VerticeGrafica<String> vecino : vertice) {
                if (vecino.getColor() == Color.ROJO) {
                    vecino.setColor(Color.NEGRO);
                    p.mete(vecino);
                }
            }
        }
        System.out.println(cadena);

        /* DFS de la clase */
        cadena = "DFS 2: ";
        grafica.dfs("a", new AccionVerticeGrafica<String>() {
                public void actua(VerticeGrafica<String> vertice) {
                    cadena += vertice.getElemento() + ", ";
                }
            });
        System.out.println(cadena);
    }
}
