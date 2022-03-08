package com.fortis.MiniProject.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name ="units")
public class UnitEntity implements Serializable {
    private static final long serialVersionUID = 592091589826617237L;

    /*
    {
        "id":01,
        "unitName":"<name>",
        "unitDescription":"<description>",
        "price":<price>,
        "photo1":"<url>",
        "photo2":"<url>",
        "photo3":"<url>",
        "photo4":"<url>",
        "photo5":"<url>",
    }
     */


    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private UserEntity userEntity;

    @Column(nullable = false)
    private String unitId;

    @Column(nullable = false)
    private String unitName;

    @Column(nullable = false)
    private String unitDescription;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String photo1;

    @Column
    private String photo2;

    @Column
    private String photo3;

    @Column
    private String photo4;

    @Column
    private String photo5;

    @Column
    private Boolean deleted = false;

    @Column
    private Double rating = 5d;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
