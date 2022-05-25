package com.bifrostsmp.heimdall.discord.buttons.application;

import com.bifrostsmp.heimdall.config.ConfigParser;
import com.bifrostsmp.heimdall.discord.HasRole;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;

public class Deny extends ListenerAdapter {
    Member member;
    User applicant;
    MessageEmbed embed;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("Deny")) return;
        if (!HasRole.hasRole(event.getUser().getIdLong(), getGuild().getIdLong(), ConfigParser.getStaffRole())) {
            event.reply("You do not have the Staff role!").queue(
                    message -> {
                        message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    });
            return;
        }
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        member = hook.getInteraction().getMember(); //gets the member details of the button clicker

        embed = event.getMessage().getEmbeds().get(0);
        String discordID = embed.getFooter().getText();
        applicant = hook.getJDA().retrieveUserById(discordID).complete();

        SelectMenu.Builder menu = SelectMenu.create("DenyReason");
        menu
                .addOption("Poor-application", "Poor application, please be more thorough in your responses.", applicant.getId())
                .addOption("Rules", "Please review our rules again", applicant.getId())
                .addOption("Age", "Unfortunately you do not meat the minimum age requirement", applicant.getId())
                .addOption("Other", "other", applicant.getId());
        hook.sendMessage("Choose a Reason").addActionRow(menu.build()).queue();
        event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
    }

    @Override
    public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
        String choice = event.getSelectedOptions().get(0).getLabel();
        String value = event.getSelectedOptions().get(0).getValue();
        String description = event.getSelectedOptions().get(0).getDescription();
        System.out.println(choice);
        if (choice.equals("Poor-application") || choice.equals("Rules") || choice.equals("Age") || choice.equals("Other")) {
            send(description, member, embed, event, value);
            event.reply("You choice has been noted (If you chose 'other' reply to the bot DM with your custom response)").queue(
                    message -> {
                        message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    });
        }
        event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
    }

    public static void send(String applicant, Member member, MessageEmbed embed, Event event, String response) {
        event.getJDA()
                .openPrivateChannelById(applicant)
                .queue(
                        message -> {
                            EmbedBuilder deny = new EmbedBuilder();
                            deny.setTitle("Your Bifrost Application has been Denied");
                            deny.addField("Reason: ", response, false);
                            deny.setDescription("Please review the rules");
                            deny.setFooter("Applicants can be banned after three denied applications");
                            message.sendMessageEmbeds(deny.build()).queue();
                        });

        List<MessageEmbed.Field> fields = embed.getFields();
        EmbedBuilder denyEmbed = new EmbedBuilder();
        denyEmbed.setTitle(embed.getTitle());
        for (MessageEmbed.Field field : fields) {
            denyEmbed.addField(field);
        }
        denyEmbed.setFooter("Denied by " + member.getEffectiveName() + ".\nReason: " + response);

        MessageChannel denyChannel =
                event.getJDA()
                        .getGuildById(ConfigParser.getDiscordId())
                        .getTextChannelById(ConfigParser.getAppDenied());
        denyChannel
                .sendMessageEmbeds(denyEmbed.build())
                .queue();

    }
}
