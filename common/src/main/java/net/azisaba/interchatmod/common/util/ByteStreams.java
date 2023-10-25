package net.azisaba.interchatmod.common.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ByteStreams {
    public static byte @NotNull [] readFully(@NotNull InputStream is) throws IOException {
        byte[] bytes = new byte[is.available()];
        if (bytes.length > 0 && is.read(bytes) == -1) {
            throw new RuntimeException("read nothing");
        }
        return bytes;
    }

    @Contract("_, _ -> new")
    public static @NotNull String readString(@NotNull InputStream is, Charset charset) throws IOException {
        return new String(readFully(is), charset);
    }
}
