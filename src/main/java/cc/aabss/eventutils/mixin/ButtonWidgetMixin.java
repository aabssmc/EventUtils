package cc.aabss.eventutils.mixin;

import cc.aabss.eventutils.EventUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ButtonWidget.class)
public abstract class ButtonWidgetMixin extends PressableWidget {
    public ButtonWidgetMixin(int i, int j, int k, int l, Text text) {
        super(i, j, k, l, text);
    }

    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    private void onPress(CallbackInfo ci){
        if (!EventUtils.MOD.config.confirmDisconnect || !Text.translatable("menu.disconnect").equals(getMessage())) return;
        final MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || !(client.currentScreen instanceof GameMenuScreen)) return;

        ci.cancel();
        final Screen current = client.currentScreen;
        client.setScreen(new ConfirmScreen(yes -> {
            if (yes) {
                disconnect();
                return;
            }
            client.setScreen(current);
        }, Text.literal("Confirm Disconnect"), Text.literal("Are you sure you want to disconnect?")));
    }

    @Unique
    private void disconnect() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        boolean bl = client.isInSingleplayer();
        ServerInfo serverInfo = MinecraftClient.getInstance().getCurrentServerEntry();
        client.world.disconnect();
        if (bl) {
            client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        } else {
            client.disconnect();
        }
        TitleScreen titleScreen = new TitleScreen();
        if (bl) {
            client.setScreen(titleScreen);
        } else if (serverInfo != null && serverInfo.isRealm()) {
            client.setScreen(new RealmsMainScreen(titleScreen));
        } else {
            client.setScreen(new MultiplayerScreen(titleScreen));
        }
    }
}
