package com.jc.jcsports.utils;

public interface ServerURL {

    String userController = "userController";
    String fileController = "fileController";
    String bulletinController = "bulletinController";
    String chatController = "chatController";
    String userListController = "userListController";
    int joinNotification = 1, bulletinNotification = 2, userNotification = 3;
    String VIDEO_URL = "http://192.168.25.35:8080/fileController/showVideo?videoName=";
    String IMAGE_URL = "http://192.168.25.35:8080/fileController/showImage?imageName=";


}
