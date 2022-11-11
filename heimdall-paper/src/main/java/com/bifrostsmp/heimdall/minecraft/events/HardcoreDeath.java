package com.bifrostsmp.heimdall.minecraft.events;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.Date;
import java.time.Instant;

import static com.bifrostsmp.heimdall.config.ConfigParser.getTempBan;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class HardcoreDeath extends Event implements Listener {
    @Override
    public @NotNull HandlerList getHandlers() {return null;}

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[Heimdall] onPlayerDeathEvent Triggered" );
        Player p = event.getPlayer();
        Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), "Died", Date.from(Instant.now().plusSeconds((long) getTempBan() *60*60)),null);
        p.kick(Component.text(("Helheim doth not accept you.  Your Soul will linger in Purgatory for ? hours.  May the Gods have mercy on your soul").replace("?", String.valueOf(getTempBan()))));
    }

}
