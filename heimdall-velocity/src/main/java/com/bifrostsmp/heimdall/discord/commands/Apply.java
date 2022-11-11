package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.discord.applications.Questions;
import common.YamlParser;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.bifrostsmp.heimdall.HeimdallVelocity.getDataDirectory;
import static com.bifrostsmp.heimdall.HeimdallVelocity.getDiscordBot;
import static com.bifrostsmp.heimdall.config.Config.*;
import static com.bifrostsmp.heimdall.discord.common.Tickets.newTicket;

public class Apply extends ListenerAdapter {

    private static final ArrayList<String> appAnswers = new ArrayList<>();
    private static final int var = 0;
    private static long ch;
    private static Message message;
    private static Member member;

    public static void apply(SlashCommandInteractionEvent event) {
        
        event.deferReply().queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);
        member = event.getMember();
        Role staff = getDiscordBot().getRoleById(getStaffRole());
        Guild guild = event.getGuild();
        
//        String application;
        User user = event.getUser();

        String fieldMessage = "Your application has started\nType '!cancel' at any time to cancel your application";

        TextChannel channel = newTicket(member, hook, staff, guild, "application", fieldMessage);

        SelectMenu.Builder menu = SelectMenu.create("Application");
        for (int i = 1; i < getApplications().size()+1; i++) {
            menu
                    .addOption( getApplications().get(i), String.valueOf(i), member.getId());
        }

        channel.sendMessage("Choose Application").setActionRow(menu.build()).queue();

        event.getChannel().sendMessage(user.getName() + " application started.").queue(message -> {
                    message.delete().queueAfter(15, TimeUnit.SECONDS);
                });

        
//        switch (event.getSubcommandName().toLowerCase()) {
//            case "whitelist" -> {
//                application = "whitelist.yml";
//            }
//            case "staff" -> {
//                application =" staff.yml";
//            }
//            case "family" -> {
//                application =" family.yml";
//            }
//            default -> throw new IllegalStateException("Unexpected value: " + event.getSubcommandName().toLowerCase());
//        }
        hook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS);

//        if (event.getSubcommandName().equalsIgnoreCase("whitelist")) ApplyWhitelist.whitelist(event);
//        if (event.getSubcommandName().equalsIgnoreCase("staff")) ApplyStaff.staff(event);

    }

    public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
        if (!event.getUser().getId().equals(event.getSelectedOptions().get(0).getDescription())) {
            if(isHeimdallDebug()) System.out.println("[Heimdall] DEBUG: Apply menu Member mismatch");
            return;
        }
        //Prevents matching any of the values from the deny menu
        if (getApplicationDenyOptions().containsKey(event.getSelectedOptions().get(0).getLabel()) || event.getSelectedOptions().get(0).getLabel().equals("Other")) return;

        String label = event.getSelectedOptions().get(0).getLabel();
        String value = event.getSelectedOptions().get(0).getValue();
        String memberId = event.getSelectedOptions().get(0).getDescription();

        event.deferReply().queue();

        if (isHeimdallDebug()) System.out.println("[Heimdall] DEBUG: " + label + ": " + value + ": " + memberId);
        InteractionHook hook = event.getHook();

        String applicationName = label + ".yml";
        if(isHeimdallDebug()) System.out.println("[Heimdall] DEBUG: " + applicationName);
        Member member = event.getGuild().retrieveMemberById(memberId).complete();

        Map<String, Object> getApp = YamlParser.parse(Path.of(getDataDirectory() + "/applications/" + applicationName));
        Map<Integer, Object> getQuestionObjects = (Map<Integer, Object>) getApp.get("Questions");
        hook.getInteraction()
                .getJDA()
                .addEventListener(new Questions(member.getIdLong(), event.getChannel().getIdLong(), getQuestionObjects));
        hook.sendMessage("What is your IGN? (In Game Name)").queue(Apply::setMessage);
        event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
    }

    public static Message getMessage() {
        return message;
    }

    public static void setMessage(Message message) {
        Apply.message = message;
    }
}