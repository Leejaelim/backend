@org.springframework.modulith.ApplicationModule(
    id = "realtime",
    allowedDependencies = {
        "shared-kernel :: *",
        "group-decision :: events",
        "group-decision :: model",
        "group-decision :: results",
        "group-decision :: api",
        "group-decision :: errors",
        "identity :: queries",
        "identity :: member-model"
    }
)
package matchuri.backend.realtime;
