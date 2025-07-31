package com.scorpio.portfoliotracker.ui.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.formatMoney(): String =
    NumberFormat.getNumberInstance(Locale("en", "IN")).format(this)