package net.starly.cheque.util;

import net.starly.cheque.context.MessageContent;
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ChequeItemUtil {

    @SuppressWarnings("ConstantConditions")
    public static ItemStack getChequeItem(long money, int amount) {
        ItemStack itemStack = new ItemStack(Material.valueOf(MessageContent.getInstance().getMessage(MessageContent.MessageType.CHEQUE, "material")), amount);
        ItemStackWrapper itemStackWrapper = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper nbtTagCompoundWrapper = itemStackWrapper.getTag();

        if (nbtTagCompoundWrapper == null) nbtTagCompoundWrapper = NmsItemStackUtil.getInstance().getNbtCompoundUtil().newInstance();

        nbtTagCompoundWrapper.setString("cheque", String.valueOf(money));
        itemStackWrapper.setTag(nbtTagCompoundWrapper);

        ItemMeta itemMeta = NmsItemStackUtil.getInstance().asBukkitCopy(itemStackWrapper).getItemMeta();

        itemMeta.setDisplayName(MessageContent.getInstance().getMessage(MessageContent.MessageType.CHEQUE, "displayname")
                .replace("{money}", String.valueOf(money)));

        itemMeta.setLore(MessageContent.getInstance().getMessages(MessageContent.MessageType.CHEQUE, "lore")
                .stream()
                .map(s -> s.replace("{money}", String.valueOf(money))).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
