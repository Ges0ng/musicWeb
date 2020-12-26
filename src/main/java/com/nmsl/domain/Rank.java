package com.nmsl.domain;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Paracosm
 * 评价
 */
@Data
@Accessors(chain = true)
@ToString
public class Rank implements Serializable {

    private Integer id;

    /**
     * 歌单id
     */
    private Integer songListId;

    /**
     * 用户id
     */
    private Integer consumerId;

    /**
     * 评分
     */
    private Integer score;
}
