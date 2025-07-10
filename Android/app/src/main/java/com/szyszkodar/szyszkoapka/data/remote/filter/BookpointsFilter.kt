package com.szyszkodar.szyszkoapka.data.remote.filter

import com.szyszkodar.szyszkoapka.domain.remote.filter.Filter
import com.szyszkodar.szyszkoapka.domain.remote.filterParams.FieldParam
import com.szyszkodar.szyszkoapka.domain.remote.filterParams.OperatorParam

data class BookpointsFilter(
    val field: FieldParam,
    val operator: OperatorParam,
    val value: String
): Filter {
    companion object {
        fun <T> from(field: FieldParam, operator: OperatorParam, value: T): BookpointsFilter {
            return BookpointsFilter(field, operator, value.toString())
        }
    }
}
