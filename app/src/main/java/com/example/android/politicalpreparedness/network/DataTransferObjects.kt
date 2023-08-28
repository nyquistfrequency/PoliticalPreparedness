package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.Election

fun List<Election>.asDatabaseModel(): Array<Election> {
    return map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            division = it.division
        )
    }.toTypedArray()
}