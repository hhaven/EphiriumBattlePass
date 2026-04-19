package com.ephirium.ephiriumBattlePass.listeners;

import com.ephirium.ephiriumBattlePass.EphiriumBattlePass;
import com.ephirium.ephiriumBattlePass.model.PlayerData;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public class CashbackListener implements Listener {

    private final Currency currency;

    public CashbackListener() {
        this.currency = CoinsEngineAPI.getCurrency("money");
    }

    @EventHandler
    public void onSellTransaction(PostTransactionEvent e) {
        Transaction.Result result = e.getTransactionResult();
        if (result != Transaction.Result.SUCCESS
                && result != Transaction.Result.SUCCESS_COMMANDS_EXECUTED) return;

        if (!e.getTransactionType().name().contains("SELL")) return;

        Player player = e.getPlayer();
        if (player == null) return;

        double price = e.getPrice();
        if (price <= 0) return;

        EphiriumBattlePass.getInstance()
                .getMissionManager()
                .progress(player, "MONEY", (int) price);
    }

    @EventHandler
    public void onPostTransaction(PostTransactionEvent e) {
        // Solo transacciones de compra exitosas
        Transaction.Result result = e.getTransactionResult();
        if (result != Transaction.Result.SUCCESS
                && result != Transaction.Result.SUCCESS_COMMANDS_EXECUTED) return;

        if (!e.getTransactionType().name().contains("BUY")) return;

        Player player = e.getPlayer();
        if (player == null) return;

        PlayerData data = EphiriumBattlePass.getInstance()
                .getBattlePassManager()
                .getData(player);

        if (!data.hasActiveCashback()) return;

        double price = e.getPrice();
        if (price <= 0) return;

        double cashback = Math.round(price * (data.getCashbackPercent() / 100.0));
        if (cashback <= 0) return;

        CoinsEngineAPI.addBalance(player, currency, cashback);
        player.sendMessage("§a+§e" + (int) cashback
                + " §acashback §7(§e" + (int) data.getCashbackPercent() + "%§7)");
    }
}
