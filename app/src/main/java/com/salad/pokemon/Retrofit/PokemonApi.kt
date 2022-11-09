package com.salad.pokemon.Retrofit

import androidx.annotation.Keep
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonApi {
    @Keep
    @GET
    suspend fun getPokemon(@Url url: String): String
}