package main.java.me.avankziar.aep.spigot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.spigot.database.LanguageObject.LanguageType;

public class YamlManager
{
	private LanguageType languageType = LanguageType.GERMAN;
	private LanguageType defaultLanguageType = LanguageType.GERMAN;
	private static LinkedHashMap<String, LanguageObject> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LanguageObject> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LanguageObject> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LanguageObject> loggerSettingsKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initFilterSettings();
	}
	
	public LanguageType getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(LanguageType languageType)
	{
		this.languageType = languageType;
	}
	
	public LanguageType getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, LanguageObject> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, LanguageObject> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, LanguageObject> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, LanguageObject> getLoggerSettingsKey()
	{
		return loggerSettingsKeys;
	}
	
	public void setFileInput(YamlConfiguration yml, LinkedHashMap<String, LanguageObject> keyMap, String key, LanguageType languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap
				.get(key)
				.languageValues
				.get(languageType)
				.length == 1)
		{
			yml.set(key,
				keyMap
				.get(key)
				.languageValues
				.get(languageType)[0]);
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add((String) o);
					} else
					{
						stringList.add(o.toString());
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	@SuppressWarnings("unused")
	public void initConfig()
	{
		Base:
		{
			configKeys.put("Language"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"English"}));
			configKeys.put("Prefix"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"&7[&2AdvancedEconomyPlus&7] &r"}));
			configKeys.put("Bungee"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
		}
		Mysql:
		{
			configKeys.put("Mysql.Status"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Mysql.Host"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"127.0.0.1"}));
			configKeys.put("Mysql.Port"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					3306}));
			configKeys.put("Mysql.DatabaseName"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"mydatabase"}));
			configKeys.put("Mysql.SSLEnabled"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Mysql.AutoReconnect"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					true}));
			configKeys.put("Mysql.VerifyServerCertificate"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("Mysql.User"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"admin"}));
			configKeys.put("Mysql.Password"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"not_0123456789"}));
			configKeys.put("Mysql.TableNameI"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyPlayerData"}));
			configKeys.put("Mysql.TableNameII"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyBankData"}));
			configKeys.put("Mysql.TableNameIII"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyActionLogger"}));
			configKeys.put("Mysql.TableNameIV"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyTrendLogger"}));
			configKeys.put("Mysql.TableNameV"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyStandingOrder"}));
			configKeys.put("Mysql.TableNameVI"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyLoan"}));
			configKeys.put("Mysql.TableNameVII"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"economyLoggerSettingsPreset"}));
		}
		Generator:
		{
			configKeys.put("Identifier.Click"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"click"}));
			configKeys.put("Identifier.Hover"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"hover"}));
			configKeys.put("Seperator.BetweenFunction"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"~"}));
			configKeys.put("Seperator.WhithinFuction"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"@"}));
			configKeys.put("Seperator.Space"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"+"}));
			configKeys.put("Seperator.HoverNewLine"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"~!~"}));
		}
		MechanicSettings:
		{
			configKeys.put("UsePlayerAccount"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					true}));
			configKeys.put("UseBank"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("UseStandingOrder"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
			configKeys.put("UseLoan"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					false}));
		}
		EconomySettings:
		{
			configKeys.put("StartMoney"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					0.0}));
			configKeys.put("CurrencyNameSingular"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"Euro"}));
			configKeys.put("CurrencyNamePlural"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"Euros"}));
			configKeys.put("TrendLogger.ValueIsStabil"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					1000.0}));
			configKeys.put("GraficSpaceSymbol"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"ˉ"}));
			configKeys.put("GraficPointSymbol"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"x"}));
		}
		JobsRebornHook:
		{
			configKeys.put("JobsRebornHookTaskTimer"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"0", "15", "30", "45"}));
		}
		BankSettings:
		{
			configKeys.put("ReservedNames"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"YourSever", "YourServerMk2"}));
			configKeys.put("BankAccountFromat"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"FOUR_DIGITS_TIMES_THREE"}));
		}
		RepeatingTimes:
		{
			configKeys.put("StandingOrderRepeatTime"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					180}));
			configKeys.put("LoanRepaymentRepeatTime"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					180}));
		}
	}
	
	@SuppressWarnings("unused") //TODO:Commands
	public void initCommands()
	{
		Money:
		{
		commandsKeys.put("money.Name"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				""}));
		commandsKeys.put("money.Permission"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				""}));
		commandsKeys.put("money.Suggestion"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				""}));
		commandsKeys.put("money.CommandString"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				""}));
		commandsKeys.put("money.Explanation"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				""}));
		
		commandsKeys.put("money_loggersettings.Argument"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				"filtersettings"}));
		commandsKeys.put("money_loggersettings.Permission"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				"eco.cmd.money.loggersettings"}));
		commandsKeys.put("money_loggersettings.Suggestion"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				"/money loggersetting [page] [playername]"}));
		commandsKeys.put("money_loggersettings.CommandString"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				"/money loggersettings "}));
		commandsKeys.put("money_loggersettings.Explanation"
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				"Open the Gui for the loggersettings for Action- and trendlog"}));
		}
		commandsKeys.put(""
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
				""}));
	}
	
	public void initLanguage()
	{
		languageKeys.put("NoPermission"
			, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
			"&cDu hast dafür keine Rechte!",
			"&cYou have no rights!"}));
		languageKeys.put(""
				, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
				"",
				""}));
	}
	
	@SuppressWarnings("unused") //TODO:FilterSettings
	public void initFilterSettings()
	{
		ActualParameter:
		{
			loggerSettingsKeys.put("ActualParameter"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§eAktuelle Werte:",
					"§bSpieler: §f%uuid%",
					"§bBanknummber: §f%number%",
					"§bOrderSpalte: §f%ordercolumn%",
					"§bVon: §f%from% §f| §bZu: §f%to%",
					"§bAuftraggeber: §f%orderer%",
					"§bKommentar: §f%comment%",
					"§bMin > §f%min% | §bMax < §f%max%",
					"§bErstStand > §f%firststand%",
					"§bLetzterStand < §f%laststand%",
					"§bistAbsteigen: §f%descending%",
					
					"§eActual Values:",
					"§bPlayer: §f%uuid%",
					"§bBanknumber: §f%number%",
					"§bOrderColumn: §f%ordercolumn%",
					"§bFrom: §f%from% §f| §bTo: §f%to%",
					"§bOrderer: §f%orderer%",
					"§bComment: §f%comment%",
					"§bMin > §f%min% §f| §bMax < §f%max%",
					"§bFirstStand > §f%firststand%",
					"§bLastStand < §f%laststand%",
					"§bisDescending: §f%descending%",
					}));
			
			loggerSettingsKeys.put("Preset."
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§eAktuelle Werte:",
					"§cKein Preset vorhanden.",
					
					"§eActual Values:",
					"§cNo preset available.",
					}));
		}
		Output:
		{
			loggerSettingsKeys.put("40.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6BarChart",
					"&6BarChart"}));
			loggerSettingsKeys.put("40.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BIRCH_DOOR"}));
			loggerSettingsKeys.put("40.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§eKlick zum Aufrufen des BarChart des","§eAktionlogs nach den jetztigen Parameter.",
					"§eClick to call the barchart of the","§eaction log according to the current parameters."}));
			
			loggerSettingsKeys.put("48.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Diagram",
					"&6Diagram"}));
			loggerSettingsKeys.put("48.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"OAK_DOOR"}));
			loggerSettingsKeys.put("48.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Aufrufen des Diagramms des","§eAktionlogs nach den jetzt Parameter.",
					"§cRechsklick §ezum Aufrufen des Diagramms des","§eTrendlogs nach den jetzt Parameter.",
					"§aLeft-click §eto call the diagram of the","§eaction log according to the current parameters.",
					"§cRight-click §eto call the diagram of the","§etrend log according to the current parameters.",}));
			
			loggerSettingsKeys.put("49.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Log",
					"&6Log"}));
			loggerSettingsKeys.put("49.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"SPRUCE_DOOR"}));
			loggerSettingsKeys.put("49.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Aufrufen des Logs des","§eAktionlogs nach den jetztigen Parameter.",
					"§cRechsklick §ezum Aufrufen des Logs des","§eTrendlogs nach den jetzt Parameter.",
					"§aLeft-click §eto call the log of the","§eaction log according to the current parameters.",
					"§cRight-click §eto call the log of the","§etrend log according to the current parameters."}));
			
			loggerSettingsKeys.put("50.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Grafik",
					"&6Grafic"}));
			loggerSettingsKeys.put("50.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"DARK_OAK_DOOR"}));
			loggerSettingsKeys.put("50.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Aufrufen der Grafik des","§eAktionlogs nach den jetztigen Parameter.",
					"§cRechsklick §ezum Aufrufen der Grafik des","§eTrendlogs nach den jetzt Parameter.",
					"§aLeft-click §eto call the grafic of the","§eaction log according to the current parameters.",
					"§cRight-click §eto call the grafic of the","§etrend log according to the current parameters."}));
		}
		PreSet:
		{
			loggerSettingsKeys.put("36.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Pre&bSet &f1",
					"&6Pre&bSet &f1"}));
			loggerSettingsKeys.put("36.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("36.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Laden des Presets.",
					"§cRechsklick §ezum Speichern der Presets.",
					"§4Shiftklick §ezum Löschen des Presets.",
					"§aLeft-click §eto load the preset.",
					"§cRight-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("44.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Pre&bSet &f2",
					"&6Pre&bSet &f2"}));
			loggerSettingsKeys.put("44.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("44.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Laden des Presets.",
					"§cRechsklick §ezum Speichern der Presets.",
					"§4Shiftklick §ezum Löschen des Presets.",
					"§aLeft-click §eto load the preset.",
					"§cRight-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("45.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Pre&bSet &f3",
					"&6Pre&bSet &f3"}));
			loggerSettingsKeys.put("45.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("45.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Laden des Presets.",
					"§cRechsklick §ezum Speichern der Presets.",
					"§4Shiftklick §ezum Löschen des Presets.",
					"§aLeft-click §eto load the preset.",
					"§cRight-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			loggerSettingsKeys.put("53.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&6Pre&bSet &f4",
					"&6Pre&bSet &f4"}));
			loggerSettingsKeys.put("53.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"BOOK"}));
			loggerSettingsKeys.put("53.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§aLinksklick §ezum Laden des Presets.",
					"§cRechsklick §ezum Speichern der Presets.",
					"§4Shiftklick §ezum Löschen des Presets.",
					"§aLeft-click §eto load the preset.",
					"§cRight-click §eto save the preset.",
					"§4Shift-click §eto delete the preset."}));
			
		}
		Min:
		{

			loggerSettingsKeys.put("0.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMin +&f1&f | &a+&f50",
					"&aMin +&f1&f | &a+&f50"}));
			loggerSettingsKeys.put("0.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("0.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAls<§f und §b>Zwischen< §fgenutzt.",
					"§aLinksklick §ezum erhöhen des","§eMinimum-Parameter um §f1§e.",
					"§cRechsklick §ezum erhöhen des","§eMinimum-Parameter um §f50§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f.",
					"§aLeft-click §eto increase the","§eminimum-parameter by §f1§e.",
					"§cRight-click §eto increase the","§eminimum-parameter by §f50§e.",
					"§4Shift-click §eto reset the parameter!"}));
			loggerSettingsKeys.put("1.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMin &c-&f1&f | &c-&f50",
					"&cMin &c-&f1&f | &c-&f50"}));
			loggerSettingsKeys.put("1.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("1.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>KleinerAls<§f und §b>Zwischen< §fgenutzt.",
					"§aLinksklick §ezum verringern des","§eMinimum-Parameter um §f1§e.",
					"§cRechsklick §ezum verringern des","§eMinimum-Parameter um §50§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>LessThan<§f and §b>Between<§f.",
					"§aLeft-click §eto decrease the","§eminimum-parameter by §f1§e.",
					"§cRight-click §eto decrease the","§eminimum-parameter by §f50§e.",
					"§4Shift-click §eto reset the parameter!"}));
			
			loggerSettingsKeys.put("9.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMin +&f1.000&f | &a+&f50.000",
					"&aMin +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("9.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("9.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAls<§f und §b>Zwischen< §fgenutzt.",
					"§aLinksklick §ezum erhöhen des","§eMinimum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum erhöhen des","§eMinimum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>LessThan<§f and §b>Between<§f.",
					"§aLeft-click §eto increase the","§eminimum-parameter by §f1,000§e.",
					"§cRight-click §eto increase the","§eminimum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("10.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMin &c-&f1.000&f | &c-&f50.000",
					"&cMin &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("10.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("10.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAls<§f und §b>Zwischen< §fgenutzt.",
					"§aLinksklick §ezum verringern des","§eMinimum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum verringern des","§eMinimum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f",
					"§aLeft-click §eto decrease the","§eminimum-parameter by §f50.000§e.",
					"§cRight-click §eto decrease the","§eminimum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("18.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMin +&f1.000.000&f | &a+&f50.000.000",
					"&aMin +&f1,000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("18.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("18.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAls<§f und §b>Zwischen< §fgenutzt.",
					"§aLinksklick §ezum erhöhen des","§eMinimum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum erhöhen des","§eMinimum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>LessThan<§f and §b>Between<§f.",
					"§aLeft-click §eto increase the","§eminimum-parameter by §f1,000,000§e.",
					"§cRight-click §eto increase the","§eminimum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("19.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMin &c-&f1.000.000&f | &c-&f50.000.000",
					"&cMin &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("19.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("19.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>KleinerAls<§f und §b>Zwischen< §fgenutzt.",
					"§aLinksklick §ezum verringern des","§eMinimum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum verringern des","§eMinimum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>LessThan<§f and §b>Between<§f.",
					"§aLeft-click §eto decrease the","§eminimum-parameter by §f1,000,000§e.",
					"§cRight-click §eto decrease the","§eminimum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
		}
		
		Max:
		{
			loggerSettingsKeys.put("7.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMax +&f1&f | &a+&f50",
					"&aMax +&f1&f | &a+&f50"}));
			loggerSettingsKeys.put("7.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("7.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAls<§f und §b>Zwischen<§f genutzt.",
					"§aLinksklick §ezum erhöhen des","§eMaximum-Parameter um §f1§e.",
					"§cRechsklick §ezum erhöhen des","§eMinimum-Parameter um §f50§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f.",
					"§aLeft-click §eto increase the","§emaximum-parameter by §f1§e.",
					"§cRight-click §eto increase the","§emaximum-parameter by §f50§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("8..Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMax &c-&f1&f | &c-&f50",
					"&cMax &c-&f1&f | &c-&f50"}));
			loggerSettingsKeys.put("8..Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("8..Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAls<§f und §b>Zwischen<§f genutzt.",
					"§aLinksklick §ezum verringern des","§eMaximum-Parameter um §f1§e.",
					"§cRechsklick §ezum verringern des","§eMaximum-Parameter um §f50§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f.",
					"§aLeft-click §eto decrease the","§emaximum-parameter by §f1§e.",
					"§cRight-click §eto decrease the","§emaximum-parameter by §f50§e.",
					"§4Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("16.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMax +&f1.000&f | &a+&f50.000",
					"&aMax +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("16.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("16.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAls<§f und §b>Zwischen<§f genutzt.",
					"§aLinksklick §ezum erhöhen des","§eMaximum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum erhöhen des","§eMaximum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f.",
					"§aLeft-click §eto increase the","§emaximum-parameter by §f1.000§e.",
					"§cRight-click §eto increase the","§emaximum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("17.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMax &c-&f1.000&f | &c-&f50.000",
					"&cMax &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("17.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("17.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAls<§f und §b>Zwischen<§f genutzt.",
					"§aLinksklick §ezum verringern des","§eMaximum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum verringern des","§eMaximum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f.",
					"§aLeft-click §eto decrease the","§emaximum-parameter by §f1,000§e.",
					"§cRight-click §eto decrease the","§emaximum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("25.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aMax +&f1.000.000&f | &a+&f50.000.000",
					"&aMax +&f1.000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("25.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_WOOL"}));
			loggerSettingsKeys.put("25.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>GrößerAls<§f und §b>Zwischen<§f genutzt.",
					"§aLinksklick §ezum erhöhen des","§eMaximum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum erhöhen des","§eMaximum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f.",
					"§aLeft-click §eto increase the","§emaximum-parameter by §f1,000,000§e.",
					"§cRight-click §eto increase the","§emaximum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("26.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cMax &c-&f1.000.000&f | &c-&f50.000.000",
					"&cMax &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("26.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_WOOL"}));
			loggerSettingsKeys.put("26.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§e§b>GrößerAls<§f und §b>Zwischen<§f genutzt.",
					"§aLinksklick §ezum verringern des","§eMaximum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum verringern des","§eMaximum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>Less than<§f and §b>Between<§f.",
					"§aLeft-click §eto decrease the","§emaximum-parameter by §f1,000,000§e.",
					"§cRight-click §eto decrease the","§emaximum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
		}
		
		FirstStand:
		{
			loggerSettingsKeys.put("29.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aErststand +&f1.000&f | &a+&f50.000",
					"&aFirstStand +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("29.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("29.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>ErstStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum erhöhen des","§eMinmum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum erhöhen des","§eMinmum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§aLeft-click §eto increase the","§eminmum-parameter by §f1,000§e.",
					"§cRight-click §eto increase the","§eminmum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("30.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aErststand &c-&f1.000&f | &c-&f50.000",
					"&aFirstStand &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("30.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("30.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>ErstStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum verringern des","§eMinmum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum verringern des","§eMinmum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§aLeft-click §eto decrease the","§eminmum-parameter by §f1,000§e.",
					"§cRight-click §eto decrease the","§eminmum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("38.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aErststand +&f1.000.000&f | &a+&f50.000.000",
					"&aFirstStand +&f1,000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("38.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("38.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>ErstStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum erhöhen des","§eMinmum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum erhöhen des","§eMinmum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§aLeft-click §eto increase the","§eminmum-parameter by §f1,000,000§e.",
					"§cRight-click §eto increase the","§eminmum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("39.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aErststand &c-&f1.000.000&f | &c-&f50.000.000",
					"&aFirstStand &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("39.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("39.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>ErstStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum verringern des","§eMinmum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum verringern des","§eMinmum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>FirstStand< (Only Trendlog)§f.",
					"§aLeft-click §eto decrease the","§eminmum-parameter by §f1,000,000§e.",
					"§cRight-click §eto decrease the","§eminmum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
		}
		
		LastStand:
		{
			loggerSettingsKeys.put("32.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aLetzterstand +&f1.000&f | &a+&f50.000",
					"&aLastStand +&f1,000&f | &a+&f50,000"}));
			loggerSettingsKeys.put("32.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("32.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>LetzterStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum erhöhen des","§eMaximum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum erhöhen des","§eMaximum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§aLeft-click §eto increase the","§emaximum-parameter by §f1,000§e.",
					"§cRight-click §eto increase the","§emaximum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("33.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aLetzterstand &c-&f1.000&f | &c-&f50.000",
					"&aFirstStand &c-&f1,000&f | &c-&f50,000"}));
			loggerSettingsKeys.put("33.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("33.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben","§b>LetzterStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum verringern des","§eMaximum-Parameter um §f1.000§e.",
					"§cRechsklick §ezum verringern des","§eMaximum-Parameter um §f50.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information","§b>LastStand< (Only Trendlog)§f.",
					"§aLeft-click §eto decrease the","§emaximum-parameter by §f1,000§e.",
					"§cRight-click §eto decrease the","§emaximum-parameter by §f50,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("41.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aLetzterstand +&f1.000.000&f | &a+&f50.000.000",
					"&aFirstStand +&f1,000,000&f | &a+&f50,000,000"}));
			loggerSettingsKeys.put("41.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"GREEN_TERRACOTTA"}));
			loggerSettingsKeys.put("41.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>LetzterStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum erhöhen des Maximum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum erhöhen des Maximum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information §b>LastStand< (Only Trendlog)§f.",
					"§aLeft-click §eto increase the maximum-parameter by §f1,000,000§e.",
					"§cRight-click §eto increase the maximum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
			loggerSettingsKeys.put("42.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&aLetzterstand &c-&f1.000.000&f | &c-&f50.000.000",
					"&aFirstStand &c-&f1,000,000&f | &c-&f50,000,000"}));
			loggerSettingsKeys.put("42.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"RED_TERRACOTTA"}));
			loggerSettingsKeys.put("42.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>LetzterStand< (Nur Trendlog)§f genutzt.",
					"§aLinksklick §ezum verringern des Maximum-Parameter um §f1.000.000§e.",
					"§cRechsklick §ezum verringern des Maximum-Parameter um §f50.000.000§e.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information §b>LastStand< (Only Trendlog)§f.",
					"§aLeft-click §eto decrease the maximum-parameter by §f1,000,000§e.",
					"§cRight-click §eto decrease the maximum-parameter by §f50,000,000§e.",
					"§4Shift-click §to reset the parameter!"}));
		}
		
		AnvilEditor:
		{
			loggerSettingsKeys.put("3.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cVon-Parameter",
					"&cFrom-parameter"}));
			loggerSettingsKeys.put("3.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"ANVIL"}));
			loggerSettingsKeys.put("3.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>Von<§f genutzt.",
					"§eKlicke zur Eingabe eines SpielerNamen oder Bankkontonummer.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information §b>From<§f.",
					"§eClicks to enter a player name or bank account number.",
					"§4Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("4.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cAuftraggeber-Parameter",
					"&cOrderer-parameter"}));
			loggerSettingsKeys.put("4.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"ANVIL"}));
			loggerSettingsKeys.put("4.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>Auftraggeber<§f genutzt.",
					"§eKlicke zur Eingabe eines SpielerNamen oder Bankkontonummer.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information §b>Orderer<§f.",
					"§eClicks to enter a player name or bank account number.",
					"§4Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("5.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cZu-Parameter",
					"&cTo-parameter"}));
			loggerSettingsKeys.put("5.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"ANVIL"}));
			loggerSettingsKeys.put("5.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>Zu<§f genutzt.",
					"§eKlicke zur Eingabe eines SpielerNamen oder Bankkontonummer.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information §b>To<§f.",
					"§eClicks to enter a player name or bank account number.",
					"§4Shift-click §to reset the parameter!"}));
			
			loggerSettingsKeys.put("13.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cKommentar-Parameter",
					"&cComment-parameter"}));
			loggerSettingsKeys.put("13.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"ANVIL"}));
			loggerSettingsKeys.put("13.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>Kommentar< (Nur Aktionlog)§f genutzt.",
					"§eKlicke zur Eingabe eines Kommentars.",
					"§4Shiftklick §ezum Zurücksetzten des Parameters!",
					"§fParameter is used for the information §b>Comment< (Only Actionlog)§f.",
					"§eClicks to enter a comment.",
					"§4Shift-click §to reset the parameter!"}));
		}
		
		Booleans:
		{
			loggerSettingsKeys.put("11.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cAuf/Absteigend-Parameter",
					"&cA/Descending-parameter"}));
			loggerSettingsKeys.put("11.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"REDSTONE_TORCH"}));
			loggerSettingsKeys.put("11.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter wird für die Angaben §b>Id<§f oder §b>Betrag< §fgenutzt.",
					"§aLinksklick §ezum §b>Aufsteigend<§e einstellen.",
					"§cRechsklick §ezum §b>Absteigend<§f einstellen.",
					"§fParameter is used for the information §b>Id<§f or §b>Amount<.",
					"§aLeft-click §eto §b>Ascending<§e set.",
					"§cRight-click §eto §b>Descending<§e set."}));
		}
		
		Enums:
		{
			loggerSettingsKeys.put("15.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cId oder Betrags-Parameter",
					"&cA/Descending-parameter"}));
			loggerSettingsKeys.put("15.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"REDSTONE_TORCH"}));
			loggerSettingsKeys.put("15.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§fParameter stellt die zu nutzende Spalte ein. Entweder §b>Id< §foder §b>Betrag<§f.",
					"§aLinksklick §ezum §b>Id<§e einstellen.",
					"§cRechsklick §ezum §b>Betrag<§f einstellen.",
					"§fParameter sets the column to be used. Either §b>Id< §for §b>Amount<§f.",
					"§aLeft-click §e to §b>Id<§e set.",
					"§cRight-click §e to §b>Amount<§e set."}));
		}
		
		Anvil:
		{
			loggerSettingsKeys.put("100.Name"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"&cAmbossEditor",
					"&cAnvilEditor"}));
			loggerSettingsKeys.put("100.Material"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN}, new Object[] {
					"PAPER"}));
			loggerSettingsKeys.put("100.Lore"
					, new LanguageObject(new LanguageType[] {LanguageType.GERMAN, LanguageType.ENGLISH}, new Object[] {
					"§eTrage deine Suchtext im Textfeld ein und",
					"§eklicke danach auf den rechten Slot zum Bestätigen.",
					"§eEnter your search text in the text field and",
					"§ethen click on the right slot to confirm."}));
		}
	}
}
