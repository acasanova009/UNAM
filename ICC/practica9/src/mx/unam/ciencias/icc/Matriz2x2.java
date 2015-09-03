package mx.unam.ciencias.icc;

/**
 * Clase para matrices de 2×2.
 *
 * Las matrices de 2×2 pueden sumarse, multiplicarse, sacar su
 * determinante, obtener su matriz inversa (una matriz multiplicada
 * por su inversa resulta en la matriz identidad), y elevarla a la
 * potencia <em>n</em> (multiplicarla consigo misma <em>n</em>
 * veces).
 *
 * Las matrices se crean con cuatro dobles a, b, c y d, tales que
 * representan a la matriz:
 *
<pre>
 ⎛ a  b ⎞
 ⎝ c  d ⎠
</pre>
 */
public class Matriz2x2 {

    private double a;
    private double b;
    private double c;
    private double d;

    /**
     * Constructor único.
     *
     * Dado que no proveemos <em>setters</em>, nuestras matrices de
     * 2×2 son <em>inmutables</em>; no podemos cambiar sus valores.
     */
    public Matriz2x2(double a, double b,
                     double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        // Aquí va su código.
    }

    /**
     * Regresa el elemento <tt>a</tt> de la matriz de 2×2.
     * @return El elemento <tt>a</tt> de la matriz de 2×2.
     */
    public double getA() {
        return a;
    }

    /**
     * Regresa el elemento <tt>b</tt> de la matriz de 2×2.
     * @return El elemento <tt>b</tt> de la matriz de 2×2.
     */
    public double getB() {
        return b;
    }

    /**
     * Regresa el elemento <tt>c</tt> de la matriz de 2×2.
     * @return El elemento <tt>c</tt> de la matriz de 2×2.
     */
    public double getC() {
        // Aquí va su código.
      return c;
    }

    /**
     * Regresa el elemento <tt>d</tt> de la matriz de 2×2.
     * @return El elemento <tt>d</tt> de la matriz de 2×2.
     */
    public double getD() {
        // Aquí va su código.
      return d;
    }

    /**
     * Suma la matriz de 2×2 con la matriz de 2×2 que recibe como
     * parámetro.
     * @param m La matriz de 2×2 con la que hay que sumar.
     * @return La suma con la matriz de 2×2 <tt>m</tt>.
     *
     */
    public Matriz2x2 suma(Matriz2x2 m) {
        return new Matriz2x2(a + m.a, b + m.b,
                             c + m.c, d + m.d);
    }

    /**
     * Multiplica la matriz de 2×2 con la matriz de 2×2 que recibe
     * como parámetro.
     * @param m La matriz de 2×2 con la que hay que multiplicar.
     * @return La multiplicación con la matriz de 2×2 <tt>m</tt>.
     */
    public Matriz2x2 multiplica(Matriz2x2 m) {
        // Aquí va su código.
      return new Matriz2x2(this.a * m.a + this.b * m.c, this.a * m.b + this.b *m.d,
                             this.c * m.a + this.d * m.c, this.c * m.b + this.d * m.d);
    }

    /**
     * Multiplica la matriz de 2×2 con la constante que recibe como
     * parámetro.
     * @param x La constante con la que hay que multiplicar.
     * @return La multiplicación con la constante <tt>x</tt>.
     */
    public Matriz2x2 multiplica(double x) {
      
      
         return new Matriz2x2(a * x, b * x,
                             c * x, d * x);
    }

    /**
     * Calcula el determinante de la matriz de 2×2.
     * @return El determinante de la matriz de 2×2.
     */
    public double determinante() {
        // Aquí va su código.
      double det = 0;
      det = (a *d) - (b *c);
      return det;
    }

    /**
     * Calcula la inversa de la matriz de 2×2.
     *
     * Si multiplicamos una matriz de 2×2 con su inversa, obtenemos
     * la matriz identidad.
     * @return La inversa de la matriz de 2×2, o <tt>null</tt> si la
     *         matriz no es inversible.
     */
    public Matriz2x2 inversa() {
        double det = determinante();
        if (det == 0.0)
            return null;
        
        return new Matriz2x2(d/det, -b/det,-c/det, a/det);
    }

    /**
     * Calcula la <em>n</em>-ésima potencia de la matriz de 2×2.
     *
     * La <em>n</em>-ésima potencia de una matriz de 2×2 es el
     * resultado de multiplicar la matriz consigo misma <em>n</em>
     * veces.
     * @param n La potencia a la que hay que elevar la matriz; si
     *          <em>n</em> es menor que 2, regresa una copia de la
     *          matriz de 2×2.
     * @return la <em>n</em>-ésima potencia de la matriz de 2×2.
     */
    public Matriz2x2 potencia(int n) {
        Matriz2x2 m = new Matriz2x2(a, b, c, d);
        if (n < 2)
            return m;
        while (n > 1) {
            m = m.multiplica(this);
            n--;
        }
        return m;
    }

    /**
     * Nos dice si el objeto recibido es una matrix de 2×2 igual a
     * la que manda llamar al método.
     * @param o el objeto con el que se comparará el que manda
     *        llamar el método.
     * @return <tt>true</tt> si el objeto o es una matrix de 2×2
     *         igual a la que manda llamar al método; <tt>false</tt>
     *         en otro caso.
     */
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Matriz2x2))
            return false;
        Matriz2x2 m = (Matriz2x2)o;
        return a == m.a && b == m.b && c == m.c && d == m.d;
    }

    /* Método auxiliar de toString(): agrega espacios a la cadena
     * hasta que tenga longitud n. */
    private String agregaEspacios(String s, int n) {
        String r = s;
        while (r.length() < n)
            r = " " + r;
        return r;
    }

    /**
     * Regresa una representación en cadena de la matriz de 2×2. La
     * representación es de la forma:
<pre>
 ⎛ a  b ⎞
 ⎝ c  d ⎠
</pre>
     * @return una representación en cadena de la matriz de 2×2.
     */
    public String toString() {
        String sa = String.format("%2.3f", a);
        String sb = String.format("%2.3f", b);
        String sc = String.format("%2.3f", c);
        String sd = String.format("%2.3f", d);
        int n = Math.max(Math.max(sa.length(), sb.length()),
                         Math.max(sc.length(), sd.length()));
        sa = agregaEspacios(sa, n);
        sb = agregaEspacios(sb, n);
        sc = agregaEspacios(sc, n);
        sd = agregaEspacios(sd, n);
        return
            
            String.format("⎛ %s, %s ⎞\n", sa, sb) +
            String.format("⎝ %s, %s ⎠",   sc, sd);
    }
}
