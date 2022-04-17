package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.HeimdallVelocity;
import com.bifrostsmp.heimdall.config.Parse;
import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.ResultSet;

public class WhitelistAdd extends ListenerAdapter {

  boolean hasRole;

  @SneakyThrows
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    //Guild guild = event.getGuild();
    //Role role = guild.getRoleById("960690838894166096");
    if (event.getAuthor().isBot() || !hasRole(event.getAuthor().getId(), event.getGuild().getIdLong())) return;
    // We don't want to respond to other bot accounts, including ourselves
    Message message = event.getMessage();
    String[] content = message.getContentRaw().split("\\s+");
    // Checks for proper /whitelist add syntax
    if (content[0].equalsIgnoreCase(HeimdallVelocity.prefix + "whitelist")
        && content[1].equalsIgnoreCase("add")) {
      // checks for player name
      if (content[2] != null) {
        String name = content[2];
        String id = NameToID.nameToID((name));

        if (id != null) {}

        ResultSet result = Query.checkPlayers(id);
        if (!result.next()) {
          Query.insertPlayers(name, id);
          Query.updateTrigger();
          // success embed block
          EmbedBuilder info = new EmbedBuilder();
          info.setTitle("Whitelist");
          info.setDescription(name + " " + id + " has been added to the whitelist");
          info.setColor(Color.GREEN);
          MessageChannel channel = event.getChannel(); // get message channel
          channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
          info.clear(); // clear embed from memory

        } else {
          // success embed block
          EmbedBuilder info = new EmbedBuilder();
          info.setTitle("Whitelist");
          info.setDescription(name + " " + id + " is already whitelisted");
          info.setColor(Color.RED);
          MessageChannel channel = event.getChannel(); // get message channel
          channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
          info.clear(); // clear embed from memory
          // getLogger().log(INFO, ChatColor.YELLOW + "Success");
        }
        // END MySQL query

        // if syntax error the following will run
      } else {
        // failed embed block
        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Whitelist");
        info.setDescription("You must add an IGN such as /whitelist add playerName");
        info.setColor(Color.RED);
        MessageChannel channel = event.getChannel(); // get message channel
        channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
        info.clear(); // clear embed from memory
      }
    }
  }

  boolean hasRole(String userId, Long guild) {
    for (int i = 0; i < HeimdallVelocity.getDiscordBot().getGuildById(guild).getMemberById(userId).getRoles().size(); i++) {
      if (Parse.getRole()
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
