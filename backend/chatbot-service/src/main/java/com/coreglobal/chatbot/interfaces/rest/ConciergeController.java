package com.coreglobal.chatbot.interfaces.rest;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatbot/conversations")
public class ConciergeController {
    @PostMapping
    public Map<String, Object> reply(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").toLowerCase(Locale.ROOT);
        String intent = message.contains("card") ? "CARD_SUPPORT" : message.contains("transfer") ? "TRANSFER_STATUS" : message.contains("balance") ? "ACCOUNT_BALANCE" : "GENERAL_SUPPORT";
        String reply = switch (intent) {
            case "CARD_SUPPORT" -> "Your Core Altitude card is active. I can guide you to freeze it or review recent activity.";
            case "TRANSFER_STATUS" -> "Your most recent transfer completed successfully and arrived instantly.";
            case "ACCOUNT_BALANCE" -> "Your available balances are ready in the secure Accounts view.";
            default -> "I can help with accounts, transfers, cards, offers, and connecting you with a Core Care specialist.";
        };
        return Map.of("assistant", "Cora", "intent", intent, "reply", reply, "suggestions", List.of("View activity", "Contact specialist"), "createdAt", Instant.now());
    }
}
