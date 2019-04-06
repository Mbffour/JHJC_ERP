package com.jhj.token;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 非对称加密算法RSA算法组件 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 * 
 * @author kongqz
 */
public class RSAUtil {

  public  static enum ComGameType{
    CRB,COWBOY;

    public Integer getIntValue(){
      switch (this){
        case COWBOY:
          return 1;
        case CRB:
          return 2;
      }
      return 0;
    }
  }

  public static  void main(String[] args){

  }
  // 非对称密钥算法
  public static final String KEY_ALGORITHM = "RSA";

  /**
   * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间
   */
  private static final int KEY_SIZE = 512;

  private static KeyPairGenerator keyPairGenerator;

  private static KeyFactory keyFactory;

  static {
    try {
      keyPairGenerator = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
      keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
      keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      System.out.println("\n\n\n\n init KeyPairGenerator Failed , System will exit \n\n\n\n\n");
      System.exit(0);
    }
  }

  /**
   * 初始化密钥对
   * 
   * @return String 公钥
   */
  public static String initKey() {
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    String modulusHex = Hex.encodeHexString(keyPair.getPublic().getEncoded());
    //CacheManager.getInstance().TOKEN_CACHE.put(modulusHex, keyPair);
    return modulusHex;
  }


  /**
   * 私钥加密
   * 
   * @param data 待加密数据
   * @param key 密钥
   * @return byte[] 加密数据
   */
  public static byte[] encryptByPrivateKey(byte[] data, byte[] key) {
    try {
      // 取得私钥
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
      // 生成私钥
      PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
      // 数据加密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);
      return cipher.doFinal(data);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 公钥加密
   * 
   * @param
   * @param key 密钥
   * @return byte[] 加密数据
   */
  public static byte[] encryptByPublicKey(byte[] data, byte[] key) {
    try {
      // 产生公钥
      PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(key));

      // 数据加密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      return cipher.doFinal(data);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 私钥解密
   * 
   * @param data 待解密数据
   * @param keyBytes 密钥
   * @return byte[] 解密数据
   */
  public static byte[] decryptByPrivateKey(byte[] data, byte[] keyBytes) {
    try {
      System.out.println(data.length + " | " + keyBytes.length);

      // 生成私钥
      PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

      // 数据解密
      Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return cipher.doFinal(data);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 公钥解密
   * 
   * @param data 待解密数据
   * @param key 密钥
   * @return byte[] 解密数据
   */
  public static byte[] decryptByPublicKey(byte[] data, byte[] key) {
    try {
      // 初始化公钥 密钥材料转换
      X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
      // 产生公钥
      PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
      // 数据解密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, pubKey);
      return cipher.doFinal(data);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

}

