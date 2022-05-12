package com.bifrostsmp.heimdall.discord.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;

public class TicketClose extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(!event.getButton().getLabel().equalsIgnoreCase("close")) return;
        event.reply("Your ticket will be close shortly, if you have any further issues please don't hesitate to reach out!").queue();
        InteractionHook hook = event.getHook();
        event.getChannel().delete().queueAfter(10, TimeUnit.SECONDS);
    }
}
