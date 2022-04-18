package com.bifrostsmp.heimdall.discord.commands;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommands extends ListenerAdapter {
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("say")) {
            event.reply(event.getOption("content").getAsString()).queue(); // reply immediately
        }
    }
}
