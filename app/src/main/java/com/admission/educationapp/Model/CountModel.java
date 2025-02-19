package com.admission.educationapp.Model;

public class CountModel {
    public String department;
    public String id;
    public String col_id;

    public String getCol_id() {
        return col_id;
    }

    public void setCol_id(String col_id) {
        this.col_id = col_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String count;
    public String cq_count;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCq_count() {
        return cq_count;
    }

    public void setCq_count(String cq_count) {
        this.cq_count = cq_count;
    }

    public String getMq_count() {
        return mq_count;
    }

    public void setMq_count(String mq_count) {
        this.mq_count = mq_count;
    }

    public String mq_count;
}
