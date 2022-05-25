package com.bifrostsmp.heimdall.discord.commands.set;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class Staff extends ListenerAdapter {
    public static void staff(SlashCommandInteractionEvent event) {
        Role role = event.getOption("role").getAsRole();
        ConfigParser.setStaffRole(role.getId());
        ConfigParser.build();
        event.reply("The role that has perms to use all bot commands is " + role.getName()).queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
    }
}
