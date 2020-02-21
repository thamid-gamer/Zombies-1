package net.zswca.zombie.objects.guns.logics;

import net.zswca.zombie.data.BulletStats;
import net.zswca.zombie.data.GunData;
import net.zswca.zombie.objects.User;
import net.zswca.zombie.objects.guns.Gun;
import net.zswca.zombie.objects.guns.logics.LinearBeam;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class LinearGun extends Gun {

    protected final int maxHitEntities;
    protected final Particle particle;

    /**
     * Creates a gun like the Gun superclass
     */
    LinearGun(GunData data, User user) {
        super(data, user);
        this.maxHitEntities = Integer.parseInt(this.gunStats.getFeatureInfo("maxHitEntities"));
        this.particle = Particle.valueOf(this.gunStats.getFeatureInfo("particle"));
    }

    public void shoot(){
        Player player = this.gunOwner.getPlayer();
        World world = player.getWorld();
        Vector eyeLocation = player.getEyeLocation().toVector().clone();
        Vector eyeDirection = player.getEyeLocation().getDirection().clone();
        Vector targetBlockVector = getTargetBlockVector(player, eyeLocation, eyeDirection);

        sendShot(world, eyeLocation, eyeDirection, targetBlockVector);
    }

    public void reload() {
        // TODO: Reload

    }

    private void sendShot(World world, Vector particleLocation, Vector particleDirection, Vector targetBlockVector) {
        LinearBeam beam = new LinearBeam(world, particle, particleLocation, particleDirection, targetBlockVector, maxHitEntities);
        beam.send();
    }

    private Vector getTargetBlockVector(Player player, Vector eyeLocation, Vector eyeDirection) {
        Set<Material> materials = new HashSet<>();

        materials.add(Material.AIR);
        materials.add(Material.CAVE_AIR);

        // TODO: Add slab blocks
        int range = (int) Math.ceil(this.getCurrentStats().baseRange);
        BoundingBox targetedBlockBoundingBox = player.getTargetBlock(materials, range).getBoundingBox();

        return targetedBlockBoundingBox.rayTrace(eyeLocation, eyeDirection, range).getHitPosition();
    }
}
