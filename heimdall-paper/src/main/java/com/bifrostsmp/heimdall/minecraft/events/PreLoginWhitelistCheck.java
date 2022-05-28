package com.bifrostsmp.heimdall.minecraft.events;

import database.Query;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

public class PreLoginWhitelistCheck extends Event implements Listener {

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (!Query.checkPlayer(String.valueOf(event.getUniqueId()))) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "You are not whitelisted");
    }

}
