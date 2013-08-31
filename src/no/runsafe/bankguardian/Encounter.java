package no.runsafe.bankguardian;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerTeleport;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.RunsafeWorld;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

public class Encounter implements IConfigurationChanged, IPlayerTeleport
{
	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		spawnPoint = configuration.getConfigValueAsLocation("spawnPoint");

	}

	@Override
	public boolean OnPlayerTeleport(RunsafePlayer player, RunsafeLocation from, RunsafeLocation to)
	{
		RunsafeWorld spawnWorld = spawnPoint.getWorld();
		RunsafeWorld destinationWorld = to.getWorld();
		RunsafeWorld sourceWorld = from.getWorld();

		if (destinationWorld.isWorld(spawnWorld) && !destinationWorld.isWorld(sourceWorld))
		{
			player.sendColouredMessage("There are %d players in the destination world", destinationWorld.getPlayers().size());
		}
		else if (sourceWorld.isWorld(spawnWorld) && !sourceWorld.isWorld(destinationWorld))
		{
			player.sendColouredMessage("Leaving world, %d players.", sourceWorld.getPlayers().size());
		}
		return true;
	}

	private RunsafeLocation spawnPoint;
}
