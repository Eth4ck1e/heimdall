package com.bifrostsmp.heimdall.discord.commands.set.application;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class Denied extends ListenerAdapter {
    public static void denied(SlashCommandInteractionEvent event) {
        ConfigParser.setAppDenied(event.getChannel().getId());
        ConfigParser.build();
        event.reply("Denied applications are assigned to this channel").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
