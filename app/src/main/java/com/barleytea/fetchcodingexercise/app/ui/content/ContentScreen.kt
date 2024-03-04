package com.barleytea.fetchcodingexercise.app.ui.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.barleytea.fetchcodingexercise.R
import com.barleytea.fetchcodingexercise.data.fetch.ExerciseApi.Data
import com.barleytea.fetchcodingexercise.model.ExerciseModel
import com.barleytea.fetchcodingexercise.model.State
import com.barleytea.fetchcodingexercise.ui.composables.ErrorScreen
import com.barleytea.fetchcodingexercise.ui.composables.LoadingIndicator
import com.barleytea.fetchcodingexercise.ui.composables.TextItem
import com.barleytea.fetchcodingexercise.ui.theme.FetchCodingExerciseTheme
import com.barleytea.fetchcodingexercise.ui.theme.basePadding
import com.barleytea.fetchcodingexercise.utils.require

@Composable
fun ContentScreen(
    uiState: ExerciseModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    selectList: (Int) -> Unit,
    refreshAction: () -> Unit
) {
    when(uiState.state) {
        State.SUCCESS -> {
            SuccessScreen(
                uiState = uiState,
                contentPadding = contentPadding,
                selectList = selectList
            )
        }

        State.LOADING -> {
            Box(
                Modifier.fillMaxSize()
            ) {
                LoadingIndicator()
            }
        }
        else -> {
            val isInternetError = uiState.state == State.NO_INTERNET
            ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                icon = if(isInternetError) R.drawable.no_internet else null,
                errorMessage = if(isInternetError) stringResource(R.string.error_no_internet)
                                    else stringResource(R.string.error_unexpected),
                refreshAction = refreshAction
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuccessScreen(
    uiState: ExerciseModel,
    contentPadding: PaddingValues,
    selectList: (Int) -> Unit
) {
    var selected by remember { mutableIntStateOf(-1) }
    val scrollState = rememberScrollState()
    LazyColumn(
        modifier = Modifier,
        contentPadding = contentPadding
    ) {
        stickyHeader {
            Surface(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .heightIn(min = basePadding * 2f)
                    .wrapContentHeight()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = basePadding / 2)
                    ) {
                        uiState.listIds.forEach { listId ->
                            FilterChip(
                                modifier = Modifier
                                    .padding(horizontal = basePadding),
                                selected = listId == selected,
                                onClick = {
                                    if (selected == listId) {
                                        selected = -1
                                        selectList(-1)
                                    } else {
                                        selected = listId
                                        selectList(listId)
                                    }
                                },
                                label = {
                                    Text(
                                        text = stringResource(R.string.list_num, listId)
                                    )
                                },
                                leadingIcon = if (listId == selected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = basePadding),
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

        }
        itemsIndexed(uiState.selectedItems) { idx, data ->
            data.name?.let {
                ItemCard(data = data)
                Spacer(modifier = Modifier.padding(basePadding / 2))
                if (idx != uiState.items.lastIndex) {

                }
            }
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

@Preview(
    showBackground = true,
)
@Composable
fun ContentPreview() {
    FetchCodingExerciseTheme {
        ContentScreen(
            uiState = ExerciseModel(
                items = listOf(
                    Data("0", 1, "Item 0")
                ),
                selectedItems = listOf(
                    Data("0", 1, "Item 0")
                ),
                listIds = listOf(1),
                state = State.SUCCESS
            ),
            selectList = {}
        ){}
    }
}