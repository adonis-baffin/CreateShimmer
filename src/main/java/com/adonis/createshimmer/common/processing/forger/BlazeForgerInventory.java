///*
// * Copyright (C) 2025  DragonsPlus
// * SPDX-License-Identifier: LGPL-3.0-or-later
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <https://www.gnu.org/licenses/>.
// */
//
//package plus.dragons.createenchantmentindustry.common.processing.forger;
//
//import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
//import java.util.Comparator;
//import java.util.Objects;
//import net.minecraft.core.Holder;
//import net.minecraft.core.HolderLookup.Provider;
//import net.minecraft.core.component.DataComponents;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.tags.EnchantmentTags;
//import net.minecraft.world.inventory.AnvilMenu;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.item.enchantment.EnchantmentHelper;
//import net.minecraft.world.item.enchantment.ItemEnchantments;
//import net.minecraft.world.item.enchantment.ItemEnchantments.Mutable;
//import net.neoforged.neoforge.items.ItemStackHandler;
//import plus.dragons.createenchantmentindustry.common.fluids.experience.ExperienceHelper;
//import plus.dragons.createenchantmentindustry.common.processing.enchanter.CEIEnchantmentHelper;
//import plus.dragons.createenchantmentindustry.common.processing.enchanter.EnchantingTemplateItem;
//import plus.dragons.createenchantmentindustry.common.registry.CEIAdvancements;
//import plus.dragons.createenchantmentindustry.common.registry.CEIDataMaps;
//import plus.dragons.createenchantmentindustry.common.registry.CEIItems;
//import plus.dragons.createenchantmentindustry.common.registry.CEIStats;
//import plus.dragons.createenchantmentindustry.config.CEIConfig;
//
//public class BlazeForgerInventory extends ItemStackHandler {
//    private final BlazeForgerBlockEntity forger;
//    private int cost;
//    private int mode; // Advancement Flag. 0 = merge item 1 = apply template 2 = strip down enchantment
//    private boolean conflicting; // Advancement Flag
//    private boolean overCap; // Advancement Flag
//
//    public BlazeForgerInventory(BlazeForgerBlockEntity forger) {
//        super(6);
//        this.forger = forger;
//        this.mode = 0;
//        this.conflicting = false;
//        this.overCap = false;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 1;
//    }
//
//    @Override
//    public int getSlots() {
//        return 4;
//    }
//
//    @Override
//    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
//        if (slot > 1) return stack;
//        if (!stacks.get(2).isEmpty() || !stacks.get(3).isEmpty()) return stack;
//        return super.insertItem(slot, stack, simulate);
//    }
//
//    @Override
//    protected void onLoad() {
//        var level = forger.getLevel();
//        if (level != null && !level.isClientSide)
//            updateResult();
//    }
//
//    @Override
//    protected void onContentsChanged(int slot) {
//        if (slot == 0 || slot == 1)
//            updateResult();
//        forger.notifyUpdate();
//    }
//
//    @Override
//    public void deserializeNBT(Provider provider, CompoundTag nbt) {
//        super.deserializeNBT(provider, nbt);
//        cost = nbt.getInt("Cost");
//        mode = nbt.getInt("Mode");
//        conflicting = nbt.getBoolean("Conflicting");
//        overCap = nbt.getBoolean("OverCap");
//    }
//
//    @Override
//    public CompoundTag serializeNBT(Provider provider) {
//        var nbt = super.serializeNBT(provider);
//        nbt.putInt("Cost", cost);
//        nbt.putInt("Mode", mode);
//        nbt.putBoolean("Conflicting", conflicting);
//        nbt.putBoolean("OverCap", overCap);
//        return nbt;
//    }
//
//    public boolean hasRemainingOutput() {
//        return !stacks.get(2).isEmpty() || !stacks.get(3).isEmpty();
//    }
//
//    protected int getExperienceCost() {
//        return cost == 0 ? 0 : ExperienceHelper.getExperienceForTotalLevel(cost);
//    }
//
//    protected ItemStack extractInput(int slot, boolean simulate) {
//        validateSlotIndex(slot);
//        ItemStack stack = stacks.get(slot);
//        if (stack.isEmpty())
//            return ItemStack.EMPTY;
//        if (!simulate)
//            setStackInSlot(slot, ItemStack.EMPTY);
//        return stack.copy();
//    }
//
//    protected ItemStack getResult(int slot) {
//        if (slot < 0 || slot >= 2) {
//            throw new RuntimeException("Slot " + slot + " not in valid range - [0,2)");
//        }
//        return stacks.get(slot + 4);
//    }
//
//    protected void clearInput() {
//        stacks.set(0, ItemStack.EMPTY);
//        stacks.set(1, ItemStack.EMPTY);
//        cost = 0;
//    }
//
//    protected void applyResult() {
//        stacks.set(2, stacks.get(4).copy());
//        stacks.set(3, stacks.get(5).copy());
//        clearInput();
//
//        forger.advancement.awardStat(CEIStats.FORGE.get(), 1);
//        if (forger.special) {
//            forger.advancement.awardStat(CEIStats.SUPER_ENCHANT.get(), 1);
//            if (overCap) forger.advancement.trigger(CEIAdvancements.TRANSCENDENT_OVERCLOCK.builtinTrigger());
//            if (conflicting) forger.advancement.trigger(CEIAdvancements.PARADOX_FUSION.builtinTrigger());
//        }
//        forger.advancement.trigger(mode == 0 ? CEIAdvancements.BLAZING_FUSION.builtinTrigger() : mode == 1 ? CEIAdvancements.SIGIL_CASTING.builtinTrigger() : CEIAdvancements.MAGIC_UNBINDING.builtinTrigger());
//        forger.advancement.awardStat(CEIStats.FORGE.get(), 1);
//    }
//
//    protected void updateResult() {
//        var base = stacks.get(0).copy();
//        var addition = stacks.get(1).copy();
//        stacks.set(4, base);
//        stacks.set(5, addition);
//        if (base.isEmpty() || addition.isEmpty()) {
//            cost = 0;
//            return;
//        }
//        var baseType = EnchantmentHelper.getComponentType(base);
//        var baseEnchantments = base.getOrDefault(baseType, ItemEnchantments.EMPTY);
//        var additionType = EnchantmentHelper.getComponentType(addition);
//        var additionEnchantments = addition.getOrDefault(additionType, ItemEnchantments.EMPTY);
//        mode = 0;
//        conflicting = false;
//        overCap = false;
//        if (baseType == DataComponents.STORED_ENCHANTMENTS) {
//            if (base.getItem() instanceof EnchantingTemplateItem baseTemplate) {
//                if (addition.getItem() instanceof EnchantingTemplateItem addTemplate) {
//                    if (forger.special && (!baseTemplate.isSpecial() || !addTemplate.isSpecial())) return;
//                    else {
//                        if (additionEnchantments.isEmpty()) {
//                            if (!splitEnchantments(base, addition, baseEnchantments, additionEnchantments)) return;
//                        } else {
//                            if (combineEnchantments(base, addition, baseEnchantments, additionEnchantments))
//                                stacks.set(5, ItemStack.EMPTY);
//                            else return;
//                        }
//                    }
//                }
//            } else if (base.is(Items.ENCHANTED_BOOK)) {
//                if (addition.getItem() instanceof EnchantingTemplateItem template) {
//                    if (forger.special && !template.isSpecial()) return;
//                    if (additionEnchantments.isEmpty()) {
//                        if (baseEnchantments.size() == 1) {
//                            var book = Items.BOOK.getDefaultInstance();
//                            EnchantmentHelper.setEnchantments(addition, baseEnchantments);
//                            stacks.set(4, book);
//                            stacks.set(5, addition);
//                            var enchantment = baseEnchantments.entrySet().stream().findFirst().get();
//                            cost += Math.max(1, enchantment.getKey().value().getAnvilCost() * 2) * enchantment.getIntValue();
//                        } else if (!splitEnchantments(base, addition, baseEnchantments, additionEnchantments)) return;
//                    } else {
//                        if (applyEnchantments(base, baseEnchantments, additionEnchantments)) {
//                            stacks.set(5, ItemStack.EMPTY);
//                        } else return;
//                    }
//                } else if (addition.is(Items.ENCHANTED_BOOK)) {
//                    if (combineEnchantments(base, addition, baseEnchantments, additionEnchantments)) {
//                        stacks.set(5, ItemStack.EMPTY);
//                    } else return;
//                } else return;
//            }
//        } else if (base.is(Items.BOOK) && addition.getItem() instanceof EnchantingTemplateItem template) {
//            if (forger.special && (!template.isSpecial())) return;
//            if (additionEnchantments.isEmpty()) return;
//            else {
//                if (applyEnchantmentsToBook(base, additionEnchantments))
//                    stacks.set(5, ItemStack.EMPTY);
//                else return;
//            }
//        } else {
//            if (addition.getItem() instanceof EnchantingTemplateItem template) {
//                if (forger.special && !template.isSpecial()) return;
//                if (additionEnchantments.isEmpty()) {
//                    if (baseEnchantments.isEmpty()) return;
//                    if (!splitEnchantments(base, addition, baseEnchantments, additionEnchantments)) return;
//                } else {
//                    if (applyEnchantments(base, baseEnchantments, additionEnchantments)) {
//                        stacks.set(5, ItemStack.EMPTY);
//                    } else return;
//                }
//            } else if (addition.is(Items.ENCHANTED_BOOK)) {
//                if (applyEnchantments(base, baseEnchantments, additionEnchantments)) {
//                    stacks.set(5, ItemStack.EMPTY);
//                } else return;
//            } else if (ItemStack.isSameItem(base, addition)) {
//                if (combineEnchantments(base, addition, baseEnchantments, additionEnchantments)) {
//                    stacks.set(5, ItemStack.EMPTY);
//                } else return;
//            } else return;
//        }
//        applyRepairCost(base, addition);
//    }
//
//    protected boolean splitEnchantments(ItemStack base, ItemStack addition, ItemEnchantments baseEnchantments, ItemEnchantments additionEnchantments) {
//        mode = 2;
//        if (baseEnchantments.isEmpty())
//            return false;
//        var registry = Objects.requireNonNull(forger.getLevel()).registryAccess().registryOrThrow(Registries.ENCHANTMENT);
//        var stream = baseEnchantments.keySet().stream().sorted(Comparator.comparingInt(holder -> registry.getId(holder.value())));
//        if (!forger.special) {
//            stream = stream.filter(holder -> !holder.is(EnchantmentTags.CURSE));
//        }
//        var optional = stream.findFirst();
//        if (optional.isEmpty())
//            return false;
//        var enchantment = optional.get();
//        var removedEnchantments = new ItemEnchantments.Mutable(baseEnchantments);
//        removedEnchantments.set(enchantment, 0);
//        EnchantmentHelper.setEnchantments(base, removedEnchantments.toImmutable());
//        int level = baseEnchantments.getLevel(enchantment);
//        if (!forger.special)
//            level = Math.min(level, CEIEnchantmentHelper.maxLevel(enchantment) + (CEIConfig.enchantments().splitEnchantmentRespectLevelExtension.get() ? CEIEnchantmentHelper.levelExtension(enchantment) : 0));
//        addition.enchant(enchantment, level);
//        var multiplier = enchantment.getData(CEIDataMaps.SPLITTING_COST_MULTIPLIER);
//        cost += (int) (Math.max(1, enchantment.value().getAnvilCost() * 2) * level * (multiplier != null ? multiplier : 1));
//        return true;
//    }
//
//    protected boolean applyEnchantments(ItemStack base, ItemEnchantments baseEnchantments, ItemEnchantments additionEnchantments) {
//        mode = 1;
//        int cost = 0;
//        var resultEnchantments = new Mutable(baseEnchantments);
//        boolean applied = false;
//        for (Entry<Holder<Enchantment>> entry : additionEnchantments.entrySet()) {
//            Holder<Enchantment> holder = entry.getKey();
//            int baseLevel = resultEnchantments.getLevel(holder);
//            int additionLevel = entry.getIntValue();
//            int resultLevel = baseLevel == additionLevel ? additionLevel + 1 : Math.max(additionLevel, baseLevel);
//            Enchantment enchantment = holder.value();
//            boolean applicable = base.supportsEnchantment(holder);
//            for (Holder<Enchantment> holder1 : resultEnchantments.keySet()) {
//                if (!holder1.equals(holder) && !Enchantment.areCompatible(holder, holder1)) {
//                    applicable = forger.special && CEIConfig.enchantments().ignoreEnchantmentCompatibility.get();
//                    conflicting = true;
//                    cost++;
//                }
//            }
//
//            if (applicable) {
//                applied = true;
//                int maxLevel = CEIEnchantmentHelper.maxLevel(holder);
//                int extendedMaxLevel = maxLevel + CEIEnchantmentHelper.levelExtension(holder);
//
//                if (resultLevel > extendedMaxLevel) {
//                    resultLevel = extendedMaxLevel;
//                } else if (resultLevel > maxLevel && !forger.special) {
//                    resultLevel = maxLevel;
//                }
//                if (resultLevel > maxLevel) overCap = true;
//
//                resultEnchantments.set(holder, resultLevel);
//                int anvilCost = enchantment.getAnvilCost();
//                anvilCost = Math.max(1, anvilCost / 2);
//
//                var multiplier = holder.getData(CEIDataMaps.FORGING_COST_MULTIPLIER);
//                cost += (int) (anvilCost * resultLevel * (multiplier != null ? multiplier : 1));
//            }
//        }
//        if (!applied)
//            return false;
//        EnchantmentHelper.setEnchantments(base, resultEnchantments.toImmutable());
//        this.cost += cost;
//        return true;
//    }
//
//    protected boolean applyEnchantmentsToBook(ItemStack base, ItemEnchantments additionEnchantments) {
//        mode = 1;
//        int cost = 0;
//        var resultEnchantments = new Mutable(ItemEnchantments.EMPTY);
//        boolean applied = false;
//        for (Entry<Holder<Enchantment>> entry : additionEnchantments.entrySet()) {
//            Holder<Enchantment> holder = entry.getKey();
//            Enchantment enchantment = holder.value();
//            boolean applicable = true;
//            for (Holder<Enchantment> holder1 : resultEnchantments.keySet()) {
//                if (!holder1.equals(holder) && !Enchantment.areCompatible(holder, holder1)) {
//                    applicable = forger.special && CEIConfig.enchantments().ignoreEnchantmentCompatibility.get();
//                    conflicting = applicable;
//                    cost++;
//                }
//            }
//            if (applicable) {
//                applied = true;
//                resultEnchantments.set(holder, entry.getIntValue());
//                int anvilCost = enchantment.getAnvilCost();
//                anvilCost = Math.max(1, anvilCost / 2);
//                var multiplier = holder.getData(CEIDataMaps.FORGING_COST_MULTIPLIER);
//                cost += (int) (anvilCost * entry.getIntValue() * (multiplier != null ? multiplier : 1));
//            }
//        }
//        if (!applied)
//            return false;
//        base = Items.ENCHANTED_BOOK.getDefaultInstance();
//        EnchantmentHelper.setEnchantments(base, resultEnchantments.toImmutable());
//        stacks.set(4, base);
//        this.cost += cost;
//        return true;
//    }
//
//    protected boolean combineEnchantments(ItemStack base, ItemStack addition, ItemEnchantments baseEnchantments, ItemEnchantments additionEnchantments) {
//        boolean applied = false;
//        if (base.isDamaged()) {
//            int baseDurability = base.getMaxDamage() - base.getDamageValue();
//            int additionDurability = addition.getMaxDamage() - addition.getDamageValue();
//            int fix = additionDurability + base.getMaxDamage() * 12 / 100;
//            int resultDurability = baseDurability + fix;
//            int resultDamage = base.getMaxDamage() - resultDurability;
//            if (resultDamage < 0) {
//                resultDamage = 0;
//            }
//
//            if (resultDamage < base.getDamageValue()) {
//                base.setDamageValue(resultDamage);
//                cost += 2;
//                applied = true;
//            }
//        }
//        applied |= applyEnchantments(base, baseEnchantments, additionEnchantments);
//        mode = 0;
//        return applied;
//    }
//
//    protected void applyRepairCost(ItemStack base, ItemStack addition) {
//        if (!forger.cursed)
//            return;
//        int baseCost = base.getOrDefault(DataComponents.REPAIR_COST, 0);
//        int additionCost = addition.getOrDefault(DataComponents.REPAIR_COST, 0);
//        int resultCost = AnvilMenu.calculateIncreasedRepairCost(Math.max(baseCost, additionCost));
//        base.set(DataComponents.REPAIR_COST, resultCost);
//    }
//
//    boolean forgingCompleted() {
//        return !stacks.get(2).isEmpty() && forger.processingTime == -1;
//    }
//
//    boolean notEnoughItemToForge() {
//        return stacks.get(0).isEmpty() || stacks.get(1).isEmpty();
//    }
//
//    boolean incompatibleEnchantingTemplateType() {
//        var base = stacks.get(0);
//        var addition = stacks.get(1);
//        if (!forger.special && (base.is(CEIItems.SUPER_ENCHANTING_TEMPLATE) || addition.is(CEIItems.SUPER_ENCHANTING_TEMPLATE)))
//            return true;
//        else return forger.special && (base.is(CEIItems.ENCHANTING_TEMPLATE) || addition.is(CEIItems.ENCHANTING_TEMPLATE));
//    }
//}
