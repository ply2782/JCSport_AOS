package com.jc.jcsports.utils;


import com.jc.jcsports.model.BulletinModel;
import com.jc.jcsports.model.chat.ChatListModel;
import com.jc.jcsports.model.FileModel;
import com.jc.jcsports.model.LoginModel;
import com.jc.jcsports.model.ReplyModel;
import com.jc.jcsports.model.chat.ChattingModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitService {


    // --- 회원가입 기능

    /**
     * 회원가입 여부 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/isExists")
    Single<LoginModel> isExists(
            @Path(value = "controller") String controller,
            @Field(value = "s_SocialToken") String s_SocialToken
    );


    /**
     * 아이디 생성 Api
     */
    @Multipart
    @POST("/{controller}/createAccount")
    Single<Boolean> createAccountResult(
            @Path(value = "controller") String controller,
            @Part List<MultipartBody.Part> imageFileList,
            @Part(value = "s_NickName") RequestBody s_Nickname,
            @Part(value = "s_Social") RequestBody social,
            @Part(value = "s_SocialToken") RequestBody S_socialToken,
            @Part(value = "s_Gender") RequestBody s_Gender,
            @Part(value = "s_Birth") RequestBody s_Birth,
            @Part(value = "s_FirebaseToken") RequestBody s_FirebaseToken,
            @Part(value = "s_PhoneNumber") RequestBody s_PhoneNumber,
            @Part(value = "s_AgeRange") RequestBody s_AgeRange
    );


    /**
     * 닉네임 중복 체크 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/doubleCheckNickName")
    Single<Boolean> doubleCheckNickName(
            @Path(value = "controller") String controller,
            @Field(value = "s_NickName") String s_NickName);


    // --- 게시물 기능


    /**
     * 게시물 목록 불러오기 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/getB_AllItems")
    Single<List<BulletinModel>> getB_AllItems(
            @Path(value = "controller") String controller,
            @Field(value = "page") int page
    );

    /**
     * 변경된 게시물 목록 불러오기 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/get_Changed_B_Items")
    Single<List<BulletinModel>> get_ChangedB_Items(
            @Path(value = "controller") String controller,
            @Field(value = "page") int page
    );

    /**
     * 나의 게시물 목록 불러오기 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/getB_MyItems")
    Single<List<BulletinModel>> getB_MyItems(
            @Path(value = "controller") String controller,
            @Field(value = "s_Number") int s_Number, @Field(value = "page") int page);


    /**
     * 게시물 상세 내용 불러오기 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/getB_Detail")
    Single<BulletinModel> getB_DetailContent(
            @Path(value = "controller") String controller,
            @Field(value = "b_Number") int b_Number,
            @Field(value = "s_Number") int s_Number
    );

    /**
     * 게시물 상세 파일 불러오기 Api
     */

    @FormUrlEncoded
    @POST("/{controller}/getB_DetailFiles")
    Single<List<FileModel>> getB_DetailFiles(
            @Path(value = "controller") String controller,
            @Field(value = "b_Number") int b_Number
    );

    /**
     * 게시물 상세 댓글 불러오기 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/getB_DetailReply")
    Single<List<ReplyModel>> getB_DetailReply(
            @Path(value = "controller") String controller,
            @Field(value = "s_Number") int s_Number,
            @Field(value = "b_Number") int b_Number,
            @Field(value = "page") int page
    );


    /**
     * 게시물 삭제 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/delete_B_Detail")
    Single<Boolean> delete_B_Detail(
            @Path(value = "controller") String controller,
            @Field(value = "b_Number") int b_Number
    );


    /**
     * 게시물 생성 APi
     */
    @Multipart
    @POST("/{controller}/createBulletinBoard")
    Single<Boolean> createBulletinResult(
            @Path(value = "controller") String controller,
            @Part(value = "s_Number") RequestBody s_Number,
            @Part(value = "b_Content") RequestBody b_Content,
            @Part List<MultipartBody.Part> fileModelList

    );
    /**
     * 썸네일 생성 APi
     */
    @Multipart
    @POST("/{controller}/createThumbNailBulletinBoard")
    Single<Boolean> createThumbNailBulletinResult(
            @Path(value = "controller") String controller,
            @Part(value = "s_Number") RequestBody s_Number,
            @Part(value = "b_Content") RequestBody b_Content,
            @Part List<MultipartBody.Part> thumbNailPhotoList

    );

    /**
     * 게시물 좋아요 클릭 및 클릭 수  APi
     */
    @FormUrlEncoded
    @POST("/{controller}/b_Like")
    Single<Integer> b_Like(
            @Path(value = "controller") String controller,
            @Field(value = "b_Number") int b_Number,
            @Field(value = "s_Number") int s_Number
    );

    /**
     * 게시물 댓글 클릭 및 댓글 수  APi
     */
    @FormUrlEncoded
    @POST("/{controller}/b_Reply")
    Single<Integer> b_Reply(
            @Path(value = "controller") String controller,
            @Field(value = "b_Number") int b_Number,
            @Field(value = "s_Number") int s_Number
    );


    /**
     * 게시물 방문자 리스트 불러오기 APi
     */
    @FormUrlEncoded
    @POST("/{controller}/b_LookList")
    Single<List<LoginModel>> b_LookList(
            @Path(value = "controller") String controller,
            @Field(value = "b_Number") int b_Number,
            @Field(value = "page") int page
    );


    /**
     * 게시물 변경 APi
     */
    @Multipart
    @POST("/{controller}/updateBulletinBoard")
    Single<Boolean> updateBulletinBoard(
            @Path(value = "controller") String controller,
            @Part(value = "b_Number") RequestBody b_Number,
            @Part(value = "s_Number") RequestBody s_Number,
            @Part(value = "b_Content") RequestBody b_Content,
            @Part MultipartBody.Part p_0,
            @Part(value = "p_0_delete") RequestBody p_0_delete,
            @Part MultipartBody.Part p_1,
            @Part(value = "p_1_delete") RequestBody p_1_delete,
            @Part MultipartBody.Part p_2,
            @Part(value = "p_2_delete") RequestBody p_2_delete,
            @Part MultipartBody.Part p_3,
            @Part(value = "p_3_delete") RequestBody p_3_delete,
            @Part MultipartBody.Part p_4,
            @Part(value = "p_4_delete") RequestBody p_4_delete
    );


    /**
     * 게시물 댓글 생성 APi
     */
    @FormUrlEncoded
    @POST("/{controller}/createB_Reply")
    Single<List<ReplyModel>> createB_Reply(
            @Path(value = "controller") String controller,
            @Field(value = "b_Number") int b_Number,
            @Field(value = "page") int page,
            @Field(value = "s_Number") int s_Number,
            @Field(value = "r_Content") String r_Content

    );

    /**
     * 채팅 리스트 불러오기 Api
     */
    @FormUrlEncoded
    @POST("/{controller}/c_Model")
    Single<List<ChatListModel>> chatList(
            @Path(value = "controller") String controller,
            @Field(value = "page") int page,
            @Field(value = "s_Number") int s_Number
    );


    /**
     * 채팅 내역 불러오기 Api
     * */
    @FormUrlEncoded
    @POST("/{controller}/chattingList")
    Single<List<ChattingModel>> chattingList(
            @Path(value = "controller") String controller,
            @Field(value = "page") int page,
            @Field(value = "c_Number") int c_Number,
            @Field(value = "s_Number") int s_Number
    );


    /**
     * 현재 가입된 유저 리스트 Api
     * */
    @FormUrlEncoded
    @POST("/{controller}/userList")
    Single<List<LoginModel>> userList(
            @Path(value = "controller") String controller,
            @Field(value = "page") int page,
            @Field(value = "s_Number") int s_Number
    );


    /**
     * 채팅룸 생성 Api
     * */
    @FormUrlEncoded
    @POST("/{controller}/goToChatRoom")
    Single<Integer> goToChatRoom(
            @Path(value = "controller") String controller,
            @Field(value = "myS_Number") int myS_Number,
            @Field(value = "yourS_Number") int yourS_Number
    );

}
