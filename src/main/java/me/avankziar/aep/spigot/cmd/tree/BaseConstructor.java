package main.java.me.avankziar.aep.spigot.cmd.tree;

public class BaseConstructor
{
	private String name;
	private String path;
	private String permission;
	private String suggestion;
	private String commandString;
	private boolean canConsoleAccess;
	
	public BaseConstructor(String name, String path, String permission, String suggestion, String commandString, boolean canConsoleAccess)
	{
		setName(name);
		setPath(path);
		setPermission(permission);
		setSuggestion(suggestion);
		setCommandString(commandString);
		setCanConsoleAccess(canConsoleAccess);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public String getSuggestion()
	{
		return suggestion;
	}

	public void setSuggestion(String suggestion)
	{
		this.suggestion = suggestion;
	}

	public String getCommandString()
	{
		return commandString;
	}

	public void setCommandString(String commandString)
	{
		this.commandString = commandString;
	}

	public boolean canConsoleAccess()
	{
		return canConsoleAccess;
	}

	public void setCanConsoleAccess(boolean canConsoleAccess)
	{
		this.canConsoleAccess = canConsoleAccess;
	}

}
