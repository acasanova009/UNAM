package mx.unam.ciencias.icc;

import java.util.Random;

/**
 * Práctica 3: Recursión.
 */
public class Practica3 {

    public static void main(String[] args) {
        Random rnd = new Random();
        int n = 10 + rnd.nextInt(10);

        ListaCadena l = new ListaCadena();
        for (int i = 0; i < n; i++) {
            String s = String.format("%d", rnd.nextInt(n));
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

        ListaCadena r = l.reversa();
        System.out.printf("Lista original: %s\n", l.toString());
        System.out.printf("Lista reversa : %s\n", r.toString());

        while (l.getLongitud() > 0) {
            String s = l.get(rnd.nextInt(l.getLongitud()));
            System.out.printf("Eliminamos %s\n", s);
            l.elimina(s);
            System.out.printf("Lista: %s\n", l.toString());
        }
    }
}
