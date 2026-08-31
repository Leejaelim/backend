package matchuri.backend.api.group.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Set;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.stereotype.Component;

/** OpenAPI 3.1 requires a null schema instead of the 3.0 nullable flag. */
@Component
public class GroupOpenApiCustomizer implements OpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        allowNull(openApi, "GroupRecommendationSummaryResponse", "startedAt");
        allowNull(openApi, "GroupRecommendationSummaryResponse", "endedAt");
    }

    private void allowNull(OpenAPI openApi, String schemaName, String propertyName) {
        if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
            return;
        }
        Schema<?> schema = openApi.getComponents().getSchemas().get(schemaName);
        if (schema == null || schema.getProperties() == null) {
            return;
        }
        schema.getProperties().computeIfPresent(propertyName, (name, property) ->
                new ComposedSchema()
                        .addAnyOfItem(property)
                        .addAnyOfItem(new Schema<>().types(Set.of("null"))));
    }
}
