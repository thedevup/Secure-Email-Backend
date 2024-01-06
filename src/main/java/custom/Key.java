package custom;

import java.math.BigInteger;

public class Key {
	private final PublicKey publicKey;
	private final PrivateKey privateKey;
	
	public Key(BigInteger d, BigInteger e, BigInteger n) {
		super();
		this.publicKey = new PublicKey(d, n);
		this.privateKey = new PrivateKey(e, n);
	}

	public PublicKey getPublic() {
		return publicKey;
	}

	public PrivateKey getPrivate() {
		return privateKey;
	}
	
}

class PublicKey {
	private final BigInteger d;
	private final BigInteger n;
	
	public PublicKey(BigInteger d, BigInteger n) {
		super();
		this.d = d;
		this.n = n;
	}

	public BigInteger getD() {
		return d;
	}

	public BigInteger getN() {
		return n;
	}
}

class PrivateKey {
	private final BigInteger e;
	private final BigInteger n;
	
	public PrivateKey(BigInteger e, BigInteger n) {
		super();
		this.e = e;
		this.n = n;
	}

	public BigInteger getE() {
		return e;
	}

	public BigInteger getN() {
		return n;
	}
}
