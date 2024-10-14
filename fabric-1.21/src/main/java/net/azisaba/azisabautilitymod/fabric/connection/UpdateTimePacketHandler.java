package net.azisaba.azisabautilitymod.fabric.connection;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.azisaba.azisabautilitymod.fabric.Mod;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.LongStream;

public class UpdateTimePacketHandler extends ChannelInboundHandlerAdapter {
    public static final Map<UUID, String> admin = new ConcurrentHashMap<>();
    public static final Map<UUID, String> uuidToNameMap = new ConcurrentHashMap<>();
    public final List<Long> times = new CopyOnWriteArrayList<>();

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if (msg instanceof WorldTimeUpdateS2CPacket) {
            times.add(System.currentTimeMillis());
        }
        if (msg instanceof PlayerListS2CPacket packet) {
            if (packet.getActions().contains(PlayerListS2CPacket.Action.ADD_PLAYER)) {
                for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
                    if (entry.profile() == null) continue;
                    if (entry.profile().getName() != null) {
                        uuidToNameMap.put(entry.profileId(), entry.profile().getName());
                    }
                    if (admin.containsKey(entry.profileId())) continue;
                    new Thread(() -> {
                        StringBuilder sb = new StringBuilder(3 * 10);
                        var obj = Mod.requestGson("https://api-ktor.azisaba.net/players/" + entry.profileId(), JsonObject.class);
                        if (obj.get("groups").getAsJsonArray().contains(new JsonPrimitive("developer"))) {
                            sb.append("§b").append("d+");
                        }
                        if (obj.get("groups").getAsJsonArray().contains(new JsonPrimitive("alladmin"))) {
                            sb.append("§c").append("a+");
                        }
                        var servers = obj.get("servers").getAsJsonObject();
                        for (Admins value : Admins.values()) {
                            if (!servers.has(value.name().toLowerCase())) continue;
                            var server = servers.get(value.name().toLowerCase()).getAsJsonObject();
                            if (server.get("admin").getAsBoolean()) {
                                sb.append("§c").append(value.chr);
                            } else if (server.get("moderator").getAsBoolean()) {
                                sb.append("§6").append(value.chr);
                            } else if (server.get("builder").getAsBoolean()) {
                                sb.append("§e").append(value.chr);
                            }
                        }
                        admin.put(entry.profileId(), sb.toString());
                    }).start();
                }
            }
        }
        super.channelRead(ctx, msg);
    }

    public @NotNull LongStream timesStream() {
        return times.stream().mapToLong(l -> l);
    }

    public double getAverageMsPerSecond(long period) {
        List<Long> times = new ArrayList<>();
        long lastTime = -1;
        for (long l : timesStream().filter(t -> System.currentTimeMillis() - t <= period).toArray()) {
            if (lastTime != -1) {
                times.add(l - lastTime);
            }
            lastTime = l;
        }
        return times.stream().mapToLong(l -> l).average().orElse(0.0);
    }

    private enum Admins {
        Life('L'),
        LGW('G'),
        TSL('T'),
        Vanilife('V'),
        Lobby('O'),
        Afnw2('W'),
        JG('J'),
        ;

        public final char chr;

        Admins(char chr) {
            this.chr = chr;
        }
    }
}
