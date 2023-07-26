package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.discord.commands.set.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Set extends ListenerAdapter {
    public static void set(SlashCommandInteractionEvent event) {
        if (event.getSubcommandName().equalsIgnoreCase("staff")) SetStaffRole.staff(event);
    }
}
