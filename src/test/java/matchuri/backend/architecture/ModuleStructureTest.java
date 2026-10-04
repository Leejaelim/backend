package matchuri.backend.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import matchuri.backend.BackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleStructureTest {
    @Test
    void eightClosedModulesHaveNoCyclesOrInternalAccess() {
        ApplicationModules modules = ApplicationModules.of(BackendApplication.class,
                ImportOption.Predefined.DO_NOT_INCLUDE_TESTS);
        assertThat(modules.stream().map(module -> module.getIdentifier().toString()).collect(Collectors.toSet()))
                .isEqualTo(Set.of("backend-app", "identity", "catalog", "recommendation", "group-decision", "media", "realtime", "shared-kernel"));
        modules.stream().forEach(module -> assertThat(module.isOpen()).isFalse());
        modules.verify();
    }

    @Test
    void repositoriesArePrivateExceptForApplicationSeedComposition() {
        var classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("matchuri.backend");
        for (var type : classes) {
            if (!type.getName().equals(BackendApplication.class.getName())) {
                assertThat(type.getPackageName()).as("Production type belongs to one of the eight modules: %s", type.getName())
                        .matches("matchuri\\.backend\\.(application|identity|catalog|recommendation|groupdecision|media|realtime|shared)(\\..*)?");
            }
            String sourceModule = moduleOf(type.getPackageName());
            for (var dependency : type.getDirectDependenciesFromSelf()) {
                var target = dependency.getTargetClass();
                String targetModule = moduleOf(target.getPackageName());
                if (target.getPackageName().contains(".repository") && !targetModule.isEmpty()
                        && !sourceModule.equals(targetModule)) {
                    assertThat(type.getPackageName()).as(dependency.getDescription())
                            .startsWith("matchuri.backend.application.seed");
                }
            }
        }
    }

    private String moduleOf(String packageName) {
        String prefix = "matchuri.backend.";
        return packageName.startsWith(prefix) ? packageName.substring(prefix.length()).split("\\.")[0] : "";
    }
}
