package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.text.DecimalFormat;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDiscordBot;
import static com.bifrostsmp.heimdall.config.Config.getStaffRole;
import static com.bifrostsmp.heimdall.discord.common.Tickets.newTicket;

public class Ticket extends ListenerAdapter {
    static DecimalFormat df = new DecimalFormat("0000");

    public static void ticket(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("ticket")) return;
        event.deferReply().queue();
        Member member = event.getMember();
        InteractionHook hook = event.getHook();
        Role staff = getDiscordBot().getRoleById(getStaffRole());
        Guild guild = event.getGuild();
        String fieldMessage = "Please give us a brief explanation of your issue\nand support will be with you shortly";
        TextChannel channel = newTicket(member, hook, staff, guild, "ticket", fieldMessage, null);
    }
}
