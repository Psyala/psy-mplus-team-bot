package com.psyala.controller;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.RestAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    private final Map<Guild, List<RestAction>> pendingMessageActions = new HashMap<>();
    private final ScheduledExecutorService threadPoolExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Message Sender Thread"));

    public MessageController() {
        threadPoolExecutor.scheduleAtFixedRate(() -> {
            try {
                pendingMessageActions.forEach((guild, messageActions) -> {
                    List<RestAction> messagesToAction = messageActions.stream()
                            .limit(30)
                            .collect(Collectors.toList());

                    messagesToAction.forEach(messageAction -> messageAction.queue(unused -> {
                            },
                            throwable -> {
                                LOGGER.info("Error actioning message");
                            })
                    );

                    messageActions.removeAll(messagesToAction);
                });
            } catch (Exception ex) {
                LOGGER.error("Error attempting to process pending messages", ex);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    
    public void addMessageToQueue(Guild guild, RestAction messageAction) {
        List<RestAction> pendingActionsForGuild = pendingMessageActions.getOrDefault(guild, new ArrayList<>());
        pendingActionsForGuild.add(messageAction);
        pendingMessageActions.put(guild, pendingActionsForGuild);
    }
}
