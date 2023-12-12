package org.fastcampus.oruryapi.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.global.message.error.ErrorMessage;
import org.fastcampus.oruryapi.global.message.info.InfoMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class PostController {
    String str = InfoMessage.POST_CREATED.toString();
    String str2 = ErrorMessage.NO_POST.toString();
}
