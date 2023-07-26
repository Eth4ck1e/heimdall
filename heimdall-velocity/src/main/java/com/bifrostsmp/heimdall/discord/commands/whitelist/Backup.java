package com.bifrostsmp.heimdall.discord.commands.whitelist;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import whitelist.BackupWhitelist;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Backup extends ListenerAdapter {
    public static void backup(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(false);

        BackupWhitelist.backup();

        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Whitelist");
        info.setDescription("Whitelist.json created from database");
        info.setColor(Color.GREEN);
        hook.sendMessageEmbeds(info.build())
                .queue(
                        message -> {
                            message.delete().queueAfter(30, TimeUnit.SECONDS);
                        }); // send embed to message channel
        info.clear(); // clear embed from memory
    }
}
