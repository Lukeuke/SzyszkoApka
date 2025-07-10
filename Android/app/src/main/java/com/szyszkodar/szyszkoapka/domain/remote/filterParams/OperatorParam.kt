package com.szyszkodar.szyszkoapka.domain.remote.filterParams

import com.google.gson.annotations.SerializedName

enum class OperatorParam : FilterParam {
    @SerializedName("eq")
    EQ,

    @SerializedName("neq")
    NEQ,

    @SerializedName("gt")
    GT,

    @SerializedName("gte")
    GTE,

    @SerializedName("lt")
    LT,

    @SerializedName("lte")
    LTE,

    @SerializedName("like")
    LIKE,

    @SerializedName("ilike")
    ILIKE,

    @SerializedName("in")
    IN,

    @SerializedName("is")
    IS,

    @SerializedName("match")
    MATCH,

    @SerializedName("overlap")
    OVERLAP
}
