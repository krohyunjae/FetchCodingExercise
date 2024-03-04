package com.barleytea.fetchcodingexercise.data.fetch

import com.barleytea.fetchcodingexercise.data.fetch.ExerciseApi.Data
import com.barleytea.networking.utils.ApiResponse
import com.barleytea.networking.utils.HasMoshi
import com.barleytea.networking.utils.toApiResponse
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    private val api: ExerciseApi,
    override val moshi: Moshi
): HasMoshi {
    suspend fun getMainInfo() : ApiResponse<List<Data>> {
        return toApiResponse { api.getExerciseData() }
    }

}