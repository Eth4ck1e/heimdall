package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.*;
import static com.bifrostsmp.heimdall.config.ConfigParser.getStaffCategory;

public class Ticket extends ListenerAdapter {
    static DecimalFormat df = new DecimalFormat("0000");

    public static void ticket(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("ticket")) return;
        event.deferReply().queue();
        Member member = event.getMember();
        InteractionHook hook = event.getHook();
        int ticketNumber = getTicketNumber();
        String channelName = "Ticket-" + df.format(ticketNumber);
        event
                .getGuild()
                .createTextChannel(channelName, getGuild().getCategoryById(getStaffCategory()))
                .addPermissionOverride(event.getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND), null)
                .queue();

        TextChannel channel = event.getGuild().getTextChannelsByName(channelName, true).get(0);
        EmbedBuilder ticket = new EmbedBuilder();
        ticket.setTitle("Ticket");
        ticket.setColor(Color.ORANGE);
        ticket.addField("Welcome", event.getUser().getAsMention() + "\nPlease give us a brief explanation of your issue\nand support will be with you shortly", false);
        hook.sendMessage("Your Ticket has been created look for channel " + channelName).queue(
                message -> {
                    message.delete().queueAfter(30, TimeUnit.SECONDS);
                });
        channel.sendMessageEmbeds(ticket.build()).setActionRow(Button.primary("Close", "Close").withEmoji(Emoji.fromUnicode("U+1F512"))).queue();
        ticket.clear();
        setTicketNumber(ticketNumber + 1);
    }
}
