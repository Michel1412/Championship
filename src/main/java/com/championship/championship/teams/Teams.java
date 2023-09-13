package com.championship.championship.teams;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "teams")
public class Teams {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(name = "team_name")
    public String name;
}
