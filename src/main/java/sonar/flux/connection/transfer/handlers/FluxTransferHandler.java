package sonar.flux.connection.transfer.handlers;

import sonar.core.api.energy.EnergyType;
import sonar.core.api.utils.ActionType;
import sonar.flux.api.energy.IEnergyTransfer;
import sonar.flux.api.energy.IFluxTransfer;
import sonar.flux.api.network.IFluxNetwork;
import sonar.flux.api.tiles.IFlux;

public abstract class FluxTransferHandler<T extends IFlux> extends BaseTransferHandler {

	public final T flux;

	public FluxTransferHandler(T flux) {
		this.flux = flux;
	}

	public IFluxNetwork getNetwork(){
		return flux.getNetwork();
	}
	
	@Override
	public long addToNetwork(long maxTransferRF, EnergyType energyType, ActionType actionType) {
		long added = 0;
		for (IFluxTransfer transfer : getTransfers()) {
			if (transfer != null && getNetwork().canConvert(energyType, transfer.getEnergyType()) && transfer instanceof IEnergyTransfer) {
				long toTransfer = getValidAddition(maxTransferRF - added);
				long add = ((IEnergyTransfer)transfer).addToNetwork(toTransfer, energyType, actionType);
				added += add;
				if (!actionType.shouldSimulate()) {
					max_add -= add;
				}
			}
		}
		return added;
	}

	@Override
	public long removeFromNetwork(long maxTransferRF, EnergyType energyType, ActionType actionType) {
		long removed = 0;
		for (IFluxTransfer transfer : getTransfers()) {
			if (transfer != null && getNetwork().canConvert(energyType, transfer.getEnergyType()) && transfer instanceof IEnergyTransfer) {
				long toTransfer = getValidRemoval(maxTransferRF - removed);
				long remove = ((IEnergyTransfer)transfer).removeFromNetwork(toTransfer, energyType, actionType);
				removed += remove;
				if (!actionType.shouldSimulate()) {
					max_remove -= remove;
				}
			}
		}
		return removed;
	}

	@Override
	public long getMaxRemove() {
		return flux.getTransferLimit();
	}

	@Override
	public long getMaxAdd() {
		return flux.getTransferLimit();
	}
}
