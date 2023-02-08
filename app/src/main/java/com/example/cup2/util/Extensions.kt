package com.example.cup2.util

import kotlin.math.roundToInt

fun List<Int>.toPercents(): List<Int> = this.map { (it.toDouble() / this.sum() * 100).roundToInt() }