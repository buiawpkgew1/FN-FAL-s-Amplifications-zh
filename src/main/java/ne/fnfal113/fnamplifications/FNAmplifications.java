package ne.fnfal113.fnamplifications;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

import ne.fnfal113.fnamplifications.config.ConfigManager;
import ne.fnfal113.fnamplifications.gears.commands.CheckProgress;
import ne.fnfal113.fnamplifications.gears.listener.GearListener;
import ne.fnfal113.fnamplifications.gears.runnables.ArmorEquipRunnable;
import ne.fnfal113.fnamplifications.gems.listener.GemListener;
import ne.fnfal113.fnamplifications.gems.listener.GemUnbinderListener;
import ne.fnfal113.fnamplifications.machines.listener.JukeBoxClickListener;
import ne.fnfal113.fnamplifications.mysteriousitems.listener.MysteryStickListener;
import ne.fnfal113.fnamplifications.quivers.listener.QuiverListener;
import ne.fnfal113.fnamplifications.staffs.listener.StaffListener;
import ne.fnfal113.fnamplifications.tools.listener.HoeListener;
import ne.fnfal113.fnamplifications.tools.listener.RotatorListener;
import ne.fnfal113.fnamplifications.utils.PlayerJoinLister;
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ne.fnfal113.fnamplifications.items.FNAmpItemSetup;

import java.util.Objects;
import java.util.logging.Level;

public final class FNAmplifications extends JavaPlugin implements SlimefunAddon {

    private static FNAmplifications instance;

    private final ConfigManager configManager = new ConfigManager();

    @Override
    public void onEnable() {
        setInstance(this);

        new Metrics(this, 13219);

        getLogger().info("************************************************************");
        getLogger().info("              FN Amplifications - FN 科技                   ");
        getLogger().info("           作者: FN_FAL113  汉化: buiawpkgew1                ");
        getLogger().info("    感谢 Jeff(LiteXpansion) 与 Walshy(Slimefun) 提供的工具包   ");
        getLogger().info("               如有任何问题，请前往问题追踪器汇报                ");
        getLogger().info("                作者 Discord: FN_FAL#7779                    ");
        getLogger().info("************************************************************");

        FNAmpItemSetup.INSTANCE.init();

        getServer().getPluginManager().registerEvents(new MysteryStickListener(), this);
        getServer().getPluginManager().registerEvents(new GearListener(), this);
        getServer().getPluginManager().registerEvents(new StaffListener(), this);
        getServer().getPluginManager().registerEvents(new QuiverListener(), this);
        getServer().getPluginManager().registerEvents(new HoeListener(), this);
        getServer().getPluginManager().registerEvents(new GemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinLister(), this);
        getServer().getPluginManager().registerEvents(new RotatorListener(), this);
        getServer().getPluginManager().registerEvents(new JukeBoxClickListener(), this);
        getServer().getPluginManager().registerEvents(new GemUnbinderListener(), this);
        Objects.requireNonNull(getCommand("fngear")).setExecutor(new CheckProgress());
        getServer().getScheduler().runTaskTimerAsynchronously(this, new ArmorEquipRunnable(), 0L, getConfig().getInt("armor-update-period") * 20L);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        if (getConfig().getBoolean("auto-update") &&
            getDescription().getVersion().startsWith("Build ")) {
            new GuizhanBuildsUpdater(this, getFile(), "buiawpkgew1", "FN-FAL-s-Amplifications-zh", "main", false).start();
        }
    }

    @Override
    public void onDisable(){
        Bukkit.getScheduler().cancelTasks(FNAmplifications.getInstance());
        getLogger().log(Level.INFO, "Cancelled any running task that exist");
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/buiawpkgew1/FN-FAL-s-Amplifications-zh/issues";
    }

    public ConfigManager getConfigManager(){
        return instance.configManager;
    }

    private static void setInstance(FNAmplifications ins) {
        instance = ins;
    }

    public static FNAmplifications getInstance() {
        return instance;
    }

}
