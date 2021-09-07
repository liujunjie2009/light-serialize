package org.light.serialize.core.serializer.java;

import org.light.serialize.core.io.ObjectInput;
import org.light.serialize.core.io.ObjectOutput;
import org.light.serialize.core.serializer.Serializer;
import org.light.serialize.core.util.TypeResolver;

import java.io.IOException;

/**
 * UnicodeBlockSerializer
 *
 * @author alex
 */
public class UnicodeBlockSerializer extends Serializer<Character.UnicodeBlock> {

    public UnicodeBlockSerializer() {
        super(Character.UnicodeBlock.class);
    }

    @Override
    public void write(ObjectOutput output, Character.UnicodeBlock value) throws IOException {
        output.writeString(value.toString());
    }

    @Override
    public Character.UnicodeBlock read(ObjectInput input) throws IOException {
        String blockName = input.readString();
        return blockName == null ? null : Character.UnicodeBlock.forName(blockName);
    }
}
