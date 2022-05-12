//This class handles button interaction events for applications in the pending application channel.
//There are two buttons, 'accept' and 'deny'.  The accept button gets the minecraft uuid from the ign and attempts to whitelist
//the player if not already whitelisted.  The deny button sends a message to the button clicker asking for a reason and
//stores the response.  Both buttons send a notification to the applicant of the result.

package com.bifrostsmp.heimdall.discord.buttons;

import com.bifrostsmp.heimdall.config.ConfigParser;
import com.bifrostsmp.heimdall.database.Query;
import com.bifrostsmp.heimdall.discord.applications.ResponseHandler;
import com.bifrostsmp.heimdall.discord.commands.SlashCommands;
import com.bifrostsmp.heimdall.mojangAPI.NameToID;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;
import static com.bifrostsmp.heimdall.database.Query.updateTrigger;

public class AppAcceptDeny extends ListenerAdapter {
    private String response;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (!SlashCommands.hasRole(event.getUser().getIdLong(), getGuild().getIdLong(), ConfigParser.getStaffRole())){
            event.reply("You do not have the Staff role!").queue(
                    message -> {
                        message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    });
            return;
        }

        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        ResultSet result;
        Guild guild = event.getGuild();
        assert guild != null;
        Member member = hook.getInteraction().getMember(); //gets the member details of the button clicker

        if (event.getComponentId().equals("Accept")) {  // checks if the accept button was clicked
            event.deferReply().queue();
            MessageEmbed embed = event.getMessage().getEmbeds().get(0);  //stores the embed that the buttons are attached to in a variable
            String discordID = embed.getFooter().getText();  //gets the applicants discordID from the embed footer
            User user = hook.getJDA().retrieveUserById(discordID).complete();  //stores the applicant user data in user.  Must use retrieve do to caching.  Using complete to ensure this action completes synchronously.
            String IGN = embed.getFields().get(0).getValue();
            String ID = NameToID.nameToID(IGN);
            try {
                result = Query.checkPlayers(ID);
                if (!result.next()) {
                    Query.insertPlayers(IGN, ID);
                    updateTrigger();
                    // success embed block
                    EmbedBuilder info = new EmbedBuilder();
                    info.setTitle("Whitelist");
                    info.setDescription(IGN + " has been added to the whitelist");
                    info.setColor(Color.GREEN);
                    event
                            .getChannel()
                            .sendMessageEmbeds(info.build())
                            .queue(
                                    message -> {
                                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                                    }); // send embed to message channel
                    info.clear(); // clear embed from memory
                } else {
                    // success embed block
                    EmbedBuilder info = new EmbedBuilder();
                    info.setTitle("Whitelist");
                    info.setDescription(IGN + " is already whitelisted");
                    info.setColor(Color.RED);
                    event
                            .getChannel()
                            .sendMessageEmbeds(info.build())
                            .queue(
                                    message -> {
                                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                                    }); // send embed to message channel
                    info.clear(); // clear embed from memory
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            java.util.List<Role> guildRoles = guild.getRoles();
            String[] addRolesStrings = new String[]{"Member", "Minecraft", "Fans", "Market", "Updates", "Events", "Polls", "Newsletter", "Gamenight", "Whitelisted"};

            for (String role : addRolesStrings) {
                if (guildRoles.stream().noneMatch(o -> role.equals(o.getName()))) {
                    guild
                            .createRole()
                            .setName(role)
                            .setHoisted(true)
                            .setMentionable(true)
                            .setPermissions(Permission.EMPTY_PERMISSIONS)
                            .complete();
                    System.out.println("Created guild role " + role);
                }
                if (guildRoles.stream().anyMatch(o -> role.equals(o.getName()))) {
                    guild.addRoleToMember(user, guild.getRolesByName(role, true).get(0)).queue();
                }
            }

            guild.removeRoleFromMember(user, guild.getRolesByName("Applicant", true).get(0)).queue();

            user.openPrivateChannel()
                    .queue(
                            (Channel) -> {
                                EmbedBuilder accept = new EmbedBuilder();
                                accept.setTitle("Your Bifrost Application has been Accepted!");
                                accept.addField("Server Host:", "minecraft.bifrostsmp.com", false);
                                Channel.sendMessageEmbeds(accept.build()).queue();
                            });
            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
            MessageChannel acceptChannel =
                    event.getGuild().getTextChannelsByName(ConfigParser.getAppAccepted(), true).get(0);
            List<MessageEmbed.Field> fields = embed.getFields();
            EmbedBuilder acceptEmbed = new EmbedBuilder();
            acceptEmbed.setTitle(embed.getTitle());
            for (MessageEmbed.Field field : fields) {
                acceptEmbed.addField(field);
            }
            acceptEmbed.setFooter("Accepted by " + member.getEffectiveName());

            acceptChannel.sendMessageEmbeds(acceptEmbed.build()).queue();

            hook.sendMessage("application accepted")
                    .queue(
                            message -> {
                                message.delete().queueAfter(30, TimeUnit.SECONDS);
                            });
        } else if (event.getComponentId().equals("Deny")) {
            event.deferReply().queue();
            MessageEmbed embed = event.getMessage().getEmbeds().get(0);
            String discordID = embed.getFooter().getText();
            User applicant = hook.getJDA().retrieveUserById(discordID).complete();
            hook
                    .getJDA()
                    .openPrivateChannelById(member.getIdLong())
                    .queue(
                            message -> {
                                message
                                        .sendMessage("What is the reason for the denial?")
                                        .queue(
                                                follow -> {
                                                    follow.delete().queueAfter(5, TimeUnit.MINUTES);
                                                });
                            });

            hook.getInteraction()
                    .getJDA()
                    .addEventListener(new ResponseHandler(applicant.getIdLong(), member.getIdLong(), embed));
            hook.sendMessage("Application denied").queue(
                    message -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                    });
            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }
}