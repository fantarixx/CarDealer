package org.example;

import org.example.domain.common.Color;
import org.example.domain.common.exception.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class ColorTest {

    @Test
    void shouldCreateColorWithValidRgb() {
        Color color = new Color(100, 150, 200);
        assertThat(color.toString()).isEqualTo("#FF6496C8");
    }

    @Test
    void shouldCreateColorWithValidArgb() {
        Color color = new Color(100, 150, 200, 128);
        assertThat(color.toString()).isEqualTo("#806496C8");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 256})
    void shouldThrowExceptionWhenRedOutOfRange(int invalidRed) {
        assertThatThrownBy(() -> new Color(invalidRed, 0, 0))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Color must be between 0 and 255.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 256})
    void shouldThrowExceptionWhenGreenOutOfRange(int invalidGreen) {
        assertThatThrownBy(() -> new Color(0, invalidGreen, 0))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Color must be between 0 and 255.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 256})
    void shouldThrowExceptionWhenBlueOutOfRange(int invalidBlue) {
        assertThatThrownBy(() -> new Color(0, 0, invalidBlue))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Color must be between 0 and 255.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 256})
    void shouldThrowExceptionWhenAlphaOutOfRange(int invalidAlpha) {
        assertThatThrownBy(() -> new Color(0, 0, 0, invalidAlpha))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Color must be between 0 and 255.");
    }

    @Test
    void shouldCreateColorFromHex6() {
        Color color = new Color("#FF5733");
        assertThat(color.toString()).isEqualTo("#FFFF5733");
    }

    @Test
    void shouldCreateColorFromHex6WithoutHash() {
        Color color = new Color("FF5733");
        assertThat(color.toString()).isEqualTo("#FFFF5733");
    }

    @Test
    void shouldCreateColorFromHex8() {
        Color color = new Color("#80FF5733");
        assertThat(color.toString()).isEqualTo("#80FF5733");
    }

    @Test
    void shouldCreateColorFromHex8WithoutHash() {
        Color color = new Color("80FF5733");
        assertThat(color.toString()).isEqualTo("#80FF5733");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "#FFF",
            "FFF",
            "#GGBBAA",
            "FFGGAA",
            "#12345",
            "#FF5733FFG",
            ""
    })
    void shouldThrowExceptionWhenHexStringInvalid(String invalidHex) {
        assertThatThrownBy(() -> new Color(invalidHex))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("Invalid hex color format");
    }

    @Test
    void shouldBeEqualWhenSameComponents() {
        Color color1 = new Color(10, 20, 30, 40);
        Color color2 = new Color(10, 20, 30, 40);
        assertThat(color1).isEqualTo(color2);
        assertThat(color1.hashCode()).isEqualTo(color2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentComponents() {
        Color color1 = new Color(10, 20, 30);
        Color color2 = new Color(10, 20, 31);
        assertThat(color1).isNotEqualTo(color2);
    }

    @Test
    void shouldNotBeEqualWithNull() {
        Color color = new Color(0, 0, 0);
        assertThat(color).isNotEqualTo(null);
    }

    @Test
    void shouldNotBeEqualWithDifferentClass() {
        Color color = new Color(0, 0, 0);
        assertThat(color).isNotEqualTo("some string");
    }

    @Test
    void toStringShouldReturnHexWithAlpha() {
        Color color = new Color(100, 150, 200, 128);
        assertThat(color.toString()).isEqualTo("#806496C8");
    }

    @Test
    void toStringForOpaqueColorShouldIncludeFFAlpha() {
        Color color = new Color(100, 150, 200);
        assertThat(color.toString()).isEqualTo("#FF6496C8");
    }

    @Test
    void colorShouldBeImmutable() {
        Color color = new Color(10, 20, 30);
        assertThat(color.getClass().getDeclaredFields())
                .allMatch(field -> java.lang.reflect.Modifier.isFinal(field.getModifiers()));
    }
}