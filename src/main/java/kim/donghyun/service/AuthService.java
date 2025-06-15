package kim.donghyun.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kim.donghyun.model.dto.SigninRequest;
import kim.donghyun.model.dto.SignupRequest;
import kim.donghyun.model.entity.User;
import kim.donghyun.model.entity.Wallet;
import kim.donghyun.repository.UserRepository;
import kim.donghyun.repository.WalletRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WalletRepository walletRepository;

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public String signup(SignupRequest req) {
		if (userRepository.findByEmail(req.getEmail()) != null)
			return "email";

		if (userRepository.findByUsername(req.getUsername()) != null)
			return "username";

		User user = new User();
		user.setEmail(req.getEmail());
		user.setPassword(encoder.encode(req.getPassword()));
		user.setUsername(req.getUsername());
		user.setVerified(false);

		userRepository.insert(user);
		
		// 계정 생성 후 자동 지갑 생성
		Wallet wallet = new Wallet();
		wallet.setUserId(user.getId()); // 🔑 방금 생성된 유저 ID
		wallet.setBtcBalance(BigDecimal.ZERO);
		wallet.setUsdtBalance(BigDecimal.ZERO);
		wallet.setInitialValue(BigDecimal.ZERO);
		walletRepository.insert(wallet);
		
		return "success";
	}

	public User signin(SigninRequest req) {
		User user = userRepository.findByEmail(req.getEmail());
		
		
		if (user == null)
			return null;

//		if (!user.isVerified())
//			return null; // 메일 인증 안 된 사용자 로그인 제한

		if (!encoder.matches(req.getPassword(), user.getPassword()))
			return null;

		return user;
	}
	
	public void updateLastLoginAt(User user) {
		userRepository.updateLastLoginAt(user); // 또는 JPA면 repository.save(user)
	}
}