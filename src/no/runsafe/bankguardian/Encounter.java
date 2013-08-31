package no.runsafe.bankguardian;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.player.IPlayerTeleport;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.RunsafeWorld;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import org.bukkit.entity.EntityType;

public class Encounter implements IConfigurationChanged, IPlayerTeleport
{
	public Encounter(IOutput output)
	{
		registry = CitizensAPI.getNPCRegistry(); // Citizens NPC handler.
		loaded = false;
		console = output;
	}

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

		if (!loaded && destinationWorld.isWorld(spawnWorld) && !destinationWorld.isWorld(sourceWorld))
		{
			// We are teleporting to the world with the encounter and it's not loaded, load it.
			loadEncounter();
		}
		else if (loaded && sourceWorld.isWorld(spawnWorld) && !sourceWorld.isWorld(destinationWorld) && sourceWorld.getPlayers().size() == 1)
		{
			// We are teleporting away from the world with the encounter, it's loaded, and we're the last player.
			unloadEncounter();
		}
		return true;
	}

	private void loadEncounter()
	{
		console.fine("Loading encounter");
		boss = registry.createNPC(EntityType.PLAYER,  "Astalor"); // Create an NPC for the boss.
		boss.spawn(spawnPoint.getRaw()); // Spawn it in the world at the position stored.
	}

	private void unloadEncounter()
	{
		console.fine("Unloading encounter");
		if (boss != null) // Check we actually have a boss.
			boss.despawn(); // Respawn it.
	}

	private RunsafeLocation spawnPoint;
	private NPCRegistry registry;
	private NPC boss;
	private boolean loaded;
	private IOutput console;
}
