package mx.unam.ciencias.icc;

import java.util.Random;

/**
 * Práctica 5: Genéricos.
 */
public class Practica5 {

    public static void main(String[] args) {
        Random rnd = new Random();
        int n = 10 + rnd.nextInt(10);

        Lista<Integer> l = new Lista<Integer>();
        for (int i = 0; i < n; i++)
            l.agregaFinal(rnd.nextInt(n));

        System.out.printf("Lista: %s\n", l.toString());
        System.out.println("Longitud de la lista: " + l.getLongitud());
        l.primero();
        System.out.println("Primer elemento: " + l.dame());
        l.ultimo();
        System.out.println("Último elemento: " + l.dame());

        int i = 1;
        l.primero();
        while (l.iteradorValido()) {
            System.out.printf("Elemento %d: %d\n", i++, l.dame());
            l.siguiente();
        }

        Lista<Integer> r = l.reversa();
        System.out.printf("Lista original: %s\n", l.toString());
        System.out.printf("Lista reversa: %s\n", r.toString());

        while (l.getLongitud() > 0) {
            int e = l.get(rnd.nextInt(l.getLongitud()));
            System.out.printf("Eliminamos %d\n", e);
            l.elimina(e);
            System.out.printf("Lista: %s\n", l.toString());
        }
    }
}
