package org.fastcampus.orurycommon.util;

import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUrlConverter {
    public static String convertListToString(List<String> imageList) {
        if (imageList == null || imageList.isEmpty()) return "";
        return String.join(",", imageList);
    }

    public static List<String> convertStringToList(String imagesString) {
        if (imagesString == null || imagesString.isEmpty()) return List.of();
        return Arrays.stream(imagesString.split(","))
                .toList();
    }

    public static String splitUrlToImage(String url) {
        if (Objects.isNull(url) || url.isEmpty()) return Strings.EMPTY;
        return Arrays.stream(url.split("/"))
                .reduce((first, second) -> second)
                .orElse("");
    }
}
