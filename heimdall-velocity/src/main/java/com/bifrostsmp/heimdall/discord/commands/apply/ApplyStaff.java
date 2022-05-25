package com.bifrostsmp.heimdall.discord.commands.apply;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;

public class ApplyStaff extends ListenerAdapter {
    public static void staff(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        hook.sendMessage("Staff applications have not been implemented yet")
                .queue(
                        message -> {
                            message.delete().queueAfter(30, TimeUnit.SECONDS);
                        });
    }
}
