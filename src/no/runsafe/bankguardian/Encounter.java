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
		if (destinationWorld.isWorld(spawnWorld) && !destinationWorld.isWorld(from.getWorld()))
		{
			player.sendColouredMessage("Here you would trigger a world check.");
			player.sendColouredMessage("There are %d players in the destination world", destinationWorld.getPlayers().size());
		}
		return true;
	}

	private RunsafeLocation spawnPoint;
}
