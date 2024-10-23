package com.noom.interview.fullstack.utils;

import com.noom.interview.fullstack.enums.Mood;
import com.noom.interview.fullstack.models.SleepLog;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculationUtil {

    public static Map<Mood, Long> calculateMoodFrequency(List<SleepLog> sleepLogs) {
        return sleepLogs.stream()
                .collect(Collectors.groupingBy(SleepLog::getMood, Collectors.counting()));
    }

    public static String calculateAverageSleepHours(List<SleepLog> sleepLogs) {
        if (sleepLogs.isEmpty()) {
            return formatDuration(Duration.ZERO);
        }

        long totalMinutes = sleepLogs.stream()
                .filter(log -> log.getSleepStart().isBefore(log.getSleepEnd()))
                .mapToLong(log -> Duration.between(log.getSleepStart(), log.getSleepEnd()).toMinutes())
                .sum();

        long averageMinutes = totalMinutes / sleepLogs.size();

        return formatDuration(Duration.ofMinutes(averageMinutes));
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%02d:%02d", hours, minutes);
    }

    public enum TimeType {
        BEDTIME,
        WAKEUP
    }

    public static LocalTime calculateAverageTime(List<LocalDateTime> sleepLogs, TimeType timeType) {
        if (sleepLogs.isEmpty()) {
            return null;
        }

        int totalMinutes = sleepLogs.stream()
                .mapToInt(log -> {
                    if (TimeType.BEDTIME.equals(timeType)) {
                        // If the time is before a threshold (e.g., 6 AM), treat it as the previous day.
                        return (log.getHour() < 6)
                                ? (log.getHour() + 24) * 60 + log.getMinute()
                                : log.getHour() * 60 + log.getMinute();
                    } else {
                        return log.getHour() * 60 + log.getMinute();
                    }
                })
                .sum();

        // Calculate average in minutes
        int averageMinutes = totalMinutes / sleepLogs.size();

        // Convert average minutes back to hours and minutes
        int averageHour = (averageMinutes % (24 * 60)) / 60; // Wrap around at 24 hours
        int averageMinute = averageMinutes % 60;

        // Return the average time as LocalTime
        return LocalTime.of(averageHour, averageMinute);
    }
}
