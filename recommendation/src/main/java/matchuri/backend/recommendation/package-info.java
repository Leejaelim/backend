@org.springframework.modulith.ApplicationModule(
    id = "recommendation",
    allowedDependencies = {
        "shared-kernel :: *",
        "identity :: member-model",
        "identity :: queries",
        "identity :: taste-lookup",
        "catalog :: model",
        "catalog :: queries",
        "catalog :: results",
        "catalog :: thumbnail",
        "media :: tools",
        "media :: model"
    }
)
package matchuri.backend.recommendation;
