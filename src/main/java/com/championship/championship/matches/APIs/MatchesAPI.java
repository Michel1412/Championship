package com.championship.championship.matches.APIs;

import com.championship.championship.matches.matchesDTO.MatchesDTO;
import com.championship.championship.matches.service.MatchesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
