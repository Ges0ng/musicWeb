package com.nmsl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 歌曲歌单
 * @author Paracosm
 */
@Data
public class ListSong implements Serializable {
    private Integer id;

    private Integer songId; //歌曲id
    private Integer songListId; //歌单id

}
