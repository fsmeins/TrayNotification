import com.github.fsmeins.traynotification.animations.AnimationType;
import com.github.fsmeins.traynotification.notification.NotificationType;
import com.github.fsmeins.traynotification.notification.TrayNotification;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Demo extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setWidth(200);
        primaryStage.setHeight(200);
        primaryStage.centerOnScreen();
        primaryStage.show();

        TrayNotification trayNotification = new TrayNotification();
        trayNotification.setTitle("Test");
        trayNotification.setAnimationType(AnimationType.NONE);
        trayNotification.setMessage("Test Message");
        trayNotification.setNotificationType(NotificationType.ERROR);
        trayNotification.showAndDismiss(Duration.seconds(3));
    }
}