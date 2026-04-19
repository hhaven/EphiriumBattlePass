package com.ephirium.ephiriumBattlePass.commands;

import com.ephirium.ephiriumBattlePass.listeners.SvenAriochTokenListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HiddenSvenCommand implements CommandExecutor {

    private static final String ALLOWED_PLAYER = "galsenpai";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.getName().equalsIgnoreCase(ALLOWED_PLAYER)) {
            player.sendMessage("§cComando desconocido.");
            return true;
        }

        player.getInventory().addItem(SvenAriochTokenListener.createToken());
        player.sendMessage("§aHas recibido el item especial.");
        return true;
    }
}
