package com.fchat.bot.predictorbot.config;

import com.fchat.bot.predictorbot.model.Match;
import com.fchat.bot.predictorbot.model.MatchRepository;
import com.fchat.bot.predictorbot.model.Team;
import com.fchat.bot.predictorbot.model.TeamRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;


    @EventListener({ApplicationReadyEvent.class})
    public void saveData() {
        ZoneId zone = ZoneId.of("Europe/Moscow");
        List<Team> teams = new ArrayList<>();
        Team qat = new Team(1, "тестКрылья", EmojiParser.parseToUnicode(":qa:"));
        teams.add(qat);
        Team ecu = new Team(2, "тестРостов", EmojiParser.parseToUnicode(":ec:"));
        teams.add(ecu);
        Team eng = new Team(3, "тестТорпедо", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F"));
        teams.add(eng);
        Team ira = new Team(4, "тестЗенит", EmojiParser.parseToUnicode(":ir:"));
        teams.add(ira);
        Team sen = new Team(5, "тестЛоко", EmojiParser.parseToUnicode(":sn:"));
        teams.add(sen);
        Team ned = new Team(6, "тестСпартак", EmojiParser.parseToUnicode(":nl:"));
        teams.add(ned);
        Team usa = new Team(7, "тестНьюкасл", EmojiParser.parseToUnicode(":us:"));
        teams.add(usa);
        Team wal = new Team(8, "тестЧелси", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F"));
        teams.add(wal);
        Team arg = new Team(9, "тестАталанта", EmojiParser.parseToUnicode(":ar:"));
        teams.add(arg);
        Team sar = new Team(10, "тестИнтер", EmojiParser.parseToUnicode("\uD83C\uDDF8\uD83C\uDDE6"));
        teams.add(sar);
        Team den = new Team(11, "тестХимки", EmojiParser.parseToUnicode(":dk:"));
        teams.add(den);
        Team tun = new Team(12, "тестСочи", EmojiParser.parseToUnicode(":tn:"));
        teams.add(tun);
        Team mex = new Team(13, "тестФулхэм", EmojiParser.parseToUnicode(":mx:"));
        teams.add(mex);
        Team pol = new Team(14, "тестМанЮнайтед", EmojiParser.parseToUnicode(":pl:"));
        teams.add(pol);
        Team fra = new Team(15, "Франция", EmojiParser.parseToUnicode(":fr:"));
        teams.add(fra);
        Team aus = new Team(16, "Австралия", EmojiParser.parseToUnicode(":au:"));
        teams.add(aus);
        teamRepository.saveAll(teams);
        log.info("In TeamsRepository saved all teams");

        if (matchRepository.findAll().isEmpty()) {
            List<Match> matches = new ArrayList<>();
            ZonedDateTime startTime1 = ZonedDateTime.of(2022, 11, 12, 14, 0, 0, 0, zone);
            matches.add(new Match(1, qat, ecu, 0, 0, Instant.from(startTime1)));

            ZonedDateTime startTime2 = ZonedDateTime.of(2022, 11, 12, 16, 30, 0, 0, zone);
            matches.add(new Match(2, eng, ira, 0, 0, Instant.from(startTime2)));

            ZonedDateTime startTime3 = ZonedDateTime.of(2022, 11, 12, 19, 30, 0, 0, zone);
            matches.add(new Match(3, sen, ned, 0, 0, Instant.from(startTime3)));

            ZonedDateTime startTime4 = ZonedDateTime.of(2022, 11, 12, 20, 30, 0, 0, zone);
            matches.add(new Match(4, usa, wal, 0, 0, Instant.from(startTime4)));

            ZonedDateTime startTime5 = ZonedDateTime.of(2022, 11, 13, 14, 30, 0, 0, zone);
            matches.add(new Match(5, arg, sar, 0, 0, Instant.from(startTime5)));

            ZonedDateTime startTime6 = ZonedDateTime.of(2022, 11, 13, 16, 30, 0, 0, zone);
            matches.add(new Match(6, den, tun, 0, 0, Instant.from(startTime6)));

            ZonedDateTime startTime7 = ZonedDateTime.of(2022, 11, 13, 19, 30, 0, 0, zone);
            matches.add(new Match(7, mex, pol, 0, 0, Instant.from(startTime7)));

            ZonedDateTime startTime8 = ZonedDateTime.of(2022, 11, 14, 23, 30, 0, 0, zone);
            matches.add(new Match(8, fra, aus, 0, 0, Instant.from(startTime8)));


            ZonedDateTime startTime64 = ZonedDateTime.of(2022, 12, 18, 18, 0, 0, 0, zone);
            matches.add(new Match(64, null, null, 0, 0, Instant.from(startTime64)));
            matchRepository.saveAll(matches);
            log.info("In MatchesRepository saved all matches");
        }
    }
}
