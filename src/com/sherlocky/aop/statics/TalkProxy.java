package com.sherlocky.aop.statics;

import com.sherlocky.aop.ITalk;

public class TalkProxy implements ITalk {

    private ITalk talker;

    public TalkProxy(ITalk talker) {
        super();
        this.talker = talker;
    }

    @Override
    public void talk(String msg) {
        talker.talk(msg);
    }

}
