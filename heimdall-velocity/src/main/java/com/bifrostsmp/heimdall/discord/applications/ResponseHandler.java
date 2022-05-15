package com.bifrostsmp.heimdall.discord.applications;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.discord.buttons.application.Deny.send;

public class ResponseHandler extends ListenerAdapter {
    private final long applicant;
    private final long member;
    private final MessageEmbed embed;
    private int i;

    public ResponseHandler(long applicant, long member, MessageEmbed embed) {
        this.applicant = applicant;
        this.member = member;
        this.embed = embed;
        int i = 0;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (e.getAuthor().getIdLong() != member) return;
        if (e.isFromGuild()) return;
        if (i > 0) return;

        String response = e.getMessage().getContentRaw();
        e.getMessage()
                .getChannel()
                .sendMessage("Your reason has been noted: " + response)
                .queue(
                        message -> {
                            message.delete().queueAfter(30, TimeUnit.SECONDS);
                        });

        send(String.valueOf(applicant), e.getMember(), embed, e, response);
        i++;
    }
}