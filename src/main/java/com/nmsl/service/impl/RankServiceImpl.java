package com.nmsl.service.impl;

import com.nmsl.dao.RankDAO;
import com.nmsl.domain.Rank;
import com.nmsl.service.RankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Paracosm
 * 评价的serivce实现类
 */
@Service
public class RankServiceImpl implements RankService {

    @Resource
    private RankDAO rankDAO;

    @Override
    public boolean insert(Rank rank) {
        return rankDAO.insert(rank) > 0;
    }

    @Override
    public int selectScoreSum(Integer songListId) {
        return rankDAO.selectScoreSum(songListId) ;
    }

    @Override
    public int selectRankNum(Integer songListId) {
        return rankDAO.selectRankNum(songListId);
    }

    @Override
    public int rankOfSongListId(Integer songListId) {
        int rankNum = rankDAO.selectRankNum(songListId);
        if (rankNum == 0) {
            return 5;
        }
        return rankDAO.selectScoreSum(songListId)/rankNum;
    }
}
