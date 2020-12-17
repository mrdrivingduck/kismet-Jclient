# kismet-Jclient Development Guide

Author : Mr Dk.

@2018.11 Nanjing, Jiangsu, China

---

## About

*Kismet-Jclient* is a Java client to obtain information from *Kismet* server by *Kismet*'s RESTful API.

As the author has already defined and implemented several types of message to obtain data from *Kismet* server. What should you do if you are not satisfied with these types of message and want to define your own message type?

The main purpose of this document is to guide you how to define your own types of message and obtain wanted data from *Kismet* server.

## Theory

We obtain information from *Kismet* server by Kismet's RESTful API. We must provide two important elements as parameters of our HTTP request towards Kismet's RESTful API - **fields** and **regular expressions**. - [WHY?](https://www.kismetwireless.net/docs/devel/webui_rest/commands/)

**Fields** are used to filter the data in Kismet and get the only fields that you want to get, for example, the `SSID` of a wireless network. These fields should be defined in your `xxxMessage.java` as private fields with public `getters` & `setters`.

**Regular expressions** are used to filter the data with conditions. For example, if you want to get Wi-Fi messages whose `SSID` starts with "xxx", then you should give a regular expression to express the names start with "xxx" to the `SSID` field you want to filter.

## Annotations

### `@ApiUrl`

This annotation should be added on your `xxxMessage.java` class definition, illustrating the RESTful API you want to browse. If you need to pass in parameters (like *timestamp*), you should leave a formatter like `%d`. This annotation is used for automatically generate the url for HTTP request toward *Kismet* server.

### `@MessageType`

This annotation should be added on your `xxxMessage.java` class definition, illustrating the name tag of different types of message.

```java
@MessageType("BSSID")
@ApiUrl("/devices/last-time/%d/devices.json")
public class BSSIDMessage extends KismetMessage {
    // Fields...
}
```

### `@FieldPath` & `@FieldAliase`

These two annotations should be added on the `setters` of your message fields. `@FieldPath` illustrates the path of the field you want to visit in RESTful API. `@FieldAliase` illustrates the aliases of your field. Normally, the value of `@FieldAliase` should be the same as `@FieldPath`; however, if you want to get a common field which will exist **twice or more than twice** in the result (This may happen when two fields have a same common child field), you must set different aliases for the common field. Or the common field will be returned only once in the result and you'll have no idea which parent field it belongs to. If you set different aliases, the common field will be returned in aliases respectively.

```java
@MessageType("BSSID")
@ApiUrl("/devices/last-time/%d/devices.json")
public class BSSIDMessage extends KismetMessage {
    
    int signalDBM;
    
    @FieldPath("kismet.device.base.signal/kismet.common.signal.last_signal")
    @FieldAliase("kismet.common.signal.last_signal")
    public void setSignalDBM(int signalDBM) {
        this.signalDBM = signalDBM;
    }
    
    public int getSignalDBM() {
        return signalDBM;
    }
}
```

## Implementation

Once getting the `Class` object of your `xxxMessage.java`, we use `@ApiUrl` to build a HTTP url with `host` and `port`, and take all `@FieldPath` and regular expressions as the parameters of `HTTP POST` method. Then we send it to *Kismet* server.

Once getting JSON data from *Kismet* server, we use **reflection** to instantiate an `xxxMessage` object, taking all `@FieldAliase` as JSON keys to get value from JSON Object or JSON Array. Then, all `setters` will be automatically invoked and a message object is available and published to all listeners.

## More Details

For more details, see `JClientConnector.java` and you'll easily get to know how it works. You'll see several `generateXXXMessage()` methods, which acts the core function.

* First, use `JsonParamBuilder` to add fields with `Class` object of your message class and add regular expressions, building the parameter string.
* Second, use `UriGenerator` to generate a url for HTTP request.
* Third, use `HttpRequestBuilder` to execute a `POST` or `GET` method towards RESTful API.
* Forth, get result in the form of JSON Object or JSON Array.
* At last, use `publishMsg` to generate message objects and publish it to all listeners.

---

