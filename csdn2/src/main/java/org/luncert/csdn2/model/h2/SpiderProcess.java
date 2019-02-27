package org.luncert.csdn2.model.h2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.luncert.csdn2.model.normal.Category;

import lombok.Data;

/**
 * 自动创建表的配置：none, update, create
 */
@Data
@Entity
@Table(
    name = "SpiderProcess",
    uniqueConstraints = @UniqueConstraint(columnNames = {"category"})
)
@DynamicUpdate
@DynamicInsert
public class SpiderProcess
{

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid.hex")
    @Column(name = "id", nullable = false, length = 32, unique = true)
    private String id;

    private Category category;
    private String shownOffset;
    private boolean finished = false;

    public SpiderProcess() {}

    public SpiderProcess(Category category)
    {
        this(category, null);
    }

    public SpiderProcess(String shownOffset)
    {
        this(null, shownOffset);
    }

    public SpiderProcess(Category category, String shownOffset)
    {
        this.category = category;
        this.shownOffset = shownOffset;
    }

}