package com.nmsl.controller;

import com.alibaba.fastjson.JSONObject;
import com.nmsl.domain.Rank;
import com.nmsl.service.RankService;
import com.nmsl.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author Paracosm
 * 评论
 */
@RestController
@Slf4j
@RequestMapping("/rank")
public class RankController {

    @Resource
    private RankService rankService;

    /**
     * 新增评价
     */
   @GetMapping(value = "/add")
    public Object add(String songListId ,String consumerId,String score) {
       JSONObject json = new JSONObject();

       Rank rank = new Rank();
       rank.setSongListId(Integer.parseInt(songListId));
       rank.setConsumerId(Integer.parseInt(consumerId));
       rank.setScore(Integer.parseInt(score));

            boolean flag = rankService.insert(rank);
            //成功
                if (flag) {
                    json.put(Consts.CODE, 1);
                    json.put(Consts.MSG,"评分成功!");
                }else{
                    //失败
                    json.put(Consts.CODE,0);
                    json.put(Consts.MSG,"评分失败");
                }
            return json;
    }


    /**
     * 计算评价的平均分
     */
    @GetMapping("/sum")
    public Object rankOfSongListId(String songListId){
        return rankService.selectScoreSum(Integer.parseInt(songListId));
    }


}
