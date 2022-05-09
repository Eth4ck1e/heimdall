package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.common.YamlParser;
import com.bifrostsmp.heimdall.discord.applications.Questions;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;

public class Apply extends ListenerAdapter {

  private static final ArrayList<String> appAnswers = new ArrayList<>();
  private static final int var = 0;
  private static long ch;

  public static void apply(SlashCommandInteractionEvent event) {
    if (event.getUser().isBot()) return; // returns if the event user is a bot
    if (!event.getInteraction().isFromGuild())
      return; // ensures the command is issued from the guild
    event.deferReply().queue();
    InteractionHook hook = event.getHook();
    hook.setEphemeral(true);
    if (event.getSubcommandName().equalsIgnoreCase("whitelist")) {
      long[] messageId = new long[1];

      if (event.getUser().isBot()) return;
      User user = event.getUser();
      Long userID = user.getIdLong();
      user.openPrivateChannel()
          .queue(
              (Channel) -> {
                event
                    .getChannel()
                    .sendMessage(user.getName() + " application started.")
                    .queue(
                        (message) -> {
                          messageId[0] = message.getIdLong();
                          event
                              .getChannel()
                              .deleteMessageById(messageId[0])
                              .queueAfter(30, TimeUnit.SECONDS);
                          hook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
                        });
                ch = Channel.getIdLong();
                Map<String, Object> getApp =
                    YamlParser.parse(
                        Path.of(getDataDirectory() + "/applications/BifrostSMPApplication.yml"));
                Map<Integer, Object> getQuestionObjects =
                    (Map<Integer, Object>) getApp.get("Questions");

                hook.getInteraction()
                    .getJDA()
                    .addEventListener(new Questions(userID, ch, getQuestionObjects));
                Channel.sendMessage(
                        "Your application has started\nType '!cancel' at any time to cancel your application")
                    .queue(
                        message -> {
                          message.getChannel().sendMessage("What is your minecraft IGN").queue();
                        });
              });
    }
    if (event.getSubcommandName().equalsIgnoreCase("staff")) {
      hook.sendMessage("Staff applications have not been implemented yet")
          .queue(
              message -> {
                message.delete().queueAfter(30, TimeUnit.SECONDS);
              });
    }
  }
}
