package com.github.zanini.openapi.codegen;

import com.github.zanini.openapi.client.ApiClient;
import com.github.zanini.openapi.client.Configuration;
import com.github.zanini.openapi.client.DefaultApi;
import com.github.zanini.openapi.client.DefaultApiImpl;
import com.github.zanini.openapi.client.model.NewPet;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

// see: https://vertx.io/docs/3.9.5/vertx-junit5/java/
@ExtendWith(VertxExtension.class)
class PetStoreAPITest {

    @Test
    void addPetTest(final Vertx vertx, final VertxTestContext testContext) {
        // setup
        JsonObject config = new JsonObject();
        final ApiClient apiClient = Configuration.setupDefaultApiClient(vertx, config);
        final DefaultApi api = new DefaultApiImpl(apiClient);
        // call
        final NewPet snoopy = new NewPet();
        snoopy.setName("Snoopy");
        snoopy.setTag("snoopy");
        api.addPet(snoopy, testContext.succeeding(response -> testContext.verify(() -> {
            assertThat(response, notNullValue());
            assertThat(response.getId(), greaterThan(0L));
            testContext.completeNow();
        })));
    }
}
