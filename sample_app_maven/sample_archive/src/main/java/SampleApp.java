import api.eventServiceApi.EventListener;
import com.google.protobuf.InvalidProtocolBufferException;
import core.notificationService.eventConsumerService.EventConsumerService;
import core.notificationService.packetService.PacketEventMonitor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Bytes;
import org.json.simple.JSONObject;
import org.onosproject.grpc.net.packet.models.PacketContextProtoOuterClass.PacketContextProto;
import restapihelper.JsonBuilder;

import java.io.BufferedReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SampleApp {


    public static void main(String args[]) {

        PacketProcessor packetProcessor = new PacketProcessor();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String appName = "PacketConsumerApp";
        String EventType = "PACKET";
        JsonBuilder jsonBuilder = new JsonBuilder();
        EventConsumerService monitor = new EventConsumerService();
        BufferedReader PacketsBufferReader = monitor.kafkaRegister(appName);
        JSONObject registerReponse = jsonBuilder.createJsonObject(PacketsBufferReader);
        monitor.kafkaSubscribe(EventType, appName, registerReponse);
        PacketEventMonitor packetInEventMonitor = new PacketEventMonitor(registerReponse, EventType);


        class PacketInEventListener extends EventListener {


            @Override
            public void onEvent(ConsumerRecord<Long, Bytes> record) {



                PacketContextProto packetContextProto = null;
                try {
                    packetContextProto = PacketContextProto.parseFrom(record.value().get());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }



                PacketContextProto finalPacketContextProto = packetContextProto;
                executorService.execute(() -> {
                    packetProcessor.process(finalPacketContextProto);
                });
            }
        }



        PacketInEventListener packetInEventListener = new PacketInEventListener();
        packetInEventMonitor.addEventListener(packetInEventListener);
        Thread t = new Thread(packetInEventMonitor);
        t.start();

    }

}
