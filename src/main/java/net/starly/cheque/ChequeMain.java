package net.starly.cheque;

import net.milkbowl.vault.economy.Economy;
import net.starly.cheque.command.ChequeCmd;
import net.starly.cheque.command.tabcomplete.ChequeTab;
import net.starly.cheque.context.MessageContent;
import net.starly.cheque.listener.PlayerInteractListener;
import net.starly.core.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ChequeMain extends JavaPlugin {

    private static ChequeMain instance;
    private static Economy economy = null;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("ST-Core") == null) {
            Bukkit.getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + getName() + "] 다운로드 링크 : http://starly.kr/discord");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy()) {
            Bukkit.getLogger().warning("[" + getName() + "] Vault 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + getName() + "] 다운로드 링크 : https://www.spigotmc.org/resources/vault.34315/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        new Metrics(this, 17831);

        // CONFIG
        if (!new File("config.yml").exists()) saveDefaultConfig();
        MessageContent.getInstance().initializing(getConfig());


        Bukkit.getPluginCommand("수표").setExecutor(new ChequeCmd());
        Bukkit.getPluginCommand("수표").setTabCompleter(new ChequeTab());

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static ChequeMain getInstance() {
        return instance;
    }
}