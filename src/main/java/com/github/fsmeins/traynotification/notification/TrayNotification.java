package com.github.fsmeins.traynotification.notification;

import com.github.fsmeins.traynotification.animations.*;
import com.github.fsmeins.traynotification.models.CustomStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public final class TrayNotification {

  @FXML
  private Label lblTitle, lblMessage;
  @FXML
  private Button lblClose;
  @FXML
  private ImageView imageIcon;
  @FXML
  private Pane rectangleColor;
  @FXML
  private Pane rootNode;

  private CustomStage stage;
  private NotificationType notificationType;
  private AnimationType animationType;
  private EventHandler<ActionEvent> onDismissedCallBack, onShownCallback;
  private TrayAnimation animator;
  private AnimationProvider animationProvider;

  /**
   * Initializes an instance of the tray notification object
   *
   * @param title         The title text to assign to the tray
   * @param body          The body text to assign to the tray
   * @param img           The image to show on the tray
   * @param rectangleFill The fill for the rectangle
   */
  public TrayNotification(String title, String body, Image img, String rectangleFill) {
    initTrayNotification(title, body, NotificationType.CUSTOM);

    setImage(img);
    setRectangleFill(rectangleFill);
  }

  /**
   * Initializes an instance of the tray notification object
   *
   * @param title            The title text to assign to the tray
   * @param body             The body text to assign to the tray
   * @param notificationType The notification type to assign to the tray
   */
  public TrayNotification(String title, String body, NotificationType notificationType) {
    initTrayNotification(title, body, notificationType);
  }

  /**
   * Initializes an empty instance of the tray notification
   */
  public TrayNotification() {
    initTrayNotification("", "", NotificationType.CUSTOM);
  }

  public NotificationType getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(NotificationType nType) {
    notificationType = nType;
    setRectangleFill(notificationType.getColor());
    if (notificationType != NotificationType.CUSTOM) {
      setImage(new Image(getClass().getResource(notificationType.getImagePath()).toString()));
      setTrayIcon(imageIcon.getImage());
    }
  }

  public void setTray(String title, String message, NotificationType type) {
    setTitle(title);
    setMessage(message);
    setNotificationType(type);
  }

  public void setTray(String title, String message, Image img, String rectangleFill, AnimationType animType) {
    setTitle(title);
    setMessage(message);
    setImage(img);
    setRectangleFill(rectangleFill);
    setAnimationType(animType);
  }

  public boolean isTrayShowing() {
    return animator.isShowing();
  }

  /**
   * Shows and dismisses the tray notification
   *
   * @param dismissDelay How long to delay the start of the dismiss animation
   */
  public void showAndDismiss(Duration dismissDelay) {

    if (isTrayShowing()) {
      dismiss();
    } else {
      stage.show();

      onShown();
      animator.playSequential(dismissDelay);
    }

    onDismissed();
  }

  /**
   * Displays the notification tray
   */
  public void showAndWait() {

    if (!isTrayShowing()) {
      stage.show();

      animator.playShowAnimation();

      onShown();
    }
  }

  /**
   * Dismisses the notifcation tray
   */
  public void dismiss() {

    if (isTrayShowing()) {
      animator.playDismissAnimation();
      onDismissed();
    }
  }

  /**
   * Sets an action event for when the tray has been dismissed
   *
   * @param event The event to occur when the tray has been dismissed
   */
  public void setOnDismiss(EventHandler<ActionEvent> event) {
    onDismissedCallBack = event;
  }

  /**
   * Sets an action event for when the tray has been shown
   *
   * @param event The event to occur after the tray has been shown
   */
  public void setOnShown(EventHandler<ActionEvent> event) {
    onShownCallback = event;
  }

  public Image getTrayIcon() {
    return stage.getIcons().get(0);
  }

  /**
   * Sets a new task bar image for the tray
   *
   * @param img The image to assign
   */
  public void setTrayIcon(Image img) {
    stage.getIcons().clear();
    stage.getIcons().add(img);
  }

  public String getTitle() {
    return lblTitle.getText();
  }

  /**
   * Sets a title to the tray
   *
   * @param txt The text to assign to the tray icon
   */
  public void setTitle(String txt) {
    lblTitle.setText(txt);
  }

  public String getMessage() {
    return lblMessage.getText();
  }

  /**
   * Sets the message for the tray notification
   *
   * @param txt The text to assign to the body of the tray notification
   */
  public void setMessage(String txt) {
    lblMessage.setText(txt);
  }

  public Image getImage() {
    return imageIcon.getImage();
  }

  public void setImage(Image img) {
    imageIcon.setImage(img);

    setTrayIcon(img);
  }

  public void setRectangleFill(final String color) {
    rectangleColor.setStyle("-fx-background-color: " + color + ';');
  }

  public AnimationType getAnimationType() {
    return animationType;
  }

  public void setAnimationType(AnimationType type) {
    animator = animationProvider.findFirstWhere(a -> a.getAnimationType() == type);

    animationType = type;
  }

  private void initTrayNotification(String title, String message, NotificationType type) {

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/TrayNotification.fxml"));

      fxmlLoader.setController(this);
      fxmlLoader.load();

      initStage();
      initAnimations();

      setTray(title, message, type);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initAnimations() {

    animationProvider =
        new AnimationProvider(new FadeAnimation(stage), new SlideAnimation(stage), new PopupAnimation(stage), new NoneAnimation(stage));

    //Default animation type
    setAnimationType(AnimationType.SLIDE);
  }

  private void initStage() {

    stage = new CustomStage(rootNode, StageStyle.UNDECORATED);
    stage.setScene(new Scene(rootNode));
    stage.setAlwaysOnTop(true);
    stage.setLocation(stage.getBottomRight());
    stage.getScene().getStylesheets().add("/css/style.css");

    lblClose.setOnMouseClicked(e -> dismiss());
  }

  private void onShown() {
    if (onShownCallback != null)
      onShownCallback.handle(new ActionEvent());
  }

  private void onDismissed() {
    if (onDismissedCallBack != null)
      onDismissedCallBack.handle(new ActionEvent());
  }
}