/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.ActivityType;
import repository.ActivityTypeRepository;

import java.util.List;

public class ActivityTypeController {

    private final ActivityTypeRepository repo;

    public ActivityTypeController() {
        this.repo = new ActivityTypeRepository();
    }

    public List<ActivityType> listarTipos() {
        return repo.loadAll();
    }

    public void definirLimite(ActivityType tipo, String valor) throws Exception {

        if (tipo == null)
            throw new IllegalArgumentException("Selecione um tipo.");

        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException("Informe um limite.");

        int horas;

        try {
            horas = Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O limite deve ser numérico.");
        }

        if (horas < 0)
            throw new IllegalArgumentException("O limite não pode ser negativo.");

        if (horas > 200)
            throw new IllegalArgumentException("Acima das diretrizes do curso.");

        repo.updateLimit(tipo.getName(), horas);
    }
}

