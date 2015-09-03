package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 4: Árboles binarios y árboles binarios completos.
 */
public class Practica4 {
    public static void main(String[] args) {
        ArbolBinarioCompleto<Integer> arbol = new ArbolBinarioCompleto<Integer>();
        for (int i = 0; i < 15; i++)
            arbol.agrega(i);

        int i = 0;
        while (arbol.getElementos() > 0) {
            System.out.println("----------------------------------");
            System.out.printf("Árbol con %d elemento(s): ", arbol.getElementos());
            for (Integer e : arbol)
                System.out.printf("%d, ", e);
            System.out.println();
            System.out.println(arbol);
            arbol.elimina(i++);
        }
    }
}
