package com.bifrostsmp.heimdall.discord.buttons;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;

public class RulesClickMe extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getComponentId().equalsIgnoreCase("click me")) return;
        getGuild().addRoleToMember(event.getUser(), getGuild().getRolesByName(ConfigParser.getAppRole(), true).get(0)).queue();
        event.reply("Thank you, you have been given the " + ConfigParser.getAppRole() + " role").queue(
                message -> {
                    message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
                }
        );
    }
}
