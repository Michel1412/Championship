package com.championship.championship.championships.APIs;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.service.ChampionshipService;
import com.championship.championship.classifications_table.ClassificationsTable;
import com.championship.championship.classifications_table.classificationsTableDTO.ClassificationTableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Object> startChampionship(@PathVariable Integer id, @RequestBody ClassificationTableDTO classificationTableDTO) {
        return ResponseEntity.ok(this.championshipService.startChampionship(id, classificationTableDTO));
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<Object> finishChampionship(@PathVariable Integer id) {
        return ResponseEntity.ok(this.championshipService.finishChampionship(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateChampionship(@PathVariable Integer id, @RequestBody Championship championship) {
        return ResponseEntity.ok(this.championshipService.updateChampionship(id, championship));
    }

    @GetMapping("/list-by-year/{year}")
    public ResponseEntity<List<Championship>> listChampionship(@PathVariable int year) {
        return ResponseEntity.ok(this.championshipService.findAllByYear(year));
    }

    @GetMapping("/list-teams-on/{id}")
    public ResponseEntity<List<ClassificationsTable>> listAllTeamsOnChampionship(@PathVariable Integer id) {
        return ResponseEntity.ok(this.championshipService.findAllTeamsByChampionship(id));
    }
}
