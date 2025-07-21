package com.szyszkodar.szyszkomapka.data.remote.query

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkomapka.data.remote.filter.BookpointsFilter
import com.szyszkodar.szyszkomapka.domain.remote.filterParams.SortFilter
import com.szyszkodar.szyszkomapka.domain.remote.query.Query

data class GetBookpointsQuery(
    val sort: SortFilter? = null,
    val filters: List<BookpointsFilter> = emptyList(),
    val page: Int = 1,
    @SerializedName("page_size") val pageSize: Int = 100
): Query  {
    override fun toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        sort?.let { map["sort"] = sort.toString() }
        map["filters"] = Gson().toJson(filters)
        map["page"] = page.toString()
        map["page_size"] = pageSize.toString()

        return map
    }
}