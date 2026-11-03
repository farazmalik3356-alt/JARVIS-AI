package com.jarvis.ai.ui.theme

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** Cheap sanity test for the palette so a wrong hex doesn't slip through. */
class ThemeSmokeTest {
    @Test
    fun palette_matchesDesignTokens() {
        assertThat(VoidBlack.value).isEqualTo(0xFF0A0C10)
        assertThat(NeonCyan.value).isEqualTo(0xFF00F0FF)
        assertThat(ElectricViolet.value).isEqualTo(0xFF7C3AED)
    }
}