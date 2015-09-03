package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las
 * siguientes propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la
 *      raíz).
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguno de sus descendientes tiene
 *      el mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros son autobalanceados, y por lo tanto las
 * operaciones de inserción, eliminación y búsqueda pueden
 * realizarse en <i>O</i>(log <i>n</i>).
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método
     * {@link ArbolBinarioOrdenado#agrega}, y después balancea el
     * árbol recoloreando vértices y girando el árbol como sea
     * necesario.
     * @param elemento el elemento a agregar.
     * @return un vértice que contiene al nuevo elemento.
     */
    @Override public VerticeArbolBinario<T> agrega(T elemento) {
        
        VerticeArbolBinario<T> vM= super.agrega(elemento);
        ArbolBinario<T>.Vertice<T> vv= vertice(vM);
        vv.color = Color.ROJO;
        rebalancear(vv);
        return vv;
     
    }
    /*Recibe como parametro un vertice rojo
     rebalancea el arbol*/
        
    private void rebalancear(ArbolBinario<T>.Vertice<T> v)
    {

        //ES RAIZ
        if(!v.hayPadre())
        {
            v.cambiaColor();
            return;
        }
        //EL PADRE ES NEGRO
        if(esNegro(v.padre))
            return;
        
        //TIO ROJO?
        if(tioEsRojo(v))
        {
            cambiaALosViejosDeColor(v);
            rebalancear(v.padre.padre);
            return;
            
        }
        //V y PADRE ESTAN CRUZADOS?
        if(padreCruzadoCon(v))
        {
                if(esIzqDePadre(v))
                {
                    giraDerecha(v.padre);
                    if(v.hayDerecho())
                        v= v.derecho;
                }
                else
                {
                    giraIzquierda(v.padre);
                    if(v.hayIzquierdo())
                        v=v.izquierdo;
                }
        }
        //Contrario V Y P NO cruzados.
        if (!padreCruzadoCon(v))
        {
            v.padre.cambiaColor();
            v.padre.padre.cambiaColor();
            
            if (esIzqDePadre(v))
                giraDerecha(v.padre.padre);
        
            else
                giraIzquierda(v.padre.padre);
        }
    }
        
        /** vertice tiene linea recta con su respectivo padre y abuelo?*/
         
    private boolean padreLineaRectaCon(ArbolBinario<T>.Vertice<T> v)
    {
        boolean lineaRecta = false;
        if(esIzqDePadre(v) && esIzqDePadre(v.padre))
            lineaRecta = true;
        if(!esIzqDePadre(v) && !esIzqDePadre(v.padre))
            lineaRecta = true;
        return lineaRecta;
    }
    private boolean padreCruzadoCon(ArbolBinario<T>.Vertice<T> v)
    {
        boolean estanCruzados = false;
        
        if(esIzqDePadre(v) && !esIzqDePadre(v.padre))
            estanCruzados = true;
        if(!esIzqDePadre(v) && esIzqDePadre(v.padre))
            estanCruzados = true;
        
        return estanCruzados;
    
    }
    private boolean esIzqDePadre(ArbolBinario<T>.Vertice<T> v)
    {
        boolean esIzq = false;
        if(v.hayPadre())
            if(v.padre.hayIzquierdo())
                if(v.padre.izquierdo == v)
                    esIzq = true;
        return esIzq;
        
    }
    /*** Cambia de color los parientes viejos de mi vertice, tio padre y abuelo.***/
    ///*** y regresa al abuelo.
    private void cambiaALosViejosDeColor(ArbolBinario<T>.Vertice<T> v)
    {
        ArbolBinario<T>.Vertice<T> a,p,t;
        
        p = v.padre;
        a = p.padre;
        
        if(a.hayDerecho() && a.derecho == p)
            t = a.izquierdo;
        else
            t = a.derecho;
        p.cambiaColor();
        a.cambiaColor();
        t.cambiaColor();
        
    }
        
    /*** false si hermano es null, false si hermano es negro, true sii esta y es rojo****/
    private boolean hermanoEsRojo(ArbolBinario<T>.Vertice<T> v)
    {
        boolean isRed = false;
        
        Vertice<T> t;
        if(hayHermano(v))
        {
            t = vertice(getHermano(v));
            if(t.getColor()==Color.ROJO)
                isRed=true;
        }
        
        return isRed;
    }
    private boolean hayHermano(Vertice<T> v)
    {
        boolean broLives = false;
        if(v.hayPadre())
            if(v.padre.hayDerecho() && v.padre.hayIzquierdo())
                broLives = true;
                    
        return broLives;
    }
    private ArbolBinario<T>.Vertice<T> getHermano(Vertice<T> v)
    {
            Vertice<T> vB = null;
            if(v.hayPadre())
                if(v.padre.hayDerecho() && v.padre.hayIzquierdo())
                {
                    if(v.padre.derecho == v)
                        vB=v.padre.izquierdo;
                    else
                        vB=v.padre.derecho;
                }
            
            return vB;
    }

    /*** false si tio es null, false si tio es negro, true si esta y es rojo****/
        
    private boolean tioEsRojo(ArbolBinario<T>.Vertice<T> v)
    {
            
            boolean isRed = false;
        
            Vertice<T> t;
            if(hayTio(v))
            {
                t = vertice(getTio(v));
                if(t.getColor()==Color.ROJO)
                    isRed=true;
            }
            
            return isRed;
        }
    //Regresa al tio Preferente revisar si ti tiene con el metodo hayTio()
    private VerticeArbolBinario<T> getTio(Vertice<T> v)
    {
            VerticeArbolBinario<T> uncleLives = null;
            if(esIzqDePadre(v))
            {
                //a
                if(esIzqDePadre(v.padre))
                {
                    if(v.padre.padre.hayDerecho())
                        uncleLives = v.padre.padre.getDerecho();                }
                //b
                else if(!esIzqDePadre(v.padre))
                {
                    
                    if(v.padre.padre.hayIzquierdo())
                        uncleLives = v.padre.padre.getIzquierdo();
                }
            }
            if(!esIzqDePadre(v))
            {
                //c
                if(esIzqDePadre(v.padre))
                {
                    
                    if(v.padre.padre.hayDerecho())
                        uncleLives = v.padre.padre.getDerecho();
                    
                }
                //d
                else if(!esIzqDePadre(v.padre))
                {
                    
                    if(v.padre.padre.hayIzquierdo())
                        uncleLives = v.padre.padre.getIzquierdo();
                }
            }
            return uncleLives;
        }
    /**
         * De cualquier vertice recibido revisa si tiene, padre, si este tiene abuelo, y finalmente si este tiene un hijo, cual es el tio
         */
    private boolean hayTio(Vertice<T> v)
    {
//            Vertice<T> t = null;
            boolean uncleLives = false;
            if(esIzqDePadre(v))
            {
                //a
                if(esIzqDePadre(v.padre))
                {
                    if(v.padre.padre.hayDerecho())
                        uncleLives = true;                }
                //b
                else if(!esIzqDePadre(v.padre))
                {
                    
                    if(v.padre.padre.hayIzquierdo())
                        uncleLives = true;
                }
            }
            if(!esIzqDePadre(v))
            {
                //c
                if(esIzqDePadre(v.padre))
                {
                    
                    if(v.padre.padre.hayDerecho())
                        uncleLives = true;
                    
                }
                //d
                else if(!esIzqDePadre(v.padre))
                {
                    
                    if(v.padre.padre.hayIzquierdo())
                        uncleLives = true;
                }
            }
            return uncleLives;
        }
    //Revisa si el vertice recibido es negro.
    private boolean esNegro(ArbolBinario<T>.Vertice<T> v)
    {
            boolean isBlack = false;
            if(v == null || v.getColor()==Color.NEGRO)
                isBlack = true;
            return isBlack;
        }
//    private  ArbolBinario<T>.Vertice<T> vabAgrega( ArbolBinario<T>.Vertice<T> v, ArbolBinario<T>.Vertice<T> vE)
//    {
//            if (v.elemento.compareTo(vE.elemento)>0)
//            {
//                if(v.hayIzquierdo())
//                    return vabAgrega(v.izquierdo,vE);
//                v.izquierdo = vE;        }
//            else
//            {
//                if (v.hayDerecho())
//                    return vabAgrega(v.derecho,vE);
//                v.derecho = vE;
//            }
//            vE.padre = v;
//            
//            return vE;
//        }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que
     * contiene el elemento, y recolorea y gira el árbol como sea
     * necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
        /**
         * Agrega un nuevo elemento al árbol. El método invoca al método
         * {@link ArbolBinarioOrdenado#agrega}, y después balancea el
         * árbol recoloreando vértices y girando el árbol como sea
         * necesario.
         * @param elemento el elemento a agregar.
         * @return un vértice que contiene al nuevo elemento.
         */
       /**
        * Revisa si el vertice tiene algun hijo. Revisa si existe algun hijo no null.
        * @param Vertice a revisar.
        * @return True si tiene por lo menos un hijo, false si ambos son null.
        */
    private boolean tieneAlgunHijo(VerticeArbolBinario<T> v)
        {
            boolean verticesDecendientesExisten = false;
            if(v.hayIzquierdo() || v.hayDerecho())
                verticesDecendientesExisten = true;
        
            return verticesDecendientesExisten;
        }
        
    private Vertice<T> dameUnHijo(Vertice<T> vB )
    {
     
        Vertice<T> vH = null;
        if(vB.hayIzquierdo())
            vH = vB.izquierdo;
        if(vB.hayDerecho())
            vH = vB.derecho;

        return vH;
        
    }
        
//    private void subir
    @Override public void elimina(T elemento) {
        
//        System.out.printf("\n\n\n\n\n\n ********************************ELIMINAR COMIENZA *************\n"+
//                          this+
//                          "\n" );
        
        // vB - verticeBuscado
        // vA - verticeMaximoSubarbolIzq
        // vF - Hijo real o hijo fantasma.. para evitar confusion con el hermano. vH
    
        ArbolBinario<T>.Vertice<T> vB, vMax ,vF;
        
        vB = vertice(busca(elemento));

        //De no econtrar en elemento en el arbol rojinegro regresamos, ya que no hay nada por eliminar.
        if(vB == null)
            return;
//        System.out.printf("\nDesarollo de eliminacion de ----> " + vB + "\n");
        
        elementos--;
        vMax = maximoEnSubarbol(vB.izquierdo);
//
//        System.out.printf("Despues de maximo\n");
        if(vMax != null)
            
        {
//            System.out.printf("\nPrevio al de intercambio de vMax: " + vMax + ". Con vB: " + vB );

            
            //Intercambiamos los elementos del max y el vB que encontramos inicialmente.
           
            T eM  = vMax.elemento;
            vMax.elemento = vB.elemento;
            vB.elemento = eM;
            
            vB = vMax;
            
//            System.out.printf("\nResultado de intercambio:"+
//                              "\n" + this+
//                              "\n" );
        }
        
        if(tieneAlgunHijo(vB))
        {
//            System.out.printf("\nHijo natural\n");
            //RECIBIMOS "EL" HIJO (YA que a lo mas hay un hijo, y si tiene algun hijo).
            vF = dameUnHijo(vB);
            
        }
        else
        {
//            System.out.printf("\nHijo fantsama\n");
            
            //CREAMOS EL FANTASMA DEBIDO A LA CARENCIA DE HIJO REALES.
            vF = new Vertice<T>(null);
            //VERIFICAMOS EL COLOR DEL FANTSMA.
            vF.color = Color.NEGRO;
            //METEMOS EL FANTASMA DEL LADO IZQUIERDO.
            
            vF.padre = vB;
            vB.izquierdo = vF;
        }
        
//        System.out.printf("\nArbolConUnicoHijo------------------------------->> "+
//                           vF +"\n\n" +
//                          this+
//                          "\n" );
        
        //AHORA seguro TENEMOS "un" HIJO aunque sea fantasma.

        
        //Literalmente eliminamos nuesto vB. Pero esto DESBALANCEA el arbol.
        //Se elimina a partir de cortar referencias con unico hijo, y su padre.
        //De tener padre se usa el primer metodo.
        //De ser raiz, se salta al segundo metodo.
        if(vB.hayPadre())
            conectaHijoConAbuelo(vB);
        else
            conectaHijoConLaRaiz(vB);
        
        
//        System.out.printf("\nResultado de eliminacion de vB ->>" + vB +
//                          "\nConeccion de F a su abuelo, o raiz si es necesario"+
//                          "\n" + this+
//                          "\n" );
//
        if(esNegro(vB) && !esNegro(vF))
        {
//            System.out.printf("\nvB Negro y vF rojo\n" );
            vF.cambiaColor();
            
//            System.out.printf("\nResultado de vB negro y vF rojo"+
//                              "\n" + this+
//                              "\nRaiz: "+ raiz +
//                              "\nvB: "+ vB +
//                              "\nvF: "+ vF );
//            if(raiz== vB)
//                System.out.printf("\n tenemos problemas\n");
//            if (raiz == vF)
//                System.out.printf("\n tenemos incognitas\n");
            
            return;
        }
        if(esNegro(vB) && esNegro(vF))
        {
//            System.out.printf("\n*************EMPIEZA REHAB****************\n" );
             rehab(vF);
        }
        
        //eliminar vF Si este en null es el interior.
        if(vF.elemento == null)
        {
            if(!vF.hayPadre())
            {
                raiz = null;
            }
        
            else
            {
            //borrarlo

                if(vF.padre.derecho == vF)
                    vF.padre.derecho = null;
                else
                    vF.padre.izquierdo = null;
            }
        }
        
    }
    private boolean hijosSonNegros (Vertice<T> vH)
    {
        if(vH == null)
            return false;
        boolean ambosHijosSonNegros = false;
        boolean hI =false;
        boolean hD =false;
        
        if(esNegro(vH.izquierdo))
            hI = true;
        if(esNegro(vH.derecho))
            hD = true;
        
        if(hI && hD)
            ambosHijosSonNegros = true;
        
        return ambosHijosSonNegros;
        
    }
    private boolean hijosSonBicolores (Vertice<T> vH)
    {
        if (vH == null)
            return false;
        
        boolean diferentesColores = false;
            boolean hI =false;
            boolean hD =false;
            
            if(esNegro(vH.izquierdo))
                hI = true;
            if(esNegro(vH.derecho))
                hD = true;
            
            if((hI && !hD) || (!hI && hD))
                diferentesColores = true;
            
            return diferentesColores;
            
    }
    //LLAMAR unicamente si es seguro que hay un hijo rojo de otra manera puede regresar un hijo negro.
    private Vertice<T> getHijoRojo(Vertice<T> v)
    {
        Vertice<T> vS = null;
        
        
        
        if(esNegro(v.derecho))
            vS = v.izquierdo;
        else
            vS = v.derecho;
        
        return vS;
        
    }
        
    private Vertice<T> getHijoDeMismaDireccionDe(Vertice<T>vH)
        {
            Vertice<T> sDM = null;
            if(vH == null)
                return sDM;
            if (esIzqDePadre(vH))
            {
                if(vH.hayIzquierdo())
                    sDM = vH.izquierdo;
            }else
            {
                if(vH.hayDerecho())
                    sDM = vH.derecho;
            }
            return sDM;
            
        }
        
   private void rehab (Vertice<T> vF)
    {
        //Definimos a vH como el hermano de vB, vH---vP---vF. o al reves.  vB es originalmente vF.
        //Definimos a vS como sobrino. En otras palabras hijo de vH.
        Vertice<T> vH,vS;
        vH = vS = null;
    
        //Caso 1

        if(!vF.hayPadre())
        {
//            System.out.printf("\n"+ "Caso 1");
            return;
        }
        //El hermano de vB existe. Debido a como fue invoado el metodo obligamos que sea negro el padre de este mismo.

        
        //Como existe padre, existe hermano aunque sea null.
        
        
        vH = getHermano(vF);
//        System.out.printf("\n"+ "vH es ----------------------->"+vH);
        //Caso 2
        if(!esNegro(vH))
        {
//            System.out.printf("\n"+ "Caso 2");
            //Coloreamos a hermano de NEGRO.
            vH.cambiaColor();
            //Colorear a P de ROJO
            //SUPONEMOS QUE EL PADRE EL NEGRO.----
            vF.padre.cambiaColor();
            
            if(esIzqDePadre(vF))
            {
                
                giraIzquierda(vF.padre);
                ///MUCHO CUIDADO AQUI
                vH = getHermano(vF);
//                if(vB.hayIzquierdo())
//
//                    vB=vB.izquierdo;
            }
            else
            {
                giraDerecha(vF.padre);
                ///MUCHO CUIDADO AQUI
                vH = getHermano(vF);
//                if(vB.hayDerecho())
//                    vB= vB.derecho;
                
            }
            
            
//            
//            System.out.printf("\n"+ "Resultado de Caso 2");
//            System.out.printf("\n"+ "vH es ----------------------->"+vH);
//            System.out.printf("\n"+ this +"\n" );
        }
        
        //Caso 3
        if(esNegro(vF.padre) && esNegro(vH) && hijosSonNegros(vH))
        {
//            System.out.printf("\n"+ "Caso 3");
            //Coloreamos a vH de rojo.
            vH.cambiaColor();
            rehab(vF.padre);
            return;
        }
        
        //Caso 4
        if(!esNegro(vF.padre) && esNegro(vH) && hijosSonNegros(vH))
        {
//            System.out.printf("\n"+ "Caso 4");
            vH.cambiaColor();
            vF.padre.cambiaColor();
            return;
        }
        //Caso 5
        if (hijosSonBicolores(vH) )
        {

//            System.out.printf("\n"+ "Caso 5 PREVIO");
//            Seguro mandar a pedir el hijoRojo, por como nos metimos en el if.
            
            vS =getHijoRojo(vH);
            
            
            if((esIzqDePadre(vF)&& esIzqDePadre(vS))|| (!esIzqDePadre(vF)&& !esIzqDePadre(vS)))
            {
                
                
//                System.out.printf("\n"+ "Caso 5 REAL");
                
                //SUPONEMOS que vH es negro.
                vH.cambiaColor();
                vS.cambiaColor();
                
                if(esIzqDePadre(vF))
                {
                    
                    giraDerecha(vH);
                    //ACTUALIZAR VARIABLES
                    
                    vH = getHermano(vF);
                    vS =getHijoDeMismaDireccionDe(vH);
                }
                else
                {
                    giraIzquierda(vH);
                    
                    vH = getHermano(vF);
                    vS =getHijoDeMismaDireccionDe(vH);
                    //ACTUALIZAR VARIABLES
                }
                
                
                
                
                
//                System.out.printf("\n"+ "Resultado de Caso 5");
//                System.out.printf("\n"+ "vH es ----------------------->"+vH);
//                System.out.printf("\n"+ this +"\n" );
            }
            
        }
        
        //Caso 6
        
//        System.out.printf("\n"+ "Caso 6");
        
        if (vH!= null)
        {
            

            
        vH.color = vH.padre.color;
        vH.padre.color = Color.NEGRO;
        }
        
        vS =getHijoDeMismaDireccionDe(vH);
        if (vS!=null)
            vS.color = Color.NEGRO;
        if(esIzqDePadre(vF))
        {
            
            giraIzquierda(vF.padre);
            //ACTUALIZAR VARIABLES
            
            vH = getHermano(vF);
        }
        else
        {
            giraDerecha(vF.padre);
            //ACTUALIZAR VARIABLES
            
            vH = getHermano(vF);
        }
        
        

        
        
    }
}

