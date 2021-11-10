



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import java.util.concurrent.atomic.*;
import me.earth.phobos.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import java.io.*;
import java.util.zip.*;
import net.minecraft.block.state.*;
import net.minecraft.block.material.*;
import me.earth.phobos.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.play.server.*;
import me.earth.phobos.event.events.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.util.math.*;
import me.earth.phobos.util.*;
import net.minecraft.network.play.client.*;
import java.net.*;
import java.nio.channels.*;

public class NoteBot extends Module
{
    private final Setting<Boolean> tune;
    private final Setting<Boolean> active;
    private final Setting<Boolean> downloadSongs;
    private final Setting<String> loadFileSet;
    private final Map<Sound, Byte> soundBytes;
    private final List<SoundEntry> soundEntries;
    private final List<BlockPos> posList;
    private final File file;
    private IRegister[] registers;
    private int soundIndex;
    private int index;
    private Map<BlockPos, AtomicInteger> posPitch;
    private Map<Sound, BlockPos[]> soundPositions;
    private BlockPos currentPos;
    private BlockPos nextPos;
    private BlockPos endPos;
    private int tuneStage;
    private int tuneIndex;
    private boolean tuned;
    
    public NoteBot() {
        super("NoteBot", "Plays songs.", Category.MISC, true, false, false);
        this.tune = (Setting<Boolean>)this.register(new Setting("Tune", (T)false));
        this.active = (Setting<Boolean>)this.register(new Setting("Active", (T)false));
        this.downloadSongs = (Setting<Boolean>)this.register(new Setting("DownloadSongs", (T)false));
        this.loadFileSet = (Setting<String>)this.register(new Setting("Load", (T)"Load File..."));
        this.soundBytes = new HashMap<Sound, Byte>();
        this.soundEntries = new ArrayList<SoundEntry>();
        this.posList = new ArrayList<BlockPos>();
        this.file = new File(Phobos.fileManager.getNotebot().toString());
    }
    
    public static Map<Sound, BlockPos[]> setUpSoundMap() {
        NoteBot.mc.player.getPosition();
        final LinkedHashMap<Sound, BlockPos[]> result = new LinkedHashMap<Sound, BlockPos[]>();
        final HashMap<Sound, AtomicInteger> atomicSounds = new HashMap<Sound, AtomicInteger>();
        final BlockPos[] var10002;
        final HashMap<Sound, BlockPos[]> hashMap;
        final HashMap<Sound, AtomicInteger> hashMap2;
        Arrays.asList(Sound.values()).forEach(sound -> {
            var10002 = new BlockPos[25];
            hashMap.put(sound, var10002);
            hashMap2.put(sound, new AtomicInteger());
            return;
        });
        for (int x = -6; x < 6; ++x) {
            for (int y = -1; y < 5; ++y) {
                for (int z = -6; z < 6; ++z) {
                    final BlockPos pos = NoteBot.mc.player.getPosition().add(x, y, z);
                    final Block block = NoteBot.mc.world.getBlockState(pos).getBlock();
                    if (distanceSqToCenter(pos) < 27.040000000000003 && block == Blocks.NOTEBLOCK) {
                        final Sound sound2;
                        final int soundByte;
                        if ((soundByte = atomicSounds.get(sound2 = getSoundFromBlockState(NoteBot.mc.world.getBlockState(pos.down()))).getAndIncrement()) < 25) {
                            result.get(sound2)[soundByte] = pos;
                        }
                    }
                }
            }
        }
        return result;
    }
    
    private static double distanceSqToCenter(final BlockPos pos) {
        final double var1 = Math.abs(NoteBot.mc.player.posX - pos.getX() - 0.5);
        final double var2 = Math.abs(NoteBot.mc.player.posY + NoteBot.mc.player.getEyeHeight() - pos.getY() - 0.5);
        final double var3 = Math.abs(NoteBot.mc.player.posZ - pos.getZ() - 0.5);
        return var1 * var1 + var2 * var2 + var3 * var3;
    }
    
