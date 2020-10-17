package main.java.me.avankziar.aep.spigot.database;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;

public class _YamlHandler
{
	private AdvancedEconomyPlus plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	private YamlConfiguration arafst = new YamlConfiguration();
	private File arabicfiltersettings = null;
	private YamlConfiguration dutfst = new YamlConfiguration();
	private File dutchfiltersettings = null;
	private YamlConfiguration engfst = new YamlConfiguration();
	private File englishfiltersettings = null;
	private YamlConfiguration frefst = new YamlConfiguration();
	private File frenchfiltersettings = null;
	private YamlConfiguration gerfst = new YamlConfiguration();
	private File germanfiltersettings = null;
	private YamlConfiguration hinfst = new YamlConfiguration();
	private File hindifiltersettings = null;
	private YamlConfiguration itafst = new YamlConfiguration();
	private File italianfiltersettings = null;
	private YamlConfiguration japfst = new YamlConfiguration();
	private File japanesefiltersettings = null;
	private YamlConfiguration madfst = new YamlConfiguration();
	private File mandarinfiltersettings = null;
	private YamlConfiguration rusfst = new YamlConfiguration();
	private File russianfiltersettings = null;
	private YamlConfiguration spafst = new YamlConfiguration();
	private File spanishfiltersettings = null;
	private YamlConfiguration fst = new YamlConfiguration();
	
	private File arabic = null;
	private YamlConfiguration ara = new YamlConfiguration();
	private File dutch = null;
	private YamlConfiguration dut = new YamlConfiguration();
	private File english = null;
	private YamlConfiguration eng = new YamlConfiguration();
	private File french = null;
	private YamlConfiguration fre = new YamlConfiguration();
	private File german = null;
	private YamlConfiguration ger = new YamlConfiguration();
	private File hindi = null;
	private YamlConfiguration hin = new YamlConfiguration();
	private File italian = null;
	private YamlConfiguration ita = new YamlConfiguration();
	private File japanese = null;
	private YamlConfiguration jap = new YamlConfiguration();
	private File mandarin = null;
	private YamlConfiguration mad = new YamlConfiguration();
	private File russian = null;
	private YamlConfiguration rus = new YamlConfiguration();
	private File spanish = null;
	private YamlConfiguration spa = new YamlConfiguration();
	private String languages;
	private YamlConfiguration lang = null;
	
