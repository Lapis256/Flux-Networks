package sonar.fluxnetworks.common.connection;

import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;
import sonar.fluxnetworks.api.device.IFluxDevice;
import sonar.fluxnetworks.api.misc.FluxConstants;
import sonar.fluxnetworks.api.network.AccessLevel;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.NetworkMember;
import sonar.fluxnetworks.api.network.NetworkSecurity;
import sonar.fluxnetworks.common.blockentity.FluxDeviceEntity;
import sonar.fluxnetworks.common.capability.SuperAdmin;
import sonar.fluxnetworks.common.util.FluxUtils;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Defines the base class of a flux network.
 * Instances of this class are expected on the client side.
 */
public class FluxNetworkModel implements IFluxNetwork {

    private static final String NETWORK_NAME = "networkName";
    private static final String NETWORK_COLOR = "networkColor";
    private static final String OWNER_UUID = "ownerUUID";
    private static final String PLAYER_LIST = "playerList";
    private static final String CONNECTIONS = "connections";
    private static final String SECURITY = "security";

    //public ICustomValue<Integer> network_id = new CustomValue<>();
    //public ICustomValue<String> network_name = new CustomValue<>();
    //public ICustomValue<UUID> network_owner = new CustomValue<>();
    //public ICustomValue<SecurityType> network_security = new CustomValue<>();
    //public ICustomValue<String> network_password = new CustomValue<>();
    //public ICustomValue<Integer> network_color = new CustomValue<>();
    //public ICustomValue<EnergyType> network_energy = new CustomValue<>();
    //public ICustomValue<Integer> network_wireless = new CustomValue<>(0);
    //public ICustomValue<NetworkStatistics> network_stats = new CustomValue<>(new NetworkStatistics(this));

    private int mNetworkID;
    private String mNetworkName;
    private int mNetworkColor;
    private UUID mOwnerUUID;

    protected final NetworkSecurity mSecurity = new NetworkSecurity();
    protected final NetworkStatistics mStatistics = new NetworkStatistics(this);
    protected final HashMap<UUID, NetworkMember> mMembers = new HashMap<>();
    // On server: FluxDeviceEntity (loaded) and PhantomFluxDevice (unloaded)
    // On client: PhantomFluxDevice
    protected final HashMap<GlobalPos, IFluxDevice> mConnections = new HashMap<>();

    public FluxNetworkModel() {
    }

    FluxNetworkModel(int id, String name, int color, UUID owner) {
        mNetworkID = id;
        mNetworkName = name;
        mNetworkColor = color;
        mOwnerUUID = owner;
    }

    FluxNetworkModel(int id, String name, int color, @Nonnull Player owner) {
        mNetworkID = id;
        mNetworkName = name;
        mNetworkColor = color;
        mOwnerUUID = owner.getUUID();
        mMembers.put(mOwnerUUID, NetworkMember.create(owner, AccessLevel.OWNER));
    }

    @Override
    public int getNetworkID() {
        return mNetworkID;
    }

    @Nonnull
    @Override
    public UUID getOwnerUUID() {
        return mOwnerUUID;
    }

    @Nonnull
    @Override
    public String getNetworkName() {
        return mNetworkName;
    }

    @Override
    public void setNetworkName(@Nonnull String name) {
        mNetworkName = name;
    }

    @Override
    public int getNetworkColor() {
        return mNetworkColor;
    }

    @Override
    public void setNetworkColor(int color) {
        mNetworkColor = color;
    }

    @Nonnull
    @Override
    public NetworkSecurity getSecurity() {
        return mSecurity;
    }

    @Nonnull
    @Override
    public NetworkStatistics getStatistics() {
        return mStatistics;
    }

    @Nonnull
    @Override
    public Collection<NetworkMember> getAllMembers() {
        return mMembers.values();
    }

    @Nonnull
    @Override
    public Optional<NetworkMember> getMemberByUUID(@Nonnull UUID uuid) {
        return Optional.ofNullable(mMembers.get(uuid));
    }

    @Nonnull
    @Override
    public Collection<IFluxDevice> getAllConnections() {
        return mConnections.values();
    }

    @Nonnull
    @Override
    public Optional<IFluxDevice> getConnectionByPos(@Nonnull GlobalPos pos) {
        return Optional.ofNullable(mConnections.get(pos));
    }

    @Override
    public void onEndServerTick() {
        throw new IllegalStateException();
    }

    @Override
    public void onDelete() {
        mMembers.clear();
        mConnections.clear();
    }

    @Nonnull
    @Override
    public AccessLevel getPlayerAccess(@Nonnull Player player) {
        throw new IllegalStateException();
    }

    @Nonnull
    @Override
    public List<FluxDeviceEntity> getLogicalEntities(int logic) {
        throw new IllegalStateException("Sincerely?");
    }

