package kim.donghyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kim.donghyun.model.entity.UserPosition;
import kim.donghyun.service.PositionService;

@RestController
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping("/api/position")
    public UserPosition getUserPosition(@RequestParam Long userId) {
        return positionService.getSpotPosition(userId);
    }
}
