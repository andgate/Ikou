package com.andgate.ikou.actor.player;

import com.andgate.ikou.actor.Actor
import com.andgate.ikou.actor.Scene
import com.andgate.ikou.actor.messaging.Message
import com.andgate.ikou.actor.player.commands.*
import com.andgate.ikou.actor.player.messages.*
import com.andgate.ikou.graphics.player.PlayerModel
import com.badlogic.gdx.math.Vector3

class PlayerActor(id: String,
                  scene: Scene,
                  val model: PlayerModel)
: Actor(id, scene)
{
    private val TAG: String = "PlayerActor"

    var pos = model.transform.getTranslation(Vector3())
        set(value) {
            model.transform.setTranslation(value)
            scene.dispatcher.push(PlayerPositionChangeMessage(id, value.x - field.x, value.y - field.y, value.z - field.z))
            field.set(value)
        }

    init {
        // Don't read player events, because player movement commands need to be synchronous
        scene.dispatcher.subscribe("SmoothSlide", channel)
        channel.bind("SmoothSlide", { msg ->
            val msg = msg as SmoothSlideMessage
            if(msg.playerId == id) cmd_proc.accept(SmoothSlideCommand(this, msg.end_pos))
        })

        scene.dispatcher.subscribe("StickySlide", channel)
        channel.bind("StickySlide", { msg ->
            val msg = msg as StickySlideMessage
            if(msg.playerId == id) cmd_proc.accept(StickySlideCommand(this, msg.end_pos))
        })

        scene.dispatcher.subscribe("DropDown", channel)
        channel.bind("DropDown", { msg ->
            val msg = msg as DropDownMessage
            if(msg.playerId == id) cmd_proc.accept(DropDownCommand(this))
        })

        scene.dispatcher.subscribe("HitEdge", channel)
        channel.bind("HitEdge", { msg ->
            val msg = msg as HitEdgeMessage
            if(msg.playerId == id) cmd_proc.accept(HitEdgeCommand(this))
        })

        scene.dispatcher.subscribe("FinishGame", channel)
        channel.bind("FinishGame", { msg ->
            val msg = msg as FinishGameMessage
            if(msg.playerId == id) cmd_proc.accept(FinishGameCommand(this))
        })
    }

    override fun receive(event: Message)
    {
        // Actor bound to all the relevant events,
        // no events that need to be received asynchronously
    }

    override fun dispose()
    {
        super.dispose()
        model.dispose()
    }



}
