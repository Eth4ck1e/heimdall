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
    long memberID = event.getMember().getIdLong();
    long guildID = event.getGuild().getIdLong();
    //event.getGuild().retrieveMemberById(event.getAuthor().getId()).queue();
    if (event.getAuthor().isBot() || !hasRole(memberID, guildID)) return;
    // We don't want to respond to other bot accounts, including ourselves
    Message message = event.getMessage();
    String[] content = message.getContentRaw().split("\\s+");
    if (content.length == 1 || content.length == 2) {
      // failed embed block
      EmbedBuilder info = new EmbedBuilder();
      info.setTitle("Whitelist");
      info.setDescription("Proper command syntax is /whitelist add ign \n If the ign has underscores use \\ as in \\_ign_ ");
      info.setColor(Color.RED);
      MessageChannel channel = event.getChannel(); // get message channel
      channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
      info.clear(); // clear embed from memory
      return;
    }
    if (content[0].equalsIgnoreCase(HeimdallVelocity.prefix + "whitelist")) {

      if (content[1].equalsIgnoreCase("add")) {
        // checks for player name
        if (content[2] != null) {
          String name = content[2];
          String nameNew = name.replaceAll("\\\\", "");
          String id = NameToID.nameToID((nameNew));

          if (id != null) {
            //TODO add code for handling a null return from mojang (no ID)
          }

          ResultSet result = Query.checkPlayers(id);
          if (!result.next()) {
            Query.insertPlayers(nameNew, id);
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
        }
      }
    }
  }

  boolean hasRole(Long userId, Long guild) {
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
