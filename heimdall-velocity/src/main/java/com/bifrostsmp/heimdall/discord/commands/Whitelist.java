package com.bifrostsmp.heimdall.discord.commands;

import com.bifrostsmp.heimdall.discord.commands.whitelist.Add;
import com.bifrostsmp.heimdall.discord.commands.whitelist.Import;
import com.bifrostsmp.heimdall.discord.commands.whitelist.Remove;
import com.bifrostsmp.heimdall.discord.commands.whitelist.Backup;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Whitelist extends ListenerAdapter {

    public static void whitelist(SlashCommandInteractionEvent event) {
        if (!event.getInteraction().isFromGuild()) return;
        if (event.getSubcommandName().equalsIgnoreCase("add")) Add.add(event);
        if (event.getSubcommandName().equalsIgnoreCase("remove")) Remove.remove(event);
        if (event.getSubcommandName().equalsIgnoreCase("backup")) Backup.backup(event);
        if (event.getSubcommandName().equalsIgnoreCase("import")) Import.importWhitelist(event);
    }
}

