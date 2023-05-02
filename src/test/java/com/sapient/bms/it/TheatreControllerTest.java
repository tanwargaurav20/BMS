package com.sapient.bms.it;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TheatreControllerTest {

    private static final String createTheatreRequestPayload =
            "{\n" +
                    "    \"name\": \"PVR\",\n" +
                    "    \"location\": {\n" +
                    "        \"id\": 1,\n" +
                    "        \"address\": \"Dwarka Vegas mall\",\n" +
                    "        \"city\": \"delhi\",\n" +
                    "        \"state\": \"delhi\",\n" +
                    "        \"country\": \"india\"\n" +
                    "    },\n" +
                    "    \"screens\": [\n" +
                    "        {\n" +
                    "            \"number\": 1,\n" +
                    "            \"capacity\": 30,\n" +
                    "            \"showTime\": \"18/03/2023 09:00:00 PM\",\n" +
                    "            \"movies\": [],\n" +
                    "            \"seats\": [\n" +
                    "                {\n" +
                    "                    \"rowNumber\": 1,\n" +
                    "                    \"number\": 1,\n" +
                    "                    \"available\": true,\n" +
                    "                    \"seatType\": \"REGULAR\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"rowNumber\": 2,\n" +
                    "                    \"number\": 2,\n" +
                    "                    \"available\": true,\n" +
                    "                    \"seatType\": \"PREMIUM\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"rowNumber\": 3,\n" +
                    "                    \"number\": 1,\n" +
                    "                    \"available\": true,\n" +
                    "                    \"seatType\": \"LUXURY\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    public void setup() throws Exception {
        //Register user
        mockMvc.perform(MockMvcRequestBuilders.post("/user")

                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"email\": \"admin@xyz.com\",\n" +
                        "    \"password\": \"admin\",\n" +
                        "    \"role\": \"admin\"\n" +
                        "    \n" +
                        "}"));

        //Authenticate user to get the token
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")

                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\": \"admin@xyz.com\",\n" +
                        "    \"password\": \"admin\"\n" +
                        "    \n" +
                        "}")).andReturn();

        token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    @Test
    void testCreateTheatre() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/theatre")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTheatreRequestPayload))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testGetTheatreByLocationMovieNameAndDate() throws Exception {
        testCreateTheatre();
        mockMvc.perform(MockMvcRequestBuilders.get("/theatre/criteria")
                        .param("locationId", "1")
                        .param("movieTitle", "RRR")
                        .param("date", "2019-12-31")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
