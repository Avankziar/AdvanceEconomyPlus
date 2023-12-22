package main.java.me.avankziar.aep.spigot.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.aep.general.objects.AEPUser;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.api.economy.AccountHandler;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.ifh.general.assistance.ChatApi;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.account.AccountType;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class PlayerListener implements Listener
{
	private AdvancedEconomyPlus plugin;
	private LinkedHashMap<String, Double> onDeathWalletLostinPercent = new LinkedHashMap<>();
	
	public PlayerListener(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
		for(String unsp : plugin.getYamlHandler().getConfig().getStringList("Do.OnDeath.MoneyInWalletLostInPercent"))
		{
			String[] sp = unsp.split(";");
			if(sp.length != 2)
			{
				continue;
			}
			if(!MatchApi.isDouble(sp[1]))
			{
				continue;
			}
			EconomyCurrency ec = plugin.getIFHApi().getCurrency(sp[0]);
			if(ec != null)
			{
				double a = Double.parseDouble(sp[1]);
				if(a < 0.0)
				{
					continue;
				}
				if(a > 100.0)
				{
					a = 100.0;
				}
				onDeathWalletLostinPercent.put(ec.getUniqueName(), a);
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				AEPUser aepu = (AEPUser) plugin.getMysqlHandler().getData(
						MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
				if(aepu == null)
				{
					aepu = new AEPUser(player.getUniqueId(), player.getName(),
							ConfigHandler.getDefaultMoneyFlowNotification(true),
							ConfigHandler.getDefaultMoneyFlowNotification(false), System.currentTimeMillis());
					plugin.getMysqlHandler().create(MysqlHandler.Type.PLAYERDATA, aepu);
					AccountHandler.createAllCurrencyAccounts(player, 
							plugin.getYamlHandler().getConfig().getBoolean("Enable.ConvertFromBuildThree", false));
				} else
				{
					final UUID uuid = player.getUniqueId();
					final String newname = player.getName();
					aepu.setLastTimeLogin(System.currentTimeMillis());
					aepu.setName(newname);
					updateAccounts(plugin, uuid, newname);
					plugin.getMysqlHandler().updateData(Type.PLAYERDATA, aepu, "`player_uuid` = ?", aepu.getUUID().toString());
					if(plugin.getYamlHandler().getConfig().getBoolean("Enable.CreateAccountsDespiteTheFactThatThePlayerIsRegistered", false))
					{
						AccountHandler.createAllCurrencyAccounts(player, plugin.getYamlHandler().getConfig().getBoolean("Enable.ConvertFromBuildThree", false));
					}
				}
				if(player.hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_JOINLISTENER)))
				{
					if(!plugin.getYamlHandler().getConfig().getBoolean("Do.ShowOverdueAccounts", true))
					{
						return;
					}
					int days = plugin.getYamlHandler().getConfig().getInt("Do.OverdueTimeInDays", 90);
					long overdate = System.currentTimeMillis()-(long) days*1000*60*60*24;
					int overdueac = plugin.getMysqlHandler().getCount(Type.PLAYERDATA, "`unixtime` < ?", overdate);
					int deletedays = plugin.getYamlHandler().getConfig().getInt("Do.DeleteAccountsDaysAfterOverdue", 30);
					if(overdueac > 0 && deletedays > 0)
					{	
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("JoinListener.OverdueAccounts")
								.replace("%amount%", String.valueOf(overdueac))
								.replace("%days%", String.valueOf(days))
								.replace("%deletedays%", String.valueOf(deletedays))));
					} else if(overdueac > 0 && deletedays < 0)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("JoinListener.OverdueAccountsWithoutDelete")
								.replace("%amount%", String.valueOf(overdueac))
								.replace("%days%", String.valueOf(days))));
					}
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private boolean updateAccounts(AdvancedEconomyPlus plugin, UUID uuid, String newplayername) 
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.ACCOUNT.getValue()
						+ "` SET `owner_name` = ?"
						+ " WHERE `owner_uuid` = ?";
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, newplayername);
				preparedStatement.setString(2, uuid.toString());
				
				preparedStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				AdvancedEconomyPlus.log.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (preparedStatement != null) 
					{
						preparedStatement.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return false;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		if(onDeathWalletLostinPercent.isEmpty())
		{
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				final UUID uuid = event.getEntity().getUniqueId();
				ArrayList<Account> acs = new ArrayList<>();
				try
				{
					acs = ConvertHandler.convertListII(
							plugin.getMysqlHandler().getAllListAt(Type.ACCOUNT, "`id` ASC", "`owner_uuid` = ? AND `account_type` = ?",
									uuid.toString(), AccountType.WALLET.toString()));
				} catch (Exception e)
				{
					return;
				}
				if(acs.isEmpty())
				{
					return;
				}
				final String category = plugin.getYamlHandler().getLang().getString("OnDeath.Category");
				final String comment = plugin.getYamlHandler().getLang().getString("OnDeath.Comment");
				int acCount = 0;
				LinkedHashMap<String, Double> ecmap = new LinkedHashMap<>();
				for(Account ac : acs)
				{
					acCount++;
					Account voi = plugin.getIFHApi().getDefaultAccount(uuid, AccountCategory.VOID, ac.getCurrency());
					Double aec = onDeathWalletLostinPercent.get(ac.getCurrency().getUniqueName());
					if(aec == null || aec < 0.0)
					{
						continue;
					}
					EconomyAction ea = null;
					if(voi != null)
					{
						ea = plugin.getIFHApi().transaction(
								ac, voi, ac.getBalance()*(aec/100), 
								OrdererType.PLAYER, uuid.toString(), category, comment);
					} else
					{
						ea = plugin.getIFHApi().withdraw(
								ac, ac.getBalance()*(aec/100), 
								OrdererType.PLAYER, uuid.toString(), category, comment);
					}
					if(ea.isSuccess())
					{
						double count = 0;
						if(ecmap.containsKey(ac.getCurrency().getUniqueName()))
						{
							count = ecmap.get(ac.getCurrency().getUniqueName())+ea.getWithDrawAmount();
						} else
						{
							count = ea.getWithDrawAmount();
						}
						ecmap.put(ac.getCurrency().getUniqueName(), count);
					}
				}
				if(acCount == 0)
				{
					return;
				}
				StringBuilder sb = new StringBuilder();
				sb.append(plugin.getYamlHandler().getLang().getString("DeathListener.Hover")+"~!~");
				for(String ec : ecmap.keySet())
				{
					sb.append(plugin.getIFHApi().format(ecmap.get(ec), plugin.getIFHApi().getCurrency(ec))+"~!~");
				}
				event.getEntity().spigot().sendMessage(ChatApi.hoverEvent(
						plugin.getYamlHandler().getLang().getString("DeathListener.MoneyLost")
						.replace("%account%", String.valueOf(acCount)),
						Action.SHOW_TEXT, comment));
			}
		}.runTaskAsynchronously(plugin);
	}
}
