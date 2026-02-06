package com.example.collegeschedulehomyakov.data.dto

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("groupId") val groupId: Int,
    @SerializedName("groupName") val groupName: String,
    @SerializedName("course") val course: Int,
    @SerializedName("specialty") val specialty: String
)