package mx.unam.ciencias.icc;

import java.util.Random;
/**
 * Clase para ordenar y buscar en arreglos genéricos.
 */
public class Arreglos <T extends Comparable<T>>{

    

//    public static void main(String[] args)
//    {
//        
//        int N = 20; /* Cien mil */
//        Random random = new Random();
////        NumberFormat nf = NumberFormat.getIntegerInstance();
////        long tiempoInicial, tiempoTotal;
//        
//        Integer[] arreglo = new Integer[N];
//        for (int i = 0; i < N; i++)
//            arreglo[i] = random.nextInt(20);
//        
//        
//        System.out.println("Desordenado:");
//        
//        for(int n: arreglo)
//        {
//            System.out.printf(n+" ");
//        }
//        quickSort(arreglo);
//        
//        System.out.println("\nOrdenado:");
//        for(int n: arreglo)
//        {
//            System.out.printf(n+" ");
//        }
//        
//        System.out.println("");
//       
//    }
    
    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param a un arreglo cuyos elementos son comparables.
     */
    
    public static <T extends Comparable<T>> void selectionSort(T[] a)
    {
        for(int i = 0;i < a.length;i++)
        {
//            System.out.printf("\nPorcentaje: %.2f",(float)(i*100)/a.length);
            int m = i;
            for (int j = i + 1; j <a.length;j++)
            {
               if (a[j].compareTo(a[m]) < 0)
                m =j;
            }
            swap(a,i,m);
        }
                             
    }

    private static <T extends Comparable<T>> void swap (T[] a,int i,int j)
    {
        T t = a[i];
        a[i]=a[j];
        a[j]=t;
    }
    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>>
                     void quickSort(T[] a) {
                         quickSort(a,0,a.length-1);
                
    }
    private static <T extends Comparable<T>> void quickSort(T[] a,int p, int r)
    {
        
        if (p >=r )
            return;
        Random rand = new Random();
        int t = rand.nextInt(r-p)+p;
        swap(a,t,r);
        
        int i = p-1;
        for (int j = p; j<r; j++)
            
            if(a[j].compareTo(a[r]) <= 0)
                swap(a,++i,j);
        
        swap(a,++i,r);
        
        quickSort(a,p,i-1);
        quickSort(a,i+1,r);
        
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa
     * el índice del elemento en el arreglo, o -1 si no se
     * encuentra.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se
     * encuentra.
     */
    public static <T extends Comparable<T>>  int busquedaBinaria(T[] a, T e)
    {

        int foundIt = -1;
        for (int i= 0 ; i < a.length;i++)
            if(a[i].compareTo(e) == 0 )
                foundIt = i;
        return foundIt;
    }
}