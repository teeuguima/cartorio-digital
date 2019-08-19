/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import static sun.security.x509.CertificateAlgorithmId.ALGORITHM;

/**
 *
 * @author Teeu Guima
 */
public class DecoderKey {
    public String encodePublicKey(PublicKey publicKey){
        return BASE64encode(publicKey.getEncoded());
    }
 
    public PublicKey decodePublicKey(String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(BASE64decode(publicKey));
        return keyFactory.generatePublic(publicKeySpec);          
    }
   
    public PrivateKey decodePrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(BASE64decode(privateKey));
        return keyFactory.generatePrivate(privateKeySpec);          
    }
 
    public String encodePrivateKey(PrivateKey privateKey){
        return BASE64encode(privateKey.getEncoded());
    }  
 
    public String BASE64encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }
   
    public byte[] BASE64decode(String data){
        return Base64.getDecoder().decode(data);
    }
}
