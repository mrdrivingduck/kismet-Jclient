# kismet-Jclient
A Java client for Kismet REST API.

Author : Mr Dk.

@2018.10 Nanjing, Jiangsu, China

---

### About

A Java client to get information from _Kismet Server_ by Kismet's _REST API_

### Environment

* _Kismet_ release `2018-08-beta1` - Supporting _REST API_
* `JDK` & `JRE` - Versions above 1.8

### Dependency

* `HttpClient 4.5` - A part of _Apache HttpComponents&trade;_ - [link](http://hc.apache.org/)

* `fastjson` - A fast JSON parser/generator for Java - [link](https://github.com/alibaba/fastjson/wiki)

All dependency _JAR_ files can be found in the directory `/lib`

### Usage

1. Instantiation a `JClientConnector` with `host` & `port` on which _Kismet httpd service_ is running
2. Instantiation a `JClientListener` and override methods `onInformation` & `onTerminate`
3. Subscribe specific _information types_ for `JClientListener`
4. Register the `JClientListener` onto the `JClientConnector`

```java
public class Main {
    
    public static void main(String[] args) {
        JClientConnector conn = new JClientConnector("localhost", 2501);
        JClientListener listener = new JClientListener(){
            @Override
            public void onTerminate(String reason) {
                System.out.println(reason);
            }
        
            @Override
            public void onInformation(KismetInfo info) {
                System.out.println(info);
            }
        };
        
        listener.subscribe(TimestampInfo.class);
        listener.subscribe(MessageInfo.class);
        listener.subscribe(WiFiAPInfo.class);
        conn.register(listener);
        
        // conn.kill();
    }
    
}
```

### Features

* The subscription for different types of information can be customized by calling `listener.subscribe(XXX.class);`
* The operation to different types of information can be customized by instantiate different `JclientListener` and override method `onInformation` & `onTerminate`
* Can be integrated into another Java project easily

---

Developing to support more types of _Information_