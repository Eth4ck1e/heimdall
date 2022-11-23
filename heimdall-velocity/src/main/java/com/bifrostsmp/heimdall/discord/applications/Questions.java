package com.bifrostsmp.heimdall.discord.applications;

import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import database.Query;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
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
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (i > getQuestions.size() + 1) return;
        if (!(event.getChannel().getIdLong() == channel)) return;
        if (!(event.getAuthor().getIdLong() == userID)) return;
        if (event.getMessage().getContentRaw().equalsIgnoreCase("!cancel")) {
            i = 99;
            event.getChannel().sendMessage("Your application has been canceled").queue();
            closeEvent(event);
            return;
        }
        Guild guild = event.getJDA().getGuildById(getDiscordId());
        assert guild != null;
        User author = event.getAuthor();
        //
        switch (checkIGN) {
            case 0 -> {
                IGN = event.getMessage().getContentRaw();
                uuid = NameToID.nameToID(IGN);
                if (uuid != null) {
                    checkIGN = 1;
                    count = 0;
                } else {
                    count++;
                    getMessage().editMessage("Please check the spelling of your IGN and try again " + count).queue();
                    if (count > 3) {
                        getMessage().editMessage("You have entered an invalid IGN to many times.").queue();
                        i = 99;
                    }
                    event.getMessage().delete().queue();
                    return;
                }
            }
            case 1 -> {
                switch (type = getDetails.get(2).toString()) {
                    case "number" -> {
                        if (isNumeric(event.getMessage().getContentRaw())) {
                            count = 0;
                            break;
                        }
                        count++;
                        if (count > 3) {
                            getMessage().editMessage("You have entered an invalid number to many times.").queue();
                            i = 99;
                            event.getMessage().delete().queue();
                            return;
                        }
                        event.getMessage().delete().queue();
                        getMessage().editMessage("You must enter a valid number " + count).queue();
                        return;
                    }
                    case "short" -> {
                        if (event.getMessage().getContentRaw().length() < 280) {
                            count = 0;
                            break;
                        }
                        count++;
                        if (count > 3) {
                            getMessage().editMessage("You have failed to follow instructions, please try again").queue();
                            event.getMessage().delete().queue();
                            i = 99;
                            return;
                        }
                        event.getMessage().delete().queue();
                        getMessage().editMessage("Please keep this response brief (less than 280 characters) " + count).queue();
                        return;
                    }
                    case "long" -> {
                    }
                    case "choice" -> {
                        if (isHeimdallDebug()) {
                            System.out.println("[Heimdall] DEBUG: The choice type is not yet implemented");
                        }
                    }
                    default -> {
                        getMessage().editMessage("There is an error in the application syntax. Please notify an administrator").queue();
                    }
                }
            }
        }
        answers.add(event.getMessage().getContentRaw());
        event.getMessage().delete().queue();

        //Application completion code
        if (answers.size() == getQuestions.size()) {
            getMessage().editMessage("Thank you! Your application has been submitted").queue(
                    message -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                    }
            );
            i = 99;  //Set i variable to 99 so no further replies can trigger the event listener
            EmbedBuilder app = new EmbedBuilder();
            app.setFooter(String.valueOf(userID));
            app.setColor(Color.YELLOW);
            //Loop through each application question and add the question and answer to a field in the app embed
            for (int x = 0; x < answers.size(); x++) {
                getDetails = (Map<Integer, Object>) getQuestions.get(x);
                app.addField(getDetails.get(1).toString(), answers.get(x), false);
                application.add(getDetails.get(1).toString() + ": " + answers.get(x));
            }
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
                event.getChannel().sendMessageEmbeds(app.setTitle("Application " + counter + " for " + author.getName()).build()).setActionRow(Button.primary("Accept", "Accept"), Button.primary("Deny", "Deny")).queue();
            } else {
                int counter = 1;
                Query.insertApp(userID, IGN, uuid, String.valueOf(application), counter);
                event.getChannel().sendMessageEmbeds(app.setTitle("Application " + counter + " for " + author.getName()).build()).setActionRow(Button.primary("Accept", "Accept"), Button.primary("Deny", "Deny")).queue();
            }
            if (isHeimdallDebug()) {
                System.out.println("[Heimdall] DEBUG: " + answers);
            }
            app.clear();
            closeEvent(event);
            return;
        }
        i++;
        getDetails = (Map<Integer, Object>) getQuestions.get(i);
        getMessage().editMessage(getDetails.get(1).toString()).queue();
    }

    public static void closeEvent(Event event) {
        event.getJDA().removeEventListener();
    }
}