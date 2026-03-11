package com.example.van_loading_optimiser.Controllers;

import com.example.van_loading_optimiser.Dtos.OptimizationRequestDto;
import com.example.van_loading_optimiser.Dtos.ShipmentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:Migrations"
})
class OptimizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldOptimizeSuccessfully() throws Exception {
        OptimizationRequestDto request = new OptimizationRequestDto(
                15,
                List.of(
                        new ShipmentDto("Parcel A", 5, 120),
                        new ShipmentDto("Parcel B", 10, 200),
                        new ShipmentDto("Parcel C", 3, 80),
                        new ShipmentDto("Parcel D", 8, 160)
                )
        );

        mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").exists())
                .andExpect(jsonPath("$.totalVolume", is(15)))
                .andExpect(jsonPath("$.totalRevenue", is(320)))
                .andExpect(jsonPath("$.selectedShipments", hasSize(2)));
    }

    @Test
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        String invalidJson = """
                {
                  "maxVolume": -1,
                  "availableShipments": [
                    {
                      "name": "",
                      "volume": 0,
                      "revenue": -2
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.details").isArray());
    }


    @Test
    void shouldGetOptimizationById() throws Exception {
        OptimizationRequestDto request = new OptimizationRequestDto(
                15,
                List.of(
                        new ShipmentDto("Parcel A", 5, 120),
                        new ShipmentDto("Parcel B", 10, 200),
                        new ShipmentDto("Parcel C", 3, 80),
                        new ShipmentDto("Parcel D", 8, 160)
                )
        );

        String response = mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String requestId = objectMapper.readTree(response).get("requestId").asText();

        mockMvc.perform(get("/api/v1/optimizations/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId", is(requestId)))
                .andExpect(jsonPath("$.totalVolume", is(15)))
                .andExpect(jsonPath("$.totalRevenue", is(320)))
                .andExpect(jsonPath("$.selectedShipments", hasSize(2)));
    }

    @Test
    void shouldGetAllOptimizations() throws Exception {
        OptimizationRequestDto firstRequest = new OptimizationRequestDto(
                15,
                List.of(
                        new ShipmentDto("Parcel A", 5, 120),
                        new ShipmentDto("Parcel B", 10, 200)
                )
        );

        OptimizationRequestDto secondRequest = new OptimizationRequestDto(
                10,
                List.of(
                        new ShipmentDto("Parcel X", 3, 50),
                        new ShipmentDto("Parcel Y", 2, 70)
                )
        );

        mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/optimizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/optimizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].requestId", notNullValue()))
                .andExpect(jsonPath("$[1].requestId", notNullValue()));
    }

    @Test
    void shouldReturnNotFoundWhenOptimizationDoesNotExist() throws Exception {
        String randomId = "11111111-1111-1111-1111-111111111111";

        mockMvc.perform(get("/api/v1/optimizations/{requestId}", randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not found")))
                .andExpect(jsonPath("$.details").isArray());
    }

}