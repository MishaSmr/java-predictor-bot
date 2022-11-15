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
        Team qat = new Team(1, "Катар", EmojiParser.parseToUnicode(":qa:"));
        teams.add(qat);
        Team ecu = new Team(2, "Эквадор", EmojiParser.parseToUnicode(":ec:"));
        teams.add(ecu);
        Team eng = new Team(3, "Англия", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F"));
        teams.add(eng);
        Team ira = new Team(4, "Иран", EmojiParser.parseToUnicode(":ir:"));
        teams.add(ira);
        Team sen = new Team(5, "Сенегал", EmojiParser.parseToUnicode(":sn:"));
        teams.add(sen);
        Team ned = new Team(6, "Нидерланды", EmojiParser.parseToUnicode(":nl:"));
        teams.add(ned);
        Team usa = new Team(7, "США", EmojiParser.parseToUnicode(":us:"));
        teams.add(usa);
        Team wal = new Team(8, "Уэльс", EmojiParser.parseToUnicode("\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F"));
        teams.add(wal);
        Team arg = new Team(9, "Аргентина", EmojiParser.parseToUnicode(":ar:"));
        teams.add(arg);
        Team sar = new Team(10, "Саудовская Аравия", EmojiParser.parseToUnicode("\uD83C\uDDF8\uD83C\uDDE6"));
        teams.add(sar);
        Team den = new Team(11, "Дания", EmojiParser.parseToUnicode(":dk:"));
        teams.add(den);
        Team tun = new Team(12, "Тунис", EmojiParser.parseToUnicode(":tn:"));
        teams.add(tun);
        Team mex = new Team(13, "Мексика", EmojiParser.parseToUnicode(":mx:"));
        teams.add(mex);
        Team pol = new Team(14, "Польша", EmojiParser.parseToUnicode(":pl:"));
        teams.add(pol);
        Team fra = new Team(15, "Франция", EmojiParser.parseToUnicode(":fr:"));
        teams.add(fra);
        Team aus = new Team(16, "Австралия", EmojiParser.parseToUnicode(":au:"));
        teams.add(aus);
        Team mar = new Team(17, "Марокко", EmojiParser.parseToUnicode(":ma:"));
        teams.add(mar);
        Team cro = new Team(18, "Хорватия", EmojiParser.parseToUnicode(":hr:"));
        teams.add(cro);
        Team ger = new Team(19, "Германия", EmojiParser.parseToUnicode(":de:"));
        teams.add(ger);
        Team jap = new Team(20, "Япония", EmojiParser.parseToUnicode(":jp:"));
        teams.add(jap);
        Team spa = new Team(21, "Испания", EmojiParser.parseToUnicode(":es:"));
        teams.add(spa);
        Team cor = new Team(22, "Коста-Рика", EmojiParser.parseToUnicode(":cr:"));
        teams.add(cor);
        Team bel = new Team(23, "Бельгия", EmojiParser.parseToUnicode(":be:"));
        teams.add(bel);
        Team can = new Team(24, "Канада", EmojiParser.parseToUnicode(":ca:"));
        teams.add(can);
        Team swi = new Team(25, "Швейцария", EmojiParser.parseToUnicode(":ch:"));
        teams.add(swi);
        Team cam = new Team(26, "Камерун", EmojiParser.parseToUnicode(":cm:"));
        teams.add(cam);
        Team uru = new Team(27, "Уругвай", EmojiParser.parseToUnicode(":uy:"));
        teams.add(uru);
        Team sko = new Team(28, "Южная Корея", EmojiParser.parseToUnicode(":kr:"));
        teams.add(sko);
        Team por = new Team(29, "Португалия", EmojiParser.parseToUnicode(":pt:"));
        teams.add(por);
        Team gha = new Team(30, "Гана", EmojiParser.parseToUnicode(":gh:"));
        teams.add(gha);
        Team bra = new Team(31, "Бразилия", EmojiParser.parseToUnicode(":br:"));
        teams.add(bra);
        Team ser = new Team(32, "Сербия", EmojiParser.parseToUnicode(":rs:"));
        teams.add(ser);
        teamRepository.saveAll(teams);
        log.info("In TeamsRepository saved all teams");

        if (matchRepository.findAll().isEmpty()) {
            List<Match> matches = new ArrayList<>();
            ZonedDateTime startTime1 = ZonedDateTime.of(2022, 11, 20, 19, 0, 0, 0, zone);
            matches.add(new Match(1, qat, ecu, 0, 0, Instant.from(startTime1)));

            ZonedDateTime startTime2 = ZonedDateTime.of(2022, 11, 21, 16, 0, 0, 0, zone);
            matches.add(new Match(2, eng, ira, 0, 0, Instant.from(startTime2)));

            ZonedDateTime startTime3 = ZonedDateTime.of(2022, 11, 21, 19, 0, 0, 0, zone);
            matches.add(new Match(3, sen, ned, 0, 0, Instant.from(startTime3)));

            ZonedDateTime startTime4 = ZonedDateTime.of(2022, 11, 21, 22, 0, 0, 0, zone);
            matches.add(new Match(4, usa, wal, 0, 0, Instant.from(startTime4)));

            ZonedDateTime startTime5 = ZonedDateTime.of(2022, 11, 22, 13, 0, 0, 0, zone);
            matches.add(new Match(5, arg, sar, 0, 0, Instant.from(startTime5)));

            ZonedDateTime startTime6 = ZonedDateTime.of(2022, 11, 22, 16, 0, 0, 0, zone);
            matches.add(new Match(6, den, tun, 0, 0, Instant.from(startTime6)));

            ZonedDateTime startTime7 = ZonedDateTime.of(2022, 11, 22, 19, 0, 0, 0, zone);
            matches.add(new Match(7, mex, pol, 0, 0, Instant.from(startTime7)));

            ZonedDateTime startTime8 = ZonedDateTime.of(2022, 11, 22, 22, 0, 0, 0, zone);
            matches.add(new Match(8, fra, aus, 0, 0, Instant.from(startTime8)));

            ZonedDateTime startTime9 = ZonedDateTime.of(2022, 11, 23, 13, 0, 0, 0, zone);
            matches.add(new Match(9, mar, cro, 0, 0, Instant.from(startTime9)));

            ZonedDateTime startTime10 = ZonedDateTime.of(2022, 11, 23, 16, 0, 0, 0, zone);
            matches.add(new Match(10, ger, jap, 0, 0, Instant.from(startTime10)));

            ZonedDateTime startTime11 = ZonedDateTime.of(2022, 11, 23, 19, 0, 0, 0, zone);
            matches.add(new Match(11, spa, cor, 0, 0, Instant.from(startTime11)));

            ZonedDateTime startTime12 = ZonedDateTime.of(2022, 11, 23, 22, 0, 0, 0, zone);
            matches.add(new Match(12, bel, can, 0, 0, Instant.from(startTime12)));

            ZonedDateTime startTime13 = ZonedDateTime.of(2022, 11, 24, 13, 0, 0, 0, zone);
            matches.add(new Match(13, swi, cam, 0, 0, Instant.from(startTime13)));

            ZonedDateTime startTime14 = ZonedDateTime.of(2022, 11, 24, 16, 0, 0, 0, zone);
            matches.add(new Match(14, uru, sko, 0, 0, Instant.from(startTime14)));

            ZonedDateTime startTime15 = ZonedDateTime.of(2022, 11, 24, 19, 0, 0, 0, zone);
            matches.add(new Match(15, por, gha, 0, 0, Instant.from(startTime15)));

            ZonedDateTime startTime16 = ZonedDateTime.of(2022, 11, 24, 22, 0, 0, 0, zone);
            matches.add(new Match(16, bra, ser, 0, 0, Instant.from(startTime16)));


            ZonedDateTime startTime64 = ZonedDateTime.of(2022, 12, 18, 18, 0, 0, 0, zone);
            matches.add(new Match(64, null, null, 0, 0, Instant.from(startTime64)));
            matchRepository.saveAll(matches);
            log.info("In MatchesRepository saved all matches");
        }
    }
}
