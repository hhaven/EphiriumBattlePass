package com.ephirium.ephiriumBattlePass.commands;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BattlePassAdminCommand implements CommandExecutor {

    private final BattlePassManager manager;

    public BattlePassAdminCommand(BattlePassManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 🔒 SOLO OP
        if (!(sender instanceof Player player) || !player.isOp()) {
            sender.sendMessage("§cNo tienes permiso.");
            return true;
        }

        if (args.length < 2) {
            sendHelp(player);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage("§cJugador no encontrado.");
            return true;
        }

        PlayerData data = manager.getData(target);

        String action = args[1].toLowerCase();

        switch (action) {

            case "addxp" -> {
                if (args.length < 3) return false;

                int amount = Integer.parseInt(args[2]);
                manager.addXP(target, amount);

                player.sendMessage("§a+" + amount + " XP a " + target.getName());
            }

            case "removexp" -> {
                if (args.length < 3) return false;

                int amount = Integer.parseInt(args[2]);
                data.setXP(Math.max(0, data.getXP() - amount));

                player.sendMessage("§c-" + amount + " XP a " + target.getName());
            }

            case "setlevel" -> {
                if (args.length < 3) return false;

                int level = Integer.parseInt(args[2]);
                data.setLevel(level);

                player.sendMessage("§eNivel seteado a " + level);
            }

            case "addlevel" -> {
                if (args.length < 3) return false;

                int level = Integer.parseInt(args[2]);
                data.setLevel(data.getLevel() + level);

                player.sendMessage("§a+" + level + " niveles");
            }

            case "removelevel" -> {
                if (args.length < 3) return false;

                int level = Integer.parseInt(args[2]);
                data.setLevel(Math.max(0, data.getLevel() - level));

                player.sendMessage("§c-" + level + " niveles");
            }

            case "resetrewards" -> {
                data.getClaimedRewards().clear();
                player.sendMessage("§eRecompensas reseteadas.");
            }

            case "resetmissions" -> {
                data.getMissionProgress().clear();
                data.getMissionsCompleted().clear();
                player.sendMessage("§eMisiones reseteadas.");
            }

            case "resetall" -> {
                data.setXP(0);
                data.setLevel(0);
                data.setDailyXP(0);
                data.setLastXPDate(null);
                data.getClaimedRewards().clear();
                data.getMissionProgress().clear();
                data.getMissionsCompleted().clear();
                data.setBoosterEndTime(0);

                player.sendMessage("§cTodo reseteado.");
            }

            default -> sendHelp(player);
        }

        // 💾 GUARDAR
        manager.save(target);

        return true;
    }

    private void sendHelp(Player p) {
        p.sendMessage("§8§m------------------------");
        p.sendMessage("§6§lAdmin Pase de Batalla");
        p.sendMessage("§e/bpadmin <player> addxp <cantidad>");
        p.sendMessage("§e/bpadmin <player> removexp <cantidad>");
        p.sendMessage("§e/bpadmin <player> setlevel <nivel>");
        p.sendMessage("§e/bpadmin <player> addlevel <nivel>");
        p.sendMessage("§e/bpadmin <player> removelevel <nivel>");
        p.sendMessage("§e/bpadmin <player> resetrewards");
        p.sendMessage("§e/bpadmin <player> resetmissions");
        p.sendMessage("§e/bpadmin <player> resetall");
        p.sendMessage("§8§m------------------------");
    }
}