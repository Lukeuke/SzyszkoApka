package com.szyszkodar.szyszkoapka.domain.remote.filterParams

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

enum class FieldParam: FilterParam {
    @SerializedName("id") ID,
    @SerializedName("lat") LAT,
    @SerializedName("lon") LON,
    @SerializedName("created_at") CREATED_AT,
    @SerializedName("updated_at") UPDATED_AT,
}