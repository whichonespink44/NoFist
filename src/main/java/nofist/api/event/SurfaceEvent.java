package nofist.api.event;


import net.minecraft.block.state.IBlockState;

import net.minecraftforge.fml.common.eventhandler.Event;


public class SurfaceEvent extends Event {

    public SurfaceEvent() {
        super();
    }

    public static class BoulderBlock extends SurfaceEvent {

        private int worldX;
        private int worldY;
        private int worldZ;
        private IBlockState block;

        public BoulderBlock(int worldX, int worldY, int worldZ, IBlockState defaultBlock) {

            super();

            this.worldX = worldX;
            this.worldY = worldY;
            this.worldZ = worldZ;
            this.block = defaultBlock;
        }

        public int getWorldX() {

            return worldX;
        }

        public int getWorldY() {

            return worldY;
        }

        public int getWorldZ() {

            return worldZ;
        }

        public IBlockState getBlock() {

            return block;
        }

        public void setBlock(IBlockState block) {

            this.block = block;
        }
    }
}