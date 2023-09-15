package com.championship.championship.matches.matchesDTO;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
public class MatchesDTO {

    @NotNull
    private Integer homeTeamId;

    @NotNull
    private Integer visitingTeamId;

    private Integer championshipId;

    @NotNull
    private Calendar matchDate;
}
