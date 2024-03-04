package com.barleytea.fetchcodingexercise.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barleytea.fetchcodingexercise.data.fetch.ExerciseApi.*
import com.barleytea.fetchcodingexercise.data.fetch.ExerciseRepository
import com.barleytea.fetchcodingexercise.model.ExerciseModel
import com.barleytea.fetchcodingexercise.model.State
import com.barleytea.fetchcodingexercise.utils.require
import com.barleytea.networking.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseModel(items = emptyList(), state = State.LOADING))
    val uiState: StateFlow<ExerciseModel> = _uiState.asStateFlow()

    init {
        reload()
    }


    fun reload() {
        _uiState.update { it.copy( state = State.LOADING ) }
        viewModelScope.launch {
            when (val response = exerciseRepository.getMainInfo()) {
                is ApiResponse.Success.WithContent -> {
                    _uiState.update {
                        it.copy(
                            state = State.SUCCESS,
                            items = sort(response.content)
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

    private fun handleEmptyOrNull(list: List<Data>): List<Data> {
        return list.filter { !it.name.isNullOrEmpty() }
    }

    private fun sort(list: List<Data>): List<Data> {
        return handleEmptyOrNull(list).sortedWith(compareBy<Data> { it.listId }
            .thenBy { it.name.require().split(" ").last().toIntOrNull() })
    }

}
