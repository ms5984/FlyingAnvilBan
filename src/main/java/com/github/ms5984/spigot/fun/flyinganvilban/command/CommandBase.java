package com.github.ms5984.spigot.fun.flyinganvilban.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class CommandBase extends Command {
    protected final CommandData commandData;

    protected CommandBase() {
        this(CommandData.detectClass());
    }

    protected CommandBase(final CommandData commandData) {
        super(commandData.getLabel());
        this.commandData = commandData;
        setAliases(this.commandData.getAliases());
        setDescription(this.commandData.getDescription());
        setPermission(this.commandData.getPermissionNode());
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(commandData.plugin.getName(), this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Unable to add '" + toString() + "' to the command map!", e);
        }
    }

    protected enum CommandData {
        ANVIL_HAMMER("commands.anvilhammer", AnvilHammerCommand.class),
        BAN_ANVIL("commands.bananvil", BanAnvilCommand.class);

        private final Class<? extends CommandBase> implClass;
        private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());
        private final String confLine;

        <T extends CommandBase> CommandData (String configPath, Class<T> implementation) {
            this.confLine = configPath;
            this.implClass = implementation;
        }

        public String getLabel() {
            return plugin.getConfig().getString(confLine + ".label", "null");
        }

        public List<String> getAliases() {
            return plugin.getConfig().getStringList(confLine + ".aliases");
        }

        public String getDescription() {
            return plugin.getConfig().getString(confLine + ".description", "");
        }

        public String getPermissionNode() {
            return plugin.getConfig().getString(confLine + ".permission");
        }

        private static CommandData detectClass() {
            try {
                final Class<?> aClass = Class.forName(((new Throwable()).getStackTrace()[3].getClassName()));
                if (aClass.getSuperclass() != CommandBase.class) {
                    throw new IllegalStateException("This method must be called from a subclass command constructor only!");
                }
                return Arrays.stream(CommandData.values())
                        .filter(commandData -> commandData.implClass == aClass)
                        .findAny().orElseThrow(IllegalStateException::new);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Command classes lost. The JAR may be corrupt.", e);
            }
        }
    }
}
