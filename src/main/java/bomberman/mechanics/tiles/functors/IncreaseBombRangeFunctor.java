package bomberman.mechanics.tiles.functors;

import bomberman.mechanics.Bomberman;
import bomberman.mechanics.WorldEvent;
import bomberman.mechanics.interfaces.EventStashable;
import bomberman.mechanics.interfaces.EventType;

public class IncreaseBombRangeFunctor extends ActionTileAbstractFunctor {

    public IncreaseBombRangeFunctor(EventStashable eventList) {
        super(eventList);
    }

    @Override
    public void applyAction(Bomberman bomberman) {
        bomberman.increaseExplosionRange();
        eventList.addWorldEvent(new WorldEvent(EventType.ENTITY_UPDATED, bomberman.getType(), bomberman.getID()));
        eventList.addWorldEvent(new WorldEvent(EventType.TILE_REMOVED, owner.getType(), owner.getID()));
        owner.markForDestruction();
    }
}