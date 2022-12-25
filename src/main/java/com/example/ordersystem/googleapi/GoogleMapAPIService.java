package com.example.ordersystem.googleapi;

import com.example.ordersystem.payload.GetDistanceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GoogleMapAPIService {
    private static final String API_ENDPOINT = "https://routes.googleapis.com/directions/v2:computeRoutes";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate googleMapHTTPClient;

    public Optional<Integer> getDistance(Double originLatitude, Double originLongitude, Double destinationLatitude, Double destinationLongitude) {
        Map<String, Object> map = new HashMap<>();
        map.put("origin", Map.of("location", Map.of("latLng", Map.of("latitude", originLatitude, "longitude", originLongitude))));
        map.put("destination", Map.of("location", Map.of("latLng", Map.of("latitude", destinationLatitude, "longitude", destinationLongitude))));
        map.put("travelMode", "DRIVE");

        HttpEntity<String> requestEntity = new HttpEntity<>(new JSONObject(map).toString());

        ResponseEntity<String> responseEntity = googleMapHTTPClient.exchange(API_ENDPOINT, HttpMethod.POST, requestEntity, String.class);

        var responseJsonString = responseEntity.getBody();

        GetDistanceResponse response;

        try {
            response = objectMapper.readValue(responseJsonString, GetDistanceResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("convert json body error");
        }

        if (response == null || response.routes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(response.routes.get(0).distanceMeters);
    }
}
