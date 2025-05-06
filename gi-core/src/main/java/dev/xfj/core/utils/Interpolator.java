package dev.xfj.core.utils;

import dev.xfj.core.services.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class Interpolator {
    private static final Logger log = LoggerFactory.getLogger(Interpolator.class);
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{(\\w+):(\\w+)\\}");
    private final Map<String, Function<Object, String>> typeHandlers;

    public Interpolator() {
        this.typeHandlers = Map.of(
                "P", value -> formatValue(value, "P"),
                "F1P", value -> formatValue(value, "F1P"),
                "F2P", value -> formatValue(value, "F2P"),
                "F1", value -> formatValue(value, "F1"),
                "I", value -> formatValue(value, "I")
        );
    }

    public String interpolate(String inputString, List<?> data) {
        try {
            Matcher matcher = PARAMETER_PATTERN.matcher(inputString);
            StringBuilder result = new StringBuilder();

            while (matcher.find()) {
                String parameter = matcher.group(1);
                String type = matcher.group(2);
                int index = parseIndex(parameter);

                if (index < 0 || index >= data.size()) {
                    log.error(format("Invalid index for: %s (%s)", inputString, type));
                    return "";
                }

                Object value = data.get(index);

                if (value == null) {
                    log.error(format("No value found for: %s (%s)", inputString, type));
                    return "";
                }

                Function<Object, String> handler = typeHandlers.get(type);

                if (handler == null) {
                    log.error(format("No type handler for: %s (%s) (%s)", type, value, inputString));
                    return "";
                }

                matcher.appendReplacement(result, handler.apply(value));
            }

            matcher.appendTail(result);

            return result.toString();
        } catch (Exception e) {
            log.error("Failed for: " + inputString);
            throw new RuntimeException(e.getMessage());
        }
    }

    private int parseIndex(String parameter) {
        try {
            return Integer.parseInt(parameter.replaceAll("\\D", "")) - 1;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String formatValue(Object value, String type) {
        if (value instanceof Number) {
            return switch (type) {
                case "P" -> format("%.0f%%", ((Number) value).doubleValue() * 100);
                case "F1P" -> format("%.1f%%", ((Number) value).doubleValue() * 100);
                case "F2P" -> format("%.2f%%", ((Number) value).doubleValue() * 100);
                case "F1" -> format("%.1f", value);
                case "I" -> format("%.0f", value);
                default -> "Invalid type: " + type;
            };
        }

        log.error("Not a number!");
        return "";
    }
}
