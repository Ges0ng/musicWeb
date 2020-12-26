package com.nmsl.service;

import com.nmsl.domain.Comment;

import java.util.List;

/**
 * 评论功能
 * @author Paracosm
 */
public interface CommentService {

    //增
    boolean insert(Comment comment);
    //改
    boolean update(Comment comment);
    //删
    boolean delete(Integer id);

    Comment selectById(Integer id); //根据id查询评论
    List<Comment> selectAllComment(); //查询所有评论
    List<Comment> commentOfSongId(Integer songId);   //查询某个歌曲的所有评论
    List<Comment> commentOfSongListId(Integer songListId);//查询歌单下的评论
}
