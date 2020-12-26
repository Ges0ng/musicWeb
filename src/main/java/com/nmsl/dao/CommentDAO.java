package com.nmsl.dao;

import com.nmsl.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 评论dao
 * @author Paracosm
 */
@Mapper
public interface CommentDAO {
    //增
    int insert(Comment comment);
    //改
    int update(Comment comment);
    //删
    int delete(Integer id);

    Comment selectById(Integer id); //根据id查询评论
    List<Comment> selectAllComment(); //查询所有评论
    List<Comment> commentOfSongId(Integer songId);   //查询某个歌曲的所有评论
    List<Comment> commentOfSongListId(Integer songListId);//查询歌单下的评论

}
