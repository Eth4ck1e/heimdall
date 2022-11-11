package com.bifrostsmp.heimdall.discord.commands.set.application;

import com.bifrostsmp.heimdall.config.Config;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SetDeniedChannel extends ListenerAdapter {
    public static void denied(SlashCommandInteractionEvent event) {
        Config.setAppDenied(event.getChannel().getId());
        Config.build();
        event.reply("Denied applications are assigned to this channel").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
