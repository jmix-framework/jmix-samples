package com.company.sociallogin.security;

import com.company.sociallogin.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserPersistence {

    @Autowired
    private DataManager dataManager;

    @Authenticated
    public User loadUserByGoogleId(String googleId) {
        return dataManager.load(User.class)
                .query("select u from User u where u.googleId = :googleId")
                .parameter("googleId", googleId)
                .optional()
                .orElseGet(() -> {
                    User user = dataManager.create(User.class);
                    user.setGoogleId(googleId);
                    return user;
                });
    }

    @Authenticated
    public User loadUserByGithubId(Integer githubId) {
        return dataManager.load(User.class)
                .query("select u from User u where u.githubId = :githubId")
                .parameter("githubId", githubId)
                .optional()
                .orElseGet(() -> {
                    User user = dataManager.create(User.class);
                    user.setGithubId(githubId);
                    return user;
                });
    }

    @Authenticated
    public User saveUser(User user) {
        return dataManager.save(user);
    }
}
