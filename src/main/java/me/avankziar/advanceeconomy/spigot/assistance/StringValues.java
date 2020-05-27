package main.java.me.avankziar.advanceeconomy.spigot.assistance;

public class StringValues
{
	final public static String 
	PATH_ECO = "CmdEco.",
	PATH_MONEY = "CmdMoney.",
	PATH_BANK = "CmdBank.",
	
	ARG_BANK_ACCEPT = "accept",
	ARG_BANK_ACCEPT_ALIAS = "akzeptieren",
	ARG_BANK_ACCOUNT = "account",
	ARG_BANK_ACCOUNT_ALIAS = "konto",
	ARG_BANK_BANKPAY = "bankpay",
	ARG_BANK_BANKPAY_ALIAS = "kontoüberweisen",
	ARG_BANK_CREATE = "create",
	ARG_BANK_CREATE_ALIAS = "erstellen",
	ARG_BANK_DELETE = "delete",
	ARG_BANK_DELETE_ALIAS = "löschen",
	ARG_BANK_INVITE = "invite",
	ARG_BANK_INVITE_ALIAS = "einladen",
	ARG_BANK_KICK = "kick",
	ARG_BANK_KICK_ALIAS = "rausschmeißen",
	ARG_BANK_LEAVE = "leave",
	ARG_BANK_LEAVE_ALIAS = "verlassen",
	ARG_BANK_LOG = "log",
	ARG_BANK_LOG_ALIAS = "einträge",
	ARG_BANK_PAY = "pay",
	ARG_BANK_PAY_ALIAS = "zahlen",
	ARG_BANK_SUBMIT = "submit",
	ARG_BANK_SUBMIT_ALIAS = "übergeben",
	ARG_BANK_TOGGLE = "toggle",
	ARG_BANK_TOGGLE_ALIAS = "schalten",
	ARG_BANK_TOP = "top",
	ARG_BANK_TOP_ALIAS = "bestenliste",
	ARG_BANK_TRENDGRAFIC = "trendgrafic",
	ARG_BANK_TRENDGRAFIC_ALIAS = "trendgrafik",
	ARG_BANK_TRENDLOG = "trendlog",
	ARG_BANK_TRENDLOG_ALIAS = "trendeinträge",
	ARG_BANK_VICE = "vice",
	ARG_BANK_VICE_ALIAS = "stellvertreter",
	
	BANK_SUGGEST_ACCEPT = "/bank accept",
	BANK_SUGGEST_ACCOUNT = "/bank account <bankname/number>",
	BANK_SUGGEST_BANKPAY = "/bank bankpay <bankname/number> <value> [note]",
	BANK_SUGGEST_CREATE = "/bank create <bankname>",
	BANK_SUGGEST_DELETE = "/bank delete <bankname/number>",
	BANK_SUGGEST_INVITE = "/bank invite <bankname/number> <player>",
	BANK_SUGGEST_KICK = "/bank kick <bankname/number> <player>",
	BANK_SUGGEST_LEAVE = "/bank leave <bankname/number>",
	BANK_SUGGEST_LOG = "/bank log <bankname/number> [pagenumber]",
	BANK_SUGGEST_PAY = "/bank pay <player> <value> [note]",
	BANK_SUGGEST_SUBMIT = "/bank submit <bankname/number> <player>",
	BANK_SUGGEST_TAKE = "/bank <bankname/number> <value> [note]",
	BANK_SUGGEST_TOGGLE = "/bank toggle",
	BANK_SUGGEST_TOP = "/bank top [pagenumber]",
	BANK_SUGGEST_TRENDGRAFIC = "",
	BANK_SUGGEST_TRENDLOG = "",
	BANK_SUGGEST_VICE = "/bank vice <bankname/number> <player>",
	
	PERM_CMD_BANK = "eco.cmd.bank",
	PERM_CMD_BANK_ACCEPT = "eco.cmd.bank.accept",
	PERM_CMD_BANK_ACCOUNT = "eco.cmd.bank.account",
	PERM_CMD_BANK_BANKPAY = "eco.cmd.bank.bankpay",
	PERM_CMD_BANK_CREATE = "eco.cmd.bank.create",
	PERM_CMD_BANK_DELETE = "eco.cmd.bank.delete",
	PERM_CMD_BANK_INVITE = "eco.cmd.bank.invite",
	PERM_CMD_BANK_KICK = "eco.cmd.bank.kick",
	PERM_CMD_BANK_LEAVE = "eco.cmd.bank.leave",
	PERM_CMD_BANK_LOG = "eco.cmd.bank.log",
	PERM_CMD_BANK_PAY = "eco.cmd.bank.pay",
	PERM_CMD_BANK_SUBMIT = "eco.cmd.bank.submit",
	PERM_CMD_BANK_TOGGLE = "eco.cmd.bank.toggle",
	PERM_CMD_BANK_TOP = "eco.cmd.bank.top",
	PERM_CMD_BANK_TRENDGRAFIC = "eco.cmd.bank.trendgrafic",
	PERM_CMD_BANK_TRENDLOG = "eco.cmd.bank.trendlog",
	PERM_CMD_BANK_VICE = "eco.cmd.bank.vice",
	
