package com.bifrostsmp.heimdall.discord.commands.set;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SetWelcomeChannel extends ListenerAdapter {
    public static void welcome(SlashCommandInteractionEvent event) {
        ConfigParser.setWelcomeChannel(event.getChannel().getId());
        ConfigParser.build();
        event.reply("Welcome Channel for posting welcome information for new players is set to this channel").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
