package mx.unam.ciencias.edd;

/**
 * Clase para fabricar generadores de huellas digitales.
 */
public class HuellaDigitalXor<T> implements HuellaDigital<T> {

    public int huellaDigital(T objeto){
        
        byte[] k = objeto.toString().getBytes();
        
        int r=0, i=0, p=1;
        int l = k.length;
        
        while(l>=4)
        {
            int x = ((k[i]<<24)|(k[i+1]<<16)|(k[i+2]<<8)|(k[i+3]));
            if(p>0){
                r = x;
                p= 0;
            }else{
                r^=x;
            }
            i+=4;
            l-=4;
        }
        
        int t =0;
        
        switch(l){
            case 3: t|=(k[i+2]<<8);
            case 2: t|=(k[i+1]<<16);
            case 1: t|=(k[i]<<24);
        }
        if(l>0)r^=t;
        
    
        return r;
    }
    
}
