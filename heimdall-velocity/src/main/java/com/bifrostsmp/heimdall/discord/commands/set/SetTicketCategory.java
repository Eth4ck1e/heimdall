package com.bifrostsmp.heimdall.discord.commands.set;

import com.bifrostsmp.heimdall.config.Config;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.List;

public class SetTicketCategory extends ListenerAdapter {
    public static void ticket(SlashCommandInteractionEvent event) {
        List<Category> categories = event.getHook().getJDA().getCategories();
        ReplyCallbackAction reply = event.reply("Choose the category where tickets will be created");
        SelectMenu.Builder menu = SelectMenu.create("Choose-Category");

        for (Category category : categories) {
            menu.addOption(category.getName(), category.getId());
        }
        reply.addActionRow(menu.build()).queue();
    }

    @Override
    public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
        if (!event.getComponentId().equals("Choose-Category")) return;
        event.reply("You chose " + event.getValues().get(0)).queue();
        Config.setStaffCategory(event.getValues().get(0));
        Config.build();
    }
}
