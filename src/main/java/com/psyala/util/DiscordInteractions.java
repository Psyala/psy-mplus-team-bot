package com.psyala.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.entities.TextChannelImpl;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DiscordInteractions {

    public static Optional<TextChannelImpl> getGuildTextChannel(Guild guild, String channelName) {
        AtomicReference<TextChannelImpl> foundChannel = new AtomicReference<>(null);
        guild.getChannels().forEach(guildChannel -> {
            if (guildChannel instanceof TextChannelImpl && guildChannel.getName().equals(channelName)) {
                foundChannel.set((TextChannelImpl) guildChannel);
            }
        });
        return Optional.ofNullable(foundChannel.get());
    }

    public static void deleteMessage(Message message) {
        message.delete()
                .queue(
                        unused -> {
                        },
                        throwable -> {
                        }
                );
    }

    public static void cleanupTextChannel(TextChannelImpl textChannel) {
        textChannel.getIterableHistory()
                .forEach(message -> {
                            message.delete()
                                    .queue(
                                            unused -> {
                                            },
                                            throwable -> {
                                            }
                                    );
                        }
                );
    }
}
