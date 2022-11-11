package com.bifrostsmp.heimdall.discord.applications;

import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import database.Query;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.config.Config.getDiscordId;
import static com.bifrostsmp.heimdall.config.Config.isHeimdallDebug;
import static com.bifrostsmp.heimdall.discord.commands.Apply.getMessage;

public class Questions extends ListenerAdapter {
    private final Long userID;
    private final long channel;
    private final Map<Integer, Object> getQuestions;
    private final ArrayList<String> answers = new ArrayList<>();
    private final ArrayList<String> application = new ArrayList<>();
    int checkIGN;
    int count;
    String IGN;
    String uuid;
    String type;
    Map<Integer, Object> getDetails;
    private long messageID;
    private int i;

    public Questions(Long userID, long ch, Map<Integer, Object> getApp) {
        this.userID = userID;
        this.channel = ch;
        this.getQuestions = getApp;
        i = 0;
        checkIGN = 0;
        count = 0;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Long d = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (i > getQuestions.size() + 1) return;
        if (e.getAuthor().isBot()) return;
        if (!(e.getChannel().getIdLong() == channel)) return;
        if (!(e.getAuthor().getIdLong() == userID)) return;
        if (e.getMessage().getContentRaw().equalsIgnoreCase("!cancel")) {
            i = 99;
            e.getChannel().sendMessage("Your application has been canceled").queue();
            return;
        }
        Guild guild = e.getJDA().getGuildById(getDiscordId());
        assert guild != null;
        //MessageChannel tc = guild.getTextChannelById(ConfigParser.getAppPending());
        User author = e.getAuthor();
        switch (checkIGN) {
            case 0 -> {
                IGN = e.getMessage().getContentRaw();
                uuid = NameToID.nameToID(IGN);
                if (uuid != null) {
                    checkIGN = 1;
                    count = 0;
                    //answers.add(IGN);
                } else {
                    count++;
                    getMessage().editMessage("Please check the spelling of your IGN and try again " + count).queue();
                    //e.getChannel().sendMessage("Please check the spelling of your IGN and try again").queue();
                    if (count > 3) {
                        getMessage().editMessage("You have entered an invalid IGN to many times.").queue();
                        i = 99;
                    }
                    e.getMessage().delete().queue();
                    return;
                }
            }
            case 1 -> {
                switch (type = getDetails.get(2).toString()) {
                    case "number" -> {
                        if (isNumeric(e.getMessage().getContentRaw())) {
                            count = 0;
                            break;
                        }
                        count++;
                        if (count > 3) {
                            getMessage().editMessage("You have entered an invalid number to many times.").queue();
                            i = 99;
                            e.getMessage().delete().queue();
                            return;
                        }
                        e.getMessage().delete().queue();
                        getMessage().editMessage("You must enter a valid number " + count).queue();
                        return;
                    }
                    case "short" -> {
                        if (e.getMessage().getContentRaw().length() < 280) {
                            count = 0;
                            break;
                        }
                        count++;
                        if (count > 3) {
                            getMessage().editMessage("You have failed to follow instructions, please try again").queue();
                            e.getMessage().delete().queue();
                            i = 99;
                            return;
                        }
                        e.getMessage().delete().queue();
                        getMessage().editMessage("Please keep this response brief (less than 280 characters) " + count).queue();
                        return;
                    }
                    case "long" -> {
                    }
                    case "choice" -> {
                        if (isHeimdallDebug()) {
                            System.out.println("[Heimdall] DEBUG: The choice type is not yet implemented");
                        }
                        //e.getChannel().sendMessage("This question has choices, please be a better coder and add them").queue();
                    }
                    default -> {
                        getMessage().editMessage("There is an error in the application syntax. Please notify an administrator").queue();
                    }
                }
            }
        }
        answers.add(e.getMessage().getContentRaw());
        e.getMessage().delete().queue();

        if (answers.size() == getQuestions.size()) {
            getMessage().editMessage("Thank you! Your application has been submitted").queue(
                    message -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                    }
            );
            i = 99;
            EmbedBuilder app = new EmbedBuilder();
            app.setFooter(String.valueOf(userID));
            //app.setTitle("Application");
            app.setColor(Color.YELLOW);
            for (int x = 0; x < answers.size(); x++) {
                getDetails = (Map<Integer, Object>) getQuestions.get(x);
                app.addField(getDetails.get(1).toString(), answers.get(x), false);
                application.add(getDetails.get(1).toString() + ": " + answers.get(x));
            }
            //assert tc != null;

            if (Query.checkForApp(userID)) {
                if (isHeimdallDebug()) {
                    System.out.println("[Heimdall] DEBUG: " + Query.checkForApp(userID));
                }
                int counter = Query.getAppCounter(userID);
                if (isHeimdallDebug()) {
                    System.out.println("[Heimdall] DEBUG: " + counter);
                }
                counter++;
                Query.updateApp(userID, String.valueOf(application), counter);
                e.getChannel().sendMessageEmbeds(app.setTitle("Application " + counter + " for " + author.getName()).build()).setActionRow(Button.primary("Accept", "Accept"), Button.primary("Deny", "Deny")).queue();
            } else {
                int counter = 1;
                Query.insertApp(userID, IGN, uuid, String.valueOf(application), counter);
                e.getChannel().sendMessageEmbeds(app.setTitle("Application " + counter + " for " + author.getName()).build()).setActionRow(Button.primary("Accept", "Accept"), Button.primary("Deny", "Deny")).queue();
            }
            //e.getChannel().sendMessageEmbeds(app.build()).queue();
            if (isHeimdallDebug()) {
                System.out.println("[Heimdall] DEBUG: " + answers);
            }
            app.clear();
            return;
        }
        if (i < 1) {
            i++;
            getDetails = (Map<Integer, Object>) getQuestions.get(i);
            getMessage().editMessage(getDetails.get(1).toString()).queue();
        } else {
            i++;
            getDetails = (Map<Integer, Object>) getQuestions.get(i);
            getMessage().editMessage(getDetails.get(1).toString()).queue();
        }
    }
}