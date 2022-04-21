package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.HeimdallVelocity;
import com.bifrostsmp.heimdall.config.Parse;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommands extends ListenerAdapter {
    boolean hasRole;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        long memberID = event.getMember().getIdLong();
        long guildID = event.getGuild().getIdLong();
        if (event.getUser().isBot() || !hasRole(memberID, guildID)) return;
        // Only accept commands from guilds
        if (event.getGuild() == null) return;
        if (event.getName().equalsIgnoreCase("repeat")) {
        Repeat.repeat(
          event,
          event.getOption("content").getAsString()); // content is required so no null-check here
        }
        if (event.getName().equalsIgnoreCase("whitelist")) {
            Whitelist.whitelist(event);
        }
    }

    boolean hasRole(Long userId, Long guild) {
        for (int i = 0; i < HeimdallVelocity.getDiscordBot().getGuildById(guild).getMemberById(userId).getRoles().size(); i++) {
            if (Parse.getRole()
                    .equalsIgnoreCase(
                            HeimdallVelocity.getDiscordBot()
                                    .getGuildById(guild)
                                    .getMemberById(userId)
                                    .getRoles()
                                    .get(i)
                                    .getName())) {
                hasRole = true;
            }
        }
        return hasRole;
    }
}
