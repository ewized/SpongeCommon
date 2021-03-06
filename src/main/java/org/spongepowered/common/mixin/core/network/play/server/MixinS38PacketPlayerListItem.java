/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.core.network.play.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.network.IMixinS38PacketPlayerListItem$AddPlayerData;

import java.util.List;

@Mixin(S38PacketPlayerListItem.class)
public class MixinS38PacketPlayerListItem {

    private static final String CTOR_VARARG =
            "<init>(Lnet/minecraft/network/play/server/S38PacketPlayerListItem$Action;[Lnet/minecraft/entity/player/EntityPlayerMP;)V";

    @Shadow @Final private List<S38PacketPlayerListItem.AddPlayerData> players;

    @Inject(method = CTOR_VARARG, at = @At("RETURN"))
    private void onPlayerAdd(S38PacketPlayerListItem.Action action, EntityPlayerMP[] players, CallbackInfo callbackInfo) {
        for (int i = 0; i < this.players.size(); i++) {
            S38PacketPlayerListItem.AddPlayerData playerData = this.players.get(i);
            IMixinS38PacketPlayerListItem$AddPlayerData actualData = (IMixinS38PacketPlayerListItem$AddPlayerData) playerData;
            EntityPlayerMP player = players[i];
            actualData.setPlayer(player);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/network/play/server/S38PacketPlayerListItem$Action;Ljava/lang/Iterable;)V", at = @At("RETURN"))
    private void onPlayerAdd(S38PacketPlayerListItem.Action action, Iterable<EntityPlayerMP> players, CallbackInfo callbackInfo) {
        int index = 0;
        for (EntityPlayerMP playerMP : players) {
            S38PacketPlayerListItem.AddPlayerData playerData = this.players.get(index++);
            IMixinS38PacketPlayerListItem$AddPlayerData actualData = (IMixinS38PacketPlayerListItem$AddPlayerData) playerData;
            actualData.setPlayer(playerMP);
        }
    }

}
