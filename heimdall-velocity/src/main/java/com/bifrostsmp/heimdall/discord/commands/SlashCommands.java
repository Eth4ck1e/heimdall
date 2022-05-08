package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.HeimdallVelocity;
import com.bifrostsmp.heimdall.config.Parser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SlashCommands extends ListenerAdapter {
  boolean hasRole;

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    if (event.getGuild() == null) return;
    if (event.getUser().isBot()) return;
    long memberID = event.getMember().getIdLong();
    long guildID = event.getGuild().getIdLong();
    // Only accept commands from guilds

    String command = event.getName().toLowerCase();
    switch (command) {
      case "repeat" -> {
        Repeat.repeat(
                event,
                event.getOption("content").getAsString()); // content is required so no null-check here
      }
      case "whitelist" -> {
        if (!hasRole(memberID, guildID, Parser.getStaffRole())) {
          event.reply("You must have Staff or above role to use this command!").queue(
                  message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                  });
          return;
        }
        Whitelist.whitelist(event);
      }
      case "apply" -> {
        if (!hasRole(memberID, guildID, Parser.getAppRole())) {
          event.reply("You do not have the applicant role!").queue(
                  message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                  });
          return;
        }
        Apply.apply(event);
      }
      case "info" -> {
        Info.info(event);
      }
      case "ping" -> {
        PingPong.pingPong(event);
      }
    }
  }

  boolean hasRole(Long userId, Long guild, String role) {
    for (int i = 0;
        i
            < HeimdallVelocity.getDiscordBot()
                .getGuildById(guild)
                .getMemberById(userId)
                .getRoles()
                .size();
        i++) {
      if (role
          .equalsIgnoreCase(
              HeimdallVelocity.getDiscordBot()
                  .getGuildById(guild)
                  .getMemberById(userId)
                  .getRoles()
                  .get(i)
                  .getName())) {
        hasRole = true;
      }
    }
    return hasRole;
  }
}
