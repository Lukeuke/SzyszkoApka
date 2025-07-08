package com.szyszkodar.szyszkoapka.domain.remote.filterParams

import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkoapka.domain.remote.filter.Filter

enum class SortFilter: Filter {
    @SerializedName("id_desc") ID_DESC,
    @SerializedName("id_asc") ID_ASC,

    @SerializedName("lat_desc") LAT_DESC,
    @SerializedName("lat_asc") LAT_ASC,

    @SerializedName("lon_desc") LON_DESC,
    @SerializedName("lon_asc") LON_ASC,

    @SerializedName("created_at_desc") CREATED_AT_DESC,
    @SerializedName("created_at_asc") CREATED_AT_ASC,

    @SerializedName("updated_at_desc") UPDATED_AT_DESC,
    @SerializedName("updated_at_asc") UPDATED_AT_ASC
}