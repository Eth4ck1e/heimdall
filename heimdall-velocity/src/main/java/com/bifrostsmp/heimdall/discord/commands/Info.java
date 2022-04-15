package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.HeimdallVelocity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class Info extends ListenerAdapter {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;
    // We don't want to respond to other bot accounts, including ourselves
    Message message = event.getMessage(); // get message from discord event listener
    String[] content = message.getContentRaw().split("\\s+");
    if (content[0].equalsIgnoreCase(HeimdallVelocity.prefix + "info")) {
      // embed block
      EmbedBuilder info = new EmbedBuilder();
      info.setTitle("🤖 Information 🤖");
      info.setDescription("A Bot to connect all the realms, a Bifrost Bot");
      info.addField("Developers", "Eth4ck1e, HunnaG", false);
      info.setColor(Color.GREEN);
      MessageChannel channel = event.getChannel(); // get message channel
      channel.sendMessageEmbeds(info.build()).queue(); // send embed to message channel
      info.clear(); // clear embed from memory
    }
  }
}
