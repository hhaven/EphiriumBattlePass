package com.ephirium.ephiriumBattlePass.manager;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
import com.ephirium.ephiriumBattlePass.listeners.RepairTokenListener;
import com.ephirium.ephiriumBattlePass.model.BattlePassReward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.*;
import java.util.UUID;

public class RewardManager {

    public static final Set<Integer> FREE_TIERS = Set.of(4, 16, 19, 24);

    public static boolean isFreeTier(int level) {
        return FREE_TIERS.contains(level);
    }

    private final Map<Integer, BattlePassReward> rewards = new HashMap<>();
    private Currency currency;

    public RewardManager() {
        loadCurrency();
        loadRewards();
    }
    //setup de dinero
    private void loadCurrency() {
        currency = CoinsEngineAPI.getCurrency("money"); // ⚠️ cambia si usas otra
    }

    public BattlePassReward getReward(int level) {
        return rewards.get(level);
    }

    private static void giveItems(Player p, ItemStack... items) {
        Map<Integer, ItemStack> leftover = p.getInventory().addItem(items);
        for (ItemStack item : leftover.values()) {
            p.getWorld().dropItemNaturally(p.getLocation(), item);
        }
    }

    // =========================
    // 🎁 RECOMPENSAS
    // =========================
    private void loadRewards() {

        rewards.put(1, new BattlePassReward(1, "Dinero Tier 1", p -> {
            CoinsEngineAPI.addBalance(p, currency, 20000);
            p.sendMessage("§a+$20,000");
        }));

        rewards.put(2, new BattlePassReward(2, "Caja de Cobre", p -> {
            giveItems(p,
                    new ItemStack(Material.AMETHYST_BLOCK, 32),
                    new ItemStack(Material.COPPER_BLOCK, 32),
                    new ItemStack(Material.CALCITE, 32)
            );
        }));

        rewards.put(3, new BattlePassReward(3, "Materiales de Terreno", p -> {
            giveItems(p,
                    new ItemStack(Material.GRAVEL, 64),
                    new ItemStack(Material.GRAVEL, 64),   // 128 total
                    new ItemStack(Material.SAND, 64),
                    new ItemStack(Material.SAND, 64),     // 128 total
                    new ItemStack(Material.MOSSY_STONE_BRICKS, 64),
                    new ItemStack(Material.CRACKED_STONE_BRICKS, 64),
                    new ItemStack(Material.BRICKS, 64),
                    new ItemStack(Material.GRAVEL, 64),   // extra x64
                    new ItemStack(Material.MUD_BRICKS, 32),
                    new ItemStack(Material.SLIME_BLOCK, 32)
            );
        }));

        rewards.put(4, new BattlePassReward(4, "Provisiones", p -> {
            giveItems(p,
                    new ItemStack(Material.COOKED_BEEF, 64),
                    new ItemStack(Material.COOKED_PORKCHOP, 64),
                    new ItemStack(Material.COOKED_CHICKEN, 64)
            );
        }));

        rewards.put(5, new BattlePassReward(5, "Mochila Diamante", p -> {

            String playerName = p.getName();

            // Ejecutar comando de BackpackPlus
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "backpack give 3 " + playerName
            );

            p.sendMessage("§bRecibiste una §eMochila Diamante");
        }));

        rewards.put(6, new BattlePassReward(6, "Kit del Constructor", p -> {
            giveItems(p,
                    new ItemStack(Material.TERRACOTTA, 128),
                    new ItemStack(Material.QUARTZ_BLOCK, 128),
                    new ItemStack(Material.NETHER_BRICKS, 128),
                    new ItemStack(Material.WHITE_CONCRETE, 128)
            );
        }));

        rewards.put(7, new BattlePassReward(7, "Kit del Minero", p -> {
            giveItems(p,
                    new ItemStack(Material.LAPIS_BLOCK, 32),
                    new ItemStack(Material.GOLD_BLOCK, 16),
                    new ItemStack(Material.EMERALD_BLOCK, 16)
            );
        }));

