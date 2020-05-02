package com.dziadek.compose.playground

import androidx.animation.Infinite
import androidx.animation.IntPropKey
import androidx.animation.LinearEasing
import androidx.animation.transitionDefinition
import androidx.compose.Composable
import androidx.ui.animation.Transition
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.graphics.Color
import androidx.ui.layout.Stack
import androidx.ui.layout.aspectRatio
import androidx.ui.layout.fillMaxSize
import androidx.ui.tooling.preview.Preview
import com.dziadek.compose.modifiers.drawLinearGradientBackground

private val angle = IntPropKey()
private val angleRotationTransitionDefinition = transitionDefinition {
    state(0) { this[angle] = 0 }
    state(1) { this[angle] = 360 }

    transition(0 to 1) {
        angle using repeatable {
            animation = tween {
                duration = 500
                easing = LinearEasing
            }
            iterations = Infinite
        }
    }
}

@Composable
@Preview
fun GradientAngleTransition() {
    Transition(definition = angleRotationTransitionDefinition, initState = 0, toState = 1) { state ->
        GradientBox(angle = state[angle], colors = listOf(Color.Red, Color.Yellow))
    }
}

@Composable
private fun GradientBox(angle: Int, colors: List<Color>) {
    Stack(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .aspectRatio(16 / 9f)
                .gravity(Alignment.Center)
                .drawLinearGradientBackground(
                    colors = colors,
                    angle = angle
                )
        )
    }
}
