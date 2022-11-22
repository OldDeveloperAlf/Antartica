package wtf.myles.hcfcore.managers;

import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.utils.Message;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Myles on 29/06/2015.
 */
public class AutoMessageManager extends BukkitRunnable {

    private List<Message> messageList = new ArrayList<Message>();
    private String prefix = "[Default]";
    int count = messageList.size();
    static AutoMessageManager messageManager = new AutoMessageManager();

    public void addMessage(String message) {
        Message message1 = new Message(ChatColor.translateAlternateColorCodes('&', message));
        this.messageList.add(message1);
        this.count = messageList.size();
        save(Main.getInstance().getMessageFile());
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void save(File file)
    {
        JSONObject baseObj = new JSONObject();

        baseObj.put("prefix", this.prefix);

        JSONArray array = new JSONArray();
        for (Message message : this.messageList)
        {
            JSONObject object = new JSONObject();
            object.put("message", message.getMessage());
            array.add(object);
        }
        baseObj.put("messages", array);
        try
        {
            FileUtils.write(file, (CharSequence) new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(baseObj.toString())));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void load(File file)
    {
        JSONParser parser = new JSONParser();
        try
        {

            FileReader reader = new FileReader(file);
            JSONObject object = (JSONObject)parser.parse(reader);

            setPrefix((String)object.get("prefix"));

            JSONArray array = (JSONArray)object.get("messages");
            for (Object obj : array)
            {
                JSONObject serializedMessage = (JSONObject)obj;

                String message = (String)serializedMessage.get("message");

                Message message1 = new Message(message);

                this.messageList.add(message1);
            }
            reader.close();

            setCount(this.messageList.size());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static AutoMessageManager getMessageManager() {
        return messageManager;
    }

    Random random = new Random();
    int num = 0;

    public void run()
    {
        if (this.messageList.size() == 0)
        {
            System.out.println("Messages is empty! Not broadcasting!");
        }
        else
        {
            if (this.num >= this.messageList.size()) {
                this.num = 0;
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.prefix + " " + ((Message) this.messageList.get(this.num)).getMessage()));
            this.num += 1;
        }
    }

    public List<Message> getMessages()
    {
        return this.messageList;
    }

    public String getPrefix()
    {
        return this.prefix;
    }
}
