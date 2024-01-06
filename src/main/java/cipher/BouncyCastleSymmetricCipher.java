package cipher;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class BouncyCastleSymmetricCipher implements ICipher {

	private byte[] key;
	private byte[] iv;
	// Probably going to add it back later
	// private static final int BLOCK_SIZE = 16; // AES block size (128 bits)

	public BouncyCastleSymmetricCipher(byte[] key, byte[] iv) {
		this.key = key;
		this.iv = iv;
	}

	// Encrypts the message using the AES key and IV (initialization vector)
	@Override
	public synchronized byte[] encrypt(byte[] message) {
		BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
		cipher.init(true, new ParametersWithIV(new KeyParameter(key), iv));

		byte[] encrypted = new byte[cipher.getOutputSize(message.length)];
		int length = cipher.processBytes(message, 0, message.length, encrypted, 0);
		
		// We have to make sure to handle the following exception: InvalidCipherTextException
		try {
			cipher.doFinal(encrypted, length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypted;
	}

	// Decrypts the message using the AES key and IV (initialization vector)
	@Override
	public synchronized byte[] decrypt(byte[] message) {
		BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
		cipher.init(false, new ParametersWithIV(new KeyParameter(key), iv));

		byte[] decrypted = new byte[cipher.getOutputSize(message.length)];
		int length = cipher.processBytes(message, 0, message.length, decrypted, 0);
		
		// We have to make sure to handle the following exception: InvalidCipherTextException
		try {
			cipher.doFinal(decrypted, length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decrypted;
	}
	
	public synchronized String encryptString(String data) {
		byte[] inputBytes = data.getBytes();
		byte[] outputBytes = encrypt(inputBytes);
		return new String(outputBytes);
	}

	public synchronized String decryptString(String data) {
		byte[] inputBytes = data.getBytes();
		byte[] outputBytes = decrypt(inputBytes);
		return new String(outputBytes);
	}
}

