package dev.kalonic.randomitems;

import dev.kalonic.randomitems.commands.RandomItems;
import dev.kalonic.randomitems.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public final class Main extends JavaPlugin {

    private BukkitTask itemTask;

    private int mintime = 30;
    private int maxtime = 90;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        isLicence();
        new RandomItems(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void startTask() {
        // BukkitTask itemTask = new RandomItemTask(this).runTaskTimer(this, 0L, 30*20L);
        this.itemTask = (new BukkitRunnable() {
            public void run() {

                Collection<? extends Player> players = Bukkit.getOnlinePlayers();

                Iterator iterator = players.iterator();

                while(iterator.hasNext()) {

                    Material[] materials = Material.values();

                    int randint = ThreadLocalRandom.current().nextInt(0, materials.length - 1);
                    ItemStack randitem = new ItemStack(materials[randint]);

                    while(randitem.getType().name().contains("SPAWN_EGG")) {
                        randint = ThreadLocalRandom.current().nextInt(0, materials.length - 1);
                        randitem = new ItemStack(materials[randint]);
                    }



                    Player p = (Player) iterator.next();
                    if(p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(Utils.chat("&cYour inventory is full and you could not be given an item."));
                    } else {
                        p.getInventory().addItem(randitem);
                        p.sendMessage(Utils.chat("&aYou received a " + randitem.getType().name()));
                    }
                }

            }
        }).runTaskTimer(this, 0L, ThreadLocalRandom.current().nextLong(mintime*20L, maxtime*20L));
    }


    private void isLicence() {
        String key = this.getConfig().getString("licence-key");
        try{
            String url = "https://pastebin/raw/" + key;
            URLConnection openConnection = new URL(url).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            @SuppressWarnings("resource")
            Scanner scan = new Scanner((new InputStreamReader(openConnection.getInputStream())));
            while(scan.hasNextLine()){
                String firstline = scan.nextLine();
                if(firstline.contains("SeLYqH5Y6j")){
                    String customer = scan.nextLine();
                    this.getLogger().info("ThisPlugin has been successfully licenced. The user who purchased this product was " + customer + ".");
                    return;
                }
            }
        }catch(Exception e){

        }
        this.getLogger().info("This plugin was not successfully licenced. It has been disabled.");
        Bukkit.getPluginManager().disablePlugin(this);
        return;
    }






    public void stopTask() {
        if(this.itemTask != null) {
            this.itemTask.cancel();
            this.itemTask = null;
        }

    }
}