package com.bifrostsmp.heimdall.discord.commands.set.application;

import com.bifrostsmp.heimdall.config.Config;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SetAppRole extends ListenerAdapter {
    public static void role(SlashCommandInteractionEvent event) {
        Role role = event.getOption("role").getAsRole();
        Config.setAppRole(role.getId());
        Config.build();
        event.reply("The Applicant role is set to " + role.getName()).queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
