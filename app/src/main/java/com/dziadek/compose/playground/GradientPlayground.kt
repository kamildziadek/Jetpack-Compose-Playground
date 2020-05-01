package com.dziadek.compose.playground

import androidx.compose.Composable
import androidx.ui.core.DensityAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredWidth
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.px
import com.dziadek.compose.modifiers.GradientDirection
import com.dziadek.compose.modifiers.drawLinearGradientBackground

@Composable
@Preview
fun GradientPreview() {
    val size = with((DensityAmbient.current)) {
        100.px.toDp()
    }
    Column {
        GradientDirection.values().forEach {
            Box(
                modifier = Modifier.preferredWidth(size).preferredHeight(size).drawLinearGradientBackground(
                    colors = listOf(Color.Black, Color.White),
                    direction = it
                )
            )

        }
    }

}