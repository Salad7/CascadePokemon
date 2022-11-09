package com.salad.pokemon.Retrofit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonObject(val base_experience : Double) {
}