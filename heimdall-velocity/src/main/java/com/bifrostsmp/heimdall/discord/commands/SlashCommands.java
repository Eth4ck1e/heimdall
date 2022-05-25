package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.config.ConfigParser;
import com.bifrostsmp.heimdall.discord.HasRole;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class SlashCommands extends ListenerAdapter {
    static boolean hasRole;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        if (event.getUser().isBot()) return;
        long memberID = event.getMember().getIdLong();
        long guildID = event.getGuild().getIdLong();
        // Only accept commands from guilds

        String command = event.getName().toLowerCase();
        switch (command) {
            case "repeat" -> {
                Repeat.repeat(
                        event,
                        event.getOption("content").getAsString()); // content is required so no null-check here
            }
            case "whitelist" -> {
                if (!HasRole.hasRole(memberID, guildID, ConfigParser.getStaffRole())) {
                    event.reply("You must have Staff or above role to use this command!").queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                Whitelist.whitelist(event);
            }
            case "apply" -> {
                if (!event.getInteraction().isFromGuild())
                    return; // ensures the command is issued from the guild
                if (HasRole.hasRole(memberID, guildID, ConfigParser.getAppRole()) || HasRole.hasRole(memberID, guildID, ConfigParser.getStaffRole())) {
                    Apply.apply(event);
                } else {
                    event.reply("You do not have the applicant role!").queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                }

            }
            case "info" -> {
                Info.info(event);
            }
            case "ping" -> {
                PingPong.pingPong(event);
            }
            case "ticket" -> {
                TicketCommand.ticket(event);
            }
            case "invite" -> {
                if (!HasRole.hasRole(memberID, guildID, ConfigParser.getStaffRole())) {
                    event.reply("You must have Staff or above role to use this command!").queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                Invite.invite(event);
            }
            case "set" -> {
                if (HasRole.hasRole(memberID, guildID, ConfigParser.getStaffRole()) || event.getGuild().getOwner().equals(event.getMember())) {
                    Set.set(event);
                } else {
                    event.reply("You must have Staff or above role to use this command!").queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                }
            }
            case "welcome" -> {
                if (!HasRole.hasRole(memberID, guildID, ConfigParser.getStaffRole())) {
                    event.reply("You must have Staff or above role to use this command!").queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                Welcome.welcome(event);
            }
            case "reload" -> {
                if (!HasRole.hasRole(memberID, guildID, ConfigParser.getStaffRole())) {
                    event.reply("You must have Staff or above role to use this command!").queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                ConfigParser.reloadConfig();
                event.reply("Config file has been reloaded").queue();
            }
            case "setup" -> {
                if (HasRole.hasRole(memberID, guildID, ConfigParser.getStaffRole()) || event.getGuild().getOwner().equals(event.getMember())) {
                    Setup.setup(event);
                } else {
                    event.reply("You must have Staff or above role to use this command!").queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                }

            }
        }
    }
}
