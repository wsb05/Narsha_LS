package kr.hs.narsha_ls.restapi

import okhttp3.RequestBody

import okhttp3.MultipartBody

import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.http.POST

import retrofit2.http.Multipart
import retrofit2.http.Part


interface UploadService {
    @Multipart
    @POST("/upload")
    fun postImage(
        @Part image: MultipartBody.Part?,
        @Part("upload") name: RequestBody?
    ): Call<ResponseBody?>?
}

//@GET("discover/movie")
//        /**
//         * REST 요청을 처리하기 위한 메서드
//         * @param par QueryMap을 통해 질의한 쿼리문을 Map으로 부터 받는다.
//         * @return Call<T> 콜백 인터페이스 반환, T는 주고 받을 데이터 구조
//         * @QueryMap 어노테이션은 위치가 바뀌어도 동적으로 값을 받아올 수 있게 한다.
//         */
//fun getTop(@QueryMap par: Map<String, String>): Call<MovieListResponse>