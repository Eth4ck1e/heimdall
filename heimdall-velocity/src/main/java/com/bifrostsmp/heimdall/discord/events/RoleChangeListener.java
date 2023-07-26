package com.bifrostsmp.heimdall.discord.events;

import database.Query;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class RoleChangeListener extends ListenerAdapter {
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        //Get the affected member and guild
        Member member = event.getMember();
        Guild guild = event.getGuild();
        List<Role> addedRoles = event.getRoles();

        for (Role addedRole : addedRoles) {
            if (addedRole.getName().equalsIgnoreCase("whitelisted")) {
                //uuid =
                Query.updateWhitelistByDiscordID(event.getMember().getId(), true);
                System.out.println(member.getUser().getAsTag() + " was assigned the role: " + addedRole.getName());
            } else {
                System.out.println("Role Change did not include whitelisted");
            }
        }


    }

}
