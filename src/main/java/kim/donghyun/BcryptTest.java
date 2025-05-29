package kim.donghyun;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String raw = "1234";
		String encoded = encoder.encode(raw);
		System.out.println("encoded = " + encoded);
	}
}
