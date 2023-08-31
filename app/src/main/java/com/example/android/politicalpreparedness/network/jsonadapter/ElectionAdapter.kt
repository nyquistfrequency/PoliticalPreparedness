package com.example.android.politicalpreparedness.network.jsonadapter

import com.example.android.politicalpreparedness.network.models.Division
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ElectionAdapter {

    //After Submission Feedback: Added a DistrictDelimiter to divisionFromJson to consider
    //edge cases where state is null but district is given instead
    //e.g. ocd-division/country:us/district:dc
    @FromJson
    fun divisionFromJson (ocdDivisionId: String): Division {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val districtDelimiter = "district:"
        val country = ocdDivisionId.substringAfter(countryDelimiter,"")
                .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter,"")
                .substringBefore("/")

        //Assumption: The district shall be used if no state is given, otherwise use the state
        val district = ocdDivisionId.substringAfter(districtDelimiter,"")
            .substringBefore("/")
        return if (state == "") {
            Division(ocdDivisionId,country,district)
        } else {
            Division(ocdDivisionId, country, state)
        }
    }

    @ToJson
    fun divisionToJson (division: Division): String {
        return division.id
    }
}