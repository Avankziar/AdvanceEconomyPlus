package main.java.me.avankziar.aep.spigot.cmd.cet.account;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.AccountManagement;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.ifh.general.economy.account.AccountManagementType;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;

public class AccountSetOwner extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	private ArgumentConstructor ac;
	//ADDME wenn Eigentümer wechselt, müssen angehangende Loan & StandingOrder mitübertragen werden.
	
	public AccountSetOwner(ArgumentConstructor ac)
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
	 * aep account setowner <playername> <accountname> <newowner>
	 */
	private void middlePart(Player player, String[] args) throws IOException
	{
		EconomyEntity ee = plugin.getIFHApi().getEntity(args[2]);
		String acname = args[3];
		EconomyEntity nowner = plugin.getIFHApi().getEntity(args[4]);
		if(ee == null || nowner == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("EntityNotExist")));
			return;
		}
		Account ac = plugin.getIFHApi().getAccount(ee, acname);
		if(ac == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Pay.AccountDontExist")));
			return;
		}
		if(ac.getCurrency() == null)
		{
			player.sendMessage(plugin.getYamlHandler().getLang().getString("Cmd.CurrencyNoLoaded").replace("%acn%", ac.getAccountName()));
			return;
		}
		if(!plugin.getIFHApi().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_SET_OWNERSHIP))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.Manage.YouCannotManageTheAccount")));
			return;
		}
		ArrayList<AccountManagement> listAM = ConvertHandler.convertListIX(
				plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCOUNTMANAGEMENT,
						"`id` ASC",
						"`player_uuid` = ? AND `account_id` = ?", ac.getOwner().getUUID().toString(), ac.getID()));
		final String oldname = ac.getAccountName();
		ac.setOwner(nowner);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.ACCOUNT, ac, "`id` = ?", ac.getID());
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Cmd.Account.SetOwner.SetOwner")
				.replace("%acname%", oldname)
				.replace("%acowner%", ac.getOwner().getName())
				.replace("%newowner%", nowner.getName())
				.replace("%newownertype%", plugin.getIFHApi().getEconomyEntityType(ac.getOwner().getType()))));
		if(nowner.getType() == EconomyType.PLAYER)
		{
			for(AccountManagement am : listAM)
			{
				if(!plugin.getIFHApi().canManageAccount(ac, nowner.getUUID(), am.getMaganagementType()))
				{
					plugin.getIFHApi().addManagementTypeToAccount(ac, nowner.getUUID(), am.getMaganagementType());
					plugin.getIFHApi().removeManagementTypeFromAccount(ac, ee.getUUID(), am.getMaganagementType());
				}
			}
		}
		return;
	}
}