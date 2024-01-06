package cipher;

public interface ICipher {
	default String encrypt(String message) throws Exception {
		return new String(encrypt(message.getBytes()));
	}
	default String decrypt(String encrypted) throws Exception {
		return new String(decrypt(encrypted.getBytes()));
	}
	byte[] encrypt(byte[] message) throws Exception;
	byte[] decrypt(byte[] encrypted) throws Exception;
}
