package com.nmsl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Paracosm
 */
@Data
public class Song implements Serializable {
    private Integer id;
    //歌手id
    private Integer singerId;
    //歌名
    private String name;
    //简介
    private String introduction;
    //添加时间
    private Date create_time;
    //修改时间
    private Date update_time;
    //歌曲图片
    private String pic;
    //歌词
    private String lyric;
    //歌曲地址
    private String url;

}
