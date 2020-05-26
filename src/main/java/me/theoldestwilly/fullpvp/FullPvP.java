package me.theoldestwilly.fullpvp;


import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import me.theoldestwilly.fullpvp.anvil.AnvilCommand;
import me.theoldestwilly.fullpvp.anvil.AnvilHandler;
import me.theoldestwilly.fullpvp.chests.kitschest.KitsChestListener;
import me.theoldestwilly.fullpvp.chests.kitschest.KitsChestManager;
import me.theoldestwilly.fullpvp.chests.kitschest.command.KitChestCommand;
import me.theoldestwilly.fullpvp.chests.mapchest.MapChestListener;
import me.theoldestwilly.fullpvp.chests.mapchest.MapChestManager;
import me.theoldestwilly.fullpvp.chests.mapchest.command.MapChestCommand;
import me.theoldestwilly.fullpvp.clan.ClanListener;
import me.theoldestwilly.fullpvp.clan.ClanManager;
import me.theoldestwilly.fullpvp.clan.command.ClanCommand;
import me.theoldestwilly.fullpvp.combatlogger.CombatLoggerHandler;
import me.theoldestwilly.fullpvp.crate.CrateListener;
import me.theoldestwilly.fullpvp.crate.CrateManager;
import me.theoldestwilly.fullpvp.crate.command.CrateCommand;
import me.theoldestwilly.fullpvp.economy.EconomyManager;
import me.theoldestwilly.fullpvp.economy.SignShopListener;
import me.theoldestwilly.fullpvp.economy.command.BalanceCommand;
import me.theoldestwilly.fullpvp.economy.command.EconomyCommand;
import me.theoldestwilly.fullpvp.economy.command.PayCommand;
import me.theoldestwilly.fullpvp.event.command.GlobalEventCommand;
import me.theoldestwilly.fullpvp.event.global.dropper.command.DropSourceCommand;
import me.theoldestwilly.fullpvp.event.global.GlobalEventsManager;
import me.theoldestwilly.fullpvp.event.global.dtc.command.DestroyTheCoreCommand;
import me.theoldestwilly.fullpvp.event.global.koths.command.KothCommand;
import me.theoldestwilly.fullpvp.event.hosted.HostedEventsManager;
import me.theoldestwilly.fullpvp.event.hosted.command.*;
import me.theoldestwilly.fullpvp.event.hosted.structure.HostedEvent;
import me.theoldestwilly.fullpvp.event.scheduler.CheckCurrentTimeCommand;
import me.theoldestwilly.fullpvp.event.scheduler.SchedulerHandler;
import me.theoldestwilly.fullpvp.kits.KitManager;
import me.theoldestwilly.fullpvp.kits.KitsListener;
import me.theoldestwilly.fullpvp.kits.command.KitCommand;
import me.theoldestwilly.fullpvp.leaderboard.LeaderBoardCommand;
import me.theoldestwilly.fullpvp.leaderboard.LeaderBoardManager;
import me.theoldestwilly.fullpvp.modes.ModesManager;
import me.theoldestwilly.fullpvp.modes.command.ServerModeCommand;
import me.theoldestwilly.fullpvp.mongo.MongoManager;
import me.theoldestwilly.fullpvp.profileviewer.ProfileViewerHandler;
import me.theoldestwilly.fullpvp.profileviewer.ProfileVillagerCommand;
import me.theoldestwilly.fullpvp.pvptimer.PvpTimerCommand;
import me.theoldestwilly.fullpvp.rename.RenameCommand;
import me.theoldestwilly.fullpvp.rename.RenameManager;
import me.theoldestwilly.fullpvp.scoreboard.ScoreboardManager;
import me.theoldestwilly.fullpvp.scoreboard.ToggleScoreboardCommand;
import me.theoldestwilly.fullpvp.staffmode.FreezeCommand;
import me.theoldestwilly.fullpvp.staffmode.FreezeHandler;
import me.theoldestwilly.fullpvp.staffmode.StaffModeCommand;
import me.theoldestwilly.fullpvp.staffmode.StaffModeHandler;
import me.theoldestwilly.fullpvp.users.PlayersHandler;
import me.theoldestwilly.fullpvp.users.ProfileHandler;
import me.theoldestwilly.fullpvp.users.commands.*;
import me.theoldestwilly.fullpvp.users.commands.manager.PlayersCommandsManager;
import me.theoldestwilly.fullpvp.users.rewards.ReclaimCommand;
import me.theoldestwilly.fullpvp.users.rewards.RewardCommand;
import me.theoldestwilly.fullpvp.users.rewards.RewardsListener;
import me.theoldestwilly.fullpvp.users.rewards.RewardsManager;
import me.theoldestwilly.fullpvp.users.tutorial.TutorialHandler;
import me.theoldestwilly.fullpvp.utilities.Config;
import me.theoldestwilly.fullpvp.utilities.commands.ArgumentExecutor;
import me.theoldestwilly.fullpvp.world.serverspawn.RemoveSpawnCommand;
import me.theoldestwilly.fullpvp.world.serverspawn.SetSpawnCommand;
import me.theoldestwilly.fullpvp.world.WorldProtectorListener;
import me.theoldestwilly.fullpvp.world.claims.ClaimHandler;
import me.theoldestwilly.fullpvp.world.claims.command.ClaimCommand;
import me.theoldestwilly.fullpvp.world.serverspawn.SpawnHandler;
import net.silexpvp.nightmare.Nightmare;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class FullPvP extends JavaPlugin {
    private static @Getter FullPvP instance;

    private @Getter Nightmare nightmare;
    private @Getter WorldEditPlugin worldEditPlugin;

    private @Getter Config config;
    private @Getter boolean debugEnabled;
    private @Getter int maxCoreHealthDtc;
    private @Getter Long secondsBeforeHealthDtc;

    private @Getter ProfileHandler profileHandler;
    private @Getter ClaimHandler claimHandler;
    private @Getter PlayersHandler playersHandler;
    private @Getter CombatLoggerHandler combatLoggerHandler;

    private @Getter ScoreboardManager scoreboardManager;
    private @Getter MapChestManager mapChestManager;
    private @Getter AnvilHandler anvilHandler;
    private @Getter KitsChestManager kitsChestManager;
    private @Getter EconomyManager economyManager;
    private @Getter CrateManager crateManager;
    private @Getter KitManager kitManager;
    private @Getter MongoManager pluginDatabase;
    private @Getter GlobalEventsManager globalEventsManager;
    private @Getter ClanManager clanManager;
    private @Getter RewardsManager rewardsManager;
    private @Getter PlayersCommandsManager playersCommandsManager;
    private @Getter RenameManager renameManager;
    private @Getter ProfileViewerHandler profileViewerHandler;
    private @Getter HostedEventsManager hostedEventsManager;
    private @Getter SchedulerHandler schedulerHandler;
    private @Getter TutorialHandler tutorialHandler;
    private @Getter LeaderBoardManager leaderBoardManager;
    private @Getter SpawnHandler spawnHandler;
    private @Getter ModesManager modesManager;


    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this, "config");
        pluginDatabase = new MongoManager(this);
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + Lang.SUCCESS_COLOR + "Successfully connected to the database [Mongo]");
        registerDependencies();
        registerCommandsUsingNightmare();
        registerManagers();
        registerListenersAndHandlers();
        registerCommands();
        registerConfigValues();
        //En caso que se haya activado por reload o en su defecto con plugman
        profileHandler.loadOnlineProfiles();

    }

    @Override
    public void onDisable() {
        combatLoggerHandler.removeAllTags();
        kitManager.saveKits();
        scoreboardManager.unregisterAll();
        anvilHandler.saveAnvils();
        mapChestManager.saveMapChests();
        kitsChestManager.saveKitsChest();
        crateManager.saveCrates();
        profileHandler.saveOnlineProfiles();
        globalEventsManager.saveEvents();
        claimHandler.saveClaims();
        clanManager.saveClans();
        profileViewerHandler.saveVillagers();
        leaderBoardManager.saveLeaderBoards();
        HostedEvent event = hostedEventsManager.getCurrentEvent();
        spawnHandler.saveSpawns();
        if (event != null) event.onPluginDisabled();
        instance = null;
    }

    private void registerConfigValues() {
        debugEnabled = config.getBoolean("debug-msg");
        maxCoreHealthDtc = config.getInt("events.dtc.health", 50);
        secondsBeforeHealthDtc = TimeUnit.SECONDS.toMillis(config.getInt("events.dtc.seconds-to-healing", 20));
    }

    private void registerManagers() {
        clanManager = new ClanManager(this);
        scoreboardManager = new ScoreboardManager(this);
        mapChestManager = new MapChestManager(this);
        kitsChestManager = new KitsChestManager(this);
        economyManager = new EconomyManager(this);
        crateManager = new CrateManager(this);
        kitManager = new KitManager(this);
        globalEventsManager = new GlobalEventsManager(this);
        rewardsManager = new RewardsManager(this);
        playersCommandsManager = new PlayersCommandsManager(this);
        renameManager = new RenameManager(this);
        modesManager = new ModesManager(this);
    }

    private void registerDependencies() {
        nightmare = JavaPlugin.getPlugin(Nightmare.class);
        worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEditPlugin == null) {
            getServer().getPluginManager().disablePlugin(this);
            Bukkit.getServer().getConsoleSender().sendMessage(Lang.PREFIX + Lang.ERROR_COLOR + "World Edit not found.");
        }
    }


    private void registerListenersAndHandlers() {
        registerListeners(
                profileHandler = new ProfileHandler(this),
                claimHandler = new ClaimHandler(this),
                anvilHandler = new AnvilHandler(this),
                playersHandler = new PlayersHandler(this),
                combatLoggerHandler = new CombatLoggerHandler(this),
                profileViewerHandler = new ProfileViewerHandler(this),
                schedulerHandler = new SchedulerHandler(this),
                tutorialHandler = new TutorialHandler(this),
                hostedEventsManager = new HostedEventsManager(this),
                leaderBoardManager = new LeaderBoardManager(this),
                spawnHandler = new SpawnHandler(this),
                new MapChestListener(),
                new KitsChestListener(),
                new CrateListener(this),
                new KitsListener(this),
                new WorldProtectorListener(this),
                new ClanListener(this),
                new RewardsListener(this),
                new SignShopListener(this),
                new StaffModeHandler(this),
                new FreezeHandler(this)
        );
    }

    private void registerListeners(Listener... listeners) {
        Stream.of(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        //Console command
        registerCommandWithoutPlYml(new ServerModeCommand(this), false);

        //Commands with perms: Node = "fullpvp.command." + cmdname
        registerCommandWithoutPlYml(new MapChestCommand(this), true);
        registerCommandWithoutPlYml(new KitChestCommand(this), true);
        registerCommandWithoutPlYml(new EconomyCommand(this), true);
        registerCommandWithoutPlYml(new CrateCommand(this), true);
        registerCommandWithoutPlYml(new ErrorCheckCommand(this), true);
        registerCommandWithoutPlYml(new KothCommand(this), true);
        registerCommandWithoutPlYml(new RewardCommand(this), true);
        registerCommandWithoutPlYml(new ClaimCommand(this), true);
        registerCommandWithoutPlYml(new SetSpawnCommand(this), true);
        registerCommandWithoutPlYml(new EventSimulatorCommand(), true);
        registerCommandWithoutPlYml(new ForceCommand(), true);
        registerCommandWithoutPlYml(new RenameCommand(this), true);
        registerCommandWithoutPlYml(new StaffModeCommand(this), true);
        registerCommandWithoutPlYml(new ProfileVillagerCommand(this), true);
        registerCommandWithoutPlYml(new DropSourceCommand(this), true);
        registerCommandWithoutPlYml(new DestroyTheCoreCommand(this), true);
        registerCommandWithoutPlYml(new GlobalEventCommand(this), true);
        registerCommandWithoutPlYml(new RemoveSpawnCommand(this), true);
        registerCommandWithoutPlYml(new FreezeCommand(this), true);
        registerCommandWithoutPlYml(new PvpTimerCommand(this), true);

        //This command has permissions by each argument but not global
        registerCommandWithoutPlYml(new EventGameCommand(this), false);
        registerCommandWithoutPlYml(new KitCommand(this), false);

        //Commands without perms
        registerCommandWithoutPlYml(new CheckCurrentTimeCommand(), false);
        registerCommandWithoutPlYml(new LeaderBoardCommand(), false);
        registerCommandWithoutPlYml(new KillsCheckerCommand(this), false);
        registerCommandWithoutPlYml(new BalanceCommand(this), false);
        registerCommandWithoutPlYml(new PayCommand(this), false);
        registerCommandWithoutPlYml(new ClanCommand(this), false);
        registerCommandWithoutPlYml(new ReclaimCommand(this), false);
        registerCommandWithoutPlYml(new CoordsCommand(this), false);
        registerCommandWithoutPlYml(new StatsCommand(this), false);
        registerCommandWithoutPlYml(new HelpCommand(this), false);
        registerCommandWithoutPlYml(new SpawnCommand(this), false);
        //registerCommandWithoutPlYml(new TutorialCommand(this), false);
        registerCommandWithoutPlYml(new JoinEventCommand(this), false);
        registerCommandWithoutPlYml(new LeaveEventCommand(this), false);
        registerCommandWithoutPlYml(new SpectEventCommand(this), false);
        registerCommandWithoutPlYml(new EventCommand(this), false);
    }

    private void registerCommandWithoutPlYml(ArgumentExecutor executor, boolean requiresPermission) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(executor.getLabel(), this);
            pluginCommand.setExecutor(executor);
            pluginCommand.setTabCompleter(executor);
            if (executor.getAliases() != null)
                pluginCommand.setAliases(Arrays.asList(executor.getAliases()));
            pluginCommand.setPermissionMessage(ChatColor.RED + "You do not have permission to execute this command.");
            if (requiresPermission) {
                pluginCommand.setPermission("fullpvp.command." + executor.getLabel());
            }

            CommandMap commandMap = null;
            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap) field.get(this.getServer().getPluginManager());
            } catch (Exception var2) {
                var2.printStackTrace();
            }

            if (commandMap != null && !commandMap.register(executor.getLabel(), pluginCommand)) {
                pluginCommand.unregister(commandMap);
                commandMap.register(executor.getLabel(), pluginCommand);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void registerCommandsUsingNightmare() {
        nightmare.registerCommand(new ToggleScoreboardCommand());
        nightmare.registerCommand(new AnvilCommand());
    }
}
