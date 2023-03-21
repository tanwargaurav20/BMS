package com.sapient.bms.it;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerTest {

    private final String createMovieRequestPayload =
            "{\n" +
                    "    \"title\": \"RRR\",\n" +
                    "    \"description\": \"RRR desc\",\n" +
                    "    \"language\": \"Hindi\",\n" +
                    "    \"genre\": \"thriller\",\n" +
                    "    \"duration\": 120,\n" +
                    "    \"price\": \"500\"\n" +
                    "}\n";

    private final String createTheatreRequestPayload =
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
    MockMvc mockMvc;

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

        mockMvc.perform(MockMvcRequestBuilders.post("/theatre")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTheatreRequestPayload));

        mockMvc.perform(MockMvcRequestBuilders.post("/movie")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .param("screenId", "1")
                .content(createMovieRequestPayload));

        mockMvc.perform(MockMvcRequestBuilders.post("/discount")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .param("screenId", "1")
                .content("{\n" +
                        "            \"locationId\": 1,\n" +
                        "                \"theatreId\": 1,\n" +
                        "                \"discountCode\": \"20 off on 3rd\",\n" +
                        "                \"discountPercentage\": 20,\n" +
                        "                \"minQuantity\": 3,\n" +
                        "                \"active\":true\n" +
                        "        }"));


    }

    @Test
    void testBookSeats() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("screenId", "1")
                        .content("{\n" +
                                "    \"screenId\": \"1\",\n" +
                                "    \"movieId\": \"1\",\n" +
                                "    \"seats\": [{\"id\": 1}, {\"id\":2}],\n" +
                                "    \"discountCode\": \"20 off on 3rd\"\n" +
                                "}\n"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testPayBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/bookings/1/pay")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
