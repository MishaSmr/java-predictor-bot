package com.fchat.bot.predictorbot.service;

import com.fchat.bot.predictorbot.config.BotConfig;
import com.fchat.bot.predictorbot.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
public class TelegramBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final PredictionRepository predictionRepository;
    private final BotConfig botConfig;

    private HashMap<Long, String> lastMessages; // Здесь хранятся последние сообщения от каждого пользователя

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private final String HELP_TEXT = "Да поможет вам бот:\n\n" +
            "/predict - \n" +
            "/help - \n";

    public TelegramBot(BotConfig botConfig, UserRepository userRepository, MatchRepository matchRepository,
                       PredictionRepository predictionRepository) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.predictionRepository = predictionRepository;
        lastMessages = new HashMap<>();
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "start"));
        commands.add(new BotCommand("/predict", "make prediction"));
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
            if (lastMessages.containsKey(chatId)) {
                savePredict(Integer.parseInt(lastMessages.get(chatId)), update.getMessage());
                return;
            }
            switch (messageText) {
                case "/start":
                    String name = update.getMessage().getChat().getFirstName();
                    String text = "Превет, " + name + "!";
                    sendMessage(chatId, text);
                    registerUser(update.getMessage());
                    log.info("Send start message to User: " + name);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/predict":
                    handlePredictCommand(chatId);
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
                    sendMessage(chatId, "Прогноз на матч уже был сделан");
                    return;
                }
                InlineKeyboardButton button = new InlineKeyboardButton();
                List<InlineKeyboardButton> rowInLine = new ArrayList<>();
                button.setText("Отмена");
                button.setCallbackData("CANCEL");
                rowInLine.add(button);
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("Выбран матч " +
                        makeShortTextFromMatch(matchRepository.findById(matchId).get()) +
                        "\nОтправьте счет матча в формате 'X-X' (например 5-0).\nНажмите кнопку \"Отмена\" если передумали");
                message.setReplyMarkup(makeInLineKeyboard(rowInLine));
                executeMessage(message);
                lastMessages.put(chatId, callbackData);
            }
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    private void registerUser(Message msg) {
        long chatId = msg.getChatId();
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            Chat chat = msg.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setUsername(chat.getUserName());
            userRepository.save(user);
            sendMessage(chatId, "You are registered!");
            log.info("User saved: " + user);
            return;
        }
        sendMessage(chatId, "You are already registered. Let's start thinking about predicts");
    }

    private void handlePredictCommand(long chatId) {
        if (userRepository.findById(chatId).isEmpty()) {
            sendMessage(chatId, "You are not registered! Use /start command");
            return;
        }
        ZoneId zone = ZoneId.of("Europe/Moscow");
        ZonedDateTime startTime = ZonedDateTime.ofInstant(Instant.now(), zone);
        List<Match> availableMatches = matchRepository.findAll()
                .stream()
                .filter(m -> ZonedDateTime.ofInstant(m.getStart(), zone).getDayOfYear() == startTime.getDayOfYear())
                .filter(m -> ZonedDateTime.ofInstant(m.getStart(), zone).minusMinutes(120).isAfter(startTime))
                .sorted(Comparator.comparing(Match::getMatchId))
                .collect(Collectors.toList());
        if (availableMatches.isEmpty()) {
            sendMessage(chatId, "В данный момент нет доступных для прогноза матчей");
            return;
        }
        sendMessage(chatId, "На какой матч делаем прогноз?");
        StringBuilder sb = new StringBuilder();
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

    private String makeTextFromMatch(Match match) {
        ZoneId zone = ZoneId.of("Europe/Moscow");
        return match.getTeam1().getFlag() + " " + match.getTeam1().getName() + " : " +
                match.getTeam2().getName() + " " + match.getTeam2().getFlag() + "  " +
                formatter.format(ZonedDateTime.ofInstant(match.getStart(), zone)) + "(MSK)";
    }

    private String makeShortTextFromMatch(Match match) {
        return match.getTeam1().getName() + " : " + match.getTeam2().getName();
    }

    private void savePredict(int matchId, Message msg) {
        String[] scores = msg.getText().split("-");
        checkInputScores(scores[0], msg.getChatId());
        checkInputScores(scores[1], msg.getChatId());
        Prediction prediction = new Prediction(
                null,
                userRepository.getReferenceById(msg.getChatId()),
                matchRepository.getReferenceById(matchId),
                Integer.parseInt(scores[0]),
                Integer.parseInt(scores[1])
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
}

