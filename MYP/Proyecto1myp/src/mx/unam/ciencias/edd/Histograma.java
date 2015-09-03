

package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import java.math.BigInteger;

/**
 * Clase para almacenamiento de histogramas.
 * Operaciones:
 * generaSVG grafica de barras o lineas, Int Mayores || Int menores.
 * generaSVG grafica de pastel **
 * Puede ver si se esta relacionado con otro Histograma para ver si tienen mismos T's.
 * Se puede agregar histogramas tipo T.
 * Se agregan elementos al histograma, como Tipo T y la cantidad de veces que sale.
 * Se puede agregar simultaneamente dos listas, una tipo T y otra con sus valores correspondientes.
 * Se eliminan tipos T.
 * Metodo estatico para evaluacion lineal de documentos. Seccionamiento.
 */
public class Histograma<T> implements Iterable<T> {
    

//    /**
//     * Regresa un iterador para iterar el histograma. El histograma se
//     * itera en el orden en que fueron agregados sus elementos.
//     * @return un iterador para iterar la gráfica.
//     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(this);
//        return null;
    }
//
//    
//    /* Clase privada para iteradores de histogramas. */
    private class Iterador<T> implements Iterator<T> {
        
        /* Iterador auxiliar. */
        private Iterator<Histograma<T>.Contador<T>> iterator;
        
        /* Construye un nuevo iterador, auxiliándose del diccionario de contadores.
         *  */
        public Iterador(Histograma<T> h) {
             iterator =  h.d.iterator();
        }
        
        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            return iterator.hasNext();
        }
        
        /* Regresa el siguiente elemento. */
        public T next() {
            Histograma<T>.Contador<T> c  = iterator.next();
            return c.elemento;
        }
        
        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    
    
    public boolean relacionado(Histograma<T> h)
    {
        
        boolean estaRelacionado = false;
        for(T t : this)
            if(h.contiene(t))
            {
                estaRelacionado= true;
                break;
            }
        
    
        return estaRelacionado;
    }

    private class Contador<T> implements Comparable<Contador<T>> {
        
        /*
         *Llevara el conteo de las repeticiones de un unico T elemento
         */
        
        /* Cantidad de veces que esta el elemento. */
        public int cantidad;
        /* Elemtnto T */
        public T elemento;
        
        Contador(T t, int c )
        {
            cantidad = c;
            elemento = t;
        }
        
        //En camparable la cantidad es mas importante que el elemento.
        @Override public int compareTo(Contador<T> c)
        {
            int igual = 0;
            
            //LA IDEA ERA DEFINIR CON PRESICION CUAL CONTADOR ERA MAYOR... Y TAMBIEN PODER ORDENAR LOS TIPO T SI ERAN COMPARABLES...
//            if(cantidad == c.cantidad && elemento instanceof Comparable )
//            {
//               @SuppressWarnings("unchecked") Comparable<T> e = (Comparable<T>)c;
//               @SuppressWarnings("unchecked") Comparable<T> cE = (Comparable<T>)c.elemento;
////
//                igual = cE.compareTo(e);
//            }
            if(cantidad > c.cantidad)
                igual = 1;
            if(cantidad < c.cantidad)
                igual = -1;
            
            return igual;
            
        }
        @Override public String toString()
        {
            return String.format("(%s,%d)",elemento.toString(),cantidad);
        }
    }
    
    
    /*
     * Diccionario donde estaran guardados los elementos Tipo T con su respectivo Int deCantidad.
     */
    
    
    /*Nombre de este histograma*/
    public String nombre;
    /*Acumulador de cada cantidad de veces que salen en total todos los elementos*/
    private int apariciones;
    /*Diccionario donde estaran los elementos cuantificados*/
    private Diccionario<T, Contador<T> > d;

    public Histograma()
    {
        this("");
    }
    public Histograma(String name)
    {
        apariciones = 0;
        nombre = name;
        d =  new Diccionario<T, Contador<T>>();
     
    }
    
    

    public void agrega(Histograma<T> h )
    {
        for(T t : h)
        {
            Contador<T> c = h.d.get(t);
            
            
            if(this.contiene(t))
            {
                Contador<T> cT = this.d.get(t);
                cT.cantidad+=c.cantidad;
                apariciones+=c.cantidad;
            }
            else
            {
                this.agrega( c.elemento, c.cantidad );
            }
        }
    }
    
    public void agrega(Lista<T> elementos)
    {
        for(T e : elementos)
            agrega(e);
        
    }
    public void agrega(Lista<T> elementos, Lista<Integer> values)
    {
        if(elementos.getLongitud()!= values.getLongitud())
            return;
        
        Iterator<Integer> vI = values.iterator();
        
        for(T e : elementos)
        {
            Integer i = vI.next();
            agrega(e, i.intValue());
        }

    }
    
    /**
     * Agrega un elemento al diccionario.
     * @param elemento el elemento que queremos vamos a agregar.
     * en el diccionario.
     */
    public void agrega(T e )
    {
        agrega(e,1);
    }
    
     /**
     * Agrega un elemento al diccionario con su respectiva cantidad.
     * @param elemento el elemento que queremos vamos a agregar.
     * @param c la cantidad de veces que este elemento aparece.
     * en el diccionario.
     */
    public void agrega(T e, int c )
    {
        //Revisar si ya existe.
        
        apariciones+=c;
        if(d.contiene(e))
        {
            Contador<T> cT = this.d.get(e);
            cT.cantidad+=c;
        }
        else{
        
            Contador<T> conta = new Contador<T>(e, c);
            d.agrega(e, conta);
        }
    }
    
    
    /**
     * Regresa el número de valores en el histograma.
     * @return el número de valores en el histograma.
     */
    public int getTotal()
    {
        return d.getTotal();
    }
    
    /**
     * Regresa la suma de apariciones de todas los elementos.
     * @return el número de apariciones en el histograma.
     */
    public int getTotalApariciones()
    {
        return apariciones;
    }
    /**
     * Elimina el elemento. Y ajusta la cantidad de apariciones.
     */

    public void elimina(T e)
    {
        if(!d.contiene(e))
            return;
        
        Contador c = d.get(e);
        apariciones-=c.cantidad;
        d.elimina(e);
    }
    /** Contiene*/
    
    public boolean contiene(T e)
    {
        return d.contiene(e);
    }
    /** Metodo que permite con una lista de elementos comparables, ordenarlos, y cuantificarlos.
     *  Si tengo [a,b,a,a,b,c,a,c];
     *  Regresa un histograma con [(a,4),(b,2),(c,2)];
     */
    
    public static <T extends Comparable<T>> Histograma<T> seccionamiento(Lista<T> lista)
    {
        lista = Lista.mergeSort(lista);
        Histograma<T> h = new Histograma<T>();
        
        T same = lista.getPrimero();
        int c = 0;
        for(T e : lista)
        {
            if(same.equals(e))
                c++;
            else
            {
                //crea nuevo contador
                h.agrega(same, c);
                same = e;
                c=1;
            }
        }
        h.agrega(same,c);

        return h;
    }
    
    private Lista<Contador<T>> getOrderedValues(Lista<Contador<T>> ordered, boolean maxValuesFirst )
    {
        ordered = Lista.mergeSort(ordered);
    
        if(maxValuesFirst)
            ordered =ordered.reversa();
        
        
        
        return ordered;
    }
    
    
    /**
     * Genera un string con la representacion de una grafica del histograma en formato svg.
     *
     * @return String que contiene la representacion grafica.
     *
     */
    public String toScalableVectorGraphicsBars()
    {
        return toScalableVectorGraphicsBars(true);
    }
    
    /**
     * Genera un string con la representacion de una grafica del histograma en formato svg.
     *
     * @param drawMaxValuesFirst boolean que nos permite decidir si pintamos los valores maximo al principio.
     * @return String que contiene la representacion grafica.
     *
     */
    public String toScalableVectorGraphicsBars(boolean drawMaxValuesFirst)
    {
        return toScalableVectorGraphicsBars(drawMaxValuesFirst,d.getTotal(),false,true);
    }
    
   
    
    /**
     * Genera un string con la representacion de una grafica del histograma en formato svg.
     *
     * @param drawMaxValuesFirst boolean que nos permite decidir si pintamos los valores maximo al principio.
     * @param howMany Numero barras que se va a imprimir.
     * @return String que contiene la representacion grafica.
     *
     */

    public String toScalableVectorGraphicsBars(boolean drawMaxValuesFirst ,int howMany)
    {
        return toScalableVectorGraphicsBars(drawMaxValuesFirst,howMany,false, true);
    }
    
    
    /**
     * Genera un string con la representacion de una grafica del histograma en formato svg.
     *
     * @param drawMaxValuesFirst boolean que nos permite decidir si pintamos los valores maximo al principio.
     * @param howMany Numero barras que se va a imprimir.
     * @param line Define si va a ser grafica estilo lineas o barras.
     * @param highestValueBeUsedAsMax Si vamos a usar la sumatoria total de aparciciones de o el valor maixmo como el tope de la grafica.
     * @return String que contiene la representacion grafica.
     *
     */
    
    public String toScalableVectorGraphicsBars(boolean drawMaxValuesFirst, int howMany, boolean line, boolean highestValueBeUsedAsMax )
    {
        return toScalableVectorGraphicsBars(drawMaxValuesFirst,howMany,line,highestValueBeUsedAsMax, false);
    }
    
    /**
     * Genera un string con la representacion de una grafica del histograma en formato svg.
     *
     * @param drawMaxValuesFirst boolean que nos permite decidir si pintamos los valores maximo al principio.
     * @param howMany Numero barras que se va a imprimir.
     * @param line Define si va a ser grafica estilo lineas o barras.
     * @param highestValueBeUsedAsMax Si vamos a usar la sumatoria total de aparciciones de o el valor maixmo como el tope de la grafica.
     * @param randomOrder booleano que define si el orden, de ser -true-, es random.
     * @return String que contiene la representacion grafica.
     *
     */
    
    public String toScalableVectorGraphicsBars(boolean drawMaxValuesFirst, int howMany, boolean line, boolean highestValueBeUsedAsMax,boolean randomOrder)
    {
        //SI no hay nada, pues no hacemos nada.
        if(d.getTotal()==0)
            return "";
        
        //Revisamos que la cantidad de elementos que nos pide el usuario este dentro del rango. 0 genera que se impriman todos.
        if(howMany> d.getTotal() || howMany<1)
            howMany = d.getTotal();
        
        
        //Altura de la "grafica"
        final int height = 256;
        //Anchura de cada barra.
        final int width = 40;
        
        //DONDE EStara el nombre del Histograma.
        final int head = 40;
        //Espacio a los costados de la grafica.
        final int lap = 50;
        //Inspace espacio entre barras.
        int inSpace = 10;
        if(line)
            inSpace = 0;
        
        String mS = "";
        
        //Valor maximo de la grafica.
        int valorDeGrafica = apariciones;
        int maxValueInside = 0;
        
        
        //Lista donde estaran los valores de la futura grafica.
        Lista<Contador<T>> ordenados;
        
        //Si no queremos orden
        if(randomOrder)
        {
            ordenados = d.valores();
            maxValueInside = maxValueFromListContador(ordenados);
            valorDeGrafica = maxValueInside;
            ordenados = purgeLowValues(ordenados,maxValueInside);
        }
        else
            ordenados = getOrderedValues(this.d.valores(), drawMaxValuesFirst);
        
        //Verificamos la cuenta correspondiente al tamano.
        if(howMany> ordenados.getLongitud() || howMany<1)
            howMany = ordenados.getLongitud();
        
        
        //Tamao de anchura del cuadrado que contienee las barras.
        int innerRectangleWidth = (width*howMany)+(inSpace*howMany) + inSpace;
        mS+= svgSize(height+width+head,innerRectangleWidth+lap);
        
        //Si vamos a usar a los elementos en orden descreciente
        if(highestValueBeUsedAsMax)
        {
            maxValueInside = maxValueFromListContador(ordenados);
            valorDeGrafica = maxValueInside;
        }

        
        //Coloreamos el rectangulo que contiene a la grafica.
        mS += rectTag("histogramaBarsContainer",lap, head, innerRectangleWidth, height);
//                      ,"gray","black",2, 0.1,1.0);
        //Espacio donde esta escrito el nombre del histograma.
        mS += valueTag("black", lap + (innerRectangleWidth/2), head/2 , nombre, 25);
        
        //PINTAR LINEAS.
        //Calcular la lineas horizontales.
        //Que sean potencias de 2.
        int cantidadDeSegmentos = 8;
        int segmentos = height/cantidadDeSegmentos;
        for(int s = 0; s<=cantidadDeSegmentos; s++)
        {
        
            //Caluculamos posisciones y valores.
            int alturaDeSegmentoActual = (segmentos*s);
            int inicialDelSegmento = height - alturaDeSegmentoActual + head;
            double cantidadDeLaBarraParalelaHorizontal = getCantidad(alturaDeSegmentoActual,height, valorDeGrafica);
            
            //SI la linea actual es par le pintamos su valor.
            if(esPar(s))
            {
                mS += lineTag(lap-5,inicialDelSegmento,innerRectangleWidth+lap,inicialDelSegmento);
                mS += valueTag("black", lap/2 +10 , inicialDelSegmento+4,String.valueOf( cantidadDeLaBarraParalelaHorizontal), 13);
            }
            
            else
                mS += lineTag(lap,inicialDelSegmento,innerRectangleWidth+lap,inicialDelSegmento);
            
        }
        
        
        //Iterador para generar los puntos "desconocidos" en la grafica de lineas.
        Iterator<Histograma<T>.Contador<T>> iter = null;
        if(line)
        {
            iter = ordenados.iterator();
            iter.next();
        }
    
        
        //PINTAR todos los valores que se requieren.
        int i = 0;
        double tiempoAcumulado = 0;
        double tiempoDiferencial = 0.05;
        double avanzador = inSpace;
        for(Contador<T> c : ordenados)
        {
            //Si ya acabamos de pintar la cantidad de valores que nos pidio el usuario salimos.
            if(i==howMany)
                break;
            
            //Calculos necesarios de posicion y valor.
            double altura = getAltura(c.cantidad, height, valorDeGrafica);
            double inicial = height - altura + head;
            
            
            //COLOR irrelevante pero nos da otro tono cuando esta en deorden la grafica.
            String color = "green";
            if(randomOrder)
                color = "blue";
            
            //Si este histograma va a ser representado con lineas o barras
            if(line)
            {

                
                //De ser lienas, buscamos el siguiente lugar a partir del iterador que habimos convocado anteroirmente.
                if(iter.hasNext())
                {
                    Contador<T> conta = iter.next();
                    double altura2 = getAltura(conta.cantidad,height, valorDeGrafica);
                    double inicial2 = height - altura2 + head;
                    mS+= lineTag("histogramaLine",lap+avanzador+(width/2), inicial,lap+avanzador+width+(width/2),inicial2, height+head, tiempoAcumulado, tiempoDiferencial);

                }
                    mS+= cirlceTag("histogramaDot",lap+avanzador+(width/2), inicial, 6,height+head, tiempoAcumulado);
                
                tiempoAcumulado+=tiempoDiferencial;
            }
            
            //De ser grafica de barra creamos rectangulos.
            else
            {
                mS += rectTag("histogramaBar",avanzador+lap, inicial, width, altura);
            }
            
            
            
            //Independiente del tipo de grafica, en la parte inferior de la barra impimimos el nombre del elemento que se esta cuantificando
            mS += valueTag("black", avanzador+(width/2)+lap,height+15+head, c.elemento.toString(),15);
            
            
            //El avanzador indica la posicion en el eje x del actual valor, y en cada vuelta del for se aumenta en las constantes.
            avanzador+=width+inSpace;
            
            //Sera el contador para saber si ya tenemos la cantidad de de elementos impresos que el usuario desea.
            i++;
        }
        
        mS+="\n</g>\n</svg>\n</div>";
        return mS;
    }
    
    
    //Recibe una lista tipo contador que quita los elementos que tienen una valor menor del 20 prociento
    private Lista<Contador<T>> purgeLowValues(Lista<Contador<T>> l, int maxValue )
    {
        Lista<Contador<T>> lC = new Lista<Contador<T>>();
        
        for(Contador<T> c: l)
        {
            if(((c.cantidad*100)/maxValue) > 20)
                lC.agregaFinal(c);
        }
        
        return lC;
    }

    
    //Calcula el valor maximo en una lista de contadores.
    private int maxValueFromListContador(Lista<Contador<T>> l )
    {
        if(l.getLongitud()==0)
            return 0;
        int max = 0;
        for(Contador<T> c:l)
        {
            if(c.cantidad>max)
                max=c.cantidad;
        }
        return max;
    }
    private double getAltura(int cantidad, int height, int valorDeGrafica)
    {
        double porcentaje =  ((double)cantidad * 100.0)/(double)valorDeGrafica;
        return (porcentaje * (double)height) /100.0;
        
    }
    
    private double getCantidad(int altura, int height, int valorDeGrafica)
    {
        double porcentaje = (100.0* (double)altura)/height;
        return ((porcentaje*(double)valorDeGrafica)/100.0);
    }
    public String toScalableVectorGraphicsPieChart()
    {
        
        //Total nos da la suma de todas las apariciones.
        int total  = apariciones;
        
        //Lista donde estan los elementos ordenados para trabajar la impresions directamente.
        Lista<Contador<T>> ordenados = getOrderedValues(this.d.valores(), true);

        //Espacio alrededor del circulo para lo que quieras.
        int espacioPalabras = 50;
        //Epscacio en la parte superior del circulo para el nombre del documento.
        int head = 50;
        //El radio del circulo
        int radio = 140;
        //El radio intero es donde estara la variable con el procentaje y el elemento visible.
        int radioInterno = 3*radio/4;
        
        //El la posicion desfasada del eje y
        int posicionY = radio+ head;
        //Es la posisionc desfasada del eje x
        int posicionX = radio+ espacioPalabras;
        //Vamos a usa random para colorear nuestras rebanadas del pastel.
        Random r = new Random();
        

        //Calculamos las dimensiones del svg.
        String mS = svgSize(radio*2 + head + 10 ,radio*2 + (espacioPalabras*2) );
        
        //POnemos un circulo por pura manera que se vea mejor.
//        mS+=cirlceTag(posicionX,posicionY,radio+1,rgb(0,0,0));
                mS+=cirlceTag("histogramaArcContainer",posicionX,posicionY,radio+1);
        //Angulo acumuloado indica la suma de aquellas rebanadas que eran mas pequeñas que el minimoGradoAcumulable de grados.
        double acumulatedAngles = 0.0;
        //Define el minimo grado acumulable
        double minimioGradoAcumulable = 20.0;
        //Ir llevando la cuenta de los angulos incciales y finales.
        double  startAngle = 0 ;
        double  endAngle = 0;
        //Genera un caso unnico de if, para la primera linea que se pintara no se sobreponga.

        String misLineas = "";
        String misDatos = "";
        for(Contador<T> c : ordenados)
        {
    
            //Calculamos en angulo correspondiente a la cantidad
            double currentAngle =(360.0 * c.cantidad /total);

            //AQUI REVISAMOS SI EN PEDASO DEL CIRCULO ES MUY PEQUEño, si es asi, lo ignoramos y acumulamos en angulo.
            if(currentAngle<=minimioGradoAcumulable)
            {
                acumulatedAngles+=currentAngle;
                continue;
            }
            
            //Calculamos el angulo medio de la rebanda para colocar en la rebanada el porcentaje y elemento.
            double middleAngle = startAngle + currentAngle/2;
            
        
            //Calculamos las posiciones correspondientes del arco.
            endAngle = startAngle + currentAngle;
            
            double x1 = posicionX + radio*Math.cos(Math.toRadians(startAngle));
            double y1 = posicionY + radio*Math.sin(Math.toRadians(startAngle));
            
            double x2 = posicionX + radio*Math.cos(Math.toRadians(endAngle));
            double y2 = posicionY + radio*Math.sin(Math.toRadians(endAngle));
            

            //PINTAMOS con las posiciones ya definidas.
            
            String path = "";
            if(c.cantidad == total){
                    mS+=cirlceTag(posicionX,posicionY,radio,rgbRandom(r));
            }
            else
            {
                
                //Tenemos este if, que cambia el valor de lowe de 0 a 1, devido a la forma en que trabaja path de svg.
                int lowe = 0;
                if(c.cantidad>=((double)total/2.0))
                    lowe = 1;
                
                //Creamos el path, o arco con todas sus caracteristicas.
                path = ("M"+posicionX+","+posicionY+"  L" + x1 + "," + y1 + "  A"+radio+","+radio+" " + 0 +" " + lowe+","+ 1+" "+ x2 + "," + y2 + " z");
            
                //Agreganmos el arco y definimos su color.
                mS+=pathTag(path, rgbRandom(r));
                //Separamos las lineas para que los arcos no esten "arriba" de ellas.
                misLineas += lineTagStroke(posicionX,posicionY,x2,y2,2);
            }
            
            
            //Calculamos el valor a medida del anguloMedio.
            double x3 = posicionX + radioInterno*Math.cos(Math.toRadians(middleAngle));
            double y3 = posicionY + radioInterno*Math.sin(Math.toRadians(middleAngle));
            
            //Pintamos el elemento y su respectivo porcentaje
            
            double porcentaje = currentAngle/3.6;
            if(porcentaje == 100)
                x3 = posicionX;
            
            misDatos+=valueTag("white",x3,y3,c.elemento.toString(),20);
            misDatos+=valueTag("white",x3,y3+20,String.format("%.0f%%",porcentaje),20);
            
            startAngle = endAngle;
        }
        //VAMOS A PINTAR EL CACHO DE ANGULO ACUMULADO EN LLAMADO -ELSE-
        // SE PUEDE FACTORIZAR EL METODO PERO tengo huevita.
        if(acumulatedAngles!=0.0)
        {

            
            endAngle = startAngle + acumulatedAngles;
            double middleAngle = startAngle + acumulatedAngles/2;
            double x1 = posicionX + radio*Math.cos(Math.toRadians(startAngle));
            double y1 = posicionY + radio*Math.sin(Math.toRadians(startAngle));
            
            double x2 = posicionX + radio*Math.cos(Math.toRadians(endAngle));
            double y2 = posicionY + radio*Math.sin(Math.toRadians(endAngle));
            
            
            
            double inversaDeAngulo = acumulatedAngles*total/360.0;
            String path = "";
                int lowe = 0;
                if(inversaDeAngulo>=((double)total/2.0))
                    lowe = 1;
                path = ("M"+posicionX+","+posicionY+"  L" + x1 + "," + y1 + "  A"+radio+","+radio+" " + 0 +" " + lowe+","+ 1+" "+ x2 + "," + y2 + " z");
                
                mS+=pathTag(path, rgbRedRandom(r));
            
            
            

            misLineas += lineTagStroke(posicionX,posicionY,x2,y2,2);
            
            
            //Calculamos el valor a medida del anguloMedio.
            double x3 = posicionX + radioInterno*Math.cos(Math.toRadians(middleAngle));
            double y3 = posicionY + radioInterno*Math.sin(Math.toRadians(middleAngle));
            
            
            //Pintamos el elemento y su respectivo porcentaje
            misDatos+=valueTag("white",x3,y3,"Else",20);
            double porcentaje = acumulatedAngles/3.6;
            misDatos+=valueTag("white",x3,y3+20,String.format("%.0f%%",porcentaje),20);

            
        }
        mS+=misLineas;
        mS+=misDatos;
        mS+=valueTag("black",posicionX, head/2  ,nombre,25);
        mS+="\n</g>\n</svg>\n</div>";

        
        return  mS;
    }

    
    private String rectTag(String clase, double x, double y, double w, double h)
    {
        return String.format("<rect class=\"%s\" x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" /> \n",clase,x,y,w,h);
    }
    
    private String rgbRedRandom(Random r)
    {
        return rgb(r.nextInt(255),0,0);
    }
    

    private String rgbRandom(Random r)
    {
        return rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255));
    }
    
    private String rgb(int r, int g, int b)
    {
        return String.format("rgb(%d,%d,%d) ",r,g,b);
    }
    
    private String pathTag(String holdPath, String color)
    {
        
        return String.format("<path d=\"%s\" fill=%s />\n",holdPath, color);
    }
    private String textTag(String s, int size, boolean l)
    {
        String lock = "unlocked";
        if(l)
            lock = "locked";
        return String.format("<p>%s is %s. Left: %d </p>",s,lock,size);
    }
    private String valueTag( String fill , double  x , double y, String s, int size)
    {
        return String.format("<text fill='%s' font-family='sans-serif' font-size='%d' x='%.2f' y='%.2f' text-anchor='middle'>%s</text>\n",fill,size ,x,y,s);
        
    }
    private String lineTag(double x1, double y1, double x2, double y2)
    {
        
        return String.format("<line x1='%.2f' y1='%.2f' x2='%.2f' y2='%.2f' stroke='black' stroke-width='0.2' />\n",x1,y1,x2,y2 );
    }
    
    
    
    private String lineTag(String clase, double x1, double y1, double x2, double y2, double from, double duration , double timeLapse)
    {
        return String.format("<line class=\"%s\" x1='%.2f' y1='%.2f' x2='%.2f' y2='%.2f' stroke='black' stroke-width='0.2' >\n"+
                             
                             "<set attributeName=\"y1\" attributeType=\"XML\""+
                             "to=\"%.2f\""+
                             "begin=\"%.2fs\"  />\n"+
                             "<set attributeName=\"y2\" attributeType=\"XML\""+
                             "to=\"%.2f\""+
                             "begin=\"%.2fs\"  />\n"+
                             
                             "<animate attributeName=\"y1\" attributeType=\"XML\""+
                             "from=\"%.2f\"  to=\"%.2f\""+
                             "begin=\"%.2fs\" dur=\"1s\""+
                             "/>\n"+
                             "<animate attributeName=\"y2\" attributeType=\"XML\""+
                             "from=\"%.2f\"  to=\"%.2f\""+
                             "begin=\"%.2fs\" dur=\"1s\""+
                             "/>\n"+
                             
                             "</line>\n",clase,x1,from,x2,from, y1,duration,y2,duration+timeLapse, from,y1,duration, from,y2,duration+timeLapse);
    }
    
    private String lineTagStroke(double x1, double y1, double x2, double y2, double stroke)
    {
        
        return String.format("<line x1='%.2f' y1='%.2f' x2='%.2f' y2='%.2f' stroke='black' stroke-width='%.2f' />\n",x1,y1,x2,y2,stroke );
    }
    private String svgSize(double height , double width)
    {
        return String.format("\n<div class=\"svgContainer\"> \n<svg width='%.2f' height='%.2f'>\n<g>\n",width,height);
    }
    
    private String cirlceTag(double x, double y, double r, String fill)
    {
        
        return String.format("<circle cx='%.2f' cy='%.2f' r='%.2f' stroke='black' stroke-width='2' fill='%s' />\n ",x,y,r,fill);
    }
    
    
    private String cirlceTag(String clase, double x, double y, double r)
    {
        
        return String.format("<circle class=\"%s\"  cx='%.2f' cy='%.2f' r='%.2f' />\n ",clase,x,y,r);
    }
    
    
    private String cirlceTag(String clase, double x, double y, double r, double from, double duration)
    {

        return String.format("<circle class=\"%s\"  cx='%.2f' cy='%.2f' r='%.2f' >\n "+
                             "<set attributeName=\"cy\" attributeType=\"XML\""+
                             "to=\"%.2f\""+
                             "begin=\"%.2fs\"  />\n"+
                             
                             
                             "<animate attributeName=\"cy\" attributeType=\"XML\""+
                             "from=\"%.2f\"  to=\"%.2f\""+
                             
                             "begin=\"%.2fs\" dur=\"1s\""+

                             "/>\n"+
                             "</circle>\n"
                             ,clase,x,from,r, y,duration,  from, y,duration);
    }
    
    
    private boolean esPar(int x) {
        if ((x % 2) == 0)
            return true;
        return false;
    }
    
    @Override public String toString()
    {
        if(!nombre.equals(""))
            return nombre+": "+d.toString();
        return d.toString();
    }

}
