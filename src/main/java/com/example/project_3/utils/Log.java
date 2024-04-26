package com.example.project_3.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.util.Collection;
import java.util.Map;

public class Log {
    private Log() {
        throw new AssertionError();
    }

    public static String getIndentation(int level) {
        return "    ".repeat(Math.max(0, level));
    }

    public static String format(Object obj, int level) {
        StringBuilder result = new StringBuilder();
        if (obj == null)
            return result.append("Null").toString();
        result.append(obj.getClass().getSimpleName()).append(": ");
        if (obj instanceof Map<?, ?> || obj instanceof Collection<?> || obj instanceof Object[]) {
            result.append("[");
            String items = formatItems(obj, level + 1);
            if (!items.isBlank())
                result.append('\n').append(items).append(getIndentation(level));
            result.append("]");
        } else {
            result.append("{").append(obj).append("}");
        }
        return result.toString();
    }

    public static String formatItems(Object obj, int level) {
        StringBuilder result = new StringBuilder();
        if (obj instanceof Map<?, ?>)
            result.append(formatMap((Map<?, ?>) obj, level));
        else if (obj instanceof Collection<?>)
            result.append(formatCollection((Collection<?>) obj, level));
        else if (obj instanceof Object[])
            result.append(formatArray((Object[]) obj, level));
        if (!result.toString().isBlank()) {
            result.setLength(result.length() - 2); // Remove the last ",\n"
            result.append('\n');
        }
        return result.toString();
    }

    public static String formatMap(Map<?, ?> map, int level) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            result.append(getIndentation(level))
                .append("-> Key: ").append(format(entry.getKey(), level)).append(", ")
                .append("Value: ").append(format(entry.getValue(), level)).append(",\n");
        }
        return result.toString();
    }

    public static String formatCollection(Collection<?> collection, int level) {
        StringBuilder result = new StringBuilder();
        for (Object item : collection) {
            result.append(getIndentation(level))
                .append(format(item, level))
                .append(",\n");
        }
        return result.toString();
    }

    public static String formatArray(Object[] array, int level) {
        StringBuilder result = new StringBuilder();
        for (Object item : array) {
            result.append(getIndentation(level))
                .append(format(item, level))
                .append(",\n");
        }
        return result.toString();
    }

    public static void log(String name, Level level, String... messages) {
        StringBuilder message = new StringBuilder("\n");
        for (String msg : messages)
            message.append(msg).append(",\n");
        if (messages.length != 0)
            message.setLength(message.length() - 2); // Remove the last ",\n"
        LogManager.getLogger(name).log(level, message.toString());
    }

    public static void log(Class<?> clazz, Level level, String... messages) {
        log(clazz.getSimpleName(), level, messages);
    }

    public static void log(Level level, String... messages) {
        log("Application", level, messages);
    }

    public static void log(String name, Level level, Object... objects) {
        StringBuilder message = new StringBuilder("\n");
        for (Object obj : objects)
            message.append(format(obj, 0)).append(",\n");
        if (objects.length != 0)
            message.setLength(message.length() - 2); // Remove the last ",\n"
        LogManager.getLogger(name).log(level, message.toString());
    }

    public static void log(Class<?> clazz, Level level, Object... objects) {
        log(clazz.getSimpleName(), level, objects);
    }

    public static void log(Level level, Object... objects) {
        log("Application", level, objects);
    }

    public static void trace(Class<?> clazz, String... messages) {
        log(clazz.getSimpleName(), Level.TRACE, messages);
    }

    public static void trace(String... messages) {
        log(Level.TRACE, messages);
    }

    public static void trace(String name, Object... objects) {
        log(name, Level.TRACE, objects);
    }

    public static void trace(Class<?> clazz, Object... objects) {
        log(clazz.getSimpleName(), Level.TRACE, objects);
    }

    public static void trace(Object... objects) {
        log(Level.TRACE, objects);
    }

    public static void debug(Class<?> clazz, String... messages) {
        log(clazz.getSimpleName(), Level.DEBUG, messages);
    }

    public static void debug(String... messages) {
        log(Level.DEBUG, messages);
    }

    public static void debug(String name, Object... objects) {
        log(name, Level.DEBUG, objects);
    }

    public static void debug(Class<?> clazz, Object... objects) {
        log(clazz.getSimpleName(), Level.DEBUG, objects);
    }

    public static void debug(Object... objects) {
        log(Level.DEBUG, objects);
    }

    public static void info(Class<?> clazz, String... messages) {
        log(clazz.getSimpleName(), Level.INFO, messages);
    }

    public static void info(String... messages) {
        log(Level.INFO, messages);
    }

    public static void info(String name, Object... objects) {
        log(name, Level.INFO, objects);
    }

    public static void info(Class<?> clazz, Object... objects) {
        log(clazz.getSimpleName(), Level.INFO, objects);
    }

    public static void info(Object... objects) {
        log(Level.INFO, objects);
    }

    public static void warn(Class<?> clazz, String... messages) {
        log(clazz.getSimpleName(), Level.WARN, messages);
    }

    public static void warn(String... messages) {
        log(Level.WARN, messages);
    }

    public static void warn(String name, Object... objects) {
        log(name, Level.WARN, objects);
    }

    public static void warn(Class<?> clazz, Object... objects) {
        log(clazz.getSimpleName(), Level.WARN, objects);
    }

    public static void warn(Object... objects) {
        log(Level.WARN, objects);
    }

    public static void error(Class<?> clazz, String... messages) {
        log(clazz.getSimpleName(), Level.ERROR, messages);
    }

    public static void error(String... messages) {
        log(Level.ERROR, messages);
    }

    public static void error(String name, Object... objects) {
        log(name, Level.ERROR, objects);
    }

    public static void error(Class<?> clazz, Object... objects) {
        log(clazz.getSimpleName(), Level.ERROR, objects);
    }

    public static void error(Object... objects) {
        log(Level.ERROR, objects);
    }

    public static void fatal(Class<?> clazz, String... messages) {
        log(clazz.getSimpleName(), Level.FATAL, messages);
    }

    public static void fatal(String... messages) {
        log(Level.FATAL, messages);
    }

    public static void fatal(String name, Object... objects) {
        log(name, Level.FATAL, objects);
    }

    public static void fatal(Class<?> clazz, Object... objects) {
        log(clazz.getSimpleName(), Level.FATAL, objects);
    }

    public static void fatal(Object... objects) {
        log(Level.FATAL, objects);
    }
}
