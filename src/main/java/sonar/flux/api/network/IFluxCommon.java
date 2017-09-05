package sonar.flux.api.network;

import sonar.core.api.nbt.INBTSyncable;
import sonar.core.utils.CustomColour;
import sonar.flux.api.ClientFlux;

import java.util.ArrayList;
import java.util.UUID;

/**
 * both client and server networks implement this
 */
public interface IFluxCommon extends INBTSyncable {

    enum AccessType {
        PUBLIC, PRIVATE, RESTRICTED;

        public String getName() {
            switch (this) {
                case PUBLIC:
                    return "network.public";
                case PRIVATE:
                    return "network.private";
                case RESTRICTED:
                    return "network.restricted";
            }
            return "";
        }
    }

    /**
     * the access settings of this network
     */
    AccessType getAccessType();

    /**
     * the id this network is registered by under the Owners name
     */
    int getNetworkID();

    /**
     * the custom set network name of this network
     */
    String getNetworkName();

    /**
     * the player name of the owner, this may change to a UUID in the future be warned...
     */
    String getCachedPlayerName();

    UUID getOwnerUUID();

    /**
     * gets the custom set network colour
     **/
    CustomColour getNetworkColour();

    /**
     * gets the latest network statistics for display
     */
    INetworkStatistics getStatistics();

    /**
     * gets the last full count of energy available in the system from connected Flux Storage
     */
    long getEnergyAvailable();

    /**
     * gets the last full count of the max energy stored allowed in connected Flux Storage
     */
    long getMaxEnergyStored();

    void setClientConnections(ArrayList<ClientFlux> flux);

    ArrayList<ClientFlux> getClientFluxConnection();

    boolean isFakeNetwork();

    FluxPlayersList getPlayers();
}

