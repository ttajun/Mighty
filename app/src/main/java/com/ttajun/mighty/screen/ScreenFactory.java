package com.ttajun.mighty.screen;

/**
 * Created by ttajun on 2015-04-22.
 */
public class ScreenFactory {
    static ScreenFactory m_screen_factory = null;

    public static final int LOADING_SCREEN = 1;
    public static final int TITLE_SCREEN = 2;
    public static final int GAME_SCREEN = 3;

    ScreenFactory() {

    }

    public static ScreenFactory getInstance() {
        if(null == m_screen_factory) m_screen_factory = new ScreenFactory();
        return m_screen_factory;
    }

    public BaseScreen createScreen(int sCode) {
        BaseScreen screen = null;

        switch (sCode) {
            case LOADING_SCREEN:
                screen = new LoadingScreen();
                break;
            case TITLE_SCREEN:
                screen = new TitleScreen();
                break;
            case GAME_SCREEN:
                screen = new GameScreen();
                break;
        }
        return screen;
    }
}
