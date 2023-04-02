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
    private static Economy economy;

    @Override
    public void onEnable() {
        if (!isPluginEnabled("net.starly.core.StarlyCore")) {
            Bukkit.getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!isPluginEnabled("net.milkbowl.vault.Vault")) {
            Bukkit.getLogger().warning("[" + getName() + "] Vault 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + getName() + "] 다운로드 링크 : https://www.spigotmc.org/resources/vault.34315/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        if (economy == null) {
            Bukkit.getLogger().warning("[" + getName() + "] Vault와 연동되는 Economy 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + getName() + "] 다운로드 링크 : https://essentialsx.net/downloads.html");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        new Metrics(this, 17831);

        // CONFIG
        saveDefaultConfig();
        MessageContent.getInstance().initializing(getConfig());


        Bukkit.getPluginCommand("cheque").setExecutor(new ChequeCmd());
        Bukkit.getPluginCommand("cheque").setTabCompleter(new ChequeTab());

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static ChequeMain getInstance() {
        return instance;
    }

    private boolean isPluginEnabled(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (ClassNotFoundException ignored) {
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }
}