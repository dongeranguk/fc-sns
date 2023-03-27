package com.fastcampus.fcsns.service;

import com.fastcampus.fcsns.exception.ErrorCode;
import com.fastcampus.fcsns.exception.SnsApplicationException;
import com.fastcampus.fcsns.model.entity.PostEntity;
import com.fastcampus.fcsns.model.entity.UserEntity;
import com.fastcampus.fcsns.repository.PostEntityRepository;
import com.fastcampus.fcsns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {

        // find user
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // save post
        postEntityRepository.save(new PostEntity());

        // return
    }

}
