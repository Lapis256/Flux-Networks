package sonar.fluxnetworks.api;

import sonar.fluxnetworks.FluxNetworks;

/**
 * Revision: 7.0.3
 */
public final class FluxConstants {

    public static final int INVALID_NETWORK_ID = -1;
    public static final int INVALID_NETWORK_COLOR = 0xb2b2b2;

    /**
     * NBT access type, save data to disk (R/W server only).
     */
    public static final byte NBT_SAVE_ALL = 1;
    /**
     * NBT access type, tile update or read stack.
     * <ul>
     * <li>UPDATE: S->C (Write - server, Read - client)</li>
     * <li>DROP: (Write - server, Read - client/server)</li>
     * <li>SETTINGS: C->S (Write - client, Read - server)</li>
     * </ul>
     */
    public static final byte
            NBT_TILE_UPDATE = 11,
            NBT_TILE_DROP = 15,
            NBT_TILE_SETTINGS = 19;
    /**
     * NBT access type, update phantom flux device.
     * S->C (Write - server, read - client).
     */
    public static final byte NBT_PHANTOM_UPDATE = 20;
    /**
     * NBT access type, network data-sync or operation.
     * Write - server, Read - client/server.
     * <ul>
     *     <li>(Server to client) Basic: network ID, network name, network color</li>
     *     <li>(Client requests) General: ownerUUID, securityLevel, wirelessMode</li>
     *     <li>(Client requests) Members: All network members</li>
     *     <li>(Client requests) Connections: All network connections (i.e. loaded and unloaded)</li>
     *     <li>(Client requests) Statistics: Latest network statistics</li>
     * </ul>
     * Note that password is always opaque to clients (even if you are super admin).
     */
    //TODO update relevant message handling
    public static final byte
            NBT_NET_BASIC = 21,
            NBT_NET_MEMBERS = 22,
            NBT_NET_ALL_CONNECTIONS = 23,
            NBT_NET_STATISTICS = 24;

    // Network connections editing flags
    /*public static final int
            FLAG_EDIT_NAME = 1,
            FLAG_EDIT_PRIORITY = 1 << 1,
            FLAG_EDIT_LIMIT = 1 << 2,
            FLAG_EDIT_SURGE_MODE = 1 << 3,
            FLAG_EDIT_DISABLE_LIMIT = 1 << 4,
            FLAG_EDIT_CHUNK_LOADING = 1 << 5,
            FLAG_EDIT_DISCONNECT = 1 << 6;*/

    /**
     * Response codes. Positive - Have Toast, Negative - Action Only.
     */
    public static final int
            RESPONSE_SUCCESS = -1,
            RESPONSE_REQUIRE_PASSWORD = -2;
    public static final int
            RESPONSE_REJECT = 1,
            RESPONSE_NO_OWNER = 2,
            RESPONSE_NO_ADMIN = 3,
            RESPONSE_NO_SPACE = 4,
            RESPONSE_HAS_CONTROLLER = 5,
            RESPONSE_INVALID_USER = 6,
            RESPONSE_INVALID_PASSWORD = 7,
            RESPONSE_BANNED_LOADING = 8;

    /**
     * Request keys.
     */
    public static final int
            REQUEST_CREATE_NETWORK = 1,
            REQUEST_DELETE_NETWORK = 2,
            REQUEST_EDIT_TILE = 3,
            REQUEST_TILE_NETWORK = 4,
            REQUEST_EDIT_MEMBER = 5,
            REQUEST_EDIT_NETWORK = 6,
            REQUEST_EDIT_CONNECTION = 7,
            REQUEST_UPDATE_NETWORK = 8,
            REQUEST_UPDATE_CONNECTION = 9,
            REQUEST_DISCONNECT = 10;

    // Network members editing type
    public static final byte MEMBERSHIP_SET_USER = 1;
    public static final byte MEMBERSHIP_SET_ADMIN = 2;
    public static final byte MEMBERSHIP_CANCEL_MEMBERSHIP = 3;
    public static final byte MEMBERSHIP_TRANSFER_OWNERSHIP = 4;

    /*
     * Device buffer message type, C2S positive
     */
    /*public static final byte DEVICE_C2S_CUSTOM_NAME = 1;
    public static final byte DEVICE_C2S_PRIORITY = 2;
    public static final byte DEVICE_C2S_LIMIT = 3;
    public static final byte DEVICE_C2S_SURGE_MODE = 4;
    public static final byte DEVICE_C2S_DISABLE_LIMIT = 5;
    public static final byte DEVICE_C2S_CHUNK_LOADING = 6;*/

    /**
     * Device buffer message type, S2C negative
     */
    public static final byte DEVICE_S2C_GUI_SYNC = -1;
    public static final byte DEVICE_S2C_STORAGE_ENERGY = -2;

    // NBT sub-tag key
    public static final String FLUX_DATA_COMPONENT = "flux_data";
    public static final String FLUX_CONFIG_COMPONENT = "device_config";
    public static final String STORED_ENERGY_COMPONENT = "stored_energy";

    // NBT root key
    public static final String FLUX_COLOR_COMPONENT = "flux_color";

    // NBT key
    public static final String NETWORK_ID = "network_id";
    public static final String CUSTOM_NAME = "custom_name";
    public static final String PRIORITY = "priority";
    public static final String LIMIT = "limit";
    public static final String SURGE_MODE = "surge_mode";
    public static final String DISABLE_LIMIT = "disable_limit";
    public static final String PLAYER_UUID = "player_uuid";

    public static final String CLIENT_COLOR = "client_color";
    public static final String FLAGS = "flags";

    public static final String DEVICE_TYPE = "device_type";
    public static final String FORCED_LOADING = "forced_loading";
    public static final String CHUNK_LOADED = "chunk_loaded";

    public static final String ENERGY = "energy"; // equals to buffer, but with different display text
    public static final String CHANGE = "change";

    public static final String SUPER_ADMIN = "super_admin";
    public static final String WIRELESS_MODE = "wireless_mode";
    public static final String WIRELESS_NETWORK = "wireless_network";

    static {
        // All constants are expected to be inlined at compile-time
        // TODO: Should this be an error instead?
        FluxNetworks.LOGGER.warn("FluxConstants was class-loaded and initialized, this shouldn't happen...");
        assert false;
    }

    private FluxConstants() {
    }
}
