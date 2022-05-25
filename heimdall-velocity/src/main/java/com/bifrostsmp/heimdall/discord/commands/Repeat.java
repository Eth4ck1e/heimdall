package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class Repeat extends ListenerAdapter {
    public static void repeat(SlashCommandInteractionEvent event, String content) {
        if (event.getUser().isBot()) return;
        if (!event.getName().equalsIgnoreCase("repeat")) return;
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.sendMessage(content).queue();
    }
}
