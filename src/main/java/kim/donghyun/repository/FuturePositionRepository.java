package kim.donghyun.repository;

import java.util.List;

import kim.donghyun.model.entity.FuturePosition;

public interface FuturePositionRepository {
    void insert(FuturePosition position);
    List<FuturePosition> findOpenPositions();
    void closePosition(Long id);
}