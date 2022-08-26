package main.java.me.avankziar.aep.bungee.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.aep.bungee.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.database.Language;
import main.java.me.avankziar.aep.spigot.database.Language.ISO639_2B;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlHandler
{
	private AdvancedEconomyPlus plugin;
	private File config = null;
	private Configuration cfg = new Configuration();
	
	private String languages;
	private File language = null;
	private Configuration lang = new Configuration();
	
	private LinkedHashMap<String, Configuration> cy = new LinkedHashMap<>();//Currency Uniquename

	public YamlHandler(AdvancedEconomyPlus plugin)
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public Configuration getConfig()
	{
		return cfg;
	}
	
	public Configuration getLang()
	{
		return lang;
	}
	
	public Configuration getCurrency(String uniquename)
	{
		return cy.get(uniquename);
	}
	
	private Configuration loadYamlTask(File file, Configuration yaml)
	{
		Configuration y = null;
		try 
		{
			yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) 
		{
			AdvancedEconomyPlus.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		y = yaml;
		return y;
	}
	
	private boolean writeFile(File file, Configuration yml, LinkedHashMap<String, Language> keyMap) 
	{
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBungee(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBungee(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		try
		{
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean loadYamlHandler()
	{
		plugin.setYamlManager(new YamlManager());
		if(!mkdirStaticFiles())
		{
			return false;
		}
		if(!mkdirDynamicFiles())
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles()
	{
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create config.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, config.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		cfg = loadYamlTask(config, cfg);
		writeFile(config, cfg, plugin.getYamlManager().getConfigKey());
		
		languages = plugin.getAdministration() == null 
				? cfg.getString("Language", "ENG").toUpperCase() 
				: plugin.getAdministration().getLanguage();
		return true;
	}
	
	private boolean mkdirDynamicFiles()
	{
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
		plugin.getYamlManager().setLanguageType(languageType);
		if(!mkdirLanguage())
		{
			return false;
		}
		if(!mkdirCurrencies())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage()
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
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, language.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		lang = loadYamlTask(language, lang);
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		return true;
	}
	
	private boolean mkdirCurrencies()
	{
		File directory = new File(plugin.getDataFolder()+"/Currencies/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		for(String currency : getConfig().getStringList("LoadCurrency"))
		{
			File cur = new File(directory.getPath(), currency+".yml");
			boolean exist = cur.exists();
			if(!exist) 
			{
				AdvancedEconomyPlus.log.info("Create %cur%.yml...".replace("%cur%", currency));
				try(InputStream in = plugin.getResourceAsStream("default.yml"))
				{
					//Erstellung einer "leere" config.yml
					Files.copy(in, cur.toPath());
				} catch (IOException e)
				{
					AdvancedEconomyPlus.log.info("Error by %cur%.yml! Check your Configs!".replace("%cur%", currency));
					continue;
				}
			}
			Configuration c = new Configuration();
			c = loadYamlTask(cur, c);
			if(c == null)
			{
				return false;
			}
			if(!exist) 
			{
				writeFile(cur, c, plugin.getYamlManager().getCurrencyKey(currency));
			}
			cy.put(currency, c);
		}
		return true;
	}
}