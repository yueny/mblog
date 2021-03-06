package com.mtons.mblog.entity.bao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : langhsu
 */
@Entity
@Table(name = "mto_tag")
@TableName("mto_tag")
@ToString
@Getter
@Setter
public class Tag extends AbstractUpdatePlusEntry {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = 32)
    private String name;

    @Column(length = 128)
    @Deprecated
    private String thumbnail;

    private String description;

    private Long latestPostId;

//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date created;
//
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date updated;

    private Integer posts;

}
