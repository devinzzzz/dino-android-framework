/*
 * Copyright 2018 The NetCircle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thenetcircle.dinoandroidframework.activity

import android.os.Bundle
import android.widget.Toast
import com.thenetcircle.dino.DinoError
import com.thenetcircle.dino.interfaces.DinoErrorListener
import com.thenetcircle.dino.interfaces.DinoJoinRoomListener
import com.thenetcircle.dino.interfaces.DinoMessageReceivedListener
import com.thenetcircle.dino.model.data.ChatSendMessage
import com.thenetcircle.dino.model.data.JoinRoomModel
import com.thenetcircle.dino.model.data.LeaveRoomModel
import com.thenetcircle.dino.model.results.JoinRoomResultModel
import com.thenetcircle.dino.model.results.MessageReceived
import com.thenetcircle.dinoandroidframework.fragment.TNCChatRoomFragment

/**
 * Created by aaron on 16/01/2018.
 */
class TNCChatRoomActivity : TNCBaseActivity(), DinoMessageReceivedListener, DinoErrorListener, TNCChatRoomFragment.ChatRoomListener {
    private val chatRoomFragment = TNCChatRoomFragment()

    companion object {
        val ROOM_ID: String = "ROOM_ID"
    }

    private var roomID: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTrans(chatRoomFragment)
        //chatRoomFragment.room = intent.extras.get(ROOM_EXTRA) as JoinRoomResultModel
        roomID = intent.extras.get(ROOM_ID) as String?
    }

    override fun onResume() {
        super.onResume()
        dinoChatConnection.joinRoom(JoinRoomModel(roomID!!), object : DinoJoinRoomListener {
            override fun onResult(result: JoinRoomResultModel) {
                chatRoomFragment.room = result
            }
        }, this)

    }

    override fun onPause() {
        super.onPause()
        dinoChatConnection.leaveRoom(LeaveRoomModel(roomID!!), this)
    }

    override fun sendMessage(message: String) {
        dinoChatConnection.sendMessage(ChatSendMessage(roomID!!, message), this)
    }

    override fun onResult(result: MessageReceived) {
        chatRoomFragment.displayMessage(result)


    }
    override fun onError(error: DinoError) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
    }
}