package com.ephirium.ephiriumBattlePass.commands;

import com.ephirium.ephiriumBattlePass.manager.BattlePassManager;
import com.ephirium.ephiriumBattlePass.model.PlayerData;
import com.ephirium.ephiriumBattlePass.gui.BattlePassGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattlePassCommand implements CommandExecutor {

    private final BattlePassManager battlePassManager;
    private final BattlePassGUI battlePassGUI;

    public BattlePassCommand(BattlePassManager battlePassManager) {
        this.battlePassManager = battlePassManager;
        this.battlePassGUI = new BattlePassGUI(battlePassManager);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        PlayerData data = battlePassManager.getData(player);

        //admin

        // =========================
        // 📊 /bpa (DEFAULT)
        // =========================
        if (args.length == 0) {
            battlePassGUI.open(player);
            return true;
        }

        // =========================
        // 📜 SUBCOMANDOS
        // =========================
        switch (args[0].toLowerCase()) {

            case "missions" -> {
                sendMissions(player);
            }

            case "daily" -> {
                sendDaily(player, data);
            }

            case "give" -> {
                if (!player.hasPermission("battlepass.admin")) {
                    player.sendMessage("§cNo tienes permisos.");
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage("§cUso: /bpa give <xp>");
                    return true;
                }

                try {
                    int xp = Integer.parseInt(args[1]);
                    battlePassManager.addXPFromMission(player, xp);
                    player.sendMessage("§aSe agregaron " + xp + " XP (modo misión)");
                } catch (NumberFormatException e) {
                    player.sendMessage("§cNúmero inválido.");
                }
            }

            case "resetdaily" -> {
                if (!player.hasPermission("battlepass.admin")) {
                    player.sendMessage("§cNo tienes permisos.");
                    return true;
                }

                data.setDailyXP(0);
                player.sendMessage("§aDaily XP reiniciado.");
            }

            case "help" -> {
                sendHelp(player);
            }

            default -> {
                player.sendMessage("§cSubcomando desconocido. Usa /bpa help");
            }
        }

        return true;
    }

    // =========================
    // 📊 INFO PRINCIPAL
    // =========================
    private void sendMainInfo(Player player, PlayerData data) {

        int level = data.getLevel();
        int xp = data.getXP();
        int dailyXP = data.getDailyXP();
        int remaining = data.getRemainingDailyXP();
        int xpToNext = 500 - xp;

        player.sendMessage("§8§m----------------------------");
        player.sendMessage("§6§lPASE DE BATALLA");
        player.sendMessage("");
        player.sendMessage("§eNivel: §f" + level);
        player.sendMessage("§eXP: §f" + xp + " §7/ 500");
        player.sendMessage("§eFaltante: §f" + xpToNext + " XP");
        player.sendMessage("");
        player.sendMessage("§bXP diaria: §f" + dailyXP + " §7/ 1000");
        player.sendMessage("§bRestante hoy: §f" + remaining);
        player.sendMessage("§8§m----------------------------");
    }

    // =========================
    // 📜 MISIONES
    // =========================
    private void sendMissions(Player player) {

        player.sendMessage("§8§m----------------------------");
        player.sendMessage("§d§lMISIONES ACTIVAS");
        player.sendMessage("");

        // 🔥 Placeholder (luego lo conectas a tu MissionManager)
        player.sendMessage("§7- Mina 100 bloques §8(45/100)");
        player.sendMessage("§7- Mata 20 mobs §8(10/20)");
        player.sendMessage("§7- Juega 30 minutos §8(15/30)");

        player.sendMessage("");
        player.sendMessage("§8Próximamente: recompensas automáticas");
        player.sendMessage("§8§m----------------------------");
    }

    // =========================
    // 📅 DAILY INFO
    // =========================
    private void sendDaily(Player player, PlayerData data) {

        player.sendMessage("§8§m----------------------------");
        player.sendMessage("§b§lPROGRESO DIARIO");
        player.sendMessage("");
        player.sendMessage("§7XP ganada hoy: §f" + data.getDailyXP());
        player.sendMessage("§7Restante: §f" + data.getRemainingDailyXP());
        player.sendMessage("");
        player.sendMessage("§8Reset: 5:00 AM");
        player.sendMessage("§8§m----------------------------");
    }

    // =========================
    // ❓ HELP
    // =========================
    private void sendHelp(Player player) {

        player.sendMessage("§8§m----------------------------");
        player.sendMessage("§6§lCOMANDOS PASE DE BATALLA");
        player.sendMessage("");
        player.sendMessage("§e/bpa §7→ Ver progreso");
        player.sendMessage("§e/bpa missions §7→ Ver misiones");
        player.sendMessage("§e/bpa daily §7→ Progreso diario");
        player.sendMessage("§e/bpa help §7→ Ayuda");

        if (player.hasPermission("battlepass.admin")) {
            player.sendMessage("");
            player.sendMessage("§cADMIN:");
            player.sendMessage("§e/bpa give <xp>");
            player.sendMessage("§e/bpa resetdaily");
        }

        player.sendMessage("§8§m----------------------------");
    }
}