package com.github.fsmeins.traynotification.animations;

import com.github.fsmeins.traynotification.models.CustomStage;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class NoneAnimation implements TrayAnimation {
    private final Timeline showAnimation, dismissAnimation;
    private final SequentialTransition sq;
    private final CustomStage stage;
    private boolean trayIsShowing;

    public NoneAnimation(CustomStage s) {
        this.stage = s;
        showAnimation = setupShowAnimation();
        dismissAnimation = setupDismissAnimation();
        sq = new SequentialTransition(setupShowAnimation(), setupDismissAnimation());
    }

    private Timeline setupShowAnimation() {
        final Timeline tl = new Timeline();
        tl.setOnFinished(e -> trayIsShowing = true);

        return tl;
    }

    private Timeline setupDismissAnimation() {
        final Timeline tl = new Timeline();
        KeyFrame frame1 = new KeyFrame(Duration.ZERO);
        KeyFrame frame2 = new KeyFrame(Duration.millis(1));
        tl.getKeyFrames().addAll(frame1, frame2);
        tl.setOnFinished(e ->
        {
            trayIsShowing = false;
            stage.close();
            stage.setLocation(stage.getBottomRight());
        });

        return tl;
    }

    @Override
    public AnimationType getAnimationType() {
        return AnimationType.NONE;
    }

    @Override
    public void playSequential(Duration dismissDelay) {
        sq.getChildren().get(1).setDelay(dismissDelay);
        sq.play();
    }

    @Override
    public void playShowAnimation() {
        showAnimation.play();
    }

    @Override
    public void playDismissAnimation() {
        dismissAnimation.play();
    }

    @Override
    public boolean isShowing() {
        return trayIsShowing;
    }
}
