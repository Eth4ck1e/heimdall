package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class Info extends ListenerAdapter {

    public static void info(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        if (!event.getName().equalsIgnoreCase("info")) return;
        // We don't want to respond to other bot accounts, including ourselves
        // embed block
        EmbedBuilder info = new EmbedBuilder();
        info.setTitle(":robot: Information :robot:");
        info.setDescription("This bot provides applications, tickets, whitelisting, and welcome functions to your discord\ncheck out the wiki to get started!");
        info.addField("Developers", "Eth4ck1e, HunnaG", false);
        info.setColor(Color.GREEN);
        MessageChannel channel = event.getChannel(); // get message channel
        channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
        info.clear(); // clear embed from memory
    }

}
