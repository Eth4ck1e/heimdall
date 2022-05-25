package com.bifrostsmp.heimdall.discord.commands.set.application;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class AppRole extends ListenerAdapter {
    public static void role(SlashCommandInteractionEvent event) {
        Role role = event.getOption("role").getAsRole();
        ConfigParser.setAppRole(role.getId());
        ConfigParser.build();
        event.reply("The Applicant role is set to " + role.getName()).queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
