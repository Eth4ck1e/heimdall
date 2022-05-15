package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class Welcome extends ListenerAdapter {
    public static void welcome(SlashCommandInteractionEvent event) {

        ReplyCallbackAction reply = event.reply("Set True or False");
        SelectMenu.Builder menu = SelectMenu.create("boolean");
        menu.addOption("True", "true").addOption("False", "false");
        reply.addActionRow(menu.build()).queue();
    }

    @Override
    public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
        if (!event.getComponentId().equals("boolean")) return;
        event.reply("You chose " + event.getValues().get(0)).queue();
        ConfigParser.setWelcomeMessages(Boolean.parseBoolean(event.getValues().get(0)));
        ConfigParser.build();
    }
}
