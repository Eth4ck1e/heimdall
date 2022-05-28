package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.discord.commands.set.*;
import com.bifrostsmp.heimdall.discord.commands.set.application.SetAcceptedChannel;
import com.bifrostsmp.heimdall.discord.commands.set.application.SetAppRole;
import com.bifrostsmp.heimdall.discord.commands.set.application.SetDeniedChannel;
import com.bifrostsmp.heimdall.discord.commands.set.application.SetPendingChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Set extends ListenerAdapter {
    public static void set(SlashCommandInteractionEvent event) {
        if (event.getSubcommandName().equalsIgnoreCase("Accepted")) SetAcceptedChannel.accepted(event);
        if (event.getSubcommandName().equalsIgnoreCase("Denied")) SetDeniedChannel.denied(event);
        if (event.getSubcommandName().equalsIgnoreCase("Pending")) SetPendingChannel.pending(event);
        if (event.getSubcommandName().equalsIgnoreCase("tickets")) SetTicketCategory.ticket(event);
        if (event.getSubcommandName().equalsIgnoreCase("role")) SetAppRole.role(event);
        if (event.getSubcommandName().equalsIgnoreCase("staff")) SetStaffRole.staff(event);
        if (event.getSubcommandName().equalsIgnoreCase("welcome-channel")) SetWelcomeChannel.welcome(event);
        if (event.getSubcommandName().equalsIgnoreCase("howdy-channel")) SetHowdyChannel.howdy(event);
        if (event.getSubcommandName().equalsIgnoreCase("rules-channel")) SetRulesChannel.rules(event);
    }
}
