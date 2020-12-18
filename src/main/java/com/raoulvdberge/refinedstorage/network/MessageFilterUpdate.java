package com.raoulvdberge.refinedstorage.network;

import com.raoulvdberge.refinedstorage.container.ContainerFilter;
import com.raoulvdberge.refinedstorage.item.ItemFilter;
import com.raoulvdberge.refinedstorage.tile.config.FilterType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageFilterUpdate extends MessageHandlerPlayerToServer<MessageFilterUpdate> implements IMessage {
    private int compare;
    private int mode;
    private boolean modFilter;
    private String name;
    private FilterType type;

    public MessageFilterUpdate() {
    }

    public MessageFilterUpdate(int compare, int mode, boolean modFilter, String name, FilterType type) {
        this.compare = compare;
        this.mode = mode;
        this.modFilter = modFilter;
        this.name = name;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        compare = buf.readInt();
        mode = buf.readInt();
        modFilter = buf.readBoolean();
        name = ByteBufUtils.readUTF8String(buf);
        type = FilterType.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(compare);
        buf.writeInt(mode);
        buf.writeBoolean(modFilter);
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeInt(type.ordinal());
    }

    @Override
    public void handle(MessageFilterUpdate message, EntityPlayerMP player) {
        if (player.openContainer instanceof ContainerFilter) {
            ItemFilter.setCompare(((ContainerFilter) player.openContainer).getStack(), message.compare);
            ItemFilter.setMode(((ContainerFilter) player.openContainer).getStack(), message.mode);
            ItemFilter.setModFilter(((ContainerFilter) player.openContainer).getStack(), message.modFilter);
            ItemFilter.setName(((ContainerFilter) player.openContainer).getStack(), message.name);
            ItemFilter.setType(((ContainerFilter) player.openContainer).getStack(), message.type);
        }
    }
}
