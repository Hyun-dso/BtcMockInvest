package kim.donghyun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.FuturePosition;
import kim.donghyun.repository.FuturePositionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FuturePositionService {

    private final FuturePositionRepository repository;

    public void openPosition(FuturePosition position) {
        repository.insert(position);
    }

    public List<FuturePosition> getOpenPositions() {
        return repository.findOpenPositions();
    }

    public void closePosition(Long id) {
        repository.closePosition(id);
    }
}