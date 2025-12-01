/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import catalog.ActivityTypeCatalog;
import model.ActivityType;

import java.util.List;
import util.ValidatorUtil;

public class ActivityTypeController {

    private final ActivityTypeCatalog catalog;

    public ActivityTypeController() {
        this.catalog = new ActivityTypeCatalog();
    }

    public List<ActivityType> listTypes() {
        return catalog.getAll();
    }

    public void setHourLimit(ActivityType tipo, String valor) throws Exception {

    if (!ValidatorUtil.validateNotNull(tipo)) {
        throw new IllegalArgumentException("Selecione um tipo.");
    }

    int horas = ValidatorUtil.parseAndValidateHourLimit(valor);

    catalog.updateLimit(tipo.getName(), horas);
}


}
