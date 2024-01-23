package org.fastcampus.orurycommon.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperatingTimeConverter {
    public static LocalTime extractOpenTime(String operatingTime) {
        String openTime = operatingTime.split("~")[0];
        int openHour = Integer.parseInt(openTime.split(":")[0]);
        int openMinute = Integer.parseInt(openTime.split(":")[1]);
        return LocalTime.of(openHour, openMinute);
    }

    public static LocalTime extractCloseTime(String operatingTime) {
        String closeTime = operatingTime.split("~")[1];
        int closeHour = Integer.parseInt(closeTime.split(":")[0]);
        int closeMinute = Integer.parseInt(closeTime.split(":")[1]);
        return LocalTime.of(closeHour, closeMinute);
    }
}
