package com.nmsl.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 收藏功能
 * @author Paracosm
 */
@Data
@Accessors(chain = true)
public class Collect implements Serializable {
    private Integer id;

    private Integer userId;

    /**
     * 收藏类型（0歌曲1歌单）
     */

    private Byte type;

    private Integer songId;
    /**
     * 歌单id
     */
    private Integer songListId;

    /**
     * 收藏的时间
     */
    private Date createTime;


}
