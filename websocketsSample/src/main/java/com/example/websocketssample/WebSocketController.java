package com.example.websocketssample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private int timeCorrection = 0;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Sveiki atvykę, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
    private int counter = 0;

    @Scheduled(fixedRate = 3000)
    public void sendGreeting() {
        messagingTemplate.convertAndSend("/topic/heyhey", "Hello " + counter++);
    }

    @MessageMapping("/region")
    public void setRegionOffset(Map<String, String> message) {
        String offset = message.get("offset");
        timeCorrection = Integer.parseInt(offset);
    }

    @Scheduled(fixedRate = 1000)
    public void sendClock() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime correctedTime = LocalTime.now().plusHours(timeCorrection);
        String formattedTime = correctedTime.format(timeFormatter);
        messagingTemplate.convertAndSend("/topic/clock", "Current time: " + formattedTime);
    }

    // Atsitiktinis juokelis kas 5 sekundės
    private final List<String> jokes = Arrays.asList(
            "Tėvas taip norėjo, kad jo sūnus taptų fiziku, jog bausdavo jį ne diržu, o elektros srove.",
            "Tėvas sako sūnui- jei gerai mokinsies, aš tau pavogsiu dviratį.",
            "-Daktare, mes praradome pacientą.-Kas atsitiko? -Pasveiko.",
            "Jei manai, kad valdžiai ant tavęs nusispjaut, pabandyk nors kartą nesumokėti mokesčių.",
            "Teismas paskyrė bausmę benamiui- namų areštą."
    );
    private final Random random = new Random();

    @Scheduled(fixedRate = 5000)
    public void sendJoke() {
        String randomJoke = jokes.get(random.nextInt(jokes.size()));
        messagingTemplate.convertAndSend("/topic/jokes", randomJoke);
    }




}

