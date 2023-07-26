package com.bifrostsmp.heimdall.discord.commands.whitelist;

import database.ImportWhitelist;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Import extends ListenerAdapter {
    public static void importWhitelist(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(false);

        ImportWhitelist.parser();

        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Whitelist");
        info.setDescription("Database has been updated using the Whitelist.json");
        info.setColor(Color.GREEN);
        hook.sendMessageEmbeds(info.build())
                .queue(
                        message -> {
                            message.delete().queueAfter(30, TimeUnit.SECONDS);
                        }); // send embed to message channel
        info.clear(); // clear embed from memory
    }
}
