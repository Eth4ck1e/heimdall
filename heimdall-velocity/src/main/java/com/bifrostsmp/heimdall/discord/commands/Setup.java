package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Setup extends ListenerAdapter {
    public static void setup(SlashCommandInteractionEvent event) {
        event.reply("To setup the bot use the /set commands in the specific text channel you want to set for that action\n/set Tickets will provide a drop down selection for tickets\nand /set application role can be run anywhere").queue();
    }
}
