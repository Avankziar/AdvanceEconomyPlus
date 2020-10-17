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
import main.java.me.avankziar.aep.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.aep.spigot.handler.LogMethodeHandler.Methode;
import main.java.me.avankziar.aep.spigot.object.ActionLogger;
import main.java.me.avankziar.aep.spigot.object.EcoPlayer;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.InventoryHandlerType;
import main.java.me.avankziar.aep.spigot.object.LoggerSettings.OrderType;

public class LoggerSettingsHandler
{
	public AdvancedEconomyPlus plugin;
	private static LinkedHashMap<UUID, LoggerSettings> filterSettings = new LinkedHashMap<>();
	public static String actionBarChartCmdString = "";
	public static String actionDiagramCmdString = "";
	public static String trendDiagramCmdString = "";
	public static String actionGraficCmdString = "";
	public static String trendGraficCmdString = "";
	public static String actionLogCmdString = "";
	public static String trendLogCmdString = "";
	
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
			inventory = Bukkit.createInventory(null, 9*6, ChatApi.tl("&cFilter &aSettings"));
		}
		LoggerSettings fst = getLoggerSettings().get(uuid);
		if(fst == null)
		{
			fst = new LoggerSettings(UseUUID, UseBankNumber, page);
			fst.setInventoryHandlerType(InventoryHandlerType.NORMAL);
		}
		for(int i = 0; i < inventory.getSize(); i++)
		{
			switch(i)
			{
			//Fall-Through
			case 40: //Barchart Befehl ausführen
			case 48: //Diagram Befehl ausführen. Linksklick: Action | Rechtsklick: Trend
			case 49: //Log Befehl ausführen.
			case 50: //Grafik Befehl ausführen.
			
			//Min ist für LessThan, Between
			case 0: //Min. Linksklick: +1 | Rechtsklick: +10
			case 1: //Min. Linksklick: -1 | Rechtsklick: -10
			case 9: //Min. Linksklick: +100 | Rechtsklick: +1_000
			case 10: //Min. Linksklick: -100 | Rechtsklick: -1_000
			case 18: //Min. Linksklick: +10_000 | Rechtsklick: +100_000
			case 19: //Min. Linksklick: -10_000 | Rechtsklick: -100_000
			
			//Max ist für Greatthan, Between
			case 7: //Max. Linksklick: +1 | Rechtsklick: +10
			case 8: //Max. Linksklick: -1 | Rechtsklick: -10
			case 16: //Max. Linksklick: +100 | Rechtsklick: +1_000
			case 17: //Max. Linksklick: -100 | Rechtsklick: -1_000
			case 25: //Max. Linksklick: +10_000 | Rechtsklick: +100_000
			case 26: //Max. Linksklick: -10_000 | Rechtsklick: -100_000
				
			case 29: //FirstStand LK: +1_000 | RK: +50_000
			case 30: //FirstStand LK: -1_000 | RK: -50_000
			case 38: //FirstStand LK: +1_000_000 | RK: +50_000_000
			case 39: //FirstStand LK: -1_000_000 | RK: -50_000_000
				
			case 32: //LastStand LK: +1_000 | RK: +50_000
			case 33: //LastStand LK: -1_000 | RK: -50_000
			case 41: //LastStand LK: +1_000_000 | RK: +50_000_000
			case 42: //LastStand LK: -1_000_000 | RK: -50_000_000
				
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
				ItemStack preset = generateItem(i, fst, uuid, true, true);
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
		ClickType clickType = event.getClick();
		if(clickType != ClickType.RIGHT 
				&& clickType != ClickType.LEFT
				&& clickType != ClickType.SHIFT_RIGHT
				&& clickType != ClickType.SHIFT_LEFT)
		{
			event.setResult(Result.DENY);
			event.setCancelled(true);
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
		switch(event.getSlot())
		{
		default:
			break;
			
		case 40: //Barchart Befehl ausführen
			forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.BARCHART, 0);
			break;
		case 48: //Diagram Befehl ausführen. Linksklick: Action | Rechtsklick: Trend
			if(event.isLeftClick())
			{
				forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.DIAGRAM, 0);
			} else if(event.isRightClick())
			{
				forwardingToOutput((Player) event.getWhoClicked(), fst, false, Methode.DIAGRAM, 0);
				fst.setAction(false);
				getLoggerSettings().replace(uuid, fst);
			}
			break;
		case 50: //Grafik Befehl ausführen.
			if(event.isLeftClick())
			{
				forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.GRAFIC, 0);
			} else if(event.isRightClick())
			{
				forwardingToOutput((Player) event.getWhoClicked(), fst, false, Methode.GRAFIC, 0);
				fst.setAction(false);
				getLoggerSettings().replace(uuid, fst);
			}
			break;
		case 49: //Log Befehl ausführen.
			if(event.isLeftClick())
			{
				forwardingToOutput((Player) event.getWhoClicked(), fst, true, Methode.LOG, 0);
			} else if(event.isRightClick())
			{
				forwardingToOutput((Player) event.getWhoClicked(), fst, false, Methode.LOG, 0);
				fst.setAction(false);
				getLoggerSettings().replace(uuid, fst);
			}
			break;
		
		case 36: //PreSet 1 TODO
			if(event.isShiftClick())
			{
				
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 44: //PreSet 2
			break;
		case 45: //PreSet 3
			break;
		case 53: //PreSet 4
			break;
			
		case 3: //From Set Befehl ausführen
			fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_FROM);
			getLoggerSettings().replace(uuid, fst);
			generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
			break;
		case 4: //Orderer Befehl ausführen. Wird auf für den Trend genutzt.
			fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_ORDERER);
			getLoggerSettings().replace(uuid, fst);
			generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
			break;
		case 5: //to Set Befehl ausführen
			fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_TO);
			getLoggerSettings().replace(uuid, fst);
			generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
			break;
		case 13: //Comment Set
			fst.setInventoryHandlerType(InventoryHandlerType.ANVILEDITOR_COMMENT);
			getLoggerSettings().replace(uuid, fst);
			generateAnvilStringEditor((Player)event.getWhoClicked(), fst);
			break;
			
		//Min ist für LessThan, Between
		case 0: //Min. Linksklick: +1 | Rechtsklick: +50
			if(event.isShiftClick())
			{
				fst.setMin(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMin(), 1);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMin(), 50);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 1: //Min. Linksklick: -1 | Rechtsklick: -50
			if(event.isShiftClick())
			{
				fst.setMin(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMin(), -1);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMin(), -50);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 9: //Min. Linksklick: +1_000 | Rechtsklick: +50_000
			if(event.isShiftClick())
			{
				fst.setMin(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMin(), 1_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMin(), 50_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 10: //Min. Linksklick: -1_000 | Rechtsklick: -50_000
			if(event.isShiftClick())
			{
				fst.setMin(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMin(), -1_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMin(), -50_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 18: //Min. Linksklick: +1_000_000 | Rechtsklick: +50_000_000
			if(event.isShiftClick())
			{
				fst.setMin(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMin(), 1_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMin(), 50_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 19: //Min. Linksklick: -1_000_000 | Rechtsklick: -50_000_000
			if(event.isShiftClick())
			{
				fst.setMin(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMin(), -1_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMin(), -50_000_000);
				fst.setMin(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		
		//Max ist für Greatthan, Between
		case 7: //Max. Linksklick: +1 | Rechtsklick: +50
			if(event.isShiftClick())
			{
				fst.setMax(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMax(), 1);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMax(), 50);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 8: //Max. Linksklick: -1 | Rechtsklick: -50
			if(event.isShiftClick())
			{
				fst.setMax(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMax(), -1);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMax(), -50);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 16: //Max. Linksklick: +1_000 | Rechtsklick: +50_000
			if(event.isShiftClick())
			{
				fst.setMax(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMax(), 1_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMax(), 50_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 17: //Max. Linksklick: -1_000 | Rechtsklick: -50_000
			if(event.isShiftClick())
			{
				fst.setMax(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMax(), -1_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMax(), -50_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 25: //Max. Linksklick: +1_000_000 | Rechtsklick: +50_000_000
			if(event.isShiftClick())
			{
				fst.setMax(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMax(), 1_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMax(), 50_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 26: //Max. Linksklick: -1_000_000 | Rechtsklick: -50_000_000
			if(event.isShiftClick())
			{
				fst.setMax(null);;
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getMax(), -1_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getMax(), -50_000_000);
				fst.setMax(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
			
		case 29: //FirstStand LK: +1_000 | RK: +50_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setFirstStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 1_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 50_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 30: //FirstStand LK: -1_000 | RK: -50_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setFirstStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -1_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -50_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 38: //FirstStand LK: +1_000_000 | RK: +50_000_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setFirstStand(null);
				getLoggerSettings().replace(uuid, fst);
			}  else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 1_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), 50_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 39: //FirstStand LK: -1_000_000 | RK: -50_000_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setFirstStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -1_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getFirstStand(), -50_000_000);
				fst.getTrendfFilter().setFirstStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 32: //LastStand LK: +1_000 | RK: +50_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setLastStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 1_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 50_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 33: //LastStand LK: -1_000 | RK: -50_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setLastStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), -1_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d =addingDoubles(fst.getTrendfFilter().getLastStand(), -50_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 41: //LastStand LK: +1_000_000 | RK: +50_000_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setLastStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 1_000_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), 50_000_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 42: //LastStand LK: -1_000_000 | RK: -50_000_000
			if(event.isShiftClick())
			{
				fst.getTrendfFilter().setLastStand(null);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isLeftClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), -1_000_000);
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				Double d = addingDoubles(fst.getTrendfFilter().getLastStand(), -50_000_000);;
				fst.getTrendfFilter().setLastStand(d);
				getLoggerSettings().replace(uuid, fst);				
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 11: //Descending boolean einbauen
			if(event.isLeftClick())
			{
				fst.setDescending(false);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				fst.setDescending(true);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
			break;
		case 15: //OrderType einstellen.
			if(event.isLeftClick())
			{
				fst.setOrderType(OrderType.AMOUNT);
				getLoggerSettings().replace(uuid, fst);
			} else if(event.isRightClick())
			{
				fst.setOrderType(OrderType.ID);
				getLoggerSettings().replace(uuid, fst);
			}
			generateGUI((Player) event.getWhoClicked(), uuid, fst.getUuid(), fst.getBankNumber(), inventory, fst.getPage());
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
		Inventory anvil = Bukkit.createInventory(null, InventoryType.ANVIL, "§eFilterSettings AnvilEditor");
		ItemStack is = generateItem(100, fst, player.getUniqueId(), false, false);
		anvil.setItem(0, is);
		player.openInventory(anvil);
	}
	
	public void anvilStringEditorOutput(InventoryClickEvent event)
	{
		ItemStack edited = event.getCurrentItem().clone();
		if(edited == null)
		{
			return;
		}
		if(!edited.hasItemMeta() || !edited.getItemMeta().hasDisplayName())
		{
			event.setCurrentItem(null);
			return;
		}
		String s = edited.getItemMeta().getDisplayName();
		LoggerSettings fst = getLoggerSettings().get(event.getWhoClicked().getUniqueId());
		if(fst == null)
		{
			event.setCurrentItem(null);
			return;
		}
		if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_COMMENT)
		{
			fst.getActionFilter().setComment(s);
		} else if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_FROM)
		{
			fst.getActionFilter().setFrom(s);
		} else if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_TO)
		{
			fst.getActionFilter().setTo(s);
		} else if(fst.getInventoryHandlerType() == InventoryHandlerType.ANVILEDITOR_ORDERER)
		{
			fst.getActionFilter().setOrderer(s);
		} else
		{
			event.setResult(Result.DENY);
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}
		fst.setInventoryHandlerType(InventoryHandlerType.NORMAL);
		generateGUI((Player) event.getWhoClicked(), event.getWhoClicked().getUniqueId(), null, null, null, fst.getPage());
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
			ls = (LoggerSettings) plugin.getMysqlHandler().getData(Type.LOGGERSETTINGSPRESET,
					"`slotid` = ? AND `player_uuid` = ?", slotid, uuid.toString());
			if(ls != null)
			{
				ArrayList<String> actualValues = (ArrayList<String>) plugin.getYamlHandler().getFilSet().getStringList("ActualParameter");
				for(int i = 0; i < actualValues.size(); i++)
				{
					String s = actualValues.get(i);
					if(i == 0)
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
						s = s.replace("%descending%", String.valueOf(fst.isDescending()));
						lore.add(s);
						continue;
					}
					if(s.contains("%uuid%") && fst.getUuid() != null)
					{
						EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(fst.getUuid());
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
					if((s.contains("%from%") || s.contains("%to%"))
							&& (fst.getActionFilter().getFrom() != null || fst.getActionFilter().getTo() != null))
					{
						EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(fst.getActionFilter().getFrom());
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
						EcoPlayer ecoII = EcoPlayerHandler.getEcoPlayer(fst.getActionFilter().getTo());
						if(ecoII != null)
						{
							s = s.replace("%to%", ecoII.getName());
						} else if(fst.getActionFilter().getTo() != null)
						{
							s = s.replace("%to%", fst.getActionFilter().getFrom());
						} else
						{
							s = s.replace("%to%", "/");
						}
						lore.add(s);
						continue;
					}
					if(s.contains("%orderer%") && fst.getActionFilter().getOrderer() != null)
					{
						s = s.replace("%orderer%", fst.getActionFilter().getOrderer());
						lore.add(s);
						continue;
					}
					if(s.contains("%comment%") && fst.getActionFilter().getComment() != null)
					{
						s = s.replace("%comment%", fst.getActionFilter().getComment());
						lore.add(s);
						continue;
					}
					if((s.contains("%min%") || s.contains("%max%"))
							&& (fst.getMin() != null || fst.getMax() != null))
					{
						if(fst.getMin() != null)
						{
							s = s.replace("%min%", AdvancedEconomyPlus.getVaultApi().format(fst.getMin()));
						} else
						{
							s = s.replace("%min%", "/");
						}
						if(fst.getMax() != null)
						{
							s = s.replace("%max%", AdvancedEconomyPlus.getVaultApi().format(fst.getMax()));
						} else
						{
							s = s.replace("%max%", "/");
						}
						lore.add(s);
						continue;
					}
					if(s.contains("%firststand%") && fst.getTrendfFilter().getFirstStand() != null)
					{
						s = s.replace("%firststand%", AdvancedEconomyPlus.getVaultApi().format(fst.getTrendfFilter().getFirstStand()));
						lore.add(s);
						continue;
					}
					if(s.contains("%laststand%") && fst.getTrendfFilter().getLastStand() != null)
					{
						s = s.replace("%laststand%", AdvancedEconomyPlus.getVaultApi().format(fst.getTrendfFilter().getLastStand()));
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
				if(i == 0)
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
					s = s.replace("%descending%", String.valueOf(fst.isDescending()));
					lore.add(s);
					continue;
				}
				if(s.contains("%uuid%") && fst.getUuid() != null)
				{
					EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(fst.getUuid());
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
				if((s.contains("%from%") || s.contains("%to%"))
						&& (fst.getActionFilter().getFrom() != null || fst.getActionFilter().getTo() != null))
				{
					EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(fst.getActionFilter().getFrom());
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
					EcoPlayer ecoII = EcoPlayerHandler.getEcoPlayer(fst.getActionFilter().getTo());
					if(ecoII != null)
					{
						s = s.replace("%to%", ecoII.getName());
					} else if(fst.getActionFilter().getTo() != null)
					{
						s = s.replace("%to%", fst.getActionFilter().getFrom());
					} else
					{
						s = s.replace("%to%", "/");
					}
					lore.add(s);
					continue;
				}
				if(s.contains("%orderer%") && fst.getActionFilter().getOrderer() != null)
				{
					s = s.replace("%orderer%", fst.getActionFilter().getOrderer());
					lore.add(s);
					continue;
				}
				if(s.contains("%comment%") && fst.getActionFilter().getComment() != null)
				{
					s = s.replace("%comment%", fst.getActionFilter().getComment());
					lore.add(s);
					continue;
				}
				if((s.contains("%min%") || s.contains("%max%"))
						&& (fst.getMin() != null || fst.getMax() != null))
				{
					if(fst.getMin() != null)
					{
						s = s.replace("%min%", AdvancedEconomyPlus.getVaultApi().format(fst.getMin()));
					} else
					{
						s = s.replace("%min%", "/");
					}
					if(fst.getMax() != null)
					{
						s = s.replace("%max%", AdvancedEconomyPlus.getVaultApi().format(fst.getMax()));
					} else
					{
						s = s.replace("%max%", "/");
					}
					lore.add(s);
					continue;
				}
				if(s.contains("%firststand%") && fst.getTrendfFilter().getFirstStand() != null)
				{
					s = s.replace("%firststand%", AdvancedEconomyPlus.getVaultApi().format(fst.getTrendfFilter().getFirstStand()));
					lore.add(s);
					continue;
				}
				if(s.contains("%laststand%") && fst.getTrendfFilter().getLastStand() != null)
				{
					s = s.replace("%laststand%", AdvancedEconomyPlus.getVaultApi().format(fst.getTrendfFilter().getLastStand()));
					lore.add(s);
					continue;
				}
			}
		}
		
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	public void forwardingToOutput(Player player, LoggerSettings fst, boolean isAction, LogMethodeHandler.Methode methode, int page) throws IOException
	{
		if(fst.getActionFilter().getFrom() != null
				&& fst.getActionFilter().getTo() != null
				&& fst.getActionFilter().getFrom().equals(fst.getActionFilter().getTo())
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
			if(fst.getActionFilter().getFrom() != null)
			{
				query += "(`from_uuidornumber` = ? OR ";
				whereObjects.add(fst.getActionFilter().getFrom());
			} else
			{
				query += "`from_uuidornumber` = ? OR ";
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
			if(fst.getActionFilter().getOrderer() != null)
			{
				query += "`orderer_uuid` = ? AND ";
				whereObjects.add(fst.getActionFilter().getOrderer());
			}
			if(fst.getActionFilter().getComment() != null)
			{
				query += "(`comment` LIKE ?) AND ";
				whereObjects.add(fst.getActionFilter().getComment());
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
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayer(player.getUniqueId());
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
		if(Methode.BARCHART == methode)
		{
			start = page*1;
			end = page*1+1;
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime ending = LocalDateTime.of(
					now.getYear()-(2*start),
					now.getMonth(),
					now.getDayOfMonth(),
					now.getHour(),
					now.getMinute(),
					now.getSecond());
			LocalDateTime starting = LocalDateTime.of(
					now.getYear()-(2*end),
					now.getMonth(),
					1, 0, 0, 1);
			ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
					plugin.getMysqlHandler().getAllListAtIIIDateTimeModified(plugin, order, fst.isDescending(), starting, ending,
					query,whereObjects));
			int last = plugin.getMysqlHandler().countWhereID(Type.ACTION, query,whereObjects);
			LogHandler.sendActionBarChart(plugin, player, eco, list, page, end, player.getName(), last, actionBarChartCmdString);
			return;
		} else if(Methode.DIAGRAM == methode)
		{
			if(isAction)
			{
				start = page*10;
				end = 9;
				ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
						plugin.getMysqlHandler().getList(Type.ACTION, order, fst.isDescending(), start, end,
								query, whereObjects));
				int last = plugin.getMysqlHandler().countWhereID(Type.ACTION,query, whereObjects);
				LogHandler.sendActionDiagram(plugin, player, eco, list, page, end, player.getName(), last, actionDiagramCmdString);
				return;
			} else
			{
				start = page*10;
				end = 9;
				ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
						plugin.getMysqlHandler().getList(Type.TREND, order, fst.isDescending(), start, end,
								query, whereObjects));
				int last = plugin.getMysqlHandler().countWhereID(Type.TREND, query, whereObjects);
				LogHandler.sendTrendDiagram(plugin, player, eco, list, page, end, player.getName(), last, trendLogCmdString);
				return;
			}
		} else if(Methode.GRAFIC == methode)
		{
			if(isAction)
			{
				start = page*25;
				end = 24;
				ArrayList<ActionLogger> list = ConvertHandler.convertListIII(
						plugin.getMysqlHandler().getList(Type.ACTION, order, fst.isDescending(), start, end,
								query, whereObjects));
				int last = plugin.getMysqlHandler().countWhereID(Type.ACTION, query, whereObjects);
				LogHandler.sendActionGrafic(plugin, player, eco, list, page, end, player.getName(), last, actionGraficCmdString);
				return;
			} else
			{
				start = page*25;
				end = 24;
				ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
						plugin.getMysqlHandler().getList(Type.TREND, order, fst.isDescending(), start, end,
								query, whereObjects));
				int last = plugin.getMysqlHandler().countWhereID(Type.TREND, query, whereObjects);
				LogHandler.sendTrendGrafic(plugin, player, eco, list, page, end, player.getName(), last, trendGraficCmdString);
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
								query, whereObjects));
				int last = plugin.getMysqlHandler().countWhereID(Type.ACTION, query, whereObjects);
				LogHandler.sendActionLogs(plugin, player, eco, list, page, end, player.getName(), last, actionLogCmdString);
				return;
			} else
			{
				start = page*10;
				end = 9;
				ArrayList<TrendLogger> list = ConvertHandler.convertListIV(
						plugin.getMysqlHandler().getList(Type.TREND, order, fst.isDescending(), start, end,
								query, whereObjects));
				int last = plugin.getMysqlHandler().countWhereID(Type.TREND, query, whereObjects);
				LogHandler.sendTrendLogs(plugin, player, eco, list, page, end, player.getName(), last, trendLogCmdString);
				return;
			}
		}
	}
}