package com.barleytea.fetchcodingexercise.data.fetch

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET

interface ExerciseApi {
    @GET("/hiring.json")
    suspend fun getExerciseData(): Response<List<Data>>

    @JsonClass(generateAdapter = true)
    data class Data(
        val id: String,
        val listId: Int,
        val name: String?
    )
}

