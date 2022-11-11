package com.bifrostsmp.heimdall.discord.buttons.application;

import com.bifrostsmp.heimdall.config.Config;
import com.bifrostsmp.heimdall.discord.common.HasRole;
import com.bifrostsmp.heimdall.discord.applications.DenyResponseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDiscordBot;
import static com.bifrostsmp.heimdall.HeimdallVelocity.getGuild;
import static com.bifrostsmp.heimdall.config.Config.getApplicationDenyOptions;
import static com.bifrostsmp.heimdall.config.Config.isHeimdallDebug;

public class Deny extends ListenerAdapter {
    User staff;
    User applicant;
    MessageEmbed embed;

    public static void send(User staff, MessageEmbed embed, TextChannel channel, String response) {

        EmbedBuilder deny = new EmbedBuilder();
        deny.setTitle("Your Bifrost Application has been Denied");
        deny.addField("Reason: ", response, false);
        deny.setDescription("Please review the rules");
        deny.setFooter("Applicants can be banned after three denied applications");
        channel.sendMessageEmbeds(deny.build()).queue();

        List<MessageEmbed.Field> fields = embed.getFields();
        EmbedBuilder denyEmbed = new EmbedBuilder();
        denyEmbed.setTitle(embed.getTitle());
        for (MessageEmbed.Field field : fields) {
            denyEmbed.addField(field);
        }
        denyEmbed.setFooter("Denied by " + staff.getName() + ".\nReason: " + response);

        MessageChannel denyChannel =
                getDiscordBot()
                        .getGuildById(Config.getDiscordId())
                        .getTextChannelById(Config.getAppDenied());
        denyChannel
                .sendMessageEmbeds(denyEmbed.build())
                .queue();

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("Deny")) {
            if(isHeimdallDebug()) System.out.println("[Heimdall] DEBUG: Deny button component != Deny");
            return;
        }
        if (!HasRole.hasRole(event.getUser().getIdLong(), getGuild().getIdLong(), Config.getStaffRole())) {
            event.reply("You do not have the Staff role!").queue(
                    message -> {
                        message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    });
            return;
        }
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        staff = hook.getInteraction().getUser(); //gets the member details of the button clicker

        embed = event.getMessage().getEmbeds().get(0);
        String discordID = embed.getFooter().getText();
        applicant = hook.getJDA().retrieveUserById(discordID).complete();

        SelectMenu.Builder menu = SelectMenu.create("DenyReason");
        Map<String, String> options = getApplicationDenyOptions();
        for (Map.Entry<String, String> option : options.entrySet()) {
            menu.addOption(option.getKey(), option.getValue(), applicant.getId());
        }
        menu.addOption("Other", "other", applicant.getId());

        hook.sendMessage("Choose a Reason").addActionRow(menu.build()).queue();
        event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
    }

    public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
        if (!event.getUser().equals(staff)) {
            if(isHeimdallDebug()) System.out.println("[Heimdall] DEBUG: Application Deny menu Member mismatch");
            return;
        }
        InteractionHook hook = event.getHook();
        TextChannel channel = event.getChannel().asTextChannel();
        String choice = event.getSelectedOptions().get(0).getLabel();
        String value = event.getSelectedOptions().get(0).getValue();

        if (getApplicationDenyOptions().containsKey(choice)) {
            send(staff, embed, channel, value);
            event.reply("You choice has been noted").queue(
                    message -> {
                        message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    });
        } else if (choice.equals("Other")) {
            hook.getInteraction().getJDA().addEventListener(new DenyResponseHandler(staff, embed, channel));
            event.reply("Please respond with your reason for choosing the 'Other' option.").queue(
                    message -> {
                        message.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    });
        } else {
            return;
        }
        event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
    }

}