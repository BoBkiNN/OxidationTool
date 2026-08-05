package xyz.bobkinn.oxidizer;

import org.bukkit.Material;

import java.util.List;

/**
 * New copper stages added in 1.21.9
 */
public class CopperStages1219 {

    private CopperStages1219() {}

    public static final List<Material> CHEST = List.of(
            Material.COPPER_CHEST,
            Material.EXPOSED_COPPER_CHEST,
            Material.WEATHERED_COPPER_CHEST,
            Material.OXIDIZED_COPPER_CHEST
    );

    public static final List<Material> WAXED_CHEST = List.of(
            Material.WAXED_COPPER_CHEST,
            Material.WAXED_EXPOSED_COPPER_CHEST,
            Material.WAXED_WEATHERED_COPPER_CHEST,
            Material.WAXED_OXIDIZED_COPPER_CHEST
    );

    public static final List<Material> GOLEM_STATUE = List.of(
            Material.COPPER_GOLEM_STATUE,
            Material.EXPOSED_COPPER_GOLEM_STATUE,
            Material.WEATHERED_COPPER_GOLEM_STATUE,
            Material.OXIDIZED_COPPER_GOLEM_STATUE
    );

    public static final List<Material> WAXED_GOLEM_STATUE = List.of(
            Material.WAXED_COPPER_GOLEM_STATUE,
            Material.WAXED_EXPOSED_COPPER_GOLEM_STATUE,
            Material.WAXED_WEATHERED_COPPER_GOLEM_STATUE,
            Material.WAXED_OXIDIZED_COPPER_GOLEM_STATUE
    );

    public static final List<Material> LIGHTNING_ROD = List.of(
            Material.LIGHTNING_ROD,
            Material.EXPOSED_LIGHTNING_ROD,
            Material.WEATHERED_LIGHTNING_ROD,
            Material.OXIDIZED_LIGHTNING_ROD
    );

    public static final List<Material> WAXED_LIGHTNING_ROD = List.of(
            Material.WAXED_LIGHTNING_ROD,
            Material.WAXED_EXPOSED_LIGHTNING_ROD,
            Material.WAXED_WEATHERED_LIGHTNING_ROD,
            Material.WAXED_OXIDIZED_LIGHTNING_ROD
    );

    public static final List<Material> COPPER_BARS = List.of(
            Material.COPPER_BARS,
            Material.EXPOSED_COPPER_BARS,
            Material.WEATHERED_COPPER_BARS,
            Material.OXIDIZED_COPPER_BARS
    );

    public static final List<Material> WAXED_COPPER_BARS = List.of(
            Material.WAXED_COPPER_BARS,
            Material.WAXED_EXPOSED_COPPER_BARS,
            Material.WAXED_WEATHERED_COPPER_BARS,
            Material.WAXED_OXIDIZED_COPPER_BARS
    );

    public static final List<Material> COPPER_CHAIN = List.of(
            Material.COPPER_CHAIN,
            Material.EXPOSED_COPPER_CHAIN,
            Material.WEATHERED_COPPER_CHAIN,
            Material.OXIDIZED_COPPER_CHAIN
    );

    public static final List<Material> WAXED_COPPER_CHAIN = List.of(
            Material.WAXED_COPPER_CHAIN,
            Material.WAXED_EXPOSED_COPPER_CHAIN,
            Material.WAXED_WEATHERED_COPPER_CHAIN,
            Material.WAXED_OXIDIZED_COPPER_CHAIN
    );

    public static final List<Material> COPPER_LANTERN = List.of(
            Material.COPPER_LANTERN,
            Material.EXPOSED_COPPER_LANTERN,
            Material.WEATHERED_COPPER_LANTERN,
            Material.OXIDIZED_COPPER_LANTERN
    );

    public static final List<Material> WAXED_COPPER_LANTERN = List.of(
            Material.WAXED_COPPER_LANTERN,
            Material.WAXED_EXPOSED_COPPER_LANTERN,
            Material.WAXED_WEATHERED_COPPER_LANTERN,
            Material.WAXED_OXIDIZED_COPPER_LANTERN
    );

    public static final List<List<Material>> TYPES = List.of(
            CHEST, WAXED_CHEST, GOLEM_STATUE, WAXED_GOLEM_STATUE,
            LIGHTNING_ROD, WAXED_LIGHTNING_ROD, COPPER_BARS, WAXED_COPPER_BARS,
            COPPER_CHAIN, WAXED_COPPER_CHAIN, COPPER_LANTERN, WAXED_COPPER_LANTERN
    );
}
