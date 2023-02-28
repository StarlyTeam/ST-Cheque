package net.starly.cheque.util;

import net.starly.cheque.context.MessageContent;
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChequeItemUtil {

    public static ItemStack getChequeItem(long money, int amount) {
        ItemStack itemStack = new ItemStack(Material.valueOf(MessageContent.getInstance().getMessage(MessageContent.MessageType.CHEQUE, "material")), amount);
        ItemStackWrapper itemStackWrapper = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper nbtTagCompoundWrapper = itemStackWrapper.getTag();

        if(nbtTagCompoundWrapper == null) nbtTagCompoundWrapper = NmsItemStackUtil.getInstance().getNbtCompoundUtil().newInstance();

        nbtTagCompoundWrapper.setString("cheque", String.valueOf(money));
        itemStackWrapper.setTag(nbtTagCompoundWrapper);
        itemStack.setItemMeta(NmsItemStackUtil.getInstance().asBukkitCopy(itemStackWrapper).getItemMeta());
        return itemStack;
    }
}
