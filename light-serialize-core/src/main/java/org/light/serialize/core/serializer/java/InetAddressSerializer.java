package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;

import java.io.IOException;
import java.net.InetAddress;

/**
 * InetAddressSerializer
 *
 * @author alex
 */
public class InetAddressSerializer extends Serializer<InetAddress> {

    public InetAddressSerializer(Class<? extends InetAddress> type) {
        super(type);
    }

    @Override
    public InetAddress read(ObjectInput input) throws IOException {
        String hostName = input.readString();
        byte[] address = input.readBytes(input.readVarInt());
        return InetAddress.getByAddress(hostName, address);
    }

    @Override
    public void write(ObjectOutput output, InetAddress value) throws IOException {
        String hostName = value.getHostName();
        byte[] address = value.getAddress();

        output.writeString(hostName);
        output.writeVarInt(address.length);
        output.writeBytes(address);
    }
}
