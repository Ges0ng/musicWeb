package com.nmsl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Paracosm
 * 歌单管理
 */
@Data
public class SongList implements Serializable {
    private Integer id;
    //歌单标题
    private String title;
    //歌单图片
    private String pic;
    //简介
    private String introduction;
    //风格
    private String style;


}