    @Override
    public long getBufferLimiter() {
        throw new IllegalStateException();
    }

    @Override
    public boolean enqueueConnectionAddition(@Nonnull FluxDeviceEntity device) {
        throw new IllegalStateException();
    }

    @Override
    public void enqueueConnectionRemoval(@Nonnull FluxDeviceEntity device, boolean chunkUnload) {
        throw new IllegalStateException();
    }

    /*@Override
    public <T> T getSetting(NetworkSettings<T> setting) {
        return setting.getValue(this).getValue();
    }

    @Override
    public <T> void setSetting(NetworkSettings<T> settings, T value) {
        settings.getValue(this).setValue(value);
    }*/

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void writeCustomTag(@Nonnull CompoundTag tag, int type) {
        if (type == FluxConstants.TYPE_NET_BASIC || type == FluxConstants.TYPE_SAVE_ALL) {
            tag.putInt(FluxConstants.NETWORK_ID, mNetworkID);
            tag.putString(NETWORK_NAME, mNetworkName);
            tag.putInt(NETWORK_COLOR, mNetworkColor);
            tag.putUUID(OWNER_UUID, mOwnerUUID);
            CompoundTag subTag = new CompoundTag();
            mSecurity.writeNBT(tag, type == FluxConstants.TYPE_SAVE_ALL);
            tag.put(SECURITY, subTag);
        }
        if (type == FluxConstants.TYPE_SAVE_ALL) {
            Collection<NetworkMember> members = getAllMembers();
            if (!members.isEmpty()) {
                ListTag list = new ListTag();
                for (NetworkMember m : members) {
                    CompoundTag subTag = new CompoundTag();
                    m.writeNBT(subTag);
                    list.add(subTag);
                }
                tag.put(PLAYER_LIST, list);
            }

            Collection<IFluxDevice> connections = getAllConnections();
            // all unloaded
            if (!connections.isEmpty()) {
                ListTag list = new ListTag();
                for (IFluxDevice d : connections) {
                    if (!d.isChunkLoaded()) {
                        CompoundTag subTag = new CompoundTag();
                        d.writeCustomTag(subTag, FluxConstants.TYPE_SAVE_ALL);
                        list.add(subTag);
                    }
                }
                tag.put(CONNECTIONS, list);
            }
        }
        if (type == FluxConstants.TYPE_NET_MEMBERS) {
            Collection<NetworkMember> members = getAllMembers();
            ListTag list = new ListTag();
            if (!members.isEmpty()) {
                for (NetworkMember m : members) {
                    CompoundTag subTag = new CompoundTag();
                    m.writeNBT(subTag);
                    list.add(subTag);
                }
            }
            List<ServerPlayer> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
            if (!players.isEmpty()) {
                for (ServerPlayer p : players) {
                    if (getMemberByUUID(p.getUUID()).isEmpty()) {
                        CompoundTag subTag = new CompoundTag();
                        NetworkMember m = NetworkMember.create(p,
                                SuperAdmin.isPlayerSuperAdmin(p) ? AccessLevel.SUPER_ADMIN :
                                        AccessLevel.BLOCKED);
                        m.writeNBT(subTag);
                        list.add(subTag);
                    }
                }
            }
            tag.put(PLAYER_LIST, list);
        }
        if (type == FluxConstants.TYPE_NET_CONNECTIONS) {
            Collection<IFluxDevice> connections = getAllConnections();
            if (!connections.isEmpty()) {
                ListTag list = new ListTag();
                for (IFluxDevice d : connections) {
                    CompoundTag subTag = new CompoundTag();
                    d.writeCustomTag(subTag, FluxConstants.TYPE_CONNECTION_UPDATE);
                    list.add(subTag);
                }
                tag.put(CONNECTIONS, list);
            }
        }
        if (type == FluxConstants.TYPE_NET_STATISTICS) {
            mStatistics.writeNBT(tag);
        }
        /*if (flags == NBTType.NETWORK_GENERAL || flags == NBTType.ALL_SAVE) {
            nbt.putInt(FluxNetworkData.NETWORK_ID, network_id.getValue());
            nbt.putString(FluxNetworkData.NETWORK_NAME, network_name.getValue());
            nbt.putUniqueId(FluxNetworkData.OWNER_UUID, network_owner.getValue());
            nbt.putInt(FluxNetworkData.SECURITY_TYPE, network_security.getValue().ordinal());
            nbt.putString(FluxNetworkData.NETWORK_PASSWORD, network_password.getValue());
            nbt.putInt(FluxNetworkData.NETWORK_COLOR, network_color.getValue());
            nbt.putInt(FluxNetworkData.ENERGY_TYPE, network_energy.getValue().ordinal());
            nbt.putInt(FluxNetworkData.WIRELESS_MODE, network_wireless.getValue());

            if (flags == NBTType.ALL_SAVE) {
                FluxNetworkData.writePlayers(this, nbt);
                FluxNetworkData.writeConnections(this, nbt);
            }
        }

        if (flags == NBTType.NETWORK_PLAYERS) {
            FluxNetworkData.writeAllPlayers(this, nbt);
        }

        if (flags == NBTType.NETWORK_CONNECTIONS) {
            allDevices.getValue().removeIf(IFluxDevice::isChunkLoaded);
            List<IFluxDevice> connectors = getConnections(FluxLogicType.ANY);
            connectors.forEach(f -> allDevices.getValue().add(new SimpleFluxDevice(f)));
            FluxNetworkData.writeAllConnections(this, nbt);
        }
        if (flags == NBTType.NETWORK_STATISTICS) {
            network_stats.getValue().writeNBT(nbt);
        }
        if (flags == NBTType.NETWORK_CLEAR) {
            nbt.putBoolean("clear", true); // Nothing
        }*/
    }

