package hrashton.ocpg;

import android.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Класс реализующий работу с алгоритмом шифрования DES
 * @author Rumoku
 */
class DesEncrypter {
  public static String encrypt(String plainMessage,
                               String symKeyHex) {
    final byte[] symKeyData = Base64.decode(symKeyHex, Base64.DEFAULT);

    final byte[] encodedMessage = plainMessage.getBytes(Charset.forName("UTF-8"));
    try {
      final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      final int blockSize = cipher.getBlockSize();

      // create the key
      final SecretKeySpec symKey = new SecretKeySpec(Arrays.copyOf(symKeyData, 32), "AES");

      // generate random IV using block size (possibly create a method for
      // this)
      final byte[] ivData = new byte[blockSize];
      final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
      rnd.nextBytes(ivData);
      final IvParameterSpec iv = new IvParameterSpec(ivData);

      cipher.init(Cipher.ENCRYPT_MODE, symKey, iv);

      final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

      // concatenate IV and encrypted message
      final byte[] ivAndEncryptedMessage = new byte[ivData.length
              + encryptedMessage.length];
      System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
      System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage,
              blockSize, encryptedMessage.length);

      final String ivAndEncryptedMessageBase64 = Base64.encodeToString(ivAndEncryptedMessage, Base64.DEFAULT);

      return ivAndEncryptedMessageBase64;
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException(
              "key argument does not contain a valid AES key");
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(
              "Unexpected exception during encryption", e);
    }
  }

  public static String decrypt(String ivAndEncryptedMessageBase64,
                               String symKeyHex) {
    final byte[] symKeyData = Base64.decode(symKeyHex, Base64.DEFAULT);

    final byte[] ivAndEncryptedMessage = Base64.decode(ivAndEncryptedMessageBase64, Base64.DEFAULT);
    try {
      final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      final int blockSize = cipher.getBlockSize();

      // create the key
      final SecretKeySpec symKey = new SecretKeySpec(Arrays.copyOf(symKeyData, 32), "AES");

      // retrieve random IV from start of the received message
      final byte[] ivData = new byte[blockSize];
      System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
      final IvParameterSpec iv = new IvParameterSpec(ivData);

      // retrieve the encrypted message itself
      final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length
              - blockSize];
      System.arraycopy(ivAndEncryptedMessage, blockSize,
              encryptedMessage, 0, encryptedMessage.length);

      cipher.init(Cipher.DECRYPT_MODE, symKey, iv);

      final byte[] encodedMessage = cipher.doFinal(encryptedMessage);

      // concatenate IV and encrypted message
      final String message = new String(encodedMessage,
              Charset.forName("UTF-8"));

      return message;
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException(
              "key argument does not contain a valid AES key");
    } catch (BadPaddingException e) {
      // you'd better know about padding oracle attacks
      return null;
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(
              "Unexpected exception during decryption", e);
    }
  }

  //Cipher ecipher;
  //Cipher dcipher;
  //
  ///**
  // * Конструктор
  // *
  // * @param key секретный ключ алгоритма DES. Экземпляр класса SecretKey
  // * @throws NoSuchAlgorithmException
  // * @throws NoSuchPaddingException
  // * @throws InvalidKeyException
  // */
  //public DesEncrypter(SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
  //  ecipher = Cipher.getInstance("DES");
  //  dcipher = Cipher.getInstance("DES");
  //  ecipher.init(Cipher.ENCRYPT_MODE, key);
  //  dcipher.init(Cipher.DECRYPT_MODE, key);
  //}
  //
  ///**
  // * Функция шифровнаия
  // *
  // * @param str строка открытого текста
  // * @return зашифрованная строка в формате Base64
  // */
  //public String encrypt(String str) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
  //  byte[] utf8 = str.getBytes("UTF8");
  //  byte[] enc = ecipher.doFinal(utf8);
  //  return Base64.encodeToString(enc, Base64.DEFAULT);
  //}
  //
  ///**
  // * Функция расшифрования
  // *
  // * @param str зашифрованная строка в формате Base64
  // * @return расшифрованная строка
  // */
  //public String decrypt(String str) throws IOException, IllegalBlockSizeException, BadPaddingException {
  //  byte[] dec = Base64.decode(str, Base64.DEFAULT);
  //  byte[] utf8 = dcipher.doFinal(dec);
  //  return new String(utf8, "UTF8");
  //}
}