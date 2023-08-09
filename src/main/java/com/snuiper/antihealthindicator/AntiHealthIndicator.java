package com.snuiper.antihealthindicator;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedAttribute;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public final class AntiHealthIndicator extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        MinecraftVersion minecraftVersion = MinecraftVersion.getServerVersion();
        int serverVersion = minecraftVersion.getVersion();

        Field modifierFiled;
        try {
            modifierFiled = WrappedAttribute.class.getDeclaredField("modifier");
        } catch (NoSuchFieldException e) {
            System.err.println("--------------------------------------");
            System.err.println("|| Anti health indicator bug!!!");
            System.err.println("|| Getting modifier field");
            System.err.println("|| Exception: " + e);
            System.err.println("--------------------------------------");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        modifierFiled.setAccessible(true);

        getServer().getPluginManager().registerEvents(this, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.UPDATE_ATTRIBUTES, PacketType.Play.Server.ENTITY_EFFECT) {
            private final HashMap<Entity, Object> attributes = new HashMap<>();
            private final HashMap<Entity, Double> baseValue = new HashMap<>();

            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.isPlayerTemporary()) return;
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                int entityId = packet.getIntegers().read(0);
                if (player.getEntityId() == entityId) {
                    if (packet.getType() == PacketType.Play.Server.UPDATE_ATTRIBUTES) {
                        Object value = baseValue.getOrDefault(player, null);
                        Object list = attributes.getOrDefault(player, null);
                        for (WrappedAttribute attribute : packet.getAttributeCollectionModifier().read(0)) {
                            if (attribute.getAttributeKey().equals("generic.max_health") || attribute.getAttributeKey().equals("generic.maxHealth")) {
                                try {
                                    StructureModifier<Object> modifier = (StructureModifier<Object>) modifierFiled.get(attribute);
                                    if (value != null) {
                                        baseValue.remove(player);
                                        modifier.withType(double.class).write(0, value);
                                    }
                                    if (list != null) {
                                        attributes.remove(player);
                                        modifier.withType(Collection.class).write(0, list);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    return;
                }
                Entity entity = null;
                for (Entity e : player.getWorld().getEntities()) {
                    if (e.getEntityId() == entityId) {
                        entity = e;
                        break;
                    }
                }
                if (!(entity instanceof LivingEntity)
                        || entity instanceof EnderDragon || entity instanceof Wither
                        || player.getVehicle() == entity) {
                    return;
                }

                if (packet.getType() == PacketType.Play.Server.ENTITY_METADATA) {
                    packet = packet.deepClone();
                    event.setPacket(packet);
                    if (serverVersion >= MinecraftVersion.m1_19_3.getVersion()) {
                        for (WrappedDataValue watchable : packet.getDataValueCollectionModifier().read(0)) {
                            if (!(watchable.getValue() instanceof Float) || (float) watchable.getValue() <= 0.01D) {
                                continue;
                            }
                            if (watchable.getIndex() == 9) { //Health
                                if (entity instanceof IronGolem) {
                                    IronGolem ironGolem = (IronGolem) entity;
                                    double healthPercentage = getHealthPercentage(ironGolem.getHealth(), ironGolem.getMaxHealth());
                                    if (healthPercentage >= 0.75d) {
                                        watchable.setValue((float)ironGolem.getMaxHealth() * 0.75f);
                                    } else if (healthPercentage >= 0.50d) {
                                        watchable.setValue((float)ironGolem.getMaxHealth() * 0.5f);
                                    } else if (healthPercentage >= 0.25d) {
                                        watchable.setValue((float)ironGolem.getMaxHealth() * 0.25f);
                                    } else {
                                        watchable.setValue(1f);
                                    }
                                } else {
                                    watchable.setValue(1f);
                                }
                            } else if (watchable.getIndex() == 15) { //Additional Hearts (AKA golden hearts)
                                watchable.setValue(0f);
                            }
                        }
                    } else {
                        int healthIndex = 9;
                        int additionalHeartsIndex = 15;

                        if (serverVersion <= MinecraftVersion.m1_9_4.getVersion()) {
                            healthIndex = 6;
                            additionalHeartsIndex = 10;
                        }else if (serverVersion <= MinecraftVersion.m1_16_5.getVersion()) {
                            healthIndex = 8;
                            additionalHeartsIndex = 14;
                        }

                        for (WrappedWatchableObject watchable : packet.getWatchableCollectionModifier().read(0)) {
                            if (!(watchable.getValue() instanceof Float) || (float) watchable.getValue() <= 0.01D) {
                                continue;
                            }
                            if (watchable.getIndex() == healthIndex) { //Health
                                if (entity instanceof IronGolem) {
                                    IronGolem ironGolem = (IronGolem) entity;
                                    double healthPercentage = getHealthPercentage(ironGolem.getHealth(), ironGolem.getMaxHealth());
                                    if (healthPercentage >= 0.75d) {
                                        watchable.setValue((float)ironGolem.getMaxHealth() * 0.75f);
                                    } else if (healthPercentage >= 0.50d) {
                                        watchable.setValue((float)ironGolem.getMaxHealth() * 0.5f);
                                    } else if (healthPercentage >= 0.25d) {
                                        watchable.setValue((float)ironGolem.getMaxHealth() * 0.25f);
                                    } else {
                                        watchable.setValue(1f);
                                    }
                                } else {
                                    watchable.setValue(1f);
                                }
                            } else if (watchable.getIndex() == additionalHeartsIndex) { //Additional Hearts (AKA golden hearts)
                                watchable.setValue(0f);
                            }
                        }
                    }
                } else if (packet.getType() == PacketType.Play.Server.UPDATE_ATTRIBUTES) {
                    for (WrappedAttribute attribute : packet.getAttributeCollectionModifier().read(0)) {
                        if (attribute.getAttributeKey().equals("generic.max_health") || attribute.getAttributeKey().equals("generic.maxHealth")) {
                            try {
                                StructureModifier<Object> modifier = (StructureModifier<Object>) modifierFiled.get(attribute);
                                double maxHealth = (Double) modifier.withType(double.class).read(0);
                                if (entity instanceof Player) {
                                    baseValue.put(entity, maxHealth);
                                }
                                if (entity instanceof Golem) {
                                    modifier.withType(double.class).write(0, maxHealth);
                                } else {
                                    modifier.withType(double.class).write(0, 20d);
                                    attributes.put(entity, modifier.withType(Collection.class).read(0));
                                    modifier.withType(Collection.class).write(0, Collections.EMPTY_LIST);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    @EventHandler
    public void onMount(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player)) {
            return;
        }
        Vehicle vehicle = event.getVehicle();
        Player player = (Player) event.getEntered();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            if (!vehicle.isValid() || !player.isValid() || player.getVehicle() != vehicle) {
                return;
            }
            ProtocolLibrary.getProtocolManager().updateEntity(vehicle, Collections.singletonList(player));
        });
    }

    @EventHandler
    public void onDismount(VehicleExitEvent event) {
        if (!(event.getExited() instanceof Player)) {
            return;
        }
        Vehicle vehicle = event.getVehicle();
        Player player = (Player) event.getExited();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> ProtocolLibrary.getProtocolManager().updateEntity(vehicle, Collections.singletonList(player)));
    }

    private double getHealthPercentage(double health, double maxHealth) {
        return health / maxHealth;
    }
}
