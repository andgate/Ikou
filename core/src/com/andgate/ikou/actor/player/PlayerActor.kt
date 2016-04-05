package com.andgate.ikou.actor.player;

import com.andgate.ikou.actor.Actor
import com.andgate.ikou.actor.Scene
import com.andgate.ikou.actor.messaging.Message
import com.andgate.ikou.actor.player.commands.*
import com.andgate.ikou.actor.player.messages.*
import com.andgate.ikou.animate.Animator
import com.andgate.ikou.graphics.player.PlayerModel
import com.badlogic.gdx.math.Vector3

class PlayerActor(id: String,
                  scene: Scene,
                  val model: PlayerModel)
: Actor(id, scene)
{
    private val TAG: String = "PlayerActor"

    val animator = Animator(model.transform)

    val pos = Vector3()
        get() = model.transform.getTranslation(field)

    init {
        // Bind to events that are coming from the maze
        scene.dispatcher.subscribe("SmoothSlide", channel)
        channel.bind("SmoothSlide", { msg ->
            val msg = msg as SmoothSlideMessage
            if(msg.playerId == id) cmd_proc.accept(SmoothSlideCommand(this, msg.start, msg.end))
        })

        scene.dispatcher.subscribe("StickySlide", channel)
        channel.bind("StickySlide", { msg ->
            val msg = msg as StickySlideMessage
            if(msg.playerId == id) cmd_proc.accept(StickySlideCommand(this, msg.start, msg.end))
        })

        scene.dispatcher.subscribe("DropDown", channel)
        channel.bind("DropDown", { msg ->
            val msg = msg as DropDownMessage
            if(msg.playerId == id) cmd_proc.accept(DropDownCommand(this, msg.start, msg.end))
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

    override fun update(delta_time: Float)
    {
        animator.update(delta_time)
    }

    override fun dispose()
    {
        super.dispose()
        model.dispose()
    }

}
