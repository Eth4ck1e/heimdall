package com.bifrostsmp.heimdall.discord.commands.whitelist;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.database.Query.updateTrigger;

public class Update extends ListenerAdapter {
    public static void update(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(false);

        try {
            updateTrigger();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Whitelist");
        info.setDescription("Whitelist update started");
        info.setColor(Color.GREEN);
        hook.sendMessageEmbeds(info.build())
                .queue(
                        message -> {
                            message.delete().queueAfter(30, TimeUnit.SECONDS);
                        }); // send embed to message channel
        info.clear(); // clear embed from memory
    }
}
