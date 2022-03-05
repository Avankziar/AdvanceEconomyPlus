package main.java.me.avankziar.aep.spigot.cmd.cet.base;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;

public class DeleteAllPlayerAccounts extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	
	public DeleteAllPlayerAccounts(ArgumentConstructor ac)
	{
		super(ac);
		this.plugin = BaseConstructor.getPlugin();
		this.ac = ac;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Cmd only for Players!");
			return;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(ac.getPermission()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				try
				{
					middlePart(player, args);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	/*
	 * aep deleteallplayeraccounts <playername>
	 */
	private void middlePart(Player player, String[] args) throws IOException
	{
		String playername = args[1];
		AEPUser u = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", playername);
		if(u == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Pay.PlayerIsNotRegistered")));
			return;
		}
		final int accamount = plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCOUNT, "`owner_uuid` = ?", u.getUUID().toString());
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.ACCOUNT, "`owner_uuid` = ?", u.getUUID().toString());
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", u.getUUID().toString());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.DeleteAllPlayerAccounts.Delete")
				.replace("%player%", playername)
				.replace("%amount%", String.valueOf(accamount))));
		return;
	}
}