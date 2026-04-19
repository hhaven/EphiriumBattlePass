package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
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
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class RepairTokenListener implements Listener {

    private static final String PDC_KEY = "repair_token";

    public static ItemStack createToken() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bToken de Reparación");
        meta.setLore(List.of(
                "§7Sujeta en la mano principal y haz",
                "§7clic derecho para reparar el ítem",
                "§7de tu mano secundaria.",
                "§c¡Se consume al usar!"
        ));
        NamespacedKey key = new NamespacedKey(EphiriumBattlePass.getInstance(), PDC_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isRepairToken(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        NamespacedKey key = new NamespacedKey(EphiriumBattlePass.getInstance(), PDC_KEY);
        return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent e) {
        for (ItemStack ingredient : e.getInventory().getMatrix()) {
            if (isRepairToken(ingredient)) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;

        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!isRepairToken(mainHand)) return;

        e.setCancelled(true);

        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (offhand.getType() == Material.AIR) {
            player.sendMessage("§cNo tienes ningún ítem en la mano secundaria.");
            return;
        }

        ItemMeta offMeta = offhand.getItemMeta();
        if (!(offMeta instanceof Damageable dmg)) {
            player.sendMessage("§cEse ítem no se puede reparar.");
            return;
        }

        if (dmg.getDamage() == 0) {
            player.sendMessage("§cEse ítem ya está completamente reparado.");
            return;
        }

        dmg.setDamage(0);
        offhand.setItemMeta(offMeta);

        if (mainHand.getAmount() <= 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            mainHand.setAmount(mainHand.getAmount() - 1);
        }

        player.sendMessage("§a¡Tu ítem ha sido reparado completamente!");
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1.2f);
    }
}
