package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SvenAriochTokenListener implements Listener {

    private static final String PDC_KEY = "sven_arioch_token";
    private static final String ALLOWED_PLAYER = "galsenpai";
    private static final String TARGET_PLAYER = "SvenArioch";

    public static ItemStack createToken() {
        ItemStack item = new ItemStack(Material.WITHER_ROSE);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.setDisplayName("§0Recado Silencioso");
        meta.setLore(List.of(
                "§7Click derecho para activarlo.",
                "§8Solo responde a su verdadero dueño.",
                "§cSe consume al usarse."
        ));

        NamespacedKey key = new NamespacedKey(EphiriumBattlePass.getInstance(), PDC_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isSpecialToken(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        NamespacedKey key = new NamespacedKey(EphiriumBattlePass.getInstance(), PDC_KEY);
        return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        for (ItemStack ingredient : event.getInventory().getMatrix()) {
            if (isSpecialToken(ingredient)) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!isSpecialToken(mainHand)) {
            return;
        }

        event.setCancelled(true);

        if (!player.getName().equalsIgnoreCase(ALLOWED_PLAYER)) {
            player.sendMessage("§cEl item no responde a ti.");
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + TARGET_PLAYER);

        if (mainHand.getAmount() <= 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            mainHand.setAmount(mainHand.getAmount() - 1);
        }

        player.sendMessage("§aEl recado ha sido entregado.");
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.7f, 1.6f);
    }
}
