package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.database.Query.removePlayer;
import static com.bifrostsmp.heimdall.database.Query.updateTrigger;

public class Whitelist extends ListenerAdapter {

  public static void whitelist(SlashCommandInteractionEvent event) {
    if (event.getUser().isBot()) return;
    if (!event.getInteraction().isFromGuild()) return;
    event.deferReply().queue();
    InteractionHook hook = event.getHook();
    hook.setEphemeral(false);
    String name;
    String id;
    ResultSet result;
    if (event.getSubcommandName().equalsIgnoreCase("add")) {
      name = event.getOption("player").getAsString();
      id = NameToID.nameToID(name);
      if (id == null) {
        // success embed block
        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Whitelist");
        info.setDescription(name + " is not a valid IGN");
        info.setColor(Color.RED);
        hook.sendMessageEmbeds(info.build())
                .queue(
                        message -> {
                          message.delete().queueAfter(30, TimeUnit.SECONDS);
                        }); // send embed to message channel
        info.clear(); // clear embed from memory
        return;
      }
      try {
        result = Query.checkPlayers(id);
        if (!result.next()) {
          Query.insertPlayers(name, id);
          updateTrigger();
          // success embed block
          EmbedBuilder info = new EmbedBuilder();
          info.setTitle("Whitelist");
          info.setDescription(name + " has been added to the whitelist");
          info.setColor(Color.GREEN);
          hook.sendMessageEmbeds(info.build())
              .queue(
                  message -> {
                    message.delete().queueAfter(30, TimeUnit.SECONDS);
                  }); // send embed to message channel
          info.clear(); // clear embed from memory
        } else {
          // success embed block
          EmbedBuilder info = new EmbedBuilder();
          info.setTitle("Whitelist");
          info.setDescription(name + " is already whitelisted");
          info.setColor(Color.RED);
          hook.sendMessageEmbeds(info.build())
              .queue(
                  message -> {
                    message.delete().queueAfter(30, TimeUnit.SECONDS);
                  }); // send embed to message channel
          info.clear(); // clear embed from memory
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (event.getSubcommandName().equalsIgnoreCase("remove")) {
      name = event.getOption("player").getAsString();
      id = NameToID.nameToID(name);
      try {
        result = Query.checkPlayers(id);
        if (result.next()) {
          removePlayer(id);
          updateTrigger();
          // success embed block
          EmbedBuilder info = new EmbedBuilder();
          info.setTitle("Whitelist");
          info.setDescription(name + " has been removed from the whitelist");
          info.setColor(Color.GREEN);
          hook.sendMessageEmbeds(info.build())
              .queue(
                  message -> {
                    message.delete().queueAfter(30, TimeUnit.SECONDS);
                  }); // send embed to message channel
          info.clear(); // clear embed from memory
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (event.getSubcommandName().equalsIgnoreCase("update")) {
      try {
        updateTrigger();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      EmbedBuilder info = new EmbedBuilder();
      info.setTitle("Whitelist");
      info.setDescription("Whitelist update started");
      info.setColor(Color.GREEN);
      hook.sendMessageEmbeds(info.build())
          .queue(
              message -> {
                message.delete().queueAfter(30, TimeUnit.SECONDS);
              }); // send embed to message channel
      info.clear(); // clear embed from memory
    }
  }
}

