package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.Comment;
import com.nmsl.service.CommentService;
import com.nmsl.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;


/**
 * @author Paracosm
 *  评论
 */
@RestController
@Slf4j
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 添加评论
     * @param userId
     * @param type
     * @param songId
     * @param songListId
     * @param content
     * @return
     */
    @PostMapping("/add")
    public Object addComment(String userId,String type,String songId,String songListId,String content){
        JSONObject json = new JSONObject();

        Comment comment = new Comment();
        comment.setUserId(Integer.parseInt(userId));
        byte newType = new Byte(type);
        comment.setType(newType);
        //因为评论类型只有0和1,要么针对歌曲要么针对歌单的
        if (newType == 0){
            comment.setSongId(Integer.parseInt(songId));
        }else{
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);

        boolean flag = commentService.insert(comment);
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"评论成功!");
        }else{
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"评论失败");
        }
        return json;
    }

    /**
     * 编辑评论内容
     * @param id
     * @param userId
     * @param type
     * @param songId
     * @param songListId
     * @param content
     * @return
     */
    @PostMapping("/update")
    public Object updateComment(String id,String userId,String type,String songId,String songListId,String content){
        JSONObject json = new JSONObject();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUserId(Integer.parseInt(userId));
        byte newType = new Byte(type);
        comment.setType(newType);

        if (songId!=null && "".equals(songId)){
            songId = null;
        }else{
            comment.setSongId(Integer.parseInt(songId));
        }
        if (songListId!=null && "".equals(songListId)){
            songListId = null;
        }else{
            comment.setSongId(Integer.parseInt(songListId));
        }

        comment.setContent(content);

        boolean flag = commentService.update(comment);
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"修改成功!");
        }else{
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"修改失败");
        }
        return json;
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Object deleteComment(String id) {
        JSONObject json = new JSONObject();

        boolean flag = commentService.delete(Integer.parseInt(id));

        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"删除成功!");
        }else{
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"删除失败");
        }

        return flag;
    }

    /**
     * 根据id查询评论
     * @param id
     * @return
     */
    @GetMapping("/selectById")
    public Comment selectById(String id){
        return commentService.selectById(Integer.parseInt(id));
    }

    /**
     * 查询所有的评论
     * @return
     */
    @GetMapping(value = "/allComment")
    public Object selectAllComment(){
        return commentService.selectAllComment();
    }

    /**
     * 歌曲下的评论内容
     * @param songId
     * @return
     */
    @GetMapping("/commentOfSongId")
    public Object commentOfSongId (String songId){
        return commentService.commentOfSongId(Integer.parseInt(songId));
    }

    /**
     * 歌单下的评论内容
     * @param songListId
     * @return
     */
    @GetMapping("/commentOfSongListId")
    public Object commentOfSongListId (String songListId){
        return commentService.commentOfSongListId(Integer.parseInt(songListId));
    }

    /**
     * 给某个评论点赞
     * @param up
     * @param id
     * @return
     */
    @PostMapping("/like")
    public Object up (String id,String up){
        JSONObject json = new JSONObject();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUp(Integer.parseInt(up));

        //有人点赞就在up上+1
        boolean flag = commentService.update(comment);
        if (flag){
            json.put(Consts.CODE,1);
            json.put(Consts.MSG,"点赞成功!");
        }else{
            json.put(Consts.CODE,0);
            json.put(Consts.MSG,"点赞失败");
        }
        return json;
    }

}
