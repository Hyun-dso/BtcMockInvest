package kim.donghyun.repository;

import kim.donghyun.model.entity.User;

public interface UserRepository {
    
    // 이메일로 회원 조회
    User findByEmail(String email);

    // 회원 등록
    void insert(User user);

    // (선택) 닉네임 중복 조회
    User findByNickname(String nickname);

    // (선택) 모든 유저 조회
    // List<User> findAll();
}
