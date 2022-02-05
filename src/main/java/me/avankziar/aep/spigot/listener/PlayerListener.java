package main.java.me.avankziar.aep.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.economy.AccountHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.ConfigHandler;
import main.java.me.avankziar.aep.spigot.object.ne_w.AEPUser;

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
		//ADDME die alten Accounts übertragen
		if(aepu == null)
		{
			aepu = new AEPUser(event.getPlayer().getUniqueId(), event.getPlayer().getName(), 0,
					ConfigHandler.getDefaultMoneyFlowNotification(true),
					ConfigHandler.getDefaultMoneyFlowNotification(false));
			AccountHandler.createAllCurrencyAccounts(event.getPlayer());
		} else
		{
			String newname = event.getPlayer().getName();
			if(!newname.equals(aepu.getName()))
			{
				aepu.setName(newname);
				plugin.getMysqlHandler().updateData(Type.PLAYERDATA, aepu, "`player_uuid` = ?", aepu.getUUID().toString());
				//ADDME alle Accounts updaten wo der Spieler als Eigentümer eingetragen ist.
			}
		}
	}
}
