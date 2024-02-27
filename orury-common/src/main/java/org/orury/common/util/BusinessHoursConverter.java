package org.orury.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessHoursConverter {
    public static LocalTime extractOpenTime(String businessHour) {
        try {
            String openTime = businessHour.split("-")[0];
            int openHour = Integer.parseInt(openTime.split(":")[0]);
            int openMinute = Integer.parseInt(openTime.split(":")[1]);
            return LocalTime.of(openHour, openMinute);
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            return LocalTime.MAX;
        }
    }

    public static LocalTime extractCloseTime(String businessHour) {
        try {
            String closeTime = businessHour.split("-")[1];
            int closeHour = Integer.parseInt(closeTime.split(":")[0]);
            int closeMinute = Integer.parseInt(closeTime.split(":")[1]);
            return LocalTime.of(closeHour, closeMinute);
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException | DateTimeException e) {
            return LocalTime.MAX;
        }
    }
}
