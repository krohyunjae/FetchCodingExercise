package com.barleytea.fetchcodingexercise.model

import com.barleytea.fetchcodingexercise.data.fetch.ExerciseApi.Data

data class ExerciseModel(
    val items: List<Data>,
    val listIds: List<Int>,
    val selectedListId: Int = -1,
    val selectedItems: List<Data>,
    val state: State,
)

enum class State {
    LOADING,
    SUCCESS,
    NO_INTERNET,
    ERROR
}