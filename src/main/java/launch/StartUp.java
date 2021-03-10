package launch;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Description
 *
 * @author yumingtao2008@gmail.com
 * @date 3/10/21 10:43 AM
 */
public class StartUp {
    public static void main(String[] args) throws LifecycleException {
        String webAppDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8088";
        }

        tomcat.setPort(Integer.valueOf(webPort));

        //trigger tomcat to create a default connector, and set the connector to tomcat
        //Tomcat 9 no longer automatically adds a Connector to the server, but tomcat 8
        //If use tomcat8, this line can be commented
        //Refer to https://stackoverflow.com/questions/48998387/code-works-with-embedded-apache-tomcat-8-but-not-with-9-whats-changed
        //Other useful references https://nkonev.name/post/101
        tomcat.setConnector(tomcat.getConnector());

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webAppDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir:" + new File("./" + webAppDirLocation).getAbsolutePath());

        File additionalWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionalWebInfClasses.getAbsolutePath(), "/"));

        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}