	ARG_ECO_DELETELOG = "deletelog",
	ARG_ECO_DELETELOG_ALIAS = "eintraglöschen",
	ARG_ECO_PLAYER = "player",
	ARG_ECO_PLAYER_ALIAS = "spieler",
	ARG_ECO_RECOMMENT = "recomment",
	ARG_ECO_RECOMMENT_ALIAS = "neucommentieren",
	
	ECO_SUGGEST_DELETELOG = "/econ deletelog <ID>",
	ECO_SUGGEST_PLAYER = "/econ player <Player>",
	ECO_SUGGEST_RECOMMENT = "/econ recomment <ID> <Message>",
	
	PERM_CMD_ECO_DELETELOG = "eco.cmd.eco.deletelog",
	PERM_CMD_ECO_PLAYER = "eco.cmd.eco.player",
	PERM_CMD_ECO_RECOMMENT = "eco.cmd.eco.recomment",
	
	ARG_MONEY_BANKPAY = "bankpay",
	ARG_MONEY_BANKPAY_ALIAS = "kontoüberweisen",
	ARG_MONEY_FILTERLOGBETWEEN = "filterlogbetween",
	ARG_MONEY_FILTERLOGBETWEEN_ALIAS = "filtereinträgezwischen",
	ARG_MONEY_FILTERLOGCOMMENT = "filterlogcomment",
	ARG_MONEY_FILTERLOGCOMMENT_ALIAS = "filtereinträgenotiz",
	ARG_MONEY_FILTERLOGCOMMENTASCENDING = "filterlogcommentascending",
	ARG_MONEY_FILTERLOGCOMMENTASCENDING_ALIAS = "filtereinträgenotizaufsteigend",
	ARG_MONEY_FILTERLOGCOMMENTDESCENDING = "filterlogcommentdescending",
	ARG_MONEY_FILTERLOGCOMMENTDESCENDING_ALIAS = "filtereinträgenotizabsteigend",
	ARG_MONEY_FILTERLOGFROM = "filterlogfrom",
	ARG_MONEY_FILTERLOGFROM_ALIAS = "filtereinträgevon",
	ARG_MONEY_FILTERLOGGREATERTHAN = "filterloggreaterthan",
	ARG_MONEY_FILTERLOGGREATERTHAN_ALIAS = "filtereinträgegrößerals",
	ARG_MONEY_FILTERLOGLESSTHAN = "filterloglessthan",
	ARG_MONEY_FILTERLOGLESSTHAN_ALIAS = "filtereinträgekleinerals",
	ARG_MONEY_FILTERLOGORDERER = "filterlogorderer",
	ARG_MONEY_FILTERLOGORDERER_ALIAS = "filtereinträgeauftraggeber",
	ARG_MONEY_FILTERLOGSORTASCENDING = "filterlogsortascending",
	ARG_MONEY_FILTERLOGSORTASCENDING_ALIAS = "filtereinträgesortierenaufsteigend",
	ARG_MONEY_FILTERLOGSORTDESCENDING = "filterlogsortdescending",
	ARG_MONEY_FILTERLOGSORTDESCENDING_ALIAS = "filtereinträgesortierenabsteigend",
	ARG_MONEY_FILTERLOGTO = "filterlogto",
	ARG_MONEY_FILTERLOGTO_ALIAS = "filtereinträgezu",
	ARG_MONEY_FREEZE = "freeze",
	ARG_MONEY_FREEZE_ALIAS = "einfrieren",
	ARG_MONEY_GETTOTAL = "gettotal",
	ARG_MONEY_GETTOTAL_ALIAS = "ausgabegesamt",
	ARG_MONEY_GIVE = "give",
	ARG_MONEY_GIVE_ALIAS = "geben",
	ARG_MONEY_LOG = "log",
	ARG_MONEY_LOG_ALIAS = "einträge",
	ARG_MONEY_PAY = "pay",
	ARG_MONEY_PAY_ALIAS = "überweisen",
	ARG_MONEY_TAKE = "take",
	ARG_MONEY_TAKE_ALIAS = "nehmen",
	ARG_MONEY_TOGGLE = "toggle",
	ARG_MONEY_TOGGLE_ALIAS = "schalten",
	ARG_MONEY_TOP = "top",
	ARG_MONEY_TOP_ALIAS = "bestenliste",
	ARG_MONEY_TRENDDIAGRAM = "trenddiagram",
	ARG_MONEY_TRENDDIAGRAM_ALIAS = "trenddiagramm",
	ARG_MONEY_TRENDGRAFIC = "trendgrafic",
	ARG_MONEY_TRENDGRAFIC_ALIAS = "trendgrafik",
	ARG_MONEY_TRENDLOG = "trendlog",
	ARG_MONEY_TRENDLOG_ALIAS = "trendeinträge",
	
