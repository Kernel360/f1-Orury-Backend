package org.orury.client.gym.interfaces.request;

public record GymAreaRequest(
        float latitude,
        float longitude,
        AreaGrid areaGrid
) {
}

// TODO: 아래와 같이 구현하면, GymReaderImpl까지의 코드에서 AreaGrid말고 Map<String, Double>로 가져갈 수도 있습니다.
//    public record GymAreaRequest(
//            float latitude,
//            float longitude,
//            double bottomLatitude,
//            double topLatitude,
//            double leftLongitude,
//            double rightLongitude
//    ) {
//        public Map<String, Double> getGridMap() {
//            var gridMap = new HashMap<String, Double>();
//            gridMap.put("bottom", bottomLatitude);
//            gridMap.put("top", topLatitude);
//            gridMap.put("left", leftLongitude);
//            gridMap.put("right", rightLongitude);
//            return gridMap;
//        }
//    }