package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.config.Config;
import com.bifrostsmp.heimdall.discord.common.HasRole;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.logger;

public class SlashCommands extends ListenerAdapter {
    static boolean hasRole;
    private static String requiredStaffRole;
    private static String requiredAppRole;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        if (event.getUser().isBot()) return;
        long memberID = event.getMember().getIdLong();
        long guildID = event.getGuild().getIdLong();
        // Only accept commands from guilds
        try {
            requiredStaffRole = "You must have " + event.getGuild().getRoleById(Config.getStaffRole()).getName() + " role to use this command!";
            requiredAppRole = "You must have " + event.getGuild().getRoleById(Config.getAppRole()).getName() + " role to use this command!";
        } catch (Exception e) {
            requiredStaffRole = "not set";
            requiredAppRole = "not set";
            logger.info("Staff and App roles are not set in the config");
        }

        String command = event.getName().toLowerCase();
        switch (command) {
            case "repeat" -> {
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
            case "apply" -> {
                if (!event.getInteraction().isFromGuild())
                    return; // ensures the command is issued from the guild
                if (HasRole.hasRole(memberID, guildID, Config.getAppRole()) || HasRole.hasRole(memberID, guildID, Config.getStaffRole())) {
                    Apply.apply(event);
                } else {
                    event.reply(requiredAppRole).queue(
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
                Ticket.ticket(event);
            }
            case "invite" -> {
                if (!HasRole.hasRole(memberID, guildID, Config.getStaffRole())) {
                    event.reply(requiredStaffRole).queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                Invite.invite(event);
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
            case "welcome" -> {
                if (!HasRole.hasRole(memberID, guildID, Config.getStaffRole())) {
                    event.reply(requiredStaffRole).queue(
                            message -> {
                                message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                            });
                    return;
                }
                Welcome.welcome(event);
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
        }
    }
}
