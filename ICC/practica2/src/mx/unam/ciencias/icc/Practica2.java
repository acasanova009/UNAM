package mx.unam.ciencias.icc;

import java.util.Random;

/**
 * Práctica 2: Listas y estructuras de control.
 */
public class Practica2 {

    public static void main(String[] args) {
        Random r = new Random();
        int n = 10 + r.nextInt(10);

        ListaCadena l = new ListaCadena();
        for (int i = 0; i < n; i++) {
            String s = String.format("%d", r.nextInt(n));
            l.agregaFinal(s);
        }

        System.out.printf("Lista: %s\n", l.toString());
        System.out.println("Longitud de la lista: " + l.getLongitud());
        l.primero();
        System.out.println("Primer elemento: " + l.get());
        l.ultimo();
        System.out.println("Último elemento: " + l.get());

        int i = 1;
        l.primero();
        while (l.iteradorValido()) {
            System.out.printf("Elemento %d: %s\n", i++, l.get());
            l.siguiente();
        }

        while (l.getLongitud() > 0) {
            String s = l.get(r.nextInt(l.getLongitud()));
            System.out.printf("Eliminamos %s\n", s);
            l.elimina(s);
            System.out.printf("Lista: %s\n", l.toString());
        }
    }
}
