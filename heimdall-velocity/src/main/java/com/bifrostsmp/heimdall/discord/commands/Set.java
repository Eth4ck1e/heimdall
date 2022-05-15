package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.discord.commands.set.*;
import com.bifrostsmp.heimdall.discord.commands.set.application.Accepted;
import com.bifrostsmp.heimdall.discord.commands.set.application.AppRole;
import com.bifrostsmp.heimdall.discord.commands.set.application.Denied;
import com.bifrostsmp.heimdall.discord.commands.set.application.Pending;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Set extends ListenerAdapter {
    public static void set(SlashCommandInteractionEvent event) {
        if (event.getSubcommandName().equalsIgnoreCase("Accepted")) Accepted.accepted(event);
        if (event.getSubcommandName().equalsIgnoreCase("Denied")) Denied.denied(event);
        if (event.getSubcommandName().equalsIgnoreCase("Pending")) Pending.pending(event);
        if (event.getSubcommandName().equalsIgnoreCase("tickets")) Ticket.ticket(event);
        if (event.getSubcommandName().equalsIgnoreCase("role")) AppRole.role(event);
        if (event.getSubcommandName().equalsIgnoreCase("staff")) Staff.staff(event);
        if (event.getSubcommandName().equalsIgnoreCase("welcome-channel")) WelcomeChannel.welcome(event);
        if (event.getSubcommandName().equalsIgnoreCase("howdy-channel")) Howdy.howdy(event);
        if (event.getSubcommandName().equalsIgnoreCase("rules-channel")) Rules.rules(event);
    }
}
