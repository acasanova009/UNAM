package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 2: Pilas y colas.
 */
public class Practica3 {
    public static void main(String[] args) {
        String[] a = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

        Pila<String> pila = new Pila<String>();
        Cola<String> cola = new Cola<String>();

        System.out.println("Arreglo original:");
        for (String s : a) {
            System.out.print(s + " ");
            pila.mete(s);
            cola.mete(s);
        }
        System.out.println();

        System.out.println("Pila:");
        while (!pila.esVacia())
            System.out.print(pila.saca() + " ");
        System.out.println();

        System.out.println("Cola:");
        while (!cola.esVacia())
            System.out.print(cola.saca() + " ");
        System.out.println();
    }
}
