package cipher;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

public class BouncyCastleAsymmetricCipher implements ICipher {

	private CipherParameters publicKey;
	private CipherParameters privateKey;

	public BouncyCastleAsymmetricCipher(CipherParameters publicKey, CipherParameters privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	// This method is used to generate and RSA key pair
	public static AsymmetricCipherKeyPair generateKeyPair() {
		RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
		// Parameters: BigInteger publicExponent, SecureRandom random, int strength, int certainty
		RSAKeyGenerationParameters params = new RSAKeyGenerationParameters(
				BigInteger.valueOf(65537),
				new SecureRandom(),
				2048,
				80
		);
		generator.init(params);
		return generator.generateKeyPair();
	}

	// Encrypt the provided message (byte[]) using the RSA public key
	@Override
	public synchronized byte[] encrypt(byte[] message) {
		RSAEngine engine = new RSAEngine();
		engine.init(true, publicKey);
		return engine.processBlock(message, 0, message.length);

	}
	
	// Decrypt the message using the private key
	@Override
	public synchronized byte[] decrypt(byte[] message) {
		RSAEngine engine = new RSAEngine();
		engine.init(false, privateKey);
		return engine.processBlock(message, 0, message.length);
	}
	
	// Encrypt a string
	public synchronized String encrypt(String data) {
		byte[] inputBytes = data.getBytes();
		byte[] outputBytes = encrypt(inputBytes);
		return new String(outputBytes);
	}

	// Decript a string
	public synchronized String decrypt(String data) {
		byte[] inputBytes = data.getBytes();
		byte[] outputBytes = decrypt(inputBytes);
		return new String(outputBytes);
	}
	
}

