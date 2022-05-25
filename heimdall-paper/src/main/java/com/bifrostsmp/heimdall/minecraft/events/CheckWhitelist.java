package com.bifrostsmp.heimdall.minecraft.events;

import com.bifrostsmp.heimdall.database.Query;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.UUID;

public class CheckWhitelist extends Event {

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        if (!isWhitelisted(String.valueOf(event.getUniqueId()))) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "You are not whitelisted");

    }

    private boolean isWhitelisted(String uuid) {
        //TODO Send message to proxy over proxy messaging channel to check if player is whitelisted
        //TODO or submit query to db directly to determine if player is whitelisted
        return Query.checkPlayer(uuid);
    }

}
