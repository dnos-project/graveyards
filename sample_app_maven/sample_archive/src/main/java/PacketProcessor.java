import org.onlab.packet.DeserializationException;
import org.onlab.packet.Ethernet;
import org.onlab.packet.IPv4;
import org.onosproject.grpc.net.packet.models.PacketContextProtoOuterClass;

public class PacketProcessor {


    public PacketProcessor() {

    }


    public void process(PacketContextProtoOuterClass.PacketContextProto packetContext) {
        System.out.println("Processing Packet");

        byte[] packetByteArray = packetContext.getInboundPacket().getData().toByteArray();
        Ethernet ethernet = new Ethernet();

        try {
            ethernet = Ethernet.deserializer().deserialize(packetByteArray, 0, packetByteArray.length);
        } catch (DeserializationException e) {
            e.printStackTrace();
        }


        Short ethType = ethernet.getEtherType();
        if (ethType == Ethernet.TYPE_LLDP || ethType == Ethernet.TYPE_BSN || ethType == Ethernet.TYPE_ARP) {

            return;
        }

        IPv4 ipv4Packet = (IPv4) ethernet.getPayload();


        System.out.println(ipv4Packet.getSourceAddress());


        //System.out.println(Hex.encodeHexString(packetByteArray));
        System.out.println();

    }

}
