package com.windows.Utility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Utility extends JavaPlugin implements Listener{
	  boolean PVP1 = false;
	  boolean stop = false;
	  boolean PVP = false;
	  boolean protection = false;
	  Integer time = 0;
	  String prefix = "§f§l[ §e§l소망온라인 §f§l] §f";
	  
	  public void onEnable()
	  {
		getServer().getPluginManager().registerEvents(this, this);
	    PVP = getConfig().getBoolean("대전시스템");
	    getConfig().set("대전리스트", null);
	    getConfig().set("보호", false);
	    getConfig().set("보류", null);
	    saveConfig();
	    Bukkit.getConsoleSender().sendMessage("§e§f§l[ §e§l소망온라인 §f§l] §a1VS1 대전 플러그인 활성화");
	  }
	  
	  public void onDisable() {
		  Bukkit.getConsoleSender().sendMessage("§e§f§l[ §e§l소망온라인 §f§l] §c1VS1 대전 플러그인 비활성화");
		    getConfig().set("대전리스트", null);
		    getConfig().set("보호", false);
		    saveConfig();
		  }
	  
		public void DecompileProtect() {
			ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10));
			list.stream().filter((Integer num) -> num % 2 == 0);
		}
	  
	  public static String getFinalArg(String[] args, int start)
	  {
	    StringBuilder bldr = new StringBuilder();
	    for (int i = start; i < args.length; i++) {
	      if (i != start) {
	        bldr.append(" ");
	      }
	      bldr.append(args[i]);
	    }
	    return bldr.toString();
	  }
	  
	  public static String readFile(File textFileName) throws IOException {
		    FileReader fr = new FileReader(textFileName);

		    String s = new String();
		    int a;
		    while ((a = fr.read()) != -1)
		    {
		      s = s + (char)a;
		    }
		    fr.close();
		    return s;
		  }
	  
	  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			final Player player = (Player) sender;
			if (label.equalsIgnoreCase("관전")){
				if (!getConfig().contains("관전장")) {
					player.sendMessage(prefix + "관전장이 설정되어있지 않습니다.");
					player.sendMessage(prefix + "\"/대전 관전장설정\" 명령어로 관전장 위치를 설정해주세요.");
					return false;
				}
				if (!PVP) {
					player.sendMessage(prefix + "§c대전시스템이 비활성화 되어있습니다.");
					return false;
				}
					player.sendMessage(prefix + "관전장으로 이동되었습니다.");
					String World1 = getConfig().getString("관전장.World");
			    	double X1 = getConfig().getDouble("관전장.X");
			    	double Y1 = getConfig().getDouble("관전장.Y");
			    	double Z1 = getConfig().getDouble("관전장.Z");
			    	String Yaw1 = getConfig().getString("관전장.Yaw");
			    	String Pitch1 = getConfig().getString("관전장.Pitch");
			    	Location view = new Location(Bukkit.getWorld(World1), X1, Y1, Z1, Float.parseFloat(Yaw1), Float.parseFloat(Pitch1));
					player.teleport(view);
					return false;
			}
		    if (label.equals("대전시스템"))
		    {
		      if (player.hasPermission("windows.admin"))
		      {
		        if (!PVP)
		        {
		          PVP = true;
		          Bukkit.broadcastMessage(prefix + "§e▒ 소망온라인 대전시스템");
		          Bukkit.broadcastMessage(ChatColor.YELLOW + "§f§l[ §e§l소망온라인 §f§l] §7대전시스템이 §a활성화 §7되었습니다.");
		          getConfig().set("대전시스템", true);
		          saveConfig();
		          return false;
		        }

		        PVP = false;
		        Bukkit.broadcastMessage(prefix + "§e▒ 소망온라인 대전시스템");
		        Bukkit.broadcastMessage(ChatColor.YELLOW + "§f§l[ §e§l소망온라인 §f§l] §7대전시스템이 §c비활성화 §7되었습니다.");
		        getConfig().set("대전시스템", false);
		        saveConfig();
		        return false;
		      }

		      player.sendMessage(ChatColor.YELLOW + "§f§l[ §e§l소망온라인 §f§l] §c당신은 권한이 없습니다.");
		      return false;
		    }
			if (label.equalsIgnoreCase("대전")){
				if (!PVP) {
					player.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
					player.sendMessage(prefix + "§7대전시스템이 §c비활성화 §7되어있습니다.");
					return false;
				}
				if (args.length == 0) {
					player.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
					player.sendMessage(prefix + "/대전 신청 <아이디> | 대상에게 1:1 대전을 신청합니다.");
					player.sendMessage(prefix + "/대전 전적 | 당신의 1:1 전적을 확인합니다.");
					player.sendMessage(prefix + "/대전 동의 | 1:1 대전을 수락합니다.");
					player.sendMessage(prefix + "/대전 거절 | 1:1 대전을 거절합니다.");
					player.sendMessage(prefix + "/대전 보기 | 현재 대전을 신청한사람이 누군지 확인합니다.");
					player.sendMessage(prefix + "/대전 남은시간 | 현재 진행중인 대전의 남은시간을 확인합니다.");
					player.sendMessage(prefix + "/대전 정보 | 현재 대전중인 플레이어의 아이템 착용 정보를 봅니다.");
					player.sendMessage(prefix + "/대전 허용 <아이디> | 대전 차단중에만 사용 가능하며, 대상의 대전신청을 허용/차단합니다.");
					player.sendMessage(prefix + "/대전 차단 | 허용한 사람 외에 대전신청을 전부 차단합니다.");
					if (player.hasPermission("admin")) {
						player.sendMessage("");
						player.sendMessage(prefix + "§e▒ 관리자 명령어 - 관리자에게만 표시됩니다.");
						player.sendMessage(prefix + "/대전시스템 | 대전시스템을 §a활성화§f/§c비활성화 §f시킵니다.");
						player.sendMessage(prefix + "/대전 관전장설정 | 현재 서있는곳을 관전장으로 설정합니다.");
						player.sendMessage(prefix + "§c[!] §e도움말 - 대전이 진행되는것을 관전할 수 있는 곳입니다.");
						player.sendMessage(prefix + "/대전 대전장1설정 | 현재 서있는곳을 대전 플레이어1 좌표로 설정합니다.");
						player.sendMessage(prefix + "/대전 대전장2설정 | 현재 서있는곳을 대전 플레이어2 좌표로 설정합니다.");
						player.sendMessage(prefix + "§c[!] §e도움말 - 대전할 플레이어1, 플레이어2가 이동되는 곳입니다.");
						player.sendMessage(prefix + "/대전 스폰설정 | 현재 서있는곳을 스폰 좌표로 설정합니다.");
						player.sendMessage(prefix + "§c[!] §e도움말 - 대전이 종료될 시 이동되는 곳입니다.");
					}
					return false;
				}
				if (args[0].equalsIgnoreCase("관전장설정")) {
					if (!player.hasPermission("admin")) {
						player.sendMessage(prefix + "§c당신은 권한이 없습니다.");
						return false;
					}
					getConfig().set("관전장.World", player.getLocation().getWorld().getName());
					getConfig().set("관전장.X", player.getLocation().getX());
					getConfig().set("관전장.Y", player.getLocation().getY());
					getConfig().set("관전장.Z", player.getLocation().getZ());
					getConfig().set("관전장.Yaw", player.getLocation().getYaw());
					getConfig().set("관전장.Pitch", player.getLocation().getPitch());
					saveConfig();
					player.sendMessage(prefix + "§f관전장 좌표가 정상적으로 설정되었습니다.");
					return false;
				}
				if (args[0].equalsIgnoreCase("대전장1설정")) {
					if (!player.hasPermission("admin")) {
						player.sendMessage(prefix + "§c당신은 권한이 없습니다.");
						return false;
					}
					getConfig().set("대전장1.World", player.getLocation().getWorld().getName());
					getConfig().set("대전장1.X", player.getLocation().getX());
					getConfig().set("대전장1.Y", player.getLocation().getY());
					getConfig().set("대전장1.Z", player.getLocation().getZ());
					getConfig().set("대전장1.Yaw", player.getLocation().getYaw());
					getConfig().set("대전장1.Pitch", player.getLocation().getPitch());
					saveConfig();
					player.sendMessage(prefix + "§f대전장1 좌표가 정상적으로 설정되었습니다.");
					return false;
				}
				if (args[0].equalsIgnoreCase("대전장2설정")) {
					if (!player.hasPermission("admin")) {
						player.sendMessage(prefix + "§c당신은 권한이 없습니다.");
						return false;
					}
					getConfig().set("대전장2.World", player.getLocation().getWorld().getName());
					getConfig().set("대전장2.X", player.getLocation().getX());
					getConfig().set("대전장2.Y", player.getLocation().getY());
					getConfig().set("대전장2.Z", player.getLocation().getZ());
					getConfig().set("대전장2.Yaw", player.getLocation().getYaw());
					getConfig().set("대전장2.Pitch", player.getLocation().getPitch());
					saveConfig();
					player.sendMessage(prefix + "§f대전장2 좌표가 정상적으로 설정되었습니다.");
					return false;
				}
				if (args[0].equalsIgnoreCase("스폰설정")) {
					if (!player.hasPermission("admin")) {
						player.sendMessage(prefix + "§c당신은 권한이 없습니다.");
						return false;
					}
					getConfig().set("스폰.World", player.getLocation().getWorld().getName());
					getConfig().set("스폰.X", player.getLocation().getX());
					getConfig().set("스폰.Y", player.getLocation().getY());
					getConfig().set("스폰.Z", player.getLocation().getZ());
					getConfig().set("스폰.Yaw", player.getLocation().getYaw());
					getConfig().set("스폰.Pitch", player.getLocation().getPitch());
					saveConfig();
					player.sendMessage(prefix + "§f스폰 좌표가 정상적으로 설정되었습니다.");
					return false;
				}
			    if (args[0].equalsIgnoreCase("신청")) {
					if (!getConfig().contains("대전장1")) {
						player.sendMessage(prefix + "§c대전 플레이어1 좌표가 설정되어있지 않습니다.");
						player.sendMessage(prefix + "§c\"/대전\" 명령어의 관리자 명령어를 확인해주세요.");
						return false;
					}
					if (!getConfig().contains("대전장2")) {
						player.sendMessage(prefix + "§c대전 플레이어2 좌표가 설정되어있지 않습니다.");
						player.sendMessage(prefix + "§c\"/대전\" 명령어의 관리자 명령어를 확인해주세요.");
						return false;
					}
					if (!getConfig().contains("스폰")) {
						player.sendMessage(prefix + "§c스폰이 설정되어있지 않습니다.");
						player.sendMessage(prefix + "§c\"/대전\" 명령어의 관리자 명령어를 확인해주세요.");
						return false;
					}
			        if (args.length != 2) {
			          player.sendMessage(prefix + "§c올바르지 않은 명령어. §f/대전 신청 <아이디>");
			          return false;
			        }
			        if (Bukkit.getPlayer(args[1]) == null) {
			          player.sendMessage(prefix + "§c해당 플레이어를 찾을 수 없습니다.");
			          return false;
			        }
			        if (player.getName() == Bukkit.getPlayer(args[1]).getName()) {
			            player.sendMessage(prefix + "자기 자신에게는 1:1 대전신청을 할 수 없습니다.");
			            return false;
			          }
			        if (PVP1 == true) {
			    		player.sendMessage(prefix + "다른유저가 이미 대전장을 이용중입니다.");
			    		return false;
			    	}
			        if (getConfig().contains("차단." + Bukkit.getPlayer(args[1]).getName())) {
			        	if (!getConfig().contains("차단." + Bukkit.getPlayer(args[1]).getName() + "." + player.getName())) {
			        		player.sendMessage(prefix + "상대방이 현재 대전차단 상태입니다.");
			        	return false;
			        }
			        }
			        if (getConfig().contains("보류." + Bukkit.getPlayer(args[1]).getName())) {
			            player.sendMessage(prefix + "대상이 이미 다른사람에게 받은 1:1 대전신청을 보류중입니다.");
			            return false;
			          }
			        if (getConfig().contains("대전리스트." + player.getName())) {
			    		player.sendMessage(prefix + "당신은 이미 다른사람과 대전중입니다.");
						return false;
			    	}
			    	if (getConfig().contains("대전리스트." + Bukkit.getPlayer(args[1]).getName())) {
			    		player.sendMessage(prefix + "대상이 이미 다른사람과 대전중입니다.");
						return false;
			    	}
			        final Player p = Bukkit.getPlayer(args[1]);
					final String pi = p.getName();
			        getConfig().set("보류." + pi + ".대상", player.getName());
			        saveConfig();
			        player.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
			        player.sendMessage(prefix + "당신은 §c" + pi + " §f에게 1:1 대전을 신청했습니다.");
			        player.sendMessage(prefix + "상대방이 15초내로 동의하지 않을 시 대전 신청은 자동 취소됩니다.");
			        p.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
			        p.sendMessage(prefix + "§c" + player.getName() + " §f가 당신에게 1:1 대전을 신청했습니다.");
			        p.sendMessage(prefix + "동의하시려면 '/대전 동의', 거절하시려면 '/대전 거절'을 입력해주세요.");
			        p.sendMessage(prefix + "15초내로 입력하지 않을 시 대전 신청은 자동 취소됩니다.");
					new BukkitRunnable(){
						Integer i = 15;
						@Override
						public void run(){
							if (i >= 1){
								if (getConfig().contains("보류." + pi)) {
							i--;
								return;
							}
							}
							if (i == 0){
								if (getConfig().contains("보류." + pi)) {
								player.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
								player.sendMessage(prefix + "상대방이 동의하지않아 대전 신청이 자동으로 취소되었습니다.");
								p.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
								p.sendMessage(prefix + "15초가 지나 대전 신청이 자동으로 취소되었습니다.");
								getConfig().set("보류." + pi, null);
								saveConfig();
								cancel();
								return;
							}
							}
							cancel();
							return;
						}
					}.runTaskTimer(this,0,20);
			        return false;
			      }
			    if (args[0].equalsIgnoreCase("동의")) {
					if (!getConfig().contains("대전장1")) {
						player.sendMessage(prefix + "§c대전 플레이어1 좌표가 설정되어있지 않습니다.");
						player.sendMessage(prefix + "§c\"/대전\" 명령어의 관리자 명령어를 확인해주세요.");
						return false;
					}
					if (!getConfig().contains("대전장2")) {
						player.sendMessage(prefix + "§c대전 플레이어2 좌표가 설정되어있지 않습니다.");
						player.sendMessage(prefix + "§c\"/대전\" 명령어의 관리자 명령어를 확인해주세요.");
						return false;
					}
					if (!getConfig().contains("스폰")) {
						player.sendMessage(prefix + "§c스폰이 설정되어있지 않습니다.");
						player.sendMessage(prefix + "§c\"/대전\" 명령어의 관리자 명령어를 확인해주세요.");
						return false;
					}
			    	if (!getConfig().contains("보류." + player.getName())){
						player.sendMessage(prefix + "당신은 1:1 대전신청을 받은적이 없습니다.");
						return false;
					}
			    	String lead = getConfig().getString("보류." + player.getName() + ".대상");
			    	if (Bukkit.getPlayer(lead) == null) {
				          player.sendMessage(prefix + "1:1 대전을 신청한유저가 접속중이지 않습니다.");
				          return false;
				        }
			    	if (getConfig().contains("대전리스트." + player.getName())) {
			    		player.sendMessage(prefix + "당신은 이미 다른사람과 대전중입니다.");
						return false;
			    	}
			    	if (getConfig().contains("대전리스트." + getConfig().get("보류." + player.getName() + ".대상"))) {
			    		player.sendMessage(prefix + "대상이 이미 다른사람과 대전중입니다.");
						return false;
			    	}
			    	if (PVP1 == true) {
			    		player.sendMessage(prefix + "다른유저가 이미 대전장을 이용중입니다.");
			    		return false;
			    	}
			    	getConfig().set("대전리스트." + player.getName(), 1);
			    	getConfig().set("대전리스트." + getConfig().get("보류." + player.getName() + ".대상"), 1);
			    	String keyword = getConfig().getString("보류." + player.getName() + ".대상");
			    	Bukkit.broadcastMessage(prefix + "§c" + player.getName() + " §f님과 §c" + keyword + " §f님의 대전이 시작됩니다.");
			    	Bukkit.broadcastMessage(prefix + "관전하시려면 §e'/관전' §f명령어를 이용해주세요.");
			    	String World1 = getConfig().getString("대전장1.World");
			    	double X1 = getConfig().getDouble("대전장1.X");
			    	double Y1 = getConfig().getDouble("대전장1.Y");
			    	double Z1 = getConfig().getDouble("대전장1.Z");
			    	String Yaw1 = getConfig().getString("대전장1.Yaw");
			    	String Pitch1 = getConfig().getString("대전장1.Pitch");
			    	Location player1 = new Location(Bukkit.getWorld(World1), X1, Y1, Z1, Float.parseFloat(Yaw1), Float.parseFloat(Pitch1));
			    	String World2 = getConfig().getString("대전장2.World");
			    	double X2 = getConfig().getDouble("대전장2.X");
			    	double Y2 = getConfig().getDouble("대전장2.Y");
			    	double Z2 = getConfig().getDouble("대전장2.Z");
			    	String Yaw2 = getConfig().getString("대전장2.Yaw");
			    	String Pitch2 = getConfig().getString("대전장2.Pitch");
			    	Location player2 = new Location(Bukkit.getWorld(World2), X2, Y2, Z2, Float.parseFloat(Yaw2), Float.parseFloat(Pitch2));
					player.teleport(player1);
					Bukkit.getPlayer(lead).teleport(player2);
			    	PVP1 = true;
			    	getConfig().set("보류." + player.getName(), null);
			    	getConfig().set("알림", "check");
			    	getConfig().set("알림2", "check");
			    	saveConfig();
					for (String key : getConfig().getConfigurationSection("대전리스트").getKeys(false)){
						if (Bukkit.getPlayer(key) == null){
						} else {
							if (Bukkit.getPlayer(key).getName() == player.getName()){
								stop = true;
								protection = true;
								getConfig().set("착용1.이름", player.getName());
								if (player.getEquipment().getHelmet() != null) {
									if (player.getEquipment().getHelmet().getItemMeta().getDisplayName() != null) {
									getConfig().set("착용1.투구", player.getEquipment().getHelmet().getItemMeta().getDisplayName());
									} else {
									getConfig().set("착용1.투구", player.getEquipment().getHelmet().getType().name().replaceAll("DIAMOND_HELMET", "§f다이아몬드 투구").replaceAll("GOLD_HELMET", "§f금 투구").replaceAll("LEATHER_HELMET", "§f가죽 모자").replaceAll("IRON_HELMET", "§f철 투구").replaceAll("CHAINMAIL_HELMET", "§f사슬 투구"));
									}
									} else {
									getConfig().set("착용1.투구", "§f미착용");
									}
									if (player.getEquipment().getChestplate() != null) {
										if (player.getEquipment().getChestplate().getItemMeta().getDisplayName() != null) {
									getConfig().set("착용1.갑옷", player.getEquipment().getChestplate().getItemMeta().getDisplayName());
									} else {
									getConfig().set("착용1.갑옷", player.getEquipment().getChestplate().getType().name().replaceAll("DIAMOND_CHESTPLATE", "§f다이아몬드 갑옷").replaceAll("GOLD_CHESTPLATE", "§f금 갑옷").replaceAll("LEATHER_CHESTPLATE", "§f가죽 튜닉").replaceAll("IRON_CHESTPLATE", "§f철 갑옷").replaceAll("CHAINMAIL_CHESTPLATE", "§f사슬 갑옷"));
									}
									} else {
									getConfig().set("착용1.갑옷", "§f미착용");
									}
									if (player.getEquipment().getLeggings() != null) {
										if (player.getEquipment().getLeggings().getItemMeta().getDisplayName() != null) {
									getConfig().set("착용1.바지", player.getEquipment().getLeggings().getItemMeta().getDisplayName());
									} else {
									getConfig().set("착용1.바지", player.getEquipment().getLeggings().getType().name().replaceAll("DIAMOND_LEGGINGS", "§f다이아몬드 레깅스").replaceAll("GOLD_LEGGINGS", "§f금 레깅스").replaceAll("LEATHER_LEGGINGS", "§f가죽 바지").replaceAll("IRON_LEGGINGS", "§f철 레깅스").replaceAll("CHAINMAIL_LEGGINGS", "§f사슬 레깅스"));
									}
									} else {
									getConfig().set("착용1.바지", "§f미착용");
									}
									if (player.getEquipment().getBoots() != null) {
										if (player.getEquipment().getBoots().getItemMeta().getDisplayName() != null) {
									getConfig().set("착용1.신발", player.getEquipment().getBoots().getItemMeta().getDisplayName());
									} else {
									getConfig().set("착용1.신발", player.getEquipment().getBoots().getType().name().replaceAll("DIAMOND_BOOTS", "§f다이아몬드 부츠").replaceAll("GOLD_BOOTS", "§f금 부츠").replaceAll("LEATHER_BOOTS", "§f가죽 장화").replaceAll("IRON_BOOTS", "§f철 부츠").replaceAll("CHAINMAIL_BOOTS", "§f사슬 부츠"));
									}
									} else {
									getConfig().set("착용1.신발", "§f미착용");
									}
								saveConfig();
								new BukkitRunnable(){
									Integer i = 10;
									@Override
									public void run(){
										if (i == 10){
											if (getConfig().get("알림") != null) {
												player.sendMessage(prefix + ChatColor.RED + i + "§f초 후 전투가 시작됩니다.");
												player.setWalkSpeed(0.2F);
												i--;
												return;
										}
										}
										if ((i >= 1) && (i <= 9)){
											if (getConfig().get("알림") != null) {
										player.sendMessage(prefix + ChatColor.RED + i + "§f초 후 전투가 시작됩니다.");
										i--;
											return;
										}
										}
										if (i == 0){
											if (getConfig().get("알림") != null) {
												player.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
												player.sendMessage(prefix + "전투가 시작되었습니다.");
												player.sendMessage(prefix + "전투는 1분안에 끝내셔야하며, 1분내로 끝나지 않을시에는 전투가 강제종료 됩니다.");
												player.setHealth(20);
												stop = false;
												protection = false;
												cancel();
												return;
										}	
										}
										cancel();
										return;
									}
								}.runTaskTimer(this,0,20);
							} else {
								final Player p = Bukkit.getPlayer(key);
								getConfig().set("착용2.이름", p.getName());
								if (p.getEquipment().getHelmet() != null) {
									if (p.getEquipment().getHelmet().getItemMeta().getDisplayName() != null) {
								getConfig().set("착용2.투구", p.getEquipment().getHelmet().getItemMeta().getDisplayName());
								} else {
								getConfig().set("착용2.투구", p.getEquipment().getHelmet().getType().name().replaceAll("DIAMOND_HELMET", "§f다이아몬드 투구").replaceAll("GOLD_HELMET", "§f금 투구").replaceAll("LEATHER_HELMET", "§f가죽 모자").replaceAll("IRON_HELMET", "§f철 투구").replaceAll("CHAINMAIL_HELMET", "§f사슬 투구"));
								}
								} else {
								getConfig().set("착용2.투구", "§f미착용");
								}
								if (p.getEquipment().getChestplate() != null) {
									if (p.getEquipment().getChestplate().getItemMeta().getDisplayName() != null) {
								getConfig().set("착용2.갑옷", p.getEquipment().getChestplate().getItemMeta().getDisplayName());
								} else {
								getConfig().set("착용2.갑옷", p.getEquipment().getChestplate().getType().name().replaceAll("DIAMOND_CHESTPLATE", "§f다이아몬드 갑옷").replaceAll("GOLD_CHESTPLATE", "§f금 갑옷").replaceAll("LEATHER_CHESTPLATE", "§f가죽 튜닉").replaceAll("IRON_CHESTPLATE", "§f철 갑옷").replaceAll("CHAINMAIL_CHESTPLATE", "§f사슬 갑옷"));
								}
								} else {
								getConfig().set("착용2.갑옷", "§f미착용");
								}
								if (p.getEquipment().getLeggings() != null) {
									if (p.getEquipment().getLeggings().getItemMeta().getDisplayName() != null) {
								getConfig().set("착용2.바지", p.getEquipment().getLeggings().getItemMeta().getDisplayName());
								} else {
								getConfig().set("착용2.바지", p.getEquipment().getLeggings().getType().name().replaceAll("DIAMOND_LEGGINGS", "§f다이아몬드 레깅스").replaceAll("GOLD_LEGGINGS", "§f금 레깅스").replaceAll("LEATHER_LEGGINGS", "§f가죽 바지").replaceAll("IRON_LEGGINGS", "§f철 레깅스").replaceAll("CHAINMAIL_LEGGINGS", "§f사슬 레깅스"));
								}
								} else {
								getConfig().set("착용2.바지", "§f미착용");
								}
								if (p.getEquipment().getBoots() != null) {
									if (p.getEquipment().getBoots().getItemMeta().getDisplayName() != null) {
								getConfig().set("착용2.신발", p.getEquipment().getBoots().getItemMeta().getDisplayName());
								} else {
								getConfig().set("착용2.신발", p.getEquipment().getBoots().getType().name().replaceAll("DIAMOND_BOOTS", "§f다이아몬드 부츠").replaceAll("GOLD_BOOTS", "§f금 부츠").replaceAll("LEATHER_BOOTS", "§f가죽 장화").replaceAll("IRON_BOOTS", "§f철 부츠").replaceAll("CHAINMAIL_BOOTS", "§f사슬 부츠"));
								}
								} else {
								getConfig().set("착용2.신발", "§f미착용");
								}
								saveConfig();
								new BukkitRunnable(){
									Integer i = 10;
									@Override
									public void run(){
										if (i == 10){
											if (getConfig().get("알림") != null) {
										p.sendMessage(prefix + ChatColor.RED + i + "§f초 후 전투가 시작됩니다.");
										p.setWalkSpeed(0.2F);
										i--;
											return;
										}
										}
										if ((i >= 1) && (i <= 9)){
											if (getConfig().get("알림") != null) {
										p.sendMessage(prefix + ChatColor.RED + i + "§f초 후 전투가 시작됩니다.");
										i--;
											return;
										}
										}
										if (i == 0){
											if (getConfig().get("알림") != null) {
											p.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
											p.sendMessage(prefix + "전투가 시작되었습니다.");
											p.sendMessage(prefix + "전투는 1분안에 끝내셔야하며, 1분내로 끝나지 않을시에는 전투가 강제종료 됩니다.");
											p.setHealth(20);
											p.hidePlayer(player);
											p.showPlayer(player);
											player.hidePlayer(p);
											player.showPlayer(p);
											cancel();
											return;
										}
										}
										cancel();
										return;
									}
								}.runTaskTimer(this,0,20);
							}
						}
					}
					new BukkitRunnable(){
						Integer i = 70;
						@Override
						public void run(){
							if (i >= 6){
								if (getConfig().get("알림") != null) {
									time = i;
							i--;
								return;
							}
							}
							if ((i <= 5) && (i >= 1)) {
								if (getConfig().get("알림") != null) {
									for (String key : getConfig().getConfigurationSection("대전리스트").getKeys(false)){
									Bukkit.getPlayer(key).sendMessage(prefix + ChatColor.RED + i + "§f초 후 강제로 스폰으로 이동됩니다.");
									}
									time = i;
							i--;
								return;
							}
							}
							if (i == 0){
								if (getConfig().get("알림") != null) {
								for (String key : getConfig().getConfigurationSection("대전리스트").getKeys(false)){
									String World1 = getConfig().getString("스폰.World");
							    	double X1 = getConfig().getDouble("스폰.X");
							    	double Y1 = getConfig().getDouble("스폰.Y");
							    	double Z1 = getConfig().getDouble("스폰.Z");
							    	String Yaw1 = getConfig().getString("스폰.Yaw");
							    	String Pitch1 = getConfig().getString("스폰.Pitch");
							    	Location spawn = new Location(Bukkit.getWorld(World1), X1, Y1, Z1, Float.parseFloat(Yaw1), Float.parseFloat(Pitch1));
									Bukkit.getPlayer(key).teleport(spawn);
									Bukkit.getPlayer(key).setFireTicks(0);
								}
								Bukkit.broadcastMessage(prefix + "대전시간이 만료되어 전투가 강제종료 되었습니다.");
								getConfig().set("대전리스트", null);
								getConfig().set("알림", null);
								getConfig().set("착용1", null);
								getConfig().set("착용2", null);
							    PVP1 = false;
							    saveConfig();
							    time = 0;
								cancel();
								return;
							}
							}
							cancel();
							return;
						}
					}.runTaskTimer(this,0,20);
			    }
			    if (args[0].equalsIgnoreCase("거절")) {
			    	if (!getConfig().contains("보류." + player.getName())){
						player.sendMessage(prefix + "당신은 1:1 대전신청을 받은적이 없습니다.");
						return false;
					}
					String lead = getConfig().getString("보류." + player.getName() + ".대상");
					if (Bukkit.getPlayer(lead) == null){
					} else {
						Player data = Bukkit.getPlayer(lead);
						data.sendMessage(prefix + "§c"+ player.getName() + " §f가 당신의 1:1 대전신청을 거절했습니다.");
						player.sendMessage(prefix + "§c" + data.getName() + " §f의 1:1 대전신청을 거절했습니다.");
					}
					getConfig().set("보류." + player.getName(), null);
					saveConfig();
					return false;
			    }
			    if (args[0].equalsIgnoreCase("보기")) {
			    	if (!getConfig().contains("보류." + player.getName())){
						player.sendMessage(prefix + "당신은 1:1 대전신청을 받은적이 없습니다.");
						return false;
					}
			    	player.sendMessage(prefix + "대기중인 1:1 대전신청자 : §c" + getConfig().get("보류." + player.getName() + ".대상"));
			    	return false;
			    }
			    if (args[0].equalsIgnoreCase("남은시간")) {
			    	if (time == 0){
						player.sendMessage(prefix + "현재 아무도 대전중이지 않습니다.");
						return false;
					}
			    	player.sendMessage(prefix + "진행중인 대전 남은시간 : §c" + time + "§f초");
			    	return false;
			    }
			    if (args[0].equalsIgnoreCase("정보")) {
			    	if (time == 0){
						player.sendMessage(prefix + "현재 아무도 대전중이지 않습니다.");
						return false;
					}
			    	String a = getConfig().getString("착용1.이름");
			    	String b = getConfig().getString("착용2.이름");
			    	int id1 = Bukkit.getPlayer(a).getItemInHand().getTypeId();
			    	int id2 = Bukkit.getPlayer(b).getItemInHand().getTypeId();
			    	player.sendMessage("");
			    	player.sendMessage(prefix + "§c" + getConfig().get("착용1.이름") + " §f의 착용 정보 ■");
			    	player.sendMessage(prefix + "§e투구 : " + getConfig().get("착용1.투구"));
			    	player.sendMessage(prefix + "§e갑옷 : " + getConfig().get("착용1.갑옷"));
			    	player.sendMessage(prefix + "§e바지 : " + getConfig().get("착용1.바지"));
			    	player.sendMessage(prefix + "§e신발 : " + getConfig().get("착용1.신발"));
			    	if ((id1 == 267) || (id1 == 268) || (id1 == 272) || (id1 == 276) || (id1 == 283)) {
			    		if (Bukkit.getPlayer(a).getItemInHand().getItemMeta().getDisplayName() != null) {
				    		player.sendMessage(prefix + "§e검 : " + Bukkit.getPlayer(a).getItemInHand().getItemMeta().getDisplayName());
			    		} else {
			    			player.sendMessage(prefix + "§e검 : " + Bukkit.getPlayer(a).getItemInHand().getType().name().replaceAll("DIAMOND_SWORD", "§f다이아몬드 검").replaceAll("GOLD_SWORD", "§f금 검").replaceAll("STONE_SWORD", "§f돌 검").replaceAll("IRON_SWORD", "§f철 검").replaceAll("WOOD_SWORD", "§f나무 검"));
			    		}
			    	} else {
			    		player.sendMessage(prefix + "§e검 : §f검 종류만 표시됩니다.");
			    	}
			    	player.sendMessage("");
			    	player.sendMessage(prefix + "§c" + getConfig().get("착용2.이름") + " §f의 착용 정보 ■");
			    	player.sendMessage(prefix + "§e투구 : " + getConfig().get("착용2.투구"));
			    	player.sendMessage(prefix + "§e갑옷 : " + getConfig().get("착용2.갑옷"));
			    	player.sendMessage(prefix + "§e바지 : " + getConfig().get("착용2.바지"));
			    	player.sendMessage(prefix + "§e신발 : " + getConfig().get("착용2.신발"));
			    	if ((id2 == 267) || (id2 == 268) || (id2 == 272) || (id2 == 276) || (id2 == 283)) {
			    		if (Bukkit.getPlayer(b).getItemInHand().getItemMeta().getDisplayName() != null) {
				    		player.sendMessage(prefix + "§e검 : " + Bukkit.getPlayer(b).getItemInHand().getItemMeta().getDisplayName());
			    		} else {
			    			player.sendMessage(prefix + "§e검 : " + Bukkit.getPlayer(b).getItemInHand().getType().name().replaceAll("DIAMOND_SWORD", "§f다이아몬드 검").replaceAll("GOLD_SWORD", "§f금 검").replaceAll("STONE_SWORD", "§f돌 검").replaceAll("IRON_SWORD", "§f철 검").replaceAll("WOOD_SWORD", "§f나무 검"));
			    		}
			    	} else {
			    		player.sendMessage(prefix + "§e검 : §f검 종류만 표시됩니다.");
			    	}
			    	return false;
			    }
			    if (args[0].equalsIgnoreCase("전적")) {
			    	if (getConfig().contains("전적." + player.getName() + ".승")) {
			    		int a = getConfig().getInt("전적." + player.getName() + ".승");
			    		player.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
				    	player.sendMessage(prefix + "§e승리 : §a" + a);
			    	} else {
			    		int a = 0;
			    		player.sendMessage(prefix + "§e▒ 소망온라인 대전시스템");
			    		player.sendMessage(prefix + "§e승리 : §a" + a);
			    	}
			    	if (getConfig().contains("전적." + player.getName() + ".패")) {
			    		int b = getConfig().getInt("전적." + player.getName() + ".패");
			    		player.sendMessage(prefix + "§e패배 : §c" + b);
			    	} else {
			    		int b = 0;
			    		player.sendMessage(prefix + "§e패배 : §c" + b);
			    	}
			    	return false;
			    }
			    if (args[0].equalsIgnoreCase("허용")) {
			    	if (args.length != 2) {
				          player.sendMessage(prefix + "§c올바르지 않은 명령어. §f/대전 허용 <아이디>");
				          return false;
				        }
			    	if (!getConfig().contains("차단." + player.getName())) {
			    		player.sendMessage(prefix + "대전 차단상태에서만 사용 가능한 명령어입니다.");
			    		return false;
			    	}
				    if (Bukkit.getPlayer(args[1]) == null) {
				    	player.sendMessage(prefix + "§c해당 플레이어를 찾을 수 없습니다.");
				        return false;
				    }
				    if (player.getName() == Bukkit.getPlayer(args[1]).getName()) {
			            player.sendMessage(prefix + "자기 자신은 허용할 수 없습니다.");
			            return false;
			        }
				    if (getConfig().contains("차단." + player.getName() + "." + Bukkit.getPlayer(args[1]).getName())) {
			            player.sendMessage(prefix + "§c" + Bukkit.getPlayer(args[1]).getName() + " §f가 대전신청 허용목록에서 삭제되었습니다.");
			            getConfig().set("차단." + player.getName() + "." + Bukkit.getPlayer(args[1]).getName(), null);
			            saveConfig();
			            return false;
			        }
			    	player.sendMessage(prefix + "§c" + Bukkit.getPlayer(args[1]).getName() + " §f가 대전신청 허용목록에 추가되었습니다.");
			    	getConfig().set("차단." + player.getName() + "." + Bukkit.getPlayer(args[1]).getName(), 1);
			    	saveConfig();
			    	return false;
			    }
			    if (args[0].equalsIgnoreCase("차단")) {
			    	if (getConfig().contains("차단." + player.getName())) {
						player.sendMessage(prefix + "대전 차단이 §c해제§f되었습니다.");
						getConfig().set("차단." + player.getName(), null);
						saveConfig();
						return false;
					}
			    	player.sendMessage(prefix + "대전 차단이 §a설정§f되었습니다.");
			    	getConfig().set("차단." + player.getName(), 1);
			    	saveConfig();
			    	return false;
			    }
			}
			return true;
	  }
	
	@EventHandler
	  public void playerQuit(PlayerQuitEvent event) {
		  if (getConfig().contains("대전리스트." + event.getPlayer().getName())) {
			  Bukkit.broadcastMessage(prefix + "§c" + event.getPlayer().getName() + " §f님이 1:1 대전중 퇴장하여 죽었습니다.");
			  event.getPlayer().setHealth(0);
			  if (getConfig().contains("전적." + event.getPlayer().getName() + ".패")) {
				  int a = getConfig().getInt("전적." + event.getPlayer().getName() + ".패");
				  a++;
				  getConfig().set("전적." + event.getPlayer().getName() + ".패", a);
				  saveConfig();
			  } else {
				  getConfig().set("전적." + event.getPlayer().getName() + ".패", 1);
				  saveConfig();
			  }
			  getConfig().set("대전리스트." + event.getPlayer().getName(), null);
			  getConfig().set("알림", null);
			  getConfig().set("알림2", null);
			  getConfig().set("착용1", null);
			  getConfig().set("착용2", null);
			  time = 0;
					for (String key : getConfig().getConfigurationSection("대전리스트").getKeys(false)){
						if (Bukkit.getPlayer(key) != null){
							final Player p = Bukkit.getPlayer(key);
							new BukkitRunnable(){
								Integer i = 10;
								@Override
								public void run(){
									if (i == 10){
										if (getConfig().get("알림2") != null) {
										p.sendMessage(prefix + ChatColor.RED + i + "§f초 후 스폰으로 이동됩니다.");
										p.setFireTicks(0);
										i--;
											return;
										}
									}
									if ((i >= 1) && (i <= 9)){
										if (getConfig().get("알림2") != null) {
										p.sendMessage(prefix + ChatColor.RED + i + "§f초 후 스폰으로 이동됩니다.");
									i--;
										return;
									}
									}
									if (i == 0){
										if (getConfig().get("알림2") != null) {
											String World1 = getConfig().getString("스폰.World");
									    	double X1 = getConfig().getDouble("스폰.X");
									    	double Y1 = getConfig().getDouble("스폰.Y");
									    	double Z1 = getConfig().getDouble("스폰.Z");
									    	String Yaw1 = getConfig().getString("스폰.Yaw");
									    	String Pitch1 = getConfig().getString("스폰.Pitch");
									    	Location spawn = new Location(Bukkit.getWorld(World1), X1, Y1, Z1, Float.parseFloat(Yaw1), Float.parseFloat(Pitch1));
									    	p.teleport(spawn);
										if (getConfig().contains("전적." + p.getName() + ".승")) {
											  int a = getConfig().getInt("전적." + p.getName() + ".승");
											  a++;
											  getConfig().set("전적." + p.getName() + ".승", a);
											  saveConfig();
										  } else {
											  getConfig().set("전적." + p.getName() + ".승", 1);
											  saveConfig();
										  }
										getConfig().set("대전리스트", null);
									    PVP1 = false;
									    saveConfig();
										cancel();
										return;
									}
									}
									cancel();
									return;
								}
							}.runTaskTimer(this,0,20);

						}

					}
		  }
	  }
	  
	  @EventHandler
	  public void playerDeath(PlayerDeathEvent event) {
	    Player dead = event.getEntity();
	    if (getConfig().contains("대전리스트." + event.getEntity().getName())) {
	    	if (getConfig().contains("전적." + event.getEntity().getName() + ".패")) {
				  int a = getConfig().getInt("전적." + event.getEntity().getName() + ".패");
				  a++;
				  getConfig().set("전적." + event.getEntity().getName() + ".패", a);
				  saveConfig();
			  } else {
				  getConfig().set("전적." + event.getEntity().getName() + ".패", 1);
				  saveConfig();
			  }
	    	getConfig().set("대전리스트." + event.getEntity().getName(), null);
	    	getConfig().set("알림", null);
	    	getConfig().set("착용1", null);
			getConfig().set("착용2", null);
	    	time = 0;
	    	for (String key : getConfig().getConfigurationSection("대전리스트").getKeys(false)){
				if (Bukkit.getPlayer(key) != null){
					final Player p = Bukkit.getPlayer(key);
					Bukkit.broadcastMessage(prefix + "§c" + p.getName() + " §f님이 1:1 대전에서 §c" + dead.getName() + " §f님을 이겼습니다!");
			new BukkitRunnable(){
				Integer i = 10;
				@Override
				public void run(){
					if (i == 10){
						p.sendMessage(prefix + ChatColor.RED + i + "§f초 후 스폰으로 이동됩니다.");
						p.setFireTicks(0);
						i--;
							return;
						}
					if ((i >= 1) && (i <= 9)){
					p.sendMessage(prefix + ChatColor.RED + i + "§f초 후 스폰으로 이동됩니다.");
					i--;
						return;
					}
					if (i == 0){
						String World1 = getConfig().getString("스폰.World");
				    	double X1 = getConfig().getDouble("스폰.X");
				    	double Y1 = getConfig().getDouble("스폰.Y");
				    	double Z1 = getConfig().getDouble("스폰.Z");
				    	String Yaw1 = getConfig().getString("스폰.Yaw");
				    	String Pitch1 = getConfig().getString("스폰.Pitch");
				    	Location spawn = new Location(Bukkit.getWorld(World1), X1, Y1, Z1, Float.parseFloat(Yaw1), Float.parseFloat(Pitch1));
						p.teleport(spawn);
						if (getConfig().contains("전적." + p.getName() + ".승")) {
							  int a = getConfig().getInt("전적." + p.getName() + ".승");
							  a++;
							  getConfig().set("전적." + p.getName() + ".승", a);
							  saveConfig();
						  } else {
							  getConfig().set("전적." + p.getName() + ".승", 1);
							  saveConfig();
						  }
						getConfig().set("대전리스트", null);
						getConfig().set("알림", null);
					    PVP1 = false;
					    saveConfig();
						cancel();
						return;
					}
					cancel();
					return;
				}
			}.runTaskTimer(this,0,20);
				}
	    	}
	    }
	  }
	  
	  @EventHandler
	  public void playerStop(PlayerMoveEvent event) {
		  if (stop == true) {
	      if (getConfig().contains("대전리스트." + event.getPlayer().getName()))
	      {
	        event.getPlayer().teleport(event.getPlayer());
	      }
		  }
	    }
	  
		@SuppressWarnings("deprecation")
		@EventHandler
		public void ArmorTookUpEvent(InventoryClickEvent event)
		{
			Player player = Bukkit.getPlayer(event.getWhoClicked().getName());
			if (getConfig().contains("대전리스트." + event.getWhoClicked().getName()) && event.getCurrentItem().getType() != Material.AIR) {
				player.sendMessage(prefix + "§c부정행위를 방지하기 위해 대전중에는 아이템을 집을 수 없습니다.");
				event.setCancelled(true);
				player.updateInventory();
			}
		}
		  
		  @SuppressWarnings("deprecation")
		@EventHandler
		  public void onClickAction(PlayerInteractEvent event) { 
			  Player player = event.getPlayer();
			  int id = player.getItemInHand().getTypeId();
			  if (getConfig().contains("대전리스트." + event.getPlayer().getName())) {
				  if ((id >= 298) && (id <= 317)) {
					  event.setCancelled(true);
					  player.updateInventory();
					  return;
				  }
			  }
		  }
		  
		  @EventHandler(priority = EventPriority.HIGHEST)
		  public void onPlayerHealth(EntityDamageByEntityEvent event) {
			  if(event.getEntity() instanceof Player) {
			  Player player = (Player)event.getEntity();
		    if (protection && getConfig().contains("대전리스트." + player.getName())) {
		    	  event.setDamage(0);
		        event.setCancelled(true);
			  }
			  }
		  }
}
