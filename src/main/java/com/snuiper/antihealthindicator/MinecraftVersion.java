package com.snuiper.antihealthindicator;

import org.bukkit.Bukkit;

public enum MinecraftVersion {
    m1_9_0(1, 9, 0),
    m1_9_2(1, 9, 2),
    m1_9_4(1, 9, 4),
    m1_10_2(1, 10, 2),
    m1_11_0(1, 11, 0),
    m1_11_1(1, 11, 1),
    m1_11_2(1, 11, 2),
    m1_12_0(1, 12, 0),
    m1_12_1(1, 12, 1),
    m1_12_2(1, 12, 2),
    m1_13_0(1, 13, 0),
    m1_13_1(1, 13, 1),
    m1_13_2(1, 13, 2),
    m1_14_0(1, 14, 0),
    m1_14_1(1, 14, 1),
    m1_14_2(1, 14, 2),
    m1_14_3(1, 14, 3),
    m1_14_4(1, 14, 4),
    m1_15_0(1, 15, 0),
    m1_15_1(1, 15, 1),
    m1_15_2(1, 15, 2),
    m1_16_1(1, 16, 1),
    m1_16_2(1, 16, 2),
    m1_16_3(1, 16, 3),
    m1_16_4(1, 16, 4),
    m1_16_5(1, 16, 5),
    m1_17_0(1, 17, 0),
    m1_17_1(1, 17, 1),
    m1_18_0(1, 18, 0),
    m1_18_1(1, 18, 1),
    m1_18_2(1, 18, 2),
    m1_19_0(1, 19, 0),
    m1_19_1(1, 19, 1),
    m1_19_2(1, 19, 2),
    m1_19_3(1, 19, 3),
    m1_19_4(1, 19, 4),
    mUNKNOWN(Integer.MAX_VALUE, 0, 0);

    private static MinecraftVersion serverVersion;

    public static MinecraftVersion getServerVersion() {
        if (serverVersion == null) {
            String minecraftVersionString = Bukkit.getBukkitVersion();
            String[] minecraftVersionNumbers = minecraftVersionString.substring(0, minecraftVersionString.indexOf('-')).split("\\.");
            String major;
            String minor;
            String build;
            major = minecraftVersionNumbers[0];
            minor = minecraftVersionNumbers[1];
            if (minecraftVersionNumbers.length > 2) {
                build = minecraftVersionNumbers[2];
            } else {
                build = "0";
            }
            try {
                serverVersion = MinecraftVersion.valueOf("m" + major + "_" + minor + "_" + build);
            } catch (IllegalArgumentException e) {
                serverVersion = mUNKNOWN;
            }
        }
        return serverVersion;
    }

    public static String getMinecraftServerPackage(MinecraftVersion version) {
        switch (version) {
            case m1_9_0:
            case m1_9_2:
            case m1_9_4:
            case m1_10_2:
            case m1_11_0:
            case m1_11_1:
            case m1_11_2:
            case m1_12_0:
            case m1_12_1:
            case m1_12_2:
            case m1_13_0:
            case m1_13_1:
            case m1_13_2:
            case m1_14_0:
            case m1_14_1:
            case m1_14_2:
            case m1_14_3:
            case m1_14_4:
            case m1_15_0:
            case m1_15_1:
            case m1_15_2:
            case m1_16_1:
            case m1_16_2:
            case m1_16_3:
            case m1_16_4:
            case m1_16_5:
                return "net.minecraft.server.v" + version.major + "_" + version.minor + "_" + getRNumber(version);
            case m1_17_0:
            case m1_17_1:
            case m1_18_0:
            case m1_18_1:
            case m1_18_2:
            case m1_19_0:
            case m1_19_1:
            case m1_19_2:
            case m1_19_3:
            case m1_19_4:
                return "net.minecraft";
            default:
                return null;
        }
    }

    public static String getBukkitPackage(MinecraftVersion version) {
        switch (version) {
            case m1_9_0:
            case m1_9_2:
            case m1_9_4:
            case m1_10_2:
            case m1_11_0:
            case m1_11_1:
            case m1_11_2:
            case m1_12_0:
            case m1_12_1:
            case m1_12_2:
            case m1_13_0:
            case m1_13_1:
            case m1_13_2:
            case m1_14_0:
            case m1_14_1:
            case m1_14_2:
            case m1_14_3:
            case m1_14_4:
            case m1_15_0:
            case m1_15_1:
            case m1_15_2:
            case m1_16_1:
            case m1_16_2:
            case m1_16_3:
            case m1_16_4:
            case m1_16_5:
            case m1_17_0:
            case m1_17_1:
            case m1_18_0:
            case m1_18_1:
            case m1_18_2:
            case m1_19_0:
            case m1_19_1:
            case m1_19_2:
            case m1_19_3:
            case m1_19_4:
                return "org.bukkit.craftbukkit.v" + version.major + "_" + version.minor + "_" + getRNumber(version);
            default:
                return null;
        }
    }

    private static String getRNumber(MinecraftVersion version) {
        switch (version) {
            case m1_9_0:
            case m1_9_2:
            case m1_10_2:
            case m1_11_0:
            case m1_11_1:
            case m1_11_2:
            case m1_12_0:
            case m1_12_1:
            case m1_12_2:
            case m1_13_0:
            case m1_14_0:
            case m1_14_1:
            case m1_14_2:
            case m1_14_3:
            case m1_14_4:
            case m1_15_0:
            case m1_15_1:
            case m1_15_2:
            case m1_16_1:
            case m1_17_0:
            case m1_17_1:
            case m1_18_0:
            case m1_18_1:
            case m1_19_0:
            case m1_19_1:
            case m1_19_2:
                return "R1";
            case m1_9_4:
            case m1_13_1:
            case m1_13_2:
            case m1_16_2:
            case m1_16_3:
            case m1_18_2:
            case m1_19_3:
                return "R2";
            case m1_16_4:
            case m1_16_5:
            case m1_19_4:
                return "R3";
            default:
                return null;
        }
    }

    private final int major;
    private final int minor;
    private final int build;
    private final int version;

    MinecraftVersion(int major, int minor, int build) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        if (major == Integer.MAX_VALUE) {
            this.version = major;
        } else {
            this.version = Integer.parseInt("" + major + minor + build);
        }
    }

    public int getVersion() {
        return version;
    }

    public String getVersionString() {
        return major + "." + minor + "." + build;
    }
}
