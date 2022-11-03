package com.gfq.common.helper

/**
 *  2022/11/3 17:57
 * @auth gaofuq
 * @description
 */
object LoadingHelper {
    interface State {
        fun show(msg: String?)
        fun dismiss()
    }

    object View : State {
        override fun show(msg: String?) {
        }

        override fun dismiss() {
        }
    }

    object Dialog : State {
        override fun show(msg: String?) {
        }

        override fun dismiss() {
        }

    }
}