package main.java.me.avankziar.aep.spigot.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.Language.ISO639_2B;

public class YamlHandler
{
	private AdvancedEconomyPlus plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	
	private String languages;
	private File language = null;
	private YamlConfiguration lang = new YamlConfiguration();
	
	private File loggersettings = null;
	private YamlConfiguration ls = new YamlConfiguration();
	
	private LinkedHashMap<String, YamlConfiguration> cy = new LinkedHashMap<>();//Currency Uniquename

	public YamlHandler(AdvancedEconomyPlus plugin) throws IOException 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public YamlConfiguration getConfig()
	{
		return cfg;
	}
	
	public YamlConfiguration getCom()
	{
		return com;
	}
	
	public YamlConfiguration getLang()
	{
		return lang;
	}
	
	public YamlConfiguration getFilSet()
	{
		return ls;
	}
	
	public LinkedHashMap<String, YamlConfiguration> getCurrency()
	{
		return cy;
	}
	
	public YamlConfiguration getCurrency(String uniquename)
	{
		return cy.get(uniquename);
	}
	
	public boolean loadYamlHandler() throws IOException
	{
		if(!mkdirStaticFiles())
		{
			return false;
		}
		
		if(!mkdirDynamicFiles()) //Per language one file
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles() throws IOException
	{
		//Erstellen aller Werte FÜR die Config.yml
		plugin.setYamlManager(new YamlManager());
		
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		
		//Initialisierung der config.yml
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create config.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				//Erstellung einer "leere" config.yml
				Files.copy(in, config.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Laden der config.yml
		cfg = loadYamlTask(config, cfg);
        if(cfg == null)
        {
        	return false;
        }
		
		//Niederschreiben aller Werte für die Datei
		writeFile(config, cfg, plugin.getYamlManager().getConfigKey());
		
		languages = cfg.getString("Language", "ENGLISH").toUpperCase();
		
		commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create commands.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				//Erstellung einer "leere" config.yml
				Files.copy(in, commands.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		com = loadYamlTask(commands, com);
		if(com == null)
		{
			return false;
		}
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());
		return true;
	}
	
	private boolean mkdirDynamicFiles() throws IOException
	{
		//Vergleich der Sprachen
		List<Language.ISO639_2B> types = new ArrayList<Language.ISO639_2B>(EnumSet.allOf(Language.ISO639_2B.class));
		ISO639_2B languageType = ISO639_2B.ENG;
		for(ISO639_2B type : types)
		{
			if(type.toString().equals(languages))
			{
				languageType = type;
				break;
			}
		}
		//Setzen der Sprache
		plugin.getYamlManager().setLanguageType(languageType);
		
		if(!mkdirLanguage())
		{
			return false;
		}
		
		if(!mkdirCurrencies())
		{
			return false;
		}
		
		if(!mkdirLoggerSettings())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage() throws IOException
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		language = new File(directory.getPath(), languageString+".yml");
		if(!language.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create %lang%.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				//Erstellung einer "leere" config.yml
				Files.copy(in, language.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Laden der Datei
		lang = loadYamlTask(language, lang);
		if(lang == null)
		{
			return false;
		}
		//Niederschreiben aller Werte in die Datei
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		return true;
	}
	
	private boolean mkdirCurrencies() throws IOException
	{
		File directory = new File(plugin.getDataFolder()+"/Currencies/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		for(String currency : getConfig().getStringList("Load.Currencies"))
		{
			File cur = new File(directory.getPath(), currency+".yml");
			boolean exist = cur.exists();
			if(!exist) 
			{
				AdvancedEconomyPlus.log.info("Create %cur%.yml...".replace("%cur%", currency));
				try(InputStream in = plugin.getResource("default.yml"))
				{
					//Erstellung einer "leere" config.yml
					Files.copy(in, cur.toPath());
				} catch (IOException e)
				{
					AdvancedEconomyPlus.log.info("Error by %cur%.yml! Check your Configs!".replace("%cur%", currency));
					continue;
				}
			}
			YamlConfiguration c = new YamlConfiguration();
			c = loadYamlTask(cur, c);
			if(c == null)
			{
				return false;
			}
			if(!exist) 
			{
				writeFile(cur, c, plugin.getYamlManager().getCurrencyKey(currency));
			}
			AdvancedEconomyPlus.log.info("Load Currency %cur%...".replace("%cur%", currency));
			cy.put(currency, c);
		}
		return true;
	}
	
	private boolean mkdirLoggerSettings() throws IOException
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/LoggerSettings/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		loggersettings = new File(directory.getPath(), languageString+"_ls.yml");
		if(!loggersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create %lang%_ls.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				//Erstellung einer "leere" config.yml
				Files.copy(in, loggersettings.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Laden der Datei
		ls = loadYamlTask(loggersettings, ls);
		if(ls == null)
		{
			return false;
		}
		//Niederschreiben aller Werte in die Datei
		writeFile(loggersettings, ls, plugin.getYamlManager().getLoggerSettingsKey());
		return true;
	}
	
	private YamlConfiguration loadYamlTask(File file, YamlConfiguration yaml)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			AdvancedEconomyPlus.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		return yaml;
	}
	
	private boolean writeFile(File file, YamlConfiguration yml, LinkedHashMap<String, Language> keyMap) throws IOException
	{
		yml.options().header("For more explanation see \n https://www.spigotmc.org/resources/advanced-economy-plus.79828/");
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		yml.save(file);
		return true;
	}
}