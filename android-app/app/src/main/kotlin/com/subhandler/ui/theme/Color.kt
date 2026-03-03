package com.subhandler.ui.theme

import androidx.compose.ui.graphics.Color

// Dark palette (matches web Tailwind slate scale)
val Background     = Color(0xFF020617)  // slate-950
val Surface        = Color(0xFF0F172A)  // slate-900
val SurfaceVariant = Color(0xFF1E293B)  // slate-800
val Outline        = Color(0xFF334155)  // slate-700
val OnBackground   = Color(0xFFF1F5F9)  // slate-100
val OnSurface      = Color(0xFFF1F5F9)  // slate-100
val OnSurfaceVar   = Color(0xFF94A3B8)  // slate-400
val Primary        = Color(0xFF6366F1)  // indigo-500
val OnPrimary      = Color(0xFFFFFFFF)

// Urgency colors
val UrgencyOverdue   = Color(0xFFEF4444)  // red-500
val UrgencyCritical  = Color(0xFFF97316)  // orange-500
val UrgencyWarning   = Color(0xFFEAB308)  // yellow-500
val UrgencyUpcoming  = Color(0xFF3B82F6)  // blue-500
val UrgencyOk        = Color(0xFF22C55E)  // green-500

// Preset subscription colors (10 options)
val PresetColors = listOf(
    Color(0xFF6366F1), // indigo
    Color(0xFFEC4899), // pink
    Color(0xFFF59E0B), // amber
    Color(0xFF22C55E), // green
    Color(0xFF06B6D4), // cyan
    Color(0xFF8B5CF6), // violet
    Color(0xFFEF4444), // red
    Color(0xFF14B8A6), // teal
    Color(0xFFF97316), // orange
    Color(0xFF64748B)  // slate
)

val PresetColorHexes = listOf(
    "#6366f1", "#ec4899", "#f59e0b", "#22c55e", "#06b6d4",
    "#8b5cf6", "#ef4444", "#14b8a6", "#f97316", "#64748b"
)
