@org.springframework.modulith.ApplicationModule(
    id = "identity",
    allowedDependencies = {
        "shared-kernel :: *",
        "catalog :: model",
        "catalog :: queries",
        "media :: model",
        "media :: queries",
        "media :: tools",
        "media :: events",
        "media :: errors"
    }
)
package matchuri.backend.identity;