    @Override
    public void readCustomTag(@Nonnull CompoundTag tag, int type) {
        if (type == FluxConstants.TYPE_NET_BASIC || type == FluxConstants.TYPE_SAVE_ALL) {
            mNetworkID = tag.getInt(FluxConstants.NETWORK_ID);
            mNetworkName = tag.getString(NETWORK_NAME);
            mNetworkColor = tag.getInt(NETWORK_COLOR);
            mOwnerUUID = tag.getUUID(OWNER_UUID);
            mSecurity.readNBT(tag.getCompound(SECURITY));
        }
        if (type == FluxConstants.TYPE_SAVE_ALL) {
            ListTag list = tag.getList(PLAYER_LIST, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag c = list.getCompound(i);
                NetworkMember m = new NetworkMember(c);
                mMembers.put(m.getPlayerUUID(), m);
            }
            list = tag.getList(CONNECTIONS, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag c = list.getCompound(i);
                PhantomFluxDevice f = PhantomFluxDevice.load(c);
                mConnections.put(f.getGlobalPos(), f);
            }
        }
        if (type == FluxConstants.TYPE_NET_MEMBERS) {
            mMembers.clear();
            ListTag list = tag.getList(PLAYER_LIST, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag c = list.getCompound(i);
                NetworkMember m = new NetworkMember(c);
                mMembers.put(m.getPlayerUUID(), m);
            }
        }
        if (type == FluxConstants.TYPE_NET_CONNECTIONS) {
            //TODO waiting for new GUI system, see GuiTabConnections, we request a full connections update
            // when we (re)open the gui, but if a tile removed by someone or on world unloads, this won't send
            // to player, so calling clear() here as a temporary solution, (f != null) is always false
            mConnections.clear();

            ListTag list = tag.getList(CONNECTIONS, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag c = list.getCompound(i);
                GlobalPos pos = FluxUtils.readGlobalPos(c);
                IFluxDevice f = mConnections.get(pos);
                if (f != null) {
                    f.readCustomTag(c, FluxConstants.TYPE_CONNECTION_UPDATE);
                } else {
                    mConnections.put(pos, PhantomFluxDevice.update(pos, c));
                }
            }
        }
        if (type == FluxConstants.TYPE_NET_STATISTICS) {
            mStatistics.readNBT(tag);
        }
        /*if (flags == NBTType.NETWORK_GENERAL || flags == NBTType.ALL_SAVE) {
            network_id.setValue(nbt.getInt(FluxNetworkData.NETWORK_ID));
            network_name.setValue(nbt.getString(FluxNetworkData.NETWORK_NAME));
            network_owner.setValue(nbt.getUniqueId(FluxNetworkData.OWNER_UUID));
            network_security.setValue(SecurityType.values()[nbt.getInt(FluxNetworkData.SECURITY_TYPE)]);
            network_password.setValue(nbt.getString(FluxNetworkData.NETWORK_PASSWORD));
            network_color.setValue(nbt.getInt(FluxNetworkData.NETWORK_COLOR));
            network_energy.setValue(EnergyType.values()[nbt.getInt(FluxNetworkData.ENERGY_TYPE)]);
            network_wireless.setValue(nbt.getInt(FluxNetworkData.WIRELESS_MODE));

            if (flags == NBTType.ALL_SAVE) {
                FluxNetworkData.readPlayers(this, nbt);
                FluxNetworkData.readConnections(this, nbt);
            }
        }

        if (flags == NBTType.NETWORK_PLAYERS) {
            FluxNetworkData.readPlayers(this, nbt);
        }

        if (flags == NBTType.NETWORK_CONNECTIONS) {
            FluxNetworkData.readAllConnections(this, nbt);
        }
        if (flags == NBTType.NETWORK_STATISTICS) {
            network_stats.getValue().readNBT(nbt);
        }*/
    }
}