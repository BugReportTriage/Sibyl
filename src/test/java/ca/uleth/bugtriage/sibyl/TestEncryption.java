package ca.uleth.bugtriage.sibyl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import ca.uleth.bugtriage.sibyl.utils.Encryption;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class TestEncryption {

	private static final String TEXT = "I'm my own grandpa";

	@Test
	public void testEncryption() {

		byte[] encrypted = Encryption.encrypt(TEXT);
		Encryption.reset();
		assertEquals(TEXT, Encryption.decrypt(encrypted));
	}

	@Test
	public void testFileOutputEncryption() {
		String filename = Environment.getServletDataDir() + "testEncrypt";
		byte[] encrypted = Encryption.encrypt(TEXT);
		try {

			System.out.println("Writing out text: " + filename);
			FileOutputStream out = new FileOutputStream(filename);
			out.write(encrypted);
			out.close();
			System.out.println("Text written out");

			Encryption.reset();
			File file = new File(filename);
			int fileSize = (int) file.length();
			FileInputStream reader = new FileInputStream(filename);

			byte[] fileInfo = new byte[fileSize];
			for (int i = 0, readByte = reader.read(); i < fileInfo.length
					&& readByte != -1; i++, readByte = reader.read()) {
				fileInfo[i] = (byte)readByte;

			}
			//System.out.println("Read: " + new String{fileInfo});
			assertEquals(TEXT, Encryption.decrypt(encrypted));
			assertEquals(TEXT, Encryption.decrypt(fileInfo));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
