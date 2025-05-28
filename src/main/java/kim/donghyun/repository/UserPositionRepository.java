package kim.donghyun.repository;

import kim.donghyun.model.entity.UserPosition;

public interface UserPositionRepository {
    UserPosition findSpotPositionByUserId(Long userId);
}
