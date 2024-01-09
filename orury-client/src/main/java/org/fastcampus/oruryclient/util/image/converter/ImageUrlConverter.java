package org.fastcampus.oruryclient.util.image.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImageUrlConverter {
    public static List<String> convertToList(String imagesString) {
        String[] imageArray = imagesString.split(",");

        List<String> imageList = Arrays.asList(imageArray);

        return imageList;
    }

    public static String convertToString(List<String> imageList) {
        String imagesString = imageList.stream().collect(Collectors.joining(","));

        return imagesString;
    }

}
