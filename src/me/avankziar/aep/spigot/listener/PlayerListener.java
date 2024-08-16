package me.avankziar.aep.spigot.listener;

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

import me.avankziar.aep.general.assistance.ChatApiOld;
import me.avankziar.aep.general.assistance.MatchApi;
import me.avankziar.aep.general.database.MysqlType;
import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.spigot.AEP;
import me.avankziar.aep.spigot.api.economy.AccountHandler;
import me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import me.avankziar.aep.spigot.handler.ConfigHandler;
import me.avankziar.aep.spigot.handler.ConvertHandler;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class PlayerListener implements Listener
{
	private AEP plugin;
	private LinkedHashMap<String, Double> onDeathWalletLostinPercent = new LinkedHashMap<>();
	
	public PlayerListener(AEP plugin)
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
						MysqlType.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
				if(aepu == null)
				{
					aepu = new AEPUser(player.getUniqueId(), player.getName(),
							ConfigHandler.getDefaultMoneyFlowNotification(true),
							ConfigHandler.getDefaultMoneyFlowNotification(false), System.currentTimeMillis());
					plugin.getMysqlHandler().create(MysqlType.PLAYERDATA, aepu);
					AccountHandler.createAllCurrencyAccounts(player);
				} else
				{
					final UUID uuid = player.getUniqueId();
					final String newname = player.getName();
					aepu.setLastTimeLogin(System.currentTimeMillis());
					aepu.setName(newname);
					updateAccounts(plugin, uuid, newname);
					plugin.getMysqlHandler().updateData(MysqlType.PLAYERDATA, aepu, "`player_uuid` = ?", aepu.getUUID().toString());
					if(plugin.getYamlHandler().getConfig().getBoolean("Enable.CreateAccountsDespiteTheFactThatThePlayerIsRegistered", false))
					{
						AccountHandler.createAllCurrencyAccounts(player);
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
					int overdueac = plugin.getMysqlHandler().getCount(MysqlType.PLAYERDATA, "`unixtime` < ?", overdate);
					int deletedays = plugin.getYamlHandler().getConfig().getInt("Do.DeleteAccountsDaysAfterOverdue", 30);
					if(overdueac > 0 && deletedays > 0)
					{	
						player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("JoinListener.OverdueAccounts")
								.replace("%amount%", String.valueOf(overdueac))
								.replace("%days%", String.valueOf(days))
								.replace("%deletedays%", String.valueOf(deletedays))));
					} else if(overdueac > 0 && deletedays < 0)
					{
						player.sendMessage(ChatApiOld.tl(plugin.getYamlHandler().getLang().getString("JoinListener.OverdueAccountsWithoutDelete")
								.replace("%amount%", String.valueOf(overdueac))
								.replace("%days%", String.valueOf(days))));
					}
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private boolean updateAccounts(AEP plugin, UUID uuid, String newplayername) 
	{
		PreparedStatement preparedStatement = null;
		try (Connection conn = plugin.getMysqlSetup().getConnection();)
		{
			String data = "UPDATE `" + MysqlType.ACCOUNT_SPIGOT.getValue()
					+ "` SET `owner_name` = ?"
					+ " WHERE `owner_uuid` = ?";
			preparedStatement = conn.prepareStatement(data);
			preparedStatement.setString(1, newplayername);
			preparedStatement.setString(2, uuid.toString());
			
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			AEP.logger.warning("Error: " + e.getMessage());
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
							plugin.getMysqlHandler().getFullList(MysqlType.ACCOUNT_SPIGOT, "`id` ASC", "`owner_uuid` = ? AND `account_type` = ?",
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
				event.getEntity().spigot().sendMessage(ChatApiOld.hover(
						plugin.getYamlHandler().getLang().getString("DeathListener.MoneyLost")
						.replace("%account%", String.valueOf(acCount)),
						Action.SHOW_TEXT, comment));
			}
		}.runTaskAsynchronously(plugin);
	}
}