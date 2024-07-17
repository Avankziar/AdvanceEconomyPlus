package me.avankziar.aep.general.cmd.tree;

import java.util.ArrayList;

import me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;

public class CommandConstructor extends BaseConstructor
{
	public ArrayList<ArgumentConstructor> subcommands;
    public ArrayList<String> tablist;

	public CommandConstructor(CommandExecuteType cet, String path, boolean canConsoleAccess,
    		ArgumentConstructor...argumentConstructors)
    {
		super(cet, getYamlHandling().getCommandString(path+".Name"),
				path,
				getYamlHandling().getCommandString(path+".Permission"),
				getYamlHandling().getCommandString(path+".Suggestion"),
				getYamlHandling().getCommandString(path+".CommandString"),
				getYamlHandling().getCommandString(path+".HelpInfo"),
				canConsoleAccess);
        this.subcommands = new ArrayList<>();
        this.tablist = new ArrayList<>();
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	this.subcommands.add(ac);
        	this.tablist.add(ac.getName());
        }
        getCommandTree().add(this);
    }
}