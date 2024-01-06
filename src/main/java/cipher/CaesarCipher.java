package cipher;

public class CaesarCipher implements ICipher {
	private int shift;
	
	public CaesarCipher(int shift) {
		this.shift = shift;
	}
	
	public CaesarCipher() {
		this.shift = 0;
	}
	
    public synchronized int getShift() {
		return shift;
	}

	public synchronized void setShift(int shift) {
		this.shift = shift;
	}
	
	public void increaseShift() {
		shift++;
	}

	public synchronized String encrypt(String message) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);

            if (Character.isLetter(ch)) {
                char shifted = (char) (((int) ch + shift - 65) % 26 + 65);
                result.append(shifted);
            } 
            else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public synchronized String decrypt(String encrypted) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < encrypted.length(); i++) {
            char ch = encrypted.charAt(i);
            if (Character.isLetter(ch)) {
                char shifted = (char) (((int) ch - shift - 65 + 26) % 26 + 65);
                result.append(shifted);
            } 
            else {
                result.append(ch);
            }
        }
        return result.toString();
    }

	@Override
	public synchronized byte[] encrypt(byte[] message) {
		return encrypt(new String(message)).getBytes();
	}

	@Override
	public synchronized byte[] decrypt(byte[] encrypted) {
		return decrypt(new String(encrypted)).getBytes();
	}
}
