package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.discord.applications.Parser;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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
  private final EventWaiter waiter;
  private final String name;
  private final String[] aliases;
  private final String help;
  private static final ArrayList<String> appAnswers = new ArrayList<>();
  private static int var = 0;
  private static long ch;

  public Apply(EventWaiter waiter) {
    this.waiter = waiter;
    this.name = "hello";
    this.aliases = new String[] {"hi"};
    this.help = "says hello and waits for response";
  }

  public static void apply(SlashCommandInteractionEvent event) {
    if (event.getUser().isBot()) return;
    if (!event.getInteraction().isFromGuild()) return;
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
                    Parser.parse(
                        Path.of(getDataDirectory() + "/applications/BifrostSMPApplication.yml"));
                Map<Integer, Object> getQuestionObjects =
                    (Map<Integer, Object>) getApp.get("Questions");

                hook.getInteraction()
                    .getJDA()
                    .addEventListener(new Questions(userID, ch, getQuestionObjects));
                Channel.sendMessage("Your application has started\nType '!cancel at any time to cancel you application'").queue(message -> {
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
