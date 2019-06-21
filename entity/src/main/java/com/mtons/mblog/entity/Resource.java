package com.mtons.mblog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mtons.mblog.base.enums.ResourceType;
import com.mtons.mblog.entity.api.IEntry;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 图片资源表
 *
 * @author saxing 2019/4/3 21:24
 */
@Data
@Entity
@Table(name = "mto_resource",
        uniqueConstraints = {@UniqueConstraint(name = "UK_MD5", columnNames = {"md5"})}
)
@TableName("mto_resource")
public class Resource implements IEntry, Serializable {
    private static final long serialVersionUID = -2263990565349962964L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP")
    @Generated(GenerationTime.INSERT)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "updated", columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Generated(GenerationTime.ALWAYS)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updated;



    /**
     * 图片资源编号
     */
    @Column(name = "thumbnail_code", columnDefinition = "varchar(128) NOT NULL DEFAULT ''")
    @Getter
    @Setter
    private String thumbnailCode;

    /**
     * md5唯一值
     */
    @Column(name = "md5", columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String md5;

    /**
     * 保存路径
     */
    @Column(name = "path", columnDefinition = "varchar(255) NOT NULL DEFAULT ''")
    private String path;

    @Column(name = "amount", columnDefinition = "bigint(20) NOT NULL DEFAULT '0'")
    private long amount;

    /**
     * 附件类型
     */
    @Getter
    @Setter
    private ResourceType resourceType;

}
