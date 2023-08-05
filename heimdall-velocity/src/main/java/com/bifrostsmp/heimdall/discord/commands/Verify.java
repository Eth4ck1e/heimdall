package com.bifrostsmp.heimdall.discord.commands;

import database.Query;
import mojangAPI.NameToID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Verify extends ListenerAdapter {

    public static void verify(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        Member member = event.getMember();
//        Role unverified = getDiscordBot().getRolesByName("unverified", true).get(0);
        String name = event.getOption("ign").getAsString();
        String id = NameToID.nameToID(name);
        Role addRole = event.getGuild().getRolesByName("verified", true).get(0);
        Role removeRole = event.getGuild().getRolesByName("Unverified", true).get(0);
        EmbedBuilder info = new EmbedBuilder();
        if (id == null) {
            // success embed block
            info.setTitle("IGN Verification");
            info.setDescription(name + " is not a valid IGN");
            info.setColor(Color.RED);
            hook.sendMessageEmbeds(info.build())
                    .queue(
                            message -> {
                                message.delete().queueAfter(10, TimeUnit.SECONDS);
                            }); // send embed to message channel
            info.clear(); // clear embed from memory
            return;
        }
        Query.insertPlayer(name, id, event.getMember().getId());
        hook.getInteraction().getGuild().addRoleToMember(member.getUser(), addRole).queue();
        hook.getInteraction().getGuild().removeRoleFromMember(member.getUser(), removeRole).queue();
        info.setTitle("IGN Verification");
        info.setDescription(name + " is valid. You should now have the Verified Role. Please continue following the instructions in the welcome channel");
        info.setColor(Color.GREEN);
        hook.sendMessageEmbeds(info.build())
                .queue(
                        message -> {
                            message.delete().queueAfter(10, TimeUnit.SECONDS);
                        }); // send embed to message channel
        info.clear(); // clear embed from memory
    }
}
