package com.xpf.dbhelper.model;

import org.litepal.crud.DataSupport;

/**
 * Created by xpf on 2017/2/21:)
 * Function:用户的Bean类(属性字段仅供测试用,属性可后续修改)
 */

public class UserBean extends DataSupport {

    private long id;
    private String name;
    private String room;
    private String phone;

    public UserBean() {
    }

    public UserBean(long id, String name, String room, String phone) {
        this.id = id;
        this.name = name;
        this.room = room;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", room='" + room + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
