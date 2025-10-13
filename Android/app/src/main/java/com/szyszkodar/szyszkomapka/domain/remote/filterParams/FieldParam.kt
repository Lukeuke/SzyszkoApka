package com.szyszkodar.szyszkomapka.domain.remote.filterParams

import com.google.gson.annotations.SerializedName

enum class FieldParam: FilterParam {
    @SerializedName("id") ID,
    @SerializedName("lat") LAT,
    @SerializedName("lon") LON,
    @SerializedName("created_by") CREATED_BY,
    @SerializedName("created_at") CREATED_AT,
    @SerializedName("updated_at") UPDATED_AT,
    @SerializedName("title") TITLE,
    @SerializedName("description") DESCRIPTION,
    @SerializedName("approved") APPROVED
}