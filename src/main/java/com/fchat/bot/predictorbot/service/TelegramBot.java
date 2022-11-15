package com.fchat.bot.predictorbot.service;

import com.fchat.bot.predictorbot.config.BotConfig;
import com.fchat.bot.predictorbot.model.*;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Transactional(readOnly = true)
public class TelegramBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final PredictionRepository predictionRepository;
    private final TeamRepository teamRepository;
    private final BotConfig botConfig;

    private HashMap<Long, String> lastMessages;
    private HashMap<Long, String> lastAdminMessages;
    ZoneId zone = ZoneId.of("Europe/Moscow");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter formatterForPatch = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm");

    private final String HELP_TEXT = "Да поможет вам бот:\n\n" +
            "/register - зарегистрироваться для участия в конкурсе прогнозов\n\n" +
            "/predict - оставить прогноз на матч\n\n" +
            "/table - посмотреть топ-20 участников, свои очки и свое место в рейтинге\n\n" +
            "/my_predicts - посмотреть историю своих прогнозов\n\n" +
            "Правила конкурса:\n" +
            " - Точный прогноз — 3 очка\n" +
            " - Угаданная разница мячей (кроме ничьих) — 2 очка\n" +
            " - Угадан победитель или ничья (не с точным счетом) — 1 очко\n" +
            " - В матчах серии плей-офф в расчет берется только основное время" +
            " - Сделать или изменить сделанный ранее прогноз можно в день матча, но не позднее, чем за час до его начала." +
            " - При подсчете рейтинга при равенстве очков выше ставится тот игрок, у кого больше точных прогнозов";

    private final int MINUTES_TO_DEADLINE = 60;

    public TelegramBot(BotConfig botConfig, UserRepository userRepository, MatchRepository matchRepository,
                       PredictionRepository predictionRepository, TeamRepository teamRepository) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.predictionRepository = predictionRepository;
        this.teamRepository = teamRepository;
        lastMessages = new HashMap<>();
        lastAdminMessages = new HashMap<>();
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "start"));
        commands.add(new BotCommand("/register", "register to participate"));
        commands.add(new BotCommand("/predict", "make prediction"));
        commands.add(new BotCommand("/table", "get top20 players"));
        commands.add(new BotCommand("/my_predicts", "get your predictions"));
        commands.add(new BotCommand("/help", "get info about bot"));
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot commands list");
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (lastAdminMessages.containsKey(chatId)) {
                if (lastAdminMessages.get(chatId).equals("/patch_match")) {
                    patchMatch(update.getMessage());
                    return;
                } else if (lastAdminMessages.get(chatId).equals("/put_score")) {
                    putScore(update.getMessage());
                    return;
                } else if (lastAdminMessages.get(chatId).equals("/message_all")) {
                    sendMessageToAllUsers(update.getMessage());
                    return;
                }
            }
            if (lastMessages.containsKey(chatId)) {
                savePredict(Integer.parseInt(lastMessages.get(chatId)), update.getMessage());
                return;
            }
            switch (messageText) {
                case "/start":
                    String name = update.getMessage().getChat().getFirstName();
                    String text = "Привет, " + name + "!\n" + "Нажми /register, чтобы начать делать прогнозы";
                    sendMessage(chatId, text);
                    log.info("Send start message to User: " + name);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/register":
                    registerUser(update.getMessage());
                    break;
                case "/predict":
                    handlePredictCommand(chatId);
                    break;
                case "/my_predicts":
                    handleMyPredictsCommand(chatId);
                    break;
                case "/table":
                    handleTableCommand(chatId);
                    break;
                case "/admin":
                    handleAdminCommand(chatId);
                    break;
                case "/patch_match":
                    if (checkAdmin(chatId)) {
                        sendMessage(chatId, "Введите матч в формате:\n id-t1-t2-s1-s2-dd.MM.yyyy.HH.mm \n" +
                                "Если время не меняется, введите вместо него 0");
                        lastAdminMessages.put(chatId, "/patch_match");
                    }
                    break;
                case "/put_score":
                    if (checkAdmin(chatId)) {
                        sendMessage(chatId, "Введите счет матча в формате:\nid-s1-s2 \n" +
                                "Внимательно проверьте, что ничего не перепутано!");
                        lastAdminMessages.put(chatId, "/put_score");
                    }
                    break;
                case "/calculate":
                    if (checkAdmin(chatId) && lastAdminMessages.containsKey(chatId)) {
                        int matchId = Integer.parseInt(lastAdminMessages.get(chatId));
                        calculatePoints(matchId, chatId);
                    }
                    break;
                case "/message_all":
                    if (checkAdmin(chatId)) {
                        lastAdminMessages.put(chatId, "/message_all");
                    }
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            if (callbackData.equals("CANCEL")) {
                if (lastMessages.containsKey(chatId)) {
                    EditMessageText message = new EditMessageText();
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Отмена. Можете выбрать другой матч");
                    message.setMessageId((int) messageId);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        log.error("Error occurred: " + e.getMessage());
                    }
                    lastMessages.remove(chatId);
                }
            } else {
                int matchId = Integer.parseInt(callbackData);
                if (predictionRepository.findByUserAndMatch(chatId, matchId) != null) {
                    Prediction p = predictionRepository.findByUserAndMatch(chatId, matchId);
                    String text = "Прогноз уже был сделан: " + p.getScores1() + "-" + p.getScores2() +
                            "\nВы можете изменить его";
                    sendMessage(chatId, text);
                }
                ZonedDateTime nowTime = ZonedDateTime.ofInstant(Instant.now(), zone);
                if (ZonedDateTime.ofInstant(matchRepository.findById(matchId).orElseThrow().getStart(), zone)
                        .minusMinutes(MINUTES_TO_DEADLINE).isAfter(nowTime)) {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    List<InlineKeyboardButton> rowInLine = new ArrayList<>();
                    button.setText("Отмена");
                    button.setCallbackData("CANCEL");
                    rowInLine.add(button);
                    SendMessage message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Выбран матч " +
                            makeShortTextFromMatch(matchRepository.findById(matchId).orElseThrow()) +
                            "\nОтправьте счет матча в формате 'X-X' (например 5-0).\nНажмите кнопку \"Отмена\" если передумали");
                    message.setReplyMarkup(makeInLineKeyboard(rowInLine));
                    executeMessage(message);
                    lastMessages.put(chatId, callbackData);
                }
            }
        }
    }

    private void sendMessageToAllUsers(Message msg) {
        lastAdminMessages.remove(msg.getChatId());
        List<User> users = userRepository.findAll();
        Thread thread = new Thread(() -> {
            for (User u : users) {
                sendMessage(u.getChatId(), msg.getText());
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            sendMessage(msg.getChatId(), "Сообщение разослано");
            log.info("Sending message to all users");
        });
        thread.start();
    }

    @Transactional
    public void registerUser(Message msg) {
        long chatId = msg.getChatId();
        if (userRepository.findById(chatId).isEmpty()) {
            Chat chat = msg.getChat();
            User user = new User();
            user.setChatId(chatId);
            if (chat.getUserName() != null) {
                user.setUsername(chat.getUserName());
            } else {
                user.setUsername(chat.getFirstName());
            }
            userRepository.save(user);
            sendMessage(chatId, "You are registered!");
            log.info("User saved: " + user);
            return;
        }
        sendMessage(chatId, "You are already registered. Let's start thinking about predicts");
    }

    private void handlePredictCommand(long chatId) {
        if (userRepository.findById(chatId).isEmpty()) {
            sendMessage(chatId, "You are not registered! Use /register command");
            return;
        }
        ZonedDateTime nowTime = ZonedDateTime.ofInstant(Instant.now(), zone);
        List<Match> availableMatches = matchRepository.findAll()
                .stream()
                .filter(m -> ZonedDateTime.ofInstant(m.getStart(), zone).getDayOfYear() == nowTime.getDayOfYear())
                .filter(m -> ZonedDateTime.ofInstant(m.getStart(), zone).minusMinutes(MINUTES_TO_DEADLINE).isAfter(nowTime))
                .sorted(Comparator.comparing(Match::getMatchId))
                .collect(Collectors.toList());
        if (availableMatches.isEmpty()) {
            sendMessage(chatId, "В данный момент нет доступных для прогноза матчей");
            return;
        }
        sendMessage(chatId, "На какой матч делаем прогноз?");
        StringBuilder sb = new StringBuilder();
        sb.append("Доступные для прогноза матчи:\n");
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        for (Match m : availableMatches) {
            sb.append(m.getMatchId()).append(" — ")
                    .append(makeTextFromMatch(m))
                    .append('\n');
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(String.valueOf((m.getMatchId())));
            button.setCallbackData(String.valueOf(m.getMatchId()));
            rowInLine.add(button);
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(sb.toString());
        message.setReplyMarkup(makeInLineKeyboard(rowInLine));
        executeMessage(message);
    }

    private void handleMyPredictsCommand(long chatId) {
        List<Prediction> predictions = predictionRepository.findByUser(chatId).stream()
                .sorted(Comparator.comparing(Prediction::getId))
                .collect(Collectors.toList());
        if (predictions.isEmpty()) {
            sendMessage(chatId, "Нет ни одного прогноза");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Prediction p : predictions) {
            sb.append(p.getMatch().getTeam1().getName())
                    .append(" - ")
                    .append(p.getMatch().getTeam2().getName())
                    .append(" ")
                    .append(p.getScores1())
                    .append("-")
                    .append(p.getScores2());
            if (p.getPoints() != null) {
                sb.append(" (")
                        .append(p.getMatch().getScores1())
                        .append("-")
                        .append(p.getMatch().getScores2())
                        .append(" | ")
                        .append(p.getPoints())
                        .append(")");
            }
            sb.append("\n");
        }
        sendMessage(chatId, "В скобках указан счет сыгранного матча и ваши очки за матч");
        sendMessage(chatId, sb.toString());
    }

    private void handleTableCommand(long chatId) {
        User user = userRepository.findById(chatId).orElseThrow();
        List<User> users = userRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getPoints).thenComparing(User::getExactPred).reversed())
                .collect(Collectors.toList());
        List<User> topUsers = users
                .stream()
                .limit(20)
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (User u : topUsers) {
            sb.append(i).append(" ")
                    .append(u.getChatId() == chatId ? "<b>" : "")
                    .append(u.getUsername())
                    .append(u.getChatId() == chatId ? "</b>" : "")
                    .append("  ")
                    .append("<b>" + u.getPoints() + "</b>")
                    .append("  (")
                    .append(u.getExactPred())
                    .append(")")
                    .append("\n");
            i++;
        }
        sb.append("\n");
        sb.append("Твои очки - ")
                .append(user.getPoints())
                .append(" Место в рейтинге - ")
                .append(users.indexOf(user) + 1);
        sendMessage(chatId, "В скобках указано количество точных прогнозов");
        sendMessage(chatId, sb.toString());
    }

    private void handleAdminCommand(long chatId) {
        if (checkAdmin(chatId)) {
            String text = "Admin menu \n" +
                    "Select command: \n" +
                    "/patch_match\n" +
                    "/put_score\n" +
                    "/message_all";
            sendMessage(chatId, text);
        }

    }

    @Transactional
    public void patchMatch(Message msg) {
        String[] matchData = msg.getText().split("-");
        try {
            MatchDto matchDto = new MatchDto(
                    Integer.parseInt(matchData[0]),
                    Integer.parseInt(matchData[1]),
                    Integer.parseInt(matchData[2]),
                    Integer.parseInt(matchData[3]),
                    Integer.parseInt(matchData[4]),
                    matchData[5]
            );
            Match match = matchRepository.findById(matchDto.getMatchId()).orElseThrow();
            match.setTeam1(teamRepository.findById(matchDto.getTeam1Id()).orElseThrow());
            match.setTeam2(teamRepository.findById(matchDto.getTeam2Id()).orElseThrow());
            match.setScores1(matchDto.getScores1());
            match.setScores2(matchDto.getScores2());
            if (!matchDto.getStartTime().equals("0")) {
                ZonedDateTime startTime = ZonedDateTime.of((LocalDateTime.parse(matchDto.getStartTime(), formatterForPatch)), zone);
                match.setStart(Instant.from(startTime));
            }
            matchRepository.save(match);
            sendMessage(msg.getChatId(), "Матч id=" + match.getMatchId() + " изменен.");
            log.info("Match patched: " + match.getMatchId());
            lastAdminMessages.remove(msg.getChatId());
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            sendMessage(msg.getChatId(), "Неверный формат");
            lastAdminMessages.remove(msg.getChatId());
        }
    }

    @Transactional
    public void putScore(Message msg) {
        String[] matchData = msg.getText().split("-");
        try {
            MatchScoreDto matchScoreDto = new MatchScoreDto(
                    Integer.parseInt(matchData[0]),
                    Integer.parseInt(matchData[1]),
                    Integer.parseInt(matchData[2])
            );
            Match match = matchRepository.findById(matchScoreDto.getMatchId()).orElseThrow();
            match.setScores1(matchScoreDto.getScores1());
            match.setScores2(matchScoreDto.getScores2());
            matchRepository.save(match);
            String text = "В матч " + makeShortTextFromMatch(match) + " внесен счет " +
                    matchScoreDto.getScores1() + "-" + matchScoreDto.getScores2();
            sendMessage(botConfig.getAdminOneId(), text);
            sendMessage(botConfig.getAdminTwoId(), text);
            log.info("Match patched: " + match.getMatchId());
            lastAdminMessages.put(msg.getChatId(), String.valueOf(match.getMatchId()));
            sendMessage(msg.getChatId(), "Нажмите /calculate если все верно \n/put_score чтобы ввести счет заново");
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            sendMessage(msg.getChatId(), "Неверный формат");
            lastAdminMessages.remove(msg.getChatId());
        }
    }

    @Transactional
    public void calculatePoints(int matchId, Long adminId) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        List<Prediction> matchPredictions = predictionRepository.findByMatch(matchId);
        for (Prediction p : matchPredictions) {
            User user = p.getUser();
            if (p.getScores1() == match.getScores1() && p.getScores2() == match.getScores2()) {
                user.setPoints(user.getPoints() + 3);
                user.setExactPred(user.getExactPred() + 1);
                p.setPoints(3);
            } else if (p.getScores1() - p.getScores2() == 0 && match.getScores1() - match.getScores2() == 0) {
                user.setPoints(user.getPoints() + 1);
                p.setPoints(1);
            } else if ((p.getScores1() - p.getScores2()) == (match.getScores1() - match.getScores2())) {
                user.setPoints(user.getPoints() + 2);
                p.setPoints(2);
            } else if ((p.getScores1() > p.getScores2() && match.getScores1() > match.getScores2()) ||
                    (p.getScores1() < p.getScores2() && match.getScores1() < match.getScores2())) {
                user.setPoints(user.getPoints() + 1);
                p.setPoints(1);
            } else {
                p.setPoints(0);
            }
            userRepository.save(user);
            predictionRepository.save(p);
        }
        sendMessage(adminId, "Очки посчитаны");
        lastAdminMessages.remove(adminId);
        log.info("Calculating points for match id=: " + match.getMatchId());
    }

    @Scheduled(cron = "0 0 10 * * *", zone = "Europe/Moscow")
    public void sendTodayMatches() {
        List<User> users = userRepository.findAll();
        String text;
        ZonedDateTime nowTime = ZonedDateTime.ofInstant(Instant.now(), zone);
        if (nowTime.isAfter(matchRepository.findById(64).orElseThrow().getStart().atZone(zone))) {
            text = "Сегодня нет ни одного матча. Чемпионат Мира закончен" +
                    EmojiParser.parseToUnicode("\uD83D\uDE22");
        } else {
            List<Match> todayMatches = matchRepository.findAll()
                    .stream()
                    .filter(m -> ZonedDateTime.ofInstant(m.getStart(), zone).getDayOfYear() == nowTime.getDayOfYear())
                    .sorted(Comparator.comparing(Match::getStart))
                    .collect(Collectors.toList());
            if (todayMatches.isEmpty()) {
                text = "Сегодня нет ни одного матча";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(EmojiParser.parseToUnicode("\uD83D\uDCC5"))
                        .append(" Матчи на сегодня:\n\n");
                for (Match m : todayMatches) {
                    sb.append(makeTextFromMatch(m)).append("\n");
                }
                sb.append("\nЖдем ваши прогнозы! —> /predict");
                text = sb.toString();
            }
        }
        Thread thread = new Thread(() -> {
            for (User u : users) {
                sendMessage(u.getChatId(), text);
                try {
                    Thread.sleep(2500L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    private String makeTextFromMatch(Match match) {
        ZoneId zone = ZoneId.of("Europe/Moscow");
        return match.getTeam1().getFlag() + " " + match.getTeam1().getName() + " : " +
                match.getTeam2().getName() + " " + match.getTeam2().getFlag() + "  " +
                formatter.format(ZonedDateTime.ofInstant(match.getStart(), zone)) + "(MSK)";
    }

    private String makeShortTextFromMatch(Match match) {
        return match.getTeam1().getName() + " : " + match.getTeam2().getName();
    }

    @Transactional
    public void savePredict(int matchId, Message msg) {
        String[] scores = msg.getText().split("-");
        checkInputScores(scores[0], msg.getChatId());
        checkInputScores(scores[1], msg.getChatId());
        Prediction prediction = new Prediction(
                predictionRepository.findByUserAndMatch(msg.getChatId(), matchId) != null ?
                        predictionRepository.findByUserAndMatch(msg.getChatId(), matchId).getId() : null,
                userRepository.getReferenceById(msg.getChatId()),
                matchRepository.getReferenceById(matchId),
                Integer.parseInt(scores[0]),
                Integer.parseInt(scores[1]),
                null
        );
        predictionRepository.save(prediction);
        lastMessages.remove(msg.getChatId());
        sendMessage(msg.getChatId(), "Прогноз принят");
        log.info("Predict {} on match {} by user {} saved: ", msg.getText(), matchId, msg.getChatId());
    }

    private void checkInputScores(String str, Long chatId) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат счета");
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.HTML);
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private InlineKeyboardMarkup makeInLineKeyboard(List<InlineKeyboardButton> rowInLine) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private boolean checkAdmin(long chatId) {
        return chatId == botConfig.getAdminOneId() || chatId == botConfig.getAdminTwoId();
    }
}

