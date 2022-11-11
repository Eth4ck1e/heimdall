package com.bifrostsmp.heimdall.discord.commands.set;

import com.bifrostsmp.heimdall.config.Config;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SetHowdyChannel extends ListenerAdapter {
    public static void howdy(SlashCommandInteractionEvent event) {
        Config.setHowdyChannel(event.getChannel().getId());
        Config.build();
        event.reply("Members welcome messages will be displayed in this channel [requires welcome messages to be enabled]").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
