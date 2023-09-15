package com.championship.championship.matches;

import com.championship.championship.championships.Championship;
import com.championship.championship.teams.Teams;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "matches")
public class Matches {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "home_team")
    private Teams homeTeam;

    @ManyToOne
    @JoinColumn(name = "visiting_team")
    private Teams visitingTeam;

    @ManyToOne
    @JoinColumn(name = "championship")
    private Championship championshipId;

    @Column(name = "home_team_goals")
    private Integer homeTeamGoals;

    @Column(name = "visiting_team_goals")
    private Integer vistingTeamGoals;

    @Column(name = "match_date")
    private Integer matchDate;

    @Column(name = "match_started")
    private Integer matchStarted;

    @Column(name = "match_finish")
    private Integer matchFinished;
}
