package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingPong extends ListenerAdapter {

  public static void pingPong(SlashCommandInteractionEvent event) {
    if (event.getUser().isBot()) return;
    if (!event.getName().equalsIgnoreCase("ping")) return;
    // We don't want to respond to other bot accounts, including ourselves
      MessageChannel channel = event.getChannel();
      event.reply("Pong!").queue();
  }
}

