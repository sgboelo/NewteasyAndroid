package com.SmartTech.teasyNew.api_new.appmanager.response_model;

import com.SmartTech.teasyNew.model.Notification;

import java.util.List;

public class GetNotificationsResponse extends BaseResponse {
    List<Notification> notificationList;

    public List<Notification> getNotificationList() {
        return notificationList;
    }
}
