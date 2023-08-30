package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.DateAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = Constants.BASE_URL

private val moshi = Moshi.Builder()
    .add(DateAdapter())
    .add(ElectionAdapter())
    .add(KotlinJsonAdapterFactory()) // Not completely understood why, but the order matters here - See https://knowledge.udacity.com/questions/475194
    .build()

// Tbd if ScalarsConverterFactory needs to be applied
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    //TODO: Add elections API Call

    //TODO: Add voterinfo API Call

    //TODO: Add representatives API Call
    @GET("elections")
    suspend fun getElections() : ElectionResponse

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Int
        ) : VoterInfoResponse

    @GET("representatives")
    suspend fun getRepresentativesAddress(
        @Query("address") address: String
    ) : RepresentativeResponse

}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}