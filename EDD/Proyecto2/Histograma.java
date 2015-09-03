

package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Clase para almacenamiento de histogramas.
 * Operaciones:
 * generaSVG grafica de barras, Int Mayores || Int menores.
 * generaSVG grafica de pastel **
 * Puede ver si se esta relacionado con otro Histograma para ver si tienen mismos T's.
 * Se puede agregar histogramas tipo T.
 * Se agregan elementos al histograma, como Tipo T y la cantidad de veces que sale.
 * Se eliminan tipos T.
 * Metodo estatico para evaluacion lineal de documentos.
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
    /*Se usara en la impresiodel svg a diferencia de aparciciones si el usuario lo require*/
    
    private int maxValue;
    /*Diccionario donde estaran los elementos cuantificados*/
    private Diccionario<T, Contador<T> > d;

    Histograma()
    {
        this("");
    }
    Histograma(String name)
    {
        apariciones = 0;
        nombre = name;
        d =  new Diccionario<T, Contador<T>>();
        maxValue = 0;
    }
    
    

    public void agrega(Histograma<T> h )
    {
        for(T t : h)
        {
            Contador<T> c = h.d.get(t);
            
            apariciones+=c.cantidad;
            
            if(this.contiene(t))
            {
                Contador<T> cT = this.d.get(t);
                cT.cantidad+=c.cantidad;
            }
            else
                d.agrega(t, c );
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
    
    //How many DEFAULT ALL.

    private Lista<Contador<T>> getOrderedValues(Lista<Contador<T>> ordered, boolean maxValuesFirst, boolean highestValueBeUsedAsMax )
    {
        ordered = Lista.mergeSort(ordered);
        
        
        if(highestValueBeUsedAsMax && ordered.getLongitud()>0)
        {
            Contador<T> c = ordered.getUltimo();
            maxValue = c.cantidad;

        }
        
        if(maxValuesFirst)
        {
            ordered =ordered.reversa();
        
            
        }
        
        
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
    
    public String toScalableVectorGraphicsBars(boolean drawMaxValuesFirst, int howMany, boolean line, boolean highestValueBeUsedAsMax)
    {
        if(d.getTotal()==0)
            return "";
        
        if(howMany> d.getTotal() || howMany<1)
            howMany = d.getTotal();
        
        
        //Altura de la "grafica"
        final int height = 200;
        //Anchura de cada barra.
        final int width = 40;
        
        //DONDE EStara el nombre del Histograma.
        final int head = 30;
        //Espacio donde estaran las lienas punteadas al lado izquierdo.
        final int lap = 50;
        //Inspace espacio entre barras.
        int inSpace = 10;
        if(line)
            inSpace = 0;
        
        String mS = "";
        
        //Valor maximo de la grafica.
        int valorDeGrafica = apariciones;
        
        
        
        //Tamao de anchura del cuadrado que contienee las barras.
        int innerRectangleWidth = (width*howMany)+(inSpace*howMany) + inSpace;
        mS+= svgSize(height+width+head,innerRectangleWidth+lap);
        
        
        //Lista donde estaran los valores de la futura grafica.
        Lista<Contador<T>> ordenados = getOrderedValues(this.d.valores(), drawMaxValuesFirst, highestValueBeUsedAsMax);
        if(highestValueBeUsedAsMax)
            valorDeGrafica = maxValue;

        
        //Coloreamos el rectangulo que contiene a la grafica.
        mS += rectTag(lap, head, innerRectangleWidth, height,"gray","black",2, 0.1,1.0);
        //Espacio donde esta escrito el nombre del histograma.
        mS += valueTag("black", lap + (innerRectangleWidth/2), head/2 , nombre, 15);
        
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
            {
            mS += lineTag(lap,inicialDelSegmento,innerRectangleWidth+lap,inicialDelSegmento);
            }
            
        }
        
        
        //Iterador para generar los puntos "desconocidos" en la grafica de lienas.
        Iterator<Histograma<T>.Contador<T>> iter = null;
        if(line)
        {
            iter = ordenados.iterator();
            iter.next();
        }
    
        
        //PINTAR todos los valores que se requieren.
        int i = 0;
        double avanzador = inSpace;
        for(Contador<T> c : ordenados)
        {
            //Si ya acabamos de pintar la cantidad de valores que nos pidio el usuario salimos.
            if(i==howMany)
                break;
            
            //Calculos necesarios de posicion y valor.
            double altura = getAltura(c.cantidad, height, valorDeGrafica);
            double inicial = height - altura + head;
            
            String color = "green";
            if(highestValueBeUsedAsMax)
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
                    
                    mS+= lineTagStroke(lap+avanzador+(width/2), inicial,lap+avanzador+width+(width/2),inicial2, 2);

                }
                mS+= cirlceTag(lap+avanzador+(width/2), inicial, 6, color);
            }
            
            //De ser grafica de barra creamos rectangulos.
            else
            {
                mS += rectTag(avanzador+lap, inicial, width, altura,color, "black",2, 0.7,1.0);
            }
            
            
            
            //Independiente del tipo de grafica, en la parte inferior de la barra impimimos el nombre del elemento que se esta cuantificando
            mS += valueTag("black", avanzador+(width/2)+lap,height+15+head, c.elemento.toString(),15);
            
            
            //El avanzador indica la posicion en el eje x del actual valor, y en cada vuelta del for se aumenta en las constantes.
            avanzador+=width+inSpace;
            
            //Sera el contador para saber si ya tenemos la cantidad de de elementos impresos que el usuario desea.
            i++;
        }
        
        mS+="\n</g>\n</svg>\n";
        return mS;
    }
