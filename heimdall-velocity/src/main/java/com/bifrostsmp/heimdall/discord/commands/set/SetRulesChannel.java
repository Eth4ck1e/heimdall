package com.bifrostsmp.heimdall.discord.commands.set;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SetRulesChannel extends ListenerAdapter {
    public static void rules(SlashCommandInteractionEvent event) {
        ConfigParser.setRulesChannel(event.getChannel().getId());
        ConfigParser.build();
        event.reply("Rules will be posted to this channel").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
