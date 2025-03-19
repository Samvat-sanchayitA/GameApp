package com.example.gameapp

import androidx.compose.ui.graphics.Color


// ColorFamily data class
data class ColorFamily(
    val color: Color,
    val name: String // Optional: Add a name for the color if needed
)

// extendedLight object
object extendedLight {
    val deepRed = ColorFamily(Color(0xFFD32F2F), "Crimson")
    val deepOrange = ColorFamily(Color(0xFFE64A19), "Coral")
    val deepGreen = ColorFamily(Color(0xFF388E3C), "Emerald")
    val deepTeal = ColorFamily(Color(0xFF00796B), "Turquoise")
    val deepBlue = ColorFamily(Color(0xFF1976D2), "Azure")
    val deepIndigo = ColorFamily(Color(0xFF3F51B5), "Indigo")
    val deepPurple = ColorFamily(Color(0xFF512DA8), "Violet")
}

// extendedDark object
object extendedDark {
    val deepRed = ColorFamily(Color(0xFFEF9A9A), "Rose")
    val deepOrange = ColorFamily(Color(0xFFFFCC80), "Peach")
    val deepGreen = ColorFamily(Color(0xFFA5D6A7), "Mint")
    val deepTeal = ColorFamily(Color(0xFF80CBC4), "Aqua")
    val deepBlue = ColorFamily(Color(0xFF81D4FA), "Sky Blue")
    val deepIndigo = ColorFamily(Color(0xFF9FA8DA), "Lavender")
    val deepPurple = ColorFamily(Color(0xFFB39DDB), "Lilac")
}