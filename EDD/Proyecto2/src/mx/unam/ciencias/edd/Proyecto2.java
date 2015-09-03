package mx.unam.ciencias.edd;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Proyecto 2: Diccionarios.
 */
public class Proyecto2 {

    public static void main(String[] args) {
        
        
        
        int total = 10;
        
        boolean lock = false;
        
        int m = 1;
        
        Random random = new Random();
        ArbolBinario<Integer>ab;
        String head = "<!DOCTYPE html> \n <html> \n <body>\n";
        
        
        if(args.length == 3)
        {
            try{
                //TOATL DE VETICES
                total = Integer.parseInt(args[0]);
                //DEJAR VERTICES EN POSICION INICIAL.
                int c = Integer.parseInt(args[1]);
                //DIFERENCIA DEL ALGORITMO
                m = Integer.parseInt(args[2]);
                if(c!=0)
                    lock = true;
//                lock =  Boolean.parseBoolean(args[1]);
            }
            catch(NumberFormatException e)
            {total = 4;}
//            
            Grafica<Integer> grafica = new Grafica<Integer>();
            
            for (int i = 0; i < total; i++)
                grafica.agrega(i);
            for (int i = 0; i < total; i++) {
                for (int j = i+1; j < total; j+=m)
                    grafica.conecta(i, j);
                
            }
            
            head+=grafica.generaScalableVectorGraphics(lock,m);

        }
        else if(args.length == 2)
        {
            try{
                
                total = Integer.parseInt(args[0]);
                int c = Integer.parseInt(args[1]);
                if(c!=0)
                    lock = true;
            }
            catch(NumberFormatException e){}
            
            for(int a= 1; a<total;a++)
            {
                
                Grafica<Integer> g1 = new Grafica<Integer>();
                
                for (int i = 0; i < total; i++)
                    g1.agrega(i);
                
                for (int i = 0; i < total; i++)
                    for (int j = i+1; j < total; j+=a)
                        g1.conecta(i, j);
                
                head+=g1.generaScalableVectorGraphics(lock, a);
            }
        }
        
        // VA A CREAR TOODOS LOS ESTLOS DE GRAFICA POSIBLES.
        else if(args.length == 1)
        {
            try{
                total = Integer.parseInt(args[0]);
//                int c = Integer.parseInt(args[1]);
//                if(c!=0)
//                    lock = true;
            }
            catch(NumberFormatException e){}
            
            for(int a= 1; a<total;a++)
            {
            
                Grafica<Integer> g1 = new Grafica<Integer>();
                Grafica<Integer> g2 = new Grafica<Integer>();
                Grafica<Integer> g3 = new Grafica<Integer>();
                Grafica<Integer> g4 = new Grafica<Integer>();
            
                for (int i = 0; i < total; i++)
                {
                    g1.agrega(i);
                    g2.agrega(i);
                    
                    
                }
                for (int i = 0; i < total+1; i++)
                {
                
                    g3.agrega(i);
                    g4.agrega(i);
                }
                
                for (int i = 0; i < total; i++)
                    for (int j = i+1; j < total; j+=a){
                        g1.conecta(i, j);
                        g2.conecta(i, j);
                    }
                head+=g1.generaScalableVectorGraphics(lock, a);
                head+=g2.generaScalableVectorGraphics(!lock, a);
                
                
                for (int i = 0; i < total; i++)
                    for (int j = i+1; j < total+1; j+=a)
                    {
                        g3.conecta(i, j);
                        g4.conecta(i, j);
                    }
                head+=g3.generaScalableVectorGraphics(lock, a);
                head+=g4.generaScalableVectorGraphics(!lock,a);
                
                
                
                

            }
            
        }
        
        

//        ab = new ArbolBinarioCompleto<Integer>();
//        for (int i = 0; i < total; i++)
//            ab.agrega(i);
//        
//        
//
//        head+= ab.generaScalableVectorGraphics();
//
//        ab = new ArbolBinarioOrdenado<Integer>();
//        for (int i = 0; i < total/4; i++)
//            ab.agrega(random.nextInt(total/4));
//        head+= ab.generaScalableVectorGraphics();
//
//        
//        ab = new ArbolRojinegro<Integer>();
//        for (int i = 0; i < total; i++)
//            ab.agrega(random.nextInt(total));
//        
//        head+= ab.generaScalableVectorGraphics();
//        
//        
//        
//        Lista<Integer> l=  new Lista<Integer>();
//        for (int i = 0; i < total/4; i++)
//            l.agregaInicio(i);
//        
//        head+= l.generaScalableVectorGraphics();
        
       
//        for(int i = 0; i<total; i+=5)
//        {
//            grafica.libera(i);
//        }
    
        

        
        head+="</body>\n</html>\n";

        System.out.println(head);

        
        
    
    }
}
