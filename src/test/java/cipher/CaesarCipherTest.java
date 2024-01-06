package cipher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CaesarCipherTest {

	private CaesarCipher cipher;

	@BeforeEach
	public void setUp() {
		cipher = new CaesarCipher(3);
	}

	@Test
	public void testEncrypt() {
		String message = "NOUR";
		String result = "QRXU";
		assertEquals(result, cipher.encrypt(message));
	}

	@Test
	public void testDecrypt() {
		String encrypted = "DGDP";
		String result = "ADAM";
		assertEquals(result, cipher.decrypt(encrypted));
	}

	@Test
	public void testEncryptDecrypt() {
		String message = "NOUR";
		String encrypted = cipher.encrypt(message);
		String decrypted = cipher.decrypt(encrypted);
		assertEquals(message, decrypted);
	}

	@Test
	public void testSetShift() {
		cipher.setShift(7);
		assertEquals(7, cipher.getShift());
	}
}
