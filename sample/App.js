/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from "react";
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Button,
  Alert,
  Switch
} from "react-native";

import PushNotificationIOS from "@react-native-community/push-notification-ios";
import Clipboard from '@react-native-clipboard/clipboard';

import VLX, { VeloxityEvent } from "react-native-veloxitysdk";
import analytics from '@react-native-firebase/analytics';


export default class App extends Component<{}> {
  constructor(props) {
    super(props);
    this.state = {
      deviceID: "",
      isServiceEnabled: false
    };
  }

  componentDidMount() {
    VLX.on(VeloxityEvent.AuthSucceed, () => {
      console.log("Auth Succeed");
      this.setState({ isServiceEnabled: true });
      this.setDeviceID();
    });

    VLX.on(VeloxityEvent.AuthFailed, () => {
      console.log("Auth Failed");
      this.setState({ isServiceEnabled: false });
      this.setDeviceID();
    });
    
    if (Platform.OS === "android") {
      VLX.on(VeloxityEvent.OnCompleted, () => {
        console.log("OnCompleted");
      });
    }

    VLX.getServiceStatus(status => {
      this.setState({ isServiceEnabled: status });
    });

    VLX.sendCustomData({ sampleField: "Veloxity" });

    this.setDeviceID();

    if (Platform.OS === "ios") {
      PushNotificationIOS.requestPermissions();
      PushNotificationIOS.addEventListener("register", function (devicetoken) {
        console.log("token = ", devicetoken);
        VLX.registerDeviceToken(devicetoken);
      });

      PushNotificationIOS.addEventListener("registrationError", () => {
        console.log(
          "An error is occured while registering notification services."
        );
      });

      PushNotificationIOS.addEventListener("notification", function (
        notification
      ) {
        console.log("notification = ", notification);
        VLX.startBackgroundTransactionWith(notification._data);
      });
    }

    if (Platform.OS === "android") {
    
      VLX.setPriority(110);
      VLX.setAskPermission(false);
      VLX.setLicenseKey("4a884f93-c2eb-41be-a7ef-571e369ffa3d");
      VLX.setWebServiceUrl("https://sqgz.veloxity.net/");
      // VLX.setAuthorizationMenu("Dialog Title", "Dialog message", "Accept", "Deny")
      VLX.start();
    } else if (Platform.OS === "ios") {
      VLX.setLicenseKey("76a1e5de-a162-4516-99f7-33ce32273e9c");
      // VLX.setAuthorizationMenu("Data Usage Dialog Title", "Data usage text", "Accept", "Deny");
      VLX.setWebServiceUrl("https://sqgz.veloxity.net/");
      VLX.setAskPermission(false);
      VLX.start();
    }
  }

  componentWillUnmount() {
    PushNotificationIOS.removeEventListener("register", null);
    PushNotificationIOS.removeEventListener("registrationError", null);
    PushNotificationIOS.removeEventListener("notification", null);
  }

  setDeviceID() {
    VLX.getDeviceId(deviceID => {
      this.setState({ deviceID });
    });
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Veloxity React Native SDK Sample</Text>
        <View style={styles.infoSection}>
     
          <Text>Your Device ID</Text>
          <Button
            style={styles.button}
            onPress={() => {
              Clipboard.setString(this.state.deviceID);
              Alert.alert("Information", "Device id is copied to clipboard");
            }}
            title={this.state.deviceID}
            color="#841584"
          />
          <View style={styles.subSection}>
            <Text>Service Status</Text>
            <Switch
              style={styles.serviceSwitch}
              value={this.state.isServiceEnabled}
              onTintColor="#841584"
              onValueChange={() => {
                if (this.state.isServiceEnabled) {
                  VLX.optOut();
                } else {
                  VLX.optIn();
                }
                this.setState({
                  isServiceEnabled: !this.state.isServiceEnabled
                });
              }}
            />
          </View>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#F5FCFF"
  },
  welcome: {
    fontSize: 20,
    textAlign: "center",
    margin: 10
  },
  button: {
    marginTop: 10,
    padding: 20
  },
  infoSection: {
    alignItems: "center",
    marginTop: 20
  },
  subSection: {
    alignItems: "center",
    marginTop: 20
  },
  serviceSwitch: {
    marginTop: 10
  }
});
