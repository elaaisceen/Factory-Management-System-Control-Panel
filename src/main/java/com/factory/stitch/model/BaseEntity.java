package com.factory.stitch.model;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
public abstract class BaseEntity {
    protected Integer id;

    public BaseEntity() {
    }

    public BaseEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

