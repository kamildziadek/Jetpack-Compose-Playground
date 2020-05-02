package com.dziadek.compose.animation

import androidx.animation.AnimatedFloat
import androidx.animation.AnimatedValue
import androidx.animation.AnimationBuilder
import androidx.animation.AnimationEndReason
import androidx.animation.AnimationVector
import androidx.animation.BaseAnimatedValue
import androidx.animation.PhysicsBuilder
import androidx.animation.TwoWayConverter
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.remember
import androidx.ui.animation.animatedFloat
import androidx.ui.animation.animatedValue

@Composable
fun animateFloat(
    init: Float,
    target: Float,
    builder: AnimationBuilder<Float> = remember { PhysicsBuilder() }
): AnimatedFloat = animatedFloat(initVal = init).apply {
    animateToEffect(targetValue = target, builder = builder)
}

@Composable
fun <T, V : AnimationVector> animateValue(
    from: T,
    to: T,
    converter: TwoWayConverter<T, V>,
    builder: AnimationBuilder<T> = remember { PhysicsBuilder() }
): AnimatedValue<T, V> = animatedValue(initVal = from, converter = converter).apply {
    animateToEffect(to, builder)
}

@Composable
fun <T, V : AnimationVector> BaseAnimatedValue<T, V>.animateToEffect(
    targetValue: T,
    builder: AnimationBuilder<T>,
    onEnd: ((AnimationEndReason, T) -> Unit)? = null
) {
    onCommit(targetValue) {
        animateTo(targetValue = targetValue, anim = builder, onEnd = onEnd)
    }
}
