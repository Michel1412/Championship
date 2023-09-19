package com.championship.championship.matches.APIs;

import com.championship.championship.matches.matchesDTO.MatchesDTO;
import com.championship.championship.matches.service.MatchesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class MatchesAPI {

    private final MatchesService matchesService;

    public MatchesAPI(MatchesService matchesService) {
        this.matchesService = matchesService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createMatch(@RequestBody MatchesDTO matchesDTO){
        return ResponseEntity.ok(this.matchesService.createMatch(matchesDTO));
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<Object> startMatch(@PathVariable Integer id) {
        return ResponseEntity.ok(this.matchesService.startMatch(id));
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<Object> finishMatch(@PathVariable Integer id) {
        return ResponseEntity.ok(this.matchesService.finishMatch(id));
    }
}
