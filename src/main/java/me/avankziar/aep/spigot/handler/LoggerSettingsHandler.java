package main.java.me.avankziar.aep.spigot.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.database.Language.ISO639_2B;
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.InventoryHandlerType;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.OrderType;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;
import main.java.me.avankziar.aep.spigot.object.subs.ActionFilterSettings;
import main.java.me.avankziar.aep.spigot.object.subs.TrendFilterSettings;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class LoggerSettingsHandler
{
	public enum Methode
	{
		BARCHART, DIAGRAM, GRAFIC, LOG, JSON;
	}
	
	public AdvancedEconomyPlus plugin;
	private static LinkedHashMap<UUID, LoggerSettings> filterSettings = new LinkedHashMap<>();
	public static String loggerSettingsCommandString = "";
	public static String loggerSettingsTextCommandString = "";
	
	public LoggerSettingsHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
	}
	
	public static LinkedHashMap<UUID, LoggerSettings> getLoggerSettings()
	{
		return filterSettings;
	}
	
	public void generateGUI(Player player, UUID uuid, UUID UseUUID, String UseBankNumber, Inventory openInventory, int page)
	{
		Inventory inventory = openInventory;
		if(openInventory == null)
		{
			inventory = Bukkit.createInventory(null, 9*6, ChatApi.tl("&cLogger &aSettings"));
		}
		LoggerSettings fst = getLoggerSettings().get(uuid);
		if(fst == null)
		{
			fst = new LoggerSettings(UseUUID, UseBankNumber, page);
			fst.setInventoryHandlerType(InventoryHandlerType.NORMAL);
			getLoggerSettings().put(uuid, fst);
		}
		for(int i = 0; i < inventory.getSize(); i++)
		{
			switch(i)
			{
			//Fall-Through
			//case 31: //Json
			case 40: //Barchart Befehl ausführen
			case 48: //Diagram Befehl ausführen. Linksklick: Action | Rechtsklick: Trend
			case 49: //Log Befehl ausführen.
			case 50: //Grafik Befehl ausführen.
			
			//Min ist für LessThan, Between
			case 0: //Min. +1 +50 | -1 -50
			case 9: //Min. +50k +1M | -50k -1M
			case 18: //Min. +50k +1M | -50k -1M
			
			//Max ist für Greatthan, Between
			case 8: //Max. +1 +50 | -1 -50
			case 17: //Max. +1k +50k | -1k -50k
			case 26: //Max. +50M +50M | -1M -50M
				
			case 29: //FirstStand +1k +50k | -1k -50K
			case 38: //FirstStand +1M +50M | -1M -50M
			
			case 33: //LastStand +1k +50k | -1k -50K
			case 42: //LastStand +1M +50M | -1M -50M
				
			case 3: //From Set Befehl ausführen
			case 4: //Orderer Befehl ausführen. Wird auf für den Trend genutzt.
			case 5: //to Set Befehl ausführen
			case 13: //Comment Set
			
			case 11: //Descending boolean einbauen
			case 15: //OrderType einstellen.
				ItemStack is = generateItem(i, fst, uuid, true, false);
				inventory.setItem(i, is);
				break;
			case 36: //PreSet 1
			case 44: //PreSet 2
			case 45: //PreSet 3
			case 53: //PreSet 4
				ItemStack preset = generateItem(i, fst, uuid, false, true);
				inventory.setItem(i, preset);
				break;
			default:
				break;
			}
		}
		fst.setInventoryHandlerType(InventoryHandlerType.NORMAL);
		if(openInventory == null)
		{
			player.openInventory(inventory);
		}
		return;
	}
	
	public void resetLoggerSetting(LoggerSettings fst)
	{
		fst.setMin(null);
		fst.setMax(null);
		fst.setDescending(true);
		fst.setOrderType(OrderType.ID);
		fst.setActionFilter(new ActionFilterSettings());
		fst.setTrendfFilter(new TrendFilterSettings());
	}
	
	public void generateActionFromClick(InventoryClickEvent event) throws IOException
	{
		Inventory inventory = event.getClickedInventory();
		if(inventory == null)
		{
			return;
		}
		if(event.getClickedInventory().getType() != InventoryType.CHEST)
		{
			return;
		}
		UUID uuid = event.getWhoClicked().getUniqueId();
		LoggerSettings fst = getLoggerSettings().get(uuid);
		if(fst == null)
		{
			event.setResult(Result.DENY);
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}
		if(fst.getInventoryHandlerType() != InventoryHandlerType.NORMAL)
		{
			event.setResult(Result.DENY);
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}
		event.setResult(Result.DENY);
		event.setCancelled(true);
		switch(event.getSlot())
		{
		/*case 31:
			if(event.isLeftClick())
			{
				fst.setAction(true);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.JSON, 0);
			} else if(event.isRightClick())
			{
				fst.setAction(false);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, false, Methode.JSON, 0);
			}
			break;*/
		case 40: //Barchart Befehl ausführen
			fst.setAction(true);
			getLoggerSettings().replace(uuid, fst);
			forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.BARCHART, 0);
			break;
		case 48: //Diagram Befehl ausführen. Linksklick: Action | Rechtsklick: Trend
			if(event.isLeftClick())
			{
				fst.setAction(true);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.DIAGRAM, 0);
			} else if(event.isRightClick())
			{
				fst.setAction(false);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, false, Methode.DIAGRAM, 0);
			}
			break;
		case 50: //Grafik Befehl ausführen.
			if(event.isLeftClick())
			{
				fst.setAction(true);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.GRAFIC, 0);
			} else if(event.isRightClick())
			{
				fst.setAction(false);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, false, Methode.GRAFIC, 0);
			}
			break;
		case 49: //Log Befehl ausführen.
			if(event.isLeftClick())
			{
				fst.setAction(true);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.LOG, 0);
			} else if(event.isRightClick())
			{
				fst.setAction(false);
				getLoggerSettings().replace(uuid, fst);
				forwardingToOutput((Player) event.getWhoClicked(), fst, false, Methode.LOG, 0);
			}
			break;
		case 36: //PreSet 1
			int slotid = 1;
			if(event.isShiftClick())
			{
				if(!plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(Type.LOGGERSETTINGSPRESET, "`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
			} else if(event.isLeftClick())
			{
				LoggerSettings ls = (LoggerSettings) plugin.getMysqlHandler().getData(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
				if(ls == null)
				{
					return;
				}
				getLoggerSettings().replace(uuid, ls);
			} else if(event.isRightClick())
			{
				if(plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				fst.setSlotid(slotid);
				plugin.getMysqlHandler().create(Type.LOGGERSETTINGSPRESET, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 44: //PreSet 2
			slotid = 2;
			if(event.isShiftClick())
			{
				if(!plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(Type.LOGGERSETTINGSPRESET, "`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
			} else if(event.isLeftClick())
			{
				LoggerSettings ls = (LoggerSettings) plugin.getMysqlHandler().getData(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
				if(ls == null)
				{
					return;
				}
				getLoggerSettings().replace(uuid, ls);
			} else if(event.isRightClick())
			{
				if(plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				fst.setSlotid(slotid);
				plugin.getMysqlHandler().create(Type.LOGGERSETTINGSPRESET, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 45: //PreSet 3
			slotid = 3;
			if(event.isShiftClick())
			{
				if(!plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(Type.LOGGERSETTINGSPRESET, "`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
			} else if(event.isLeftClick())
			{
				LoggerSettings ls = (LoggerSettings) plugin.getMysqlHandler().getData(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
				if(ls == null)
				{
					return;
				}
				getLoggerSettings().replace(uuid, ls);
			} else if(event.isRightClick())
			{
				if(plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				fst.setSlotid(slotid);
				plugin.getMysqlHandler().create(Type.LOGGERSETTINGSPRESET, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 53: //PreSet 4
			slotid = 4;
			if(event.isShiftClick())
			{
				if(!plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(Type.LOGGERSETTINGSPRESET, "`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
			} else if(event.isLeftClick())
			{
				LoggerSettings ls = (LoggerSettings) plugin.getMysqlHandler().getData(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
				if(ls == null)
				{
					return;
				}
				getLoggerSettings().replace(uuid, ls);
			} else if(event.isRightClick())
			{
				if(plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
				{
					return;
				}
				fst.setSlotid(slotid);
				plugin.getMysqlHandler().create(Type.LOGGERSETTINGSPRESET, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 3: //Sender Set Befehl ausführen
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getActionFilter().setFrom(null);
				getLoggerSettings().replace(uuid, fst);
				generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
				break;
			} else if(event.getClick() == ClickType.LEFT
					|| event.getClick() == ClickType.RIGHT) // Links || Rechts
			{
				fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_FROM);
				getLoggerSettings().replace(uuid, fst);
				generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
				break;
			}
			break;
		case 4: //Orderer Befehl ausführen. Wird auf für den Trend genutzt.
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getActionFilter().setOrderer(null);
				getLoggerSettings().replace(uuid, fst);
				generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
				break;
			} else if(event.getClick() == ClickType.LEFT
					|| event.getClick() == ClickType.RIGHT) // Links || Rechts
			{
				fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_ORDERER);
				getLoggerSettings().replace(uuid, fst);
				generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
				break;
			}
			break;
		case 5: //to Set Befehl ausführen
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getActionFilter().setTo(null);
				getLoggerSettings().replace(uuid, fst);
				generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
				break;
			} else if(event.getClick() == ClickType.LEFT
					|| event.getClick() == ClickType.RIGHT) // Links || Rechts
			{
				fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_TO);
				getLoggerSettings().replace(uuid, fst);
				generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
				break;
			}
			break;
		case 13: //Comment Set
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getActionFilter().setComment(null);
				getLoggerSettings().replace(uuid, fst);
				generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
				break;
			} else if(event.getClick() == ClickType.LEFT
					|| event.getClick() == ClickType.RIGHT) // Links || Rechts
			{
				fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_COMMENT);
				getLoggerSettings().replace(uuid, fst);
				generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
				break;
			}
			break;
		//Min ist für LessThan, Between
		case 0:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q : Alles Löschen
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q : Nur diesen Parameter löschen
			{
				fst.setMin(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links : +1
			{
				Double d = addingDoubles(fst.getMin(), 1);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts : +50
			{
				Double d = addingDoubles(fst.getMin(), 50);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : -1
			{
				Double d = addingDoubles(fst.getMin(), -1);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts : -50
			{
				Double d = addingDoubles(fst.getMin(), -50);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 9:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q : Alles Löschen
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q : Nur diesen Parameter löschen
			{
				fst.setMin(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links : +1_000
			{
				Double d = addingDoubles(fst.getMin(), 1_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts : +50_000
			{
				Double d = addingDoubles(fst.getMin(), 50_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : -1_000
			{
				Double d = addingDoubles(fst.getMin(), -1_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts : -50_000
			{
				Double d = addingDoubles(fst.getMin(), -50_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;	
		case 18:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q : Alles Löschen
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q : Nur diesen Parameter löschen
			{
				fst.setMin(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links : +1_000_000
			{
				Double d = addingDoubles(fst.getMin(), 1_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts : +50_000_000
			{
				Double d = addingDoubles(fst.getMin(), 50_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : -1_000_000
			{
				Double d = addingDoubles(fst.getMin(), -1_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts : -5_000_000
			{
				Double d = addingDoubles(fst.getMin(), -50_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;	
		//Max ist für Greatthan, Between
		case 8:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q : Alles Löschen
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q : Nur diesen Parameter löschen
			{
				fst.setMax(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links : +1
			{
				Double d = addingDoubles(fst.getMax(), 1);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts : +50
			{
				Double d = addingDoubles(fst.getMax(), 50);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : -1
			{
				Double d = addingDoubles(fst.getMax(), -1);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts : -50
			{
				Double d = addingDoubles(fst.getMax(), -50);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 17:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q : Alles Löschen
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q : Nur diesen Parameter löschen
			{
				fst.setMax(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links : +50_000
			{
				Double d = addingDoubles(fst.getMax(), 1_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts : +1_000_000
			{
				Double d = addingDoubles(fst.getMax(), 50_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : -50_000
			{
				Double d = addingDoubles(fst.getMax(), -1_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts : -1_000_000
			{
				Double d = addingDoubles(fst.getMax(), -50_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 26:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q : Alles Löschen
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q : Nur diesen Parameter löschen
			{
				fst.setMax(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links : +1_000_000
			{
				Double d = addingDoubles(fst.getMax(), 1_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts : +5_000_000
			{
				Double d = addingDoubles(fst.getMax(), 50_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : -1_000_000
			{
				Double d = addingDoubles(fst.getMax(), -1_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts : -50_000_000
			{
				Double d = addingDoubles(fst.getMax(), -50_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		//Firststand
		case 29:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getTrendfFilter().setFirstStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 1_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 50_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : 
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -1_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -50_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 38:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getTrendfFilter().setFirstStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 1_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts :
			{

				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 50_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : 
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -1_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts :
			{

				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -50_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		//LastStand
		case 33:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getTrendfFilter().setLastStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 1_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 50_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : 
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), -1_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), -50_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 42:
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.DROP) // Q
			{
				fst.getTrendfFilter().setLastStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 1_000_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 50_000_000);;
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_LEFT) // Shift + Links : 
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), -1_000_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.SHIFT_RIGHT) // Shift + Rechts :
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), -50_000_000);;
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 11: //Descending boolean einbauen
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links
			{
				fst.setDescending(false);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts 
			{
				fst.setDescending(true);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 15: //OrderType einstellen.
			if(event.getClick() == ClickType.CONTROL_DROP) // Strg + Q
			{
				resetLoggerSetting(fst);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.LEFT) // Links
			{
				fst.setOrderType(OrderType.AMOUNT);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.getClick() == ClickType.RIGHT) // Rechts
			{
				fst.setOrderType(OrderType.ID);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		default:
			break;
		}
	}
	
	public double addingDoubles(Double base, double adding)
	{
		Double d = base;
		if(d == null)
		{
			d = 0.0;
		}
		d += adding;
		if(d < 0)
		{
			d = 0.0;
		}
		return d;
	}
	
	public void generateAnvilStringEditor(Player player, LoggerSettings fst)
	{
		player.closeInventory();
		player.spigot().sendMessage(ChatApi.clickEvent
				(plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsTextSuggest")
				, Action.SUGGEST_COMMAND, loggerSettingsTextCommandString));
	}
	
	public void anvilStringEditorOutput(Player player, String searchtext)
	{
		LoggerSettings fst = LoggerSettingsHandler.getLoggerSettings().get(player.getUniqueId());
		if(fst == null)
		{
			return;
		}
		AEPUser eco = null;
		if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_COMMENT)
		{
			fst.getActionFilter().setComment(searchtext.replace("'", ""));
		} else if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_FROM)
		{
			eco = AEPUserHandler.getEcoPlayer(searchtext.replace("'", ""));
			if(eco == null)
			{
				fst.getActionFilter().setFrom(searchtext.replace("'", ""));
			} else
			{
				fst.getActionFilter().setFrom(eco.getUUID().toString());
			}
		} else if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_TO)
		{
			eco = AEPUserHandler.getEcoPlayer(searchtext.replace("'", ""));
			if(eco == null)
			{
				fst.getActionFilter().setTo(searchtext.replace("'", ""));
			} else
			{
				fst.getActionFilter().setTo(eco.getUUID().toString());
			}
		} else if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER)
		{
			eco = AEPUserHandler.getEcoPlayer(searchtext.replace("'", ""));
			if(eco == null)
			{
				fst.getActionFilter().setOrderer(searchtext.replace("'", ""));
			} else
			{
				fst.getActionFilter().setOrderer(eco.getUUID().toString());
			}
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsTextOnlyThroughGUI")));
			return;
		}
		getLoggerSettings().put(player.getUniqueId(), fst);
		generateGUI(player, player.getUniqueId(), null, null, null, fst.getPage());
	}
	
	public ItemStack generateItem(int slot, LoggerSettings fst, UUID uuid, boolean withEnd, boolean isPreset)
	{
		ItemStack is = new ItemStack(Material.valueOf(plugin.getYamlHandler().getFilSet().getString(slot+".Material")));
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatApi.tl(plugin.getYamlHandler().getFilSet().getString(slot+".Name")));
		ArrayList<String> lore = (ArrayList<String>) plugin.getYamlHandler().getFilSet().getStringList(slot+".Lore");
		if(isPreset)
		{
			LoggerSettings ls = null;
			int slotid = 0;
			if(slot == 36)
			{
				slotid = 1;
			} else if(slot == 44)
			{
				slotid = 2;
			} else if(slot == 45)
			{
				slotid = 3;
			} else if(slot == 53)
			{
				slotid = 4;
			}
			
			if(plugin.getMysqlHandler().exist(Type.LOGGERSETTINGSPRESET, "`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString()))
			{
				ls = (LoggerSettings) plugin.getMysqlHandler().getData(Type.LOGGERSETTINGSPRESET,
						"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
				ArrayList<String> actualValues = (ArrayList<String>) plugin.getYamlHandler().getFilSet().getStringList("ActualParameter");
				for(int i = 0; i < actualValues.size(); i++)
				{
					String s = actualValues.get(i);
					if(i == 0 || i == 1)
					{
						lore.add(s);
						continue;
					}
					if(s.contains("%ordercolumn%"))
					{
						s = s.replace("%ordercolumn%", ls.getOrderType().toString());
						lore.add(s);
						continue;
					}
					if(s.contains("%descending%"))
					{
						s = s.replace("%descending%", valueOf(ls.isDescending()));
						lore.add(s);
						continue;
					}
					if(s.contains("%uuid%") && ls.getUuid() != null)
					{
						AEPUser eco = AEPUserHandler.getEcoPlayer(ls.getUuid());
						if(eco != null)
						{
							s = s.replace("%uuid%", eco.getName());
						} else
						{
							s = s.replace("%uuid%", ls.getUuid().toString());
						}
						lore.add(s);
						continue;
					}
					if(s.contains("%number%") && ls.getBankNumber() != null)
					{
						s = s.replace("%number%", ls.getBankNumber());
						lore.add(s);
						continue;
					}
					if(fst.getActionFilter() != null)
					{
						if((s.contains("%from%") || s.contains("%to%"))
								&& (ls.getActionFilter().getFrom() != null || ls.getActionFilter().getTo() != null))
						{
							AEPUser eco = null;
							if(ls.getActionFilter().getFrom() != null)
							{
								try
								{
									eco = AEPUserHandler.getEcoPlayer(UUID.fromString(ls.getActionFilter().getFrom()));
								} catch(IllegalArgumentException e) {}
							}
							if(eco != null)
							{
								s = s.replace("%from%", eco.getName());
							} else if(ls.getActionFilter().getFrom() != null)
							{
								s = s.replace("%from%", ls.getActionFilter().getFrom());
							} else
							{
								s = s.replace("%from%", "/");
							}
							AEPUser ecoII = null;
							if(ls.getActionFilter().getTo() != null)
							{
								try
								{
									ecoII = AEPUserHandler.getEcoPlayer(UUID.fromString(ls.getActionFilter().getTo()));
								} catch(IllegalArgumentException e) {}
							}
							if(ecoII != null)
							{
								s = s.replace("%to%", ecoII.getName());
							} else if(ls.getActionFilter().getTo() != null)
							{
								s = s.replace("%to%", ls.getActionFilter().getTo());
							} else
							{
								s = s.replace("%to%", "/");
							}
							lore.add(s);
							continue;
						}
						if(s.contains("%orderer%") && ls.getActionFilter().getOrderer() != null)
						{
							AEPUser eco = AEPUserHandler.getEcoPlayer(UUID.fromString(ls.getActionFilter().getOrderer()));
							if(eco != null)
							{
								s = s.replace("%orderer%", eco.getName());
							} else if(ls.getActionFilter().getOrderer() != null)
							{
								s = s.replace("%orderer%", ls.getActionFilter().getOrderer());
							} else
							{
								s = s.replace("%orderer%", "/");
							}
							s = s.replace("%orderer%", ls.getActionFilter().getOrderer());
							lore.add(s);
							continue;
						}
						if(s.contains("%comment%") && ls.getActionFilter().getComment() != null)
						{
							s = s.replace("%comment%", ChatApi.tl(ls.getActionFilter().getComment()));
							lore.add(s);
							continue;
						}
					}
					if((s.contains("%min%") || s.contains("%max%"))
							&& (ls.getMin() != null || ls.getMax() != null))
					{
						if(ls.getMin() != null)
						{
							s = s.replace("%min%", AdvancedEconomyPlus.getVault().format(ls.getMin()));
						} else
						{
							s = s.replace("%min%", "/");
						}
						if(ls.getMax() != null)
						{
							s = s.replace("%max%", AdvancedEconomyPlus.getVault().format(ls.getMax()));
						} else
						{
							s = s.replace("%max%", "/");
						}
						lore.add(s);
						continue;
					}
					if(s.contains("%firststand%") && ls.getTrendfFilter().getFirstStand() != null)
					{
						s = s.replace("%firststand%", AdvancedEconomyPlus.getVault().format(ls.getTrendfFilter().getFirstStand()));
						lore.add(s);
						continue;
					}
					if(s.contains("%laststand%") && ls.getTrendfFilter().getLastStand() != null)
					{
						s = s.replace("%laststand%", AdvancedEconomyPlus.getVault().format(ls.getTrendfFilter().getLastStand()));
						lore.add(s);
						continue;
					}
				}
			} else
			{
				ArrayList<String> preset = (ArrayList<String>) plugin.getYamlHandler().getFilSet().getStringList("Preset");
				lore.addAll(preset);
			}
		}
		if(withEnd)
		{
			ArrayList<String> actualValues = (ArrayList<String>) plugin.getYamlHandler().getFilSet().getStringList("ActualParameter");
			for(int i = 0; i < actualValues.size(); i++)
			{
				String s = actualValues.get(i);
				if(i == 0 || i == 1)
				{
					lore.add(s);
					continue;
				}
				if(s.contains("%ordercolumn%"))
				{
					s = s.replace("%ordercolumn%", fst.getOrderType().toString());
					lore.add(s);
					continue;
				}
				if(s.contains("%descending%"))
				{
					s = s.replace("%descending%", valueOf(fst.isDescending()));
					lore.add(s);
					continue;
				}
				if(s.contains("%uuid%") && fst.getUuid() != null)
				{
					AEPUser eco = AEPUserHandler.getEcoPlayer(fst.getUuid());
					if(eco != null)
					{
						s = s.replace("%uuid%", eco.getName());
					} else
					{
						s = s.replace("%uuid%", fst.getUuid().toString());
					}
					lore.add(s);
					continue;
				}
				if(s.contains("%number%") && fst.getBankNumber() != null)
				{
					s = s.replace("%number%", fst.getBankNumber());
					lore.add(s);
					continue;
				}
				if(fst.getActionFilter() != null)
				{
					if((s.contains("%from%") || s.contains("%to%"))
							&& (fst.getActionFilter().getFrom() != null || fst.getActionFilter().getTo() != null))
					{
						AEPUser eco = null;
						if(fst.getActionFilter().getFrom() != null)
						{
							try
							{
								eco = AEPUserHandler.getEcoPlayer(UUID.fromString(fst.getActionFilter().getFrom()));
							} catch(IllegalArgumentException e) {}
						}
						if(eco != null)
						{
							s = s.replace("%from%", eco.getName());
						} else if(fst.getActionFilter().getFrom() != null)
						{
							s = s.replace("%from%", fst.getActionFilter().getFrom());
						} else
						{
							s = s.replace("%from%", "/");
						}
						AEPUser ecoII = null;
						if(fst.getActionFilter().getTo() != null)
						{
							try
							{
								ecoII = AEPUserHandler.getEcoPlayer(UUID.fromString(fst.getActionFilter().getTo()));
							} catch(IllegalArgumentException e) {}
						}
						if(ecoII != null)
						{
							s = s.replace("%to%", ecoII.getName());
						} else if(fst.getActionFilter().getTo() != null)
						{
							s = s.replace("%to%", fst.getActionFilter().getTo());
						} else
						{
							s = s.replace("%to%", "/");
						}
						lore.add(s);
						continue;
					}
					if(s.contains("%orderer%") && fst.getActionFilter().getOrderer() != null)
					{
						AEPUser eco = null;
						try
						{
							eco = AEPUserHandler.getEcoPlayer(UUID.fromString(fst.getActionFilter().getOrderer()));
						} catch(IllegalArgumentException e) {}
						if(eco != null)
						{
							s = s.replace("%orderer%", eco.getName());
						} else if(fst.getActionFilter().getOrderer() != null)
						{
							s = s.replace("%orderer%", fst.getActionFilter().getOrderer());
						} else
						{
							s = s.replace("%orderer%", "/");
						}
						s = s.replace("%orderer%", fst.getActionFilter().getOrderer());
						lore.add(s);
						continue;
					}
					if(s.contains("%comment%") && fst.getActionFilter().getComment() != null)
					{
						s = s.replace("%comment%", ChatApi.tl(fst.getActionFilter().getComment()));
						lore.add(s);
						continue;
					}
				}
				
				if((s.contains("%min%") || s.contains("%max%"))
						&& (fst.getMin() != null || fst.getMax() != null))
				{
					if(fst.getMin() != null)
					{
						s = s.replace("%min%", AdvancedEconomyPlus.getVault().format(fst.getMin()));
					} else
					{
						s = s.replace("%min%", "/");
					}
					if(fst.getMax() != null)
					{
						s = s.replace("%max%", AdvancedEconomyPlus.getVault().format(fst.getMax()));
					} else
					{
						s = s.replace("%max%", "/");
					}
					lore.add(s);
					continue;
				}
				if(s.contains("%firststand%") && fst.getTrendfFilter().getFirstStand() != null)
				{
					s = s.replace("%firststand%", AdvancedEconomyPlus.getVault().format(fst.getTrendfFilter().getFirstStand()));
					lore.add(s);
					continue;
				}
				if(s.contains("%laststand%") && fst.getTrendfFilter().getLastStand() != null)
				{
					s = s.replace("%laststand%", AdvancedEconomyPlus.getVault().format(fst.getTrendfFilter().getLastStand()));
					lore.add(s);
					continue;
				}
			}
		}
		
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	public void forwardingToOutput(Player player, LoggerSettings fst, boolean isAction, Methode methode, int page) throws IOException
	{
		if(fst.getActionFilter().getFrom() != null
				&& fst.getActionFilter().getTo() != null
				&& (fst.getActionFilter().getFrom().equals(fst.getActionFilter().getTo())
						|| (!fst.getActionFilter().getFrom().equals(player.getName())
								&& !fst.getActionFilter().getTo().equals(player.getName())))
				&& !player.hasPermission(Utility.PERM_BYPASS_LOGOTHER))
		{
			fst.setInventoryHandlerType(InventoryHandlerType.NONE);
			getLoggerSettings().replace(player.getUniqueId(), fst);
			player.closeInventory();
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPermission")));
			return;
		}
		player.closeInventory();
		ArrayList<Object> whereObjects = new ArrayList<>();
		String query = "";
		String order = "`id`";
		if(isAction)
		{
			if(fst.getOrderType() != OrderType.ID)
			{
				order = "`amount`";
			}
			if(fst.getActionFilter().getFrom() != null
					&& fst.getActionFilter().getTo() != null
					&& fst.getActionFilter().getFrom().equals(fst.getActionFilter().getTo()))
			{
				query += "(`from_uuidornumber` = ? OR ";
				whereObjects.add(fst.getActionFilter().getFrom());
				query += "`to_uuidornumber` = ?) AND ";
				whereObjects.add(fst.getActionFilter().getTo());
			} else if(fst.getActionFilter().getFrom() != null
					|| fst.getActionFilter().getTo() != null)
			{
				if(fst.getActionFilter().getFrom() != null)
				{
					query += "(`from_uuidornumber` = ? AND ";
					whereObjects.add(fst.getActionFilter().getFrom());
				} else
				{
					query += "(`from_uuidornumber` = ? AND ";
					if(fst.getUuid() != null)
					{
						whereObjects.add(fst.getUuid().toString());
					} else
					{
						whereObjects.add(fst.getBankNumber());
					}
				}
				if(fst.getActionFilter().getTo() != null)
				{
					query += "`to_uuidornumber` = ?) AND ";
					whereObjects.add(fst.getActionFilter().getTo());
				} else
				{
					query += "`to_uuidornumber` = ?) AND ";
					if(fst.getUuid() != null)
					{
						whereObjects.add(fst.getUuid().toString());
					} else
					{
						whereObjects.add(fst.getBankNumber());
					}
				}
			} else
			{
				query += "(`from_uuidornumber` = ? OR ";
				if(fst.getUuid() != null)
				{
					whereObjects.add(fst.getUuid().toString());
				} else
				{
					whereObjects.add(fst.getBankNumber());
				}
				query += "`to_uuidornumber` = ?) AND ";
				if(fst.getUuid() != null)
				{
					whereObjects.add(fst.getUuid().toString());
				} else
				{
					whereObjects.add(fst.getBankNumber());
				}
			}
			if(fst.getActionFilter().getOrderer() != null)
			{
				query += "`orderer_uuid` = ? AND ";
				whereObjects.add(fst.getActionFilter().getOrderer());
			}
			if(fst.getActionFilter().getComment() != null)
			{
				query += "(`comment` LIKE ?) AND ";
				whereObjects.add("%"+fst.getActionFilter().getComment()+"%");
			}
			if(fst.getMin() != null)
			{
				query += "`amount` > ? AND ";
				whereObjects.add(fst.getMin());
			}
			if(fst.getMax() != null)
			{
				query += "`amount` < ? AND ";
				whereObjects.add(fst.getMax());
			}
		} else
		{
			if(fst.getOrderType() != OrderType.ID)
			{
				order = "`relative_amount_change`";
			}
			if(fst.getUuid() != null)
			{
				query += "`uuidornumber` = ? AND ";
				whereObjects.add(fst.getUuid().toString());
			} else if(fst.getBankNumber() != null)
			{
				query += "`uuidornumber` = ? AND ";
				whereObjects.add(fst.getBankNumber());
			}
			if(fst.getTrendfFilter().getFirstStand() != null)
			{
				query += "`firstvalue` > ? AND ";
				whereObjects.add(fst.getTrendfFilter().getFirstStand());
			}
			if(fst.getTrendfFilter().getLastStand() != null)
			{
				query += "`lastvalue` < ? AND ";
				whereObjects.add(fst.getTrendfFilter().getLastStand());
			}
			if(fst.getMin() != null)
			{
				query += "`relative_amount_change` > ? AND ";
				whereObjects.add(fst.getMin());
			}
			if(fst.getMax() != null)
			{
				query += "`relative_amount_change` < ? AND ";
				whereObjects.add(fst.getMax());
			}
		}
		query = query.substring(0, query.length()-5);
		AEPUser eco = AEPUserHandler.getEcoPlayer(player.getUniqueId());
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		fst.setInventoryHandlerType(InventoryHandlerType.NONE);
		getLoggerSettings().replace(player.getUniqueId(), fst);
		int start = 0;
		int end = 0;
		player.closeInventory();
		Object[] whereObject = whereObjects.toArray(new Object[whereObjects.size()]);
		if(Methode.BARCHART == methode)
		{
			start = page;
			end = page+1;
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime ending = LocalDateTime.of(
					now.getYear()-(start),
					now.getMonth(),
					now.getDayOfMonth(),
					now.getHour(),
					now.getMinute(),
					now.getSecond());
			LocalDateTime starting = LocalDateTime.of(
					now.getYear()-(end),
					now.getMonth(),
					1, 1, 1, 1);
			ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getAllListAtIIIDateTimeModified(plugin, order, fst.isDescending(), starting, ending,
					query, whereObject));
			int last = plugin.getMysqlHandler().countWhereID(Type.ACTION, query, whereObject);
			LogHandler.sendActionBarChart(plugin, player, eco, list, page, end, player.getName(), last, loggerSettingsCommandString);
			return;
		} else if(Methode.DIAGRAM == methode)
		{
			if(isAction)
			{
				start = page*10;
				end = 9;
				ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
						plugin.getMysqlHandler().getList(Type.ACTION, order, fst.isDescending(), start, end,
								query, whereObject));
				int last = plugin.getMysqlHandler().countWhereID(Type.ACTION, query, whereObject);
				LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, player.getName(), last, loggerSettingsCommandString);
				return;
			} else
			{
				start = page*10;
				end = 9;
				ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
						plugin.getMysqlHandler().getList(Type.TREND, order, fst.isDescending(), start, end,
								query, whereObject));
				int last = plugin.getMysqlHandler().countWhereID(Type.TREND, query, whereObject);
				LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, player.getName(), last, loggerSettingsCommandString);
				return;
			}
		} else if(Methode.GRAFIC == methode)
		{
			if(isAction)
			{
				start = page*26;
				end = 25;
				ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
						plugin.getMysqlHandler().getList(Type.ACTION, order, fst.isDescending(), start, end,
								query, whereObject));
				int last = plugin.getMysqlHandler().countWhereID(Type.ACTION, query, whereObject);
				LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, player.getName(), last, loggerSettingsCommandString);
				return;
			} else
			{
				start = page*26;
				end = 26;
				ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
						plugin.getMysqlHandler().getList(Type.TREND, order, fst.isDescending(), start, end,
								query, whereObject));
				int last = plugin.getMysqlHandler().countWhereID(Type.TREND, query, whereObject);
				LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, player.getName(), last, loggerSettingsCommandString);
				return;
			}
		} else if(Methode.LOG == methode)
		{
			if(isAction)
			{
				start = page*10;
				end = 9;
				ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
						plugin.getMysqlHandler().getList(Type.ACTION, order, fst.isDescending(), start, end,
								query, whereObject));
				int last = plugin.getMysqlHandler().countWhereID(Type.ACTION, query, whereObject);
				LogHandler.sendActionLogs(plugin, player, eco, fst, list, page, end, player.getName(), last, loggerSettingsCommandString);
				return;
			} else
			{
				start = page*10;
				end = 9;
				ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
						plugin.getMysqlHandler().getList(Type.TREND, order, fst.isDescending(), start, end,
								query, whereObject));
				int last = plugin.getMysqlHandler().countWhereID(Type.TREND, query, whereObject);
				LogHandler.sendTrendLogs(plugin, player, eco, fst, list, page, end, player.getName(), last, loggerSettingsCommandString);
				return;
			}
		} else if(Methode.JSON == methode)
		{
			if(isAction)
			{
				String json = plugin.getMysqlHandler().getJSONOutputIII(plugin, player.getName(), order, fst.isDescending(), query, whereObject);
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsJSONOutput"),
						ClickEvent.Action.COPY_TO_CLIPBOARD,
						json));
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsJSONWebsiteText"),
						ClickEvent.Action.OPEN_URL,
						plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsJSONWebsite")));
			} else
			{
				String json = plugin.getMysqlHandler().getJSONOutputIV(plugin, player.getName(), order, fst.isDescending(), query, whereObject);
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsJSONOutput"),
						ClickEvent.Action.COPY_TO_CLIPBOARD,
						json));
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsJSONWebsiteText"),
						ClickEvent.Action.OPEN_URL,
						plugin.getYamlHandler().getL().getString("CmdMoney.Log.LoggerSettingsJSONWebsite")));
			}
			return;
		}
	}
	
	public String valueOf(boolean boo)
	{
		if(plugin.getYamlManager().getLanguageType() != ISO639_2B.GER)
		{
			return String.valueOf(boo);
		} else
		{
			if(boo)
			{
				return "Ja";
			} else
			{
				return "Nein";
			}
		}
	}
}