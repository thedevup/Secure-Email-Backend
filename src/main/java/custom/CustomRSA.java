package custom;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import cc.redberry.rings.primes.BigPrimes;

public class CustomRSA {
	public static int BIT_LENGTH = 32; // Very weak

	public static void main(String[] args) throws NoSuchAlgorithmException {
		Key key = generateKey();
		BigInteger message = BigInteger.valueOf(123456);
		BigInteger encrypted = encrypt(key.getPublic(), message);
		BigInteger decrypted = decrypt(key.getPrivate(), encrypted);
		System.out.println(decrypted);
		
		Key crackedKey = crack(key.getPublic());
		encrypted = encrypt(crackedKey.getPublic(), message);
		decrypted = decrypt(crackedKey.getPrivate(), encrypted);
		System.out.println(decrypted);
	}
	
	public static Key crack(PublicKey publicKey) throws NoSuchAlgorithmException {
		long[] factors = BigPrimes.primeFactors(publicKey.getN().longValue());
		Key key = generateKey(BigInteger.valueOf(factors[0]), BigInteger.valueOf(factors[1]));
		return key;
	}
	
	public static Key generateKey() throws NoSuchAlgorithmException {
		Random random = SecureRandom.getInstanceStrong();
		
		// Generate random primes
		BigInteger p = BigInteger.probablePrime(BIT_LENGTH / 2, random);
		BigInteger q = BigInteger.probablePrime(BIT_LENGTH / 2, random);
		
		return generateKey(p, q);
		
	}
	
	public static Key generateKey(BigInteger p, BigInteger q) throws NoSuchAlgorithmException {
		Random random = SecureRandom.getInstanceStrong();
		// Calculate products
		BigInteger n = p.multiply(q);
		BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

		// Generate public and private exponents
		BigInteger e;
		do e = new BigInteger(phi.bitLength(), random);
		while (e.compareTo(BigInteger.ONE) <= 0
			|| e.compareTo(phi) >= 0
			|| !e.gcd(phi).equals(BigInteger.ONE));
		BigInteger d = e.modInverse(phi);
		
		Key key = new Key(d, e, n);
		return key;
	}

	public static BigInteger encrypt(PublicKey key, BigInteger message) {
		BigInteger enc = message.modPow(key.getD(), key.getN());
		return enc;
	}
	
	public static BigInteger decrypt(PrivateKey key, BigInteger enc) {
		BigInteger dec = enc.modPow(key.getE(), key.getN());
		return dec;
	}
}
