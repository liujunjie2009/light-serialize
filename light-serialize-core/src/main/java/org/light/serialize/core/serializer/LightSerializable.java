package org.light.serialize.core.serializer;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;

import java.io.IOException;

/**
 * LightSerializable
 *
 * @author alex
 */
public interface LightSerializable {

    void write(ObjectOutput output) throws IOException;

    void read(ObjectInput input) throws IOException;

}
