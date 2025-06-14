package kim.donghyun.model.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {
	private Long id;
	private String email;
	private String password;
	private String username;
	private boolean isVerified;
	private LocalDateTime createdAt;
	private LocalDateTime lastLoginAt;

	// getter, setter lombok으로 자동

}
