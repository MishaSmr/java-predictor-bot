package com.fchat.bot.predictorbot.service;

import com.fchat.bot.predictorbot.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@PropertySource("application.properties")
public class ApiService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final PredictionRepository predictionRepository;
    @Value("${api.token}")
    String token;

    public ApiService(UserRepository userRepository, MatchRepository matchRepository, PredictionRepository predictionRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.predictionRepository = predictionRepository;
    }

    public Integer[] getScore(Integer id) {
        JsonObject jsonObjectMatch = sendRequest(String.valueOf(id));
        if (jsonObjectMatch != null) {
            String status = jsonObjectMatch.get("status").getAsString();
            String duration = jsonObjectMatch.getAsJsonObject("score").get("duration").getAsString();
            JsonObject jsonObjectScore = jsonObjectMatch.getAsJsonObject("score");
            if (status.equals("FINISHED") && duration.equals("REGULAR")) {
                int homeScore = jsonObjectScore.getAsJsonObject("fullTime").get("home").getAsInt();
                int awayScore = jsonObjectScore.getAsJsonObject("fullTime").get("away").getAsInt();
                return new Integer[]{homeScore, awayScore};
            } else if (status.equals("FINISHED") || status.equals("EXTRA_TIME") || status.equals("PENALTY_SHOOTOUT") ||
                    (status.equals("IN_PLAY") && duration.equals("EXTRA_TIME")) ||
                    (status.equals("IN_PLAY") && duration.equals("PENALTY_SHOOTOUT"))) {
                int homeScore = jsonObjectScore.getAsJsonObject("regularTime").get("home").getAsInt();
                int awayScore = jsonObjectScore.getAsJsonObject("regularTime").get("away").getAsInt();
                return new Integer[]{homeScore, awayScore};
            } else if (status.equals("POSTPONED") || status.equals("CANCELLED")) {
                return new Integer[]{999, 999};
            }
        }
        return null;
    }

    @Transactional
    public void putScore(int apiId, Integer[] score) {
        Match match = matchRepository.findByApiId(apiId);
        match.setScores1(score[0]);
        match.setScores2(score[1]);
        matchRepository.save(match);
        log.info("Match patched: " + match.getMatchId());
    }

    @Transactional
    public void calculatePoints(int apiId) {
        Match match = matchRepository.findByApiId(apiId);
        List<Prediction> matchPredictions = predictionRepository.findByMatch(match.getMatchId());
        for (Prediction p : matchPredictions) {
            User user = p.getUser();
            if (p.getScores1() == match.getScores1() && p.getScores2() == match.getScores2()) {
                user.setPoints(user.getPoints() + 10);
                user.setExactPred(user.getExactPred() + 1);
                p.setPoints(10);
            } else if ((p.getScores1() - p.getScores2()) == (match.getScores1() - match.getScores2())) {
                user.setPoints(user.getPoints() + 6);
                p.setPoints(6);
            } else if ((p.getScores1() > p.getScores2() && match.getScores1() > match.getScores2()) ||
                    (p.getScores1() < p.getScores2() && match.getScores1() < match.getScores2())) {
                if (p.getScores1() == match.getScores1() || p.getScores2() == match.getScores2()) {
                    user.setPoints(user.getPoints() + 5);
                    p.setPoints(5);
                } else {
                    user.setPoints(user.getPoints() + 3);
                    p.setPoints(3);
                }
            } else {
                p.setPoints(0);
            }
            userRepository.save(user);
            predictionRepository.save(p);
        }
        log.info("Calculating points for match id=: " + match.getMatchId());
    }


    public JsonObject sendRequest(String id) {
        HttpClient client = HttpClient.newHttpClient();
        URI url2 = URI.create("https://api.football-data.org/v4/matches/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url2)
                .header("X-Auth-Token", token)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем, успешно ли обработан запрос
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
                    log.error("Ответ от сервера не соответствует ожидаемому.");
                    return null;
                }

                return jsonElement.getAsJsonObject();
            } else {
                log.error("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            log.error("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            return null;
        }
    }
}
