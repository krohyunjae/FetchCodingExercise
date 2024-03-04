package com.barleytea.fetchcodingexercise.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.barleytea.fetchcodingexercise.R
import com.barleytea.fetchcodingexercise.ui.theme.basePadding


private const val ERROR_SCREEN_ICON = "Error screen icon"
private const val ERROR_SCREEN_RELOAD_ICON = "Error screen reload icon"
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String,
    @DrawableRes icon: Int? = null,
    isRefreshEnabled: Boolean = true,
    refreshAction: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                icon?.let {
                    Icon(
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(id = icon),
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        contentDescription = ERROR_SCREEN_ICON
                    )
                }
                Spacer(modifier = Modifier.size(basePadding * 2))
                TextItem(
                    modifier = Modifier.width(250.dp),
                    text = errorMessage
                )
                Spacer(modifier = Modifier.size(basePadding * 2))
                refreshAction?.let {
                    Icon(
                        modifier = Modifier
                            .padding(top = basePadding)
                            .size(48.dp)
                            .clip(shape = RoundedCornerShape(basePadding))
                            .align(Alignment.CenterHorizontally)
                            .clickable(
                                enabled = isRefreshEnabled,
                                onClick = refreshAction
                            ),
                        painter = painterResource(id = R.drawable.reload),
                        tint = if(isRefreshEnabled) Color.Blue else Color.Gray,
                        contentDescription = ERROR_SCREEN_RELOAD_ICON
                    )
                }
            }
        }
    }
}