package com.example.type1.model;

import java.io.Serializable;

/**
 * Created by sjy on 2017/5/18.
 */

public class TextModel implements Serializable {
    //会议编号
    private String ConferenceID;

    //会议名称
    private String ConferenceName;

    //会议主题
    private String ConferenceTitle;


    //会议详情
    private String ConAbstracts;

    private String BeginTime;

    //结束时间
    private String FinishTime;

    //会议人数
    private String NumParticipant;

    //会议地址
    private String Location;

    //场所名称
    private String Site;

    //负责人
    private String Director;

    //负责人电话
    private String Telephone;

    //创建时间
    private String CreateTime;

    //会场面积
    private String Area;

    //备注
    private String Remark;

    //公司ID
    private String CompanyID;

    //会议图标
    private String Photourl;

    //报名费用
    private String Money;

    public String getConferenceID() {
        return ConferenceID;
    }

    public void setConferenceID(String conferenceID) {
        this.ConferenceID = conferenceID;
    }

    public String getConferenceName() {
        return ConferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.ConferenceName = conferenceName;
    }

    public String getConferenceTitle() {
        return ConferenceTitle;
    }

    public void setConferenceTitle(String conferenceTitle) {
        this.ConferenceTitle = conferenceTitle;
    }

    public String getConAbstracts() {
        return ConAbstracts;
    }

    public void setConAbstracts(String conAbstracts) {
        ConAbstracts = conAbstracts;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        this.BeginTime = beginTime;
    }

    public String getFinishTime() {
        return FinishTime;
    }

    public void setFinishTime(String finishTime) {
        this.FinishTime = finishTime;
    }

    public String getNumParticipant() {
        return NumParticipant;
    }

    public void setNumParticipant(String numParticipant) {
        this.NumParticipant = numParticipant;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getSite() {
        return Site;
    }

    public void setSite(String site) {
        this.Site = site;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        this.Director = director;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        this.Telephone = telephone;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        this.CreateTime = createTime;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        this.Area = area;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        this.CompanyID = companyID;
    }

    public String getPhotourl() {
        return Photourl;
    }

    public void setPhotourl(String photourl) {
        this.Photourl = photourl;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        this.Money = money;
    }
}
