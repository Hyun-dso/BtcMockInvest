package kim.donghyun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kim.donghyun.model.dto.SignupRequest;
import kim.donghyun.model.entity.User;
import kim.donghyun.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public boolean signup(SignupRequest req) {
		if (userRepository.findByEmail(req.getEmail()) != null)
			return false;

		User user = new User();
		user.setEmail(req.getEmail());
		user.setPassword(encoder.encode(req.getPassword()));
		user.setUsername(req.getUsername());
		user.setVerified(false);

		userRepository.insert(user);
		return true;
	}
}