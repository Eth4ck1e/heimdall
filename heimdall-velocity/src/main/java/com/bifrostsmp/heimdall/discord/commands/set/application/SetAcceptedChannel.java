package com.bifrostsmp.heimdall.discord.commands.set.application;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SetAcceptedChannel extends ListenerAdapter {
    public static void accepted(SlashCommandInteractionEvent event) {
        ConfigParser.setAppAccepted(event.getChannel().getId());
        ConfigParser.build();
        event.reply("Accepted applications are assigned to this channel").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
