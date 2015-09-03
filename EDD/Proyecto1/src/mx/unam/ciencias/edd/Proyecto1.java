package mx.unam.ciencias.edd;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

//import java.util.Scanner;

/**
 * Proyecto
 */
public class Proyecto1 {
    
   
    
    private static void textWritingFromARN(ArbolRojinegro<String> arn, String nombreArchivo)
    {
        
        try {
            FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
            OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
            BufferedWriter out = new BufferedWriter(osOut);
            
            for (String s : arn)
                out.write(String.format("%s\n",s));
            
            out.close();
            
        } catch (IOException ioe) {
            System.exit(1);
        }
    }
    
    private static ArbolRojinegro<String> parseArchiveFromISR(String nombreArchivo)
    {
        
        ArbolRojinegro<String> arnS = new ArbolRojinegro<String>();
        
        try
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(nombreArchivo)));

            String line;
            
            while ((line = in.readLine()) != null) {
                    arnS.agrega(line);
            }
            in.close();
        }
        catch(IOException ioe)
        {
            System.out.printf("El archivo %s tiene un error, o no existe.\n",nombreArchivo);
        }
        
        return arnS;
    }
    
    public static void main(String[] args) {
//        uso();
            ArbolRojinegro<String> arn = new ArbolRojinegro<String>();
        
        if (args.length > 0)
        {
            //Leer de archivos
            for(int i = 0; i <args.length; i++ )
            {
                
                ArbolRojinegro<String> iArn = new ArbolRojinegro<String>();
                String nombreArchivo = args[i];
                iArn = parseArchiveFromISR(nombreArchivo);
                for (String s : iArn)
                    arn.agrega(s);
                
            }
        }
        else
            //Leer de la entrada estandar
        {
//                Scanner scn = new Scanner(System.in);
//                String line;
//                while(scn.hasNext())
//                {
//                    line = scn.next();
//                    arn.agrega(line);
//                }
            
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String line;
                        while ((line = in.readLine()) != null) {
                            arn.agrega(line);
                        }
            }
            catch(IOException e){
                        System.out.printf("Algun error extraño del sistema. \n");
            }
        }
        
        
        
        //Escribir a la entrada estandar.
//        try{
//            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
//            
//            String line;
//            
//            for (String s : arn)
//            {
//                out.write(String.format(s+"\n" ));
//                
//            }
//            out.close();
//        }
//        catch(IOException e){
//            System.out.printf("No se puede escribir al sistema. \n");
//        }

        
        
//        for (String s : arn)
//            System.out.println(s);
        
        
        //Precaucion puede sobre escribir sin preguntar......
//        File archivo = new File(nombreArchivo);
//        String name = "archive";
//        while(archivo.exists())
//        {
//            
//            archivo = new File()
//            
//        }

        File f = new File("/Users/Alfonso/Documents/UNAM/EDD/Proyecto3/Dir/Peg/Nimbar/");
        if(!f.exists())
        {
            f.mkdirs();
            
        }

        textWritingFromARN(arn,"Dir/miArchiv.txt");
        

    }
}
