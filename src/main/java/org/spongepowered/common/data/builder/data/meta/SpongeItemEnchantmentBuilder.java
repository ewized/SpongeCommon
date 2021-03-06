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
package org.spongepowered.common.data.builder.data.meta;

import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.util.persistence.DataBuilder;
import org.spongepowered.api.util.persistence.DataContentUpdater;
import org.spongepowered.api.util.persistence.InvalidDataException;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.SpongeDataManager;
import org.spongepowered.common.data.util.DataUtil;

import java.util.Optional;

public class SpongeItemEnchantmentBuilder implements DataBuilder<ItemEnchantment> {

    private static final int SUPPORTED_VERSION = 1;

    @Override
    public Optional<ItemEnchantment> build(DataView container) throws InvalidDataException {
        if (container.contains(Queries.CONTENT_VERSION)) {
            final int contentVersion = DataUtil.getData(container, Queries.CONTENT_VERSION, Integer.class);
            if (contentVersion < SUPPORTED_VERSION) {
                Optional<DataContentUpdater> updater = SpongeDataManager.getInstance().getWrappedContentUpdater(ItemEnchantment.class, contentVersion, SUPPORTED_VERSION);
                if (!updater.isPresent()) {
                    throw new InvalidDataException("Could not get an updater for ItemEnchantment data from the version: " + contentVersion
                    + " to " + SUPPORTED_VERSION + ". Please notify the SpongePowered developers of this issue!");
                }
                container = updater.get().update(container);
            }
        }
        if (container.contains(Queries.ENCHANTMENT_ID, Queries.LEVEL)) {
            final String enchantmentId = DataUtil.getData(container, Queries.ENCHANTMENT_ID, String.class);
            final int enchantmentLevel = DataUtil.getData(container, Queries.LEVEL, Integer.class);
            final Enchantment enchantment = SpongeImpl.getRegistry().getType(Enchantment.class, enchantmentId).get();
            return Optional.of(new ItemEnchantment(enchantment, enchantmentLevel));
        }
        return Optional.empty();
    }
}
