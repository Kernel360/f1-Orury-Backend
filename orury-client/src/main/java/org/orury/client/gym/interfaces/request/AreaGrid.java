package org.orury.client.gym.interfaces.request;

import java.util.HashMap;
import java.util.Map;

public record AreaGrid(
        double bottomLatitude,
        double topLatitude,
        double leftLongitude,
        double rightLongitude
) {
    public Map<String, Double> toGridMap() {
        Map<String, Double> gridMap = new HashMap<>();
        gridMap.put("bottom", bottomLatitude);
        gridMap.put("top", topLatitude);
        gridMap.put("left", leftLongitude);
        gridMap.put("right", rightLongitude);
        return gridMap;
    }
}

// TODO: 프론트에서 double로 값을 요청하지 못하거나 연산이 더 든다면, String으로 받아 아래 코드로 대체합니다.
//    Map<String, Double> gridMap = new HashMap<>();
//    try {
//        gridMap.put("bottom", Double.parseDouble(bottomLatitude));
//        gridMap.put("top", Double.parseDouble(topLatitude));
//        gridMap.put("left", Double.parseDouble(leftLongitude));
//        gridMap.put("right", Double.parseDouble(rightLongitude));
//    } catch (NullPointerException | NumberFormatException exception) {
//        throw new BusinessException(GymErrorCode.INVALID_GRID_TYPE);
//    }
//    return gridMap;
