package com.bifrostsmp.heimdall.discord.applications;

import com.bifrostsmp.heimdall.config.Parser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ResponseHandler extends ListenerAdapter {
  private final long applicant;
  private final long member;
  private final MessageEmbed embed;
  private int i;

  public ResponseHandler(long applicant, long member, MessageEmbed embed) {
    this.applicant = applicant;
    this.member = member;
    this.embed = embed;
    int i = 0;
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent e) {
    if (e.getAuthor().isBot()) return;
    if (e.getAuthor().getIdLong() != member) return;
    if (i > 0) return;

    MessageChannel denyChannel =
        e.getJDA()
            .getGuildById(Parser.getDiscordId())
            .getTextChannelsByName("denied-applications", true)
            .get(0);
    String response = e.getMessage().getContentRaw();
    e.getMessage()
        .getChannel()
        .sendMessage("Your reason has been noted: " + response)
        .queue(
            message -> {
              message.delete().queueAfter(30, TimeUnit.SECONDS);
            });
    e.getJDA()
        .openPrivateChannelById(applicant)
        .queue(
            message -> {
              EmbedBuilder deny = new EmbedBuilder();
              deny.setTitle("Your Bifrost Application has been Denied");
              deny.addField("Reason: ", response, false);
              deny.setDescription("Please review the rules");
              deny.setFooter("Applicants can be banned after three denied applications");
              message.sendMessageEmbeds(deny.build()).queue();
            });

    denyChannel
        .sendMessageEmbeds(embed)
        .queue(
            message -> {
              message
                  .getChannel()
                  .sendMessage("Application Denied for reason: " + response)
                  .queue();
            });
    i++;
  }
}