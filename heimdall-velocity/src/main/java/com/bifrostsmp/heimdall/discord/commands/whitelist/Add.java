package com.bifrostsmp.heimdall.discord.commands.whitelist;

import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import database.Query;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import static database.Query.updateTrigger;

public class Add extends ListenerAdapter {
    public static void add(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(false);
        String name;
        String id;
        boolean result;

        name = event.getOption("player").getAsString();
        id = NameToID.nameToID(name);
        if (id == null) {
            // success embed block
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Whitelist");
            info.setDescription(name + " is not a valid IGN");
            info.setColor(Color.RED);
            hook.sendMessageEmbeds(info.build())
                    .queue(
                            message -> {
                                message.delete().queueAfter(30, TimeUnit.SECONDS);
                            }); // send embed to message channel
            info.clear(); // clear embed from memory
            return;
        }
        result = Query.checkPlayer(id);
        if (!result) {
            Query.insertPlayer(name, id);
            updateTrigger();
            // success embed block
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Whitelist");
            info.setDescription(name + " has been added to the whitelist");
            info.setColor(Color.GREEN);
            hook.sendMessageEmbeds(info.build())
                    .queue(
                            message -> {
                                message.delete().queueAfter(30, TimeUnit.SECONDS);
                            }); // send embed to message channel
            info.clear(); // clear embed from memory
        } else {
            // success embed block
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Whitelist");
            info.setDescription(name + " is already whitelisted");
            info.setColor(Color.RED);
            hook.sendMessageEmbeds(info.build())
                    .queue(
                            message -> {
                                message.delete().queueAfter(30, TimeUnit.SECONDS);
                            }); // send embed to message channel
            info.clear(); // clear embed from memory
        }
    }
}
