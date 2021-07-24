package com.smilegate.worksmile.network

import com.smilegate.worksmile.model.BaseResponse
import com.smilegate.worksmile.model.chat.SlangList
import com.smilegate.worksmile.model.chatroom.*
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface RoomApi {
    @GET("/api/chat/rooms/cur-user/{userId}")
    fun getRoomList(@Path("userId") userId: String): Single<BaseResponse<RoomList>>

    @GET("/api/chat/users/room/{roomId}")
    fun getMemberList(@Path("roomId") roomId: String): Single<BaseResponse<RoomUsers>>

    @POST("/api/chat/user/")
    fun inviteMemberList(@Body req: InviteRoomUsersReq): Single<BaseResponse<RoomUsers>>

    @POST("/api/chat/room/")
    fun createRoom(@Body req: CreateRoomReq): Single<BaseResponse<CreateRoomResp>>

    @HTTP(method = "DELETE", hasBody = true, path = "/api/chat/room/")
    fun deleteRoom(@Body req: DeleteRoomReqResp): Single<BaseResponse<DeleteRoomReqResp>>

    @POST("/api/chat/room/{roomId}")
    fun getRoomInfo(
        @Path("roomId") roomId: String,
        @Body req: RoomIdUserIdReq
    ): Single<BaseResponse<CreateRoomResp>>

    @HTTP(method = "DELETE", hasBody = true, path = "/api/chat/user/")
    fun exitRoom(
        @Body req: RoomIdUserIdReq
    ): Single<BaseResponse<UserResp>>

    @Multipart
    @POST("/api/messages/file/file-upload")
    fun updateFile(@Part part: List<MultipartBody.Part>): Single<BaseResponse<UploadFileResp>>

    @GET("/api/messages/msg/collection/room/{roomId}")
    fun getCollectionList(
        @Path("roomId") roomId: String,
        @Query("type") type: String
    ): Single<BaseResponse<ChatAttachmentResp>>

    @PUT("/api/messages/msg/user-off")
    fun refreshLastMessageIndex(@Body req: LastMessageIndex): Completable

    @GET("/api/chat/forbidden-words")
    fun getSlangList(): Single<BaseResponse<SlangList>>
}