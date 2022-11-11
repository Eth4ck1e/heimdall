package com.bifrostsmp.heimdall.discord.commands.set.application;

import com.bifrostsmp.heimdall.config.Config;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SetPendingChannel extends ListenerAdapter {
    public static void pending(SlashCommandInteractionEvent event) {
        Config.setAppPending(event.getChannel().getId());
        Config.build();
        event.reply("Pending applications are assigned to this channel").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
