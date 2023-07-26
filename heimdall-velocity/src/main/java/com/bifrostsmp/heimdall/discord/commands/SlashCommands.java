package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.config.Config;
import com.bifrostsmp.heimdall.discord.commands.whitelist.Backup;
import com.bifrostsmp.heimdall.discord.common.HasRole;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import whitelist.BackupWhitelist;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.logger;

public class SlashCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        if (event.getUser().isBot()) return;
        long memberID = event.getMember().getIdLong();
        long guildID = event.getGuild().getIdLong();
        // Only accept commands from guilds
        String requiredStaffRole;
        try {
            requiredStaffRole = "You must have " + event.getGuild().getRoleById(Config.getStaffRole()).getName() + " role to use this command!";
        } catch (Exception e) {
            requiredStaffRole = "not set";
            logger.info("Staff and App roles are not set in the config");
        }

        String command = event.getName().toLowerCase();
        switch (command) {
            case "say" -> {
                Repeat.repeat(
                        event,
                        event.getOption("content").getAsString()); // content is required so no null-check here
            }
            case "whitelist" -> {
                if (!HasRole.hasRole(memberID, guildID, Config.getStaffRole())) {
                    event.reply(requiredStaffRole).queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                Whitelist.whitelist(event);
            }
            case "info" -> {
                Info.info(event);
            }
            case "ping" -> {
                PingPong.pingPong(event);
            }
            case "set" -> {
                if (HasRole.hasRole(memberID, guildID, Config.getStaffRole()) || event.getGuild().getOwner().equals(event.getMember())) {
                    Set.set(event);
                } else {
                    event.reply(requiredStaffRole).queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                }
            }
            case "reload" -> {
                if (!HasRole.hasRole(memberID, guildID, Config.getStaffRole())) {
                    event.reply(requiredStaffRole).queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                Config.reloadConfig();
                event.reply("Config file has been reloaded").queue();
            }
            case "setup" -> {
                if (HasRole.hasRole(memberID, guildID, Config.getStaffRole()) || Objects.equals(event.getGuild().getOwner(), event.getMember())) {
                    Setup.setup(event);
                } else {
                    event.reply(requiredStaffRole).queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                }

            }
            case "verify" -> {
                if (HasRole.hasRole(memberID, guildID, "unverified") || HasRole.hasRole(memberID, guildID, Config.getStaffRole())) {
                    Verify.verify(event);
                    return;
                }
                event.reply("You do not have the unverified role").queue(
                        message -> {
                            message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                        });
            }
        }
    }
}
