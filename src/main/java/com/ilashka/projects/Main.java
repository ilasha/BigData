package com.ilashka.projects;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ilashka.projects.crud.UserRepository;
import com.ilashka.projects.crud.WallPostRepository;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.queries.users.UserField;
import com.vk.api.sdk.queries.wall.WallGetFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WallPostRepository wallPostRepository;
    /*https://oauth.vk.com/authorize?client_id=5762460&display=page&redirect_uri=https://oauth.vk.com/blank.html
    &scope=manage&response_type=token&v=5.60*/
    private final int USER_ID = 22101689;
    private final String ACCESS_TOKEN = "9b93b7b9fdd9b82a0a417965eb296ee7ddb310708502682dfd38089372463c4edcd84ba69e40d595bcea4";
    private final int MAX_COUNT_WALL_POST = 100;
    private final int SLEEP_TIME = 5000;
    private final int MAX_COUNT_USERS = 1000;


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor actor = new UserActor(USER_ID, ACCESS_TOKEN);
        BufferedReader bufferedReaderWallPost = new BufferedReader(new FileReader("indexWallPost.txt"));
        BufferedReader bufferedReaderUsers = new BufferedReader(new FileReader("indexUsers.txt"));
        try {
            JsonObject jsonObjectWallPost;
            String str = bufferedReaderWallPost.readLine();
            if(str != null) jsonObjectWallPost = new JsonParser().parse(str).getAsJsonObject();
            else {
                jsonObjectWallPost = new JsonObject();
                jsonObjectWallPost.addProperty("indexWallPost", 0);
                jsonObjectWallPost.addProperty("group_id", -64818082);
            }
            bufferedReaderWallPost.close();

            Deserializer deserializer = new Deserializer();
            for(int i = jsonObjectWallPost.get("indexWallPost").getAsInt(); i < 10; i++) {
                FileWriter fileWriterWallPost = new FileWriter(new File("indexWallPost.txt"), false);
                System.out.println("i: " + i);
                jsonObjectWallPost.remove("indexWallPost");
                jsonObjectWallPost.addProperty("indexWallPost", i);
                fileWriterWallPost.write(jsonObjectWallPost.toString());
                List<WallpostFull> wallpostFulls = vk.wall().get(actor).ownerId(jsonObjectWallPost.get("group_id").
                        getAsInt()).filter(WallGetFilter.OWNER).count(MAX_COUNT_WALL_POST).
                        offset(MAX_COUNT_WALL_POST*i).execute().getItems();
                for (WallpostFull wallpostFull: wallpostFulls) {
                    wallPostRepository.save(deserializer.deserializeWallPost(wallpostFull));
                }
                fileWriterWallPost.close();
                Thread.sleep(SLEEP_TIME);
            }

            ArrayList<UserField> userFields = new ArrayList<>();
            userFields.add(UserField.PERSONAL);
            userFields.add(UserField.ABOUT);
            userFields.add(UserField.ACTIVITIES);
            userFields.add(UserField.BDATE);
            userFields.add(UserField.CAREER);
            userFields.add(UserField.CITY);
            userFields.add(UserField.COUNTRY);
            userFields.add(UserField.UNIVERSITIES);
            userFields.add(UserField.SEX);
            userFields.add(UserField.SCHOOLS);
            userFields.add(UserField.NICKNAME);
            userFields.add(UserField.EDUCATION);
            userFields.add(UserField.BOOKS);
            userFields.add(UserField.INTERESTS);
            userFields.add(UserField.PERSONAL);
            userFields.add(UserField.MUSIC);
            userFields.add(UserField.MOVIES);
            userFields.add(UserField.RELATION);
            userFields.add(UserField.RELATIVES);
            userFields.add(UserField.FOLLOWERS_COUNT);


            JsonObject jsonObjectUsers;
            String str1 = bufferedReaderUsers.readLine();
            if(str1 != null) jsonObjectUsers = new JsonParser().parse(str1).getAsJsonObject();
            else {
                jsonObjectUsers = new JsonObject();
                jsonObjectUsers.addProperty("indexUser", 0);
                jsonObjectUsers.addProperty("group_id", "88165975");
            }
            bufferedReaderUsers.close();

            for(int i = jsonObjectUsers.get("indexUser").getAsInt(); i < 10; i++) {
                FileWriter fileWriterUsers = new FileWriter(new File("indexUsers.txt"), false);
                System.out.println("i: " + i);
                jsonObjectUsers.remove("indexUser");
                jsonObjectUsers.addProperty("indexUser", i);
                fileWriterUsers.write(jsonObjectUsers.toString());
                List<UserXtrCounters> list = vk.users().get(actor).fields(userFields).userIds(vk.groups().
                        getMembers(actor).groupId(jsonObjectUsers.get("group_id").getAsString()).count(MAX_COUNT_USERS)
                        .offset(MAX_COUNT_USERS*i).execute().getItems().toString()).execute();
                for (UserXtrCounters userXtrCounters : list) {
                    userRepository.save(deserializer.deserializeUser(userXtrCounters));
                }
                fileWriterUsers.close();
                Thread.sleep(SLEEP_TIME);
            }

        }
        catch (ApiException | ClientException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
