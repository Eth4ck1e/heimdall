package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.discord.commands.apply.ApplyStaff;
import com.bifrostsmp.heimdall.discord.commands.apply.ApplyWhitelist;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class Apply extends ListenerAdapter {

    private static final ArrayList<String> appAnswers = new ArrayList<>();
    private static final int var = 0;
    private static long ch;

    public static void apply(SlashCommandInteractionEvent event) {

        if (event.getSubcommandName().equalsIgnoreCase("whitelist")) ApplyWhitelist.whitelist(event);
        if (event.getSubcommandName().equalsIgnoreCase("staff")) ApplyStaff.staff(event);

    }
}
