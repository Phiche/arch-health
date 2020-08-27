package ru.tinkoff.bpm.dossier;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ru.tinkoff.bpm.dossier");

        noClasses()
            .that()
                .resideInAnyPackage("ru.tinkoff.bpm.dossier.service..")
            .or()
                .resideInAnyPackage("ru.tinkoff.bpm.dossier.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..ru.tinkoff.bpm.dossier.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
