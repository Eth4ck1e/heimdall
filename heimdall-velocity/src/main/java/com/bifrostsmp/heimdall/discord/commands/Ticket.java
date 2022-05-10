package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Ticket extends ListenerAdapter {

    static Random rand = new Random();
    static DecimalFormat df = new DecimalFormat("0000");

    public static void ticket(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        if (!event.getName().equalsIgnoreCase("ticket")) return;
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        String channelName = "Ticket-" + df.format(rand.nextInt(1000));
        event.getGuild().createTextChannel(channelName).complete();
        TextChannel channel = event.getGuild().getTextChannelsByName(channelName,true).get(0);
        EmbedBuilder ticket = new EmbedBuilder();
        ticket.setTitle("Ticket");
        ticket.setColor(Color.ORANGE);
        ticket.addField("Welcome", event.getUser().getAsMention() + "\nPlease give us a brief explanation of your issue\nand support will be with you shortly", false);
        ticket.setFooter(event.getUser().getId());
        hook.sendMessage("Your Ticket has been created look for channel " + channelName).queue(
                message -> {
                    message.delete().queueAfter(30, TimeUnit.SECONDS);
                });
        channel.sendMessageEmbeds(ticket.build()).setActionRow(Button.primary("Close", "Close").withEmoji(Emoji.fromUnicode("U+1F512"))).queue();
        ticket.clear();

        //TODO set channel permissions so that only staff and up as well as the user can see and chat in the channel
        //TODO create slash command for staff and possibly members to be able to add members to the channel
    }
}
