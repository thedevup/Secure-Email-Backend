package conf;


public class Settings {

	public static final String KEY_ALGORITHM = "AES/CBC/PKCS5Padding";
	// use in BouncyCastleSymmetricCipher?
	public static final String KEY_GENERATOR_ALGORITHM = "AES";
	public static final String SECURITY_PROVIDER = "BC";
	public static final String WORDS_DIR = "C:\\Temp";
	public static final String WORDS_FILE = "C:\\Temp\\Words";
	public static final int DEFAULT_POLLING_INTERVAL = 60000; // One minute
	public static final String POP3_PASSWORD = "StrongPassword@";
	public static final int POP3_PORT = 995;
	public static final String POP3_SERVER = "outlook.office365.com";
	public static final String POP3_USER = "messi-201906@hotmail.fr";
	public static final String SMTP_PASSWORD = "StrongPassword@";
	public static final int SMTP_PORT = 587;
	public static final String SMTP_SERVER = "smtp.office365.com";
	public static final String SMTP_USER = "messi-201906@hotmail.fr";
	public static final int LISTENER_PORT = 5432;
	public static final String TCP_SERVER_NAME = "localhost";
}
