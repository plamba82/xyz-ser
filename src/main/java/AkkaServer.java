import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.japi.Function;
import akka.japi.JavaPartialFunction;


import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.http.javadsl.model.ws.BinaryMessage;
import akka.http.javadsl.model.ws.WebSocket;
import akka.util.ByteString;
import com.typesafe.config.Config;


/*
Author : Bibin KT
 */
public class AkkaServer {

    static ActorSystem system = ActorSystem.create();
    static final Materializer materializer = ActorMaterializer.create(system);

    private static ActorRef imeActor = system.actorOf(Props.create(ImeResolverActor.class),
            ImeResolverActor.class.getSimpleName());

    public static HttpResponse handleRequest(HttpRequest request) {
        System.out.println("Handling request to " + request.getUri());
        if (request.getUri().path().equals("/bridge")) {
            final Flow<Message, Message, NotUsed> handleMessage = handleMessage();
            return WebSocket.handleWebSocketRequestWith(request, handleMessage);
        } else {
            return HttpResponse.create().withStatus(404);
        }
    }

    public static void main(String[] args) throws Exception {

        try {
            final Function<HttpRequest, HttpResponse> handler = request -> handleRequest(request);
            CompletionStage<ServerBinding> serverBindingFuture =
                    Http.get(system).bindAndHandleSync(
                            handler, ConnectHttp.toHost("localhost", 9000), materializer);

            // will throw if binding fails
            serverBindingFuture.toCompletableFuture().get(1, TimeUnit.SECONDS);
            System.out.println("Press ENTER to stop.");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            //system.terminate();
        }
    }
    int numMessagesReceived=0;
    final Sink<Message, CompletionStage<Done>> wsSink = Sink.foreach((message) -> {
        numMessagesReceived += 1;
    });


    public static Flow<Message, Message, NotUsed> handleMessage() {
        return
                Flow.<Message>create()
                        .collect(new JavaPartialFunction<Message, Message>() {
                            @Override
                            public Message apply(Message msg, boolean isCheck) throws Exception {
                                if(msg.asBinaryMessage().isStrict()) {
                                    System.out.println("Strict data !!!"+msg.asBinaryMessage().getStrictData().mkString());
                                    imeActor.tell(msg.asBinaryMessage().getStrictData(),imeActor);
                                    return TextMessage.create("100");
                                }else{

                                    System.out.println("Streaming data");
                                    Source<ByteString, ?> streamedData = msg.asBinaryMessage().getStreamedData();

                                    CompletionStage<ByteString> byteStringCompletionStage =
                                            streamedData.runFold(ByteString.empty(), (aggr, next) -> aggr.concat(next), materializer);
                                    byteStringCompletionStage.thenApply(new java.util.function.Function<ByteString, Object>() {

                                        @Override
                                        public Object apply(ByteString message){
                                            System.out.println("hi bibin got input -->"+message);
                                            imeActor.tell(message,imeActor);
                                            return message;
                                        }
                                    });
                                    return TextMessage.create("200");
                                }
                            }
                        });
    }
}
