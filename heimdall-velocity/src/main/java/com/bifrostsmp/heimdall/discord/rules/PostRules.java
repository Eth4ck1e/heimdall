package com.bifrostsmp.heimdall.discord.rules;

import com.bifrostsmp.heimdall.common.YamlParser;
import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;
import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;

public class PostRules {
    private static final Path discordRules = Path.of(getDataDirectory() + "/rules/discord.yml");
    private static final Path minecraftRules = Path.of(getDataDirectory() + "/rules/minecraft.yml");
    private static final Map<String, Object> getDiscordRules =
            YamlParser.parse(discordRules);
    private static final Map<String, Object> getMinecraftRules =
            YamlParser.parse(minecraftRules);
    private static Message discordMessage;

    private static Message minecraftMessage;

    private static Message finalMessage;

    public static void postRules() {
        TextChannel channel = getGuild().getTextChannelById(ConfigParser.getRulesChannel());
        EmbedBuilder discord = new EmbedBuilder();
        discord.setTitle("DISCORD RULES");
        discord.setColor(Color.BLUE);
        Map<String, Object> discordRules = (Map<String, Object>) getDiscordRules.get("rules");
        for (int i = 0; i < discordRules.size(); i++) {
            ArrayList nestedArray = (ArrayList) discordRules.get(i);
            Map<String, Object> rule = (Map<String, Object>) nestedArray.get(0);
            discord.addField("", "#" + (i + 1) + " " + rule.get("name").toString(), true);
            Map<Integer, String> details = (Map<Integer, String>) rule.get("details");
            for (int x = 1; x <= details.size(); x++) {
                discord.addField("", "* " + details.get(x), false);
            }
        }
        channel.sendMessageEmbeds(discord.build()).queue(
                PostRules::setDiscordMessage);
        discord.clear();

        EmbedBuilder minecraft = new EmbedBuilder();
        minecraft.setTitle("MINECRAFT RULES");
        minecraft.setColor(Color.GREEN);
        Map<String, Object> minecraftRules = (Map<String, Object>) getMinecraftRules.get("rules");
        for (int i = 0; i < minecraftRules.size(); i++) {
            ArrayList nestedArray = (ArrayList) minecraftRules.get(i);
            Map<String, Object> rule = (Map<String, Object>) nestedArray.get(0);
            minecraft.addField("", "#" + (i + 1) + " " + rule.get("name").toString(), true);
            Map<Integer, String> details = (Map<Integer, String>) rule.get("details");
            for (int x = 1; x <= details.size(); x++) {
                minecraft.addField("", "* " + details.get(x), false);
            }
        }
        channel.sendMessageEmbeds(minecraft.build()).queue(
                PostRules::setMinecraftMessage);
        minecraft.clear();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setTitle("RULES AGREEMENT");
        Path filePath = Path.of(getDataDirectory() + "/rules/finalMessage.txt");
        String content = null;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        embed.setDescription(content);
        channel.sendMessageEmbeds(embed.build()).setActionRow(Button.primary("clickMe", "Click Me")).queue(
                PostRules::setFinalMessage
        );
    }

    private static void setDiscordMessage(Message message) {
        PostRules.discordMessage = message;
    }

    public static Message getDiscordMessage() {
        return discordMessage;
    }

    public static void setMinecraftMessage(Message minecraftMessage) {
        PostRules.minecraftMessage = minecraftMessage;
    }

    public static Message getMinecraftMessage() {
        return minecraftMessage;
    }

    public static Message getFinalMessage() {
        return finalMessage;
    }

    public static void setFinalMessage(Message finalMessage) {
        PostRules.finalMessage = finalMessage;
    }
}
