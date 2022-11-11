package com.bifrostsmp.heimdall.discord.applications;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDiscordBot;
import static com.bifrostsmp.heimdall.config.Config.isHeimdallDebug;
import static com.bifrostsmp.heimdall.discord.buttons.application.Deny.send;

public class DenyResponseHandler extends ListenerAdapter {
    private final User staff;
    private final MessageEmbed embed;

    private final TextChannel channel;
    private int i;

    public DenyResponseHandler(User staff, MessageEmbed embed, TextChannel channel) {
        this.staff = getDiscordBot().getUserById(staff.getId());
        this.embed = embed;
        this.channel = channel;
        int i = 0;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (i > 0) return;
        if (e.getAuthor().isBot()) {
            if (isHeimdallDebug()) {
                System.out.println("[Heimdall] DEBUG: Author is a Bot");
            }
            return;
        }
        if (!e.getChannel().asTextChannel().equals(channel)) {
            System.out.println("[Heimdall] DEBUG: Event is not registering from the correct ticket channel");
            return;
        }
        if (!e.isFromGuild()) {
            if (isHeimdallDebug()) {
                System.out.println("[Heimdall] DEBUG: Event is registering from DM not Guild");
            }
            return;
        }
        if (e.getAuthor().getIdLong() != staff.getIdLong()) {
            if (isHeimdallDebug()) {
                System.out.println("[Heimdall] DEBUG: Author is not the Member that denied the Application");
            }
            return;
        }


        String response = e.getMessage().getContentRaw();
        e.getMessage().delete().queue();
        e.getMessage()
                .getChannel()
                .sendMessage("Your reason has been noted: " + response)
                .queue(
                        message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });

        send(e.getMember().getUser(), embed, channel, response);
        i++;
    }
}