package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        final String sensorName = "Sensor2";

        registerSensor(sensorName);

        Random random = new Random();

        float minTemp = -15.0f;
        float maxTemp = 45.0f;

        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            sendMeasurement(random.nextFloat(minTemp, maxTemp), random.nextBoolean(), sensorName);
        }
    }

    private static void sendMeasurement(float nextFloat, boolean nextBoolean, String sensorName) {
        final String url = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", nextFloat);
        jsonData.put("raining", nextBoolean);
        jsonData.put("sensor", Map.of("name", sensorName));

        makePostRequestWithJsonData(jsonData, url);
    }

    private static void registerSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJsonData(jsonData, url);
    }

    private static void makePostRequestWithJsonData(Map<String, Object> jsonData, String url) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, httpHeaders);

        try{
            restTemplate.postForObject(url, request, String.class);
            System.out.println("Изменение успешно отправлено на сервер");
        }
        catch (HttpClientErrorException e){
            System.out.println("Ошибка");
            System.out.println(e.getMessage());
        }
    }
}
