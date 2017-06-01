package ca.uleth.bugtriage.sibyl.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class Encryption {

	private static Cipher cipher = null;

	private static final String ALGORITHM = "DES";

	private static final String KEY_FILENAME = "encryption.key";

	private static Key key = null;

	private static Key getKey() {
		if (key == null) {
			String keyFile = Environment.getServletDataDir() + KEY_FILENAME;
			try {
				System.out.println("Reading in key ("
						+ keyFile + ")");
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(keyFile));
				key = (Key) in.readObject();
				System.out.println("Key retrieved");
			} catch (FileNotFoundException e) {
				// Encryption key doesn't exist, create a new one
				Key newKey = createKey();
				saveKey(newKey, keyFile);
				return newKey;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return key;
	}

	private static Cipher getCipher() {
		try {
			if (cipher == null) {
				cipher = Cipher.getInstance(ALGORITHM);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipher;
	}

	public static byte[] encrypt(String clearText) {
		byte[] encrypted = new byte[0];
		try {
			getCipher().init(Cipher.ENCRYPT_MODE, getKey());
			encrypted = getCipher().doFinal(clearText.getBytes());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encrypted;
	}

	public static String decrypt(byte[] encrypted) {
		String clearText = "";

		try {
			getCipher().init(Cipher.DECRYPT_MODE, getKey());
			clearText = new String(getCipher().doFinal(encrypted));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clearText;

	}

	private static Key createKey() {
		try {
			System.out.println("Creating key");
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			generator.init(new SecureRandom());
			return generator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void saveKey(Key key, String filename) {
		try {

			System.out.println("Writing out key: " + filename);
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(filename));
			out.writeObject(key);
			out.close();
			System.out.println("Key written out");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setKey(Key key) {
		Encryption.key = key;
	}

	public static void reset() {
		cipher = null;
		key = null;
	}

	public static void main(String[] args) {
		Key key = createKey();
		saveKey(key, Environment.getServletDataDir() + KEY_FILENAME);
	}

}
