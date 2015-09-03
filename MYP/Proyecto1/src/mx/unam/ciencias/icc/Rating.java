package mx.unam.ciencias.icc;

import java.lang.Comparable;
import java.io.IOException;
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
public class Rating{

    
    private int rate;
    final int  maxRate = 5;
    /**
     * Constructor único.
     *
     * Dado que no proveemos <em>setters</em>, nuestras matrices de
     * 2×2 son <em>inmutables</em>; no podemos cambiar sus valores.
     */
    public Rating(){
        setRate(0);
    }
    public Rating(int i){
        setRate(0);
        setRate(i);
    }

    /**
     * Regresa el rate de la cancion.
     * @return El rate <tt>a</tt> de la cancion.
     */
    public int getRate() {
        return rate;
    }
    

    /**
     * Pone un valor especifioco de rating dentro de un rango 0-maxRate
     * @return Boolean de se logro cambiar el rate <tt>a</tt> de la cancion?.
     */
    public boolean setRate(int i)
    {
        
        if(i>maxRate || i<0)
        return false;
        
        rate = i;
        
        return true;
    }

    /**
     * Aumenta el valor del rating en uno.
     *
     */
    public void aumentarRate()
    {
        if( rate == maxRate)
            setRate(0);
        
        else
        setRate(rate+1);
    }
    /**
     * Aumenta el valor del rating en uno.
     *
     */
    public void disminuirRate()
    {
        setRate(rate-1);
    }

    /**
     * Multiplica la matriz de 2×2 con la matriz de 2×2 que recibe
     * como parámetro.
     * @param m La matriz de 2×2 con la que hay que multiplicar.
     * @return La multiplicación con la matriz de 2×2 <tt>m</tt>.
     */
    /**
     * Nos dice si el objeto recibio es un rating object.
     * @param o el objeto con el que se comparará el que manda
     *        llamar el método.
     * @return <tt>true</tt> Si el objeto es identico, expceto su enlce; <tt>false</tt>
     *         en otro caso.
     */
    public boolean equals(Object obj) {
        
        boolean isEqual = false;
        
        
        
        if (obj !=null && (obj instanceof Rating))
        {
            Rating m = (Rating)obj;
            if(this.rate == m.rate)
                isEqual = true;
        }
        
        return isEqual;
    }


    /**
     * Regresa una representación en cadena de la matriz de 2×2. La
     * representación es de la forma:
<pre>
 [-*-*-] || [-] || [-*-];
</pre>
     * @return una representación en cadena del rating.
     */
    public String toString() {
        
        String imp = "[";
        for(int i =0;i<rate;i++)
            imp+="-*";
        for(int i =rate; i <maxRate;i++)
            imp+="-o";
        
        
        imp+="-]";
        return imp;
        
    }
 
}
