package org.example.domain.common;

import org.example.domain.common.exception.DomainValidationException;

import java.util.Objects;

public class Color {
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public Color(int red, int green, int blue, int alpha) {
        validateComponent(red);
        validateComponent(green);
        validateComponent(blue);
        validateComponent(alpha);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(String color) {
        String clean = color.trim();
        if (clean.startsWith("#")) {
            clean = clean.substring(1);
        }

        try {
            if (clean.length() == 6) {
                int rgb = Integer.parseInt(clean, 16);
                red = (rgb >> 16) & 0xFF;
                green = (rgb >> 8) & 0xFF;
                blue = rgb & 0xFF;
                alpha = 255;
            } else if (clean.length() == 8) {
                long argb = Long.parseLong(clean, 16);
                alpha = (int) ((argb >> 24) & 0xFF);
                red = (int) ((argb >> 16) & 0xFF);
                green = (int) ((argb >> 8) & 0xFF);
                blue = (int) (argb & 0xFF);
            } else {
                throw new DomainValidationException("Invalid hex color format. Expected #RRGGBB or #RRGGBBAA");
            }
        } catch (NumberFormatException e) {
            throw new DomainValidationException("Invalid hex color format. Expected #RRGGBB or #RRGGBBAA");
        }
    }

    private void validateComponent(int value) {
        if (value < 0 || value > 255) {
            throw new DomainValidationException("Color must be between 0 and 255.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Color color = (Color) o;
        return red == color.red && green == color.green && blue == color.blue && alpha == color.alpha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return String.format("#%02X%02X%02X%02X", alpha, red, green, blue);
    }
}
