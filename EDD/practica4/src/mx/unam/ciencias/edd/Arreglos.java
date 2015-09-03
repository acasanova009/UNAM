package mx.unam.ciencias.edd;
import java.util.Random;
/**
 * Clase para manipular arreglos genéricos.
 */
public class Arreglos {

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>>
                     void selectionSort(T[] a) {
        // Aquí va su código.
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
        // Aquí va su código.
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
    public static <T extends Comparable<T>>
                     int busquedaBinaria(T[] a, T e) {
                         return busquedaBinariaR(a,e,0,a.length-1);
    }
    public static <T extends Comparable<T>>
    int busquedaBinariaR(T[] a, T e,int i,int f) {
        
        int foundIt = -1;
        if (i<=f)
        {
            int pIndex = (i+f)/2;
            T pElemet = a[pIndex];
            if (e.compareTo(pElemet)>0)
            foundIt = busquedaBinariaR(a,e,pIndex+1,f);
            else if (e.compareTo(pElemet)<0 )
            foundIt= busquedaBinariaR(a,e,i,pIndex-1);
            else foundIt = pIndex;
        }
        
        return foundIt;
    }
}