	PERM_CMD_MONEY = "eco.cmd.money",
	PERM_CMD_MONEY_BANKPAY = "eco.cmd.money.bankpay",
	PERM_CMD_MONEY_FILTERLOGBETWEEN = "eco.cmd.money.filterlogbetween",
	PERM_CMD_MONEY_FILTERLOGCOMMENT = "eco.cmd.money.filterlogcomment",
	PERM_CMD_MONEY_FILTERLOGCOMMENTASCENDING = "eco.cmd.money.filterlogcommentascending",
	PERM_CMD_MONEY_FILTERLOGCOMMENTDESCENDING = "eco.cmd.money.filterlogcommentdescending",
	PERM_CMD_MONEY_FILTERLOGFROM = "eco.cmd.money.filterlogfrom",
	PERM_CMD_MONEY_FILTERLOGGREATERTHAN = "eco.cmd.money.filterloggreaterthan",
	PERM_CMD_MONEY_FILTERLOGLESSTHAN = "eco.cmd.money.filterloglessthan",
	PERM_CMD_MONEY_FILTERLOGORDERER = "eco.cmd.money.filterlogorderer",
	PERM_CMD_MONEY_FILTERLOGOTHER = "eco.cmd.money.filterlogother",
	PERM_CMD_MONEY_FILTERLOGSORTASCENDING = "eco.cmd.money.filterlogsortascending",
	PERM_CMD_MONEY_FILTERLOGSORTDESCENDING = "eco.cmd.money.filterlogsortdescending",
	PERM_CMD_MONEY_FILTERLOGTO = "eco.cmd.money.filterlogto",
	PERM_CMD_MONEY_FREEZE = "eco.cmd.money.freeze",
	PERM_CMD_MONEY_GETTOTAL = "eco.cmd.money.gettotal",
	PERM_CMD_MONEY_GETTOTALOTHER = "eco.cmd.money.gettotalother",
	PERM_CMD_MONEY_GIVE = "eco.cmd.money.give",
	PERM_CMD_MONEY_LOG = "eco.cmd.money.log",
	PERM_CMD_MONEY_LOG_OTHER = "eco.cmd.money.logother",
	PERM_CMD_MONEY_PAY = "eco.cmd.money.pay",
	PERM_CMD_MONEY_TAKE = "eco.cmd.money.take",
	PERM_CMD_MONEY_TOGGLE = "eco.cmd.money.toggle",
	PERM_CMD_MONEY_TOP = "eco.cmd.money.top",
	PERM_CMD_MONEY_TRENDDIAGRAM = "eco.cmd.money.trenddiagram",
	PERM_CMD_MONEY_TRENDDIAGRAM_OTHER = "eco.cmd.money.trenddiagramother",
	PERM_CMD_MONEY_TRENDGRAFIC = "eco.cmd.money.trendgrafic",
	PERM_CMD_MONEY_TRENDGRAFIC_OTHER = "eco.cmd.money.trendgraficother",
	PERM_CMD_MONEY_TRENDLOG = "eco.cmd.money.trendlog",
	PERM_CMD_MONEY_TRENDLOG_OTHER = "eco.cmd.money.trendlogother",
	
	MONEY_SUGGEST_BANKPAY = "/money bankpay <bankname/number> <value> [note]",
	MONEY_SUGGEST_FILTERLOGBETWEEN = "/money filterlogbetween <min> <max> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGCOMMENT = "/money filterlogcomment <searchword> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGCOMMENTASCENDING = "/money filterlogcommentascending <searchword> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGCOMMENTDESCENDING = "/money filterlogcommentdescending <searchword> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGFROM = "/money filterlogfrom <searchword> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGGREATERTHAN = "/money filterloggreaterthan <value> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGLESSTHAN = "/money filterloglessthan <value> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGORDERER = "/money filterlogorderer <searchword> [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGSORTASCENDING = "/money filterlogsortascending [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGSORTDESCENDING = "/money filterlogsortdescending [pageynumber] [player]",
	MONEY_SUGGEST_FILTERLOGTO = "/money filterlog <searchword> [pageynumber] [player]",
	MONEY_SUGGEST_FREEZE = "/money freeze <player>",
	MONEY_SUGGEST_GETTOTAL = "/money gettotal [searchword] [player]",
	MONEY_SUGGEST_GIVE = "/money give <player> <value> [note]",
	MONEY_SUGGEST_LOG = "/money log [pagenumber] [player]",
	MONEY_SUGGEST_PAY = "/money pay <player> <value> [note]",
	MONEY_SUGGEST_TAKE = "/money take <player> <value> [note]",
	MONEY_SUGGEST_TOGGLE = "/money toggle",
	MONEY_SUGGEST_TOP = "/money top [pagenumber]",
	MONEY_SUGGEST_TRENDDIAGRAM = "/money trenddiagram [pagenumber] [player]",
	MONEY_SUGGEST_TRENDGRAFIC = "/money trendgrafic [pagenumber] [player]",
	MONEY_SUGGEST_TRENDLOG = "/money trendlog [pagenumber] [player]",
	
	PERM_BYPASS_RESERVEDNAME = "eco.cmd.bypass.reservednames",
	PERM_BYPASS_BANKINFO = "eco.cmd.bypass.bankinfo",
	PERM_BYPASS_RECOMMENT = "eco.cmd.bypass.recomment";
}