"use strict";

import { NativeModules, NativeEventEmitter } from "react-native";
// name as defined via ReactContextBaseJavaModule's getName
const { VLX } = NativeModules;

const EventEmitter = new NativeEventEmitter(VLX || {});

export const VeloxityEvent = {
  AuthSucceed: "AuthSucceed",
  AuthFailed: "AuthFailed",
  OnCompleted: "OnCompleted"
};

export const _VLX = {
  setLicenseKey: function (licenseKey: string) {
    VLX.setLicenseKey(licenseKey);
  },

  setWebServiceUrl: function (webServiceUrl: string) {
    VLX.setWebServiceUrl(webServiceUrl);
  },

  setAskPermission: function(askPermission: boolean) {
    VLX.setAskPermission(askPermission);
  },

  setAuthorizationMenu: function (
    title: string,
    message: string,
    acceptTitle: string,
    denyTitle: string
  ) {
    VLX.setAuthorizationMenu(title, message, acceptTitle, denyTitle);
  },

  registerDeviceToken: function (deviceToken: string) {
    VLX.registerDeviceToken(deviceToken);
  },

  registerLifeCycleCallbacks: function(application: Object, callback) {
    VLX.registerLifeCycleCallbacks(application, callback);
  },

  setPriority: function (priority: number) {
    VLX.setPriority(priority);
  },

  setFCMSenderID: function (senderID: string) {
    VLX.setFCMSenderID(senderID);
  },

  setFCMProjectID: function (projectId: string) {
    VLX.setFCMProjectID(projectId);
  },

  setFCMAppID: function (appId: string) {
    VLX.setFCMAppID(appId)
  },

  setFCMApiKey: function (apiKey: string) {
    VLX.setFCMApiKey(apiKey)
  },

  sendCustomData: function (data) {
    VLX.sendCustomData(data);
  },

  sendDataUsageStatus: function (
    endPointUrl: string,
    isDataUsageAccepted: boolean
  ) {
    VLX.sendDataUsageStatus(endPointUrl, isDataUsageAccepted);
  },

  startBackgroundTransactionWith: function (data) {
    VLX.startBackgroundTransactionWith(data);
  },

  start: function () {
    VLX.start();
  },

  optIn: function () {
    VLX.optIn();
  },

  optOut: function () {
    VLX.optOut();
  },

  getServiceStatus: function (callback) {
    VLX.getServiceStatus(callback);
  },

  getDeviceId: function (callback) {
    VLX.getDeviceId(callback);
  },

  on: function (event, callback) {
    if (!Object.values(VeloxityEvent).includes(event)) {
      throw new Error(
        `Invalid Veloxity event subscription, use import {VeloxityEvent} from 'react-native-veloxity' to avoid type`
      );
    }
    return EventEmitter.addListener(event, callback);
  }
};

export default _VLX;
