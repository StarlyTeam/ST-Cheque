package net.starly.cheque.listener;

import net.starly.cheque.ChequeMain;
import net.starly.cheque.context.MessageContent;
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!(event.getHand() == EquipmentSlot.HAND)) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) return;

        ItemStackWrapper itemStackWrapper = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper nbtTagCompoundWrapper = itemStackWrapper.getTag();

        if (nbtTagCompoundWrapper == null) return;
        event.setCancelled(true);

        String stringNumber = nbtTagCompoundWrapper.getString("cheque");
        if (stringNumber == null || stringNumber.isEmpty()) return;

        ChequeMain.getEconomy().depositPlayer(player, Long.parseLong(stringNumber));
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.NORMAL, "useCheque")
                .replace("{money}", nbtTagCompoundWrapper.getString("cheque")));
    }
}
