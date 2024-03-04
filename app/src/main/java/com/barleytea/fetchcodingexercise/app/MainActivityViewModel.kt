package com.barleytea.fetchcodingexercise.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barleytea.fetchcodingexercise.data.fetch.ExerciseApi.Data
import com.barleytea.fetchcodingexercise.data.fetch.ExerciseRepository
import com.barleytea.fetchcodingexercise.model.ExerciseModel
import com.barleytea.fetchcodingexercise.model.State
import com.barleytea.fetchcodingexercise.utils.require
import com.barleytea.networking.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ExerciseModel(
            items = emptyList(),
            listIds = emptyList(),
            selectedItems = emptyList(),
            state = State.LOADING
        )
    )
    val uiState: StateFlow<ExerciseModel> = _uiState.asStateFlow()

    init {
        loadData()
    }

    /**
     * Load the data from the repository and map it to the state.
     * Triggered at launch and when the user taps on the reload button
     */
    fun loadData() {
        // Show loading initially
        _uiState.update { it.copy( state = State.LOADING) }
        viewModelScope.launch {
            when (val response = exerciseRepository.getMainInfo()) {
                is ApiResponse.Success.WithContent -> {
                    val list = sort(handleEmptyOrNull(response.content))
                    _uiState.update {
                        it.copy(
                            state = State.SUCCESS,
                            items = list,
                            selectedItems = list,
                            selectedListId = -1,
                            listIds = list.map { data -> data.listId }.distinct()
                        )
                    }
                }
                is ApiResponse.Error.NoInternet -> {
                    _uiState.update {
                        it.copy(
                            state = State.NO_INTERNET
                        )
                    }
                }
                else -> {
                    _uiState.update {
                        it.copy(
                            state = State.ERROR
                        )
                    }
                }
            }
        }
    }

    fun selectList( selectedListId: Int ) {
        _uiState.update {
            it.copy(
                selectedListId = selectedListId,
                selectedItems = it.items.filter { data ->
                    if(selectedListId != ALL) {
                        return@filter data.listId == selectedListId
                    } else {
                        return@filter true
                    }
                }
            )
        }
    }
    private fun handleEmptyOrNull(list: List<Data>): List<Data> {
        return list.filter { !it.name.isNullOrEmpty() }
    }

    // Helper function to filter empty/null entry out and sort by the item name
    private fun sort(list: List<Data>): List<Data> {
        return list.sortedWith(compareBy<Data> { it.listId }
            .thenBy { it.name.require().split(" ").last().toIntOrNull() })
    }

    companion object {
        private const val ALL = -1;
    }

}
