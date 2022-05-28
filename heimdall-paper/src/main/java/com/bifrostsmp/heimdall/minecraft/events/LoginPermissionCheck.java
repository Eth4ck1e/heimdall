package com.bifrostsmp.heimdall.minecraft.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

public class LoginPermissionCheck extends Event implements Listener {
    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        //TODO use another plugin or write the code myself
    }
}
