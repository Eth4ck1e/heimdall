package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.HeimdallVelocity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingPong extends ListenerAdapter {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;
    // We don't want to respond to other bot accounts, including ourselves
    Message message = event.getMessage();
    String[] content = ((Message) message).getContentRaw().split("\\s+");
    if (content[0].equalsIgnoreCase(HeimdallVelocity.prefix + "ping")) {
      MessageChannel channel = event.getChannel();
      channel
          .sendMessage("Pong!")
          .queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
    }
  }
}
