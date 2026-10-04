@org.springframework.modulith.ApplicationModule(
    id = "backend-app",
    allowedDependencies = {
        "shared-kernel :: *",
        "media :: *",
        "catalog :: *",
        "identity :: *",
        "recommendation :: *",
        "group-decision :: *",
        "realtime :: *"
    }
)
package matchuri.backend.application;
