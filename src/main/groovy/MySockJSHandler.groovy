import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.FaviconHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.SockJSHandler

class MySockJSHandler extends AbstractVerticle {

    static void main(def args) {
        Vertx.vertx().deployVerticle(this.newInstance())
    }

    @Override
    void start() throws Exception {
        def server = vertx.createHttpServer()

        def router = Router.router(vertx)

        def inboundPermitted1 = [
                address: "eventsIn"
        ]

        def outboundPermitted1 = [
                address: "eventsIn"
        ]

        def options = [
                inboundPermitteds : [
                        inboundPermitted1
                ],
                outboundPermitteds: [
                        outboundPermitted1
                ]
        ]

        def sockJSHandler = SockJSHandler.create(vertx).bridge(options)

        router.route("/eventbus/*").handler(sockJSHandler)

        router.route().handler(StaticHandler.create())

        router.route().handler(FaviconHandler.create())

        server.requestHandler(router.&accept).listen(8080, {
            if (it.succeeded()) {
                println("success")
            } else {
                println(it.cause().message)
            }
        })
    }
}
