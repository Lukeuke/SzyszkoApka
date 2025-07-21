package com.szyszkodar.szyszkomapka.data.remote.filter

import com.szyszkodar.szyszkomapka.domain.remote.filter.Filter
import com.szyszkodar.szyszkomapka.domain.remote.filterParams.FieldParam
import com.szyszkodar.szyszkomapka.domain.remote.filterParams.OperatorParam

data class BookpointsFilter(
    val field: FieldParam,
    val operator: OperatorParam,
    val value: String
): Filter {
    companion object {
        fun <T> generic(field: FieldParam, operator: OperatorParam, value: T): BookpointsFilter {
            return BookpointsFilter(field, operator, value.toString())
        }
    }
}
