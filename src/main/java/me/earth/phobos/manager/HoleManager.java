



package me.earth.phobos.manager;

import me.earth.phobos.features.*;
import java.util.concurrent.atomic.*;
import me.earth.phobos.features.modules.client.*;
import me.earth.phobos.features.modules.render.*;
import me.earth.phobos.features.modules.combat.*;
import me.earth.phobos.features.modules.movement.*;
import java.util.concurrent.*;
import net.minecraft.entity.player.*;
import me.earth.phobos.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.block.*;

public class HoleManager extends Feature implements Runnable
{
    private static final BlockPos[] surroundOffset;
    private final List<BlockPos> midSafety;
    private final TimerUtil syncTimer;
    private final AtomicBoolean shouldInterrupt;
    private final TimerUtil holeTimer;
    private List<BlockPos> holes;
    private ScheduledExecutorService executorService;
    private int lastUpdates;
    private Thread thread;
    
    public HoleManager() {
        this.midSafety = new ArrayList<BlockPos>();
        this.syncTimer = new TimerUtil();
        this.shouldInterrupt = new AtomicBoolean(false);
        this.holeTimer = new TimerUtil();
        this.holes = new ArrayList<BlockPos>();
    }
    
    public void update() {
        if (Management.getInstance().holeThread.getValue() == Management.ThreadMode.WHILE) {
            if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || this.syncTimer.passedMs((int)Management.getInstance().holeSync.getValue())) {
                if (this.thread == null) {
                    this.thread = new Thread(this);
                }
                else if (this.syncTimer.passedMs((int)Management.getInstance().holeSync.getValue()) && !this.shouldInterrupt.get()) {
                    this.shouldInterrupt.set(true);
                    this.syncTimer.reset();
                    return;
                }
                if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
                    this.thread = new Thread(this);
                }
                if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
                    try {
                        this.thread.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.syncTimer.reset();
                }
            }
        }
        else if (Management.getInstance().holeThread.getValue() == Management.ThreadMode.WHILE) {
            if (this.executorService == null || this.executorService.isTerminated() || this.executorService.isShutdown() || this.syncTimer.passedMs(10000L) || this.lastUpdates != (int)Management.getInstance().holeUpdates.getValue()) {
                this.lastUpdates = (int)Management.getInstance().holeUpdates.getValue();
                if (this.executorService != null) {
                    this.executorService.shutdown();
                }
                this.executorService = this.getExecutor();
            }
        }
        else if (this.holeTimer.passedMs((int)Management.getInstance().holeUpdates.getValue()) && !fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn() || HoleTP.getInstance().isOn())) {
            this.holes = this.calcHoles();
            this.holeTimer.reset();
        }
    }
    
    public void settingChanged() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
        if (this.thread != null) {
            this.shouldInterrupt.set(true);
        }
    }
    
    private ScheduledExecutorService getExecutor() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this,  0L,  (int)Management.getInstance().holeUpdates.getValue(),  TimeUnit.MILLISECONDS);
        return service;
    }
    
    public void run() {
        if (Management.getInstance().holeThread.getValue() == Management.ThreadMode.WHILE) {
            while (!this.shouldInterrupt.get()) {
                if (!fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn() || HoleTP.getInstance().isOn())) {
                    this.holes = this.calcHoles();
                }
                try {
                    Thread.sleep((int)Management.getInstance().holeUpdates.getValue());
                }
                catch (InterruptedException e) {
                    this.thread.interrupt();
                    e.printStackTrace();
                }
            }
            this.shouldInterrupt.set(false);
            this.syncTimer.reset();
            Thread.currentThread().interrupt();
            return;
        }
        if (Management.getInstance().holeThread.getValue() == Management.ThreadMode.POOL && !fullNullCheck() && (HoleESP.getInstance().isOn() || HoleFiller.getInstance().isOn())) {
            this.holes = this.calcHoles();
        }
    }
    
    public List<BlockPos> getHoles() {
        return this.holes;
    }
    
    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }
    
    public List<BlockPos> getSortedHoles() {
        if (fullNullCheck()) {
            return null;
        }
        this.holes.sort(Comparator.comparingDouble(hole -> HoleManager.mc.player.getDistanceSq(hole)));
        return this.getHoles();
    }
    
    public List<BlockPos> calcHoles() {
        final ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        this.midSafety.clear();
        final List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)HoleManager.mc.player),  (float)Management.getInstance().holeRange.getValue(),  ((Float)Management.getInstance().holeRange.getValue()).intValue(),  false,  true,  0);
        for (final BlockPos pos : positions) {
            if (fullNullCheck()) {
                return null;
            }
            if (!HoleManager.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !HoleManager.mc.world.getBlockState(pos.add(0,  1,  0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleManager.mc.world.getBlockState(pos.add(0,  2,  0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            boolean isSafe = true;
            boolean midSafe = true;
            for (final BlockPos offset : HoleManager.surroundOffset) {
                final Block block = HoleManager.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (BlockUtil.isBlockUnSolid(block)) {
                    midSafe = false;
                }
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST) {
                    if (block != Blocks.ANVIL) {
                        isSafe = false;
                    }
                }
            }
            if (isSafe) {
                safeSpots.add(pos);
            }
            if (!midSafe) {
                continue;
            }
            this.midSafety.add(pos);
        }
        return safeSpots;
    }
    
    public boolean isSafe(final BlockPos pos) {
        boolean isSafe = true;
        for (final BlockPos offset : HoleManager.surroundOffset) {
            final Block block = HoleManager.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
            if (block != Blocks.BEDROCK) {
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }
    
    static {
        surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0,  true,  true));
    }
}
