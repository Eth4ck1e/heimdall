package com.bifrostsmp.heimdall.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;

public class Invite extends ListenerAdapter {
    public static void invite(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        if (!event.getName().equalsIgnoreCase("invite")) return;
        InteractionHook hook = event.getHook();
        event
                .getGuild()
                .getTextChannelsByName(event.getChannel().getName(), true).get(0)
                .getManager()
                .getChannel()
                .createPermissionOverride(event.getOption("user").getAsMember())
                .setAllow(Permission.VIEW_CHANNEL)
                .setAllow(Permission.MESSAGE_HISTORY)
                .setAllow(Permission.MESSAGE_SEND)
                .queue();
        event.reply(event.getOption("user").getAsMember().getAsMention() + " has been added to the ticket!").queue(
                message -> {
                    message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                });
//        hook.getJDA().getEventManager().unregister(hook.getJDA());
//        event.getJDA().removeEventListener(event);
    }
}
