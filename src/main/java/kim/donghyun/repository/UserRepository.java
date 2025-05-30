package kim.donghyun.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kim.donghyun.model.entity.User;

@Mapper
public interface UserRepository {

	// 이메일로 회원 조회
	User findByEmail(@Param("email") String email);

	User findByUsername(@Param("username") String username);

	// 회원 등록
	void insert(User user);

	// (선택) 모든 유저 조회
	// List<User> findAll();
}
