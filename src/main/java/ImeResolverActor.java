import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ws.BinaryMessage;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Random;

public class ImeResolverActor extends AbstractActor {

    ActorSystem system = ActorSystem.create();

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ByteString.class, this::handleProcessRequest)
                .matchAny(o ->  {
                    System.out.println("Hi Invalid Message");
                    unhandled(o);
                }).build();
    }

    private void handleProcessRequest(ByteString message) throws Exception{

        Random rand = new Random();
        int num = rand.nextInt(900000) + 100000;
        // System.out.println(num);
        String text = String.valueOf(num);
        byte [] data = message.toArray();
        InputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("C:\\Users\\lambapi\\Documents\\MyJabberFiles\\bibin.thiruvoth@verizon.com\\peronal\\AIModel\\Imesamples\\"+text+".jpg"));
        System.out.println("image created");

        System.out.println("Hi received the message !!!"+message.mkString());
    }
}
