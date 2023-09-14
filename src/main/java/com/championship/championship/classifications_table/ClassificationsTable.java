package com.championship.championship.classifications_table;

import com.championship.championship.championships.Championship;
import com.championship.championship.teams.Teams;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "classification_table")
public class ClassificationsTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "team")
    private Teams teams;

    @ManyToOne
    @JoinColumn(name = "championship")
    private Championship championship;

    @Column(name = "points")
    private Integer points;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "draw")
    private Integer draw;

    @Column(name = "loses")
    private Integer loses;

    @Column(name = "goals_scored")
    private Integer goalsScored;

    @Column(name = "goals_conceded")
    private Integer goalsConceded;

    @Column(name = "goals_difference")
    private Integer goalsdifference;

}