//
//    /**
//     * Genera un string con la representacion de una grafica del histograma en formato svg.
//     *
//     * @param drawMaxValuesFirst boolean que nos permite decidir si pintamos los valores maximo al principio.
//     * @param howMany Numero barras que se va a imprimir.
//     * @param line Define si va a ser grafica estilo lineas o barras.
//     * @param highestValueBeUsedAsMax Si vamos a usar la sumatoria total de aparciciones de o el valor maixmo como el tope de la grafica.
//     * @return String que contiene la representacion grafica.
//     *
//     */
//    
//
//    public static <T extends Comparable<T>> String toScalableVectorGraphicsBars(Histograma<T> h ,boolean graphElementsInsteadOfValues ,boolean drawMaxValuesFirst, int howMany, boolean line, boolean highestValueBeUsedAsMax)
//    {
//        
//        if(h.getTotal()==0)
//            return "";
//        
//        if(howMany> h.getTotal() || howMany<1)
//            howMany = h.getTotal();
//        
//        
//        //Altura de la "grafica"
//        final int height = 200;
//        //Anchura de cada barra.
//        final int width = 40;
//        
//        //DONDE EStara el nombre del Histograma.
//        final int head = 30;
//        //Espacio donde estaran las lienas punteadas al lado izquierdo.
//        final int lap = 50;
//        //Inspace espacio entre barras.
//        int inSpace = 10;
//        if(line)
//            inSpace = 0;
//        
//        String mS = "";
//        
//        //Valor maximo de la grafica.
//        int valorDeGrafica = h.getTotalApariciones();
//        
//        
//        
//        //Tamao de anchura del cuadrado que contienee las barras.
//        int innerRectangleWidth = (width*howMany)+(inSpace*howMany) + inSpace;
//        mS+= svgSize(height+width+head,innerRectangleWidth+lap);
//        
//        
//        //Lista donde estaran los valores de la futura grafica.
//        Lista<Contador<T>> listaDeElementos=null;
//        if(graphElementsInsteadOfValues)
//            listaDeElementos= getOrderedElements(h.d.llaves());
//        else
//            listaDeElementos = h.d.valores();
//            
//        Lista<Contador<T>> ordenados = createGraphicalOrder(listaDeElementos, drawMaxValuesFirst);
//        
//        if(highestValueBeUsedAsMax)
//            valorDeGrafica = maxValue(ordenados);
//        
//        
//        
//        //Coloreamos el rectangulo que contiene a la grafica.
//        mS += rectTag(lap, head, innerRectangleWidth, height,"gray","black",2, 0.1,1.0);
//        //Espacio donde esta escrito el nombre del histograma.
//        mS += valueTag("black", lap + (innerRectangleWidth/2), head/2 , nombre, 15);
//        
//        //PINTAR LINEAS.
//        //Calcular la lineas horizontales.
//        //Que sean potencias de 2.
//        int cantidadDeSegmentos = 8;
//        int segmentos = height/cantidadDeSegmentos;
//        for(int s = 0; s<=cantidadDeSegmentos; s++)
//        {
//            
//            //Caluculamos posisciones y valores.
//            int alturaDeSegmentoActual = (segmentos*s);
//            int inicialDelSegmento = height - alturaDeSegmentoActual + head;
//            double cantidadDeLaBarraParalelaHorizontal = getCantidad(alturaDeSegmentoActual,height, valorDeGrafica);
//            
//            //SI la linea actual es par le pintamos su valor.
//            if(esPar(s))
//            {
//                mS += lineTag(lap-5,inicialDelSegmento,innerRectangleWidth+lap,inicialDelSegmento);
//                mS += valueTag("black", lap/2 +10 , inicialDelSegmento+4,String.valueOf( cantidadDeLaBarraParalelaHorizontal), 13);
//            }
//            
//            else
//            {
//                mS += lineTag(lap,inicialDelSegmento,innerRectangleWidth+lap,inicialDelSegmento);
//            }
//            
//        }
//        
//        
//        //Iterador para generar los puntos "desconocidos" en la grafica de lienas.
//        Iterator<Histograma<T>.Contador<T>> iter = null;
//        if(line)
//        {
//            iter = ordenados.iterator();
//            iter.next();
//        }
//        
//        
//        //PINTAR todos los valores que se requieren.
//        int i = 0;
//        double avanzador = inSpace;
//        for(Contador<T> c : ordenados)
//        {
//            //Si ya acabamos de pintar la cantidad de valores que nos pidio el usuario salimos.
//            if(i==howMany)
//                break;
//            
//            //Calculos necesarios de posicion y valor.
//            double altura = getAltura(c.cantidad, height, valorDeGrafica);
//            double inicial = height - altura + head;
//            
//            String color = "green";
//            if(highestValueBeUsedAsMax)
//                color = "blue";
//            
//            //Si este histograma va a ser representado con lineas o barras
//            if(line)
//            {
//                
//                //De ser lienas, buscamos el siguiente lugar a partir del iterador que habimos convocado anteroirmente.
//                
//                if(iter.hasNext())
//                {
//                    Contador<T> conta = iter.next();
//                    double altura2 = getAltura(conta.cantidad,height, valorDeGrafica);
//                    double inicial2 = height - altura2 + head;
//                    
//                    mS+= lineTagStroke(lap+avanzador+(width/2), inicial,lap+avanzador+width+(width/2),inicial2, 2);
//                    
//                }
//                mS+= cirlceTag(lap+avanzador+(width/2), inicial, 6, color);
//            }
//            
//            //De ser grafica de barra creamos rectangulos.
//            else
//            {
//                mS += rectTag(avanzador+lap, inicial, width, altura,color, "black",2, 0.7,1.0);
//            }
//            
//            
//            
//            //Independiente del tipo de grafica, en la parte inferior de la barra impimimos el nombre del elemento que se esta cuantificando
//            mS += valueTag("black", avanzador+(width/2)+lap,height+15+head, c.elemento.toString(),15);
//            
//            
//            //El avanzador indica la posicion en el eje x del actual valor, y en cada vuelta del for se aumenta en las constantes.
//            avanzador+=width+inSpace;
//            
//            //Sera el contador para saber si ya tenemos la cantidad de de elementos impresos que el usuario desea.
//            i++;
//        }
//        
//        mS+="\n</g>\n</svg>\n";
//        return mS;
//        
//    }
//    
//    
////    Lista<Contador<T>> ordenados = getOrderedValues(drawMaxValuesFirst, highestValueBeUsedAsMax);
////    if(highestValueBeUsedAsMax)
////        valorDeGrafica = maxValue;
//    
//        
//    public static Lista<Contador<T>> getOrderedValues( Lista<Contador<T>> lista, boolean drawMaxValuesFirst)
//    {
//        
//        return null;
//        
//    }
//    public static <T extends Comparable<T>> Lista<Contador<T>> getOrderedElements(Lista<T> l ){
//        
//        return null;
//    }
//    public static int maxValue(Lista<Contador<T>> n)
//    {
//        return 0 ;
//    }
    
    
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
    private String rectTag(double x, double y, double w, double h, String fill, String stroke, double sW,  double fA,double sA )
    {
        return String.format("<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" style=\"fill:%s ;stroke:%s; stroke-width:%.2f;fill-opacity:%.2f;stroke-opacity:%.2f\" /> \n",x,y,w,h,fill, stroke,sW, fA,sA);
    }
    public String toScalableVectorGraphicsPieChart()
    {
        int total  = apariciones;
        
        Lista<Contador<T>> ordenados = getOrderedValues(this.d.valores(), true, false);

        


        //Espacio alrededor del circulo para lo que quieras.
        int espacioPalabras = 50;
        //Epscacio en la parte superior del circulo para el nombre del documento.
        int head = 100;
        //El noseque es el radio del circulo
        int noseque = 300;
        //El la posicion desfasada del eje y
        int posicionY = noseque+ head;
        //Es la posisionc desfasada del eje x
        int posicionX = noseque+ espacioPalabras;
        //Vamos a usa random para colorear nuestras rebanadas del pastel.
        Random r = new Random();
        

        //Calculamos las dimensiones del svg.
        String mS = svgSize(noseque*2 + head ,noseque*2 + (espacioPalabras*2) );
        
        //POnemos un circulo por pura manera que se vea mejor.
        mS+=cirlceTag(posicionX,posicionY,noseque+1,rgb(0,0,0));
        
        //Angulo acumuloado indica la suma de aquellas rebanadas que eran mas pequeñas que el minimoGradoAcumulable grados.
        double acumulatedAngles = 0.0;
        //Define el minimo grado acumulable
        double minimioGradoAcumulable = 10.0;
        //Ir llevando la cuenta de los angulos incciales y finales.
        double  startAngle = 0 ;
        double  endAngle = 0;
        for(Contador<T> c : ordenados)
        {
            //
            startAngle = endAngle;
            double currentAngle =(360.0 * c.cantidad /total);

            //AQUI REVISAMOS SI EN PEDASO DEL CIRCULO ES MUY PEQUEño, si es asi, lo ignoramos y acumulamos en angulo.
            if(currentAngle<=10.0)
            {
                acumulatedAngles+=currentAngle;
                continue;
            }
            
            //Calculamos el angulo medio de la rebanda para colocar en la rebanada el porcentaje y elemento.
            double middleAngle = startAngle + currentAngle/2;
            
        
            //Calculamos las posiciones correspondientes del arco.
            endAngle = startAngle + currentAngle;
            
            double x1 = posicionX + noseque*Math.cos(Math.toRadians(startAngle));
            double y1 = posicionY + noseque*Math.sin(Math.toRadians(startAngle));
            
            double x2 = posicionX + noseque*Math.cos(Math.toRadians(endAngle));
            double y2 = posicionY + noseque*Math.sin(Math.toRadians(endAngle));
            

            //PINTAMOS con las posiciones ya definidas.
            
            String path = "";
            if(c.cantidad == total)
                mS+=cirlceTag(posicionX,posicionY,noseque,rgbRandom(r));
            else
            {
                int lowe = 0;
                if(c.cantidad>=((double)total/2.0))
                    lowe = 1;
                path = ("M"+posicionX+","+posicionY+"  L" + x1 + "," + y1 + "  A"+noseque+","+noseque+" " + 0 +" " + lowe+","+ 1+" "+ x2 + "," + y2 + " z");
            
                mS+=pathTag(path, rgbRandom(r));
                
                mS += lineTagStroke(posicionX,posicionY,x2,y2,5);
            }
            
            
            //Calculamos el valor a medida del anguloMedio.
            double x3 = posicionX + noseque*Math.cos(Math.toRadians(middleAngle));
            double y3 = posicionY + noseque*Math.sin(Math.toRadians(middleAngle));
            
            //Pintamos
            mS+=valueTag("black",x3,y3,c.elemento.toString(),30);
            
            startAngle = endAngle;
        }
        //VAMOS A PINTAR EL CACHO DE ANGULO ACUMULADO EN LLAMADO -ELSE-
        //SI SE PUEDE FACTORIZAR EL METODO PERO tengo huevita.
        if(acumulatedAngles!=0.0)
        {

            
            endAngle = startAngle + acumulatedAngles;
            
            double x1 = posicionX + noseque*Math.cos(Math.toRadians(startAngle));
            double y1 = posicionY + noseque*Math.sin(Math.toRadians(startAngle));
            
            double x2 = posicionX + noseque*Math.cos(Math.toRadians(endAngle));
            double y2 = posicionY + noseque*Math.sin(Math.toRadians(endAngle));
            
            
            
            String path = "";
                int lowe = 0;
                if(acumulatedAngles>=((double)total/2.0))
                    lowe = 1;
                path = ("M"+posicionX+","+posicionY+"  L" + x1 + "," + y1 + "  A"+noseque+","+noseque+" " + 0 +" " + lowe+","+ 1+" "+ x2 + "," + y2 + " z");
                
                mS+=pathTag(path, rgbRedRandom(r));
            
            mS += lineTagStroke(posicionX,posicionY,x2,y2,5);

            
        }
        
        mS+=valueTag("black",posicionX, head/2 + 5 ,nombre,20);
        mS+="\n</g>\n</svg>\n";

        
        return  mS;
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
    private String lineTagStroke(double x1, double y1, double x2, double y2, double stroke)
    {
        
        return String.format("<line x1='%.2f' y1='%.2f' x2='%.2f' y2='%.2f' stroke='black' stroke-width='%.2f' />\n",x1,y1,x2,y2,stroke );
    }
    private String svgSize(double height , double width)
    {
        return String.format("\n\n<svg width='%.2f' height='%.2f'>\n<g>\n",width,height);
    }
    
    private String cirlceTag(double x, double y, double r, String fill)
    {
        
        return String.format("<circle cx='%.2f' cy='%.2f' r='%.2f' stroke='black' stroke-width='2' fill='%s' />\n ",x,y,r,fill);
    }
    private boolean esPar(int x) {
        if ((x % 2) == 0)
            return true;
        return false;
    }
    
    @Override public String toString()
    {
        return d.toString();
    }

}
