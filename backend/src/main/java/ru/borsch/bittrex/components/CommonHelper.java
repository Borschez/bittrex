package ru.borsch.bittrex.components;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class CommonHelper {

    @Autowired
    private EmbeddedWebApplicationContext appContext;

    public String getBaseUrl() {
        Connector connector = ((TomcatEmbeddedServletContainer) appContext.getEmbeddedServletContainer()).getTomcat().getConnector();
        if (connector == null) return "";
        String scheme = connector.getScheme();
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int port = connector.getPort();
        String contextPath = appContext.getServletContext().getContextPath();
        return scheme + "://" + ip + ":" + port + contextPath;
    }




}
