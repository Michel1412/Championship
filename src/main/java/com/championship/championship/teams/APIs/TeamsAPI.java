package com.championship.championship.teams.APIs;


import com.championship.championship.teams.Teams;
import com.championship.championship.teams.service.TeamsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
public class TeamsAPI {

    private final TeamsService teamsService;

    public TeamsAPI( TeamsService teamsService) {
        this.teamsService = teamsService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createTeam(@RequestBody Teams teams) {
        return ResponseEntity.ok(this.teamsService.createTeams(teams));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTeam(@PathVariable Integer id, @RequestBody Teams teams) {
        return ResponseEntity.ok(this.teamsService.updateTeam(id, teams));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Integer id) {
        return ResponseEntity.ok(this.teamsService.deleteTeam(id));
    }
}
