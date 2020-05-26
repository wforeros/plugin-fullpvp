package me.theoldestwilly.fullpvp.utilities.cuboid;


import org.bukkit.block.BlockFace;

public enum CuboidDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN,
    HORIZONTAL,
    VERTICAL,
    BOTH,
    UNKNOWN;

    private CuboidDirection() {
    }

    public CuboidDirection opposite() {
        switch(this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case HORIZONTAL:
                return VERTICAL;
            case VERTICAL:
                return HORIZONTAL;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case BOTH:
                return BOTH;
            default:
                return UNKNOWN;
        }
    }

    public BlockFace toBukkitDirection() {
        switch(this) {
            case NORTH:
                return BlockFace.NORTH;
            case EAST:
                return BlockFace.EAST;
            case SOUTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
            case HORIZONTAL:
            case VERTICAL:
            default:
                return null;
            case UP:
                return BlockFace.UP;
            case DOWN:
                return BlockFace.DOWN;
        }
    }
}
