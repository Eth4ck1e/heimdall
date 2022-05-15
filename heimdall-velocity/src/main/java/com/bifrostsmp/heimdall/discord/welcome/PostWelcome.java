package com.bifrostsmp.heimdall.discord.welcome;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;
import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;

public class PostWelcome {

    private static Message welcomeMessage;

    public static void postWelcome() {
        TextChannel channel = getGuild().getTextChannelById(ConfigParser.getWelcomeChannel());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);
        embed.setTitle("Welcome!");
        Path filePath = Path.of(getDataDirectory() + "/welcome/welcome.txt");
        String content = null;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        embed.setDescription(content);
        channel.sendMessageEmbeds(embed.build()).queue(
                PostWelcome::setWelcomeMessage
        );
    }

    private static void setWelcomeMessage(Message message) {
        welcomeMessage = message;
    }

    public static Message getWelcomeMessage() {
        return welcomeMessage;
    }
}
