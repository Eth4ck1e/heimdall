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

import static com.bifrostsmp.heimdall.database.Query.removePlayer;
import static com.bifrostsmp.heimdall.database.Query.updateTrigger;

public class Whitelist extends ListenerAdapter {

  public static void whitelist(SlashCommandInteractionEvent event) {
    event.deferReply().queue();
    InteractionHook hook = event.getHook();
    hook.setEphemeral(true);
    String name;
    String id;
    ResultSet result;
    if (event.getSubcommandName().equalsIgnoreCase("add")) {
      name = event.getOption("player").getAsString();
      id = NameToID.nameToID(name);
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
          hook.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
          info.clear(); // clear embed from memory
        } else {
          // success embed block
          EmbedBuilder info = new EmbedBuilder();
          info.setTitle("Whitelist");
          info.setDescription(name + " is already whitelisted");
          info.setColor(Color.RED);
          hook.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
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
          hook.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
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
      hook.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
      info.clear(); // clear embed from memory
    }
  }
}
//
//  @SneakyThrows
//  @Override
//  public void onMessageReceived(MessageReceivedEvent event) {
//
//    long memberID = event.getMember().getIdLong();
//    long guildID = event.getGuild().getIdLong();
//
//    if (event.getAuthor().isBot() || !hasRole(memberID, guildID)) return;
//    // We don't want to respond to other bot accounts, including ourselves
//    Message message = event.getMessage();
//    String[] content = message.getContentRaw().split("\\s+");
//    if (((content.length == 1 || content.length == 2) &&
// message.getContentRaw().contains("whitelist")) && !message.getContentRaw().contains("update")) {
//      // failed embed block
//      EmbedBuilder info = new EmbedBuilder();
//      info.setTitle("Whitelist");
//      info.setDescription("Proper command syntax is /whitelist add ign. \nIf the ign has
// underscores use \\ as in \\_ign_. \nOther sub options are update, reload, and remove.");
//      info.setColor(Color.RED);
//      MessageChannel channel = event.getChannel(); // get message channel
//      channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
//      info.clear(); // clear embed from memory
//      return;
//    }
//
//    if (content[0].equalsIgnoreCase(HeimdallVelocity.prefix + "whitelist")) {
//
//      if (content[1].equalsIgnoreCase("remove")) {
//        String name = content[2];
//        String nameNew = name.replaceAll("\\\\", "");
//        String id = NameToID.nameToID((nameNew));
//        ResultSet result = Query.checkPlayers(id);
//        if (result.next()) {
//          removePlayer(id);
//          updateTrigger();
//          // success embed block
//          EmbedBuilder info = new EmbedBuilder();
//          info.setTitle("Whitelist");
//          info.setDescription(name + " " + id + " has been removed from the whitelist");
//          info.setColor(Color.GREEN);
//          MessageChannel channel = event.getChannel(); // get message channel
//          channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
//          info.clear(); // clear embed from memory
//        }
//      }
//
//      if (content[1].equalsIgnoreCase("update")) {
//        updateTrigger();
//        EmbedBuilder info = new EmbedBuilder();
//        info.setTitle("Whitelist");
//        info.setDescription("Whitelist update started");
//        info.setColor(Color.GREEN);
//        MessageChannel channel = event.getChannel(); // get message channel
//        channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
//        info.clear(); // clear embed from memory
//      }
//
//      if (content[1].equalsIgnoreCase("reload")) {
//        //TODO add whitelist reload logic
//      }
//
//      if (content[1].equalsIgnoreCase("add")) {
//        // checks for player name
//        if (content[2] != null) {
//          String name = content[2];
//          String nameNew = name.replaceAll("\\\\", "");
//          String id = NameToID.nameToID((nameNew));
//
//          if (id != null) {
//            //TODO add code for handling a null return from mojang (no ID)
//          }
//
//          ResultSet result = Query.checkPlayers(id);
//          if (!result.next()) {
//            Query.insertPlayers(nameNew, id);
//            updateTrigger();
//            // success embed block
//            EmbedBuilder info = new EmbedBuilder();
//            info.setTitle("Whitelist");
//            info.setDescription(name + " " + id + " has been added to the whitelist");
//            info.setColor(Color.GREEN);
//            MessageChannel channel = event.getChannel(); // get message channel
//            channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
//            info.clear(); // clear embed from memory
//
//          } else {
//            // success embed block
//            EmbedBuilder info = new EmbedBuilder();
//            info.setTitle("Whitelist");
//            info.setDescription(name + " " + id + " is already whitelisted");
//            info.setColor(Color.RED);
//            MessageChannel channel = event.getChannel(); // get message channel
//            channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
//            info.clear(); // clear embed from memory
//            // getLogger().log(INFO, ChatColor.YELLOW + "Success");
//          }
//          // END MySQL query
//        }
//      }
//    }
//  }
//
//  boolean hasRole(Long userId, Long guild) {
//    for (int i = 0; i <
// HeimdallVelocity.getDiscordBot().getGuildById(guild).getMemberById(userId).getRoles().size();
// i++) {
//      if (Parse.getRole()
//          .equalsIgnoreCase(
//              HeimdallVelocity.getDiscordBot()
//                  .getGuildById(guild)
//                  .getMemberById(userId)
//                  .getRoles()
//                  .get(i)
//                  .getName())) {
//        hasRole = true;
//      }
//    }
//    return hasRole;
//  }
