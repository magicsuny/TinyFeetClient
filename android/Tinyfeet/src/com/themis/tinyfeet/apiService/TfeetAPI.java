package com.themis.tinyfeet.apiService;

import android.util.Base64;
import com.google.protobuf.InvalidProtocolBufferException;
import com.themis.tinyfeet.bo.CommentBO;
import com.themis.tinyfeet.bo.TfeetBO;
import com.themis.tinyfeet.bo.UserBO;
import com.themis.tinyfeet.proto.Tfeet;
import com.themis.tinyfeet.proto.User;
import com.themis.tinyfeet.utils.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-13
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public class TfeetAPI {
    private static final String API_HEADER_XDATE= "X-Date";
    private static final String API_HEADER_XID= "X-ID";
    private static final String API_HEADER_TIME_STRING= "time_string";
    private static final String API_HEADER_SRC_KEY= "src_key";

    private static final String APT_REQUEST_PARAMNAME = "DATA";
    private static final String API_PATH_ADD_TFEET = "/t/add";
    private static final String API_PATH_LIKE_TFEET = "/t/like";
    private static final String API_PATH_ADD_COMMENT = "/t/comment";
    private static final String API_PATH_GET_TFEET_DETAIL = "/t/";
    private static final String API_PATH_GET_TFEET_COMMENTLIST = "/t/m";
    private static final String API_PATH_GET_TFEET_LOCLIST = "/t/loc";
    private static final String API_PATH_GET_TFEET_FRIENDS = "/t/u";
    private static final String API_PATH_GET_USERDETAIL = "/u/";
    private static final String API_PATH_GET_USEROSS = "/u/oss";
    private static final String API_PATH_GET_USER_FOLLOW = "/u/follow";

    private static Map<String,String> genAPIHeader(){
        Map<String,String> headers = new HashMap<String,String>();
        headers.put(API_HEADER_XDATE, "");
        headers.put(API_HEADER_XID, "");
        headers.put(API_HEADER_TIME_STRING, "");
        headers.put(API_HEADER_SRC_KEY, "");
        return headers;
    }

    /**
     * 新增tfeet
     * @param tfeetBO
     * @return
     */
    public static TfeetBO addTfeet(TfeetBO tfeetBO) throws InvalidProtocolBufferException, JSONException {
        Tfeet.TFeet tfeet =  tfeetBO.bo2Protobuf();
        JSONObject reqParams = new JSONObject();
        reqParams.put(APT_REQUEST_PARAMNAME,new String(Base64.encode(tfeet.toByteArray(),Base64.DEFAULT)));

        String responseStr = HttpUtils.getResponseForPost(API_PATH_ADD_TFEET,reqParams,genAPIHeader());

        //Tfeet.TFeet resTfeet = Tfeet.TFeet.parseFrom(Base64.decode(responseStr,Base64.DEFAULT));

        return tfeetBO;
    }

    /**
     * 喜欢tfeet
     * @param tfid tfeetid
     * @param uId  用户id
     * @param shared2SNS 是否分享到微博
     * @throws JSONException
     */
    public static void likeTfeet(long tfid,String uId,Boolean shared2SNS) throws JSONException {
        Tfeet.LikeTFeetRequest.Builder  likeTfeetRequest = Tfeet.LikeTFeetRequest.newBuilder();
        likeTfeetRequest.setTfid(tfid);
        likeTfeetRequest.setUid(uId);
        likeTfeetRequest.setShare2Sns(shared2SNS?shared2SNS:true);
        JSONObject reqParams = new JSONObject();
        reqParams.put(APT_REQUEST_PARAMNAME,new String(Base64.encode(likeTfeetRequest.build().toByteArray(), Base64.DEFAULT)));
        String responseStr = HttpUtils.getResponseForPost(API_PATH_LIKE_TFEET,reqParams,genAPIHeader());
    }


    /**
     * 评论tfeet
     * @param tfid 被评论的tfid
     * @param uId  用户id
     * @param text 评论内容
     * @param shared2SNS 是否分享到微博
     * @throws JSONException
     */
    public static void commentTfeet(long tfid,String uId,String text,Boolean shared2SNS) throws JSONException {
        Tfeet.AddCommentRequest.Builder addCommentRequest = Tfeet.AddCommentRequest.newBuilder();
        addCommentRequest.setTfid(tfid);
        addCommentRequest.setUid(uId);
        addCommentRequest.setText(text);
        addCommentRequest.setShare2Sns(shared2SNS?shared2SNS:true);
        JSONObject reqParams = new JSONObject();
        reqParams.put(APT_REQUEST_PARAMNAME,new String(Base64.encode(addCommentRequest.build().toByteArray(), Base64.DEFAULT)));
        String responseStr = HttpUtils.getResponseForPost(API_PATH_ADD_COMMENT,reqParams,genAPIHeader());
    }

    /**
     * 获取tfeet的详细信息
     * @param tfid  tfeetid
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static TfeetBO getTfeetDetail(long tfid) throws InvalidProtocolBufferException {
        String responseStr = HttpUtils.getResponseForGet(wrapRESTFulUrl(API_PATH_GET_TFEET_DETAIL,tfid),genAPIHeader());
        Tfeet.TFeet tfeet =  Tfeet.TFeet.parseFrom(Base64.decode(responseStr,Base64.DEFAULT));
        TfeetBO tfeetBO = new TfeetBO();
        tfeetBO.protobuf2BO(tfeet);
        return tfeetBO;
    }

    /**
     * 获取tfeet的评论列表
     * @param tfid
     * @param timestamp
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static List<CommentBO> getCommentList(long tfid,long timestamp) throws InvalidProtocolBufferException {
        List<CommentBO> commentBOList = new ArrayList<CommentBO>();
        String responseStr = HttpUtils.getResponseForGet(wrapRESTFulUrl(API_PATH_GET_TFEET_COMMENTLIST,tfid,timestamp),genAPIHeader());
        Tfeet.CommentList resCommentList = Tfeet.CommentList.parseFrom(Base64.decode(responseStr,Base64.DEFAULT));
        List<Tfeet.Comment> commentList = resCommentList.getCommentsList();
        for(Tfeet.Comment comment:commentList){
            CommentBO bo = new CommentBO();
            bo.protoBuf2BO(comment);
            commentBOList.add(bo);
        }
        return commentBOList;
    }

    /**
     * 获取指定位置的tfeet列表
     * @param lat 经度
     * @param lng 纬度
     * @param season  季节
     * @param daynight 时间
     * @param lastSqid 上次结果最后一个sqid 分页
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static List<TfeetBO> getLocTfeetList(long lat,long lng,int season,int daynight,long lastSqid) throws InvalidProtocolBufferException {
        List<TfeetBO> tfeetBOList = new ArrayList<TfeetBO>();
        String responseStr = HttpUtils.getResponseForGet(wrapRESTFulUrl(API_PATH_GET_TFEET_LOCLIST,lat,lng,season,daynight,lastSqid),genAPIHeader());
        Tfeet.TFeetList resTfeetList = Tfeet.TFeetList.parseFrom(Base64.decode(responseStr,Base64.DEFAULT));
        List<Tfeet.TFeet> tfeetList = resTfeetList.getTfeetList();
        for(Tfeet.TFeet tfeet:tfeetList){
            TfeetBO bo = new TfeetBO();
            bo.protobuf2BO(tfeet);
            tfeetBOList.add(bo);
        }
        return tfeetBOList;
    }


    /**
     * 获取指定用户的tfeet列表
     * @param uid 用户ID
     * @param lastSqid 上一次最后一条tfeet的sqid 分页
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static List<TfeetBO> getFriendsTfeetList(long uid,long lastSqid) throws InvalidProtocolBufferException {
        List<TfeetBO> tfeetBOList = new ArrayList<TfeetBO>();
        String responseStr = HttpUtils.getResponseForGet(wrapRESTFulUrl(API_PATH_GET_TFEET_FRIENDS,uid,lastSqid),genAPIHeader());
        Tfeet.TFeetList resTfeetList = Tfeet.TFeetList.parseFrom(Base64.decode(responseStr, Base64.DEFAULT));
        List<Tfeet.TFeet> tfeetList = resTfeetList.getTfeetList();
        for(Tfeet.TFeet tfeet:tfeetList){
            TfeetBO bo = new TfeetBO();
            bo.protobuf2BO(tfeet);
            tfeetBOList.add(bo);
        }
        return tfeetBOList;
    }

    /**
     * 获取用户详细信息
     * @param uid 用户id
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static UserBO getUserDetail(String uid) throws InvalidProtocolBufferException {
        String responseStr = HttpUtils.getResponseForGet(wrapRESTFulUrl(API_PATH_GET_USERDETAIL,uid),genAPIHeader());
        User.UserDetail userDetail = User.UserDetail.parseFrom(Base64.decode(responseStr, Base64.DEFAULT));
        UserBO bo = new UserBO();
        bo.protoBuf2BO(userDetail);
        return bo;
    }

    /**
     * 获取个人信息
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static UserBO getCurrentUserDetail() throws InvalidProtocolBufferException {
        String responseStr = HttpUtils.getResponseForGet(API_PATH_GET_USEROSS,genAPIHeader());
        User.UserDetail userDetail = User.UserDetail.parseFrom(Base64.decode(responseStr, Base64.DEFAULT));
        UserBO bo = new UserBO();
        bo.protoBuf2BO(userDetail);
        return bo;
    }

    /**
     * 关注某用户
     * @param uid 用户ID
     * @param friendUid 关注的用户ID
     * @throws JSONException
     */
    public static void followUser(String uid,String friendUid) throws JSONException {
        User.FollowRequest.Builder followRequest = User.FollowRequest.newBuilder();
        followRequest.setUid(uid);
        followRequest.setFriendUid(friendUid);

        JSONObject reqParams = new JSONObject();
        reqParams.put(APT_REQUEST_PARAMNAME,new String(Base64.encode(followRequest.build().toByteArray(), Base64.DEFAULT)));
        HttpUtils.getResponseForPost(API_PATH_GET_USER_FOLLOW,reqParams,genAPIHeader());
    }

    /**
     * 取消关注某人
     * @param uid
     * @param friendUid
     * @throws JSONException
     */
    public static void unFoloowUser(String uid,String friendUid) throws JSONException {
        User.UnFollowRequest.Builder unFollowRequest = User.UnFollowRequest.newBuilder();
        unFollowRequest.setUid(uid);
        unFollowRequest.setFriendUid(friendUid);

        JSONObject reqParams = new JSONObject();
        reqParams.put(APT_REQUEST_PARAMNAME,new String(Base64.encode(unFollowRequest.build().toByteArray(), Base64.DEFAULT)));
        HttpUtils.getResponseForPost(API_PATH_GET_USER_FOLLOW,reqParams,genAPIHeader());
    }

    private static String wrapRESTFulUrl(String baseUrl,Object ...params){
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        for(int i=0;i<params.length;i++){
            stringBuilder.append("/%s");
        }
        return stringBuilder.toString().format(baseUrl,params);
    }

}
