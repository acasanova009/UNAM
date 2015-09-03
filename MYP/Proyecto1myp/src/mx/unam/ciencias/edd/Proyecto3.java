package mx.unam.ciencias.edd;

import java.text.NumberFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Proyecto 3: Historgarmas.
 */
public class Proyecto3 {

    public static void main(String[] args) {
        
        
        if(args.length<=2)
            System.exit(1);
        
        if(!args[0].equals("-o"))
            System.exit(2);
        
        String  dir = "";
        dir = args[1];
        File loc = new File(dir);
        if(!loc.exists())
            loc.mkdirs();
        
        
        Histograma<String> bigHistograma =  new Histograma<String>("Histograma.");
        Lista<Histograma<String>> histogramas = new Lista<Histograma<String>>();
        Grafica<String> nameHistogramas = new Grafica<String>();
        
        for(int i = 2; i <args.length; i++ )
            {
                Lista<String> lista = new Lista<String>();
                String nombreArchivo = args[i];
                lista = analizarArchivoPorPalabra(nombreArchivo);
                if(lista==null)
                    continue;
                Histograma<String> his = Histograma.seccionamiento(lista);
                his.nombre = nombreArchivo;

                try{
                nameHistogramas.agrega(nombreArchivo);
                    for(Histograma<String> h : histogramas)
                        if(h.relacionado(his))
                            nameHistogramas.conecta(his.nombre, h.nombre);
                }
                catch(IllegalArgumentException e)
                {}

                histogramas.agregaFinal(his);
                bigHistograma.agrega(his);
                
            }
        
//        
//        //IMPRIMIMOS EL INDICE.
//        String inx = "";
//        inx = ("\n<h2>Indice:</h2>\n"+
//        "<ul>\n"+
//        "<li><a href=\"principal.html\">Histograma Principal</a></li>\n"+
//        "<li><a href=\"pasteles.html\">Documentos</a></li>\n"+
//        "<li><a href=\"grafica.html\">Grafica</a></li>\n"+
//        "<li><a href=\"graficaMasiva.html\">Grafica Masiva</a></li>\n"+
//        "</ul>\n");
//        inx+= headCreator("- - - Alfonso Casanova.",4);
//        inx= encapusuladorHtmlConRegreso(null,inx,"Proyecto 3.");
//        writeStringToFile(inx,new File(dir+"index.html"));
        
        //IMPRIMIMOS EL HISTOGRAMA PRINCIPAL.
        String p = "";
        
        p+= headCreator("Barras",2);
        p+= bigHistograma.toScalableVectorGraphicsBars(true, 20, false, true);
        p+= headCreator("Lineas",2);
        p+= bigHistograma.toScalableVectorGraphicsBars(true,0, true, true);
        p+= headCreator("En desorden",2);
        p+= bigHistograma.toScalableVectorGraphicsBars(true,0, true, true,true);
        p = encapusuladorHtmlConRegreso("index",p,"Histograma Principal");
        writeStringToFile(p,new File(dir+"principal.html"));
//        
        //IMPRIMIMOS LOS PASTELES.
        String g = "";
        g+= headCreator(" Cada documento con sus respectivas graficas.",2);
        for(Histograma<String> h: histogramas)
        {
            g+="<hr/>";
            g+= headCreator("",2);
            
            g+=h.toScalableVectorGraphicsBars(true,20, false, true);
            g+=h.toScalableVectorGraphicsBars(true,15, true, true);
            g+=h.toScalableVectorGraphicsBars(true,0, true, true,true);

            g+=h.toScalableVectorGraphicsPieChart();
        }
        g = encapusuladorHtmlConRegreso("index",g,"Histogramas y grafica por documentos");
        writeStringToFile(g,new File(dir+"pasteles.html"));
//
        //IMPRIMIMOS LA GRAFICAS.
        String f = "";
        f+= headCreator("Grafica con los docuemtnos relacionados", 2);
        f+= nameHistogramas.toScalableVectorGraphics();
        f= encapusuladorHtmlConRegreso("index",f,"Grafica");
        writeStringToFile(f,new File(dir+"grafica.html"));
        
//        IMPRIMIMOS ALGO extra
        
        Grafica<Integer> grafica = new Grafica<Integer>();

        int total = 50;
        int modifier = 5;
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j+=modifier)
                grafica.conecta(i, j);
            
        
        
        String m  = "";
        m+= headCreator("Grafica masiva", 2);
        m+= grafica.generaScalableVectorGraphics(true, modifier);
        m= encapusuladorHtmlConRegreso("index",m,"GraficaMasiva");
        writeStringToFile(m,new File(dir+"graficaMasiva.html"));
        
    
    }
    private static String headCreator(String title, int i)
    {
        return String.format("\n<h%d>%s</h%d>\n",i,title,i);
    }
    private static String encapusuladorHtmlConRegreso(String paginaAnterior, String aEncapsular, String tituloActual)
    {
        
        String m = ("<!DOCTYPE html>\n<html>\n<head>\n"+
                    
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"\n>"+
                    "<link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">"+
                    "<link rel=\"stylesheet\" href=\"styles.css\" />\n"+
                    "</head>");
        m+= String.format("\n<body>\n<h1 class=\"mainTitle\">%s</h1>\n<hr/>\n",tituloActual);
        m+= aEncapsular;
        m+="<br/><br/>";
        if(paginaAnterior!=null)
        m+=String.format("<a href=\"%s.html\">&larr; Volver</a>",paginaAnterior);
        m+="\n</body>\n</html>";
        
        return m ;
    }
    private static void writeStringToFile(String stringToWrite, File file)
    {
        
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
            BufferedWriter out = new BufferedWriter(osOut);
                out.write(stringToWrite);
            out.close();
            
        } catch (IOException ioe) {
            System.exit(1);
        }
    }
    
    
    
    public static Lista<String> analizarArchivoPorPalabra(String nombreArchivo)
    {
        
        
        Lista<String> l = new Lista<String>();
        File f = new File(nombreArchivo);
        if(!f.exists())
            return null;
        String delimitadores= "[ .,;?!¡¿\'\"\\[\\]]+";
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] palabrasSeparadas = line.split(delimitadores);
                for(String s: palabrasSeparadas)
                    l.agregaFinal(s);
            }
            in.close();
        }
        catch(IOException ioe)
        {
            System.out.printf("El archivo %s tiene un error, o no existe.\n",nombreArchivo);
        }
        
        
        return l;
    }
    
    
    
}
