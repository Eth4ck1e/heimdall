package com.bifrostsmp.heimdall.discord.common;

import com.bifrostsmp.heimdall.HeimdallVelocity;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class HasRole {
    static boolean hasRole;

    public static boolean hasRole(Long userId, Long guild, String role) {
        HeimdallVelocity.getDiscordBot().getGuildById(guild).retrieveMemberById(userId).complete();
        List<Role> MemberRoles = HeimdallVelocity.getDiscordBot().getGuildById(guild).getMemberById(userId).getRoles();
        hasRole = false;
        for (Role memberRole : MemberRoles) {
            if (memberRole.getId().equalsIgnoreCase(role)) {
                hasRole = true;
            }
        }
        return hasRole;
    }
}
