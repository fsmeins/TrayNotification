package com.github.fsmeins.traynotification.notification;

public enum NotificationType {
  INFORMATION("/images/info.png", "#2C54AB"),
  NOTICE("/images/notice.png", "#8D9695"),
  SUCCESS("/images/success.png", "#009961"),
  WARNING("/images/warning.png", "#E23E0A"),
  ERROR("/images/error.png", "#CC0033"),
  CUSTOM(null, null);

  private String imagePath;
  private String color;

  NotificationType(String imagePath, String color) {
    this.imagePath = imagePath;
    this.color = color;
  }

  public String getImagePath() {
    return imagePath;
  }

  public String getColor() {
    return color;
  }
}
