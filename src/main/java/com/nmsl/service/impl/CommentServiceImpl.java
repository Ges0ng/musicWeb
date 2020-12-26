package com.nmsl.service.impl;

import com.nmsl.dao.CommentDAO;
import com.nmsl.domain.Comment;
import com.nmsl.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Paracosm
 * 评论功能实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDAO commentDAO;

    @Override
    public boolean insert(Comment comment) {
        return commentDAO.insert(comment) > 0;
    }

    @Override
    public boolean update(Comment comment) {
        return commentDAO.update(comment) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return commentDAO.delete(id) > 0;
    }

    @Override
    public Comment selectById(Integer id) {
        return commentDAO.selectById(id);
    }

    @Override
    public List<Comment> selectAllComment() {
        return commentDAO.selectAllComment();
    }

    @Override
    public List<Comment> commentOfSongId(Integer songId) {
        return commentDAO.commentOfSongId(songId);
    }

    @Override
    public List<Comment> commentOfSongListId(Integer songListId) {
        return commentDAO.commentOfSongListId(songListId);
    }
}
