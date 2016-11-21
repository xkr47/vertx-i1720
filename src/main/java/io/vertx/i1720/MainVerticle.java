package io.vertx.i1720;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;

/**
 * Test program for https://github.com/eclipse/vert.x/issues/1720
 *
 * To run server:
 *
 * mvn clean package && java -jar target/i1720-1.0-SNAPSHOT-fat.jar
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println();
        System.out.println(" Test program for https://github.com/eclipse/vert.x/issues/1720");
        System.out.println();
        System.out.println(" Now run:  wget -O /dev/null http://localhost:8080/");
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println();
        HttpServerOptions options = new HttpServerOptions().setIdleTimeout(10);
        vertx
                .createHttpServer(options)
                .requestHandler(r -> {
                    System.out.println("Sending file 'target/bigfile'...");
                    r.response().sendFile("target/bigfile", asyncResult -> {
                        if (asyncResult.succeeded()) {
                            System.out.println("File sent successfully");
                        } else {
                            System.err.println("sendFile() reported error:");
                            Throwable t = asyncResult.cause();
                            t.printStackTrace();
                            r.response().setStatusCode(500);
                            r.response().setStatusMessage(t.toString());
                            try {
                                r.response().end();
                            } catch (RuntimeException e) {
                                // ignore
                            }
                        }
                    });
                })
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });
    }
}
