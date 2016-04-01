package com.andgate.ikou.actor.player;

import com.andgate.ikou.Ikou;
import com.andgate.ikou.command.CommandProcessor
import com.andgate.ikou.graphics.player.PlayerModel
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class PlayerActor(val game: Ikou,
                  val model: PlayerModel)
: Disposable
{
    private val TAG: String = "PlayerActor"
    val cmd_proc = CommandProcessor()
    val pos_observer = PlayerPositionObserver()

    var pos = model.transform.getTranslation(Vector3())
        set(value) {
            model.transform.setTranslation(value);
            pos_observer.notify(value.x - field.x, value.y - field.y, value.z - field.z)
            field.set(value)
        }

    fun update(delta_time: Float)
    {
        cmd_proc.update(delta_time)
    }

    override fun dispose()
    {
        model.dispose()
    }



}
