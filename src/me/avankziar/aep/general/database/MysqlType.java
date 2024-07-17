package me.avankziar.aep.general.database;

import me.avankziar.aep.general.objects.AEPUser;
import me.avankziar.aep.general.objects.AccountBungee;
import me.avankziar.aep.general.objects.AccountManagement;
import me.avankziar.aep.general.objects.AccountSpigot;
import me.avankziar.aep.general.objects.AccountVelo;
import me.avankziar.aep.general.objects.ActionLogger;
import me.avankziar.aep.general.objects.DefaultAccount;
import me.avankziar.aep.general.objects.EntityData;
import me.avankziar.aep.general.objects.LoanRepayment;
import me.avankziar.aep.general.objects.LoggerSettings;
import me.avankziar.aep.general.objects.QuickPayAccount;
import me.avankziar.aep.general.objects.StandingOrder;
import me.avankziar.aep.general.objects.TrendLogger;

public enum MysqlType
{
	ACTION("aepActionLogger", new ActionLogger(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " unixtime bigint,"
    		+ " from_account_id int,"
    		+ " to_account_id int,"
    		+ " tax_account_id int,"
    		+ " orderer_type text,"
    		+ " orderer_uuid text,"
    		+ " orderer_plugin text,"
    		+ " amount_to_withdraw double,"
    		+ " amount_to_deposit double,"
    		+ " amount_to_tax double,"
    		+ " category text,"
    		+ " comment mediumtext"
    		+ ");"),
	TREND("aepTrendLogger", new TrendLogger(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " dates bigint,"
    		+ " trend_type text,"
    		+ " account_id text,"
    		+ " relative_amount_change double,"
    		+ " firstvalue double,"
    		+ " lastvalue double"
    		+ ");"),
	STANDINGORDER("aepStandingOrder", new StandingOrder(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " standing_order_name text,"
    		+ " owner_uuid text,"
    		+ " from_account int,"
    		+ " to_account int,"
    		+ " amount double,"
    		+ " amount_paid_so_far double,"
    		+ " amount_paid_to_tax double,"
    		+ " start_time bigint,"
    		+ " repeating_time bigint,"
    		+ " last_time bigint,"
    		+ " end_time bigint,"
    		+ " cancelled boolean,"
    		+ " paused boolean"
    		+ ");"),
	LOAN("aepLoan", new LoanRepayment(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " name text,"
    		+ " from_account int,"
    		+ " to_account int,"
    		+ " loan_owner text,"
    		+ " debtor text,"
    		+ " total_amount double,"
    		+ " loan_amount double,"
    		+ " amount_ratio double,"
    		+ " tax_in_decimal double,"
    		+ " amount_paid_so_far double,"
    		+ " amount_paid_to_tax double,"
    		+ " interest double,"
    		+ " start_time bigint,"
    		+ " repeating_time bigint,"
    		+ " last_time bigint,"
    		+ " end_time bigint,"
    		+ " forgiven boolean,"
    		+ " paused boolean,"
    		+ " finished boolean"
    		+ ");"),
	LOGGERSETTINGSPRESET("aepLoggerSettingsPreset", new LoggerSettings(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " slotid int,"
    		+ " player_uuid text,"
    		+ " account_id int,"
    		+ " isaction boolean,"
    		+ " inventoryhandlertype text,"
    		+ " isdescending boolean,"
    		+ " ordertype text,"
    		+ " minimum double,"
    		+ " maximum double,"
    		+ " category text,"
    		+ " orderer text,"
    		+ " comment text,"
    		+ " firststand double,"
    		+ " laststand double"
    		+ ");"),
	ENTITYDATA("aepEntityData", new EntityData(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " entity_uuid char(36) NOT NULL UNIQUE,"
    		+ " entity_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
    		+ " entity_type text"
    		+ ");"),
	PLAYERDATA("aepPlayerData", new AEPUser(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " player_uuid char(36) NOT NULL UNIQUE,"
    		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
    		+ " wallet_moneyflow_notification boolean,"
    		+ " bank_moneyflow_notification boolean,"
    		+ " unixtime bigint"
    		+ ");"),
	ACCOUNT_BUNGEE("aepAccount", new AccountBungee(), "BUNGEE",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " account_name text,"
    		+ " account_type text,"
    		+ " account_category text,"
    		+ " account_currency text,"
    		+ " account_predefined boolean,"
    		+ " owner_uuid text NOT NULL,"
    		+ " owner_type text,"
    		+ " owner_name text,"
    		+ " balance double"
    		+ ");"),
	ACCOUNT_SPIGOT("aepAccount", new AccountSpigot(), "SPIGOT",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " account_name text,"
    		+ " account_type text,"
    		+ " account_category text,"
    		+ " account_currency text,"
    		+ " account_predefined boolean,"
    		+ " owner_uuid text NOT NULL,"
    		+ " owner_type text,"
    		+ " owner_name text,"
    		+ " balance double"
    		+ ");"),
	ACCOUNT_VELOCITY("aepAccount", new AccountVelo(), "VELOCITY",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " account_name text,"
    		+ " account_type text,"
    		+ " account_category text,"
    		+ " account_currency text,"
    		+ " account_predefined boolean,"
    		+ " owner_uuid text NOT NULL,"
    		+ " owner_type text,"
    		+ " owner_name text,"
    		+ " balance double"
    		+ ");"),
	DEFAULTACCOUNT("aepDefaultAccount", new DefaultAccount(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " player_uuid text NOT NULL,"
    		+ " account_id int,"
    		+ " account_currency text,"
    		+ " account_category text"
    		+ ");"),
	ACCOUNTMANAGEMENT("aepAccountManagement", new AccountManagement(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " player_uuid text NOT NULL,"
    		+ " account_id int,"
    		+ " account_management_type text"
    		+ ");"),
	QUICKPAYACCOUNT("aepQuickPayAccount", new QuickPayAccount(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " player_uuid text NOT NULL,"
    		+ " account_id int,"
    		+ " account_currency text"
    		+ ");")
	;
	
	private MysqlType(String tableName, Object object, String usedOnServer, String setupQuery)
	{
		this.tableName = tableName;
		this.object = object;
		this.usedOnServer = usedOnServer;
		this.setupQuery = setupQuery.replace("%%tablename%%", tableName);
	}
	
	private final String tableName;
	private final Object object;
	private final String usedOnServer;
	private final String setupQuery;

	public String getValue()
	{
		return tableName;
	}
	
	public Object getObject()
	{
		return object;
	}
	
	public String getUsedOnServer()
	{
		return usedOnServer;
	}
	
	public String getSetupQuery()
	{
		return setupQuery;
	}
}