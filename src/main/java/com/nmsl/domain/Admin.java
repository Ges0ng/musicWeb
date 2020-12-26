package com.nmsl.domain;


import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author Paracosm
 */
@Data
@Accessors(chain = true)
public class Admin implements Serializable {

    private Integer id;
    private String name;
    private String password;

}
