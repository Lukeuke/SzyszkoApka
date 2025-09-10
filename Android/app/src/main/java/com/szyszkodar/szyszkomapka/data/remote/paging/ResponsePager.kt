package com.szyszkodar.szyszkomapka.data.remote.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.szyszkodar.szyszkomapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.remote.response.PageableResponse
import com.szyszkodar.szyszkomapka.domain.remote.response.ResponseElement

class ResponsePager<T: PageableResponse<R>, R: ResponseElement>(
    private val pageSize: Int,
    private val loadDataFunction: suspend (Int) -> Result<T, NetworkError>
) {
    private inner class ResponsePagingSource: PagingSource<Int, R>() {
        override fun getRefreshKey(state: PagingState<Int, R>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, R> {
            val page = params.key ?: 1

            return when(val response = loadDataFunction(page)) {
                is Result.Success -> LoadResult.Page(
                    data = response.data.data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page >= response.data.total/pageSize - 1) null else page + 1
                )

                is Result.Error -> LoadResult.Error(Exception(response.error.message))
            }
        }
    }

    val pager = Pager(
        config = PagingConfig(pageSize),
        pagingSourceFactory = { ResponsePagingSource() }
    ).flow

}