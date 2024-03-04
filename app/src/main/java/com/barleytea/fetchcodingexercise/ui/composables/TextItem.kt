package com.barleytea.fetchcodingexercise.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

/**
 * Text item for column/row
 */
@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    text: String
) {
    return Text(
        modifier = modifier,
        textAlign = textAlign,
        text = text
    )
}