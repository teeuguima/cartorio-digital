/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**Classe para Codificação e Decodificação de chaves privadas e públicas!
 *
 * @author Some Java Stuff (Blog)
 */
public class ConvertKey implements Serializable {
    private final String ALGORITHM = "DSA";
    
    public ConvertKey(){
        
    }
    
    public String converterPublicKey(PublicKey pbKey){
        String pubKey = Base64.getEncoder().encodeToString(pbKey.getEncoded());
        return pubKey;
    }
    
    private String Base64ToUtf8(byte[] bytes){
        String convert = new String(bytes, StandardCharsets.UTF_8);
        return convert;
    }
    
    public String converterPrivateKey(PrivateKey pvKey){
        String privKey = Base64.getEncoder().encodeToString(pvKey.getEncoded());
        return privKey;
    }
    
    public PublicKey convertStringToPublicKey(String pbKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(BASE64decode(pbKey));
        return keyFactory.generatePublic(publicKeySpec); 
    }
    
    public PrivateKey convertStringToPrivateKey(String pvKey) throws InvalidKeySpecException, NoSuchAlgorithmException{
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(BASE64decode(pvKey));
        return keyFactory.generatePrivate(privateKeySpec); 
    }
    
    public String BASE64encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }
   
    public byte[] BASE64decode(String data){
        return Base64.getDecoder().decode(BASE64encode(data.getBytes()));
    }
    
    public String UTF8encode(byte[] data){
        return new String(data, StandardCharsets.UTF_8);
    }
   
    public byte[] UTF8decode(String data){
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
