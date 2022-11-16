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
            // group 1st round
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

            // group 2nd round
            ZonedDateTime startTime17 = ZonedDateTime.of(2022, 11, 25, 13, 0, 0, 0, zone);
            matches.add(new Match(17, wal, ira, 0, 0, Instant.from(startTime17)));

            ZonedDateTime startTime18 = ZonedDateTime.of(2022, 11, 25, 16, 0, 0, 0, zone);
            matches.add(new Match(18, qat, sen, 0, 0, Instant.from(startTime18)));

            ZonedDateTime startTime19 = ZonedDateTime.of(2022, 11, 25, 19, 0, 0, 0, zone);
            matches.add(new Match(19, ned, ecu, 0, 0, Instant.from(startTime19)));

            ZonedDateTime startTime20 = ZonedDateTime.of(2022, 11, 25, 22, 0, 0, 0, zone);
            matches.add(new Match(20, eng, usa, 0, 0, Instant.from(startTime20)));

            ZonedDateTime startTime21 = ZonedDateTime.of(2022, 11, 26, 13, 0, 0, 0, zone);
            matches.add(new Match(21, tun, aus, 0, 0, Instant.from(startTime21)));

            ZonedDateTime startTime22 = ZonedDateTime.of(2022, 11, 26, 16, 0, 0, 0, zone);
            matches.add(new Match(22, pol, sar, 0, 0, Instant.from(startTime22)));

            ZonedDateTime startTime23 = ZonedDateTime.of(2022, 11, 26, 19, 0, 0, 0, zone);
            matches.add(new Match(23, fra, den, 0, 0, Instant.from(startTime23)));

            ZonedDateTime startTime24 = ZonedDateTime.of(2022, 11, 26, 22, 0, 0, 0, zone);
            matches.add(new Match(24, arg, mex, 0, 0, Instant.from(startTime24)));

            ZonedDateTime startTime25 = ZonedDateTime.of(2022, 11, 27, 13, 0, 0, 0, zone);
            matches.add(new Match(25, jap, cor, 0, 0, Instant.from(startTime25)));

            ZonedDateTime startTime26 = ZonedDateTime.of(2022, 11, 27, 16, 0, 0, 0, zone);
            matches.add(new Match(26, bel, mar, 0, 0, Instant.from(startTime26)));

            ZonedDateTime startTime27 = ZonedDateTime.of(2022, 11, 27, 19, 0, 0, 0, zone);
            matches.add(new Match(27, cro, can, 0, 0, Instant.from(startTime27)));

            ZonedDateTime startTime28 = ZonedDateTime.of(2022, 11, 27, 22, 0, 0, 0, zone);
            matches.add(new Match(28, spa, ger, 0, 0, Instant.from(startTime28)));

            ZonedDateTime startTime29 = ZonedDateTime.of(2022, 11, 28, 13, 0, 0, 0, zone);
            matches.add(new Match(29, cam, ser, 0, 0, Instant.from(startTime29)));

            ZonedDateTime startTime30 = ZonedDateTime.of(2022, 11, 28, 16, 0, 0, 0, zone);
            matches.add(new Match(30, sko, gha, 0, 0, Instant.from(startTime30)));

            ZonedDateTime startTime31 = ZonedDateTime.of(2022, 11, 28, 19, 0, 0, 0, zone);
            matches.add(new Match(31, bra, swi, 0, 0, Instant.from(startTime31)));

            ZonedDateTime startTime32 = ZonedDateTime.of(2022, 11, 28, 22, 0, 0, 0, zone);
            matches.add(new Match(32, por, uru, 0, 0, Instant.from(startTime32)));

            // group 3rd round
            ZonedDateTime startTime33 = ZonedDateTime.of(2022, 11, 29, 18, 0, 0, 0, zone);
            matches.add(new Match(33, ned, qat, 0, 0, Instant.from(startTime33)));

            ZonedDateTime startTime34 = ZonedDateTime.of(2022, 11, 29, 18, 0, 0, 0, zone);
            matches.add(new Match(34, ecu, sen, 0, 0, Instant.from(startTime34)));

            ZonedDateTime startTime35 = ZonedDateTime.of(2022, 11, 29, 22, 0, 0, 0, zone);
            matches.add(new Match(35, wal, eng, 0, 0, Instant.from(startTime35)));

            ZonedDateTime startTime36 = ZonedDateTime.of(2022, 11, 29, 22, 0, 0, 0, zone);
            matches.add(new Match(36, ira, usa, 0, 0, Instant.from(startTime36)));

            ZonedDateTime startTime37 = ZonedDateTime.of(2022, 11, 30, 18, 0, 0, 0, zone);
            matches.add(new Match(37, tun, fra, 0, 0, Instant.from(startTime37)));

            ZonedDateTime startTime38 = ZonedDateTime.of(2022, 11, 30, 18, 0, 0, 0, zone);
            matches.add(new Match(38, aus, den, 0, 0, Instant.from(startTime38)));

            ZonedDateTime startTime39 = ZonedDateTime.of(2022, 11, 30, 22, 0, 0, 0, zone);
            matches.add(new Match(39, pol, arg, 0, 0, Instant.from(startTime39)));

            ZonedDateTime startTime40 = ZonedDateTime.of(2022, 11, 30, 22, 0, 0, 0, zone);
            matches.add(new Match(40, sar, mex, 0, 0, Instant.from(startTime40)));

            ZonedDateTime startTime41 = ZonedDateTime.of(2022, 12, 1, 18, 0, 0, 0, zone);
            matches.add(new Match(41, cro, bel, 0, 0, Instant.from(startTime41)));

            ZonedDateTime startTime42 = ZonedDateTime.of(2022, 12, 1, 18, 0, 0, 0, zone);
            matches.add(new Match(42, can, mar, 0, 0, Instant.from(startTime42)));

            ZonedDateTime startTime43 = ZonedDateTime.of(2022, 12, 1, 22, 0, 0, 0, zone);
            matches.add(new Match(43, jap, spa, 0, 0, Instant.from(startTime43)));

            ZonedDateTime startTime44 = ZonedDateTime.of(2022, 12, 1, 22, 0, 0, 0, zone);
            matches.add(new Match(44, cor, ger, 0, 0, Instant.from(startTime44)));

            ZonedDateTime startTime45 = ZonedDateTime.of(2022, 12, 2, 18, 0, 0, 0, zone);
            matches.add(new Match(45, gha, uru, 0, 0, Instant.from(startTime45)));

            ZonedDateTime startTime46 = ZonedDateTime.of(2022, 12, 2, 18, 0, 0, 0, zone);
            matches.add(new Match(46, sko, por, 0, 0, Instant.from(startTime46)));

            ZonedDateTime startTime47 = ZonedDateTime.of(2022, 12, 2, 22, 0, 0, 0, zone);
            matches.add(new Match(47, ser, swi, 0, 0, Instant.from(startTime47)));

            ZonedDateTime startTime48 = ZonedDateTime.of(2022, 12, 2, 22, 0, 0, 0, zone);
            matches.add(new Match(48, cam, bra, 0, 0, Instant.from(startTime48)));

            // 1/8
            ZonedDateTime startTime49 = ZonedDateTime.of(2022, 12, 3, 18, 0, 0, 0, zone);
            matches.add(new Match(49, null, null, 0, 0, Instant.from(startTime49)));

            ZonedDateTime startTime50 = ZonedDateTime.of(2022, 12, 3, 22, 0, 0, 0, zone);
            matches.add(new Match(50, null, null, 0, 0, Instant.from(startTime50)));

            ZonedDateTime startTime51 = ZonedDateTime.of(2022, 12, 4, 18, 0, 0, 0, zone);
            matches.add(new Match(51, null, null, 0, 0, Instant.from(startTime51)));

            ZonedDateTime startTime52 = ZonedDateTime.of(2022, 12, 4, 22, 0, 0, 0, zone);
            matches.add(new Match(52, null, null, 0, 0, Instant.from(startTime52)));

            ZonedDateTime startTime53 = ZonedDateTime.of(2022, 12, 5, 18, 0, 0, 0, zone);
            matches.add(new Match(53, null, null, 0, 0, Instant.from(startTime53)));

            ZonedDateTime startTime54 = ZonedDateTime.of(2022, 12, 5, 22, 0, 0, 0, zone);
            matches.add(new Match(54, null, null, 0, 0, Instant.from(startTime54)));

            ZonedDateTime startTime55 = ZonedDateTime.of(2022, 12, 6, 18, 0, 0, 0, zone);
            matches.add(new Match(55, null, null, 0, 0, Instant.from(startTime55)));

            ZonedDateTime startTime56 = ZonedDateTime.of(2022, 12, 6, 22, 0, 0, 0, zone);
            matches.add(new Match(56, null, null, 0, 0, Instant.from(startTime56)));

            // 1/4
            ZonedDateTime startTime57 = ZonedDateTime.of(2022, 12, 9, 18, 0, 0, 0, zone);
            matches.add(new Match(57, null, null, 0, 0, Instant.from(startTime57)));

            ZonedDateTime startTime58 = ZonedDateTime.of(2022, 12, 9, 22, 0, 0, 0, zone);
            matches.add(new Match(58, null, null, 0, 0, Instant.from(startTime58)));

            ZonedDateTime startTime59 = ZonedDateTime.of(2022, 12, 10, 18, 0, 0, 0, zone);
            matches.add(new Match(59, null, null, 0, 0, Instant.from(startTime59)));

            ZonedDateTime startTime60 = ZonedDateTime.of(2022, 12, 10, 22, 0, 0, 0, zone);
            matches.add(new Match(60, null, null, 0, 0, Instant.from(startTime60)));

            // 1/2
            ZonedDateTime startTime61 = ZonedDateTime.of(2022, 12, 13, 22, 0, 0, 0, zone);
            matches.add(new Match(61, null, null, 0, 0, Instant.from(startTime61)));

            ZonedDateTime startTime62 = ZonedDateTime.of(2022, 12, 14, 22, 0, 0, 0, zone);
            matches.add(new Match(62, null, null, 0, 0, Instant.from(startTime62)));

            // 3rd place
            ZonedDateTime startTime63 = ZonedDateTime.of(2022, 12, 17, 18, 0, 0, 0, zone);
            matches.add(new Match(63, null, null, 0, 0, Instant.from(startTime63)));

            // final
            ZonedDateTime startTime64 = ZonedDateTime.of(2022, 12, 18, 18, 0, 0, 0, zone);
            matches.add(new Match(64, null, null, 0, 0, Instant.from(startTime64)));
            matchRepository.saveAll(matches);
            log.info("In MatchesRepository saved all matches");
        }
    }
}
