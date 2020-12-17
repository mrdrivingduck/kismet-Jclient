# kismet-Jclient

🌉 A Java client for Kismet RESTful API.

Created by : Mr Dk.

@2018.10, Nanjing, Jiangsu, China

---

## About

A Java client to get information from *Kismet* server by *Kismet*'s RESTful API

## Environment

* *Kismet* release `2018-08-beta1` - Supporting RESTful API
* Java 8

## Dependency

* `HttpClient` - A part of *Apache HttpComponents&trade;* - [link](http://hc.apache.org/)
* `fastjson` - A fast JSON parser/generator for Java - [link](https://github.com/alibaba/fastjson/wiki)
* `Commons-csv` - A CSV parser of *Apache Commons CSV&trade;* - [link](https://commons.apache.org/proper/commons-csv/)

## Usage

1. Instantiation a `JClientConnector` with `host` & `port` on which **Kismet httpd service** is running
2. Instantiation a `JClientListener` and override methods `onMessage()` & `onTerminate()`
3. Subscribe specific message types for `JClientListener`
4. Register the `JClientListener` onto the `JClientConnector`

```java
public class Main {
    
    public static void main(String[] args) {
        JClientConnector conn = new JClientConnector("localhost", 2501);
        JClientListener listener = new JClientListener() {
            
            @Override
            public void onTerminate(String reason) {
                System.out.println(reason);
            }
        
            @Override
            public void onMessage(KismetMessage msg) {
                System.out.println(msg);
            }
        };
        
        listener.subscribe(TimeMessage.class);
        // subscribe more messages
        conn.register(listener);
        
        // conn.kill();
    }
    
}
```

## Features

* The subscription for different types of message can be customized by calling `listener.subscribe(XXX.class);`
* The operation to different types of message can be customized by instantiate different `JclientListener` and override method `onMessage` & `onTerminate`
* Can be integrated into another Java project easily

## Extension

You can develop your own message types according to your need. View `doc/dev.md` to see how to develop a new message type with current framework.

## License

Copyright © 2018-2019, Jingtang Zhang. ([MIT License](LICENSE))

---