    private static IRegister[] createRegister(final File file) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        final byte[] arrby = new byte[fileInputStream.available()];
        fileInputStream.read(arrby);
        final ArrayList<IRegister> arrayList = new ArrayList<IRegister>();
        boolean bl = true;
        for (final int n2 : arrby) {
            final byte b = (byte)n2;
            if (n2 == 64) {
                bl = false;
                break;
            }
        }
        for (int n3 = 0, n4 = 0; n4 < arrby.length; n4 = ++n3) {
            final int n5 = arrby[n3];
            if (n5 == (bl ? 5 : 64)) {
                final byte[] arrby2 = { arrby[++n3], arrby[++n3] };
                final int n2 = (arrby2[0] & 0xFF) | (arrby2[1] & 0xFF) << 8;
                arrayList.add(new SimpleRegister(n2));
            }
            else {
                arrayList.add(new SoundRegister(Sound.values()[n5], arrby[++n3]));
            }
        }
        return arrayList.toArray(new IRegister[0]);
    }
    
    public static void unzip(final File file1, final File fileIn) {
        final byte[] var2 = new byte[1024];
        ZipInputStream zipInputStream;
        ZipEntry zipEntry;
        try {
            if (!fileIn.exists()) {
                fileIn.mkdir();
            }
            zipInputStream = new ZipInputStream(new FileInputStream(file1));
            zipEntry = zipInputStream.getNextEntry();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        while (true) {
            FileOutputStream outputStream;
            try {
                if (zipEntry == null) {
                    break;
                }
                final String fileName = zipEntry.getName();
                final File newFile = new File(fileIn, fileName);
                new File(newFile.getParent()).mkdirs();
                outputStream = new FileOutputStream(newFile);
                int index;
                while ((index = zipInputStream.read(var2)) > 0) {
                    outputStream.write(var2, 0, index);
                }
            }
            catch (IOException ioe2) {
                ioe2.printStackTrace();
                return;
            }
            try {
                outputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            catch (IOException ioe2) {
                ioe2.printStackTrace();
                return;
            }
        }
        try {
            zipInputStream.closeEntry();
            zipInputStream.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static Sound getSoundFromBlockState(final IBlockState state) {
        if (state.getBlock() == Blocks.CLAY) {
            return Sound.CLAY;
        }
        if (state.getBlock() == Blocks.GOLD_BLOCK) {
            return Sound.GOLD;
        }
        if (state.getBlock() == Blocks.WOOL) {
            return Sound.WOOL;
        }
        if (state.getBlock() == Blocks.PACKED_ICE) {
            return Sound.ICE;
        }
        if (state.getBlock() == Blocks.BONE_BLOCK) {
            return Sound.BONE;
        }
        if (state.getMaterial() == Material.ROCK) {
            return Sound.ROCK;
        }
        if (state.getMaterial() == Material.SAND) {
            return Sound.SAND;
        }
        if (state.getMaterial() == Material.GLASS) {
            return Sound.GLASS;
        }
        return (state.getMaterial() == Material.WOOD) ? Sound.WOOD : Sound.NONE;
    }
    
    @Override
    public void onLoad() {
        if (fullNullCheck()) {
            this.disable();
        }
    }
    
    @Override
    public void onEnable() {
        if (nullCheck()) {
            this.disable();
            return;
        }
        this.soundEntries.clear();
        this.getNoteBlocks();
        this.soundIndex = 0;
        this.index = 0;
        this.resetTuning();
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && this.equals(event.getSetting().getFeature())) {
            if (event.getSetting().equals(this.loadFileSet)) {
                final String file = this.loadFileSet.getPlannedValue();
                try {
                    this.registers = createRegister(new File("phobos/notebot/" + file));
                    Command.sendMessage("Loaded: " + file);
                }
                catch (Exception e) {
                    Command.sendMessage("An Error occurred with " + file);
                    e.printStackTrace();
                }
                event.setCanceled(true);
            }
            else if (event.getSetting().equals(this.tune) && this.tune.getPlannedValue()) {
                this.resetTuning();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (this.tune.getValue() && event.getPacket() instanceof SPacketBlockAction && this.tuneStage == 0 && this.soundPositions != null) {
            final SPacketBlockAction packet = (SPacketBlockAction)event.getPacket();
            final Sound sound = Sound.values()[packet.getData1()];
            final int pitch = packet.getData2();
            final BlockPos[] positions = this.soundPositions.get(sound);
            int i = 0;
            while (i < 25) {
                final BlockPos position = positions[i];
                if (!packet.getBlockPosition().equals((Object)position)) {
                    ++i;
                }
                else {
                    if (this.posPitch.get(position).intValue() != -1) {
                        break;
                    }
                    int pitchDif = i - pitch;
                    if (pitchDif < 0) {
                        pitchDif += 25;
                    }
                    this.posPitch.get(position).set(pitchDif);
                    if (pitchDif == 0) {
                        break;
                    }
                    this.tuned = false;
                    break;
                }
            }
            if (this.endPos.equals((Object)packet.getBlockPosition()) && this.tuneIndex >= this.posPitch.values().size()) {
                this.tuneStage = 1;
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(final UpdateWalkingPlayerEvent event) {
        if (this.downloadSongs.getValue()) {
            this.downloadSongs();
            Command.sendMessage("Songs downloaded");
            this.downloadSongs.setValue(false);
        }
        if (event.getStage() == 0) {
            if (this.tune.getValue()) {
                this.tunePre();
            }
            else if (this.active.getValue()) {
                this.noteBotPre();
            }
        }
        else if (this.tune.getValue()) {
            this.tunePost();
        }
        else if (this.active.getValue()) {
            this.noteBotPost();
        }
    }
    
    private void tunePre() {
        this.currentPos = null;
        if (this.tuneStage == 1 && this.getAtomicBlockPos(null) == null) {
            if (this.tuned) {
                Command.sendMessage("Done tuning.");
                this.tune.setValue(false);
            }
            else {
                this.tuned = true;
                this.tuneStage = 0;
                this.tuneIndex = 0;
            }
        }
        else {
            if (this.tuneStage != 0) {
                final BlockPos atomicBlockPos = this.getAtomicBlockPos(this.nextPos);
                this.currentPos = atomicBlockPos;
                this.nextPos = atomicBlockPos;
            }
            else {
                while (this.tuneIndex < 250 && this.currentPos == null) {
                    this.currentPos = this.soundPositions.get(Sound.values()[(int)Math.floor(this.tuneIndex / 25)])[this.tuneIndex % 25];
                    ++this.tuneIndex;
                }
            }
            if (this.currentPos != null) {
                Phobos.rotationManager.lookAtPos(this.currentPos);
            }
        }
    }
    
    private void tunePost() {
        if (this.tuneStage == 0 && this.currentPos != null) {
            final EnumFacing facing = BlockUtil.getFacing(this.currentPos);
            NoteBot.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.currentPos, facing));
            NoteBot.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentPos, facing));
        }
        else if (this.currentPos != null) {
            this.posPitch.get(this.currentPos).decrementAndGet();
            NoteBot.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.currentPos, BlockUtil.getFacing(this.currentPos), EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
    }
    
    private void resetTuning() {
        if (NoteBot.mc.world == null || NoteBot.mc.player == null) {
            this.disable();
            return;
        }
        this.tuned = true;
        this.soundPositions = setUpSoundMap();
        this.posPitch = new LinkedHashMap<BlockPos, AtomicInteger>();
        this.soundPositions.values().forEach(array -> Arrays.asList(array).forEach(pos -> {
            if (pos != null) {
                this.endPos = pos;
                this.posPitch.put(pos, new AtomicInteger(-1));
            }
        }));
        this.tuneStage = 0;
        this.tuneIndex = 0;
    }
    
    private BlockPos getAtomicBlockPos(final BlockPos blockPos) {
        for (final Map.Entry<BlockPos, AtomicInteger> entry : this.posPitch.entrySet()) {
            final BlockPos blockPos2 = entry.getKey();
            final AtomicInteger atomicInteger = entry.getValue();
            if (blockPos2 != null && !blockPos2.equals((Object)blockPos) && atomicInteger.intValue() > 0) {
                return blockPos2;
            }
        }
        return null;
    }
    
    private void noteBotPre() {
        this.posList.clear();
        if (this.registers == null) {
            return;
        }
        while (this.index < this.registers.length) {
            final IRegister register = this.registers[this.index];
            if (register instanceof SimpleRegister) {
                final SimpleRegister simpleRegister = (SimpleRegister)register;
                if (++this.soundIndex >= simpleRegister.getSound()) {
                    ++this.index;
                    this.soundIndex = 0;
                }
                if (this.posList.size() > 0) {
                    final BlockPos blockPos = this.posList.get(0);
                    Phobos.rotationManager.lookAtPos(blockPos);
                }
                return;
            }
            if (!(register instanceof SoundRegister)) {
                continue;
            }
            final SoundRegister soundRegister = (SoundRegister)register;
            final BlockPos pos = this.getRegisterPos(soundRegister);
            if (pos != null) {
                this.posList.add(pos);
            }
            ++this.index;
        }
        this.index = 0;
    }
    
    private void noteBotPost() {
        for (int i = 0; i < this.posList.size(); ++i) {
            final BlockPos pos = this.posList.get(i);
            if (pos != null) {
                if (i != 0) {
                    final float[] rotations = MathUtil.calcAngle(NoteBot.mc.player.getPositionEyes(NoteBot.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() + 0.5f), (double)(pos.getZ() + 0.5f)));
                    NoteBot.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], NoteBot.mc.player.onGround));
                }
                this.clickNoteBlock(pos);
            }
        }
    }
    
    private void getNoteBlocks() {
        this.fillSoundBytes();
        for (int x = -6; x < 6; ++x) {
            for (int y = -1; y < 5; ++y) {
                for (int z = -6; z < 6; ++z) {
                    final BlockPos pos = NoteBot.mc.player.getPosition().add(x, y, z);
                    final Block block = NoteBot.mc.world.getBlockState(pos).getBlock();
                    if (pos.distanceSqToCenter(NoteBot.mc.player.posX, NoteBot.mc.player.posY + NoteBot.mc.player.getEyeHeight(), NoteBot.mc.player.posZ) < 27.0 && block == Blocks.NOTEBLOCK) {
                        final Sound sound;
                        final byte soundByte;
                        if ((soundByte = this.soundBytes.get(sound = getSoundFromBlockState(NoteBot.mc.world.getBlockState(pos.down())))) <= 25) {
                            this.soundEntries.add(new SoundEntry(pos, new SoundRegister(sound, soundByte)));
                            this.soundBytes.replace(sound, (byte)(soundByte + 1));
                        }
                    }
                }
            }
        }
    }
    
    private void fillSoundBytes() {
        this.soundBytes.clear();
        for (final Sound sound : Sound.values()) {
            this.soundBytes.put(sound, (Byte)0);
        }
    }
    
    private void clickNoteBlock(final BlockPos pos) {
        final EnumFacing facing = BlockUtil.getFacing(pos);
        NoteBot.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
        NoteBot.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, facing));
    }
    
    private BlockPos getRegisterPos(final SoundRegister register) {
        final SoundEntry soundEntry = this.soundEntries.stream().filter(entry -> entry.getRegister().equals(register)).findFirst().orElse(null);
        if (soundEntry == null) {
            return null;
        }
        return soundEntry.getPos();
    }
    
    private void downloadSongs() {
        File songFile;
        FileChannel fileChannel;
        ReadableByteChannel readableByteChannel;
        new Thread(() -> {
            try {
                songFile = new File(this.file, "songs.zip");
                fileChannel = new FileOutputStream(songFile).getChannel();
                readableByteChannel = Channels.newChannel(new URL("https://www.futureclient.net/future/songs.zip").openStream());
                fileChannel.transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
                unzip(songFile, this.file);
                songFile.deleteOnExit();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }).start();
    }
    
    public enum Sound
    {
        NONE, 
        GOLD, 
        GLASS, 
        BONE, 
        WOOD, 
        CLAY, 
        ICE, 
        SAND, 
        ROCK, 
        WOOL;
    }
    
    public static class SoundRegister implements IRegister
    {
        private final Sound sound;
        private final byte soundByte;
        
        public SoundRegister(final Sound soundIn, final byte soundByteIn) {
            this.sound = soundIn;
            this.soundByte = soundByteIn;
        }
        
        public Sound getSound() {
            return this.sound;
        }
        
        public byte getSoundByte() {
            return this.soundByte;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (other instanceof SoundRegister) {
                final SoundRegister soundRegister = (SoundRegister)other;
                return soundRegister.getSound() == this.getSound() && soundRegister.getSoundByte() == this.getSoundByte();
            }
            return false;
        }
    }
    
    public static class SimpleRegister implements IRegister
    {
        private int sound;
        
        public SimpleRegister(final int soundIn) {
            this.sound = soundIn;
        }
        
        public int getSound() {
            return this.sound;
        }
        
        public void setSound(final int sound) {
            this.sound = sound;
        }
    }
    
    public static class SoundEntry
    {
        private final BlockPos pos;
        private final SoundRegister register;
        
        public SoundEntry(final BlockPos posIn, final SoundRegister soundRegisterIn) {
            this.pos = posIn;
            this.register = soundRegisterIn;
        }
        
        public BlockPos getPos() {
            return this.pos;
        }
        
        public SoundRegister getRegister() {
            return this.register;
        }
    }
    
    public interface IRegister
    {
    }
}
