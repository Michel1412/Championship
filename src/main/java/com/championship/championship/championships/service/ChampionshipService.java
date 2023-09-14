package com.championship.championship.championships.service;

import com.championship.championship.championships.Championship;
import com.championship.championship.championships.repository.ChampionshipRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Calendar;

@Service
public class ChampionshipService {

    private final ChampionshipRepository championshipRepository;

    public ChampionshipService(ChampionshipRepository championshipRepository) {
        this.championshipRepository = championshipRepository;
    }

    public Object createChampionship(Championship championship) {
        this.validateChampionship(championship);
        return ResponseEntity.ok(this.championshipRepository.save(championship));
    }

    public Object startChampionship(Integer id) {
        this.validateExistsChampionship(id);
        this.championshipRepository.startChampionship(id);
        return ResponseEntity.ok(this.championshipRepository.findById(id));
    }

    public Object finishChampionship(Integer id) {
        this.validateExistsChampionship(id);
        this.championshipRepository.finishChampionship(id);
        return ResponseEntity.ok(this.championshipRepository.findById(id));
    }

    private void validateExistsChampionship(Integer id) {
        if (!this.championshipRepository.existsById(id)) {
            throw new RuntimeException("Campeonato não encontrado!");
        }
    }

    private void validateChampionship(Championship championship) {
        this.validateDateChampionship(championship);
        this.validateDateAndNameChampionship(championship);
    }

    private void validateDateAndNameChampionship(Championship championship) {
        if (this.championshipRepository.countChampionshipByNameAndYear(championship.getChampionshipName(),championship.getChampionshipYear())) {
            throw new RuntimeException("Esse campeonato já foi criado no ano de " + championship.getChampionshipYear());
        }
    }

    private void validateDateChampionship(Championship championship) {
        Calendar date = Calendar.getInstance();
        if (championship.getChampionshipYear() < date.get(Calendar.YEAR)) {
            throw new RuntimeException("Não é possível criar um campeonato nesse ano!");
        }
    }
}
