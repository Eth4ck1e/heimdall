package com.bifrostsmp.heimdall.discord.rules;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;

public class ClickMe extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getButton().getLabel().equalsIgnoreCase("click me")) return;
        getGuild().addRoleToMember(event.getUser(), getGuild().getRolesByName(ConfigParser.getAppRole(), true).get(0)).queue();
        event.reply("Thank you, you have been given the " + ConfigParser.getAppRole() + " role").queue(
                message -> {
                    message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
                }
        );
    }
}