        rewards.put(8, new BattlePassReward(8, "Kit Industrial", p -> {
            giveItems(p,
                    new ItemStack(Material.HOPPER, 64),
                    new ItemStack(Material.RAIL, 64),
                    new ItemStack(Material.RAIL, 64), // 128 total
                    new ItemStack(Material.MINECART, 1),
                    new ItemStack(Material.POWERED_RAIL, 16)
            );
        }));

        rewards.put(9, new BattlePassReward(9, "Kit de Redstone", p -> {
            giveItems(p,
                    new ItemStack(Material.OBSERVER, 64),
                    new ItemStack(Material.PISTON, 64),
                    new ItemStack(Material.STICKY_PISTON, 64),
                    new ItemStack(Material.REPEATER, 64)
            );
        }));

        rewards.put(10, new BattlePassReward(10, "Hoz de Combate", p -> {

            ItemStack item = new ItemStack(Material.NETHERITE_HOE);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§cHoz de Combate");

            // 🔥 Encantamientos
            meta.addEnchant(Enchantment.SHARPNESS, 5, true);
            meta.addEnchant(Enchantment.SMITE, 5, true);
            meta.addEnchant(Enchantment.BANE_OF_ARTHROPODS, 5, true);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
            meta.addEnchant(Enchantment.UNBREAKING, 3, true);
            meta.addEnchant(Enchantment.MENDING, 1, true);

            // ⚔️ Daño
            meta.addAttributeModifier(
                    Attribute.ATTACK_DAMAGE,
                    new AttributeModifier(
                            UUID.randomUUID(),
                            "attack_damage",
                            12.0,
                            AttributeModifier.Operation.ADD_NUMBER
                    )
            );

            meta.addAttributeModifier(
                    Attribute.ATTACK_SPEED,
                    new AttributeModifier(
                            UUID.randomUUID(),
                            "attack_speed",
                            -0.5, // esto deja velocidad en ~2.0
                            AttributeModifier.Operation.ADD_SCALAR
                    )
            );

            meta.setLore(List.of(
                    "§7Un arma legendaria..."
            ));

            item.setItemMeta(meta);
            giveItems(p,item);
        }));

        rewards.put(11, new BattlePassReward(11, "Dinero Tier 2", p -> {
            CoinsEngineAPI.addBalance(p, currency, 40000);
            p.sendMessage("§a+$40,000");
        }));

        rewards.put(12, new BattlePassReward(12, "Kit de Alquimia", p -> {
            giveItems(p,
                    new ItemStack(Material.NETHER_WART, 64),
                    new ItemStack(Material.BLAZE_POWDER, 32),
                    new ItemStack(Material.MAGMA_CREAM, 16),
                    new ItemStack(Material.PHANTOM_MEMBRANE, 16),
                    new ItemStack(Material.EXPERIENCE_BOTTLE, 8)
            );
        }));

        rewards.put(13, new BattlePassReward(13, "Kit de Almacenamiento", p -> {
            giveItems(p,
                    new ItemStack(Material.SHULKER_SHELL, 4),
                    new ItemStack(Material.ENDER_CHEST, 2),
                    new ItemStack(Material.BARREL, 32),
                    new ItemStack(Material.CHEST, 16)
            );
        }));

