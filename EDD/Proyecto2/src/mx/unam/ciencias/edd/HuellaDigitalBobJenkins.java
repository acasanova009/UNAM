package mx.unam.ciencias.edd;

/**
 * Clase para fabricar generadores de huellas digitales.
 */
public class HuellaDigitalBobJenkins<T> implements HuellaDigital<T> {

    private static int c = 0;
    private static int[] huellasBJ = {
    0x3c3b12c7, 0x87d283ec, 0xa4e034c3, 0x90cc7c5f, 0x88934524,
    0xe765b9c0, 0xebd77ecb, 0x76e1f9f5, 0x4943f137, 0xb4ac0622,
    0x1745062a, 0x76f6ebe7, 0x8a553099, 0x93618619, 0x811c8dc8,
    0xa086930d, 0xde6cebcf, 0x20326b1e, 0x8ba24f58, 0x17bb0c06,
    0xfeff3a61, 0x8f1b9516, 0x6444150c, 0x096970d1, 0x7c00ba45,
    0x3573a572,
    };
    public int huellaDigital(T objeto){
        
        byte[] k = objeto.toString().getBytes();
        return huella_bj(k, k.length);
    }
    
    static int huella_bj(byte[]k, int n)
    {
        int a, b, c;
        int l ,i;
        
        l = n;
        a = b = 0x9e3779b9;
        c = 0xffffffff;
        i = 0;
        
        while (l >= 12) {
            a += (k[i] + (k[i+1] << 8) + (k[i+2]  << 16) + (k[i+3]  << 24));
            b += (k[i+4] + (k[i+5] << 8) + (k[i+6]  << 16) + (k[i+7]  << 24));
            c += (k[i+8] + (k[i+9] << 8) + (k[i+10] << 16) + (k[i+11] << 24));
            

            
            a -= b; a -= c; a ^= (c >>>  13);
            b -= c; b -= a; b ^= (a << 8);
            c -= a; c -= b; c ^= (b >>> 13);
            a -= b; a -= c; a ^= (c >>> 12);
            b -= c; b -= a; b ^= (a << 16);
            c -= a; c -= b; c ^= (b >>> 5);
            a -= b; a -= c; a ^= (c >>> 3);
            b -= c; b -= a; b ^= (a << 10);
            c -= a; c -= b; c ^= (b >>> 15);
            
            i += 12;
            l -= 12;
        }
        
        c += n;
        switch (l) {
            case 11: c += (k[i+10] << 24);
            case 10: c += (k[i+9]  << 16);
            case  9: c += (k[i+8]  <<  8);
                
            case  8: b += (k[i+7]  << 24);
            case  7: b += (k[i+6]  << 16);
            case  6: b += (k[i+5]  <<  8);
            case  5: b += k[i+4];
                
            case  4: a += (k[i+3]  << 24);
            case  3: a += (k[i+2]  << 16);
            case  2: a += (k[i+1]  <<  8);
            case  1: a += k[i];
        }
        
        a -= b; a -= c; a ^= (c >>>  13);
        b -= c; b -= a; b ^= (a << 8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a << 16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a << 10);
        c -= a; c -= b; c ^= (b >>> 15);
        
        return (int)c;
    }
//
}
