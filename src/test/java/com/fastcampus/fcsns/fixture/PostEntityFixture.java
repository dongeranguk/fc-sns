package com.fastcampus.fcsns.fixture;

import com.fastcampus.fcsns.model.entity.PostEntity;
import com.fastcampus.fcsns.model.entity.UserEntity;
import org.springframework.data.domain.Page;

public class PostEntityFixture {

    public static PostEntity get(String userName, Integer postId, Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);

        return result;
    }
}
