package com.barleytea.fetchcodingexercise.app.ui.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.barleytea.fetchcodingexercise.R
import com.barleytea.fetchcodingexercise.data.fetch.ExerciseApi.*
import com.barleytea.fetchcodingexercise.model.ExerciseModel
import com.barleytea.fetchcodingexercise.model.State
import com.barleytea.fetchcodingexercise.ui.composables.ErrorScreen
import com.barleytea.fetchcodingexercise.ui.composables.LoadingIndicator
import com.barleytea.fetchcodingexercise.ui.composables.TextItem
import com.barleytea.fetchcodingexercise.ui.theme.basePadding
import com.barleytea.fetchcodingexercise.utils.require

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentScreen(
    homeScreenUiState: ExerciseModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    refreshAction: () -> Unit
) {
    when(homeScreenUiState.state) {
        State.SUCCESS -> {
            val list = homeScreenUiState.items
            LazyColumn(
                modifier = Modifier,
                contentPadding = contentPadding
            ) {
                stickyHeader {
                    Surface(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(horizontal = basePadding)
                            .heightIn(min = basePadding * 2f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextItem(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.list_id)
                            )

                            TextItem(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.name)
                            )
                            TextItem(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.id)
                            )
                        }
                    }

                }
                itemsIndexed(list) { idx, data ->
                    data.name?.let {
                        ItemCard(data = data)
                        Spacer(modifier = Modifier.padding(basePadding / 2))
                        if (idx != homeScreenUiState.items.lastIndex) {

                        }
                    }
                }
            }
        }

        State.LOADING -> {
            Box(
                Modifier.fillMaxSize()
            ) {
                LoadingIndicator()
            }
        }
        else -> {
            val isInternetError = homeScreenUiState.state == State.NO_INTERNET
            ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                icon = if(isInternetError) R.drawable.no_internet else null,
                errorMessage = if(isInternetError) stringResource(id = R.string.error_no_internet)
                                    else stringResource(id = R.string.error_unexpected),
                refreshAction = refreshAction
            )
        }

    }
}


@Composable
fun ItemCard(data: Data) {
    Card (
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .height(basePadding * 3)
            .padding(horizontal = basePadding)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextItem(
                modifier = Modifier.weight(1f),
                text = data.listId.toString()
            )
            TextItem(
                modifier = Modifier.weight(1f),
                text = data.name.require()
            )
            TextItem(
                modifier = Modifier.weight(1f),
                text = data.id
            )
        }
    }
}