package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.*

// From DevBytes Code
fun List<Election>.asDomainModel(): List<Election> {
    return map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            division = it.division
        )
    }
}

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

//fun List<VoterInfoResponse>.asDomainModel(): List<VoterInfoResponse> {
//    return map {
//        VoterInfoResponse(
//            election = it.election,
//            pollingLocations = it.pollingLocations,
//            contests = it.contests,
//            state = it.state,
//            electionElectionOfficials = it.electionElectionOfficials
//        )
//    }
//}
//
//
//fun VoterInfoResponse.asDatabaseModel(): VoterInfo {
//    return VoterInfo(
//        id = this.election.id,
//        state = this.state,
//        votingLocations = this.pollingLocations,
//        ballotInformation = this.contests
//    )
//}


