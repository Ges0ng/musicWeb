package com.nmsl.domain;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Paracosm
 * 评论
 */
@Data
@Accessors(chain = true)
public class Comment implements Serializable {

    private Integer id;
    private Integer userId;
    private Byte type;
    private Integer songId;
    private Integer songListId;
    private String content;
    private Date createTime;
    private Integer up;

}
