package com.championship.championship.championships;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "championship")
public class Championship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer championshipId;

    @Column(name = "name")
    private String championshipName;

    @Column(name = "year")
    private Integer championshipYear;

    @Column(name = "started")
    private boolean championshipStarted;

    @Column(name = "finish")
    private boolean championshipFinished;

}
