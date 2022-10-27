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
        Team qat = new Team(1, "тестЛоко", EmojiParser.parseToUnicode(":qa:"));
        teams.add(qat);
        Team ecu = new Team(2, "тестЦСКА", EmojiParser.parseToUnicode(":ec:"));
        teams.add(ecu);
        Team eng = new Team(3, "тестБрайтон", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F"));
        teams.add(eng);
        Team ira = new Team(4, "тестЧелси", EmojiParser.parseToUnicode(":ir:"));
        teams.add(ira);
        Team sen = new Team(5, "тест Порту", EmojiParser.parseToUnicode(":sn:"));
        teams.add(sen);
        Team ned = new Team(6, "тестАтлетико", EmojiParser.parseToUnicode(":nl:"));
        teams.add(ned);
        Team usa = new Team(7, "тестБайер", EmojiParser.parseToUnicode(":us:"));
        teams.add(usa);
        Team wal = new Team(8, "тестБрюгге", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F"));
        teams.add(wal);
        Team arg = new Team(9, "тестЛестер", EmojiParser.parseToUnicode(":ar:"));
        teams.add(arg);
        Team sar = new Team(10, "тестМанСити", EmojiParser.parseToUnicode("\uD83C\uDDF8\uD83C\uDDE6"));
        teams.add(sar);
        Team den = new Team(11, "тестКраснодар", EmojiParser.parseToUnicode(":dk:"));
        teams.add(den);
        Team tun = new Team(12, "тестЗенит", EmojiParser.parseToUnicode(":tn:"));
        teams.add(tun);
        Team mex = new Team(13, "тестКрылья", EmojiParser.parseToUnicode(":mx:"));
        teams.add(mex);
        Team pol = new Team(14, "тестПариСЖ", EmojiParser.parseToUnicode(":pl:"));
        teams.add(pol);
        teamRepository.saveAll(teams);
        log.info("In TeamsRepository saved all teams");

        List<Match> matches = new ArrayList<>();
        ZonedDateTime startTime1 = ZonedDateTime.of(2022, 10, 29, 19, 00, 0, 0, zone);
        matches.add(new Match(1, qat, ecu, 0, 0, Instant.from(startTime1)));

        ZonedDateTime startTime2 = ZonedDateTime.of(2022, 10, 29, 17, 0, 0, 0, zone);
        matches.add(new Match(2, eng, ira, 0, 0, Instant.from(startTime2)));

        ZonedDateTime startTime3 = ZonedDateTime.of(2022, 11, 1, 20, 45, 0, 0, zone);
        matches.add(new Match(3, sen, ned, 0, 0, Instant.from(startTime3)));

        ZonedDateTime startTime4 = ZonedDateTime.of(2022, 11, 1, 20, 45, 0, 0, zone);
        matches.add(new Match(4, usa, wal, 0, 0, Instant.from(startTime4)));

        ZonedDateTime startTime5 = ZonedDateTime.of(2022, 10, 29, 14, 30, 0, 0, zone);
        matches.add(new Match(5, arg, sar, 0, 0, Instant.from(startTime5)));

        ZonedDateTime startTime6 = ZonedDateTime.of(2022, 10, 30, 19, 30, 0, 0, zone);
        matches.add(new Match(6, den, tun, 0, 0, Instant.from(startTime6)));

        ZonedDateTime startTime7 = ZonedDateTime.of(2022, 10, 30, 14, 00, 0, 0, zone);
        matches.add(new Match(7, mex, pol, 0, 0, Instant.from(startTime7)));



        ZonedDateTime startTime64 = ZonedDateTime.of(2022, 12, 18, 18, 0, 0, 0, zone);
        matches.add(new Match(64, null, null, 0, 0, Instant.from(startTime64)));
        matchRepository.saveAll(matches);
        log.info("In MatchesRepository saved all matches");
    }
}
