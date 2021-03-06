package service;

import net.BroadcastPackets;
import net.BroadcastSocket;
import types.Broadcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class BroadcastService extends BroadcastSocket {
    /**
     * Constructor
     *
     * @param port (used for listening on broadcast channel)
     */
    public BroadcastService(int port) throws SocketException {
        super(port);
    }

    /**
     * Handle broadcast packets
     * @param header byte
     * @param data buffer
     * @param address remote
     */
    @Override
    public void HandlePacket(byte header, ByteBuffer data, InetAddress address) {
        try {
            switch (header) {
                case Broadcast.HEADER_NM_HELO:
                    PacketHelo(data, address);
                    break;
                default:
                    System.out.printf("Invalid header: %d from %s\n", header, address.getHostAddress());
            }
        } catch(IOException e) {
            // TODO: Error handling
        }
    }

    /**
     * Discovery
     * @param data (containing packet data)
     */
    public void PacketHelo(ByteBuffer data, InetAddress address) throws IOException {
        // Verify magic
        String magic = new String(data.array(), "ASCII");
        if (!magic.equals(Broadcast.HEADER_MAGIC))
            return;

        // Answer
        BroadcastPackets.SendEhlo(GetSocket(), address, 59607);
    }
}
