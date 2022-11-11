package com.bifrostsmp.heimdall.discord.common;

import database.Query;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.EnumSet;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;
import static com.bifrostsmp.heimdall.config.Config.getStaffCategory;

public class Tickets {
    static DecimalFormat df = new DecimalFormat("0000");
    private static TextChannel channel;
    public static TextChannel newTicket(Member member, InteractionHook hook, Role staff, Guild guild, String ticketType, String fieldMessage) {

        long ticketNumber = Query.getTicketNum() + 1;
        String channelName = ticketType + "-" + df.format(ticketNumber) + " " + member.getEffectiveName();

        guild
                .createTextChannel(channelName, getGuild().getCategoryById(getStaffCategory()))
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND), null)
                .addPermissionOverride(staff, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND), null)
                .queue(ticketChannel -> {
                    channel = ticketChannel;
                    EmbedBuilder ticket = new EmbedBuilder();
                    ticket.setTitle(ticketType);
                    ticket.setColor(Color.ORANGE);
                    ticket.addField("Welcome", member.getAsMention() + "\n" + fieldMessage, false);
                    hook.sendMessage("Your Ticket has been created look for channel " + channelName).queue();
                    ticketChannel.sendMessageEmbeds(ticket.build()).setActionRow(Button.primary("Close", "Close").withEmoji(Emoji.fromUnicode("U+1F512"))).queue();
                    ticket.clear();
                });
        Query.newTicket(member.getEffectiveName());
        return channel;
    }
}
