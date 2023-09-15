package com.championship.championship.classifications_table.classificationsTableDTO;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClassificationTableDTO {

    @NotNull
    private Integer[] listTeams;
}
