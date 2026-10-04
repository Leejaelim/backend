@org.springframework.modulith.ApplicationModule(
    id = "group-decision",
    allowedDependencies = {
        "shared-kernel :: *",
        "identity :: member-model",
        "identity :: queries",
        "identity :: member-query",
        "identity :: events",
        "catalog :: model",
        "catalog :: queries",
        "catalog :: thumbnail",
        "media :: tools",
        "media :: model",
        "recommendation :: algorithms",
        "recommendation :: algorithm-input",
        "recommendation :: algorithm-output",
        "recommendation :: context"
    }
)
package matchuri.backend.groupdecision;
