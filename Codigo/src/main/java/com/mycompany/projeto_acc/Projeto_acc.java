package com.mycompany.projeto_acc;

import catalog.CoordinatorCatalog;
import catalog.SupervisorCatalog;
import catalog.StudentCatalog;
import controller.CoordinatorController;
import controller.SupervisorController;
import controller.StudentController;
import view.MainMenuView;

public class Projeto_acc {

    public static void main(String[] args) {
        // Instancia os catálogos (armazenamento em memória)
        CoordinatorCatalog coordinatorCatalog = new CoordinatorCatalog();
        SupervisorCatalog supervisorCatalog = new SupervisorCatalog();
        StudentCatalog studentCatalog = new StudentCatalog();

        // Cria os controladores com seus catálogos
        CoordinatorController coordinatorController = new CoordinatorController(coordinatorCatalog);
        SupervisorController supervisorController = new SupervisorController(supervisorCatalog);
        StudentController studentController = new StudentController(studentCatalog);

        // Abre o menu principal (interface gráfica)
        new MainMenuView(
            coordinatorController,
            supervisorController,
            studentController
        );
    }
}
