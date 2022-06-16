package com.rebliss.domain.model

data class ResonseforActivity(
    val status : Int,
    val desc : String,
    val id : List<Id>
)
{

    data class Id (

        val cat1 : Int
    )
}

