@org.springframework.modulith.ApplicationModule(
    id = "catalog",
    allowedDependencies = {
        "shared-kernel :: *",
        "media :: model",
        "media :: asset-store",
        "media :: storage",
        "media :: tools",
        "media :: api"
    }
)
package matchuri.backend.catalog;
