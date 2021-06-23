package com.oldturok.turok;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.util.UUIDTypeAdapter;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.Settings;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class TurokFriends {
   public static final TurokFriends INSTANCE = new TurokFriends();
   public static Setting<ArrayList<TurokFriends.Friend>> list_friends;
   public static Minecraft mc = Minecraft.func_71410_x();
   public static int error;

   public static void init_friends_list() {
      list_friends = Settings.custom("Friends", new ArrayList(), new TurokFriends.FriendListConverter()).buildAndRegister("friends");
   }

   public static Boolean is_friend(String name) {
      return ((ArrayList)list_friends.getValue()).stream().anyMatch((friend) -> {
         return friend.user_name.equalsIgnoreCase(name);
      });
   }

   public static TurokFriends.Friend add_friend(String name) {
      ArrayList<NetworkPlayerInfo> info_map = new ArrayList(mc.func_147114_u().func_175106_d());
      NetworkPlayerInfo profile = (NetworkPlayerInfo)info_map.stream().filter((networkPlayerInfo) -> {
         return networkPlayerInfo.func_178845_a().getName().equalsIgnoreCase(name);
      }).findFirst().orElse((Object)null);
      if (profile != null) {
         TurokFriends.Friend friend_ = new TurokFriends.Friend(profile.func_178845_a().getName(), profile.func_178845_a().getId());
         return friend_;
      } else {
         String uuid_name = request_uuid("[\"" + name + "\"]");
         if (uuid_name != null && !uuid_name.isEmpty()) {
            JsonElement element = (new JsonParser()).parse(uuid_name);
            if (element.getAsJsonArray().size() == 0) {
               ++error;
            } else {
               try {
                  String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                  String user = element.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
                  TurokFriends.Friend friend = new TurokFriends.Friend(user, UUIDTypeAdapter.fromString(id));
                  return friend;
               } catch (Exception var8) {
                  var8.printStackTrace();
                  ++error;
               }
            }
         } else {
            ++error;
         }

         return null;
      }
   }

   public static String request_uuid(String data) {
      try {
         String query = "https://api.mojang.com/profiles/minecraft";
         URL url = new URL(query);
         HttpURLConnection connect = (HttpURLConnection)url.openConnection();
         connect.setConnectTimeout(5000);
         connect.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
         connect.setDoOutput(true);
         connect.setDoInput(true);
         connect.setRequestMethod("POST");
         OutputStream output = connect.getOutputStream();
         output.write(data.getBytes("UTF-8"));
         output.close();
         InputStream input = new BufferedInputStream(connect.getInputStream());
         String return_ = convert_to(input);
         input.close();
         connect.disconnect();
         return return_;
      } catch (Exception var8) {
         return null;
      }
   }

   public static String convert_to(InputStream input) {
      Scanner scanned = (new Scanner(input)).useDelimiter("\\A");
      String resp = scanned.hasNext() ? scanned.next() : "/";
      return resp;
   }

   public static class Friend {
      String user_name;
      UUID uuid;

      public Friend(String user_name, UUID uuid) {
         this.user_name = user_name;
         this.uuid = uuid;
      }

      public String get_user_name() {
         return this.user_name;
      }
   }

   public static class FriendListConverter extends Converter<ArrayList<TurokFriends.Friend>, JsonElement> {
      protected JsonElement doForward(ArrayList<TurokFriends.Friend> list) {
         StringBuilder present = new StringBuilder();
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            TurokFriends.Friend friend = (TurokFriends.Friend)var3.next();
            present.append(String.format("%s;%s$", friend.user_name, friend.uuid.toString()));
         }

         return new JsonPrimitive(present.toString());
      }

      protected ArrayList<TurokFriends.Friend> doBackward(JsonElement jsonElement) {
         String string = jsonElement.getAsString();
         String[] pairs = string.split(Pattern.quote("$"));
         ArrayList<TurokFriends.Friend> friends = new ArrayList();
         String[] var5 = pairs;
         int var6 = pairs.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String pair = var5[var7];

            try {
               String[] split = pair.split(";");
               String user_name = split[0];
               UUID uuid = UUID.fromString(split[1]);
               friends.add(new TurokFriends.Friend(user_name_uuid(uuid, user_name), uuid));
            } catch (Exception var12) {
            }
         }

         return friends;
      }

      public static String user_name_uuid(UUID uuid, String saved) {
         String src = get_source("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
         if (src != null && !src.isEmpty()) {
            try {
               JsonElement object = (new JsonParser()).parse(src);
               return object.getAsJsonObject().get("name").getAsString();
            } catch (Exception var4) {
               var4.printStackTrace();
               System.err.println(src);
               return saved;
            }
         } else {
            return saved;
         }
      }

      public static String get_source(String link) {
         try {
            URL url = new URL(link);
            URLConnection connect = url.openConnection();
            BufferedReader buffer_read = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            StringBuilder buffer = new StringBuilder();

            String input;
            while((input = buffer_read.readLine()) != null) {
               buffer.append(input);
            }

            buffer_read.close();
            return buffer.toString();
         } catch (Exception var6) {
            return null;
         }
      }
   }
}
