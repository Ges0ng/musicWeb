package com.nmsl.dao;

import com.nmsl.domain.Rank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 评价
 */
@Mapper
public interface RankDAO {


    //增
    int insert(Rank rank);

    int selectScoreSum(Integer songListId); //查询评价分数
    int selectRankNum(Integer songListId);//查询评分的人数
}
