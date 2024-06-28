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
        Team ger = new Team(1, "Германия", EmojiParser.parseToUnicode(":de:"));
        teams.add(ger);
        Team sco = new Team(2, "Шотландия", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F"));
        teams.add(sco);
        Team hun = new Team(3, "Венгрия", EmojiParser.parseToUnicode(":hu:"));
        teams.add(hun);
        Team swi = new Team(4, "Швейцария", EmojiParser.parseToUnicode(":ch:"));
        teams.add(swi);
        Team spa = new Team(5, "Испания", EmojiParser.parseToUnicode(":es:"));
        teams.add(spa);
        Team cro = new Team(6, "Хорватия", EmojiParser.parseToUnicode(":hr:"));
        teams.add(cro);
        Team ita = new Team(7, "Италия", EmojiParser.parseToUnicode(":it:"));
        teams.add(ita);
        Team alb = new Team(8, "Албания", EmojiParser.parseToUnicode(":al:"));
        teams.add(alb);
        Team pol = new Team(9, "Польша", EmojiParser.parseToUnicode(":pl:"));
        teams.add(pol);
        Team ned = new Team(10, "Нидерланды", EmojiParser.parseToUnicode(":nl:"));
        teams.add(ned);
        Team slo = new Team(11, "Словения", EmojiParser.parseToUnicode(":si:"));
        teams.add(slo);
        Team den = new Team(12, "Дания", EmojiParser.parseToUnicode(":dk:"));
        teams.add(den);
        Team ser = new Team(13, "Сербия", EmojiParser.parseToUnicode(":rs:"));
        teams.add(ser);
        Team eng = new Team(14, "Англия", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F"));
        teams.add(eng);
        Team rom = new Team(15, "Румыния", EmojiParser.parseToUnicode(":ro:"));
        teams.add(rom);
        Team ukr = new Team(16, "Украина", EmojiParser.parseToUnicode(":ua:"));
        teams.add(ukr);
        Team bel = new Team(17, "Бельгия", EmojiParser.parseToUnicode(":be:"));
        teams.add(bel);
        Team svk = new Team(18, "Словакия", EmojiParser.parseToUnicode(":sk:"));
        teams.add(svk);
        Team aus = new Team(19, "Австрия", EmojiParser.parseToUnicode(":at:"));
        teams.add(aus);
        Team fra = new Team(20, "Франция", EmojiParser.parseToUnicode(":fr:"));
        teams.add(fra);
        Team tur = new Team(21, "Турция", EmojiParser.parseToUnicode(":tr:"));
        teams.add(tur);
        Team geo = new Team(22, "Грузия", EmojiParser.parseToUnicode(":ge:"));
        teams.add(geo);
        Team por = new Team(23, "Португалия", EmojiParser.parseToUnicode(":pt:"));
        teams.add(por);
        Team cze = new Team(24, "Чехия", EmojiParser.parseToUnicode(":cz:"));
        teams.add(cze);
        teamRepository.saveAll(teams);
        log.info("In TeamsRepository saved all teams");

        if (matchRepository.findAll().isEmpty()) {
            List<Match> matches = new ArrayList<>();
            // group 1st round
            ZonedDateTime startTime1 = ZonedDateTime.of(2024, 6, 14, 22, 0, 0, 0, zone);
            matches.add(new Match(1, ger, sco, 0, 0, Instant.from(startTime1), 428747));

            ZonedDateTime startTime2 = ZonedDateTime.of(2024, 6, 15, 16, 0, 0, 0, zone);
            matches.add(new Match(2, hun, swi, 0, 0, Instant.from(startTime2), 428746));

            ZonedDateTime startTime3 = ZonedDateTime.of(2024, 6, 15, 19, 0, 0, 0, zone);
            matches.add(new Match(3, spa, cro, 0, 0, Instant.from(startTime3), 428753));

            ZonedDateTime startTime4 = ZonedDateTime.of(2024, 6, 15, 22, 0, 0, 0, zone);
            matches.add(new Match(4, ita, alb, 0, 0, Instant.from(startTime4), 428752));

            ZonedDateTime startTime5 = ZonedDateTime.of(2024, 6, 16, 16, 0, 0, 0, zone);
            matches.add(new Match(5, pol, ned, 0, 0, Instant.from(startTime5), 428765));

            ZonedDateTime startTime6 = ZonedDateTime.of(2024, 6, 16, 19, 0, 0, 0, zone);
            matches.add(new Match(6, slo, den, 0, 0, Instant.from(startTime6), 428758));

            ZonedDateTime startTime7 = ZonedDateTime.of(2024, 6, 16, 22, 0, 0, 0, zone);
            matches.add(new Match(7, ser, eng, 0, 0, Instant.from(startTime7), 428759));

            ZonedDateTime startTime8 = ZonedDateTime.of(2024, 6, 17, 16, 0, 0, 0, zone);
            matches.add(new Match(8, rom, ukr, 0, 0, Instant.from(startTime8), 428770));

            ZonedDateTime startTime9 = ZonedDateTime.of(2024, 6, 17, 19, 0, 0, 0, zone);
            matches.add(new Match(9, bel, svk, 0, 0, Instant.from(startTime9), 428771));

            ZonedDateTime startTime10 = ZonedDateTime.of(2024, 6, 17, 22, 0, 0, 0, zone);
            matches.add(new Match(10, aus, fra, 0, 0, Instant.from(startTime10), 428764));

            ZonedDateTime startTime11 = ZonedDateTime.of(2024, 6, 18, 19, 0, 0, 0, zone);
            matches.add(new Match(11, tur, geo, 0, 0, Instant.from(startTime11), 428776));

            ZonedDateTime startTime12 = ZonedDateTime.of(2024, 6, 18, 22, 0, 0, 0, zone);
            matches.add(new Match(12, por, cze, 0, 0, Instant.from(startTime12), 428777));

// group 2nd round
            ZonedDateTime startTime13 = ZonedDateTime.of(2024, 6, 19, 16, 0, 0, 0, zone);
            matches.add(new Match(13, cro, alb, 0, 0, Instant.from(startTime13), 428751));

            ZonedDateTime startTime14 = ZonedDateTime.of(2024, 6, 19, 19, 0, 0, 0, zone);
            matches.add(new Match(14, ger, hun, 0, 0, Instant.from(startTime14), 428744));

            ZonedDateTime startTime15 = ZonedDateTime.of(2024, 6, 19, 22, 0, 0, 0, zone);
            matches.add(new Match(15, sco, swi, 0, 0, Instant.from(startTime15), 428745));

            ZonedDateTime startTime16 = ZonedDateTime.of(2024, 6, 20, 16, 0, 0, 0, zone);
            matches.add(new Match(16, slo, ser, 0, 0, Instant.from(startTime16), 428756));

            ZonedDateTime startTime17 = ZonedDateTime.of(2024, 6, 20, 19, 0, 0, 0, zone);
            matches.add(new Match(17, den, eng, 0, 0, Instant.from(startTime17), 428757));

            ZonedDateTime startTime18 = ZonedDateTime.of(2024, 6, 20, 22, 0, 0, 0, zone);
            matches.add(new Match(18, spa, ita, 0, 0, Instant.from(startTime18), 428750));

            ZonedDateTime startTime19 = ZonedDateTime.of(2024, 6, 21, 16, 0, 0, 0, zone);
            matches.add(new Match(19, svk, ukr, 0, 0, Instant.from(startTime19), 428769));

            ZonedDateTime startTime20 = ZonedDateTime.of(2024, 6, 21, 19, 0, 0, 0, zone);
            matches.add(new Match(20, pol, aus, 0, 0, Instant.from(startTime20), 428763));

            ZonedDateTime startTime21 = ZonedDateTime.of(2024, 6, 21, 22, 0, 0, 0, zone);
            matches.add(new Match(21, ned, fra, 0, 0, Instant.from(startTime21), 428762));

            ZonedDateTime startTime22 = ZonedDateTime.of(2024, 6, 22, 16, 0, 0, 0, zone);
            matches.add(new Match(22, geo, cze, 0, 0, Instant.from(startTime22), 428775));

            ZonedDateTime startTime23 = ZonedDateTime.of(2024, 6, 22, 19, 0, 0, 0, zone);
            matches.add(new Match(23, tur, por, 0, 0, Instant.from(startTime23), 428774));

            ZonedDateTime startTime24 = ZonedDateTime.of(2024, 6, 22, 22, 0, 0, 0, zone);
            matches.add(new Match(24, bel, rom, 0, 0, Instant.from(startTime24), 428768));

// group 3rd round
            ZonedDateTime startTime25 = ZonedDateTime.of(2024, 6, 23, 22, 0, 0, 0, zone);
            matches.add(new Match(25, swi, ger, 0, 0, Instant.from(startTime25), 428743));

            ZonedDateTime startTime26 = ZonedDateTime.of(2024, 6, 23, 22, 0, 0, 0, zone);
            matches.add(new Match(26, sco, hun, 0, 0, Instant.from(startTime26), 428742));

            ZonedDateTime startTime27 = ZonedDateTime.of(2024, 6, 24, 22, 0, 0, 0, zone);
            matches.add(new Match(27, cro, ita, 0, 0, Instant.from(startTime27), 428749));

            ZonedDateTime startTime28 = ZonedDateTime.of(2024, 6, 24, 22, 0, 0, 0, zone);
            matches.add(new Match(28, alb, spa, 0, 0, Instant.from(startTime28), 428748));

            ZonedDateTime startTime29 = ZonedDateTime.of(2024, 6, 25, 19, 0, 0, 0, zone);
            matches.add(new Match(29, ned, aus, 0, 0, Instant.from(startTime29), 428761));

            ZonedDateTime startTime30 = ZonedDateTime.of(2024, 6, 25, 19, 0, 0, 0, zone);
            matches.add(new Match(30, fra, pol, 0, 0, Instant.from(startTime30), 428760));

            ZonedDateTime startTime31 = ZonedDateTime.of(2024, 6, 25, 22, 0, 0, 0, zone);
            matches.add(new Match(31, eng, slo, 0, 0, Instant.from(startTime31), 428755));

            ZonedDateTime startTime32 = ZonedDateTime.of(2024, 6, 25, 22, 0, 0, 0, zone);
            matches.add(new Match(32, den, ser, 0, 0, Instant.from(startTime32), 428754));

            ZonedDateTime startTime33 = ZonedDateTime.of(2024, 6, 26, 19, 0, 0, 0, zone);
            matches.add(new Match(33, svk, rom, 0, 0, Instant.from(startTime33), 428767));

            ZonedDateTime startTime34 = ZonedDateTime.of(2024, 6, 26, 19, 0, 0, 0, zone);
            matches.add(new Match(34, ukr, bel, 0, 0, Instant.from(startTime34), 428766));

            ZonedDateTime startTime35 = ZonedDateTime.of(2024, 6, 26, 22, 0, 0, 0, zone);
            matches.add(new Match(35, cze, tur, 0, 0, Instant.from(startTime35), 428772));

            ZonedDateTime startTime36 = ZonedDateTime.of(2024, 6, 26, 22, 0, 0, 0, zone);
            matches.add(new Match(36, geo, por, 0, 0, Instant.from(startTime36), 428773));

            // 1/8
            ZonedDateTime startTime37 = ZonedDateTime.of(2024, 6, 29, 19, 0, 0, 0, zone);
            matches.add(new Match(37, null, null, 0, 0, Instant.from(startTime37), 428791));

            ZonedDateTime startTime38 = ZonedDateTime.of(2024, 6, 29, 22, 0, 0, 0, zone);
            matches.add(new Match(38, null, null, 0, 0, Instant.from(startTime38), 428792));

            ZonedDateTime startTime39 = ZonedDateTime.of(2024, 6, 30, 19, 0, 0, 0, zone);
            matches.add(new Match(39, null, null, 0, 0, Instant.from(startTime39), 428789));

            ZonedDateTime startTime40 = ZonedDateTime.of(2024, 6, 30, 22, 0, 0, 0, zone);
            matches.add(new Match(40, null, null, 0, 0, Instant.from(startTime40), 428790));

            ZonedDateTime startTime41 = ZonedDateTime.of(2024, 7, 1, 19, 0, 0, 0, zone);
            matches.add(new Match(41, null, null, 0, 0, Instant.from(startTime41), 428787));

            ZonedDateTime startTime42 = ZonedDateTime.of(2024, 7, 1, 22, 0, 0, 0, zone);
            matches.add(new Match(42, null, null, 0, 0, Instant.from(startTime42), 428788));

            ZonedDateTime startTime43 = ZonedDateTime.of(2024, 7, 2, 19, 0, 0, 0, zone);
            matches.add(new Match(43, null, null, 0, 0, Instant.from(startTime43), 428786));

            ZonedDateTime startTime44 = ZonedDateTime.of(2024, 7, 2, 22, 0, 0, 0, zone);
            matches.add(new Match(44, null, null, 0, 0, Instant.from(startTime44), 428785));

// 1/4
            ZonedDateTime startTime45 = ZonedDateTime.of(2024, 7, 5, 19, 0, 0, 0, zone);
            matches.add(new Match(45, null, null, 0, 0, Instant.from(startTime45), 495401));

            ZonedDateTime startTime46 = ZonedDateTime.of(2024, 7, 5, 22, 0, 0, 0, zone);
            matches.add(new Match(46, null, null, 0, 0, Instant.from(startTime46), 495402));

            ZonedDateTime startTime47 = ZonedDateTime.of(2024, 7, 6, 19, 0, 0, 0, zone);
            matches.add(new Match(47, null, null, 0, 0, Instant.from(startTime47), 495403));

            ZonedDateTime startTime48 = ZonedDateTime.of(2024, 7, 6, 22, 0, 0, 0, zone);
            matches.add(new Match(48, null, null, 0, 0, Instant.from(startTime48), 495404));

            // 1/2
            ZonedDateTime startTime49 = ZonedDateTime.of(2024, 7, 9, 22, 0, 0, 0, zone);
            matches.add(new Match(49, null, null, 0, 0, Instant.from(startTime49), 428780));

            ZonedDateTime startTime50 = ZonedDateTime.of(2024, 7, 10, 22, 0, 0, 0, zone);
            matches.add(new Match(50, null, null, 0, 0, Instant.from(startTime50), 428779));

// final
            ZonedDateTime startTime51 = ZonedDateTime.of(2024, 7, 14, 22, 0, 0, 0, zone);
            matches.add(new Match(51, null, null, 0, 0, Instant.from(startTime51), 428778));
            matchRepository.saveAll(matches);
            log.info("In MatchesRepository saved all matches");
        }
    }
}