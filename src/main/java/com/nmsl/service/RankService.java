package com.nmsl.service;

import com.nmsl.domain.Rank;

/**
 * 评价 service接口
 */
public interface RankService {

    //增
    boolean insert(Rank rank);

    int selectScoreSum(Integer songListId); //查询评价分数
    int selectRankNum(Integer songListId);//查询评分的人数

    /**
     * 平局分
     */
    int rankOfSongListId(Integer songListId);
}