	public _YamlHandler(AdvancedEconomyPlus plugin) 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		if(!mkdirConfig())
		{
			return false;
		}
		if(!loadYamlTask(config, cfg, "config.yml"))
		{
			return false;
		}
		if(!loadYamlTask(commands, com, "commands.yml"))
		{
			return false;
		}
		languages = cfg.getString("Language", "English");
		if(!mkdir())
		{
			return false;
		}
		if(!loadYamls())
		{
			return false;
		}
		initGetL();
		return true;
	}
	
	public YamlConfiguration getConfig()
	{
		return cfg;
	}
	
	public YamlConfiguration getCom()
	{
		return com;
	}
	
	public YamlConfiguration getL()
	{
		return lang;
	}
	
	public void initGetL()
	{
		if(languages.equalsIgnoreCase("Arabic"))
		{
			lang = ara;
		} else if(languages.equalsIgnoreCase("Dutch"))
		{
			lang = dut;
		} else if(languages.equalsIgnoreCase("French"))
		{
			lang = fre;
		} else if(languages.equalsIgnoreCase("German"))
		{
			lang = ger;
		} else if(languages.equalsIgnoreCase("Hindi"))
		{
			lang = hin;
		} else if(languages.equalsIgnoreCase("Italian"))
		{
			lang = ita;
		} else if(languages.equalsIgnoreCase("Japanese"))
		{
			lang = jap;
		} else if(languages.equalsIgnoreCase("Mandarin"))
		{
			lang = mad;
		} else if(languages.equalsIgnoreCase("Russian"))
		{
			lang = rus;
		} else if(languages.equalsIgnoreCase("Spanish"))
		{
			lang = spa;
		} else
		{
			lang = eng;
		}
	}
	
	public boolean mkdirConfig()
	{
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create config.yml...");
			plugin.saveResource("config.yml", false);
		}
		commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create commands.yml...");
			plugin.saveResource("commands.yml", false);
		}
		return true;
	}
	
	private boolean mkdir()
	{
		if(!mkdirLanguage())
		{
			return false;
		}
		if(!mkdirFilterSettings())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage()
	{
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		arabic = new File(directory.getPath(), "arabic.yml");
		if(!arabic.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create arabic.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("arabic.yml"), arabic);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		dutch = new File(directory.getPath(), "dutch.yml");
		if(!dutch.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create dutch.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("dutch.yml"), dutch);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		english = new File(directory.getPath(), "english.yml");
		if(!english.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create english.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("english.yml"), english);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		french = new File(directory.getPath(), "french.yml");
		if(!french.exists())
		{
			AdvancedEconomyPlus.log.info("Create french.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("french.yml"), french);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		german = new File(directory.getPath(), "german.yml");
		if(!german.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create german.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("german.yml"), german);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		hindi = new File(directory.getPath(), "hindi.yml");
		if(!hindi.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create hindi.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("hindi.yml"), hindi);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		italian = new File(directory.getPath(), "italian.yml");
		if(!italian.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create italian.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("italian.yml"), italian);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		japanese = new File(directory.getPath(), "japanese.yml");
		if(!japanese.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create japanese.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("japanese.yml"), japanese);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		mandarin = new File(directory.getPath(), "mandarin.yml");
		if(!mandarin.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create mandarin.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("mandarin.yml"), mandarin);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		russian = new File(directory.getPath(), "russian.yml");
		if(!russian.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create russian.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("russian.yml"), russian);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		spanish = new File(directory.getPath(), "spanish.yml");
		if(!spanish.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create spanish.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("spanish.yml"), spanish);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private boolean mkdirFilterSettings()
	{
		File directory = new File(plugin.getDataFolder()+"/FilterSettings/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		arabicfiltersettings = new File(directory.getPath(), "arabicfiltersettings.yml");
		if(!arabicfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create arabicfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("arabicfiltersettings.yml"), arabicfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		dutchfiltersettings = new File(directory.getPath(), "dutchfiltersettings.yml");
		if(!dutchfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create dutchfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("dutchfiltersettings.yml"), dutch);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		englishfiltersettings = new File(directory.getPath(), "englishfiltersettings.yml");
		if(!englishfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create englishfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("englishfiltersettings.yml"), englishfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		frenchfiltersettings = new File(directory.getPath(), "frenchfiltersettings.yml");
		if(!frenchfiltersettings.exists())
		{
			AdvancedEconomyPlus.log.info("Create frenchfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("frenchfiltersettings.yml"), frenchfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		germanfiltersettings = new File(directory.getPath(), "germanfiltersettings.yml");
		if(!germanfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create germanfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("germanfiltersettings.yml"), germanfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		hindifiltersettings = new File(directory.getPath(), "hindifiltersettings.yml");
		if(!hindifiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create hindifiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("hindifiltersettings.yml"), hindifiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		italianfiltersettings = new File(directory.getPath(), "italianfiltersettings.yml");
		if(!italianfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create italianfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("italianfiltersettings.yml"), italianfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		japanesefiltersettings = new File(directory.getPath(), "japanese.yml");
		if(!japanesefiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create japanesefiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("japanesefiltersettings.yml"), japanesefiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		mandarinfiltersettings = new File(directory.getPath(), "mandarinfiltersettings.yml");
		if(!mandarinfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create mandarinfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("mandarinfiltersettings.yml"), mandarinfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		russianfiltersettings = new File(directory.getPath(), "russianfiltersettings.yml");
		if(!russianfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create russianfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("russianfiltersettings.yml"), russianfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		spanishfiltersettings = new File(directory.getPath(), "spanishfiltersettings.yml");
		if(!spanishfiltersettings.exists()) 
		{
			AdvancedEconomyPlus.log.info("Create spanishfiltersettings.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("spanishfiltersettings.yml"), spanishfiltersettings);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean loadYamls()
	{
		if(!loadYamlTask(arabic, ara, "arabic.yml"))
		{
			return false;
		}
		if(!loadYamlTask(dutch, dut, "dutch.yml"))
		{
			return false;
		}
		if(!loadYamlTask(english, eng, "english.yml"))
		{
			return false;
		}
		if(!loadYamlTask(french, fre, "french.yml"))
		{
			return false;
		}
		if(!loadYamlTask(german, ger, "german.yml"))
		{
			return false;
		}
		if(!loadYamlTask(hindi, hin, "hindi.yml"))
		{
			return false;
		}
		if(!loadYamlTask(italian, ita, "italian.yml"))
		{
			return false;
		}
		if(!loadYamlTask(japanese, jap, "japanese.yml"))
		{
			return false;
		}
		if(!loadYamlTask(mandarin, mad, "mandarin.yml"))
		{
			return false;
		}
		if(!loadYamlTask(russian, rus, "russian.yml"))
		{
			return false;
		}
		if(!loadYamlTask(spanish, spa, "spanish.yml"))
		{
			return false;
		}
		return true;
	}
	
	private boolean loadYamlTask(File file, YamlConfiguration yaml, String filename)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			AdvancedEconomyPlus.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", filename)
					+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
}