package com.xpf.dbhelper.model;

import org.litepal.crud.DataSupport;

/**
 * Created by xpf on 2017/2/21:)
 * Function:门禁卡的Bean类
 */

public class IdCard extends DataSupport{

    private long id;
    private String name;
    private String phone;

    public IdCard() {
    }

    public IdCard(long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
