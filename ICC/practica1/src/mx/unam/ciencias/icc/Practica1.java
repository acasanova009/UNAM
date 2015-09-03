package mx.unam.ciencias.icc;

import java.util.Random;

/**
 * Pr√°ctica 1: Orientaci√≥n a Objetos y sintaxis de Java.
 */
public class Practica1 {

    public static void main(String[] args) {
        Random r = new Random();

        Matriz2x2 m = new Matriz2x2(r.nextDouble() * 100,
                                    r.nextDouble() * 100,
                                    r.nextDouble() * 100,
                                    r.nextDouble() * 100);
        Matriz2x2 n = new Matriz2x2(r.nextDouble() * 100,
                                    r.nextDouble() * 100,
                                    r.nextDouble() * 100,
                                    r.nextDouble() * 100);
        System.out.println("m:");
        System.out.println(m);
        System.out.println("n:");
        System.out.println(n);

        System.out.println("m + n:");
        System.out.println(m.suma(n));
        System.out.println("m * n:");
        System.out.println(m.multiplica(n));

        double x = r.nextDouble() * 20;
        System.out.printf("m * %2.3f:\n", x);
        System.out.println(m.multiplica(x));
        System.out.printf("determinante(m): %2.3f\n",
                          m.determinante());

        Matriz2x2 i = m.inversa();
        System.out.println("m‚inversa:");
        if (i == null) {
            System.out.println("La matriz m no tiene inversa.");
        } else {
            System.out.println(i);
            Matriz2x2 id = m.multiplica(i);
            System.out.println("m * m‚inversa:");
            System.out.println(id);
        }

        int p = 1 + r.nextInt(10);
        System.out.printf("m‚Potencia (n = %d):\n", p);
        System.out.println(m.potencia(p));
    }
}
