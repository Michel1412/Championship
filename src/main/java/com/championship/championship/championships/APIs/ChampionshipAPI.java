package com.championship.championship.championships.APIs;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/championship")
public class ChampionshipAPI {


    private final ChampionshipService championshipService;

    public ChampionshipAPI(ChampionshipService championshipService) {
        this.championshipService = championshipService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createChampionship(@RequestBody Championship championship) {
        return ResponseEntity.ok(this.championshipService.createChampionship(championship));
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<Object> startChampionship(@PathVariable Integer id) {
        return ResponseEntity.ok(this.championshipService.startChampionship(id));
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<Object> finishChampionship(@PathVariable Integer id) {
        return ResponseEntity.ok(this.championshipService.finishChampionship(id));
    }
}
