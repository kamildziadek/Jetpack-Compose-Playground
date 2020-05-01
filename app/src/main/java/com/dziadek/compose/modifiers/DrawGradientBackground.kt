package com.dziadek.compose.modifiers

import android.graphics.LinearGradient
import androidx.ui.core.ContentDrawScope
import androidx.ui.core.DrawModifier
import androidx.ui.core.Modifier
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ColorStop
import androidx.ui.graphics.Outline
import androidx.ui.graphics.Paint
import androidx.ui.graphics.Shader
import androidx.ui.graphics.ShaderBrush
import androidx.ui.graphics.Shape
import androidx.ui.graphics.TileMode
import androidx.ui.graphics.drawOutline
import androidx.ui.graphics.toArgb
import androidx.ui.unit.PxSize
import androidx.ui.unit.toRect

fun Modifier.drawLinearGradientBackground(
    colors: List<Color>,
    @androidx.annotation.IntRange(from = 0, to = 360)
    angle: Int,
    tileMode: TileMode = TileMode.Clamp,
    shape: Shape = RectangleShape
): Modifier = this + DrawLinearGradientBackground(
    colors = colors,
    angle = angle,
    colorStops = null,
    tileMode = tileMode,
    shape = shape
)

fun Modifier.drawLinearGradientBackground(
    vararg colorStops: ColorStop,
    @androidx.annotation.IntRange(from = 0, to = 360)
    angle: Int,
    tileMode: TileMode = TileMode.Clamp,
    shape: Shape = RectangleShape
): Modifier = this + DrawLinearGradientBackground(
    colors = colorStops.map { it.second },
    angle = angle,
    colorStops = colorStops.map { it.first },
    tileMode = tileMode,
    shape = shape
)

@Suppress("NOTHING_TO_INLINE")
inline fun Modifier.drawLinearGradientBackground(
    colors: List<Color>,
    direction: GradientDirection,
    tileMode: TileMode = TileMode.Clamp,
    shape: Shape = RectangleShape
): Modifier = drawLinearGradientBackground(
    colors = colors,
    angle = direction.angle,
    tileMode = tileMode,
    shape = shape
)

@Suppress("NOTHING_TO_INLINE")
inline fun Modifier.drawLinearGradientBackground(
    vararg colorStops: ColorStop,
    direction: GradientDirection,
    tileMode: TileMode = TileMode.Clamp,
    shape: Shape = RectangleShape
): Modifier = drawLinearGradientBackground(
    colorStops = *colorStops,
    angle = direction.angle,
    tileMode = tileMode,
    shape = shape
)

enum class GradientDirection(val angle: Int) {
    TOP_TO_BOTTOM(0),
    TOP_RIGHT_TO_BOTTOM_LEFT(45),
    RIGHT_TO_LEFT(90),
    BOTTOM_RIGHT_TO_TOP_LEFT(135),
    BOTTOM_TO_TOP(180),
    BOTTOM_LEFT_TO_TOP_RIGHT(225),
    LEFT_TO_RIGHT(270),
    TOP_LEFT_TO_BOTTOM_RIGHT(315)
}

class DrawLinearGradientBackground(
    private val colors: List<Color>,
    @androidx.annotation.IntRange(from = 0, to = 360)
    angle: Int,
    private val colorStops: List<Float>? = null,
    private val tileMode: TileMode = TileMode.Clamp,
    private val shape: Shape = RectangleShape
) : DrawModifier {

    private val angle = angle % 360

    private var lastSize: PxSize? = null
    private var lastOutline: Outline? = null
    private var lastPaint: Paint? = null

    override fun ContentDrawScope.draw() {
        val paint = lastPaint.takeIf { lastSize == size } ?: createPaint(
            width = size.width.value,
            height = size.height.value
        )

        draw(paint)
    }

    /**
     * Implementation from [androidx.ui.foundation.DrawBackground]
     */
    private fun ContentDrawScope.draw(paint: Paint) {
        if (shape === RectangleShape) {
            // shortcut to avoid Outline calculation and allocation
            drawRect(size.toRect(), paint)
        } else {
            val localOutline =
                if (size == lastSize) lastOutline!! else shape.createOutline(size, this)
            drawOutline(localOutline, paint)
            lastOutline = localOutline
            lastSize = size
        }
        drawContent()
    }

    /**
     * Creates paint from given [width] and [height].
     * The calculation are based on a cartesian plane where the center is the center of the shape described by [width] & [height].
     * After the calculation it converts the values to the Android coordinates system.
     */
    private fun createPaint(width: Float, height: Float): Paint {
        //convert angle to quarter in cartesian plane
        val quarter = Quarter.fromAngle(angle)

        val baseAngle = calculateBaseAngle(quarter)

        //calculate x,y of gradient in a cartesian plane, were center is the center of the rectangle
        val xBase = (baseAngle / 45f * width / 2).coerceIn(0f, width / 2) * quarter.xSign
        val yBase = ((90 - baseAngle) / 45f * height / 2).coerceIn(0f, height / 2) * quarter.ySign

        //convert from the cartesian plane to the Android representation
        val x0 = (xBase + width / 2).coerceIn(0f, width)
        val y0 = (yBase - height / 2).times(-1).coerceIn(0f, height)

        //x1 & y1 are always on the other side of the rectangle
        val x1 = width - x0
        val y1 = height - y0

        //create brush
        val brush = ShaderBrush(
            Shader(
                LinearGradient(
                    x0,
                    y0,
                    x1,
                    y1,
                    colors.toIntArray(),
                    colorStops?.toFloatArray(),
                    tileMode.nativeTileMode
                )
            )
        )
        //create paint
        return Paint().also {
            brush.applyTo(it)
        }
    }

    /**
     * Calculate angle representation in the First Quarter
     */
    private fun calculateBaseAngle(quarter: Quarter): Int {
        //calculate the angle in a given quarter
        val normalizedAngle = angle - quarter.number * 90

        //negate it if necessary
        return when (quarter) {
            Quarter.SECOND, Quarter.FOURTH -> 90 - normalizedAngle
            else -> normalizedAngle
        }
    }

    private fun List<Color>.toIntArray(): IntArray = IntArray(size) { i -> this[i].toArgb() }

    private enum class Quarter(
        val number: Int,
        val range: IntRange,
        val isXPositive: Boolean,
        val isYPositive: Boolean
    ) {

        FIRST(number = 0, range = 0..90, isXPositive = true, isYPositive = true),
        SECOND(number = 1, range = 90..180, isXPositive = true, isYPositive = false),
        THIRD(number = 2, range = 180..270, isXPositive = false, isYPositive = false),
        FOURTH(number = 3, range = 180..360, isXPositive = false, isYPositive = true);

        val xSign = if (isXPositive) 1 else -1
        val ySign = if (isYPositive) 1 else -1

        companion object {
            fun fromAngle(angle: Int): Quarter {
                return values().first { angle in it.range }
            }
        }
    }
}