        rewards.put(14, new BattlePassReward(14, "Mochila Diamante", p -> {
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "backpack give 3 " + p.getName()
            );
            p.sendMessage("§bRecibiste una §eMochila Diamante");
        }));

        rewards.put(15, new BattlePassReward(15, "Minerales Valiosos", p -> {
            giveItems(p,
                    new ItemStack(Material.DIAMOND, 32),
                    new ItemStack(Material.EMERALD, 32),
                    new ItemStack(Material.NETHERITE_INGOT, 4)
            );
        }));

        rewards.put(16, new BattlePassReward(16, "Caja Dorada", p -> {
            giveItems(p,
                    new ItemStack(Material.GOLDEN_CARROT, 32),
                    new ItemStack(Material.GOLDEN_APPLE, 8),
                    new ItemStack(Material.GLISTERING_MELON_SLICE, 8)
            );
        }));

        rewards.put(17, new BattlePassReward(17, "Botín del Cazador", p -> {
            giveItems(p,
                    new ItemStack(Material.GHAST_TEAR, 8),
                    new ItemStack(Material.SHULKER_SHELL, 2),
                    new ItemStack(Material.BONE, 64),
                    new ItemStack(Material.SLIME_BALL, 16)
            );
        }));

        rewards.put(18, new BattlePassReward(18, "Kit Redstone Avanzado", p -> {
            giveItems(p,
                    new ItemStack(Material.REDSTONE_BLOCK, 64),
                    new ItemStack(Material.REDSTONE_BLOCK, 64), // 128 total
                    new ItemStack(Material.HOPPER, 64),
                    new ItemStack(Material.REDSTONE_TORCH, 64),
                    new ItemStack(Material.REDSTONE_TORCH, 64) // 128 total
            );
        }));

        rewards.put(19, new BattlePassReward(19, "Kit de Iluminación", p -> {
            giveItems(p,
                    new ItemStack(Material.GLOWSTONE, 32),
                    new ItemStack(Material.SEA_LANTERN, 32),
                    new ItemStack(Material.SHROOMLIGHT, 32),
                    new ItemStack(Material.LANTERN, 32),
                    new ItemStack(Material.SOUL_LANTERN, 32),
                    new ItemStack(Material.JACK_O_LANTERN, 32),
                    new ItemStack(Material.END_ROD, 32),
                    new ItemStack(Material.OCHRE_FROGLIGHT, 32),
                    new ItemStack(Material.PEARLESCENT_FROGLIGHT, 32),
                    new ItemStack(Material.VERDANT_FROGLIGHT, 32),
                    new ItemStack(Material.REDSTONE_LAMP, 32)
            );
        }));

        rewards.put(20, new BattlePassReward(20, "Pico Dios", p -> {
            var item = new ItemStack(Material.NETHERITE_PICKAXE);
            var meta = item.getItemMeta();

            meta.addEnchant(org.bukkit.enchantments.Enchantment.SILK_TOUCH, 1, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.EFFICIENCY, 7, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.UNBREAKING, 5, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);

            meta.setDisplayName("§bPico Legendario");

            item.setItemMeta(meta);
            giveItems(p,item);
        }));

        rewards.put(21, new BattlePassReward(21, "Dinero Tier 3", p -> {
            CoinsEngineAPI.addBalance(p, currency, 60000);
            p.sendMessage("§a+$60,000");
        }));

        rewards.put(22, new BattlePassReward(22, "Tesoro Oceánico", p -> {
            giveItems(p,
                    new ItemStack(Material.SPONGE, 16),
                    new ItemStack(Material.WET_SPONGE, 8),
                    new ItemStack(Material.TURTLE_HELMET, 1),
                    new ItemStack(Material.HEART_OF_THE_SEA, 1),
                    new ItemStack(Material.PRISMARINE_BRICKS, 64),
                    new ItemStack(Material.PRISMARINE_BRICKS, 64) // 128 total
            );
        }));

        rewards.put(23, new BattlePassReward(23, "Cashback 5% x2 Semanas", p -> {
            var manager = EphiriumBattlePass.getInstance().getBattlePassManager();
            manager.getData(p).activateCashback(5.0, 14);
            manager.save(p);
            p.sendMessage("§aActivaste §eCashback §e5% §apor §e2 semanas§a!");
        }));

        rewards.put(24, new BattlePassReward(24, "Repair Tokens x3", p -> {
            ItemStack token = RepairTokenListener.createToken();
            token.setAmount(3);
            giveItems(p,token);
            p.sendMessage("§aRecibiste §ex3 Repair Tokens§a!");
        }));

        rewards.put(25, new BattlePassReward(25, "Tridente", p -> {
            giveItems(p,new ItemStack(Material.TRIDENT, 1));
        }));

        rewards.put(26, new BattlePassReward(26, "Mascota Capybara", p -> {

            String playerName = p.getName();

            if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
                Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        "lp user " + playerName + " permission set mcpet.capybara_mount true"
                );
            }

            p.sendMessage("§d¡Desbloqueaste la mascota §eCapybara§d!");
        }));
    }
}