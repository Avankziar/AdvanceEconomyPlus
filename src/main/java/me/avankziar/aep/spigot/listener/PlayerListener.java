package main.java.me.avankziar.aep.spigot.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.economy.AccountHandler;
import main.java.me.avankziar.aep.spigot.cmd.sub.ExtraPerm;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;
import main.java.me.avankziar.ifh.general.assistance.ChatApi;

public class PlayerListener implements Listener
{
	private AdvancedEconomyPlus plugin;
	
	public PlayerListener(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		AEPUser aepu = (AEPUser) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", event.getPlayer().getUniqueId().toString());
		if(aepu == null)
		{
			aepu = new AEPUser(event.getPlayer().getUniqueId(), event.getPlayer().getName(),
					ConfigHandler.getDefaultMoneyFlowNotification(true),
					ConfigHandler.getDefaultMoneyFlowNotification(false), System.currentTimeMillis());
			AccountHandler.createAllCurrencyAccounts(event.getPlayer(), plugin.getYamlHandler().getConfig().getBoolean("Enable.ConvertFromBuildThree", false));
		} else
		{
			final String oldname = aepu.getName();
			final String newname = event.getPlayer().getName();
			aepu.setLastTimeLogin(System.currentTimeMillis());
			if(!newname.equals(oldname))
			{
				aepu.setName(newname);
				updateAccounts(plugin, oldname, newname);
			}
			plugin.getMysqlHandler().updateData(Type.PLAYERDATA, aepu, "`player_uuid` = ?", aepu.getUUID().toString());
			if(plugin.getYamlHandler().getConfig().getBoolean("Enable.CreateAccountsDespiteTheFactThatThePlayerIsRegistered", false))
			{
				AccountHandler.createAllCurrencyAccounts(event.getPlayer(), plugin.getYamlHandler().getConfig().getBoolean("Enable.ConvertFromBuildThree", false));
			}
		}
		if(event.getPlayer().hasPermission(ExtraPerm.get(ExtraPerm.Type.BYPASS_JOINLISTENER)))
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
				event.getPlayer().sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("JoinListener.OverdueAccounts")
						.replace("%amount%", String.valueOf(overdueac))
						.replace("%days%", String.valueOf(days))
						.replace("%deletedays%", String.valueOf(deletedays))));
			} else if(overdueac > 0 && deletedays < 0)
			{
				event.getPlayer().sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("JoinListener.OverdueAccountsWithoutDelete")
						.replace("%amount%", String.valueOf(overdueac))
						.replace("%days%", String.valueOf(days))));
			}
		}
	}
	
	private boolean updateAccounts(AdvancedEconomyPlus plugin, String oldplayername, String newplayername) 
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.ACCOUNT.getValue()
						+ "` SET `owner_name` = ?,"
						+ " WHERE `owner_name` = ?";
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, newplayername);
				preparedStatement.setString(1, oldplayername);
				
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
}
