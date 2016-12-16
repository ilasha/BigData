package com.ilashka.projects;

import com.ilashka.projects.model.User;
import com.ilashka.projects.model.WallPost;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallpostFull;

import java.util.Date;

public final class Deserializer  {
    public User deserializeUser(UserXtrCounters userXtrCounters) {
        User user = new User();
        user.setId(userXtrCounters.getId());
        user.setFirstName(userXtrCounters.getFirstName());
        user.setLastName(userXtrCounters.getLastName());
        if(userXtrCounters.getAbout() != null) user.setAbout(userXtrCounters.getAbout());
        if(userXtrCounters.getActivities() != null) user.setActivities(userXtrCounters.getActivities());
        if(userXtrCounters.getBdate() != null) user.setBdate(userXtrCounters.getBdate());
        if(userXtrCounters.getBooks() != null) user.setBooks(userXtrCounters.getBooks());
        if(userXtrCounters.getCareer() != null) user.setCareer(userXtrCounters.getCareer());
        if(userXtrCounters.getCity() != null) user.setCity(userXtrCounters.getCity().getTitle());
        if(userXtrCounters.getCountry() != null) user.setCountry(userXtrCounters.getCountry());
        if(userXtrCounters.getEducationForm() != null) user.setEducation(userXtrCounters.getEducationForm());
        if(userXtrCounters.getInterests() != null) user.setInterests(userXtrCounters.getInterests());
        if(userXtrCounters.getUniversities() != null) user.setUniversities(userXtrCounters.getUniversities());
        if(userXtrCounters.getNickname() != null) user.setNickname(userXtrCounters.getNickname());
        if(userXtrCounters.getSex() != null) user.setSex(userXtrCounters.getSex());
        if(userXtrCounters.getSchools() != null) user.setSchools(userXtrCounters.getSchools());
        if(userXtrCounters.getRelation() != null) user.setRelation(userXtrCounters.getRelation());
        if(userXtrCounters.getRelatives() != null) user.setRelatives(userXtrCounters.getRelatives());
        if(userXtrCounters.getMovies() != null) user.setMovie(userXtrCounters.getMovies());
        if(userXtrCounters.getMusic() != null) user.setMusic(userXtrCounters.getMusic());
        if(userXtrCounters.getPersonal() != null) user.setPersonal(userXtrCounters.getPersonal());
        if(userXtrCounters.getFollowersCount() != null) user.setFollowers(userXtrCounters.getFollowersCount());
        return user;
    }

    public WallPost deserializeWallPost(WallpostFull wallpostFull) {
        WallPost wallPost = new WallPost();
        wallPost.setId(wallpostFull.getId());
        wallPost.setDate(new Date(wallpostFull.getDate()));
        if(wallpostFull.getText() != null) {
            String text = wallpostFull.getText();
            wallPost.setText(text);
            text = text.replace('-', ' ');
            boolean flag = true;
            while (flag) {
                int findHashTag = text.indexOf('#');
                if(findHashTag != -1) {
                    int secondIndex;
                    if(text.indexOf(" ", findHashTag) < 0) {
                        secondIndex = text.length();
                        flag = false;
                    }
                    else {
                        secondIndex = text.indexOf(" ", findHashTag);
                    }
                    wallPost.addHashtag(text.substring(findHashTag, secondIndex));
                    text = text.substring(secondIndex);
                }
                else flag = false;
            }
        }
        return wallPost;
    }
}
