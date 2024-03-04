package com.barleytea.fetchcodingexercise.model

import com.barleytea.fetchcodingexercise.data.fetch.ExerciseApi.Data


data class ExerciseModel(
    val items: List<Data>,
    val state: State,
    //val  selected list
)

enum class State {
    LOADING,
    SUCCESS,
    NO_INTERNET,
    ERROR
}